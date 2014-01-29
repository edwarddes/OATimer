package com.desparddesign.orienteering.timer.PTB605v13.transport;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;

public class RxTxSerialManager extends Transport
{	
	private CommPort commPort = null;
	
	public void connect(String device, int speed) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(device);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            throw new Exception("Error: Port is currently in use");
        }
        else
        {
            commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(speed,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

                serialPort.enableReceiveTimeout(1000);
                
                InputStream in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                
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
			readerThread.interrupt();
			writerThread.interrupt();
			reader.stop();
			writer.stop();
			
			commPort.close();
			commPort = null;
					
			out = null;

		}
	}
}

