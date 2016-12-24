package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import org.joda.time.LocalDateTime;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.PTB605v13Packetizer;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class SyncResponse extends PTB605Command
{
	private Integer id = 0;
	private LocalDateTime dt;
	
	//S0000xxxxxxxxxx13:12:00.000000(CR)
	public SyncResponse(PTB605CommandPacket packet)
	{
		@SuppressWarnings("unused")
		String data = new String(packet.data());
	}
	
	public SyncResponse(LocalDateTime dateTime, int i)
	{
		dt = dateTime;
		id = i;
	}
	
	public String toString()
	{
		return "Sync";
	}
	
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.encapsulate = false;
		
		packet.addByte((byte)'S');
		if(id < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)'0');
			packet.addByte((byte)'0');
			packet.addByte((byte)(id.toString().charAt(0)));
		}
		else if(id < 100)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)'0');
			packet.addByte((byte)(id.toString().charAt(0)));
			packet.addByte((byte)(id.toString().charAt(1)));
		}
		else if(id < 1000)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(id.toString().charAt(0)));
			packet.addByte((byte)(id.toString().charAt(1)));
			packet.addByte((byte)(id.toString().charAt(2)));
		}
		else
		{
			packet.addByte((byte)(id.toString().charAt(0)));
			packet.addByte((byte)(id.toString().charAt(1)));
			packet.addByte((byte)(id.toString().charAt(2)));
			packet.addByte((byte)(id.toString().charAt(3)));
		}
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		Integer hour = dt.getHourOfDay();
		if(hour < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(hour.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(hour.toString().charAt(0)));
			packet.addByte((byte)(hour.toString().charAt(1)));
		}
		packet.addByte((byte)':');
		Integer min = dt.getMinuteOfHour();
		if(min < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(min.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(min.toString().charAt(0)));
			packet.addByte((byte)(min.toString().charAt(1)));
		}
		packet.addByte((byte)':');
		Integer sec = dt.getSecondOfMinute();
		if(sec < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)(sec.toString().charAt(0)));
		}
		else
		{
			packet.addByte((byte)(sec.toString().charAt(0)));
			packet.addByte((byte)(sec.toString().charAt(1)));
		}
		packet.addByte((byte)'.');
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		return packet;
	}
}
