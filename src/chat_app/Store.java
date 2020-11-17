package chat_app;
import java.io.IOException;
import java.net.*;

public class Store {
	private void startServer() {
		try {
			DatagramSocket ds = new DatagramSocket(1004);
			System.out.println("UDP Server waiting on port = "+ds.getLocalPort());
			while(true) {
				byte[] buffer = new byte[1024];
				//수신용 패킷, 서버측
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ds.receive(dp);
				String recvData = new String(dp.getData(), 0, dp.getLength());
				if(recvData.startsWith("CALL")) {
					sendPacket("Ringing", buffer, ds,dp);
					sendPacket("OK",buffer,ds,dp);
				}else if(recvData.startsWith("ORDER")) {
					sendPacket("CHECK\n"+recvData,buffer,ds,dp);
					ds.receive(dp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void sendPacket(String data, byte[] buffer, DatagramSocket ds, DatagramPacket dp) throws IOException {
		String msg = data;
		buffer = msg.getBytes();
		InetAddress clientIP = dp.getAddress();
		int clientPort = dp.getPort();
		DatagramPacket ack = new DatagramPacket(buffer, buffer.length, clientIP,clientPort);
		ds.send(ack);
		System.out.println("Server : "+msg+" to"+clientIP+", port="+clientPort);
		
	}
	public static void main(String[] args) {
		Store store = new Store();
		store.startServer();
	}
}
