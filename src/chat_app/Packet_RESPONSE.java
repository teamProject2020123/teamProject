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
class Packet_initialTime{
	String method;
	int number,time;
	public Packet_initialTime(String method, int number, int time) {
		this.method = method;
		this.number = number;
		this.time = time;
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
class Menu {
	String method,main,sub1,sub2,sub3,description;
	public Menu(String main, String op1, String op2, String op3, String description) {
		super();
		this.main = main;
		this.sub1 = op1;
		this.sub2 = op2;
		this.sub3 = op3;
		this.description = description;
	}
}