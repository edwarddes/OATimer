package com.desparddesign.orienteering.apps.TimingDBInterface;

public class LogController 
{
	private LogPanel logPanel = null;
	
	public LogController()
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
