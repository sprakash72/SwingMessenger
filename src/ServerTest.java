import javax.swing.JFrame;

public class ServerTest 
{
	public static void main(String[] args) {
		MessengerServerUI server = new MessengerServerUI();
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		server.startMessenger();
	}
}