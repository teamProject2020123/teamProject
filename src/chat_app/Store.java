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
				if(recvData.startsWith("CALL")) {
					sendPacket("Ringing",ds,dp);
					sendPacket("OK",ds,dp);
				}else if(recvData.startsWith("ORDER")) {
					sendPacket("CHECK\n"+recvData,ds,dp); //---------------------ORDER ��Ŷ�� Ȯ�� �����°�
				}else if(recvData.startsWith("ACK")){
					sendPacket("SUCCESS to ORDER",ds,dp);
					//���� ��޾�ü�� ��Ŷ�� ���� �غ� �ؾ���.
					//�Ʒ� ������ �ٸ� Ŭ���̾�Ʈ�� �����ؾ��� (��޾�ü��)
//					sendPacket(dp.getAddress().toString()+"�� ������ּ���",dp);
				}else if(recvData.startsWith("ERROR")) {
					//�������ϵ��� ����
					//���� �ó����� (CHECK ��Ŷ���� ���� �߻���)
					/* �ֹ��ϱ� ��ư Ŭ���ÿ�
					 * 1) [Client]ORDER ��Ŷ�� ������ (ORDER + �ֹ�����)
					 * 2) [Server]CHECK ��Ŷ ������ ORDER ��Ŷ ������� CHECK+�ֹ����� ���� ���� [���⼭ DUMMY ��Ŷ �ٿ��� ���� : �ֹ����� + "sdlkjf"] : error
					 * 3) [Client]���� ��Ŷ�� ���� ��Ŷ ���Ͽ� ������ ACK, Ʋ���� ERROR ����
					 * 4) [Server]���������� 2�������� �ٽ� ����(�̶��� ���� ��Ŷ ����)
					 * 5) [Client]Ŭ���̾�Ʈ ������ ���ؼ� ACK ����.
					*/ 
					recvData = recvData.substring(6);
					System.out.println("data : "+recvData);
					sendPacket("CHECK\n"+recvData,ds,dp); 
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
		FrameExam frameExam = new FrameExam();
		frameExam.setVisible(true);
		store.recvPacket();
	}
}
