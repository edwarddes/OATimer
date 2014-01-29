package com.desparddesign.orienteering.codechecking.sportident.transport;


import java.io.InputStream;
import java.io.OutputStream;

import com.desparddesign.orienteering.jssc.SerialInputStream;
import com.desparddesign.orienteering.jssc.SerialOutputStream;

import jssc.SerialPort;

public class jsscSerialManager extends Transport
{	
	private SerialPort serialPort = null;
	
	public void connect(String device, Integer num, Boolean fast) throws Exception
    {
		serialPort = new SerialPort(device);
		
        if ( serialPort.isOpened() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
        	serialPort.openPort();
            
            if(fast)
            	serialPort.setParams(SerialPort.BAUDRATE_38400, 
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
            else
            	serialPort.setParams(SerialPort.BAUDRATE_4800, 
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
            
            InputStream in = new SerialInputStream(serialPort);
            OutputStream out = new SerialOutputStream(serialPort);
            
            reader = new Reader(in);
            writer = new Writer(out);
            readerThread = new Thread(reader);
            readerThread.start();
            writerThread = new Thread(writer);
            writerThread.start();

        }     
    }
	
	public void disconnect()
	{
		if(serialPort != null)
		{
			try
			{
				readerThread.interrupt();
				writerThread.interrupt();
				reader.stop();
				writer.stop();
				readerThread.join();
				writerThread.join();
				
				serialPort.closePort();
			}
			catch(Exception e)
			{
			}
					
			serialPort = null;
		}
	}
}
