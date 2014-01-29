package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

public class SyncResponse extends PTB605Command
{
	//S0000xxxxxxxxxx13:12:00.000000(CR)
	public SyncResponse(PTB605CommandPacket packet)
	{
		@SuppressWarnings("unused")
		String data = new String(packet.data());
	}
	
	public String toString()
	{
		return "Sync";
	}
}
