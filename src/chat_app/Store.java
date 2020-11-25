package chat_app;
import java.io.IOException;
import java.net.*;

public class Store {
	private DatagramSocket ds;
	private byte[] buffer;
	public Store() {
		try {
			ds = new DatagramSocket(1004);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void recvPacket() {
		try {
			System.out.println("UDP Server waiting on port = "+ds.getLocalPort());
			while(true) {
				buffer = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ds.receive(dp);
				String recvData = new String(dp.getData(), 0, dp.getLength());
				if(recvData.startsWith("ORDER")) {
					//TODO : �ҿ�ð�
					//���� ��޾�ü�� ��Ŷ�� ���� �غ� �ؾ���.
					//�Ʒ� ������ �ٸ� Ŭ���̾�Ʈ�� �����ؾ��� (��޾�ü��)
//					sendPacket(dp.getAddress().toString()+"�� ������ּ���",dp);
					sendPacket("SUCCESS",ds,dp);
				}
				else if(recvData.startsWith("TIME")) {
				}
			}
		} catch (Exception e) {
			ds.close();
			e.printStackTrace();
		}
	}
	private void sendPacket(String data, DatagramSocket ds,DatagramPacket dp) throws IOException {
		String msg = data;
		byte[] buffer = msg.getBytes();
		InetAddress clientIP = dp.getAddress();
		int clientPort = dp.getPort();
		DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, clientIP,clientPort);
		ds.send(sendPacket);
		System.out.println("Server : "+msg);
		
	}
	public static void main(String[] args) {
		Store store = new Store();
		store.recvPacket();
	}
}
