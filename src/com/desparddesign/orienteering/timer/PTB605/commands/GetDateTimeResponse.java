package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

public class GetDateTimeResponse extends PTB605Command
{
	String dt;
	public GetDateTimeResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		if(packet.data()[0] == 'P')
			dt = data.substring(2);//v13 used Pd and PD
		else
			dt = data.substring(5);//old protocol uses BDMY
	}
	
	public String toString()
	{
		return "DateTime: " + dt;
	}
}
