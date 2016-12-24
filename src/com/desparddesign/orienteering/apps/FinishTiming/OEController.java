package com.desparddesign.orienteering.apps.FinishTiming;

import java.util.prefs.Preferences;

import com.desparddesign.orienteering.timer.Tdc8000.Tdc8000CommandPacket;

public class OEController 
{
	private Preferences prefs;
	
	private String OESISerialPortIP = null;
	private String OESISerialPortPort = null;
	
	private String OEAlgeSerialPortIP = null;
	private String OEAlgeSerialPortPort = null;
	
	com.desparddesign.orienteering.codechecking.sportident.transport.Transport OESISerialPort = null;
	com.desparddesign.orienteering.timer.Tdc8000.transport.Transport OEAlgeSerialPort = null;
	
	Thread OESIThread;
	Thread OEAlgeThread;
	
	LogController logController;
	
	OEController(LogController log,String SIIP, String SIPort,String AlgeIP, String AlgePort)
	{
		prefs = Preferences.userNodeForPackage(this.getClass());
		
		logController = log;
		
		if(SIIP != null)
			OESISerialPortIP = SIIP;
		if(SIPort != null)
			OESISerialPortPort = SIPort;
		
		if(AlgeIP != null)
			OEAlgeSerialPortIP = AlgeIP;
		if(AlgePort != null)
			OEAlgeSerialPortPort = AlgePort;
		
		if(SIIP == null)
			OESISerialPortIP = prefs.get("OE_SI_SERIAL_PORT_IP", "");
		if(SIPort == null)
			OESISerialPortPort = prefs.get("OE_SI_SERIAL_PORT_PORT", "");
		
		if(AlgeIP == null)
			OEAlgeSerialPortIP = prefs.get("OE_ALGE_SERIAL_PORT_IP", "");
		if(AlgePort == null)
			OEAlgeSerialPortPort = prefs.get("OE_ALGE_SERIAL_PORT_PORT", "");
	}
	
	public void sendAlgePacket(Tdc8000CommandPacket packet) throws InterruptedException
	{
		if(OEAlgeSerialPort != null)
				OEAlgeSerialPort.writer.packetizer.dePacketize(packet);
	}
	
	public void connectToOESI()
	{
		try
		{
			if(OESISerialPort == null)
			{
				OESISerialPort = new com.desparddesign.orienteering.codechecking.sportident.transport.TelnetSerialManager();
				OESISerialPort.connect(OESISerialPortIP,Integer.parseInt(OESISerialPortPort),false);
			}
			else
			{	
				if(OESIThread != null)
				{
					OESIThread.interrupt();
					OESIThread.join();
					OESIThread = null;
				}
				
				OESISerialPort.disconnect();
				
				OESISerialPort.connect(OESISerialPortIP,Integer.parseInt(OESISerialPortPort),true);
			}
			
			logController.log("Connected to OE SI\n");
			
			OESIThread = new Thread()
			{			
				
			    public void run()
			    {    
			    	try
			    	{
			    		while(true)
			    		{
				    		Thread.sleep(10);
			    		}
			    	}
			    	catch(InterruptedException ex)
			    	{
			    		//exit the thread
			    	}
			    	catch(Exception e)
			    	{
			    		e.printStackTrace();
			    	}
			    }
			};
			OESIThread.start();
			
		}
		catch(Exception ex)
		{
			//from connect/disconnect
			logController.log("Error connecting to OE SI\n");
			//OESISerialPort.disconnect();
			OESISerialPort = null;
			
			OESIThread = null;
		}
	}
	public void connectToOEAlge()
	{
		try
		{
			if(OEAlgeSerialPort == null)
			{
				OEAlgeSerialPort = new com.desparddesign.orienteering.timer.Tdc8000.transport.TelnetSerialManager();
				OEAlgeSerialPort.connect(OEAlgeSerialPortIP,Integer.parseInt(OEAlgeSerialPortPort));
			}
			else
			{
				if(OEAlgeThread != null)
				{
					OEAlgeThread.interrupt();
					OEAlgeThread.join();
					OEAlgeThread = null;
				}
				
				OEAlgeSerialPort.disconnect();	
							
				OEAlgeSerialPort.connect(OEAlgeSerialPortIP,Integer.parseInt(OEAlgeSerialPortPort));
			}
			
			logController.log("Connected to OE Alge\n");
			
			OEAlgeThread = new Thread()
			{			
				
			    public void run()
			    {    
			    	try
			    	{
			    		while(true)
			    		{
				    		Thread.sleep(10);
			    		}
			    	}
			    	catch(InterruptedException ex)
			    	{
			    		//exit the thread
			    	}
			    	catch(Exception e)
			    	{
			    		e.printStackTrace();
			    	}
			    }
			};
			OEAlgeThread.start();
		}
		catch(Exception ex)
		{
			logController.log("Error connecting to OE Alge\n");
			//OESISerialPort.disconnect();
			OEAlgeSerialPort = null;
			
			OEAlgeThread = null;
		}
	}
	public void disconnectOESI()
	{
		try
		{
			if(OESIThread != null)
			{
				OESIThread.interrupt();
				OESIThread.join();
				OESIThread = null;
			}
			
			OESISerialPort.disconnect();
			OESISerialPort = null;
			
			logController.log("Disconnected from OE SI\n");
		}
		catch(Exception ex)
		{
			
		}
	}
	public void disconnectOEAlge()
	{
		try
		{
			if(OEAlgeThread != null)
			{
				OEAlgeThread.interrupt();
				OEAlgeThread.join();
				OEAlgeThread = null;
			}
			
			OEAlgeSerialPort.disconnect();
			OEAlgeSerialPort = null;
			
			logController.log("Disconnected from OE Alge\n");
		}
		catch(Exception ex)
		{
			//disconnect failed
		}
	}

	public void setOESISerialPortIP(String ip) 
	{
		OESISerialPortIP = ip;
		prefs.put("OE_SI_SERIAL_PORT_IP", ip);
	}
	public String getOESISerialPortIP() 
	{
		return OESISerialPortIP;
	}

	public void setOESISerialPortPort(String port) 
	{
		OESISerialPortPort = port;
		prefs.put("OE_SI_SERIAL_PORT_PORT", port);
	}
	public String getOESISerialPortPort() 
	{
		return OESISerialPortPort;
	}

	public void setOEAlgeSerialPortIP(String ip) 
	{
		OEAlgeSerialPortIP = ip;
		prefs.put("OE_ALGE_SERIAL_PORT_IP", ip);
	}
	public String getOEAlgeSerialPortIP() 
	{
		return OEAlgeSerialPortIP;
	}
	
	public void setOEAlgeSerialPortPort(String port) 
	{
		OEAlgeSerialPortPort = port;
		prefs.put("OE_ALGE_SERIAL_PORT_PORT", port);
	}
	public String getOEAlgeSerialPortPort() 
	{
		return OEAlgeSerialPortPort;
	}
	

	

	
}
