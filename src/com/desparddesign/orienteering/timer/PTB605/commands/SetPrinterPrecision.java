package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

public class SetPrinterPrecision 
{
	Integer precision;
	
	public SetPrinterPrecision(int p)
	{		
		precision = p;
		if(p < 0 || p > 4)
			p = 0;
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'P');
		packet.addByte((byte)'D');
		
		packet.addByte((byte)(precision.toString().charAt(0)));
		
		return packet;
	}
}
