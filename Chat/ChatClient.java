package twoserver;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.AllPermission;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ChatClient {
	Socket socket, fileSocket;

	BufferedReader in;
	PrintWriter out;

	InputStream fileIn;
	OutputStream fileOut;

	JFrame frame = new JFrame("Chatter");
	JTextField txtMessage = new JTextField(20);
	JTextArea messageArea = new JTextArea(16, 26);
	JPanel panel = new JPanel();
	JPanel panelEast = new JPanel();
	JButton btnSelect = new JButton("Select");
	JTextField txtReceiver = new JTextField(8);
	JButton btnExit = new JButton("Exit");

	JList<String> names = new JList<>();
	DefaultListModel<String> clientNames = new DefaultListModel<>();

	/**
	 * Constructs the chatClient by laying out the GUI and adding the 
	 * ActionListener for each button. The txtmessage is only available 
	 * to chatter after the server sends the confirmation message to the client
	 */
	
	public ChatClient() {

		// Layout GUI
		txtMessage.setEditable(false);
		messageArea.setEditable(false);
		txtReceiver.setEditable(false);

		panel.setLayout(new BorderLayout());
		panelEast.setLayout(new BorderLayout());
		panelEast.add(btnSelect, BorderLayout.CENTER);
		panelEast.add(btnExit, BorderLayout.SOUTH);

		panel.add(txtMessage, BorderLayout.CENTER);
		panel.add(panelEast, BorderLayout.EAST);
		panel.add(txtReceiver, BorderLayout.WEST);

		names.setModel(clientNames);
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		frame.getContentPane().add(new JScrollPane(messageArea), "Center");
		clientNames.addElement("All");
		JScrollPane scrollPaneName = new JScrollPane(names);
		Dimension d = names.getPreferredSize();
		d.width = 100;
		scrollPaneName.setPreferredSize(d);
		frame.getContentPane().add(scrollPaneName, BorderLayout.EAST);
		frame.pack();

		frame.addWindowListener(new WindowListener() {

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
				// TODO Auto-generated method stub
				out.println("EXIT " + frame.getTitle());
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
		// Add the action listener to txtmessage. The contents of message will
		// be sent to server. It includes the name of receivers.
		txtMessage.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				out.println(txtReceiver.getText() + txtMessage.getText());
				txtMessage.setText("");
			}
		});

		// add action listener to List.
		names.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				String strReceiver = "";
				List<String> listNames = names.getSelectedValuesList();
				for (String toPerson : listNames) {
					// send message to all of people in the group
					if (toPerson.equals("All")) {
						txtReceiver.setText("");
						names.setSelectedIndex(0);
						break;
					}
					// send private email to several people in the group
					else
						strReceiver = strReceiver + "@" + toPerson + ",";
				}
				//
				if (!strReceiver.equals(""))
					txtReceiver.setText(strReceiver + "#");
			}
		});

		// add Action listener to the Exit button
		btnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				out.println("EXIT " + frame.getTitle());
				System.exit(0);
			}
		});
		// Add action listener to the Select button. The chatter can choose file
		// and share it with others
		btnSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				// Send file
				JFileChooser chooser = new JFileChooser();
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					byte[] fileByte = new byte[(int) file.length()];
					FileInputStream fis;
					try {
						fis = new FileInputStream(file);
						fis.read(fileByte, 0, fileByte.length);
						fileOut.write(fileByte, 0, fileByte.length);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(frame, "FILE NOT FOUND EXCEPTION");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(frame, "IO EXCEPTION");
					}
					// send message to inform the server
					out.println("FILE " + file.getName());
					txtMessage.setText("");
				}
			}
		});
	}

	private String getServerAddress() {
		return JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:", "Welcome to the Room Chat",
				JOptionPane.QUESTION_MESSAGE);
	}

	private String getName() {
		return JOptionPane.showInputDialog(frame, "Enter your name name:", "Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Connects to the server then enters the processing loop.
	 */
	private void run() throws IOException {

		// Make connection and initialize streams
		boolean isConnected = false;
		while (!isConnected) {
			try {
				String serverAddress = getServerAddress();
				socket = new Socket(serverAddress, 9001);
				fileSocket = new Socket(serverAddress, 9002);
				isConnected = true;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, "The ID address is invalid. Retype!");
			}
		}
		// Create the stream for the text message
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		// Create the stream for the file message
		fileIn = fileSocket.getInputStream();
		fileOut = fileSocket.getOutputStream();

		// Process all messages from server, according to the protocol.
		while (true) {
			String line = in.readLine();
			if (line.startsWith("SUBMITTINGNAME")) {
				String clientName = getName();
				out.println(clientName);
				frame.setTitle(clientName);
			} else if (line.startsWith("NAMEADDED")) {
				txtMessage.setEditable(true);
			} else if (line.startsWith("NEWCLIENT")) {
				messageArea.append(line.substring(10) + " has joined our group" + "\n");
				// Add the new client to the list
				clientNames.removeAllElements();
				clientNames.addElement("All");
				String allClientnames = in.readLine();
				String[] names = allClientnames.split("~");
				for (String string : names) {
					clientNames.addElement(string);
				}
			} else if (line.startsWith("CLIENTLEFT")) {
				messageArea.append(line.substring(11) + " has left our group" + "\n");
				clientNames.removeAllElements();
				clientNames.addElement("All");
				String allClientnames = in.readLine();
				String[] names = allClientnames.split("~");
				for (String string : names) {
					clientNames.addElement(string);
				}
			} else if (line.startsWith("MESSAGE")) {
				// Receive the message from the server 
				messageArea.append(line.substring(8) + "\n");
			} else if (line.startsWith("FILE")) {
				// Receive file from the server
				messageArea.append(line.substring(5) + "\n");
				String fileName = in.readLine();
				// Save file
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File(fileName));
				if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					FileOutputStream fos = new FileOutputStream(file);
					byte[] fileByte = new byte[1024 * 1024];
					int len = fileIn.read(fileByte, 0, fileByte.length);
					fos.write(fileByte, 0, len);
					fos.close();
					out.println("has received the file");
				} else {
					out.println("has rejected to receive the file");
				}
			}
		}
	}

	
	public static void main(String[] args) throws Exception {
		ChatClient client = new ChatClient();
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setVisible(true);
		client.run();
	}
}