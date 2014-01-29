package com.desparddesign.orienteering.timer.PTB605v13.transport;


import java.io.InputStream;
import java.io.OutputStream;

import com.desparddesign.orienteering.jssc.SerialInputStream;
import com.desparddesign.orienteering.jssc.SerialOutputStream;

import jssc.SerialPort;
import jssc.SerialPortException;

public class jsscSerialManager extends Transport
{	
	private SerialPort serialPort = null;
	
	public void connect(String device, int speed) throws Exception
    {
		serialPort = new SerialPort(device);
		
        if ( serialPort.isOpened() )
        {
            throw new Exception("Error: Port is currently in use");
        }
        else
        {
        	serialPort.openPort();
            
        	serialPort.setParams(speed, 
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
			catch(InterruptedException e)
			{
				//join failed
			}
			catch(SerialPortException e)
			{
				//closePort failed
			}
			serialPort = null;
		}
	}
}
