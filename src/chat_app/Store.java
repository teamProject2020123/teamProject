package chat_app;
import java.awt.Font;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Store extends JFrame{
	private DatagramSocket ds;
	private DefaultListModel model = new DefaultListModel();
	private ArrayList<String> data = new ArrayList<>();
	private HashMap<Integer, String> map = new HashMap<Integer, String>();
	Set<Entry<Integer, String>> entries;
	private int orderSeq;
	private static final String DEST_IP = "127.0.0.1";
	private int port;
	private byte[] buffer;
	private JPanel contentPane;
	private JList<String> list;
	private int count =1;
	private JButton btn1,btn2,btn3,btn4;
	public Store() {
		setView();
		entries = map.entrySet();
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
					map.put(count, recvData.substring(6)); //HashMap에 Order 내용 추가
					updateList();
					sendMsg("SUCCESS\nTIME=5");
					sendMsg("ORDER_NUMBER="+(++orderSeq));
				} else if(recvData.equals("TIME")) {
					sendMsg("TIME TO ARRIVE=5");
				} else if(recvData.startsWith("CANCEL")) {
					int num = Integer.parseInt(recvData.substring(7));
					System.out.println("ordernumber = "+num);
					cancelOrder(num);
					sendMsg("CANCEL_OK");
				}
			}
		} catch (Exception e) {
			ds.close();
			e.printStackTrace();
		}
	}
	private void cancelOrder(int n) {
		int key=0;
		//삭제요청 들어오면 먼저, 해당하는 번호를 찾는다
		for(int i=0;i<model.getSize();i++) {
			String msg = (String) model.getElementAt(i);
			String arr[] = msg.split("번");
			key = Integer.parseInt(arr[0]);
			if(n==key) {
				//삭제하도록
				map.remove(n); //oderSeq 에 맞는 HashMap 데이터 삭제
				model.remove(i); //리스트에 보이는건 다를 수있다... -> 별도의 카운트 생성?
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

	public static void main(String[] args) {
		Store store = new Store();
		store.setVisible(true);
		store.recvPacket();
	}

}