package com.desparddesign.orienteering.timer.PTB605v13.commands.queryCommands;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class GetDateTime extends PTB605Command 
{
	public GetDateTime()
	{		
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'Q');
		packet.addByte((byte)'D');
		
		return packet;
	}
}
