package chat_app;

public class Packet_RESPONSE{
	String method;
	int number;
	public Packet_RESPONSE(String method, int number) {
//		super();
		this.method = method;
		this.number = number;
	}
	
}
class Packet_TIME{
	String method, data;
	int hour,min,seq;
	public Packet_TIME(String method, String data,int hour,int min,int seq) {
//		super();
		this.method = method;
		this.hour = hour;
		this.min = min;
		this.data = data;
		this.seq = seq;
	}
}