package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

public class DisplayResponse extends PTB605Command
{
	String display;
	public DisplayResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		display = data.substring(2);
	}
	
	public String toString()
	{
		return "Display: " + display;
	}
}
