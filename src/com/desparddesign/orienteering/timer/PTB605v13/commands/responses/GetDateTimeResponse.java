package com.desparddesign.orienteering.timer.PTB605v13.commands.responses;

import org.joda.time.LocalDateTime;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605CommandPacket;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;

public class GetDateTimeResponse extends PTB605Command
{
	LocalDateTime dateTime;
	
	public GetDateTimeResponse(PTB605CommandPacket packet)
	{
		String data = new String(packet.data());
		String dt = data.substring(2);//v13 used Pd and PD
		String DD,MM,YY,hh,mm,ss;
		if(packet.data()[1] =='D')
		{
			//DDMMYYhhmmss
			DD = dt.substring(0,2);
			MM = dt.substring(2,4);
		}
		else
		{
			//MMDDYYhhmmss
			MM = dt.substring(0,2);
			DD = dt.substring(2,4);
		}
		YY = dt.substring(4,6);
		hh = dt.substring(6,8);
		mm = dt.substring(8,10);
		ss = dt.substring(10,12);
		dateTime = new LocalDateTime(2000+new Integer(YY),new Integer(MM),new Integer(DD),new Integer(hh),new Integer(mm),new Integer(ss));
	}
	
	public String toString()
	{
		return "DateTime: " + dateTime;
	}
}
