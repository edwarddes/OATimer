package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class PrinterStatusResponse extends PTB605Command
{
	boolean status;

	public PrinterStatusResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		if(data.contains("OFF"))
			status = false;
		else
			status = true;
	}

	public String toString()
	{
		return "Printer: " + (status ? "ON" : "OFF");
	}
}