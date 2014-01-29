package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICardNumberFormatter;
import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class SI6Inserted extends SICommand
{
	public int control;
	public int cardNumber;
	
	public SI6Inserted(SICommandPacket basePacket)
	{
		byte[] packetData = basePacket.data();
		control = (packetData[0] << 8) | (packetData[1]);
		cardNumber =  ((packetData[2] & 0xff) << 24) | ((packetData[3] & 0xff) << 16) | ((packetData[4] & 0xff) << 8) | (packetData[5] & 0xff);
		cardNumber = SICardNumberFormatter.format(cardNumber);
	}
	
	public String toString()
	{
		return "Insertion detected: SI6. control: " + control + " card: " + cardNumber;
	}
}
