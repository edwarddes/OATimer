package com.desparddesign.orienteering.apps.SILogger;

import java.util.concurrent.TimeUnit;

import com.desparddesign.orienteering.codechecking.sportident.commands.GetSystemValue;
import com.desparddesign.orienteering.codechecking.sportident.commands.GetTime;
import com.desparddesign.orienteering.codechecking.sportident.commands.SICommand;
import com.desparddesign.orienteering.codechecking.sportident.commands.SRRPoll;
import com.desparddesign.orienteering.codechecking.sportident.commands.SRRPollBufferEmptyResponse;
import com.desparddesign.orienteering.codechecking.sportident.commands.SRRQuery;
import com.desparddesign.orienteering.codechecking.sportident.commands.SetSystemValue;
import com.desparddesign.orienteering.codechecking.sportident.transport.TelnetSerialManager;
import com.desparddesign.orienteering.codechecking.sportident.transport.Transport;
import com.desparddesign.orienteering.codechecking.sportident.transport.jsscSerialManager;

public class SILogger
{
	public SILogger()
	{
		super();
	}

    public static void main ( String[] args )
    {
    	
    	Transport serialPort;
    	
        try
        {
        	@SuppressWarnings("unused")
        	SILogger main = new SILogger();
            //serialPort = new jsscSerialManager();
            //serialPort.connect("/dev/tty.SLAB_USBtoUART", 0, true);
        	serialPort = new TelnetSerialManager();
        	serialPort.connect("192.168.10.119",3000,true);
            
        	//serialPort.writer.packetizer.dePacketize((new SetSystemValue(0x3E, new byte[]{0x01}) .rawPacket()));
            //serialPort.writer.packetizer.dePacketize((new GetSystemValue(0x3E,1)).rawPacket());
            //serialPort.writer.packetizer.dePacketize((new GetTime()).rawPacket());
            //serialPort.writer.packetizer.dePacketize((new SRRPoll()).rawPacket());
            
            while(true)
            {
            	SICommand command = serialPort.reader.recievePacketQueue().poll(1000, TimeUnit.MILLISECONDS);
            	if(command == null)
            		continue;
            	
            	if(!(command instanceof SRRPollBufferEmptyResponse))
            		System.out.println(command);
            	
            	//serialPort.writer.packetizer.dePacketize((new SRRPoll()).rawPacket());
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}