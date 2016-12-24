package com.desparddesign.orienteering.apps.PTBEmulator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PTBChannelsPanel extends JPanel
{
	JButton[] channels;
	JButton sync;
	
	EmulatedPTBController controller;
	
	PTBChannelsPanel(EmulatedPTBController PTB)
	{
		controller = PTB;
		
		sync = new JButton("Sync");
		channels = new JButton[16];
		channels[0] = new JButton("1");
		channels[1] = new JButton("2");
		channels[2] = new JButton("3");
		channels[3] = new JButton("4");
		channels[4] = new JButton("5");
		channels[5] = new JButton("6");
		channels[6] = new JButton("7");
		channels[7] = new JButton("8");
		channels[8] = new JButton("9");
		channels[9] = new JButton("10");
		channels[10] = new JButton("11");
		channels[11] = new JButton("12");
		channels[12] = new JButton("13");
		channels[13] = new JButton("14");
		channels[14] = new JButton("15");
		channels[15] = new JButton("16");
		
		GridLayout layout = new GridLayout(2,9);
		setLayout(layout);
		setBorder(BorderFactory.createLoweredBevelBorder());
		
		
		sync.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.sync();}});
		
		channels[0].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(1, false);}});
		channels[1].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(2, false);}});
		channels[2].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(3, false);}});
		channels[3].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(4, false);}});
		channels[4].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(5, false);}});
		channels[5].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(6, false);}});
		channels[6].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(7, false);}});
		channels[7].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(8, false);}});
		channels[8].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(9, false);}});
		channels[9].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(10, false);}});
		channels[10].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(11, false);}});
		channels[11].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(12, false);}});
		channels[12].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(13, false);}});
		channels[13].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(14, false);}});
		channels[14].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(15, false);}});
		channels[15].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(16, false);}});
		
		add(sync);
		add(channels[0]);
		add(channels[1]);
		add(channels[2]);
		add(channels[3]);
		add(channels[4]);
		add(channels[5]);
		add(channels[6]);
		add(channels[7]);
		add(new JPanel());
		
		add(channels[8]);
		add(channels[9]);
		add(channels[10]);
		add(channels[11]);
		add(channels[12]);
		add(channels[13]);
		add(channels[14]);
		add(channels[15]);
		
	}
}
