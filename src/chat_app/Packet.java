package chat_app;

public class Packet{
	String method;
	int number;
	public Packet(String method, int number) {
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
	int hour,min,seq,orderTime;
	public Packet_TIME(String method, String data,int hour,int min,int orderTime) {
		this.method = method;
		this.hour = hour;
		this.min = min;
		this.data = data;
		this.orderTime = orderTime;
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