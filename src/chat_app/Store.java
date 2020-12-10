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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

public class Store extends JFrame{
	private DatagramSocket ds;
	private DefaultListModel model = new DefaultListModel();
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
	private JButton showTotalList,showCanceledList,btn3,exitButton;
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
				if(recvData.startsWith("ORDER")) {
					System.out.println("====================================");
					recvData = recvData.substring(6);
					if(currentList.size()<3) { // 주문이 3개 이하일때 정상처리
						totalList.put(count, parsingJson(recvData));
						currentList.put(count, parsingJson(recvData)); //HashMap에 Order 내용 추가
						updateList();
						sendMsg("SUCCESS");
						sendMsg("ORDER_NUMBER="+(++orderSeq));					
						addUserList(orderSeq,port);
					} else { 
						//시간이 더 걸리는데 괜찮냐는 메시지 전송
						sendMsg("MORE_TIME_CHECK");
					}
				} else if(recvData.startsWith("TIME")) {
					String[] arr = recvData.substring(5).split(":");		
					getTime(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]));				
					if(arr[2].equals("Chicken")) {
						deliverTime = CHICKEN_TIME;
					} else if(arr[2].equals("Pizza")) {
						deliverTime = PIZZA_TIME;				
					} else if(arr[2].equals("Pork")){
						deliverTime = PORK_TIME;				
					}
					sendMsg("TIME="+(deliverTime - timer));
				} else if(recvData.startsWith("mTIME")) {
					String[] arr = recvData.substring(6).split(":");		
					getTime(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]));				
					if(arr[2].equals("Chicken")) {
						deliverTime = CHICKEN_TIME+10;
					} else if(arr[2].equals("Pizza")) {
						deliverTime = PIZZA_TIME+10;				
					} else if(arr[2].equals("Pork")){
						deliverTime = PORK_TIME+10;				
					}
					sendMsg("TIME="+(deliverTime - timer));
				} else if(recvData.startsWith("CANCEL")) {
					int num = Integer.parseInt(recvData.substring(7));
					if((deliverTime-timer)>cookTime) { //요리가 시작되면 주문 취소를 할 수 없게 하기 위해 작성함
						cancelOrder(num);
						sendMsg("CANCEL_OK");						
					} else if((deliverTime-timer)<=cookTime) {
						sendMsg("CANCEL_FAIL");
					}
				} else if(recvData.startsWith("OK")) {
					recvData = recvData.substring(9);//OK\nORDER\n짜르고 json데이터만
					currentList.put(count, parsingJson(recvData));
					updateList();
					sendMsg("SUCCESS");
					sendMsg("ORDER_NUMBER="+(++orderSeq));
				} else if(recvData.startsWith("No")) {
					System.out.println("시간이 오래걸려 사용자쪽에서 주문을 취소함");
				}

			}
		} catch (Exception e) {
			ds.close();
			e.printStackTrace();
		}
	}
	private void addUserList(int orderSeq, int port) {
//		for(int i=1;i<=userList.size();i++) {
//			//userList돌면서 port 중복체크
//			if(userList.get(i)==port) {
//				break;
//			} else {
//				continue;
//			}
//		}
//		userList.put(orderSeq, port);
	}
	private String parsingJson(String recvData) {
		Gson gson = new Gson();
		Menu menu = gson.fromJson(recvData,Menu.class);
		String orderList = menu.main;
		if(menu.main.equals("Chicken")) {	
			deliverTime += CHICKEN_TIME;	
			cookTime = CHICKEN_TIME * 0.8f;
		} else if(menu.main.equals("Pizza")) {
			deliverTime += PIZZA_TIME;	
			cookTime = PIZZA_TIME * 0.8f;
		} else {
			deliverTime += PORK_TIME;	
			cookTime = PORK_TIME * 0.8f;
		}
		if(!menu.sub1.isEmpty()) orderList += ","+menu.sub1;
		if(!menu.sub2.isEmpty()) orderList += ","+menu.sub2;
		if(!menu.sub3.isEmpty()) orderList += ","+menu.sub3;
		if(!menu.description.isEmpty()) orderList += ","+menu.description;
		System.out.println("오더 리스트는 "+orderList);
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
				currentList.remove(n); 
				model.remove(i);
				list.setModel(model);
				break;
			}
		}
	}
	private void updateList() {
		model.addElement(count+"번: "+currentList.get(count));
		list.setModel(model);
		count++;
	}
	private void actionListener() {
		showTotalList.addActionListener(e->{
			if(totalList.size()==0) {
				JOptionPane.showMessageDialog(this, "주문 내역이 없습니다.", "Error",JOptionPane.CANCEL_OPTION);
			}else {
				String data = "";
				for(int i=1;i<=totalList.size();i++) {
					data += i+"번 주문: "+totalList.get(i)+"\n";
				}
				JOptionPane.showMessageDialog(this, data, "총 주문 내역",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		showCanceledList.addActionListener(e->{
			
		});
		exitButton.addActionListener(e->{
			//모든 사용자에게 브로드캐스트로 주문이 취소되었음을 알리고 종료함
			int result = JOptionPane.showConfirmDialog(null, "종료하면 모든 주문이 취소됩니다. "
					+ "그래도 취소하시겠습니까??",
					"종료 확인", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(result == JOptionPane.YES_OPTION) {
				
				System.exit(0);
			}
		});
	}
	private void broadcastMsg() {
		
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
		setBounds(100, 100, 530, 285);
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

		showTotalList = new JButton("총 주문 내역");
		showTotalList.setFont(new Font("돋움", Font.PLAIN, 12));
		showTotalList.setBounds(350, 25, 150, 23);
		contentPane.add(showTotalList);

		showCanceledList = new JButton("2");
		showCanceledList.setFont(new Font("돋움", Font.PLAIN, 12));
		showCanceledList.setBounds(350, 85, 150, 23);
		contentPane.add(showCanceledList);

		btn3 = new JButton("3");
		btn3.setFont(new Font("돋움", Font.PLAIN, 12));
		btn3.setBounds(350, 145, 150, 23);
		contentPane.add(btn3);

		exitButton = new JButton("4");
		exitButton.setFont(new Font("돋움", Font.PLAIN, 12));
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

	public static void main(String[] args) {
		Store store = new Store();
		store.setVisible(true);
		store.recvPacket();
	}

}