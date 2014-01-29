package com.desparddesign.orienteering.timer.PTB605.transport;

import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;

import com.desparddesign.orienteering.timer.PTB605.PTB605Packetizer;
import com.desparddesign.orienteering.timer.PTB605.commands.PTB605Command;

public abstract class Transport 
{
	public Reader reader;
	public Writer writer;
	
	protected Thread readerThread;
	protected Thread writerThread;
	
	protected OutputStream out = null;
	
	public abstract void connect(String device, int speed) throws Exception;
	public abstract void disconnect() throws IOException;
	
	public abstract void xon() throws Exception;
	public abstract void xoff() throws Exception;
	

	public void printString(String message) throws Exception
	{	
		byte[] startSequence = {(byte)'L',(byte)'P',(byte)'O',(byte)'n',(byte)0x20,(byte)0x20};
		out.write(startSequence);
		Thread.sleep(10);
		
		out.write((byte)0x18);
		Thread.sleep(20);
		
		//byte[] message = {'T','E','S','T'};
		out.write(message.getBytes());
		out.write((byte)0x0D);
		
		byte[] endSequence = {(byte)'L',(byte)'A',(byte)'O',(byte)'f',(byte)0x18,(byte)0x20,(byte)0x0D};
		out.write(endSequence);
		
		Thread.sleep(20);
	}
	
	public static class Reader implements Runnable 
	{		
        InputStream in;
        PTB605Packetizer packetizer;       
        boolean requestStop = false;
        
        public Reader ( InputStream in)
        {
            this.in = in;
            packetizer = new PTB605Packetizer();
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {          	
                	packetizer.packetize(buffer, len);
                	if(requestStop)
                	{
                		requestStop = false;
                		in.close();
                		break;
                	}
                }
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
        
        public LinkedBlockingQueue<PTB605Command> recievePacketQueue()
        {
        	return packetizer.recieveQueue;
        }
	}
	public static class Writer implements Runnable 
	{
        OutputStream out;
        public PTB605Packetizer packetizer;   
        boolean requestStop = false;
        
        public Writer ( OutputStream out)
        {
            this.out = out;
            packetizer = new PTB605Packetizer();
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
