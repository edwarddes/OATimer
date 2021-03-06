package com.desparddesign.orienteering.timer.PTB605;

public class PTB605CommandPacket 
{
	private byte[] data;
	private int currentByte;
	
	public PTB605CommandPacket()
	{
		data = new byte[100];
		currentByte = 0;
	}
	
	public int length()
	{
		return currentByte;
	}
	
	public void addByte(byte dataByte)
	{
		if(currentByte < 100)
		{
			data[currentByte] = dataByte;
			currentByte++;
		}
	}
	
	public byte[] data()
	{
		return data;
	}
	
	public String toString()
	{
		return new String(data);
	}
}

