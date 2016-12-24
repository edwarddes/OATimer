package com.desparddesign.orienteering.apps.DBCSVInserter;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.desparddesign.orienteering.apps.TimingDBInterface.DBController;
import com.desparddesign.orienteering.apps.TimingDBInterface.LogController;


public class DBCSVInserter 
{
	LogController lC;
	DBController dbC;
	
	DBCSVInserter(String args[])
	{
		lC = new LogController();
		dbC = new DBController(lC,1);
		
		try
		{	
			Reader in = new FileReader("/Users/edwarddes/Documents/orienteering/software/NENSA_seed_lists/timingSimulation.csv");
			Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
			for (CSVRecord record : records) 
			{
			    String channel = record.get("channel");
			    String time = record.get("time");
			    
			    LocalTime lt = new LocalTime(time);
			    System.out.println(lt);
			    int timeInMillis = lt.getMinuteOfHour()*60*1000+lt.getSecondOfMinute()*1000+lt.getMillisOfSecond();
			    
			    Timer timer = new Timer();
			    MyTimerTask timerTask = new MyTimerTask();
			    timerTask.channel = channel;
			    timerTask.time = lt;
			    timer.schedule(timerTask,timeInMillis);
			    
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex);
		}
	}
	
	public static void main(String args[])
	{
		@SuppressWarnings("unused")
		DBCSVInserter main = new DBCSVInserter(args);
		
	}
	
	class MyTimerTask extends TimerTask  
	{
	     String channel;
	     LocalTime time;

	     public void run() 
	     {
	    	 System.out.println(channel+" "+time);
	    	 LocalTime ltplus = new LocalTime(time.getHourOfDay()+10, time.getMinuteOfHour(), time.getSecondOfMinute(), time.getMillisOfSecond());
	    	 dbC.insertImpulse(Integer.parseInt(channel),ltplus,"PTB Primary");
	     }
	}
}
