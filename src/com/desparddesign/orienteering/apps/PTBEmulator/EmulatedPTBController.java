package com.desparddesign.orienteering.apps.PTBEmulator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.desparddesign.orienteering.timer.PTB605v13.PTB605v13Packetizer;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;
import com.desparddesign.orienteering.timer.PTB605v13.commands.controlCommands.ClearMemory;
import com.desparddesign.orienteering.timer.PTB605v13.commands.controlCommands.NewSession;
import com.desparddesign.orienteering.timer.PTB605v13.commands.parameterCommands.SetDateTime;
import com.desparddesign.orienteering.timer.PTB605v13.commands.queryCommands.GetDateTime;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.GetDateTimeResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.NewSessionResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.SyncResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.TimingResponse;

public class EmulatedPTBController 
{
	private int PTBSerialPortPort = 4000;
	
	private Thread PTBMonitorThread;
	private com.desparddesign.orienteering.timer.PTB605v13.transport.Transport PTBSerialPort = null;
	
	private PTBState state = new PTBState();
	
	public byte[] ACKByte = {PTB605v13Packetizer.ACK};
	
	EmulatedPTBController()
	{
		PTBSerialPort = new com.desparddesign.orienteering.timer.PTB605v13.transport.TelnetSerialManager();
		try 
		{
			PTBSerialPort.connect(null,PTBSerialPortPort);
		} catch (Exception e) 
		{
			return;
		}
		
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
			    			System.out.println(command);
			    		}
			    			
			    		if(command instanceof ClearMemory)
						{
			    			PTBSerialPort.writer.sendPacketQueue().put(ACKByte);
			    			clearMemory();
						}
			    		else if(command instanceof NewSession)
			    		{
			    			PTBSerialPort.writer.sendPacketQueue().put(ACKByte);
			    			newSession();
			    		}
			    		else if(command instanceof SetDateTime)
			    		{
			    			state.setDateTimeWaitingForSync(((SetDateTime)command).getDateTime());
			    			PTBSerialPort.writer.sendPacketQueue().put(ACKByte);
			    			state.incrementSession();
			    			PTB605Command response = new NewSessionResponse(state.getCurrentSession(),state.dateTime());
			    			PTBSerialPort.writer.packetizer.dePacketize(response.rawPacket());
			    			state.addToMemory(response);
			    		}
			    		else if(command instanceof GetDateTime)
			    		{
			    			PTBSerialPort.writer.sendPacketQueue().put(ACKByte);
			    			PTBSerialPort.writer.packetizer.dePacketize((new GetDateTimeResponse(state.dateTime())).rawPacket());
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
	
	public void clearMemory()
	{
		state.clearMemory();
		try 
		{
			PTBSerialPort.writer.sendPacketQueue().put(ACKByte);
			state.incrementSession();
			PTB605Command response = new NewSessionResponse(state.getCurrentSession(),state.dateTime());
			PTBSerialPort.writer.packetizer.dePacketize(response.rawPacket());
			state.addToMemory(response);
		} 
		catch (InterruptedException e) 
		{
		}	
	}
	
	public void newSession()
	{
		try 
		{
			PTBSerialPort.writer.sendPacketQueue().put(ACKByte);
			state.incrementSession();
			PTB605Command response = new NewSessionResponse(state.getCurrentSession(),state.dateTime());
			PTBSerialPort.writer.packetizer.dePacketize(response.rawPacket());
			state.addToMemory(response);
		} 
		catch (InterruptedException e) 
		{
		}	
	}
	
	public void sendMemory()
	{
		try 
		{
			for(PTB605Command cmd : state.getMemory())
			{
				PTBSerialPort.writer.packetizer.dePacketize(cmd.rawPacket());
			}
		} 
		catch (InterruptedException e) 
		{
		}	
	}
	
	public void sync()
	{
		state.syncRecieved();
		try 
		{
			PTB605Command command = new SyncResponse(LocalDateTime.now(),0);
			PTBSerialPort.writer.packetizer.dePacketize(command.rawPacket());
			state.addToMemory(command);
		} 
		catch (InterruptedException e) 
		{
		}
	}
	
	public void timingImpulse(int channel, boolean manual)
	{
		try 
		{
			PTB605Command command = new TimingResponse(state.time(), channel, manual, 0);
			PTBSerialPort.writer.packetizer.dePacketize(command.rawPacket());
			state.addToMemory(command);
		} 
		catch (InterruptedException e) 
		{
		}
	}
	
	private class PTBState
	{
		private long timeOffset;
		private LocalDateTime timeWaitingForSync;
		private int currentSession;
		private LinkedList<PTB605Command> history;
		
		PTBState()
		{
			history = new LinkedList<PTB605Command>();
			currentSession = 0;
		}
		
		public void clearMemory()
		{
			history = new LinkedList<PTB605Command>();
			currentSession = 0;
		}
		public void addToMemory(PTB605Command command)
		{
			history.add(command);
		}
		
		public List<PTB605Command> getMemory()
		{
			return (List<PTB605Command>) history.clone();
		}
		
		public void incrementSession()
		{
			currentSession++;
		}
		public int getCurrentSession()
		{
			return currentSession;
		}
		
		public void setDateTimeWaitingForSync(LocalDateTime dt)
		{
			timeWaitingForSync = dt;
		}
		
		public void syncRecieved()
		{		
			timeOffset = System.currentTimeMillis() - timeWaitingForSync.toDateTime().getMillis();			
		}
		
		public LocalDateTime dateTime()
		{
			return LocalDateTime.now().plusMillis((int)timeOffset);
		}
		
		public LocalTime time()
		{
			return LocalTime.now().plusMillis((int)timeOffset);
		}
	}
}