package com.desparddesign.orienteering.codechecking.sportident.transport;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

public class RxTxSerialManager extends Transport
{	
	private CommPort commPort = null;
	
	public void connect(String device, Integer num, Boolean fast) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(device);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                if(fast)
                	serialPort.setSerialPortParams(38400,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                else
                	serialPort.setSerialPortParams(4800,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                serialPort.enableReceiveTimeout(1000);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                reader = new Reader(in);
                writer = new Writer(out);
                readerThread = new Thread(reader);
                readerThread.start();
                writerThread = new Thread(writer);
                writerThread.start();

            }
        }     
    }
	
	public void disconnect()
	{
		if(commPort != null)
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
			
			commPort.close();
			
			commPort = null;
		}
	}
}
