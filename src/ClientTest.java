import javax.swing.JFrame;

public class ClientTest extends JFrame
{
	public static void main(String[] args) 
	{
		MessengerClient charlie;
		charlie = new MessengerClient("127.0.0.1");
		charlie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		charlie.startRunning();
	}
}
