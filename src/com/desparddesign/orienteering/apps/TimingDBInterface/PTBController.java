package com.desparddesign.orienteering.apps.TimingDBInterface;

import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;

import org.joda.time.LocalDateTime;

import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;
import com.desparddesign.orienteering.timer.PTB605v13.commands.controlCommands.ClearMemory;
import com.desparddesign.orienteering.timer.PTB605v13.commands.controlCommands.NewSession;
import com.desparddesign.orienteering.timer.PTB605v13.commands.parameterCommands.BlockHighInputs;
import com.desparddesign.orienteering.timer.PTB605v13.commands.parameterCommands.SetDateTime;
import com.desparddesign.orienteering.timer.PTB605v13.commands.parameterCommands.SetLockout;
import com.desparddesign.orienteering.timer.PTB605v13.commands.queryCommands.GetDateTime;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.GetDateTimeResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.TimingResponse;

public class PTBController 
{	
	private Preferences prefs;
	
	private String PTBSerialPortIP = null;
	private String PTBSerialPortPort = null;
	
	Thread PTBMonitorThread;
	com.desparddesign.orienteering.timer.PTB605v13.transport.Transport PTBSerialPort = null;
	
	LogController logController;
	ImpulseController impulseController;
	
	private String deviceName = "";
	
	PTBController(LogController log,ImpulseController imp,String name,String IP, String port)
	{
		prefs = Preferences.userNodeForPackage(this.getClass());
		
		logController = log;
		impulseController = imp;
		
		deviceName = name;
		
		if(IP != null)
			PTBSerialPortIP = IP;
		if(port != null)
			PTBSerialPortPort = port;
		
		if(IP == null)
			PTBSerialPortIP = prefs.get("PTB_SERIAL_PORT_IP_"+name, "");
		if(port == null)
			PTBSerialPortPort = prefs.get("PTB_SERIAL_PORT_PORT_"+name, "");
	}
	
	public void connect()
	{
		try
		{
			if(PTBSerialPort == null)
			{
				PTBSerialPort = new com.desparddesign.orienteering.timer.PTB605v13.transport.TelnetSerialManager();
				PTBSerialPort.connect(PTBSerialPortIP,Integer.parseInt(PTBSerialPortPort));
			}
			else
			{
				PTBSerialPort.disconnect();
				
				if(PTBMonitorThread != null)
				{
					PTBMonitorThread.interrupt();
					PTBMonitorThread.join();
					PTBMonitorThread = null;
				}
				
				PTBSerialPort.connect(PTBSerialPortIP,Integer.parseInt(PTBSerialPortPort));
			}
			
			logController.log(deviceName + ": " + "Connected to PTB\n");
		}
		catch(Exception ex)
		{
			logController.log(deviceName + ": " + "Error connecting to PTB\n");
			try {
				PTBSerialPort.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PTBSerialPort = null;
			
			PTBMonitorThread = null;
		}
    }
	
	public void disconnect()
	{	
		try
		{
			if(PTBMonitorThread != null)
			{
				PTBMonitorThread.interrupt();
				PTBMonitorThread.join();
				PTBMonitorThread = null;
			}
			
			PTBSerialPort.disconnect();
			PTBSerialPort = null;
			
			logController.log(deviceName + ": " + "Disconnected from PTB\n");
		}
		catch(Exception ex)
		{
			
		}
	}
	
	public void sync()
	{
		try
		{
			if(PTBMonitorThread != null)
			{
				PTBMonitorThread.interrupt();
				PTBMonitorThread.join();
				PTBMonitorThread = null;
			}
			
			PTBSerialPort.writer.packetizer.dePacketize((new GetDateTime()).rawPacket());
		
			while(true)
			{
				if(PTBSerialPort.reader.recievePacketQueue().isEmpty())
				{
					Thread.sleep(500);
					PTBSerialPort.writer.packetizer.dePacketize((new GetDateTime()).rawPacket());
				}
				else
				{
					PTB605Command command = PTBSerialPort.reader.recievePacketQueue().take();
					if(command instanceof GetDateTimeResponse)
						break;
				}
			}
			logController.log(deviceName + ": " + "Synced to PTB\n");
			
			PTBMonitorThread = new Thread()
			{						
			    public void run()
			    {    
			    	try
			    	{
			    		while(true)
			    		{
				    		final PTB605Command command = PTBSerialPort.reader.recievePacketQueue().take();
				    		if(command != null)
				    		{
					    		SwingUtilities.invokeLater(new Runnable()
					    		{ 
					    			public void run()
					                {
					    				logController.log(deviceName + ": " + command + "\n");
					                }
					    		});
				    		}
				    		
				    		if(command instanceof TimingResponse)
							{
				    			impulseController.processImpulse(((TimingResponse)command).channel, ((TimingResponse)command).time, deviceName);
							}
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
			PTBMonitorThread.start();
			
		}
		catch(Exception ex)
		{
			logController.log(deviceName + ": " + "Error syncing to PTB\n");
			try {
				PTBSerialPort.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PTBSerialPort = null;
			
			PTBMonitorThread = null;
		}
		
	}
	
	public void setPTBDate()
	{
		try 
		{
			PTBSerialPort.writer.packetizer.dePacketize((new SetDateTime((new LocalDateTime()).plusMinutes(1))).rawPacket());
		} 
		catch (InterruptedException e) 
		{
			//unsuccessful
		}
	}
	
	public void newSession()
	{
		try 
		{
			PTBSerialPort.writer.packetizer.dePacketize((new NewSession()).rawPacket());
		} 
		catch (InterruptedException e) 
		{
			//unsuccessful
		}
	}

	public void clearMemory()
	{
		try 
		{
			PTBSerialPort.writer.packetizer.dePacketize((new ClearMemory()).rawPacket());
			SwingUtilities.invokeLater(new Runnable()
    		{ 
    			public void run()
                {
    				logController.log(deviceName + ": " + "PTB memory cleared\n");
                }
    		});
		} 
		catch (InterruptedException e) 
		{
			//unsuccessful
		}
	}
	
	public void enableHigh()
	{
		try 
		{
			PTBSerialPort.writer.packetizer.dePacketize((new BlockHighInputs(false)).rawPacket());
			SwingUtilities.invokeLater(new Runnable()
    		{ 
    			public void run()
                {
    				logController.log(deviceName + ": " + "PTB memory cleared\n");
                }
    		});
		} 
		catch (InterruptedException e) 
		{
			//unsuccessful
		}
	}
	public void disableHigh()
	{
		try 
		{
			PTBSerialPort.writer.packetizer.dePacketize((new BlockHighInputs(true)).rawPacket());
			SwingUtilities.invokeLater(new Runnable()
    		{ 
    			public void run()
                {
    				logController.log(deviceName + ": " + "PTB memory cleared\n");
                }
    		});
		} 
		catch (InterruptedException e) 
		{
			//unsuccessful
		}
	}
	public void setOtherLockout(final float val)
	{
		try 
		{
			PTBSerialPort.writer.packetizer.dePacketize((new SetLockout(0,false,val)).rawPacket());
			SwingUtilities.invokeLater(new Runnable()
    		{ 
    			public void run()
                {
    				logController.log(deviceName + ": " + "Other lockout set to: " +val+"\n");
                }
    		});
		} 
		catch (InterruptedException e) 
		{
			//unsuccessful
		}
	}

	public String getPTBSerialPortIP() 
	{
		return PTBSerialPortIP;
	}
	public void setPTBSerialPortIP(String ip) 
	{
		PTBSerialPortIP = ip;
		prefs.put("PTB_SERIAL_PORT_IP_"+deviceName, ip);
	}

	public String getPTBSerialPortPort() 
	{
		return PTBSerialPortPort;
	}
	public void setPTBSerialPortPort(String port) 
	{
		PTBSerialPortPort = port;
		prefs.put("PTB_SERIAL_PORT_PORT_"+deviceName, port);
	}
}
