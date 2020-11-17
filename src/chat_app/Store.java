package chat_app;
import java.net.*;

public class Store {
	public static void main(String[] args) {
		try {
			DatagramSocket ds = new DatagramSocket(1004);
			System.out.println("UDP Server waiting on port = "
			+ds.getLocalPort());
			byte[] buffer = new byte[1024];
			//수신용 패킷, 서버측
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			ds.receive(dp);
			
			String recvData = new String(dp.getData(), 0, dp.getLength());
			System.out.println("received data from udpClient : "+recvData);
			String msg = "ACK to the packet["+recvData+"]";
			buffer = msg.getBytes();
			
			InetAddress clientIP = dp.getAddress();
			int clientPort = dp.getPort();
			DatagramPacket ack = new DatagramPacket(buffer, buffer.length, clientIP,clientPort);
			ds.send(ack);
			System.out.println("Server sent : "+msg+"to"+clientIP+", port="+clientPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
