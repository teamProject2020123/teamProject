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
					sendPacket("CHECK\n"+recvData,ds,dp); //---------------------ORDER 패킷에 확인 보내는것
				}else if(recvData.startsWith("ACK")){
					sendPacket("SUCCESS to ORDER",ds,dp);
					//이제 배달업체에 패킷을 보낼 준비를 해야함.
					//아래 내용을 다른 클라이언트에 전송해야함 (배달업체로)
//					sendPacket(dp.getAddress().toString()+"로 배달해주세요",dp);
				}else if(recvData.startsWith("ERROR")) {
					//재전송하도록 설계
					//에러 시나리오 (CHECK 패킷에서 오류 발생함)
					/* 주문하기 버튼 클릭시에
					 * 1) [Client]ORDER 패킷을 전송함 (ORDER + 주문내역)
					 * 2) [Server]CHECK 패킷 보낼때 ORDER 패킷 내용들을 CHECK+주문내역 으로 보냄 [여기서 DUMMY 패킷 붙여서 보냄 : 주문내역 + "sdlkjf"] : error
					 * 3) [Client]보낸 패킷과 받은 패킷 비교하여 맞으면 ACK, 틀리면 ERROR 전송
					 * 4) [Server]서버측에서 2번내용을 다시 전송(이때는 정상 패킷 보냄)
					 * 5) [Client]클라이언트 측에서 비교해서 ACK 보냄.
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
