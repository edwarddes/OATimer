package com.desparddesign.orienteering.apps.TimingDBInterface;

import org.joda.time.LocalTime;

public class ImpulseController 
{
	private DBController dbController;
	ImpulseController(DBController db)
	{
		dbController = db;
	}
	
	public void processImpulse(int ch,LocalTime t,String name)
	{
		dbController.insertImpulse(ch, t, name);
	}
}
