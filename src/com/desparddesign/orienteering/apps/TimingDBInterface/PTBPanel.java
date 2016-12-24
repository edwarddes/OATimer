package com.desparddesign.orienteering.apps.TimingDBInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PTBPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	PTBController controller;
	
	final JButton connectToPTB = new JButton("Connect To PTB");
	final JButton disconnectPTB = new JButton("Disconnect PTB");
	final JButton syncToPTB = new JButton("SyncToPTB");
	
	final JButton setDateTime = new JButton("Set Date/Time");
	final JButton newSession = new JButton("New Session");
	final JButton clearMemory = new JButton("Clear Memory");
	
	final JButton enableHighInputs = new JButton("Enable High Inputs");
	final JButton disableHighInputs = new JButton("Disable High Inputs");
	
	PTBPanel(PTBController cont)
	{
		controller = cont;
		
		JTextField PTBIPText = new JTextField(15);
		PTBIPText.setText(controller.getPTBSerialPortIP());
		PTBIPText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				controller.setPTBSerialPortIP(e.getActionCommand());
			}
		});
		
		JTextField PTBPortText = new JTextField(4);
		PTBPortText.setText(controller.getPTBSerialPortPort());
		PTBPortText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				controller.setPTBSerialPortPort(e.getActionCommand());
			}
		});
		
		connectToPTB.setEnabled(true);
		disconnectPTB.setEnabled(false);
		syncToPTB.setEnabled(false);
		
	    connectToPTB.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent event)
	    	{
	    		controller.connect();
	    		connectToPTB.setEnabled(false);
				disconnectPTB.setEnabled(true);
				syncToPTB.setEnabled(true);
	    	}
	    });
	    
	    disconnectPTB.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				controller.disconnect();
				connectToPTB.setEnabled(true);
				disconnectPTB.setEnabled(false);
				syncToPTB.setEnabled(false);
			}
	    });
	    
	    syncToPTB.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				controller.sync();
				controller.disableHigh();
				controller.setOtherLockout(0.1f);
			}
	    });
	    
	    setDateTime.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				controller.setPTBDate();
			}
	    });
	    newSession.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				controller.newSession();
			}
	    });
	    clearMemory.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				controller.clearMemory();
			}
	    });
	    
	    enableHighInputs.setEnabled(true);
		disableHighInputs.setEnabled(true);
		enableHighInputs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event) 
			{
				controller.enableHigh();
			}
		});
		disableHighInputs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event) 
			{
				controller.disableHigh();
			}
		});
	    
	    JPanel PTBConnectPanel;
	    PTBConnectPanel = new JPanel();
	    PTBConnectPanel.setLayout(new BoxLayout(PTBConnectPanel, BoxLayout.PAGE_AXIS));
	    PTBConnectPanel.setBorder(BorderFactory.createLoweredBevelBorder());
	    PTBConnectPanel.add(connectToPTB);
	    PTBConnectPanel.add(disconnectPTB);
	    PTBConnectPanel.add(syncToPTB);
	    PTBConnectPanel.add(Box.createVerticalGlue());
	    
	    JPanel PTBControlPanel;
	    PTBControlPanel = new JPanel();
	    PTBControlPanel.setLayout(new BoxLayout(PTBControlPanel, BoxLayout.PAGE_AXIS));
	    PTBControlPanel.setBorder(BorderFactory.createLoweredBevelBorder());
	    PTBControlPanel.add(setDateTime);
	    PTBControlPanel.add(newSession);
	    PTBControlPanel.add(clearMemory);
	    PTBControlPanel.add(Box.createVerticalGlue());
	    
	    JPanel PTBInputsPanel;
	    PTBInputsPanel = new JPanel();
	    PTBInputsPanel.setLayout(new BoxLayout(PTBInputsPanel, BoxLayout.PAGE_AXIS));
	    PTBInputsPanel.setBorder(BorderFactory.createLoweredBevelBorder());
	    PTBInputsPanel.add(enableHighInputs);
	    PTBInputsPanel.add(disableHighInputs);
	    PTBInputsPanel.add(Box.createVerticalGlue());
	    
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createLoweredBevelBorder());
	
		JPanel PTBIPSelectionPanel = new JPanel();
		PTBIPSelectionPanel.setLayout(new BoxLayout(PTBIPSelectionPanel, BoxLayout.LINE_AXIS));
		PTBIPSelectionPanel.add(new JLabel("PTB"));
		PTBIPText.setMaximumSize(PTBIPText.getPreferredSize());
		PTBIPSelectionPanel.add(PTBIPText);
		PTBPortText.setMaximumSize(PTBPortText.getPreferredSize());
		PTBIPSelectionPanel.add(PTBPortText);
		PTBIPSelectionPanel.add(Box.createVerticalGlue());
		
		add(PTBIPSelectionPanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(PTBConnectPanel);
		buttonPanel.add(PTBControlPanel);
		buttonPanel.add(PTBInputsPanel);
		
		add(buttonPanel);
		
		
		add(Box.createVerticalGlue());
	}
}
