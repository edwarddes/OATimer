package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

public class MemoryStatusResponse extends PTB605Command
{
	boolean status;
	public MemoryStatusResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		if(data.contains("FULL"))
			status = false;
		else
			status = true;
	}
	
	public String toString()
	{
		return "Memory: " + (status ? "OK" : "FULL");
	}
}
