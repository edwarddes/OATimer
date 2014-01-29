package com.desparddesign.orienteering.timer.PTB605v13.commands.parameterCommands;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;

public class EnableBuzzer 
{
	boolean enabled;
	
	public EnableBuzzer(boolean on)
	{		
		enabled = on;
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'P');
		
		if(enabled)
			packet.addByte((byte)'B');
		else
			packet.addByte((byte)'b');
		
		return packet;
	}
}

