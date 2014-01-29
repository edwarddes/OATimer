package com.desparddesign.orienteering.codechecking.sportident.commands;

import org.joda.time.LocalTime;

import com.desparddesign.orienteering.codechecking.sportident.SICardNumberFormatter;
import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;
import com.desparddesign.orienteering.codechecking.sportident.SITimeFormatter;


public class SIPunchRecord extends SICommand
{
	public int control;
	public int cardNumber;
	private int TD,TH,TL,TSS;
	public LocalTime punchTime;
	
	public SIPunchRecord(SICommandPacket basePacket)
	{
		byte[] packetData = basePacket.data();
		control = ((packetData[0] & 0xff) << 8) | (packetData[1] & 0xff);
		cardNumber =  ((packetData[2] & 0xff) << 24) | ((packetData[3] & 0xff) << 16) | ((packetData[4] & 0xff) << 8) | (packetData[5] & 0xff);
		cardNumber = SICardNumberFormatter.format(cardNumber);
		TD = packetData[6] & 0xff;
		TH = packetData[7] & 0xff;
		TL = packetData[8] & 0xff;
		TSS = packetData[9] & 0xff;
		
		punchTime = SITimeFormatter.format24HourTime(TD,TH,TL,TSS);
	}
	
	public SIPunchRecord()
	{
		control = 0;
		cardNumber = 0;
		TD = 0;
		TH = 0;
		TL = 0;
		TSS = 0;
		punchTime = new LocalTime();
	}
	
	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0xD3);
		packet.setLength(13);
		
		packet.addByte((byte)((control >> 8) & 0xff));
		packet.addByte((byte)(control & 0xff));
		
		int unformattedCardNumber = SICardNumberFormatter.unFormat(cardNumber);
		packet.addByte((byte)((unformattedCardNumber >> 24) & 0xff));
		packet.addByte((byte)((unformattedCardNumber >> 16) & 0xff));
		packet.addByte((byte)((unformattedCardNumber >> 8) & 0xff));
		packet.addByte((byte)(unformattedCardNumber & 0xff));
		
		int time = SITimeFormatter.unFormat24HourTime(punchTime);
		packet.addByte((byte)((time >> 24) & 0xff));
		packet.addByte((byte)((time >> 16) & 0xff));
		packet.addByte((byte)((time >> 8) & 0xff));
		packet.addByte((byte)(time & 0xff));		
		
		packet.addByte((byte)0);
		packet.addByte((byte)0);
		packet.addByte((byte)0);
		return packet;
	}
	
	public String toString()
	{
		return "Punch Record. control: " + control + " card: " + cardNumber + " time: " + punchTime;
	}
}

