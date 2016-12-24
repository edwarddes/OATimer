package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import org.joda.time.LocalTime;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.PTB605v13Packetizer;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

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
		try
		{
			time = new LocalTime(t);
		}
		catch(Exception ex)
		{
			time = null;
		}
		
	}
	
	public TimingResponse(LocalTime dt, int c, boolean m, int s)
	{
		time = dt;
		channel = c;
		manual = m;
		sequence = s;
	}
	
	public String toString()
	{
		return "Impulse: " + (manual ? "M" : "") + channel + " " + sequence + " " + time;
	}
	
	//Txxxxx00003x03x13:12:16.345678(CR)
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.encapsulate = false;
		
		packet.addByte((byte)'T');
		//ID 0000
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		packet.addByte(PTB605v13Packetizer.SPACE);
		String seq = String.format("%05d", sequence);
		packet.addByte((byte)(seq.toString().charAt(0)));
		packet.addByte((byte)(seq.toString().charAt(1)));
		packet.addByte((byte)(seq.toString().charAt(2)));
		packet.addByte((byte)(seq.toString().charAt(3)));
		packet.addByte((byte)(seq.toString().charAt(4)));
		packet.addByte(PTB605v13Packetizer.SPACE);
		if(manual)
		{
			packet.addByte((byte)'M');
			packet.addByte((byte)(channel.toString().charAt(0)));
		}
		else
		{
			String chan = String.format("%02d", channel);
			packet.addByte((byte)(chan.toString().charAt(0)));
			packet.addByte((byte)(chan.toString().charAt(1)));
		}
		packet.addByte(PTB605v13Packetizer.SPACE);
		String hour = String.format("%02d", time.getHourOfDay());
		packet.addByte((byte)(hour.toString().charAt(0)));
		packet.addByte((byte)(hour.toString().charAt(1)));
		packet.addByte((byte)':');
		String min = String.format("%02d", time.getMinuteOfHour());
		packet.addByte((byte)(min.toString().charAt(0)));
		packet.addByte((byte)(min.toString().charAt(1)));
		packet.addByte((byte)':');
		String sec = String.format("%02d", time.getSecondOfMinute());
		packet.addByte((byte)(sec.toString().charAt(0)));
		packet.addByte((byte)(sec.toString().charAt(1)));
		packet.addByte((byte)'.');
		String millis = String.format("%03d", time.getMillisOfSecond());
		packet.addByte((byte)(millis.toString().charAt(0)));
		packet.addByte((byte)(millis.toString().charAt(1)));
		packet.addByte((byte)(millis.toString().charAt(2)));
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		
		return packet;
	}
}
