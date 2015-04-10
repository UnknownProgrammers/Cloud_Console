package com.bluefrost.nio.servernclient.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.bluefrost.nio.servernclient.listeners.ConnectionListener.ConnectionEvent;
import com.bluefrost.nio.servernclient.listeners.DisconnectListener.ClientDisconnectEvent;
import com.bluefrost.nio.servernclient.listeners.MessageListener.MessageEvent;
import com.bluefrost.nio.servernclient.main.Main;
// The host:port combination to listen on
// The channel on which we'll accept connections
// The selector we'll be monitoring
// The buffer into which we'll read data when it's available
// Create a new selector
// Create a new non-blocking server socket channel
// Bind the server socket to the specified address and port
// Register the server socket channel, indicating an interest in 
// accepting new connections
// Wait for an event one of the registered channels
// Iterate over the set of keys for which events are available
// Check what event is available and deal with it
// Clear out our read buffer so it's ready for new data
// Attempt to read off the channel
// The remote forcibly closed the connection, cancel
// the selection key and close the channel.
// Remote entity shut the socket down cleanly. Do the
// same from our end and cancel the channel.
// Hand the data off to our worker thread
// For an accept to be pending the channel must be a server socket channel.
// Accept the connection and make it non-blocking
// Register the new SocketChannel with our Selector, indicating
// we'd like to be notified when there's data waiting to be read
import com.bluefrost.nio.servernclient.useraccess.ClientManager;
import com.bluefrost.nio.servernclient.useraccess.ClientManager.Client;

/*
 * Created by:
 * Blue/CyanFrost 
 * Ellie/Eleanor <3
 * 
 * Date: some time in October.
 */

public class NIOS implements Runnable{

	// The host:port combination to listen on
	private InetAddress hostAddress;
	private int port;

	// The channel on which we'll accept connections
	public ServerSocketChannel serverChannel;

	// The selector we'll be monitoring
	private Selector selector;

	// The buffer into which we'll read data when it's available
	private ByteBuffer readBuffer = ByteBuffer.allocate(8192);

	private Worker worker;

	// A list of PendingChange instances
	private List<ChangeRequest> pendingChanges = new LinkedList<ChangeRequest>();

	// Maps a SocketChannel to a list of ByteBuffer instances
	private Map<SocketChannel, List<ByteBuffer>> pendingData = new HashMap<SocketChannel, List<ByteBuffer>>();
	
	
	public NIOS(InetAddress hostAddress, int port, Worker worker) throws IOException {
		this.hostAddress = hostAddress;
		this.port = port;
		this.selector = this.initSelector();
		this.worker = worker;
	}


	public void send(SocketChannel socket, byte[] data) {
		synchronized (this.pendingChanges) {
			// Indicate we want the interest ops set changed
			this.pendingChanges.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

			// And queue the data we want written
			synchronized (this.pendingData) {
				List<ByteBuffer> queue = (List<ByteBuffer>) this.pendingData.get(socket);
				if (queue == null) {
					queue = new ArrayList<ByteBuffer>();
					this.pendingData.put(socket, queue);
				}

				queue.add(ByteBuffer.wrap(data));
			}
		}

		// Finally, wake up our selecting thread so it can make the required changes
		this.selector.wakeup();
	}

	public static boolean alive = true;

	public void run() {
		while (alive) {
			try {
				// Process any pending changes
				synchronized (this.pendingChanges) {
					Iterator<?> changes = this.pendingChanges.iterator();
					while (changes.hasNext()) {
						ChangeRequest change = (ChangeRequest) changes.next();
						switch (change.type) {
						case ChangeRequest.CHANGEOPS:
							SelectionKey key = change.socket.keyFor(this.selector);
							key.interestOps(change.ops);
						}
					}
					this.pendingChanges.clear();
				}

				// Wait for an event one of the registered channels
				this.selector.select();

				// Iterate over the set of keys for which events are available
				Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					// Check what event is available and deal with it
					if (key.isAcceptable()) {
						this.accept(key);
					} else if (key.isReadable()) {
						this.read(key);
					} else if (key.isWritable()) {
						this.write(key);
					}
				}
			} catch (Exception e) {
				if(e instanceof ClosedSelectorException){
					System.out.println("Expected ClosedSelectorException was thrown!");
				}else{
					e.printStackTrace();
				}
			}
		}
		System.out.println("Ended!");
	}

	private void accept(SelectionKey key) throws IOException {
		// For an accept to be pending the channel must be a server socket channel.
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

		// Accept the connection and make it non-blocking
		SocketChannel socketChannel = serverSocketChannel.accept();

		ConnectionEvent event = new ConnectionEvent(socketChannel);

		Main.getEventSystem().listen(event);
		if(event.isCanceled()){
			try{
				
				//key.cancel();
				socketChannel.close();
				return;
			}catch(Exception e){}
		}
		

		socketChannel.configureBlocking(false);

		// Register the new SocketChannel with our Selector, indicating
		// we'd like to be notified when there's data waiting to be read
		socketChannel.register(this.selector, SelectionKey.OP_READ);
		
		ClientManager.store(new Client(), socketChannel);
	}


	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Clear out our read buffer so it's ready for new data
		this.readBuffer.clear();

		// Attempt to read off the channel
		int numRead;
		try {
			numRead = socketChannel.read(this.readBuffer);
		} catch (IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel.
			Main.getEventSystem().listen(new ClientDisconnectEvent(socketChannel));
			key.cancel();
			socketChannel.close();
			return;
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.

			Main.getEventSystem().listen(new ClientDisconnectEvent(socketChannel));
			key.channel().close();
			key.cancel();
			return;
		}

		// Hand the data off to our worker thread
		this.worker.processData(this, socketChannel, this.readBuffer.array(), numRead);
	}

	@Deprecated public void terminateAllClients(){
		try{

		}catch(Exception e){}
	}

	private void write(SelectionKey key) throws IOException {
		try{
		SocketChannel socketChannel = (SocketChannel) key.channel();

		synchronized (this.pendingData) {
			List<ByteBuffer> queue = (List<ByteBuffer>) this.pendingData.get(socketChannel);
			

			// Write until there's not more data ...
			while (!queue.isEmpty()) {
				ByteBuffer buf = (ByteBuffer) queue.get(0);
				socketChannel.write(buf);
				if (buf.remaining() > 0) {
					// ... or the socket's buffer fills up
					break;
				}
				queue.remove(0);
			}

			if (queue.isEmpty()) {
				// We wrote away all data, so we're no longer interested
				// in writing on this socket. Switch back to waiting for
				// data.
				key.interestOps(SelectionKey.OP_READ);
			}
		}
		}catch(Exception e){e.printStackTrace();}
	}

	private Selector initSelector() throws IOException {
		// Create a new selector
		Selector socketSelector = SelectorProvider.provider().openSelector();

		// Create a new non-blocking server socket channel
		this.serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);

		// Bind the server socket to the specified address and port
		InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
		serverChannel.socket().bind(isa);

		// Register the server socket channel, indicating an interest in 
		// accepting new connections
		serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

		return socketSelector;
	}

	public class ChangeRequest {
		public static final int REGISTER = 1;
		public static final int CHANGEOPS = 2;

		public SocketChannel socket;
		public int type;
		public int ops;

		public ChangeRequest(SocketChannel socket, int type, int ops) {
			this.socket = socket;
			this.type = type;
			this.ops = ops;
		}
	}

	public void end() {
		try{
			alive = false;
			synchronized(worker.queue){
				worker.queue.add(null);
				worker.queue.notify();
			}
			serverChannel.close();
			selector.close();
		}catch(Exception e){}
	}


	public static class SecondaryWorker implements Runnable{
		public ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<Object>();
		
		public void run(){
			
		}
	}
	
	public static class Worker implements Runnable{

		public List<ServerDataEvent> queue = new LinkedList<ServerDataEvent>();

		public void processData(NIOS server, SocketChannel socket, byte[] data, int count) {
			byte[] dataCopy = new byte[count];
			System.arraycopy(data, 0, dataCopy, 0, count);
			synchronized(queue) {
				queue.add(new ServerDataEvent(server, socket, dataCopy));
				queue.notify();
			}
		}

		public void run() {
			ServerDataEvent dataEvent;


			while(alive) {
				// Wait for data to become available
				synchronized(queue) {
					while(queue.isEmpty()) {
						try {
							queue.wait();
						} catch (InterruptedException e) {}
					}
					dataEvent = (ServerDataEvent) queue.remove(0);
				}
				if(dataEvent == null)break;
				Main.getEventSystem().listen(new MessageEvent(dataEvent.data,dataEvent.socket));
				//dataEvent.server.send(dataEvent.socket, dataEvent.data);
			}
			System.out.println("Ended! Worker");
		}

		public static class ServerDataEvent{
			public NIOS server;
			public SocketChannel socket;
			public byte[] data;

			public ServerDataEvent(NIOS server, SocketChannel socket, byte[] data) {
				this.server = server;
				this.socket = socket;
				this.data = data;
			}

		}
	}




}
