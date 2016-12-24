package com.desparddesign.orienteering.apps.FinishTiming;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OEPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private OEController controller;
	
	final JButton connectToOESI = new JButton("Connect To OE SI");
	final JButton connectToOEAlge = new JButton("Connect To OE Alge");
	
	final JButton disconnectOESI = new JButton("Disconnect OE SI");
	final JButton disconnectOEAlge = new JButton("Disconnect OE Alge");
	
	OEPanel(OEController cont)
	{
		controller = cont;
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createLoweredBevelBorder());
		add(Box.createVerticalGlue());
		
		JTextField OESIIPText = new JTextField(15);
		OESIIPText.setText(controller.getOESISerialPortIP());
		OESIIPText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				controller.setOESISerialPortIP(e.getActionCommand());
			}
		});
		
		JTextField OESIPortText = new JTextField(4);
		OESIPortText.setText(controller.getOESISerialPortPort());
		OESIPortText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				controller.setOESISerialPortPort(e.getActionCommand());
			}
		});
		
		JTextField OEAlgeIPText = new JTextField(15);
		OEAlgeIPText.setText(controller.getOEAlgeSerialPortIP());
		OEAlgeIPText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				controller.setOEAlgeSerialPortIP(e.getActionCommand());
			}
		});
		
		JTextField OEAlgePortText = new JTextField(4);
		OEAlgePortText.setText(controller.getOEAlgeSerialPortPort());
		OEAlgePortText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				controller.setOEAlgeSerialPortPort(e.getActionCommand());
			}
		});
		
		JPanel serialPortSelectionPanel = new JPanel();
		
		JPanel OESIIPSelectionPanel = new JPanel();
		OESIIPSelectionPanel.setLayout(new BoxLayout(OESIIPSelectionPanel, BoxLayout.LINE_AXIS));
		OESIIPSelectionPanel.add(new JLabel("OE SI"));
		OESIIPText.setMaximumSize(OESIIPText.getPreferredSize());
		OESIIPSelectionPanel.add(OESIIPText);
		OESIPortText.setMaximumSize(OESIPortText.getPreferredSize());
		OESIIPSelectionPanel.add(OESIPortText);
		serialPortSelectionPanel.add(OESIIPSelectionPanel);
		
		JPanel OEAlgeIPSelectionPanel = new JPanel();
		OEAlgeIPSelectionPanel.setLayout(new BoxLayout(OEAlgeIPSelectionPanel, BoxLayout.LINE_AXIS));
		OEAlgeIPSelectionPanel.add(new JLabel("OE Alge"));
		OEAlgeIPText.setMaximumSize(OEAlgeIPText.getPreferredSize());
		OEAlgeIPSelectionPanel.add(OEAlgeIPText);
		OEAlgePortText.setMaximumSize(OEAlgePortText.getPreferredSize());
		OEAlgeIPSelectionPanel.add(OEAlgePortText);
		serialPortSelectionPanel.add(OEAlgeIPSelectionPanel);
		
		serialPortSelectionPanel.add(Box.createVerticalGlue());
		
		connectToOESI.setEnabled(true);
		connectToOEAlge.setEnabled(true);
		disconnectOESI.setEnabled(false);
		disconnectOEAlge.setEnabled(false);
		
		connectToOESI.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				controller.connectToOESI();
				connectToOESI.setEnabled(false);
				disconnectOESI.setEnabled(true);
			}
	    });
		connectToOEAlge.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				controller.connectToOEAlge();
				connectToOEAlge.setEnabled(false);
				disconnectOEAlge.setEnabled(true);
			}
	    });
		disconnectOESI.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				controller.disconnectOESI();
				connectToOESI.setEnabled(true);
				disconnectOESI.setEnabled(false);
			}
	    });
		disconnectOEAlge.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent event) 
			{
				controller.disconnectOEAlge();
				connectToOEAlge.setEnabled(true);
				disconnectOEAlge.setEnabled(false);
			}
	    });
		
		JPanel OEConnectPanel;
		OEConnectPanel = new JPanel();
		OEConnectPanel.setLayout(new BoxLayout(OEConnectPanel, BoxLayout.PAGE_AXIS));
		OEConnectPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		OEConnectPanel.add(connectToOESI);
		OEConnectPanel.add(connectToOEAlge);
		OEConnectPanel.add(disconnectOESI);
		OEConnectPanel.add(disconnectOEAlge);
		OEConnectPanel.add(Box.createVerticalGlue());
		
		
		add(serialPortSelectionPanel);
		add(OEConnectPanel);
		
		add(Box.createVerticalGlue());
	}
}
