package com.desparddesign.orienteering.timer.Tdc8000;

import java.util.concurrent.LinkedBlockingQueue;

public class Tdc8000Packetizer 
{
	
	private Tdc8000CommandPacket currentPacket;
	
	private enum packetizationStage
	{
		lookingForFirstChar,
		data
	}
	
	private packetizationStage stage;
	
	
	public LinkedBlockingQueue<Tdc8000CommandPacket> recieveQueue;
	public LinkedBlockingQueue<byte[]> sendQueue;
	
	public Tdc8000Packetizer()
	{
		stage = packetizationStage.lookingForFirstChar;
		recieveQueue = new LinkedBlockingQueue<Tdc8000CommandPacket>();
		sendQueue = new LinkedBlockingQueue<byte[]>();
	}
	
	public void dePacketize(Tdc8000CommandPacket sendPacket) throws InterruptedException
	{
		byte[] data = new byte[sendPacket.length() + 1];
		
		byte[] sendPacketData = sendPacket.data();
		for(int i=0;i<sendPacket.length();i++)
			data[i] = sendPacketData[i];
		data[sendPacket.length()] = 0x0D;//CR
		
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
					currentPacket = new Tdc8000CommandPacket();
					stage = packetizationStage.data;
					
					if(inByte != 0x0D)
						currentPacket.addByte(inByte);	
					break;
				}
				case data:
				{
					if(inByte == 0x0D)
					{
						stage = packetizationStage.lookingForFirstChar;
						
						System.out.println("<-- Raw Packet");
						System.out.println(currentPacket);
						System.out.println("--> Raw Packet");
						
						//String dataString = new String(currentPacket.data());
						
						/*
						else
						{
							System.out.println("<-- Unknown Packet");
							System.out.println(currentPacket);
							System.out.println("--> Unknown Packet");
						}
						*/
						
					}
					else
						currentPacket.addByte(inByte);
					
					break;
				}
			}
		}
	}
}
