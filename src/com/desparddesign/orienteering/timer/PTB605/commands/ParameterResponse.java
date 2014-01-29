package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

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