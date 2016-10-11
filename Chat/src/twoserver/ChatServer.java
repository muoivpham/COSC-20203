package twoserver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/***************************************************************/
/*   Program Name:     Lab 4+5                                   */
/*                                                             */
/*   Student Name:     Muoi Pham			                   */
/*   Semester:         Spring, 2016		                       */
/*   Class-Section:    CoSc20203-55					           */
/*   Instructor:       Dr.J. Richard (Dick) Rinewalt           */
/*                                                             */
/*   Program Overview:                                         */
/*     	This program create the GUI chat for the client and server. */
/*     	The Clients can send message to each other in the group*/
/* 		or send the private message to one or "more than one"  */
/* 		one person. They also can send file to others in group */
/* 		by using two servers.	                               */
/*                                                             */
/*   Input:                                                    */
/*     The client can send text message for everyone or        */
/*    	private message to some people.           			   */
/*      They also can send a file (less than 1MB) to other     */
/*                                                             */
/*   Output:                                                   */
/*     Message is shown on client and server also can observe  */
/*                                                             */
/*   Program Limitations:    none                              */
/*                                                             */
/***************************************************************/
public class ChatServer {

	/**
	 * Create two ports
	 */
	private static final int PORT = 9001;// For text stream
	private static final int FILE_PORT = 9002;// For file stream

	/**
	 * Create the set of all clients' names. So the new client will not register
	 * existed name
	 */
	private static HashSet<String> names = new HashSet<String>();
	private static Map<String, PrintWriter> map = new HashMap();
	private static Map<String, OutputStream> mapFile = new HashMap();

	/**
	 * This set is to easily broadcast messages and files.
	 */
	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	private static HashSet<OutputStream> fileOutputs = new HashSet<OutputStream>();

	/**
	 * Constructs the ChatServer by laying out the GUI and main method
	 */
	private static JTextArea txtMessage = new JTextArea();
	private static JButton close = new JButton("CLOSE THE PROGRAM");
	private static JFrame frame = new JFrame("Chat Server");

	public static void main(String[] args) throws Exception {
		ServerSocket listener = new ServerSocket(PORT);// Text server
		ServerSocket fileListener = new ServerSocket(FILE_PORT);// File server

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		txtMessage.append("\nTHE STREAM ACTIVITIES WILL BE DISPLAYED BELOW: \n");
		txtMessage.setEditable(false);

		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					listener.close();
					fileListener.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					txtMessage.append("\ncannot close the server");
				}
				System.exit(0);
			}
		});
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
				try {
					listener.close();
					fileListener.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					txtMessage.append("\ncannot close the server");
				}

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
		frame.setLayout(new BorderLayout());
		frame.add(new JScrollPane(txtMessage), BorderLayout.CENTER);
		frame.add(new JLabel(new ImageIcon("TCU.jpg")), BorderLayout.NORTH);
		frame.add(close, BorderLayout.SOUTH);
		frame.setSize(500, 400);
		frame.setVisible(true);

		try {
			while (true) {
				new ChatThread(listener.accept(), fileListener.accept()).start();
			}
		} finally {
			listener.close();
			fileListener.close();
			System.exit(0);
		}
	}

	/**
	 * The thread class that is to deal with the single client's message.
	 */
	private static class ChatThread extends Thread {
		private String name;
		private Socket socket;
		private Socket fileSocket;

		// Create the Stream for text message
		private BufferedReader textInputStream;
		private PrintWriter textOutputStream;

		// Create the Stream for file message
		private OutputStream fileOut;
		private InputStream fileIn;

		public ChatThread(Socket socket, Socket fileSocket) {
			this.socket = socket;
			this.fileSocket = fileSocket;
		}

		/**
		 * Run method will deal with the message from client, including
		 * requesting all of different names, sending the private message to multi-clients sending a file to all of clients, and broadcasting the
		 * message
		 */
		public void run() {
			try {

				// Create text streams.
				textInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				textOutputStream = new PrintWriter(socket.getOutputStream(), true);

				// Create file streams.
				fileIn = fileSocket.getInputStream();
				fileOut = fileSocket.getOutputStream();

				// Request a name from this client until
				// the new name is not already in used
				while (true) {
					textOutputStream.println("SUBMITTINGNAME");
					name = textInputStream.readLine();
					if (name == null) {
						return;
					}
					synchronized (names) {
						if (!names.contains(name)) {
							txtMessage.append("\nClient" + name + " has connected");
							names.add(name);
							break;
						}
					}
				}

				// add the socket's printWriter to set of all writers so
				// this client can receive broadcast messages.
				textOutputStream.println("NAMEADDED");
				writers.add(textOutputStream);
				map.put(name, textOutputStream);
				fileOutputs.add(fileOut);
				mapFile.put(name, fileOut);

				// the set of all clients' names
				for (PrintWriter printWriter : writers) {
					printWriter.println("NEWCLIENT " + name);
					String allClients = "";
					for (String strname : names) {
						allClients = allClients + strname + "~";
					}
					allClients = allClients.substring(0, allClients.length() - 1);
					printWriter.println(allClients);
				}

				// Receive the messages from the client and deal with them.
				// After that, the server will broadcast them to other client
				while (true) {
					String input = textInputStream.readLine();
					if (input == null) {
						return;
					}
					// @Client's name
					// allow to send private message to one or more than one
					// person
					else if (input.startsWith("@")) {
						// split character "," -> array String
						String input0 = input.split("#")[0];
						String[] cNames = input0.split(",");

						txtMessage.append("\nMESSAGE Private message from " + name);
						for (int i = 0; i < cNames.length; i++) {
							String cName = cNames[i];
							// delete the character "@"
							cName = cName.substring(1);

							PrintWriter receiver = map.get(cName);
							receiver.println("MESSAGE Private message from " + name + ": "
									+ input.substring(input0.length() + 1));

							PrintWriter sender = map.get(name);
							sender.println("MESSAGE Private message to " + cName + ": "
									+ input.substring(input0.length() + 1));
							txtMessage.append("\nMESSAGE Private message to " + cName);
						}

					} else if (input.contains("FILE")) {
						// Receive file from client
						String fileName = input.substring(5);
						byte[] file = new byte[1024 * 1024];
						int len = fileIn.read(file, 0, file.length);

						// Broadcast file to all client
						for (OutputStream outputStream : fileOutputs) {
							outputStream.write(file, 0, len);
						}

						for (PrintWriter writer : writers) {
							writer.println("FILE " + name + " has sent a file.");
							writer.println(fileName);
						}
						txtMessage.append("\n" + name + "has sent a file to other clients! ");
					} else if (input.startsWith("EXIT")) {
						// A client left the chat room
						String name = input.substring(5);
						names.remove(name);
						writers.remove(map.get(name));
						map.remove(name);
						fileOutputs.remove(mapFile.get(name));
						mapFile.remove(name);
						for (PrintWriter printWriter : writers) {
							printWriter.println("CLIENTLEFT " + name);
							String allClients = "";
							for (String strname : names) {
								allClients = allClients + strname + "~";
							}
							allClients = allClients.substring(0, allClients.length() - 1);
							printWriter.println(allClients);
						}
						txtMessage.append("\n" + name + " has left the room chat ");
					} else {
						// send message to others
						for (PrintWriter writer : writers) {
							writer.println("MESSAGE " + name + ": " + input);
						}
						txtMessage.append("\n" + name + " has sent a message");
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				// Remove the left client's name and stream.
				if (name != null) {
					names.remove(name);
				}
				if (textOutputStream != null) {
					writers.remove(textOutputStream);
				}
				if (fileOut != null) {
					fileOutputs.remove(fileOut);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}