package com.desparddesign.orienteering.apps.TimingDBInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DBController 
{
	private LogController logController;
	
	private Connection c = null;
	
	DateTimeFormatter timeFormatter = ISODateTimeFormat.dateTime();
	
	private Map<Integer,String> events;
	private int eventId;
	
	private boolean shouldCreatePrimaryRecords = true;
	private boolean shouldCreateSecondaryRecords = false;
	
	public DBController(LogController l,int eId)
	{
		logController = l;
		
		try 
		{
		   Class.forName("org.postgresql.Driver");
		   c = DriverManager
		      .getConnection("jdbc:postgresql://localhost:5433/bullitttime",
		      "postgres", "13w3");
		   
		   events = getEvents();
		} 
		catch (Exception e) 
		{
		   logController.log(e.getMessage());
		}
		logController.log("Opened database connection");
		
		eventId = eId;
	}
	
	public void setEventId(int eId)
	{
		eventId = eId;
	}
	
	public int getEventId()
	{
		return eventId;
	}
	
	public void setShouldCreatePrimaryRecords(boolean val)
	{
		shouldCreatePrimaryRecords = val;
	}
	
	public void setShouldCreateSecondaryRecords(boolean val)
	{
		shouldCreateSecondaryRecords = val;
	}
	
	public Map<Integer,String> getEvents()
	{
		try
		{
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select id,name from \"Events\"");
			
			Map<Integer, String> map = new HashMap<Integer,String>();

			while (rs.next()) 
			{
			  int id = rs.getInt("id");
			  String name = rs.getString("name");
			  map.put(id, name);
			}
			return map;
			
		}
		catch(SQLException ex)
		{
			logController.log(ex.getMessage());
		}
		return null;
	}

	public void insertImpulse(int channel, LocalTime time,String device)
	{
		Integer dbTimingChannelId=null;
		
		if(device == "PTB Primary" && channel == 1)
			dbTimingChannelId=1;
		else if(device == "PTB Primary" && channel == 2)
			dbTimingChannelId=2;
		else if(device == "PTB Primary" && channel == 3)
			dbTimingChannelId=3;
		else if(device == "PTB Primary" && channel == 4)
			dbTimingChannelId=4;
		else if(device == "PTB Secondary" && channel == 1)
			dbTimingChannelId=9;
		else if(device == "PTB Secondary" && channel == 2)
			dbTimingChannelId=10;
		else if(device == "PTB Secondary" && channel == 3)
			dbTimingChannelId=11;
		else if(device == "PTB Secondary" && channel == 4)
			dbTimingChannelId=12;

		LocalDateTime now = new LocalDateTime();
		LocalDateTime dt = new LocalDateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(), time.getMillisOfSecond());
		//LocalDateTime dt = new LocalDateTime(2016, 12, 17, time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(), time.getMillisOfSecond());
		
		try
		{
			Statement stmt = c.createStatement(); 
			stmt.executeUpdate("INSERT INTO \"TimingImpulses\" (time,\"TimingChannelId\",\"EventId\") "
					+ "VALUES ('"+timeFormatter.print(dt)+"',"+dbTimingChannelId+","+eventId+");",Statement.RETURN_GENERATED_KEYS);
			ResultSet generatedKeys = stmt.getGeneratedKeys();
			Integer insertedId = null;
			if (generatedKeys.next()) 
				insertedId = generatedKeys.getInt(1);
			
			if(device == "PTB Primary" && shouldCreatePrimaryRecords)
			{
				if(channel == 1)
				{
					stmt = c.createStatement();
					stmt.executeUpdate("INSERT INTO \"StartTimingRecords\" (\"startImpulseId\",\"EventId\") "
							+ "VALUES ("+insertedId+","+eventId+");");
				}
				else if(channel == 3)
				{
					stmt = c.createStatement();
					stmt.executeUpdate("INSERT INTO \"FinishTimingRecords\" (\"finishPlungerImpulseId\",\"EventId\") "
							+ "VALUES ("+insertedId+","+eventId+");");
				}
				else if(channel == 4)
				{
					stmt = c.createStatement();
					stmt.executeUpdate("INSERT INTO \"FinishTimingRecords\" (\"finishImpulseId\",\"EventId\") "
							+ "VALUES ("+insertedId+","+eventId+");");
				}
			}
			if(device == "PTB Secondary" && shouldCreateSecondaryRecords)
			{
				if(channel == 1)
				{
					stmt = c.createStatement();
					stmt.executeUpdate("INSERT INTO \"StartTimingRecords\" (\"startImpulseId\",\"EventId\") "
							+ "VALUES ("+insertedId+","+eventId+");");
				}
				else if(channel == 3)
				{
					stmt = c.createStatement();
					stmt.executeUpdate("INSERT INTO \"FinishTimingRecords\" (\"finishPlungerImpulseId\",\"EventId\") "
							+ "VALUES ("+insertedId+","+eventId+");");
				}
				else if(channel == 4)
				{
					stmt = c.createStatement();
					stmt.executeUpdate("INSERT INTO \"FinishTimingRecords\" (\"finishImpulseId\",\"EventId\") "
							+ "VALUES ("+insertedId+","+eventId+");");
				}
			}
			
		}
		catch(SQLException ex)
		{
			logController.log(ex.getMessage());
		}
		
	}
}


