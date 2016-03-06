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
	
	public boolean isSIACRecord = false;
	public boolean SIACCardFull = false;
	public boolean SIACBatteryLow = false;
	public int SIACGateMode = 0;
	public int SIACPunchMode = 1;//1=punching 0=timing
	public int SIACRadioMode = 0;
	public int stationOperatingMode = 0;
	public int recordNumber = 0;
	public int totalRecords = 0;
	
	public SIPunchRecord(SICommandPacket basePacket)
	{
		byte[] packetData = basePacket.data();
		//check if this is a SIAC radio record
		if(((packetData[0] & 0x80) >> 7) == 1)
		{
			isSIACRecord = true;
			//mask off the top bit that let us know it was a SIAC record
			control = ((packetData[0]&0x7f)<< 8) | (packetData[1] & 0xff);
			TD = packetData[6] & 0xf;//upper 4 bits used as flags for SIAC functions
			SIACCardFull = ((packetData[6] & 0x10) >> 4) == 1;
			SIACBatteryLow = ((packetData[6] & 0x20) >> 5) == 1;
			SIACGateMode = ((packetData[6] & 0x40) >> 7);
			
			SIACPunchMode = ((packetData[10] & 0x20) >> 5);
			SIACRadioMode = ((packetData[10] & 0xC0) >> 6);
			
			stationOperatingMode = (packetData[10] & 0x1F);
			recordNumber = packetData[11];
			totalRecords = packetData[12];
		}
		else
		{
			control = (packetData[0] << 8) | (packetData[1] & 0xff);
			TD = packetData[6] & 0xff;
		}
		cardNumber =  ((packetData[2] & 0xff) << 24) | ((packetData[3] & 0xff) << 16) | ((packetData[4] & 0xff) << 8) | (packetData[5] & 0xff);
		cardNumber = SICardNumberFormatter.format(cardNumber);
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
		String data = "Punch Record. control: " + control + " card: " + cardNumber + " time: " + punchTime;
		if(isSIACRecord)
		{
			data = data+"\n"+"CardFull: "+SIACCardFull+" BatteryLow: "+SIACBatteryLow+" GateMode: "+SIACGateMode;
			data = data+"\n"+"PunchMode: "+SIACPunchMode+" RadioMode: "+SIACRadioMode+" StationMode: "+stationOperatingMode;
			data = data+"\n"+"Record: "+recordNumber+" of: "+totalRecords;
		}
		
		return data;
	}
}

