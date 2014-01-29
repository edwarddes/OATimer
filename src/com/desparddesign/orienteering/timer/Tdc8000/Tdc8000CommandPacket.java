package com.desparddesign.orienteering.timer.Tdc8000;

public class Tdc8000CommandPacket 
{
	private byte[] data;
	private int currentByte;
	
	public Tdc8000CommandPacket()
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
