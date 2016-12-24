package com.desparddesign.orienteering.apps.PTBEmulator;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class PTBEmulator 
{
	JFrame mainFrame;
	JPanel mainPane = new JPanel();
	
	EmulatedPTBController PTBController;
	PTBChannelsPanel channelsPanel;
	PTBFrontPanel frontPanel;
	
	
	PTBEmulator(String args[])
	{
		/*
		Options options = new Options();
		options.addOption("PTB_IP", true, "PTB serial port IP");
		options.addOption("PTB_Port", true, "PTB serial port port");
		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try 
		{
			cmd = parser.parse( options, args);
		} 
		catch (ParseException e) 
		{
			System.exit(0);
		}
		String PTBSerialPortIP = "";
		String PTBSerialPortPort = "";
		
		
		if( cmd.hasOption( "PTB_IP" ) ) 
	        PTBSerialPortIP = cmd.getOptionValue( "PTB_IP");
		if( cmd.hasOption( "PTB_Port" ) ) 
	        PTBSerialPortPort = cmd.getOptionValue( "PTB_Port");
		*/
		PTBController = new EmulatedPTBController();
		channelsPanel = new PTBChannelsPanel(PTBController);
		frontPanel = new PTBFrontPanel(PTBController);
		
		SwingUtilities.invokeLater(new Runnable() 
		{
		    public void run() 
		    {	
		        createAndShowGUI();
		    }
		});
	};
	
	private void createAndShowGUI()
	{
		mainFrame = new JFrame("PTB Emualtor");
	    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = mainFrame.getContentPane(); // inherit main frame
	    con.add(mainPane); // add the panel to frame
	    
	    mainPane.setLayout(new GridLayout(0,1));
	    
	    mainPane.add(frontPanel);
	    mainPane.add(channelsPanel);
	   
	    
	    mainFrame.pack();
	    mainFrame.setVisible(true); // display this frame
	}
	
	public static void main(String args[])
	{
		@SuppressWarnings("unused")
		PTBEmulator main = new PTBEmulator(args);
		
	}
}
