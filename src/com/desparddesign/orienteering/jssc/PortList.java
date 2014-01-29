package com.desparddesign.orienteering.jssc;

import java.io.File;
import java.util.LinkedList;
import java.util.regex.Pattern;

import jssc.SerialNativeInterface;

public class PortList 
{
	public static LinkedList<String> createSerialPortList()
	{
		LinkedList<String> portList = null;
        
		SerialNativeInterface serialInterface = new SerialNativeInterface();
		
		if(SerialNativeInterface.getOsType() == SerialNativeInterface.OS_MAC_OS_X)
		{
			portList = getUnixBasedPortNames("/dev", Pattern.compile("tty.(serial|usbserial|usbmodem|SLAB_USBtoUART).*"));
		}
		else if(SerialNativeInterface.getOsType() == SerialNativeInterface.OS_WINDOWS)
		{
			portList = getWindowsPortNames(Pattern.compile(""), serialInterface);
		}
		else if(SerialNativeInterface.getOsType() == SerialNativeInterface.OS_LINUX)
		{
			portList = getUnixBasedPortNames("/dev", Pattern.compile("(ttyS|ttyUSB|ttyACM|ttyAMA|rfcomm|ttyO)[0-9]{1,3}"));
		}
		else
			portList = new LinkedList<String>();
        
        return portList;
	}
	
	private static LinkedList<String> getWindowsPortNames(Pattern pattern, SerialNativeInterface serialInterface) 
	{
        String[] portNames = serialInterface.getSerialPortNames();
        if(portNames == null)
        {
            return new LinkedList<String>();
        }
       
        LinkedList<String> portList = new LinkedList<String>();
        
        for(String portName : portNames)
        {
                portList.add(portName);
        }
        
        return portList;
    }
	
	private static LinkedList<String> getUnixBasedPortNames(String searchPath, Pattern pattern) 
	{
		LinkedList<String> portList = new LinkedList<String>();
		
        searchPath = (searchPath.equals("") ? searchPath : (searchPath.endsWith("/") ? searchPath : searchPath + "/"));

        File dir = new File(searchPath);
        if(dir.exists() && dir.isDirectory())
        {
            File[] files = dir.listFiles();
            if(files.length > 0)
            {
                for(File file : files)
                {
                    String fileName = file.getName();
                    if(!file.isDirectory() && !file.isFile() && pattern.matcher(fileName).find())
                    {
                        String portName = searchPath + fileName;
                        portList.add(portName);
                    }
                }
            }
        }
        return portList;
    }
}
