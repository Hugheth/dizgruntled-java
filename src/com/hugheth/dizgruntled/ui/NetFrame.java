package com.hugheth.dizgruntled.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class NetFrame extends JFrame {

	private static final long serialVersionUID = 2L;

	public NetFrame() {
		super("Netz");
		Box box = Box.createVerticalBox();
		JButton buttonJoinLAN = new JButton("Join LAN Game");
		JButton buttonJoinWEB = new JButton("Join WEB Game");
		JButton buttonHost = new JButton("Host Game");
		box.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Network Game"));
		box.add(new JLabel("Select the type of network action that you want to take"));
		box.add(buttonJoinLAN);
		box.add(buttonJoinWEB);
		box.add(buttonHost);
		getContentPane().add(box, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
}