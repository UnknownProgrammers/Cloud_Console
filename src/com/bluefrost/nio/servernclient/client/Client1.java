package com.bluefrost.nio.servernclient.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.bluefrost.nio.servernclient.events.ClientDisconnectEvent;
import com.bluefrost.nio.servernclient.main.Main;

import bluefrost.serializable.objects.v1.LoginObject;

public class Client1 implements Runnable{

	SocketChannel client;
	public void run(){
		try{
			// Create client SocketChannel
			client = SocketChannel.open();
			
			// nonblocking I/O
			client.configureBlocking(false);

			// Connection to host port 8000
			client.connect(new java.net.InetSocketAddress("localhost",9090));
		
			// Create selector
			Selector selector = Selector.open();

			// Record to selector (OP_CONNECT type)
			SelectionKey clientKey = client.register(selector, SelectionKey.OP_CONNECT);

			// Waiting for the connection
			while (selector.select(500)> 0) {

				// Get keys
				Set keys = selector.selectedKeys();
				Iterator i = keys.iterator();

				// For each key...
				while (i.hasNext()) {
					SelectionKey key = (SelectionKey)i.next();

					// Remove the current key
					i.remove();

					// Get the socket channel held by the key
					SocketChannel channel = (SocketChannel)key.channel();

					// Attempt a connection
					if (key.isConnectable()) {

						// Connection OK
						System.out.println("Server Found");
						
						// Close pendent connections
						if (channel.isConnectionPending())
							channel.finishConnect();

						// Write continuously on the buffer
						send(new LoginObject("root","toor").toByteArray());

					}
					if(key.isReadable()){
						read(key);
					}
				}
			}
		}catch(Exception e){}
	}
	

	private ByteBuffer readBuffer = ByteBuffer.allocate(8192);
	
	public void read(SelectionKey key) throws Exception{
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

		System.out.println("ServerSaid: " + this.readBuffer.array());
		socketChannel.write(ByteBuffer.wrap(this.readBuffer.array()));
		//this.worker.processData(this, socketChannel, this.readBuffer.array(), numRead);
	}
	
	
	public void send(byte[] b){
		try{
			client.write(ByteBuffer.wrap(b));
		}catch(Exception e){}
	}
}
