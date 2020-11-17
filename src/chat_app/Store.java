package chat_app;
import java.io.IOException;
import java.net.*;

public class Store {
	private DatagramSocket ds;
	private byte[] buffer;
	private void recvPacket() {
		try {
			ds = new DatagramSocket(1004);
			System.out.println("UDP Server waiting on port = "+ds.getLocalPort());
			while(true) {
				buffer = new byte[1024];
				//수신용 패킷, 서버측
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ds.receive(dp); //클라이언트로부터 패킷을 받음.
				String recvData = new String(dp.getData(), 0, dp.getLength());
				if(recvData.startsWith("CALL")) {
					sendPacket("Ringing", buffer, ds,dp);
					sendPacket("OK",buffer,ds,dp);
				}else if(recvData.startsWith("ORDER")) {
					sendPacket("CHECK\n"+recvData,buffer,ds,dp);
//					ds.receive(dp);
//					System.out.println("패킷 수신 성공");
				}else {
					sendPacket("Cannot matching packet",buffer,ds,dp);
				}
			}
		} catch (Exception e) {
			ds.close();
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
		System.out.println("Server : "+msg);
		
	}
	public static void main(String[] args) {
		Store store = new Store();
		store.recvPacket();
	}
}
