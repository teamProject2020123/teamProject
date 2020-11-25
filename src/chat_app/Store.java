package chat_app;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Store extends JFrame{
	private DatagramSocket ds;
	private DefaultListModel model = new DefaultListModel();;
	private ArrayList<String> data = new ArrayList<>();
	private static final String DEST_IP = "127.0.0.1";
	private static final int DEST_PORT = 1004;
	private byte[] buffer;
	private JPanel contentPane;
	private JTextField textField;
	private JList list;
	private int count =0;
	private JButton btnNewButton,btnNewButton_1,btnNewButton_2,btnNewButton_3;
	public Store() {
		setView();
		try {
			ds = new DatagramSocket(1004);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	private void recvPacket() {
		try {
			while(true) {
				buffer = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ds.receive(dp);
				String recvData = new String(dp.getData(), 0, dp.getLength());
				if(recvData.startsWith("ORDER")) {
					data.add(recvData.substring(6));
					updateList();
					sendMsg("OK");
				}
			}
		} catch (Exception e) {
			ds.close();
			e.printStackTrace();
		}
	}
	private void updateList() {
		model.addElement(count+1+": "+data.get(count));
		list.setModel(model);
		count++;
	}
	private void sendMsg(String data) throws IOException {
		String msg = data;
		byte[] buffer = msg.getBytes();
		DatagramPacket dp = new DatagramPacket(buffer,buffer.length,InetAddress.getByName(DEST_IP),DEST_PORT);
		ds.send(dp);		
		System.out.println("Server : "+msg);
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

		JLabel lblNewLabel = new JLabel("\uC8FC\uBB38 \uB0B4\uC5ED");
		lblNewLabel.setFont(new Font("µ¸¿ò", Font.PLAIN, 12));
		lblNewLabel.setBounds(105, 10, 57, 15);
		contentPane.add(lblNewLabel);
		
		list = new JList(data.toArray());
		list.setBounds(31, 25, 300, 205);
		getContentPane().add(list);

		btnNewButton = new JButton("1");
		btnNewButton.setFont(new Font("µ¸¿ò", Font.PLAIN, 12));
		btnNewButton.setBounds(350, 25, 50, 23);
		contentPane.add(btnNewButton);

		btnNewButton_1 = new JButton("2");
		btnNewButton_1.setFont(new Font("µ¸¿ò", Font.PLAIN, 12));
		btnNewButton_1.setBounds(350, 85, 50, 23);
		contentPane.add(btnNewButton_1);

		btnNewButton_2 = new JButton("3");
		btnNewButton_2.setFont(new Font("µ¸¿ò", Font.PLAIN, 12));
		btnNewButton_2.setBounds(350, 145, 50, 23);
		contentPane.add(btnNewButton_2);

		btnNewButton_3 = new JButton("4");
		btnNewButton_3.setFont(new Font("µ¸¿ò", Font.PLAIN, 12));
		btnNewButton_3.setBounds(350, 205, 50, 23);
		contentPane.add(btnNewButton_3);
	}

	public static void main(String[] args) {
		Store store = new Store();
		store.setVisible(true);
		OrderFood orderFood = new OrderFood();
		orderFood.setVisible(true);
		store.recvPacket();
	}

}
