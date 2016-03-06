package com.desparddesign.orienteering.timer.PTB605v13.transport;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605v13Packetizer;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;
import com.desparddesign.orienteering.timer.PTB605v13.commands.linkCommands.LinkOff;
import com.desparddesign.orienteering.timer.PTB605v13.commands.linkCommands.LinkToPrinter;

public abstract class Transport 
{
	public Reader reader;
	public Writer writer;
	
	protected Thread readerThread;
	protected Thread writerThread;
	
	protected OutputStream out = null;
	
	public boolean NAK = false;
	
	public abstract void connect(String device, int speed) throws Exception;
	public abstract void disconnect() throws IOException;
	
	public void printString(String message) throws Exception
	{
		do
		{
			writer.packetizer.dePacketize((new LinkToPrinter()).rawPacket());
			Thread.sleep(1000);
		}while(NAK);
		
		out.write((byte)0x18);
		Thread.sleep(20);
		
		out.write(message.getBytes());
		out.write((byte)0x0D);
		
		Thread.sleep(10);
		
		writer.packetizer.dePacketize((new LinkOff()).rawPacket());
		Thread.sleep(10);
	}
		
	public static class Reader implements Runnable 
	{		
        InputStream in;
        PTB605v13Packetizer packetizer;       
        boolean requestStop = false;
        
        public Reader ( InputStream in, Transport t)
        {
            this.in = in;
            packetizer = new PTB605v13Packetizer(t);
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            
            try
            {
            	while(true)
            	{
            		try
            		{
            			len = 0;
            			len = this.in.read(buffer);
            		}
            		catch(SocketTimeoutException ex)
            		{
            			//read timed out, just go back through the loop
            		}
            		
            		packetizer.packetize(buffer, len);
                	if(requestStop)
                	{
                		requestStop = false;
                		in.close();
                		break;
                	}
            	}
            }
            catch ( InterruptedException e )
            {
                //exit thread
            }     
            catch(IOException e)
            {
            	e.printStackTrace();
            }            
        }
        
        public void stop()
        {
        	requestStop = true;
        }
        
        public LinkedBlockingQueue<PTB605Command> recievePacketQueue()
        {
        	return packetizer.recieveQueue;
        }
	}
	public static class Writer implements Runnable 
	{
        OutputStream out;
        public PTB605v13Packetizer packetizer;   
        boolean requestStop = false;
        
        public Writer ( OutputStream out, Transport t)
        {
            this.out = out;
            packetizer = new PTB605v13Packetizer(t);
        }
        
        public void run ()
        {
            try
            {                 
                while(true)
                {
                	byte[] data = packetizer.sendQueue.poll(1000, TimeUnit.MILLISECONDS);
                	if(requestStop)
            		{
            			requestStop = false;
            			out.close();
            			break;
            		}  
                	if(data != null)
                	{		
                		this.out.write(data);      		   
                	}
                }
            }
            catch ( InterruptedException e )
            {
                //exit thread
            }     
            catch(IOException e)
            {
            	e.printStackTrace();
            }
        }
        
        public void stop()
        {
        	requestStop = true;
        }
        
        public LinkedBlockingQueue<byte[]> sendPacketQueue()
        {
        	return packetizer.sendQueue;
        }
	}
}
