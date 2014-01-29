package com.desparddesign.orienteering.codechecking.sportident.commands;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class SetMSModeResponse extends SICommand
{
	public SetMSModeResponse(SetMSMode.MODE m, int c)
	{
		if(m == SetMSMode.MODE.DIRECT)
			mode = 0x4D;
		else
			mode = 0x53;
		
		control = c;
	}
	
	public SetMSModeResponse(SICommandPacket basePacket)
	{
		byte packetData[] = basePacket.data();
		control = packetData[0] << 8 | packetData[1];
		mode = packetData[2];
	}

	public String toString()
	{
		if(mode == 0x4D)
			return (new StringBuilder("Station: ")).append(control).append(" Mode: DIRECT").toString();
		else
			return (new StringBuilder("Station: ")).append(control).append(" Mode: REMOTE").toString();
	}
	
	public SICommandPacket rawPacket()
	{
		SICommandPacket packet = new SICommandPacket();
		packet.setCommand(0xF0);
		packet.setLength(3);
		
		packet.addByte((byte)((control >> 8) & 0xff));
		packet.addByte((byte)(control & 0xff));
		packet.addByte((byte)(mode & 0xff));
		return packet;
	}

	public int control;
	public int mode;
}
