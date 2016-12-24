package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import org.joda.time.LocalDateTime;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.PTB605v13Packetizer;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class NewSessionResponse extends PTB605Command
{
	int session;
	LocalDateTime dateTime;
	
	//N0000xS002xxxxx28.01.97xPrxOnx(CR)
	public NewSessionResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		String ses = data.substring(7,10);
		session = new Integer(ses);
	}
	
	public NewSessionResponse(int s, LocalDateTime dt)
	{
		session = s;
		dateTime = dt;
	}
	
	public String toString()
	{
		return "Session: " + session;
	}
	
	//N0000xS002xxxxx28.01.97xPrxOnx(CR)
	public PTB605CommandPacket rawPacket()
	{
		PTB605CommandPacket packet = new PTB605CommandPacket();
		packet.encapsulate = false;
		
		packet.addByte((byte)'N');
		//ID 0000
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)'S');
		String ses = String.format("%03d", session);
		packet.addByte((byte)(ses.toString().charAt(0)));
		packet.addByte((byte)(ses.toString().charAt(1)));
		packet.addByte((byte)(ses.toString().charAt(2)));
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		String month = String.format("%02d", dateTime.getMonthOfYear());
		packet.addByte((byte)(month.toString().charAt(0)));
		packet.addByte((byte)(month.toString().charAt(1)));
		packet.addByte((byte)'.');
		String day = String.format("%02d", dateTime.getDayOfMonth());
		packet.addByte((byte)(day.toString().charAt(0)));
		packet.addByte((byte)(day.toString().charAt(1)));
		packet.addByte((byte)'.');
		String year = String.format("%02d", dateTime.getYearOfCentury());
		packet.addByte((byte)(year.toString().charAt(0)));
		packet.addByte((byte)(year.toString().charAt(1)));
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)'P');
		packet.addByte((byte)'r');
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		packet.addByte((byte)'O');
		packet.addByte((byte)'n');
		packet.addByte((byte)PTB605v13Packetizer.SPACE);
		
		return packet;
	}
}
