package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

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
		packet.addByte((byte)'U');
		if(toPrinter)
			packet.addByte((byte)'A');
		
		return packet;
	}
}
