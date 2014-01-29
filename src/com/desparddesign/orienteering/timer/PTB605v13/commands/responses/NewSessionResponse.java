package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class NewSessionResponse extends PTB605Command
{
	//N0000xS002xxxxx28.01.97xPrxOnx(CR)
	int session;
	public NewSessionResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		String ses = data.substring(7,10);
		session = new Integer(ses);
	}
	
	public String toString()
	{
		return "Session: " + session;
	}
}
