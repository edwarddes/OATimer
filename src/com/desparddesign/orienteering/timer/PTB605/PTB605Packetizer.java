package com.desparddesign.orienteering.timer.PTB605;

import java.util.concurrent.LinkedBlockingQueue;

import com.desparddesign.orienteering.timer.PTB605.commands.BatteryStatusResponse;
import com.desparddesign.orienteering.timer.PTB605.commands.DisplayResponse;
import com.desparddesign.orienteering.timer.PTB605.commands.GetDateTimeResponse;
import com.desparddesign.orienteering.timer.PTB605.commands.IDResponse;
import com.desparddesign.orienteering.timer.PTB605.commands.MemoryStatusResponse;
import com.desparddesign.orienteering.timer.PTB605.commands.NewSessionResponse;
import com.desparddesign.orienteering.timer.PTB605.commands.PTB605Command;
import com.desparddesign.orienteering.timer.PTB605.commands.ParameterResponse;
import com.desparddesign.orienteering.timer.PTB605.commands.PrinterStatusResponse;
import com.desparddesign.orienteering.timer.PTB605.commands.SyncResponse;
import com.desparddesign.orienteering.timer.PTB605.commands.TimingResponse;

public class PTB605Packetizer
{
	static byte STX = 0x02;
	static byte ETX = 0x03;
	static byte ACK = 0x06;
	static byte NAK = 0x15;
	static byte CR  = 0x0D;
	static byte SPACE = 0x20;
		
	public LinkedBlockingQueue<PTB605Command> recieveQueue;
	public LinkedBlockingQueue<byte[]> sendQueue;
	
	private PTB605CommandPacket currentPacket;
	
	private enum packetizationStage
	{
		lookingForFirstChar,
		data
	}
	
	private packetizationStage stage;
	
	public PTB605Packetizer()
	{
		stage = packetizationStage.lookingForFirstChar;
		recieveQueue = new LinkedBlockingQueue<PTB605Command>();
		sendQueue = new LinkedBlockingQueue<byte[]>();
	}
	
	public void dePacketize(PTB605CommandPacket sendPacket) throws InterruptedException
	{
		byte[] data = new byte[sendPacket.length() + 2];
		
		byte[] sendPacketData = sendPacket.data();
		for(int i=0;i<sendPacket.length();i++)
			data[i] = sendPacketData[i];
		data[sendPacket.length()] = SPACE;
		data[sendPacket.length()+1] = CR;
		
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
					currentPacket = new PTB605CommandPacket();
					stage = packetizationStage.data;
					
					if(inByte != CR)
						currentPacket.addByte(inByte);	
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
						
						if(dataString.startsWith("MEMORY"))
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
						else if(dataString.startsWith("BDMY"))
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