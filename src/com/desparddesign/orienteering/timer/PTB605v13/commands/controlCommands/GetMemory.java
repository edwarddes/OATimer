package com.desparddesign.orienteering.timer.PTB605v13.commands.controlCommands;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;

public class GetMemory
{
	boolean toPrinter;
	
	public GetMemory(boolean printer)
	{	
		toPrinter = printer;
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'C');
		if(toPrinter)
			packet.addByte((byte)'A');
		else
			packet.addByte((byte)'U');
		
		return packet;
	}
}
