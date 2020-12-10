package chat_app;

public class Packet{
	String method,data;
	int number,hour,min;
	public Packet(String method, int number) {
		super();
		this.method = method;
		this.number = number;
	}
	public Packet(String method, int hour,int min, String data) {
		super();
		this.method = method;
		this.hour = hour;
		this.min = min;
		this.data = data;
	}
}