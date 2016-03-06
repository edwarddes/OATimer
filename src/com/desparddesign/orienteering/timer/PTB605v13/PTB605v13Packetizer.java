package com.desparddesign.orienteering.timer.PTB605v13;

import java.util.concurrent.LinkedBlockingQueue;

import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.BatteryStatusResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.DisplayResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.GetDateTimeResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.IDResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.MemoryStatusResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.NewSessionResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.ParameterResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.PrinterStatusResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.SyncResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.TimingResponse;
import com.desparddesign.orienteering.timer.PTB605v13.transport.Transport;

public class PTB605v13Packetizer
{	
	static byte STX = 0x02;
	static byte ETX = 0x03;
	static byte ACK = 0x06;
	static byte NAK = 0x15;
	static byte CR  = 0x0D;
	static byte SPACE = 0x20;
	
	public LinkedBlockingQueue<PTB605Command> recieveQueue;
	public LinkedBlockingQueue<byte[]> sendQueue;
	public Transport transport;
	
	protected PTB605CommandPacket currentPacket;
	
	private enum packetizationStage
	{
		lookingForFirstChar,
		data
	}
	
	private packetizationStage stage;

	
	public PTB605v13Packetizer(Transport t)
	{
		stage = packetizationStage.lookingForFirstChar;
		recieveQueue = new LinkedBlockingQueue<PTB605Command>();
		sendQueue = new LinkedBlockingQueue<byte[]>();
		transport = t;
	}
	
	public void dePacketize(PTB605CommandPacket sendPacket) throws InterruptedException
	{
		byte[] data = new byte[sendPacket.length() + 3];
		int checksum = 0;
		
		data[0] = STX;//space
		byte[] sendPacketData = sendPacket.data();
		for(int i=0;i<sendPacket.length();i++)
		{
			data[i+1] = sendPacketData[i];
			checksum = checksum + data[i+1];
		}
		data[sendPacket.length()+1] = (byte)((checksum % 256) & 0xFF);
		data[sendPacket.length()+2] = ETX;
		
		sendQueue.put(data);
	}
	
	public void packetize(byte[] inputData, int length) throws InterruptedException
	{
		for(int i=0;i<length;i++)
		{
			byte inByte = inputData[i];
			switch(stage)
			{
				case lookingForFirstChar:
				{
					if(inByte == ACK)
						transport.NAK = false;
					if(inByte == NAK)
						transport.NAK = true;
					
				
					if(inByte != CR && inByte != ACK && inByte != NAK)
					{
						currentPacket = new PTB605CommandPacket();
						stage = packetizationStage.data;
						currentPacket.addByte(inByte);	
					}
					break;
				}
				case data:
				{
					if(inByte == CR)
					{
						stage = packetizationStage.lookingForFirstChar;
						
						//System.out.println("<-- Raw Packet");
						//System.out.println(currentPacket);
						//System.out.println("--> Raw Packet");
						
						String dataString = new String(currentPacket.data());
						
						if(dataString.startsWith("MEMORY") || dataString.startsWith("PM"))
						{
							recieveQueue.put(new MemoryStatusResponse(currentPacket));
						}
						else if(dataString.startsWith("BATTERY"))
						{
							recieveQueue.put(new BatteryStatusResponse(currentPacket));
						} 
						else if(dataString.startsWith("PRINTER"))
						{
							recieveQueue.put(new PrinterStatusResponse(currentPacket));
						}
						else if(dataString.startsWith("PD") || dataString.startsWith("Pd"))
						{
							recieveQueue.put(new GetDateTimeResponse(currentPacket));
						} 
						else if(dataString.startsWith("ID"))
						{
							recieveQueue.put(new IDResponse(currentPacket));
						} 
						else if(dataString.startsWith("P"))
						{
							recieveQueue.put(new ParameterResponse(currentPacket));
						} 
						else if(dataString.startsWith("N"))
						{
							recieveQueue.put(new NewSessionResponse(currentPacket));
						} 
						else if(dataString.startsWith("R"))
						{
							recieveQueue.put(new DisplayResponse(currentPacket));
						} 
						else if(dataString.startsWith("S"))
						{
							recieveQueue.put(new SyncResponse(currentPacket));
						} 
						else if(dataString.startsWith("T"))
						{
							recieveQueue.put(new TimingResponse(currentPacket));
						} 
						else
						{
							System.out.println("<-- Unknown Packet");
							System.out.println(currentPacket);
							System.out.println("--> Unknown Packet");
						}
						
					}
					else
						currentPacket.addByte(inByte);
					
					break;
				}
			}
		}
	}
}