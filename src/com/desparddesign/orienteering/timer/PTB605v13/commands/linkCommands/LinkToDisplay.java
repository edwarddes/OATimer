package com.desparddesign.orienteering.timer.PTB605v13.commands.linkCommands;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;

public class LinkToDisplay 
{
	public LinkToDisplay()
	{		
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'L');
		packet.addByte((byte)'L');
		
		return packet;
	}
}
