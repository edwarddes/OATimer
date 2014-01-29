package com.desparddesign.orienteering.codechecking.sportident.commands;

import java.util.Vector;

import org.joda.time.LocalTime;

import com.desparddesign.orienteering.codechecking.sportident.SICardNumberFormatter;
import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;
import com.desparddesign.orienteering.codechecking.sportident.SITimeFormatter;

public class SI6ReadBlock extends SICommand
{
	public int control;
	public int block;
	public int cardNumber;
	public SI6PunchRecord start;
	public SI6PunchRecord finish;
	public SI6PunchRecord check;
	public SI6PunchRecord clear;
	
	public Vector<SI6PunchRecord> punchRecords;
	
	public class SI6PunchRecord
	{
		public int PTD;
		public int controlNumber;
		public LocalTime punchTime;
	}
	
	public SI6ReadBlock(SICommandPacket basePacket)
	{
		byte[] packetData = basePacket.data();
		control = (packetData[0] << 8) | (packetData[1]);
		block = (packetData[2] & 0xff);
		cardNumber =  ((packetData[13] & 0xff) << 24) | ((packetData[14] & 0xff) << 16) | ((packetData[15] & 0xff) << 8) | (packetData[16] & 0xff);
		cardNumber = SICardNumberFormatter.format(cardNumber);
		switch (block)
		{
			case 0:
			{
				finish = new SI6PunchRecord();
				finish.PTD = packetData[23] & 0xff;
				finish.controlNumber = packetData[24] & 0xff;
				finish.punchTime =  SITimeFormatter.format24HourTime(finish.PTD, ((packetData[25] & 0xff) << 8),(packetData[26] & 0xff));
				
				start = new SI6PunchRecord();
				start.PTD = packetData[27] & 0xff;
				start.controlNumber = packetData[28] & 0xff;
				start.punchTime =  SITimeFormatter.format24HourTime(start.PTD, ((packetData[29] & 0xff) << 8),(packetData[30] & 0xff));
				
				check = new SI6PunchRecord();
				check.PTD = packetData[31] & 0xff;
				check.controlNumber = packetData[32] & 0xff;
				check.punchTime =  SITimeFormatter.format24HourTime(check.PTD,((packetData[33] & 0xff) << 8),(packetData[34] & 0xff));
				
				clear = new SI6PunchRecord();
				clear.PTD = packetData[35] & 0xff;
				clear.controlNumber = packetData[36] & 0xff;
				clear.punchTime =  SITimeFormatter.format24HourTime(clear.PTD,((packetData[37] & 0xff) << 8),(packetData[38] & 0xff));
				break;
			}
			case 1:
			{
				break;
			}
			case 6:
			{
				punchRecords = new Vector<SI6PunchRecord>(32);
				int address = 3;
				while(true)
				{
					SI6PunchRecord record = new SI6PunchRecord();
					record.PTD = packetData[address] & 0xff;
					record.controlNumber = packetData[address+1] & 0xff;
					record.punchTime =  SITimeFormatter.format24HourTime(record.PTD,((packetData[address+2] & 0xff) << 8),(packetData[address+3] & 0xff));
					
					address+=4;
					
					if(record.punchTime == null)
						break;
					punchRecords.add(record);
					if(address>=128)
						break;
				}
				break;
			}
			case 7:
			{
				punchRecords = new Vector<SI6PunchRecord>(32);
				int address = 3;
				while(true)
				{
					SI6PunchRecord record = new SI6PunchRecord();
					record.PTD = packetData[address] & 0xff;
					record.controlNumber = packetData[address+1] & 0xff;
					record.punchTime =  SITimeFormatter.format24HourTime(record.PTD,((packetData[address+2] & 0xff) << 8),(packetData[address+3] & 0xff));
					
					address+=4;
					
					if(record.punchTime == null)
						break;
					punchRecords.add(record);
					if(address>=128)
						break;
				}
				break;
			}
		}
	}
	
	public String toString()
	{
		String ret =  "Read Card: SI6. control: " + control + " block: " + block + "\n";
		if(block == 0)
		{
			ret = ret + "card: " + cardNumber + "\n";
			ret = ret + "finish: " + finish.controlNumber + " " + finish.punchTime.toString("HH:mm:ss") + "\n";
			ret = ret + "start: " + start.controlNumber + " " + start.punchTime.toString("HH:mm:ss") + "\n";
			ret = ret + "check: " + check.controlNumber + " " + check.punchTime.toString("HH:mm:ss") + "\n";
			ret = ret + "clear: " + clear.controlNumber + " " + clear.punchTime.toString("HH:mm:ss") + "\n";
		}
		else if(block == 6 || block == 7)
		{
			for(SI6PunchRecord record : punchRecords)
			{
				ret = ret + "control: " + record.controlNumber + " " + record.punchTime.toString("HH:mm:ss") + "\n";
			}
		}
		return ret;		
	}
}
