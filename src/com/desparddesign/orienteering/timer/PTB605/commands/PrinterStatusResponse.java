package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

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