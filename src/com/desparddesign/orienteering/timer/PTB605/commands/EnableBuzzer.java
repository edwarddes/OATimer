package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

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
		packet.addByte((byte)'A');
		
		if(enabled)
			packet.addByte((byte)'Y');
		else
			packet.addByte((byte)'N');
		
		return packet;
	}
}

