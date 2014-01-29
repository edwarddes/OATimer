package com.desparddesign.orienteering.codechecking.sportident.commands;

import java.util.Vector;

import org.joda.time.LocalTime;

import com.desparddesign.orienteering.codechecking.sportident.SICardNumberFormatter;
import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;
import com.desparddesign.orienteering.codechecking.sportident.SITimeFormatter;

public class SI5ReadBlock extends SICommand
{
	public int control;
	public int cardNumber;
	public LocalTime start;
	public LocalTime finish;
	public LocalTime check;
	
	public int recordCount;
	
	public Vector<SI5PunchRecord> punchRecords;
	
	public class SI5PunchRecord
	{
		public int controlNumber;
		public LocalTime punchTime;
	}
	
	public SI5ReadBlock(SICommandPacket basePacket)
	{
		punchRecords = new Vector<SI5PunchRecord>(36);
		byte[] packetData = basePacket.data();
		control = (packetData[0] << 8) | (packetData[1]);
		cardNumber =  ((packetData[8] & 0xff) << 16) | ((packetData[6] & 0xff) << 8) | (packetData[7] & 0xff);
		cardNumber = SICardNumberFormatter.format(cardNumber);
		
		start = SITimeFormatter.format12HourTime(((packetData[21] & 0xff) << 8),(packetData[22] & 0xff));
		finish = SITimeFormatter.format12HourTime(((packetData[23] & 0xff) << 8),(packetData[24] & 0xff));
		check = SITimeFormatter.format12HourTime(((packetData[27] & 0xff) << 8),(packetData[28] & 0xff));
		
		recordCount = (packetData[25] & 0xff) - 1;
		
		int byteOffset = 35;
		for(int i=0;i<30;i++)
		{
			punchRecords.insertElementAt(new SI5PunchRecord(), i);
			punchRecords.get(i).controlNumber = packetData[byteOffset] & 0xff;
			punchRecords.get(i).punchTime = 
				SITimeFormatter.format12HourTime(((packetData[byteOffset+1] & 0xff) << 8),(packetData[byteOffset+2] & 0xff));
			byteOffset+=3;
			if((byteOffset - 2) % 16 == 0)
				byteOffset++;
		}
		byteOffset = 34;
		for(int i=30;i<36;i++)
		{
			punchRecords.insertElementAt(new SI5PunchRecord(), i);
			punchRecords.get(i).controlNumber = packetData[byteOffset] & 0xff;
			punchRecords.get(i).punchTime = null;
			byteOffset+=16;
		}
		
		//if we cross 12:00, add 12 to the follow up controls
		
		
	}
	
	public String toString()
	{
		String ret =  "Read Card: SI5. control: " + control + " card: " + cardNumber + "\n" + 
				"Start: " + start + 
				" Finish: " + finish + 
				" Check: " + check + "\n" +
				"Records: " + recordCount + "\n";
		for(int i=0;i<recordCount;i++)
			ret = ret + punchRecords.get(i).controlNumber + " " + punchRecords.get(i).punchTime.toString("HH:mm:ss") + "\n";
		
		return ret;
		
		
	}
}
