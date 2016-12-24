package com.desparddesign.orienteering.apps.FinishTiming;

public class LogController 
{
	private LogPanel logPanel = null;
	
	LogController()
	{
	}
	
	public void setLogPanel(LogPanel lp)
	{
		logPanel = lp;
	}
	
	public void log(String message)
	{
		if(logPanel != null)
			logPanel.append(message);
	}
}
