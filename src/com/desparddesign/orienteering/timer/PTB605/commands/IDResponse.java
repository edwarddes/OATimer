package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

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
