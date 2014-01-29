package com.desparddesign.orienteering.timer.PTB605v13.commands.queryCommands;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class GetParameters extends PTB605Command 
{
	public GetParameters()
	{		
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'Q');
		packet.addByte((byte)'P');
		
		return packet;
	}
}
