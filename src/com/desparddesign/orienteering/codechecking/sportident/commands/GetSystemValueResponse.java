package com.desparddesign.orienteering.codechecking.sportident.commands;

import java.util.Arrays;

import com.desparddesign.orienteering.codechecking.sportident.SICommandPacket;

public class GetSystemValueResponse extends SICommand
{

	public GetSystemValueResponse(SICommandPacket basePacket)
	{
		byte packetData[] = basePacket.data();
		control = packetData[0] << 8 | packetData[1];
		address = packetData[2];
		data = Arrays.copyOfRange(basePacket.data(),3,basePacket.length());
		
	}

	public String toString()
	{
		StringBuffer bytesHex = new StringBuffer();
		for (byte b : data) {
			bytesHex.append(String.format("%02X ", b));
			bytesHex.append(" "); // delimiter
		}
		return (new StringBuilder("Station: ")).append(control).append(" Address: ").append(address).append(" Data: [").append(bytesHex).append("]").toString();
	}

	public int control;
	public int address;
	public byte[] data;
}
