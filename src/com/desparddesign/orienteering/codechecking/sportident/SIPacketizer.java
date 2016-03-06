package com.desparddesign.orienteering.codechecking.sportident;

import java.util.concurrent.LinkedBlockingQueue;

import com.desparddesign.orienteering.codechecking.sportident.commands.GetSystemValueResponse;
import com.desparddesign.orienteering.codechecking.sportident.commands.GetTimeResponse;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI5Inserted;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI5ReadBlock;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI6Inserted;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI6ReadBlock;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI89Inserted;
import com.desparddesign.orienteering.codechecking.sportident.commands.SICommand;
import com.desparddesign.orienteering.codechecking.sportident.commands.SIPunchRecord;
import com.desparddesign.orienteering.codechecking.sportident.commands.SIRemoved;
import com.desparddesign.orienteering.codechecking.sportident.commands.SRRPollBufferEmptyResponse;

public class SIPacketizer 
{
	static byte STX = 0x02;
	static byte ETX = 0x03;
	static byte ACK = 0x06;
	static byte NAK = 0x15;
	static byte DLE = 0x10;
	
	private SICommandPacket currentPacket;
	
	private enum packetizationStage
	{
		lookingForSTX,
		commandByte,
		lengthByte,
		data,
		crc1,
		crc2,
		etx	
	}
	
	private packetizationStage stage;
	private int dataByteCounter = 0;
	
	@SuppressWarnings("unused")
	private int crc1;
	@SuppressWarnings("unused")
	private int crc2;
	
	public LinkedBlockingQueue<SICommand> recieveQueue;
	public LinkedBlockingQueue<byte[]> sendQueue;
	
	public SIPacketizer()
	{
		stage = packetizationStage.lookingForSTX;
		recieveQueue = new LinkedBlockingQueue<SICommand>();
		sendQueue = new LinkedBlockingQueue<byte[]>();
	}
	
	public void dePacketize(SICommandPacket sendPacket) throws InterruptedException
	{
		byte[] data = new byte[sendPacket.length() + 6];
		data[0] = STX;
		data[1] = (byte) (sendPacket.command() & 0xff);
		data[2] = (byte) (sendPacket.length() & 0xff);
		
		byte[] sendPacketData = sendPacket.data();
		for(int i=0;i<sendPacket.length();i++)
			data[i+3] = sendPacketData[i];
		
		int crc = CRCCalculator.crc(sendPacketData, sendPacket.command(), sendPacket.length());
		data[sendPacket.length() + 3] = (byte) ((crc & 0xff00) >> 8);
		data[sendPacket.length() + 4] = (byte) (crc & 0xff);
		data[sendPacket.length() + 5] = ETX;
		sendQueue.put(data);
	}
	public void packetize(byte[] inputData, int length) throws InterruptedException
	{
		for(int i=0;i<length;i++)
		{
			byte inByte = inputData[i];
			switch(stage)
			{
				case lookingForSTX:
				{
					if((inByte & 0xff) == STX)
					{
						stage = packetizationStage.commandByte;
						currentPacket = new SICommandPacket();
					}
					else
						continue;
					break;
				}
				case commandByte:
				{
					if((inByte & 0xff) == STX)
						break;
					
					currentPacket.setCommand(inByte & 0xff);
					dataByteCounter = 0;
					stage = packetizationStage.lengthByte;
					break;
				}
				case lengthByte:
				{
					currentPacket.setLength(inByte & 0xff);
					stage = packetizationStage.data;
					break;
				}
				case data:
				{
					currentPacket.addByte(inByte);
					dataByteCounter++;
					if(dataByteCounter == currentPacket.length())
						stage = packetizationStage.crc1;
					break;
				}
				case crc1:
				{
					crc1 = inByte & 0xff;
					stage = packetizationStage.crc2;
					break;
				}
				case crc2:
				{
					crc2 = inByte & 0xff;
					stage = packetizationStage.etx;
					break;
				}
				case etx:
				{
					if(Preferences.getInstance().printRawRecievePackets())
					{
						System.out.println("<-- Raw Packet");
						System.out.print(currentPacket);
						System.out.println("--> Raw Packet");
					}
					
					if(currentPacket.command() == 0x83)
						recieveQueue.put(new GetSystemValueResponse(currentPacket));
					else if(currentPacket.command() == 0xF7)
						recieveQueue.put(new GetTimeResponse(currentPacket));
					else if(currentPacket.command() == 0xB1)
						recieveQueue.put(new SI5ReadBlock(currentPacket));
					else if(currentPacket.command() == 0xD3)
					{
						if(currentPacket.length() == 1)
							recieveQueue.put(new SRRPollBufferEmptyResponse(currentPacket));
						else
							recieveQueue.put(new SIPunchRecord(currentPacket));
					}
					else if(currentPacket.command() == 0xE1)
						recieveQueue.put(new SI6ReadBlock(currentPacket));
					else if(currentPacket.command() == 0xE5)
						recieveQueue.put(new SI5Inserted(currentPacket));
					else if(currentPacket.command() == 0xE6)
						recieveQueue.put(new SI6Inserted(currentPacket));
					else if(currentPacket.command() == 0xE7)
						recieveQueue.put(new SIRemoved(currentPacket));
					else if(currentPacket.command() == 0xE8)
						recieveQueue.put(new SI89Inserted(currentPacket));
					//else if(currentPacket.command() == 0xF0)
					//	recieveQueue.put(new SetMSModeResponse(currentPacket));
					
					
					
					else
					{
						if(Preferences.getInstance().printRawRecieveUnknownPackets())
							System.out.print("Unknown Command: " + currentPacket);
					}
					stage = packetizationStage.lookingForSTX;
					break;
				}			
			}
		}
	}
	
}
