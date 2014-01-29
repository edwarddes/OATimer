package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class IDResponse extends PTB605Command
{
	String id;
	public IDResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		id = data.substring(2);
	}
	
	public String toString()
	{
		return "ID: " + id;
	}
}
