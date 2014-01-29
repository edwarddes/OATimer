package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

public class BatteryStatusResponse extends PTB605Command
{
	boolean status;
	public BatteryStatusResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		if(data.contains("LOW"))
			status = false;
		else
			status = true;
	}
	
	public String toString()
	{
		return "Battery: " + (status ? "OK" : "LOW");
	}
}
