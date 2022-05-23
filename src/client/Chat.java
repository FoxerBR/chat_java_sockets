package client;

import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;

import common.Utils;

public class Chat extends JFrame {

	private static final long serialVersionUID = 1L;
	private String connection_info;

	private JLabel jl_title;
	private JEditorPane messages;
	private JTextField jt_message;
	private JButton jb_message;
	private JPanel panel;
	private JScrollPane scroll;
	private Home home;
	private Socket connection;

	private ArrayList<String> message_list;

	public Chat(Home home, Socket connection, String connection_info, String title) {
		super("Chat:" + title);
		this.home = home;
		this.connection = connection;
		this.connection_info = connection_info;

		initComponents();
		configComponents();
		insertComponents();
		insertAction();
		start();
	}

	private void initComponents() {
		message_list = new ArrayList<String>();

		jl_title = new JLabel(connection_info.split(":")[0], SwingConstants.CENTER);
		messages = new JEditorPane();
		scroll = new JScrollPane(messages);
		jt_message = new JTextField();
		jb_message = new JButton("Enviar");
		panel = new JPanel(new BorderLayout());
	}

	private void configComponents() {
		this.setMinimumSize(new Dimension(480, 720));
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		messages.setContentType("text/html");
		messages.setEditable(false);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jb_message.setSize(100, 40);
	}

	private void insertComponents() {
		this.add(jl_title, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);
		panel.add(jt_message, BorderLayout.CENTER);
		panel.add(jb_message, BorderLayout.EAST);
	}

	private void insertAction() {
		jb_message.addActionListener(event -> send());

		jt_message.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					send();
				}

			}
		});
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				Utils.sendMessage(connection, "CHAT_CLOSE");
				home.getOpened_chats().remove(connection_info);
				home.getConnected_listener().get(connection_info).setChatOpen(false);
				home.getConnected_listener().get(connection_info).setRunning(false);
				home.getConnected_listener().remove(connection_info);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void append_message(String received) {
		message_list.add(received);
		String message = "";

		for (String str : message_list) {
			message += str;
		}

		messages.setText(message);
	}

	private void send() {
		if (jt_message.getText().length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String fields = this.getTitle().split(":")[1];
			Utils.sendMessage(connection, "MESSAGE;" + "<b>[" + sdf.format(new Date()) + "] " + fields + ": </b> "
					+ jt_message.getText() + "<br>");
			append_message("<b>[" + sdf.format(new Date()) + "] Eu: </b> " + jt_message.getText() + "<br>");
			jt_message.setText("");
		}
	}

	private void start() {
		this.pack();
		this.setVisible(true);
	}
}
