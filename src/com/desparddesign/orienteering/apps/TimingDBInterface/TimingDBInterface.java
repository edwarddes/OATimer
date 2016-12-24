package com.desparddesign.orienteering.apps.TimingDBInterface;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TimingDBInterface 
{
	JFrame mainFrame;
	JPanel mainPane = new JPanel();
	
	PTBController ptbController;
	PTBController ptbControllerSecondary;
	LogController logController;
	ImpulseController impulseController;
	DBController databaseController;
	
	LogPanel logPanel;
	PTBPanel primaryPTBPanel;
	PTBPanel secondaryPTBPanel;
	DBPanel dbPanel;
	
	TimingDBInterface(String args[])
	{
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
		String PTBSerialPortIP = null;
		String PTBSerialPortPort = null;
		
		if( cmd.hasOption( "PTB_IP" ) ) 
	        PTBSerialPortIP = cmd.getOptionValue( "PTB_IP");
		if( cmd.hasOption( "PTB_Port" ) ) 
	        PTBSerialPortPort = cmd.getOptionValue( "PTB_Port");
		
		logController = new LogController();
		logPanel = new LogPanel(logController);
		logController.setLogPanel(logPanel);
		
		databaseController = new DBController(logController,1);
		dbPanel = new DBPanel(databaseController);
		
		impulseController = new ImpulseController(databaseController);
		
    	ptbController = new PTBController(logController,impulseController,"PTB Primary",PTBSerialPortIP,PTBSerialPortPort);
    	ptbControllerSecondary = new PTBController(logController,impulseController,"PTB Secondary",null,null);
    	primaryPTBPanel = new PTBPanel(ptbController);
    	secondaryPTBPanel = new PTBPanel(ptbControllerSecondary);
		
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
		mainFrame = new JFrame("OE/PTB605 Timing");
	    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = mainFrame.getContentPane(); // inherit main frame
	    con.add(mainPane); // add the panel to frame
	    
	    JTabbedPane tabbedPane = new JTabbedPane();
	    tabbedPane.addTab("PTB Primary", primaryPTBPanel);
	    tabbedPane.addTab("PTB Secondary", secondaryPTBPanel);
	    tabbedPane.addTab("Database",dbPanel);
	    
	    JSplitPane topBottomSplit;
	    topBottomSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, logPanel);
	    topBottomSplit.setDividerLocation(220);
	    
	    mainPane.add(topBottomSplit);
	  
	    
	    mainFrame.pack();
	    mainFrame.setVisible(true); // display this frame
	}
	
	public static void main(String args[])
	{
		@SuppressWarnings("unused")
		TimingDBInterface main = new TimingDBInterface(args);
		
	}
}
