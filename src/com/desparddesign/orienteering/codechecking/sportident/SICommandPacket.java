package com.desparddesign.orienteering.codechecking.sportident;

public class SICommandPacket 
{
	private int command;
	private int length;
	private byte[] data;
	private int currentByte;
	
	public SICommandPacket()
	{
		command = 0;
		length = 0;
		data = null;
		currentByte = 0;
	}
	
	public void setCommand(int com)
	{
		command = com;
	}
	
	public int command()
	{
		return command;
	}
	
	public void setLength(int len)
	{
		length = len;
		data = new byte[length];
	}
	
	public int length()
	{
		return length;
	}
	
	public void addByte(byte dataByte)
	{
		data[currentByte] = dataByte;
		currentByte++;
	}
	
	public byte[] data()
	{
		return data;
	}
	
	public String toString()
	{
		String returnString;
		returnString = 	"command: " + Integer.toHexString((command >= 0) ? command : 256 + command) + " length: " + length + "\n";			
				
		for(int i=0;i<data.length;i++)
        {
			returnString = returnString + " " + Integer.toHexString((data[i] >= 0) ? data[i] : 256 + data[i]);     	
        }
		return returnString + "\n";
	}
}
