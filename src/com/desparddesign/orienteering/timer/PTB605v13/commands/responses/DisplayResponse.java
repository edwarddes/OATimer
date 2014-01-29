package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

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
