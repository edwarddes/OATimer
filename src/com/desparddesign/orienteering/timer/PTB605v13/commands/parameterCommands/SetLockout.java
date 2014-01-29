package com.desparddesign.orienteering.timer.PTB605v13.commands.parameterCommands;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;

public class SetLockout 
{
	char channel;
	char range;
	Float value;
	
	public SetLockout(int chan, boolean highRange, float val)
	{		
		value = val;

		if(chan == 1)
			channel = '1';
		else if(chan == 4)
			channel = '4';
		else
			channel = 'O';//all other channels
		
		if(highRange)
			range = 'S';//range in seconds
		else
			range = 'D';//range in tenths
		
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
		packet.addByte((byte)'K');
		
		packet.addByte((byte)channel);
		packet.addByte((byte)range);
		
		if(range == 'S')//value is in seconds
		{
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
		else//value is in tenths
		{
			packet.addByte((byte)(value.toString().charAt(0)));
			if(value == 0.0)
				packet.addByte((byte)'0');
			else
				packet.addByte((byte)(value.toString().charAt(2)));
		}
		
		return packet;
	}
}
