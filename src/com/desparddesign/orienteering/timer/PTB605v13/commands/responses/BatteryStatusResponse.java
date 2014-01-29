package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

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
