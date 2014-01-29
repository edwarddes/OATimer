package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

public class EnableDisplay 
{
	boolean enabled;
	
	public EnableDisplay(boolean on)
	{		
		enabled = on;
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'P');
		packet.addByte((byte)'B');
		
		if(enabled)
			packet.addByte((byte)'D');
		else
			packet.addByte((byte)'S');
		
		return packet;
	}
}
