package com.desparddesign.orienteering.timer.PTB605v13.transport;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TelnetSerialManager extends Transport
{
	private ServerSocket serverSocket = null;
	private Socket socket = null;
		
	@Override
	public void connect(String device, int port) throws Exception
    {
		if(device == null)
		{
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
		}
		else
		{
			socket = new Socket();
			socket.connect(new InetSocketAddress(device,port));
			socket.setSoTimeout(1000);
		}
		
		InputStream in = socket.getInputStream();
		out = socket.getOutputStream();
		
		reader = new Reader(in,this);
        writer = new Writer(out,this);
        readerThread = new Thread(reader);
        readerThread.start();
        
        writerThread = new Thread(writer);
        writerThread.start();
	}
	
	public void disconnect() throws IOException
	{
		if(socket != null)
		{
			try
			{
				readerThread.interrupt();
				writerThread.interrupt();
				reader.stop();
				writer.stop();
				readerThread.join();
				writerThread.join();
			}
			catch(Exception e)
			{
			}
			
			socket.close();
			socket = null;

		}
		
		if(serverSocket!= null)
		{
			serverSocket.close();
			serverSocket = null;
		}
	}
}
