package com.desparddesign.orienteering.timer.Tdc8000.commands;

import org.joda.time.LocalTime;

import com.desparddesign.orienteering.timer.Tdc8000.Tdc8000CommandPacket;

public class TimingResponse extends Tdc8000Command
{
	//xNNNNxCCxxHH:MM:SS.zhtqxGR
	public enum MODE
	{
		TIME,
		TIMEINVALID,
		TIMEMEMO,
		CLEAREDTIME,
		DQTIME,
		MANUALINPUTTIME,
		NEWSTARTTIME,
		PENALTYTIME
	};
	
	public MODE mode;
	Integer bib;
	Integer channel;
	boolean manual;
	LocalTime time;
	String group;
	
	public TimingResponse(Tdc8000CommandPacket packet)
	{
		String data = new String(packet.data());
		char m = data.charAt(0);
		switch(m)
		{
			case ' ':
			{
				mode = MODE.TIME;
				break;
			}
			case '?':
			{
				mode = MODE.TIMEINVALID;
				break;
			}
			case 'm':
			{
				mode = MODE.TIMEMEMO;
				break;
			}
			case 'c':
			{
				mode = MODE.CLEAREDTIME;
				break;
			}
			case 'd':
			{
				mode = MODE.DQTIME;
				break;
			}
			case 'i':
			{
				mode = MODE.MANUALINPUTTIME;
				break;
			}
			case 'n':
			{
				mode = MODE.NEWSTARTTIME;
				break;
			}
			case 'p':
			{
				mode = MODE.PENALTYTIME;
				break;
			}
		}
		bib = new Integer(data.substring(1,5));
		channel = new Integer(data.substring(6,8));
		manual = data.charAt(8) == 'M';
		time = new LocalTime(data.substring(10,24));
		group = data.substring(24,26);
	}
	
	public TimingResponse(MODE m, int bibNum, int chan, boolean man, LocalTime t, String gr)
	{
		mode = m;
		bib = bibNum;
		channel = chan;
		manual = man;
		time = t;
		group = gr;
	}
	
	public String toString()
	{
		return "";
	}
	
	public Tdc8000CommandPacket rawPacket()
	{
		//xNNNNxCCxxHH:MM:SS.zhtqxGR
		Tdc8000CommandPacket packet = new Tdc8000CommandPacket();
		if(mode == MODE.TIME)
			packet.addByte((byte)' ');
		else if(mode == MODE.TIMEMEMO)
			packet.addByte((byte)'m');
		else if(mode == MODE.TIMEINVALID)
			packet.addByte((byte)'?');
		
		String bibNum = String.format("%04d", bib);
		packet.addByte((byte)bibNum.charAt(0));
		packet.addByte((byte)bibNum.charAt(1));
		packet.addByte((byte)bibNum.charAt(2));
		packet.addByte((byte)bibNum.charAt(3));
		
		packet.addByte((byte)' ');
		
		packet.addByte((byte)'C');
		packet.addByte((byte)channel.toString().charAt(0));

		
		if(manual)
			packet.addByte((byte)'M');
		else
			packet.addByte((byte)' ');
		
		packet.addByte((byte)' ');
		
		Integer hour = time.getHourOfDay();
		if(hour < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)hour.toString().charAt(0));
		}
		else
		{
			packet.addByte((byte)hour.toString().charAt(0));
			packet.addByte((byte)hour.toString().charAt(1));
		}
		packet.addByte((byte)':');
		Integer minute = time.getMinuteOfHour();
		if(minute < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)minute.toString().charAt(0));
		}
		else
		{
			packet.addByte((byte)minute.toString().charAt(0));
			packet.addByte((byte)minute.toString().charAt(1));
		}
		packet.addByte((byte)':');
		Integer second = time.getSecondOfMinute();
		if(second < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)second.toString().charAt(0));
		}
		else
		{
			packet.addByte((byte)second.toString().charAt(0));
			packet.addByte((byte)second.toString().charAt(1));
		}
		packet.addByte((byte)'.');
		Integer millis = time.getMillisOfSecond();
		if(millis < 10)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)'0');
			packet.addByte((byte)millis.toString().charAt(0));
		}
		else if(millis < 100)
		{
			packet.addByte((byte)'0');
			packet.addByte((byte)millis.toString().charAt(0));
			packet.addByte((byte)millis.toString().charAt(1));
		}
		else
		{
			packet.addByte((byte)millis.toString().charAt(0));
			packet.addByte((byte)millis.toString().charAt(1));
			packet.addByte((byte)millis.toString().charAt(2));
		}
		
		packet.addByte((byte)'0');
		packet.addByte((byte)'0');
		
		packet.addByte((byte)group.charAt(0));
		packet.addByte((byte)group.charAt(1));
		
		return packet;
	}
}
/*
      	public void test() throws Exception
    	{
    		//xNNNNxCCxxHH:MM:SS.zhtqxGR(CR)
    		byte[] message = {
    				'?',
    				' ',' ',' ',' ',
    				0x20,
    				'C','1',
    				0x20,0x20,
    				'1','1',':','1','1',':','1','1','.','1','2','3','4','5',
    				'0','0',
    				0x0D			
    		};
    		out.write(message);		
    	}
 */
