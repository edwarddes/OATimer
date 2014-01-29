package com.desparddesign.orienteering.codechecking.sportident;

import java.io.FileInputStream;
import java.util.Properties;

public class Preferences 
{
	private static Preferences instance = null;
	
	private Properties prefs;
	
	private Preferences()
	{
		prefs = new Properties();
		try
		{
			//System.out.println(System.getProperty("user.dir"));
			FileInputStream in = new FileInputStream("preferences");
			prefs.load(in);
			in.close();
		}
		catch (Exception e)
		{
			//swallow
		}
	}
	
	public static Preferences getInstance()
	{
		if(instance == null)
			instance = new Preferences();
		return instance;
	}
	
	public String controlType()
	{
		return prefs.getProperty("controlType", "control");
	}
	
	public boolean printRawRecievePackets()
	{
		return (prefs.getProperty("printRawRecievePackets", "false")).equals("true");
	}
	
	public boolean printRawRecieveUnknownPackets()
	{
		return (prefs.getProperty("printRawRecieveUnknownPackets", "false")).equals("true");
	}
	
	public boolean printRecievePackets()
	{
		return (prefs.getProperty("printRecievePackets","false")).equals("true");
	}
	
	public String transport()
	{
		return prefs.getProperty("transport","RxTx");
	}
	
	public String serialPort()
	{
		return prefs.getProperty("serialPort","/dev/tty.usbserial");
	}
	
	public String race()
	{
		return prefs.getProperty("raceID");
	}
	
	public String host()
	{
		return prefs.getProperty("host");
	}
	
	public Integer port()
	{
		return new Integer(prefs.getProperty("port"));
	}
	
	public String postURL()
	{
		return prefs.getProperty("postURL");
	}
	
	
}


