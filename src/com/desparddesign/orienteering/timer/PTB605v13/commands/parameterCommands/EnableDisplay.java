package com.desparddesign.orienteering.timer.PTB605v13.commands.parameterCommands;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;

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
		
		if(enabled)
			packet.addByte((byte)'L');
		else
			packet.addByte((byte)'l');
		
		return packet;
	}
}
