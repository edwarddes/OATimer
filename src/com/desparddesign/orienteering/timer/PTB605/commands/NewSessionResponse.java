package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

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
