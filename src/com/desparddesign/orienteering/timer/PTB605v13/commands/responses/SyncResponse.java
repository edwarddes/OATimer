package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

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
