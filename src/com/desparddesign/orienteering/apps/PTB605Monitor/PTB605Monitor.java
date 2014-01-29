package com.desparddesign.orienteering.apps.PTB605Monitor;

import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.GetDateTimeResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.queryCommands.GetDateTime;
import com.desparddesign.orienteering.timer.PTB605v13.transport.RxTxSerialManager;

public class PTB605Monitor 
{
	public PTB605Monitor()
	{
	}

	public static void main(String args[])
	{
		try
		{
			@SuppressWarnings("unused")
			PTB605Monitor main = new PTB605Monitor();
			RxTxSerialManager serialPort = new RxTxSerialManager();
			serialPort.connect("/dev/tty.usbserial",9600);
			
			while(true)
			{
				if(serialPort.reader.recievePacketQueue().isEmpty())
				{
					Thread.sleep(500);
					serialPort.writer.packetizer.dePacketize((new GetDateTime()).rawPacket());
				}
				else
				{
					PTB605Command command = serialPort.reader.recievePacketQueue().take();
					if(command instanceof GetDateTimeResponse)
						break;
				}		
			}
			
			//serialPort.printString("Test - TEST - 123456789");
			
			//serialPort.writer.packetizer.dePacketize((new GetDateTime()).rawPacket());
			//serialPort.writer.packetizer.dePacketize((new GetMemoryStatus()).rawPacket());
			//serialPort.writer.packetizer.dePacketize((new GetParameters()).rawPacket());
			//serialPort.writer.packetizer.dePacketize((new SetDateTime(new LocalDateTime())).rawPacket());
			//serialPort.writer.packetizer.dePacketize((new ResetToDefaults()).rawPacket());
			//serialPort.writer.packetizer.dePacketize((new NewSession()).rawPacket());
			//serialPort.writer.packetizer.dePacketize((new GetMemory(true)).rawPacket());
			//serialPort.writer.packetizer.dePacketize((new GetMemory(false)).rawPacket());
			//serialPort.writer.packetizer.dePacketize((new ClearMemory()).rawPacket());
			//PB
			//PE
			//PA
			//serialPort.writer.packetizer.dePacketize((new SetPrinterPrecision(4)).rawPacket());
			/*
			serialPort.writer.packetizer.dePacketize((new SetLockout(0,false,0.0f)).rawPacket());
			serialPort.writer.packetizer.dePacketize((new SetLockout(0,false,0.5f)).rawPacket());
			serialPort.writer.packetizer.dePacketize((new SetLockout(0,false,7.8f)).rawPacket());
			serialPort.writer.packetizer.dePacketize((new SetLockout(0,true,1.0f)).rawPacket());
			serialPort.writer.packetizer.dePacketize((new SetLockout(0,true,12f)).rawPacket());
			serialPort.writer.packetizer.dePacketize((new SetLockout(0,true,99.3f)).rawPacket());
			*/
			while(true)
			{
				PTB605Command command = serialPort.reader.recievePacketQueue().take();
				System.out.println(command);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
