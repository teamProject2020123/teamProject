package chat_app;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UDPClient {
	private static final int PORT = 1004;
	private InetAddress ip;
	private DatagramSocket ds;
	private String result,recvData;
	private ArrayList<String> recvPacket;
	private int arrCount;
	private SimpleDateFormat format1;
	private Date time;
	private boolean IsChecked = false;

	public UDPClient() {
		recvPacket = new ArrayList<>();
		try {
			ip = InetAddress.getByName("127.0.0.1");
			ds = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void sendMsg(String msg) {
		try {
			byte[] buffer = msg.getBytes();
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length, ip, PORT);
			ds.send(dp);		
		} catch (Exception e) {
			ds.close();
			e.printStackTrace();
		}
	}
	public void dataReceiver() {
		new Thread(()->{
			while( true ) { 
				byte[] buf = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(packet);
					String recvData = new String(packet.getData(), 0, packet.getLength());
					recvPacket.add(recvData);
				} catch (Exception e) {
					ds.close();
					e.printStackTrace();
					break;
				} 
			} 
		}).start();
	}
	public void checkPacket(String recvData) { //항상 thread와 같이 사용해야함.
		if(recvData.startsWith("Ringing")) {
			format1 = new SimpleDateFormat ("HH시mm분ss초");
			time = new Date();
			System.out.println("["+format1.format(time)+"][Client]"
					+"Ringing 도착, 연결에 성공, 다음 응답을 기다립니다.");
			try {
				Thread.sleep(1000);				
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if(recvData.startsWith("OK")) {
			format1 = new SimpleDateFormat ("HH시mm분ss초");
			time = new Date();
			System.out.println("["+format1.format(time)+"][Client]"
					+"OK 도착, ORDER 화면으로 넘어갑니다.");
		} 
	}
	public boolean checkPacket(String sendPacket,String recvPacket) {
		//		while(!IsChecked) {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recvPacket = recvPacket.substring(6);
		System.out.println("확인해보자 : "+recvPacket);
		if(sendPacket.equals(recvPacket)) {
			sendMsg("ACK");
			//ack보내고 반복문 나가기
			return true;
			
		} else {
			sendMsg("ERROR\n"+sendPacket);
			//continue
			return false;
		}
		//		}
	}
	public ArrayList getArrPacket() {
		return recvPacket;
	}
	public int getCount() {
		return arrCount;
	}
	public InetAddress getInetAddress() {
		return ip;
	}
}
