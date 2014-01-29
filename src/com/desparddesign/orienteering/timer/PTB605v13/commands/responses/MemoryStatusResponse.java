package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class MemoryStatusResponse extends PTB605Command
{
	String status;
	
	public MemoryStatusResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		if(packet.data()[0] == 'P')
		{
			status = data.substring(2);
		}
		else
		{
			if(data.contains("FULL"))
				status = "FULL";
			else
				status = "OK";
		}
	}
	
	public String toString()
	{
		return "Memory: " + status;
	}
}
