package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class SRRPollBufferEmptyResponse extends SICommand
{

	public SRRPollBufferEmptyResponse(SICommandPacket basePacket)
	{
		//byte packetData[] = basePacket.data();
	}

	public String toString()
	{
		return "SRR Module Buffer Empty";
	}
}
