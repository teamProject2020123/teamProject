package chat_app;
import java.io.IOException;
import java.net.*;

public class UDPmsg {
	private InetAddress ip;
	private DatagramSocket ds;
	private String result,recvData;
	public void sendMsg(String msg) {
		try {
			ds = new DatagramSocket();
			byte[] buffer = msg.getBytes();
			ip = InetAddress.getByName("127.0.0.1");
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length, ip, 1004);
			ds.send(dp);		
		} catch (Exception e) {
			ds.close();
			e.printStackTrace();
		}
	}
	public void dataReceiver() {
		new Thread() {
			public void run() {
				while( true ) { //서버가 나에게 보낸 문자 한 줄을 계속 받아서 처리 
					//패킷 수신용 DatagramPacket 객체 생성
					byte[] buf = new byte[1024];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					try {
						ds.receive(packet);
						String recvData = new String(packet.getData(), 0, packet.getLength()); // or getData().trim()
						recvData = recvData.substring(6);
						result = recvData;
					} catch (IOException e) {
						ds.close();
						System.out.println("error at socket.receive(); will break and end thread");
						break;
					} finally {
						result = recvData;
					}
				}                                   
			};
		}.start();
	}
	public String getPacketData() {
		return result;
	}
}
