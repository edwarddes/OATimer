package com.desparddesign.orienteering.timer.PTB605.commands;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

public class SetLockout 
{
	Integer channel;
	boolean highRange;
	Float value;
	
	public SetLockout(int chan, boolean range, float val)
	{		
		value = val;
		channel = chan;
		if(chan < 0 || chan > 4)
			chan = 0;
		
		highRange = range;
		if(highRange)
		{
			if(value < 1.0 || value > 99.0)
				value = 1.0f;
		}
		else
		{
			if(value < 0.0 || value > 9.9)
				value = 0.0f;
		}
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.addByte((byte)'P');
		packet.addByte((byte)'L');
		
		packet.addByte((byte)(channel.toString().charAt(0)));
		
		if(highRange)
		{
			packet.addByte((byte)'S');
			if(value < 10)
			{
				packet.addByte((byte)'0');
				packet.addByte((byte)(value.toString().charAt(0)));
			}
			else
			{
				packet.addByte((byte)(value.toString().charAt(0)));
				packet.addByte((byte)(value.toString().charAt(1)));
			}
		
		}
		else
		{
			packet.addByte((byte)'D');
			packet.addByte((byte)(value.toString().charAt(0)));
			if(value == 0.0)
				packet.addByte((byte)'0');
			else
				packet.addByte((byte)(value.toString().charAt(2)));
		}
		
		return packet;
	}
}
