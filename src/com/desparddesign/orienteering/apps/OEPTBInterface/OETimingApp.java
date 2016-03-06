package com.desparddesign.orienteering.apps.OEPTBInterface;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.desparddesign.orienteering.codechecking.sportident.commands.SICommand;
import com.desparddesign.orienteering.codechecking.sportident.commands.SIPunchRecord;
import com.desparddesign.orienteering.jssc.PortList;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.TimingResponse;
import com.desparddesign.orienteering.timer.PTB605v13.commands.PTB605Command;
import com.desparddesign.orienteering.timer.PTB605v13.commands.controlCommands.ClearMemory;
import com.desparddesign.orienteering.timer.PTB605v13.commands.controlCommands.NewSession;
import com.desparddesign.orienteering.timer.PTB605v13.commands.queryCommands.GetDateTime;
import com.desparddesign.orienteering.timer.PTB605v13.commands.parameterCommands.SetDateTime;
import com.desparddesign.orienteering.timer.PTB605v13.commands.responses.GetDateTimeResponse;
import com.desparddesign.orienteering.timer.Tdc8000.Tdc8000CommandPacket;

public class OETimingApp
{
	JFrame mainFrame;
	JPanel mainPane = new JPanel();
	
	JSplitPane topBottomSplit;
	JPanel topPane;
	JPanel bottomPane;
	
	JPanel serialPortSelectionPanel;
	JPanel PTBConnectPanel;
	JPanel PTBControlPanel;
	JPanel connectToSIPanel;
	JPanel connectToOEPanel;
	
	JTextArea bottomText;
	
	com.desparddesign.orienteering.timer.PTB605v13.transport.Transport PTBSerialPort = null;
	com.desparddesign.orienteering.codechecking.sportident.transport.Transport SISerialPort = null;
	
	com.desparddesign.orienteering.codechecking.sportident.transport.Transport OESISerialPort = null;
	com.desparddesign.orienteering.timer.Tdc8000.transport.Transport OEAlgeSerialPort = null;
	
	Thread PTBMonitorThread;
	Thread SIMonitorThread;
	Thread OESIThread;
	Thread OEAlgeThread;
	
	Integer lastSICard = 0;
	LocalTime lastSICardPunchTime = new LocalTime();
	LocalTime impulseTime = new LocalTime();
	
	private synchronized Integer getLastSICard(){return lastSICard;}	
	private synchronized void setLastSICard(Integer card){lastSICard = card;}
	
	private synchronized LocalTime getLastSICardPunchTime(){return lastSICardPunchTime;}
	private synchronized void setLastSICardPunchTime(LocalTime time){lastSICardPunchTime = time;}
	
	private synchronized LocalTime getImpulseTime(){return impulseTime;}
	private synchronized void setImpulseTime(LocalTime time){impulseTime = time;}
	
	LinkedList<String> serialPorts;
	
	String PTBSerialPortName = "";
	String PTBSerialPortIP = "";
	String PTBSerialPortPort = "";
	
	String SISerialPortName = "";
	String SISerialPortIP = "";
	String SISerialPortPort = "";
	
	String OESISerialPortName = "";
	String OESISerialPortIP = "";
	String OESISerialPortPort = "";
	
	String OEAlgeSerialPortName = "";
	String OEAlgeSerialPortIP = "";
	String OEAlgeSerialPortPort = "";
	

	public void enableComponents(Container container, boolean enable) 
	{
        Component[] components = container.getComponents();
        for (Component component : components) 
        {
            component.setEnabled(enable);
            if (component instanceof Container) 
            {
                enableComponents((Container)component, enable);
            }
        }
    }
	
	OETimingApp(String args[])
	{
		Options options = new Options();
		options.addOption("PTB_IP", true, "PTB serial port IP");
		options.addOption("PTB_Port", true, "PTB serial port port");
		options.addOption("SI_IP", true, "SI serial port IP");
		options.addOption("SI_Port", true, "SI serial port port");
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
		
		if( cmd.hasOption( "PTB_IP" ) ) 
	        PTBSerialPortIP = cmd.getOptionValue( "PTB_IP");
		if( cmd.hasOption( "PTB_Port" ) ) 
	        PTBSerialPortPort = cmd.getOptionValue( "PTB_Port");
		if( cmd.hasOption( "SI_IP" ) ) 
			SISerialPortIP = cmd.getOptionValue( "SI_IP");
		if( cmd.hasOption( "SI_Port" ) ) 
			SISerialPortPort = cmd.getOptionValue( "SI_Port");
		if( cmd.hasOption( "OESI_IP" ) ) 
			OESISerialPortIP = cmd.getOptionValue( "OESI_IP");
		if( cmd.hasOption( "OESI_Port" ) ) 
			OESISerialPortPort = cmd.getOptionValue( "OESI_Port");
		if( cmd.hasOption( "OEAlge_IP" ) ) 
			OEAlgeSerialPortIP = cmd.getOptionValue( "OEAlge_IP");
		if( cmd.hasOption( "OEAlge_Port" ) ) 
			OEAlgeSerialPortPort = cmd.getOptionValue( "OEAlge_Port");
		
		serialPorts = PortList.createSerialPortList();
		
		SwingUtilities.invokeLater(new Runnable() 
		{
		    public void run() 
		    {
		    	createSerialPortSelectionPanel();
		    	
		    	createPTBConnectButtons();
		    	createPTBControlButtons();
		    	enableComponents(PTBControlPanel, false);
		    	
		    	createSIConnectButtons();
		    	
		    	createOEConnectButtons();
		    	
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
  
	    topPane = new JPanel();
	    topPane.setLayout(new BoxLayout(topPane, BoxLayout.LINE_AXIS));
	    bottomPane = new JPanel(new FlowLayout());
	    
	    Dimension minimumSize = new Dimension(0, 0);
	    topPane.setMinimumSize(minimumSize);
	    bottomPane.setMinimumSize(minimumSize);
	    topPane.setPreferredSize(new Dimension(1000,200));
	    bottomPane.setPreferredSize(new Dimension(1000,200));
	    
	    
	    bottomText = new JTextArea("",10,40);
	    DefaultCaret caret = (DefaultCaret)bottomText.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    bottomText.setVisible(true);
	    
	    topPane.add(Box.createHorizontalGlue());
	    topPane.add(serialPortSelectionPanel);
	    topPane.add(Box.createRigidArea(new Dimension(5,0)));
	    topPane.add(PTBConnectPanel);
	    topPane.add(Box.createRigidArea(new Dimension(5,0)));
	    topPane.add(PTBControlPanel);
	    topPane.add(Box.createRigidArea(new Dimension(5,0)));    
	    topPane.add(connectToSIPanel);
	    topPane.add(Box.createRigidArea(new Dimension(5,0)));
	    topPane.add(connectToOEPanel);
	    topPane.add(Box.createHorizontalGlue());
	    
	    bottomPane.add(new JScrollPane(bottomText));
	    
	    topBottomSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPane, bottomPane);
	    topBottomSplit.setDividerLocation(250);
	    
	    mainPane.add(topBottomSplit);
	    
	    mainFrame.pack();
	    mainFrame.setVisible(true); // display this frame
	}
	
	private void createSerialPortSelectionPanel()
	{
		JComboBox PTBSerialSelector = new JComboBox(serialPorts.toArray());
		PTBSerialSelector.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
		        JComboBox cb = (JComboBox)e.getSource();
		        PTBSerialPortName = (String)cb.getSelectedItem();        
		    }	
		});
		
		JComboBox SISerialSelector = new JComboBox(serialPorts.toArray());
		SISerialSelector.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
		        JComboBox cb = (JComboBox)e.getSource();
		        SISerialPortName = (String)cb.getSelectedItem();        
		    }	
		});
		
		JComboBox OESISerialSelector = new JComboBox(serialPorts.toArray());
		OESISerialSelector.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
		        JComboBox cb = (JComboBox)e.getSource();
		        OESISerialPortName = (String)cb.getSelectedItem();        
		    }	
		});
		
		JComboBox OEAlgeSerialSelector = new JComboBox(serialPorts.toArray());
		OEAlgeSerialSelector.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
		        JComboBox cb = (JComboBox)e.getSource();
		        OEAlgeSerialPortName = (String)cb.getSelectedItem();        
		    }	
		});
		
		JTextField PTBIPText = new JTextField(15);
		if(!PTBSerialPortIP.equals(""))
			PTBIPText.setText(PTBSerialPortIP);
		PTBIPText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				PTBSerialPortIP = e.getActionCommand();
			}
		});
		
		JTextField PTBPortText = new JTextField(4);
		if(!PTBSerialPortPort.equals(""))
			PTBPortText.setText(PTBSerialPortPort);
		PTBPortText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				PTBSerialPortPort = e.getActionCommand();
			}
		});
		
		JTextField SIIPText = new JTextField(15);
		if(!SISerialPortIP.equals(""))
			SIIPText.setText(SISerialPortIP);
		SIIPText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				SISerialPortIP = e.getActionCommand();
			}
		});
		
		JTextField SIPortText = new JTextField(4);
		if(!SISerialPortPort.equals(""))
			SIPortText.setText(SISerialPortPort);
		SIPortText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				SISerialPortPort = e.getActionCommand();
			}
		});
		
		JTextField OESIIPText = new JTextField(15);
		if(!OESISerialPortIP.equals(""))
			OESIIPText.setText(OESISerialPortIP);
		OESIIPText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				OESISerialPortIP = e.getActionCommand();
			}
		});
		
		JTextField OESIPortText = new JTextField(4);
		if(!OESISerialPortPort.equals(""))
			OESIPortText.setText(OESISerialPortPort);
		OESIPortText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				OESISerialPortPort = e.getActionCommand();
			}
		});
		
		
		JTextField OEAlgeIPText = new JTextField(15);
		if(!OEAlgeSerialPortIP.equals(""))
			OEAlgeIPText.setText(OEAlgeSerialPortIP);
		OEAlgeIPText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				OEAlgeSerialPortIP = e.getActionCommand();
			}
		});
		
		JTextField OEAlgePortText = new JTextField(4);
		if(!OEAlgeSerialPortPort.equals(""))
			OEAlgePortText.setText(OEAlgeSerialPortPort);
		OEAlgePortText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				OEAlgeSerialPortPort = e.getActionCommand();
			}
		});
		
		
		serialPortSelectionPanel = new JPanel();
		serialPortSelectionPanel.setLayout(new BoxLayout(serialPortSelectionPanel, BoxLayout.PAGE_AXIS));
		serialPortSelectionPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		JPanel PTBIPSelectionPanel = new JPanel();
		PTBIPSelectionPanel.setLayout(new BoxLayout(PTBIPSelectionPanel, BoxLayout.LINE_AXIS));
		//JTextField PTBIPText = new JTextField(15);
		//JTextField PTBPortText = new JTextField(4);
		PTBIPSelectionPanel.add(new JLabel("PTB"));
		PTBIPSelectionPanel.add(PTBIPText);
		PTBIPSelectionPanel.add(PTBPortText);
		serialPortSelectionPanel.add(PTBIPSelectionPanel);
		serialPortSelectionPanel.add(PTBSerialSelector);
		
		JPanel SIIPSelectionPanel = new JPanel();
		SIIPSelectionPanel.setLayout(new BoxLayout(SIIPSelectionPanel, BoxLayout.LINE_AXIS));
		//JTextField SIIPText = new JTextField(15);
		//JTextField SIPortText = new JTextField(4);
		SIIPSelectionPanel.add(new JLabel("SI"));
		SIIPSelectionPanel.add(SIIPText);
		SIIPSelectionPanel.add(SIPortText);
		serialPortSelectionPanel.add(SIIPSelectionPanel);
		serialPortSelectionPanel.add(SISerialSelector);
		
		JPanel OESIIPSelectionPanel = new JPanel();
		OESIIPSelectionPanel.setLayout(new BoxLayout(OESIIPSelectionPanel, BoxLayout.LINE_AXIS));
		//JTextField OESIIPText = new JTextField(15);
		//JTextField OESIPortText = new JTextField(4);
		OESIIPSelectionPanel.add(new JLabel("OE SI"));
		OESIIPSelectionPanel.add(OESIIPText);
		OESIIPSelectionPanel.add(OESIPortText);
		serialPortSelectionPanel.add(OESIIPSelectionPanel);
		serialPortSelectionPanel.add(OESISerialSelector);
		
		JPanel OEAlgeIPSelectionPanel = new JPanel();
		OEAlgeIPSelectionPanel.setLayout(new BoxLayout(OEAlgeIPSelectionPanel, BoxLayout.LINE_AXIS));
		//JTextField OEAlgeIPText = new JTextField(15);
		//JTextField OEAlgePortText = new JTextField(4);
		OEAlgeIPSelectionPanel.add(new JLabel("OE Alge"));
		OEAlgeIPSelectionPanel.add(OEAlgeIPText);
		OEAlgeIPSelectionPanel.add(OEAlgePortText);
		serialPortSelectionPanel.add(OEAlgeIPSelectionPanel);
		serialPortSelectionPanel.add(OEAlgeSerialSelector);
		
		serialPortSelectionPanel.add(Box.createVerticalGlue());
		
	}
	
	private void createOEConnectButtons()
	{
		final JButton connectToOESI;
		final JButton connectToOEAlge;
		
		final JButton disconnectOESI;
		final JButton disconnectOEAlge;
		
		connectToOESI = new JButton("Connect To OE SI");
		connectToOEAlge = new JButton("Connect To OE Alge");
		disconnectOESI = new JButton("Disconnect OE SI");
		disconnectOEAlge = new JButton("Disconnect OE Alge");
		
		
		connectToOESI.setEnabled(true);
		connectToOEAlge.setEnabled(true);
		disconnectOESI.setEnabled(false);
		disconnectOEAlge.setEnabled(false);
		
		connectToOESI.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				try
				{
					if(OESISerialPort == null)
					{
						if(OESISerialPortIP.equals("") && OESISerialPortPort.equals(""))
						{
							OESISerialPort = new com.desparddesign.orienteering.codechecking.sportident.transport.jsscSerialManager();
							OESISerialPort.connect(OESISerialPortName,0,false);
						}
						else
						{
							OESISerialPort = new com.desparddesign.orienteering.codechecking.sportident.transport.TelnetSerialManager();
							OESISerialPort.connect(OESISerialPortIP,Integer.parseInt(OESISerialPortPort),false);
						}
					}
					else
					{	
						if(OESIThread != null)
						{
							OESIThread.interrupt();
							OESIThread.join();
							OESIThread = null;
						}
						
						OESISerialPort.disconnect();
						
						if(OESISerialPortIP.equals("") && OESISerialPortPort.equals(""))
							{}//OEAlgeSerialPort.connect(OEAlgeSerialPortName,9600);
						else
							OESISerialPort.connect(OESISerialPortIP,Integer.parseInt(OESISerialPortPort),true);
					}
					
					bottomText.append("Connected to OE SI\n");
					
					OESIThread = new Thread()
					{			
						
					    public void run()
					    {    
					    	try
					    	{
					    		while(true)
					    		{
						    		Thread.sleep(10);
					    		}
					    	}
					    	catch(InterruptedException ex)
					    	{
					    		//exit the thread
					    	}
					    	catch(Exception e)
					    	{
					    		e.printStackTrace();
					    	}
					    }
					};
					OESIThread.start();
					connectToOESI.setEnabled(false);
					disconnectOESI.setEnabled(true);
					
				}
				catch(Exception ex)
				{
					//from connect/disconnect
					bottomText.append("Error connecting to OE SI\n");
					//OESISerialPort.disconnect();
					OESISerialPort = null;
					
					OESIThread = null;
				}
			}
	    });
		
		
		connectToOEAlge.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				try
				{
					if(OEAlgeSerialPort == null)
					{
						if(OEAlgeSerialPortIP.equals("") && OEAlgeSerialPortPort.equals(""))
						{
							//OEAlgeSerialPort = new com.desparddesign.orienteering.timing.Tdc8000.transport.jsscSerialManager();
							//OEAlgeSerialPort.connect(OEAlgeSerialPortName,9600);
						}
						else
						{
							OEAlgeSerialPort = new com.desparddesign.orienteering.timer.Tdc8000.transport.TelnetSerialManager();
							OEAlgeSerialPort.connect(OEAlgeSerialPortIP,Integer.parseInt(OEAlgeSerialPortPort));
						}
					}
					else
					{
						if(OEAlgeThread != null)
						{
							OEAlgeThread.interrupt();
							OEAlgeThread.join();
							OEAlgeThread = null;
						}
						
						OEAlgeSerialPort.disconnect();	
									
						if(OEAlgeSerialPortIP.equals("") && OEAlgeSerialPortPort.equals(""))
							{}//OEAlgeSerialPort.connect(OEAlgeSerialPortName,9600);
						else
							OEAlgeSerialPort.connect(OEAlgeSerialPortIP,Integer.parseInt(OEAlgeSerialPortPort));
					}
					
					bottomText.append("Connected to OE Alge\n");
					
					OEAlgeThread = new Thread()
					{			
						
					    public void run()
					    {    
					    	try
					    	{
					    		while(true)
					    		{
						    		Thread.sleep(10);
					    		}
					    	}
					    	catch(InterruptedException ex)
					    	{
					    		//exit the thread
					    	}
					    	catch(Exception e)
					    	{
					    		e.printStackTrace();
					    	}
					    }
					};
					OEAlgeThread.start();
					connectToOEAlge.setEnabled(false);
					disconnectOEAlge.setEnabled(true);
				}
				catch(Exception ex)
				{
					bottomText.append("Error connecting to OE Alge\n");
					//OESISerialPort.disconnect();
					OEAlgeSerialPort = null;
					
					OEAlgeThread = null;
				}
			}
	    });
		
		
		disconnectOESI.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				try
				{
					if(OESIThread != null)
					{
						OESIThread.interrupt();
						OESIThread.join();
						OESIThread = null;
					}
					
					OESISerialPort.disconnect();
					OESISerialPort = null;
					
					bottomText.append("Disconnected from OE SI\n");
					
					connectToOESI.setEnabled(true);
					disconnectOESI.setEnabled(false);
				}
				catch(Exception ex)
				{
					
				}
			}
	    });
		
		
		disconnectOEAlge.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				try
				{
					if(OEAlgeThread != null)
					{
						OEAlgeThread.interrupt();
						OEAlgeThread.join();
						OEAlgeThread = null;
					}
					
					OEAlgeSerialPort.disconnect();
					OEAlgeSerialPort = null;
					
					bottomText.append("Disconnected from OE Alge\n");
					
					connectToOEAlge.setEnabled(true);
					disconnectOEAlge.setEnabled(false);
				}
				catch(Exception ex)
				{
					//disconnect failed
				}
			}
	    });
		
		connectToOEPanel = new JPanel();
		connectToOEPanel.setLayout(new BoxLayout(connectToOEPanel, BoxLayout.PAGE_AXIS));
		connectToOEPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		connectToOEPanel.add(connectToOESI);
		connectToOEPanel.add(connectToOEAlge);
		connectToOEPanel.add(Box.createRigidArea(new Dimension(0,5)));
		connectToOEPanel.add(disconnectOESI);
		connectToOEPanel.add(disconnectOEAlge);
		connectToOEPanel.add(Box.createVerticalGlue());
	}
	
	private void createSIConnectButtons()
	{
		final JButton connectToSI;
		final JButton disconnectSI;
		
		connectToSI = new JButton("Connect To SI");
		disconnectSI = new JButton("Disconnect SI");
		
		connectToSI.setEnabled(true);
		disconnectSI.setEnabled(false);
		
		connectToSI.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				try
				{
					if(SISerialPort == null)
					{	
						if(SISerialPortIP.equals("") && SISerialPortPort.equals(""))
						{
							SISerialPort = new com.desparddesign.orienteering.codechecking.sportident.transport.jsscSerialManager();
							SISerialPort.connect(SISerialPortName,0,true);
						}
						else
						{
							SISerialPort = new com.desparddesign.orienteering.codechecking.sportident.transport.TelnetSerialManager();
							SISerialPort.connect(SISerialPortIP,Integer.parseInt(SISerialPortPort),false);
						}
					}
					else
					{
						SISerialPort.disconnect();	
						
						if(SIMonitorThread != null)
						{
							SIMonitorThread.interrupt();
							SIMonitorThread.join();
							SIMonitorThread = null;
						}
						
						if(SISerialPortIP.equals("") && SISerialPortPort.equals(""))
							SISerialPort.connect(SISerialPortName,0, true);
						else
							SISerialPort.connect(SISerialPortIP,Integer.parseInt(SISerialPortPort),false);
					}
					
					bottomText.append("Connected to SI\n");
					
					SIMonitorThread = new Thread()
					{			
					    public void run()
					    {    
					    	try
					    	{
					    		while(true)
					    		{
					    			if(!SISerialPort.reader.recievePacketQueue().isEmpty())
									{
										SICommand command = SISerialPort.reader.recievePacketQueue().take();
										if(command instanceof SIPunchRecord)
										{
											SIPunchRecord record = (SIPunchRecord)command;
											if(record.control == 1)
											{
												//lastSICard = ((SIPunchRecord)command).cardNumber;
												setLastSICard(((SIPunchRecord)command).cardNumber);
												//lastSICardPunchTime = ((SIPunchRecord)command).punchTime;
												setLastSICardPunchTime(((SIPunchRecord)command).punchTime);
												
												final String SIRecord = new String(
													"SI Lane 1 " + 
													getLastSICard() + 
													"-" + 
													getLastSICardPunchTime().toString("HH:mm:ss.SSS"));
												
												if(PTBSerialPort != null)
													PTBSerialPort.printString(SIRecord);
												
												SwingUtilities.invokeLater(new Runnable()
									    		{ 
									    			public void run()
									                {
									    				bottomText.append(SIRecord + "\n");
									                }
									    		});
											}
											else if(record.control == 5)
											{
												final String SILateRecord = new String(
														"SI Late Start " + 
														record.cardNumber + 
														"-" + 
														record.punchTime.toString("HH:mm:ss.SSS"));
												
												if(PTBSerialPort != null)
													PTBSerialPort.printString(SILateRecord);
												
												SwingUtilities.invokeLater(new Runnable()
									    		{ 
									    			public void run()
									                {
									    				bottomText.append(SILateRecord + "\n");
									                }
									    		});
											}
											else
											{
												final String SIUnknown = new String(
														"SI Unknown " + 
														record.cardNumber + 
														"-" + 
														record.punchTime.toString("HH:mm:ss.SSS"));
												
												if(PTBSerialPort != null)
													PTBSerialPort.printString(SIUnknown);
												
												SwingUtilities.invokeLater(new Runnable()
									    		{ 
									    			public void run()
									                {
									    				bottomText.append(SIUnknown + "\n");
									                }
									    		});
											}
											
										}
									}
						    		Thread.sleep(10);
					    		}
					    	}
					    	catch(InterruptedException ex)
					    	{
					    		//exit the thread
					    	}
					    	catch(Exception e)
					    	{
					    		e.printStackTrace();
					    	}
					    }
					};
					SIMonitorThread.start();
					
					connectToSI.setEnabled(false);
					disconnectSI.setEnabled(true);
				}
				catch(Exception ex)
				{
					bottomText.append("Error connecting to SI\n");
					try {
						SISerialPort.disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SISerialPort = null;
					
					SIMonitorThread = null;
				}
			}
	    });
		
		disconnectSI.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				try
				{
					if(SIMonitorThread != null)
					{
						SIMonitorThread.interrupt();
						SIMonitorThread.join();
						SIMonitorThread = null;
					}
					
					SISerialPort.disconnect();
					SISerialPort = null;
					
					bottomText.append("Disconnected from SI\n");
					
					connectToSI.setEnabled(true);
					disconnectSI.setEnabled(false);
				}
				catch(Exception ex)
				{
					
				}
			}
	    });
		
		connectToSIPanel = new JPanel();
		connectToSIPanel.setLayout(new BoxLayout(connectToSIPanel, BoxLayout.PAGE_AXIS));
		connectToSIPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		connectToSIPanel.add(connectToSI);
		connectToSIPanel.add(disconnectSI);
		connectToSIPanel.add(Box.createVerticalGlue());
	}
	
	private void createPTBConnectButtons()
	{
		final JButton connectToPTB;
		final JButton disconnectPTB;
		final JButton syncToPTB;
		
		connectToPTB = new JButton("Connect To PTB");
		disconnectPTB = new JButton("Disconnect PTB");
		syncToPTB = new JButton("SyncToPTB");
		
		connectToPTB.setEnabled(true);
		disconnectPTB.setEnabled(false);
		syncToPTB.setEnabled(false);
		
	    connectToPTB.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				try
				{
					if(PTBSerialPort == null)
					{
						if(PTBSerialPortIP.equals("") && PTBSerialPortPort.equals(""))
						{
							PTBSerialPort = new com.desparddesign.orienteering.timer.PTB605v13.transport.jsscSerialManager();
							PTBSerialPort.connect(PTBSerialPortName,9600);
						}
						else
						{
							PTBSerialPort = new com.desparddesign.orienteering.timer.PTB605v13.transport.TelnetSerialManager();
							PTBSerialPort.connect(PTBSerialPortIP,Integer.parseInt(PTBSerialPortPort));
						}
					}
					else
					{
						PTBSerialPort.disconnect();
						
						if(PTBMonitorThread != null)
						{
							PTBMonitorThread.interrupt();
							PTBMonitorThread.join();
							PTBMonitorThread = null;
						}
						
						if(PTBSerialPortIP.equals("") && PTBSerialPortPort.equals(""))
							PTBSerialPort.connect(PTBSerialPortName,9600);
						else
							PTBSerialPort.connect(PTBSerialPortIP,Integer.parseInt(PTBSerialPortPort));
					}
					
					bottomText.append("Connected to PTB\n");
					
					connectToPTB.setEnabled(false);
					disconnectPTB.setEnabled(true);
					syncToPTB.setEnabled(true);
					enableComponents(PTBControlPanel, true);
				}
				catch(Exception ex)
				{
					bottomText.append("Error connecting to PTB\n");
					try {
						PTBSerialPort.disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					PTBSerialPort = null;
					
					PTBMonitorThread = null;
				}
			}
	    });
	    
	    disconnectPTB.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				try
				{
					if(PTBMonitorThread != null)
					{
						PTBMonitorThread.interrupt();
						PTBMonitorThread.join();
						PTBMonitorThread = null;
					}
					
					PTBSerialPort.disconnect();
					PTBSerialPort = null;
					
					bottomText.append("Disconnected from PTB\n");
					
					connectToPTB.setEnabled(true);
					disconnectPTB.setEnabled(false);
					syncToPTB.setEnabled(false);
					enableComponents(PTBControlPanel, false);
				}
				catch(Exception ex)
				{
					
				}
			}
	    });
	    
	    syncToPTB.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				try
				{
					if(PTBMonitorThread != null)
					{
						PTBMonitorThread.interrupt();
						PTBMonitorThread.join();
						PTBMonitorThread = null;
					}
					
					PTBSerialPort.writer.packetizer.dePacketize((new GetDateTime()).rawPacket());
				
					while(true)
					{
						if(PTBSerialPort.reader.recievePacketQueue().isEmpty())
						{
							Thread.sleep(500);
							PTBSerialPort.writer.packetizer.dePacketize((new GetDateTime()).rawPacket());
						}
						else
						{
							PTB605Command command = PTBSerialPort.reader.recievePacketQueue().take();
							if(command instanceof GetDateTimeResponse)
								break;
						}
					}
					bottomText.append("Synced to PTB\n");
					
					PTBMonitorThread = new Thread()
					{						
					    public void run()
					    {    
					    	try
					    	{
					    		while(true)
					    		{
						    		final PTB605Command command = PTBSerialPort.reader.recievePacketQueue().take();
						    		if(command != null)
						    		{
							    		SwingUtilities.invokeLater(new Runnable()
							    		{ 
							    			public void run()
							                {
							    				bottomText.append(command + "\n");
							                }
							    		});
						    		}
						    		
						    		if(command instanceof TimingResponse)
									{
										//start gate impulse
										if(((TimingResponse)command).channel == 1)
										{
											setImpulseTime(((TimingResponse)command).time);
											
											if(getLastSICard() != 0)
											{
												//System.out.println("Card: " + lastSICard);
												//System.out.println("Card Time: " + lastSICardPunchTime);
												//System.out.println("Impulse Time: " + impulseTime);
												//System.out.println("");
												
												SIPunchRecord recordToSend = new SIPunchRecord();
												recordToSend.cardNumber = getLastSICard();
												recordToSend.control = 1;
												recordToSend.punchTime = getImpulseTime();
												
												//System.out.println(recordToSend);
												if(OESISerialPort != null)
													OESISerialPort.writer.packetizer.dePacketize(recordToSend.rawPacket());
												
												//once we use the SI punch, reset it so subsequent pulses will not have
												//a competitor assigned to them.
												setLastSICard(0);
												setImpulseTime(null);
											
											}
											else
											{								
												SIPunchRecord recordToSend = new SIPunchRecord();
												recordToSend.cardNumber = 0;
												recordToSend.control = 1;
												recordToSend.punchTime = getImpulseTime();
												
												final String unknownStartImpulse = new String("Start impulse with unknown SI");
												
												if(PTBSerialPort != null)
													PTBSerialPort.printString(unknownStartImpulse);
												
												SwingUtilities.invokeLater(new Runnable()
									    		{ 
									    			public void run()
									                {
									    				bottomText.append(unknownStartImpulse + "\n");
									                }
									    		});
												
												if(OESISerialPort != null)
													OESISerialPort.writer.packetizer.dePacketize(recordToSend.rawPacket());
											
											}
										}
										//photocell finish impulse
										else if(((TimingResponse)command).channel == 4)
										{
											LocalTime finishTime = ((TimingResponse)command).time;
											
											Tdc8000CommandPacket Tdc8000TimingPacket = 
												new com.desparddesign.orienteering.timer.Tdc8000.commands.TimingResponse(
													com.desparddesign.orienteering.timer.Tdc8000.commands.TimingResponse.MODE.TIMENOBIB,
													0, 1, false, finishTime, "00"
											).rawPacket();
												
											if(OEAlgeSerialPort != null)
												OEAlgeSerialPort.writer.packetizer.dePacketize(Tdc8000TimingPacket);
										}
										//manual plunger impulse
										else
										{
											LocalTime startTime = ((TimingResponse)command).time;
											
											Tdc8000CommandPacket Tdc8000TimingPacket = 
												new com.desparddesign.orienteering.timer.Tdc8000.commands.TimingResponse(
													com.desparddesign.orienteering.timer.Tdc8000.commands.TimingResponse.MODE.TIMENOBIB,
													0, 0, false, startTime, "00"
											).rawPacket();
												
											if(OEAlgeSerialPort != null)
												OEAlgeSerialPort.writer.packetizer.dePacketize(Tdc8000TimingPacket);
										}
										
									}
						    		Thread.sleep(10);
					    		}
					    	}
					    	catch(InterruptedException ex)
					    	{
					    		//exit the thread
					    	}
					    	catch(Exception e)
					    	{
					    		e.printStackTrace();
					    	}
					    }
					};
					PTBMonitorThread.start();
					
				}
				catch(Exception ex)
				{
					bottomText.append("Error syncing to PTB\n");
					try {
						PTBSerialPort.disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					PTBSerialPort = null;
					
					PTBMonitorThread = null;
				}
				
			}
	    });
	    
	    PTBConnectPanel = new JPanel();
	    PTBConnectPanel.setLayout(new BoxLayout(PTBConnectPanel, BoxLayout.PAGE_AXIS));
	    PTBConnectPanel.setBorder(BorderFactory.createLoweredBevelBorder());
	    PTBConnectPanel.add(connectToPTB);
	    PTBConnectPanel.add(disconnectPTB);
	    PTBConnectPanel.add(syncToPTB);
	    PTBConnectPanel.add(Box.createVerticalGlue());
	}
	  
	private void createPTBControlButtons()
	{
		JButton newPTBSession;
		JButton clearPTBMemory;
		JButton setPTBDateTime;
		
		setPTBDateTime = new JButton("Set PTB Date/Time");
		setPTBDateTime.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event) 
			{
				try 
				{
					PTBSerialPort.writer.packetizer.dePacketize((new SetDateTime((new LocalDateTime()).plusMinutes(1))).rawPacket());
				} 
				catch (InterruptedException e) 
				{
					//unsuccessful
				}
			}
		});
		
		newPTBSession = new JButton("New Session");
	    newPTBSession.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				try 
				{
					PTBSerialPort.writer.packetizer.dePacketize((new NewSession()).rawPacket());
				} 
				catch (InterruptedException e) 
				{
					//unsuccessful
				}
			}
	    });
	    
	    clearPTBMemory = new JButton("Clear Memory");
	    clearPTBMemory.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				try 
				{
					PTBSerialPort.writer.packetizer.dePacketize((new ClearMemory()).rawPacket());
					SwingUtilities.invokeLater(new Runnable()
		    		{ 
		    			public void run()
		                {
		    				bottomText.append("PTB memory cleared\n");
		                }
		    		});
				} 
				catch (InterruptedException e) 
				{
					//unsuccessful
				}
			}
	    });
	    
	    PTBControlPanel = new JPanel();
	    PTBControlPanel.setLayout(new BoxLayout(PTBControlPanel, BoxLayout.PAGE_AXIS));
	    PTBControlPanel.setBorder(BorderFactory.createLoweredBevelBorder());
	    PTBControlPanel.add(newPTBSession);
	    PTBControlPanel.add(clearPTBMemory);
	    PTBControlPanel.add(setPTBDateTime);
	    PTBControlPanel.add(Box.createVerticalGlue());
	}
	
	public static void main(String args[])
	{
		@SuppressWarnings("unused")
		OETimingApp main = new OETimingApp(args);
		
	}
}
