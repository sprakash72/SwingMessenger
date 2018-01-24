import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MessengerServerUI extends JFrame {
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket serverSock;
	private Socket connection;
	private JButton sendText;

	public MessengerServerUI() {
		super("Swing Messenger");
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendMessage(event.getActionCommand());
				System.out.println("Server:: " + event.getActionCommand());
				userText.setText("Hello");
			}
		});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		this.setSize(300, 150); // Sets the window size
//		this.setMaximizedBounds(env.getMaximumWindowBounds());
//        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
		setVisible(true);

	}

	public void startMessenger() {
		try {
			serverSock = new ServerSocket(3001, 10);
			while (true) 
			{
				// Wait for connection
				waitForConnection();

				// Set up input & output streams
				setupStreams();

				// Start messaging
				whileChatting();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();

		input = new ObjectInputStream(connection.getInputStream());

		showMessage("\n Streams are now setup \n");
	}

	// during the chat conversation
	private void whileChatting() throws IOException {
		String message = " You are now connected! ";
		sendMessage(message);
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			} catch (ClassNotFoundException classNotFoundException) {
				showMessage("The user has sent an unknown object!");
			}
		} while (!message.equals("CLIENT - END"));
	}

	private void waitForConnection() throws IOException 
	{
		showMessage(" Waiting for someone to connect... \n");
		connection = serverSock.accept();
		showMessage(" Now connected to " + connection.getInetAddress().getHostName());
	}

	public void closeConnection() {
		showMessage("\n Closing Connections... \n");
		ableToType(false);
		try {
			output.close(); // Closes the output path to the client
			input.close(); // Closes the input path to the server, from the
							// client.
			connection.close(); // Closes the connection between you can the
								// client
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Send a message to the client
	private void sendMessage(String message) 
	{
		try 
		{
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\nSERVER -" + message);
		}
		catch (IOException ioException) 
		{
			chatWindow.append("\n ERROR: CANNOT SEND MESSAGE, PLEASE RETRY");
		}
	}

	// update chatWindow
	private void showMessage(final String text) 
	{
		SwingUtilities.invokeLater(()->chatWindow.append(text));
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				chatWindow.append(text);
//			}
//		});
	}

	private void ableToType(final boolean tof)
	{
		SwingUtilities.invokeLater(()->userText.setEditable(tof));
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				userText.setEditable(tof);
//			}
//		});
	}
}
