package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class ParameterResponse extends PTB605Command
{
	String value;
	public ParameterResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		value = data.substring(1);
	}
	
	public String toString()
	{
		return "Parameter: " + value;
	}
}