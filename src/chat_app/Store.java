package chat_app;
import java.io.IOException;
import java.time.*;
import java.net.*;

public class Store {
	private DatagramSocket ds;
	private byte[] buffer;
	
	private int hour;
	private int minute;
	private int deliverTime = 10;
	private int timer = 0;
	
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
					//TODO : 소요시간
					//이제 배달업체에 패킷을 보낼 준비를 해야함.
					//아래 내용을 다른 클라이언트에 전송해야함 (배달업체로)
//					sendPacket(dp.getAddress().toString()+"로 배달해주세요",dp);
					setTime();
					sendPacket("SUCCESS",ds,dp);
				}
				else if(recvData.startsWith("TIME")) {
					getTime();
					sendPacket("TIME\n"+(deliverTime - timer),ds,dp);
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
	
	private void setTime() {
		LocalDateTime timePoint = LocalDateTime.now();
		hour = timePoint.getHour();
		minute = timePoint.getMinute();	
	}
	
	private void getTime() {
		
		System.out.println(timer);
		LocalDateTime checkPoint = LocalDateTime.now();
		int m_Hour = hour - checkPoint.getHour();
		int m_Minute = minute - checkPoint.getMinute();	
		System.out.println(hour+":"+minute+ " " + m_Hour+":"+m_Minute);
		
		if(m_Hour == 0) {
			timer = -m_Minute;
		}
		else if(m_Hour < 0) {
			timer = -m_Hour*60 - m_Minute;
		}
		else {
			timer = 24 - (m_Hour)*60 - m_Minute;
		}
		
		System.out.println(timer);
	}
	
	public static void main(String[] args) {
		Store store = new Store();
		store.recvPacket();
	}
}
