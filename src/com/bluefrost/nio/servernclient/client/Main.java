package com.bluefrost.nio.servernclient.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
 
 
public class Main {
 
    /**
     * @param args
     */
    public static void main(String[] args) {
        NIOC test1 = new NIOC("");
        Thread thread = new Thread(test1);
        thread.start();
        //thread2.start();
    }
 
    static class NIOC implements Runnable {
 
    	public Worker worker = new Worker();
        private String message = "";
        private Selector selector;
 
 
        public NIOC(String message){
        	
        }
 
        @Override
        public void run() {
        	new Thread(worker).start();
            SocketChannel channel;
            try {
                selector = Selector.open();
                channel = SocketChannel.open();
                channel.configureBlocking(false);
 
                channel.register(selector, SelectionKey.OP_CONNECT);
                channel.connect(new InetSocketAddress("127.0.0.1", 9090));
 
                while (!Thread.interrupted()){
 
                    selector.select(1000);
                     
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
 
                    while (keys.hasNext()){
                        SelectionKey key = keys.next();
                        keys.remove();
 
                        if (!key.isValid()) continue;
 
                        if (key.isConnectable()){
                            System.out.println("I am connected to the server");
                            connect(key);
                        }   
                        if (key.isWritable()){
                            write(key);
                        }
                        if (key.isReadable()){
                            read(key);
                        }
                    }   
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } finally {
                close();
            }
        }
         
        private void close(){
            try {
                selector.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
 
        private void read (SelectionKey key) throws IOException {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer readBuffer = ByteBuffer.allocate(1000);
            readBuffer.clear();
            int length;
            try{
            length = channel.read(readBuffer);
            } catch (IOException e){
                System.out.println("Reading problem, closing connection");
                key.cancel();
                channel.close();
                return;
            }
            if (length == -1){
                System.out.println("Nothing was read from server");
                channel.close();
                key.cancel();
                return;
            }
            readBuffer.flip();
            byte[] buff = new byte[1024];
            readBuffer.get(buff, 0, length);
            System.out.println("Server said: "+new String(buff));
        }
 
        private void write(SelectionKey key) throws IOException {
            SocketChannel channel = (SocketChannel) key.channel();
            channel.write(ByteBuffer.wrap(message.getBytes()));
 
            // lets get ready to read.
            key.interestOps(SelectionKey.OP_READ);
        }
 
        private void connect(SelectionKey key) throws IOException {
            SocketChannel channel = (SocketChannel) key.channel();
            if (channel.isConnectionPending()){
                channel.finishConnect();
            }
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_WRITE);
        }
    }
    
    

	public static class Worker implements Runnable{

		public List<ServerDataEvent> queue = new LinkedList<ServerDataEvent>();

		public void processData(NIOC server, SocketChannel socket, byte[] data, int count) {
			byte[] dataCopy = new byte[count];
			System.arraycopy(data, 0, dataCopy, 0, count);
			synchronized(queue) {
				queue.add(new ServerDataEvent(server, socket, dataCopy));
				queue.notify();
			}
		}

		public void run() {
			ServerDataEvent dataEvent;

			
			while(!Thread.interrupted()) {
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
				//Main.getEventSystem().listen(new MessageEvent(dataEvent.data,dataEvent.socket));
				//dataEvent.server.send(dataEvent.socket, dataEvent.data);
			}
			System.out.println("Ended! Worker");
		}

		public static class ServerDataEvent{
			public NIOC server;
			public SocketChannel socket;
			public byte[] data;

			public ServerDataEvent(NIOC server, SocketChannel socket, byte[] data) {
				this.server = server;
				this.socket = socket;
				this.data = data;
			}

		}
	}

}