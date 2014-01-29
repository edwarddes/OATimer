package com.desparddesign.orienteering.timer.PTB605.commands;

import org.joda.time.LocalTime;

import com.desparddesign.orienteering.timer.PTB605.PTB605CommandPacket;

public class TimingResponse extends PTB605Command
{
	//Txxxxx00003x03x13:12:16.345678(CR)
	public boolean manual;
	public Integer channel;
	public Integer sequence;
	public LocalTime time;
	
	public TimingResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		sequence = new Integer(data.substring(6,11));
		manual = data.charAt(12) == 'M';
		if(manual)
			channel = new Integer(data.substring(13,14));
		else
			channel = new Integer(data.substring(12,14));
		
		String t = data.substring(15,28);
		time = new LocalTime(t);
	}
	
	public String toString()
	{
		return "Impulse: " + (manual ? "M" : "") + channel + " " + sequence + " " + time;
	}
}
