package com.desparddesign.orienteering.codechecking.sportident.transport;

import java.io.*;
import java.net.*;


public class TelnetSerialManager extends Transport 
{	
	private Socket socket = null;
	
	private Integer port;
	
	@Override
	public void connect(String device, Integer num, Boolean fast) throws Exception 
	{
		this.port = num;
		socket = new Socket();
		socket.connect(new InetSocketAddress(device,port));
		socket.setSoTimeout(1000);
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		
		reader = new Reader(in);
        writer = new Writer(out);
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
	}
}
