package chat_app;
import java.awt.Font;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;


public class Store extends JFrame{
	private DatagramSocket ds;
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private ArrayList<String> data = new ArrayList<>();
	private HashMap<Integer, String> currentList = new HashMap<Integer, String>();
	private HashMap<Integer, String> totalList = new HashMap<Integer, String>();
	private HashMap<Integer, Integer> userList = new HashMap<Integer, Integer>();
	private int orderSeq;
	private static final String DEST_IP = "127.0.0.1";
	private int port;
	private byte[] buffer;
	private JPanel contentPane;
	private JList<String> list;
	private int count =1;
	private JButton showTotalList,showCanceledList,deliverButton,exitButton;
	private int deliverTime=0;
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
		actionListener();

	}

	private void recvPacket() {
		try {
			while(true) {
				buffer = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ds.receive(dp);
				port=dp.getPort();
				String recvData = new String(dp.getData(), 0, dp.getLength());
				recvMsg(recvData); 
			}
		} catch (Exception e) {
			ds.close();
			e.printStackTrace();
		}
	}
	private void recvMsg(String recvData) throws IOException {
		if(recvData.startsWith("ORDER")) {
			System.out.println("====================================");
			recvData = recvData.substring(5);
			if(currentList.size()<3) { // �ֹ��� 3�� �����϶� ����ó��
				totalList.put(count, parseOrder(recvData));
				currentList.put(count, parseOrder(recvData)); //HashMap�� Order ���� �߰�
				updateList();
				System.out.println("deliverTime="+deliverTime);
				//���⼭ deliverTime�� 20���� ���޵� ���� t=deliverTime-timer �ѹ�
				//�� ����� ������ ó���� 20���� ���� �׷���
				sendMsg(parseToJson("SUCCESS",++orderSeq,deliverTime));
				addUserList(orderSeq,port); //userList�� �߰�
			} else { 
				//�ð��� �� �ɸ��µ� �����Ĵ� �޽��� ����
				sendMsg("MORE_TIME_CHECK");
			}
		} 
		else if(recvData.startsWith("{")) {
			int t = deliverTime-timer;
			Gson gson = new Gson();
			Packet_RESPONSE response = gson.fromJson(recvData, Packet_RESPONSE.class);
			Packet_TIME packet_TIME = gson.fromJson(recvData, Packet_TIME.class);
			String methods = response.method;
			if(methods.equals("TIME")) {
				getTime(packet_TIME.hour,packet_TIME.min);
				switch (packet_TIME.data) {
				case "Chicken":
					deliverTime = CHICKEN_TIME;
					break;
				case "Pizza":
					deliverTime = PIZZA_TIME;
					break;
				default:
					deliverTime = PORK_TIME;
				}
				int extraSeq = searchOrderCount(packet_TIME.seq);
				t = deliverTime - timer + (extraSeq / 3) * 5;
				if(t<0){
					t=0;
					sendMsg(parseToJson("SORRY",t));
				}
				else
					sendMsg(parseToJson("TIME",t));

			} else if(methods.equals("CANCEL")) {
				if(t>cookTime) { //�丮�� ���۵Ǹ� �ֹ� ��Ҹ� �� �� ���� �ϱ� ���� �ۼ���
					cancelOrder(response.number);
					sendMsg("CANCEL_OK");						
				} else if(t<=cookTime) {
					sendMsg("CANCEL_FAIL");
				}
			} 
		} else if(recvData.startsWith("OK")) {
			recvData = recvData.substring(8);//OK\nORDER\n¥���� json�����͸�
			totalList.put(count, parseOrder(recvData));
			currentList.put(count, parseOrder(recvData));
			updateList();
			sendMsg(parseToJson("SUCCESS",++orderSeq));
			addUserList(orderSeq,port);
		}
	}
	private String parseToJson(String method, int number) {
		Gson gson = new Gson();
		Packet_RESPONSE p = new Packet_RESPONSE(method,number);
		String data = gson.toJson(p);
		return data;
	}
	private String parseToJson(String method, int number, int time) {
		Gson gson = new Gson();
		Packet_initialTime p = new Packet_initialTime(method,number,time);
		String data = gson.toJson(p);
		return data;
	}
	private void addUserList(int orderSeq, int port) {
		if(!userList.containsValue(port)) {
			userList.put(orderSeq, port);
		}
	}
	private String parseOrder(String recvData) {
		Gson gson = new Gson();
		Menu menu = gson.fromJson(recvData,Menu.class);
		String orderList = menu.main;
		if(menu.main.equals("Chicken")) {	
			deliverTime += CHICKEN_TIME;	
			cookTime = CHICKEN_TIME * 0.8f;
		} else if(menu.main.equals("Pizza")) {
			deliverTime += PIZZA_TIME;	
			cookTime = PIZZA_TIME * 0.8f;
		} else if(menu.main.equals("Pork")){
			deliverTime += PORK_TIME;	
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
		//������û ������ ����, �ش��ϴ� ��ȣ�� ã�´�
		for(int i=0;i<model.getSize();i++) {
			String msg = (String) model.getElementAt(i);
			String arr[] = msg.split("��");
			key = Integer.parseInt(arr[0]);
			if(n==key) {
				currentList.remove(n); 
				model.remove(i);
				list.setModel(model);
				break;
			}
		}
	}
	private void actionListener() {
		showTotalList.addActionListener(e->{
			if(totalList.size()==0) {
				JOptionPane.showMessageDialog(this, "�ֹ� ������ �����ϴ�.", "Error",JOptionPane.CANCEL_OPTION);
			}else {
				String data = "";
				for(int i=1;i<=totalList.size();i++) {
					data += i+"�� �ֹ�: "+totalList.get(i)+"\n";
				}
				JOptionPane.showMessageDialog(this, data, "�� �ֹ� ����",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		deliverButton.addActionListener(e->{
			/*
			 * 1. ����Ʈ�� �����ϰ� ��ư Ŭ��
			 * 2. ������ �ε����� ����Ʈ�� ����
			 * 3. ������ ����Ʈ�� ��ȣ�� �о��
			 * 4. �о�� ��ȣ�� �ش��ϴ� userList�� key�� �̿��� �ش� ��Ʈ�� ����
			 */
			int index = list.getSelectedIndex();
			for(int i=0;i<model.size();i++) {
				System.out.println(model.getElementAt(i));
			}
			if (index > -1) { //�ε����� ����� ���õǾ�����
				String data = (String) model.getElementAt(index);
				String arr[] = data.split("��");
				int user_key = Integer.parseInt(arr[0]);
				//userKey�� ���õ� ����Ʈ�� ��ȣ�� �Է���.
				currentList.remove(index);
				model.remove(index);
				list.setModel(model);
				//�ε����� �����ϰ� ����Ʈ�� ������
				
				int currentPort=userList.get(user_key);
				sendMsg("DELIVER",currentPort);
			} else { //����Ʈ�� �������� �ʰ� ��� ��ư�� ������ ��
				JOptionPane.showMessageDialog(this, "�ƹ��͵� ���õ��� �ʾҽ��ϴ�", "����",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		exitButton.addActionListener(e->{
			//��� ����ڿ��� ��ε�ĳ��Ʈ�� �ֹ��� ��ҵǾ����� �˸��� ������
			if(model.isEmpty()) {
				System.exit(0);
			} else {
				int result = JOptionPane.showConfirmDialog(null, "�����ϸ� ��� �ֹ��� ��ҵ˴ϴ�. "
						+ "�׷��� ����Ͻðڽ��ϱ�??",
						"���� Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(result == JOptionPane.YES_OPTION) {
					currentList.clear();
					model.clear();
					list.setModel(model);
					broadcastMsg("CLOSED");
					System.exit(0);
				}
			}
		});
	}
	private void updateList() {
		model.addElement(count+"��: "+currentList.get(count));
		list.setModel(model);
		count++;
	}

	private void broadcastMsg(String msg) {
		//userList�� �ִ� ��� ��Ʈ�� �޽��� ��������.
		byte[] buffer = msg.getBytes();
		DatagramPacket dp;
		for(int i=1;i<=userList.size();i++) {
			try {
				System.out.println("port : "+userList.get(i));
				dp = new DatagramPacket(buffer,buffer.length,
						InetAddress.getByName(DEST_IP),userList.get(i));
				ds.send(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("[Notice] : "+msg);
	}
	private void sendMsg(String data) {
		try {
			byte[] buffer = data.getBytes();
			DatagramPacket dp = new DatagramPacket(buffer,buffer.length,InetAddress.getByName(DEST_IP),port);
			ds.send(dp);
			System.out.println("[Server -> Client] : "+data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	private void sendMsg(String data,int port) {
		try {
			byte[] buffer = data.getBytes();
			DatagramPacket dp = new DatagramPacket(buffer,buffer.length,InetAddress.getByName(DEST_IP),port);
			ds.send(dp);
			System.out.println("[Server -> Client] : "+data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	private void setView() {
		setTitle("Store");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 530, 285);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Store");
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel.setBounds(105, 10, 57, 15);
		contentPane.add(lblNewLabel);

		list = new JList(data.toArray());
		list.setBounds(31, 25, 300, 205);
		getContentPane().add(list);

		showTotalList = new JButton("�� �ֹ� ����");
		showTotalList.setFont(new Font("����", Font.PLAIN, 12));
		showTotalList.setBounds(350, 25, 150, 23);
		contentPane.add(showTotalList);

		deliverButton = new JButton("����ϱ�");
		deliverButton.setFont(new Font("����", Font.PLAIN, 12));
		deliverButton.setBounds(350, 145, 150, 23);
		contentPane.add(deliverButton);

		exitButton = new JButton("4");
		exitButton.setFont(new Font("����", Font.PLAIN, 12));
		exitButton.setBounds(350, 205, 150, 23);
		contentPane.add(exitButton);

	}

	private void getTime(int hour, int minute) {
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

	private int searchOrderCount(int orderSeq) {
		int count=0;
		Iterator<Integer> iter = currentList.keySet().iterator();
		while(iter.hasNext()) {
			int key = iter.next();
			if(key<orderSeq)
				count++;	
		}
		return count;
	}

	public static void main(String[] args) {
		Store store = new Store();
		store.setVisible(true);
		store.recvPacket();
	}

}