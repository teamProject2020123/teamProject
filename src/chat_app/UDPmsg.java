package chat_app;
import java.net.*;

public class UDPmsg {
	private InetAddress ip;
	private String s;
	public void sendMsg(String msg) {
		try {
			DatagramSocket ds = new DatagramSocket();
			byte[] buffer = msg.getBytes();
			ip = InetAddress.getByName("127.0.0.1");
			s = "гоюл";
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length, ip, 1004);
			ds.send(dp);
			ds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
