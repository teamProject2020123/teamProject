package chat_app;
import java.awt.Font;
import java.io.IOException;
import java.time.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Store extends JFrame{
	private DatagramSocket ds;
	private DefaultListModel model = new DefaultListModel();
	private ArrayList<String> data = new ArrayList<>();
	private int orderSeq;
	private static final String DEST_IP = "127.0.0.1";
	//	private static final int DEST_PORT;
	private int port;
	private byte[] buffer;
	private JPanel contentPane;
	private JList<String> list;
	private int count =0;
	private JButton btn1,btn2,btn3,btn4;


	private int hour;
	private int minute;
	private int deliverTime = 10;
	private int timer = 0;

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
					//					System.out.println("[Client->Server] : "+recvData);
					System.out.println("====================================");
					data.add(recvData.substring(6));
					updateList();
					sendMsg("SUCCESS\nTIME=5");
					sendMsg("ORDER_NUMBER="+(++orderSeq));	
					setTime();
				}
				else if(recvData.startsWith("TIME")) {
					getTime();
					sendMsg("TIME="+(deliverTime - timer));

				} else if(recvData.startsWith("CANCEL")) {
					int num = Integer.parseInt(recvData.substring(7));
					System.out.println("ordernumber = "+num);
					//					data.remove(orderNumber);
					cancelOrder(num-1);
					sendMsg("CANCEL_OK");
				}

			}
		} catch (Exception e) {
			ds.close();
			e.printStackTrace();
		}
	}
	private void cancelOrder(int n) {
		model.remove(n);
		model.add(n, "CANCELED ORDER");
		list.setModel(model);
	}
	private void updateList() {
		model.addElement(count+1+": "+data.get(count));
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
		store.setVisible(true);
		store.recvPacket();
	}

}
