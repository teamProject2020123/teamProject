package chat_app;
import java.net.*;

public class UDPmsg {
	private InetAddress ip;
	private String OrderMsg;
	private String CheckMsg;
	public void sendMsg(String msg) {
		try {
			DatagramSocket ds = new DatagramSocket();
			byte[] buffer = msg.getBytes();
			ip = InetAddress.getByName("172.18.15.143");
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length, ip, 1004);
			ds.send(dp);
			
			DatagramPacket checker = new DatagramPacket(buffer, buffer.length);
			ds.receive(checker);
			CheckMsg = new String(checker.getData(),0,checker.getLength());
			CheckMsg = CheckMsg.substring(6);
			System.out.println(OrderMsg);
			
			boolean check = CheckOrder(OrderMsg, CheckMsg);
			
			if(OrderMsg.equals(CheckMsg)) {
				String ack = "ACK";
				buffer = ack.getBytes();
			
				DatagramPacket ACK = new DatagramPacket(buffer, buffer.length, ip, 1004);
				ds.send(ACK);		
			}
			else{
				String ack = "NAK";
				buffer = ack.getBytes();
			
				DatagramPacket ACK = new DatagramPacket(buffer, buffer.length, ip, 1004);
				ds.send(ACK);	
			}
			
			//System.out.println(CheckString);
			
			ds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void GetOrder(String msg) {
		OrderMsg = msg;
	}
}
