package com.desparddesign.orienteering.codechecking.sportident.transport;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.concurrent.LinkedBlockingQueue;

import com.desparddesign.orienteering.codechecking.sportident.SIPacketizer;
import com.desparddesign.orienteering.codechecking.sportident.commands.SICommand;

public abstract class Transport 
{
	public Reader reader;
	public Writer writer;
	
	protected Thread readerThread;
	protected Thread writerThread;
	
	public abstract void connect(String device, Integer num, Boolean fast) throws Exception;
	
	public abstract void disconnect() throws IOException;
	
	public static class Reader implements Runnable 
	{		
        InputStream in;
        SIPacketizer packetizer;       
        boolean requestStop = false;
        
        public Reader ( InputStream in )
        {
            this.in = in;
            packetizer = new SIPacketizer();
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
            			//read timeed out, just go back through the loop
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
            catch (InterruptedException e)
            {
            	//exit thread
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }            
        }
        
        public void stop()
        {
        	requestStop = true;
        }
        
        public LinkedBlockingQueue<SICommand> recievePacketQueue()
        {
        	return packetizer.recieveQueue;
        }
	}
	public static class Writer implements Runnable 
	{
        OutputStream out;
        public SIPacketizer packetizer;   
        boolean requestStop = false;
        
        public Writer ( OutputStream out )
        {
            this.out = out;
            packetizer = new SIPacketizer();
        }
        
        public void run ()
        {
            try
            {                 
                while(true)
                {
                	byte[] data = packetizer.sendQueue.take();
                	this.out.write(data);
                	if(requestStop)
                	{
                		requestStop = false;
                		out.close();
                		break;
                	}      	
                }
            }
            catch (InterruptedException e)
            {
            	//exit thread
            }
            catch ( Exception e )
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
