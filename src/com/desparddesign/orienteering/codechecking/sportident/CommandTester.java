package com.desparddesign.orienteering.codechecking.sportident;

import com.desparddesign.orienteering.codechecking.sportident.commands.GetSetMSModeResponse;
import com.desparddesign.orienteering.codechecking.sportident.commands.GetSystemValue;
import com.desparddesign.orienteering.codechecking.sportident.commands.GetSystemValueResponse;
import com.desparddesign.orienteering.codechecking.sportident.commands.SICommand;
import com.desparddesign.orienteering.codechecking.sportident.commands.SetMSMode;
import com.desparddesign.orienteering.codechecking.sportident.commands.SetSystemValue;
import com.desparddesign.orienteering.codechecking.sportident.transport.RxTxSerialManager;

public class CommandTester
{

	public CommandTester()
	{
	}

	public static void main(String args[])
	{
		try
		{
			@SuppressWarnings("unused")
			CommandTester main = new CommandTester();
			RxTxSerialManager serialPort = new RxTxSerialManager();
			serialPort.connect("/dev/tty.SLAB_USBtoUART",0, true);
			serialPort.writer.packetizer.dePacketize((new SetMSMode(SetMSMode.MODE.REMOTE)).rawPacket());
			while(true)
			{
				SICommand command = (SICommand)serialPort.reader.recievePacketQueue().take();
				while(!(command instanceof GetSystemValueResponse))
				{
					if(Preferences.getInstance().printRecievePackets())
						System.out.println(command);
					
					if(command instanceof GetSetMSModeResponse)
						serialPort.writer.packetizer.dePacketize((new GetSystemValue(15, 1)).rawPacket());
				}
				
				serialPort.writer.packetizer.dePacketize((new SetSystemValue()).rawPacket());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
