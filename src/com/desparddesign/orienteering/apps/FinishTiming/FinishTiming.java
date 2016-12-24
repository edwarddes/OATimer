package com.desparddesign.orienteering.apps.FinishTiming;

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

public class FinishTiming 
{
	JFrame mainFrame;
	JPanel mainPane = new JPanel();
	
	PTBController ptbController;
	PTBController ptbControllerSecondary;
	OEController oeController;
	LogController logController;
	
	LogPanel logPanel;
	OEPanel oePanel;
	PTBPanel primaryPTBPanel;
	PTBPanel secondaryPTBPanel;
	
	FinishTiming(String args[])
	{
		Options options = new Options();
		options.addOption("PTB_IP", true, "PTB serial port IP");
		options.addOption("PTB_Port", true, "PTB serial port port");
		options.addOption("OESI_IP", true, "OE SI serial port IP");
		options.addOption("OESI_Port", true, "OE SI serial port port");
		options.addOption("OEAlge_IP", true, "OE Alge serial port IP");
		options.addOption("OEAlge_Port", true, "OE Alge serial port port");
		
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
		
		String OESISerialPortIP = null;
		String OESISerialPortPort = null;
		
		String OEAlgeSerialPortIP = null;
		String OEAlgeSerialPortPort = null;
		
		if( cmd.hasOption( "PTB_IP" ) ) 
	        PTBSerialPortIP = cmd.getOptionValue( "PTB_IP");
		if( cmd.hasOption( "PTB_Port" ) ) 
	        PTBSerialPortPort = cmd.getOptionValue( "PTB_Port");
		
		if( cmd.hasOption( "OESI_IP" ) ) 
			OESISerialPortIP = cmd.getOptionValue( "OESI_IP");
		if( cmd.hasOption( "OESI_Port" ) ) 
			OESISerialPortPort = cmd.getOptionValue( "OESI_Port");
		if( cmd.hasOption( "OEAlge_IP" ) ) 
			OEAlgeSerialPortIP = cmd.getOptionValue( "OEAlge_IP");
		if( cmd.hasOption( "OEAlge_Port" ) ) 
			OEAlgeSerialPortPort = cmd.getOptionValue( "OEAlge_Port");
		
		logController = new LogController();
		logPanel = new LogPanel(logController);
		logController.setLogPanel(logPanel);
		
		oeController = new OEController(logController,OESISerialPortIP,OESISerialPortPort,OEAlgeSerialPortIP,OEAlgeSerialPortPort);
		oePanel = new OEPanel(oeController);
		
    	ptbController = new PTBController(logController,oeController,"PTB Primary",PTBSerialPortIP,PTBSerialPortPort);
    	ptbControllerSecondary = new PTBController(logController,null,"PTB Secondary",null,null);
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
	    tabbedPane.addTab("OE",oePanel);
	    
	    //mainPane.add(tabbedPane);
	    //mainPane.add(logController.getComponent());
	    
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
		FinishTiming main = new FinishTiming(args);
		
	}
}
