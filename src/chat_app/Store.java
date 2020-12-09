package chat_app;
import java.awt.Font;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

public class Store extends JFrame{
	private DatagramSocket ds;
	private DefaultListModel model = new DefaultListModel();
	private ArrayList<String> data = new ArrayList<>();
	private HashMap<Integer, String> map = new HashMap<Integer, String>();
	private int orderSeq;
	private static final String DEST_IP = "127.0.0.1";
	private int port;
	private byte[] buffer;
	private JPanel contentPane;
	private JList<String> list;
	private int count =1;
	private JButton btn1,btn2,btn3,btn4;
	private int hour, minute, deliverTime;
	private final int CHICKEN_TIME = 10;
	private final int PIZZA_TIME = 12;
	private final int PORK_TIME = 15;
	private int timer = 0;
	private float cookTime = 0;

	public Store() {
		setView();
		orderSeq=0;
		try {
			ds = new DatagramSocket(1004);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	public int getOrderNumber() {
		return orderSeq;
	}
	private void recvPacket() {
		try {
			while(true) {
				buffer = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ds.receive(dp);
				port=dp.getPort();
				String recvData = new String(dp.getData(), 0, dp.getLength());
				if(recvData.startsWith("ORDER")) {
					System.out.println("====================================");
					recvData = recvData.substring(6);
					if(map.size()<3) { 
						map.put(count, parsingJson(recvData)); //HashMap에 Order 내용 추가
						updateList();
						sendMsg("SUCCESS");
						sendMsg("ORDER_NUMBER="+(++orderSeq));	
						setTime();						
					} else {
						//주문 시간에 추가되도록 해야함
						
						sendMsg("FAILED");
					}
				} 
				else if(recvData.startsWith("TIME")) {
					getTime();
					sendMsg("TIME="+(deliverTime - timer));

				} else if(recvData.startsWith("CANCEL")) {
					int num = Integer.parseInt(recvData.substring(7));
					if((deliverTime-timer)>cookTime) { //요리가 시작되면 주문 취소를 할 수 없게 하기 위해 작성함
						cancelOrder(num);
						sendMsg("CANCEL_OK");						
					} else if((deliverTime-timer)<=cookTime) {
						sendMsg("CANCEL_FAIL");
					}
				}

			}
		} catch (Exception e) {
			ds.close();
			e.printStackTrace();
		}
	}
	private String parsingJson(String recvData) {
		Gson gson = new Gson();
		Menu menu = gson.fromJson(recvData,Menu.class);
		String orderList = menu.main;
		if(menu.main.equals("Chicken")) {
			deliverTime = CHICKEN_TIME;
			cookTime = CHICKEN_TIME * 0.8f;
		} else if(menu.main.equals("Pizza")) {
			deliverTime = PIZZA_TIME;
			cookTime = PIZZA_TIME * 0.8f;
		} else {
			deliverTime = PORK_TIME;
			cookTime = PORK_TIME * 0.8f;
		}
		if(!menu.sub1.isEmpty()) orderList += ","+menu.sub1;
		if(!menu.sub2.isEmpty()) orderList += ","+menu.sub2;
		if(!menu.sub3.isEmpty()) orderList += ","+menu.sub3;
		if(!menu.description.isEmpty()) orderList += ","+menu.description;
		
		return orderList;
	}
	private void cancelOrder(int n) {
		int key=0;
		//삭제요청 들어오면 먼저, 해당하는 번호를 찾는다
		for(int i=0;i<model.getSize();i++) {
			String msg = (String) model.getElementAt(i);
			String arr[] = msg.split("번");
			key = Integer.parseInt(arr[0]);
			if(n==key) {
				map.remove(n); 
				model.remove(i); 
				list.setModel(model);
				break;
			}
		}
	}
	private void updateList() {
		model.addElement(count+"번: "+map.get(count));
		list.setModel(model);
		count++;
	}

	private void sendMsg(String data) throws IOException {
		String msg = data;
		byte[] buffer = msg.getBytes();
		DatagramPacket dp = new DatagramPacket(buffer,buffer.length,InetAddress.getByName(DEST_IP),port);
		ds.send(dp);		
		System.out.println("[Server -> Client] : "+msg);
	}
	private void setView() {
		setTitle("Store");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 468, 285);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Store");
		lblNewLabel.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel.setBounds(105, 10, 57, 15);
		contentPane.add(lblNewLabel);

		list = new JList(data.toArray());
		list.setBounds(31, 25, 300, 205);
		getContentPane().add(list);

		btn1 = new JButton("1");
		btn1.setFont(new Font("돋움", Font.PLAIN, 12));
		btn1.setBounds(350, 25, 50, 23);
		contentPane.add(btn1);

		btn2 = new JButton("2");
		btn2.setFont(new Font("돋움", Font.PLAIN, 12));
		btn2.setBounds(350, 85, 50, 23);
		contentPane.add(btn2);

		btn3 = new JButton("3");
		btn3.setFont(new Font("돋움", Font.PLAIN, 12));
		btn3.setBounds(350, 145, 50, 23);
		contentPane.add(btn3);

		btn4 = new JButton("4");
		btn4.setFont(new Font("돋움", Font.PLAIN, 12));
		btn4.setBounds(350, 205, 50, 23);
		contentPane.add(btn4);

	}

	private void setTime() {
		LocalDateTime timePoint = LocalDateTime.now();
		hour = timePoint.getHour();
		minute = timePoint.getMinute();	
	}

	private void getTime() {
		LocalDateTime checkPoint = LocalDateTime.now();
		int m_Hour = hour - checkPoint.getHour();
		int m_Minute = minute - checkPoint.getMinute();	
		if(m_Hour == 0) {
			timer = -m_Minute;
		}
		else if(m_Hour < 0) {
			timer = -m_Hour*60 - m_Minute;
		}
		else {
			timer = 24 - (m_Hour)*60 - m_Minute;
		}
	}

	public static void main(String[] args) {
		Store store = new Store();
		store.setVisible(true);
		store.recvPacket();
	}

}