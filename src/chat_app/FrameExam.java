package chat_app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.*;

public class FrameExam extends JFrame {

	private JPanel contentPane;
	private JButton orderButton, exitButton;
	private OrderFood orderFood = new OrderFood();
	private UDPClient udp = new UDPClient();
	private ArrayList<String> recvPacket;
	private JLabel ipLabel;
	public FrameExam() {
		setView();
		udp.dataReceiver(); //여기서 패킷을 받기를 기다림
		orderButton = new JButton("주문");
		orderButton.setFont(new Font("돋움", Font.PLAIN, 12));
		orderButton.setBounds(40, 94, 168, 134);

		exitButton = new JButton("종료");
		exitButton.setFont(new Font("돋움", Font.PLAIN, 12));
		exitButton.addActionListener(e-> {System.exit(0);});
		exitButton.setBounds(248, 94, 162, 134);

		listener();
		ipLabel = new JLabel("IP : " +udp.getInetAddress()+ " / UDP");
		ipLabel.setBounds(49, 10, 326, 49);

		contentPane.add(orderButton);
		contentPane.add(exitButton);
		contentPane.add(ipLabel);

	}
	private void listener() {
		orderButton.addActionListener(e-> {
			orderButton.setEnabled(false);
			udp.sendMsg("CALL");
			new Thread(()->{
				try {
					Thread.sleep(1500);
				} catch (InterruptedException error) {
					error.printStackTrace();
				}
				recvPacket = udp.getArrPacket();
				for(int i=0;i<recvPacket.size();i++) {
					udp.checkPacket(recvPacket.get(i));
				}
				orderFood.setVisible(true);
				orderButton.setEnabled(true);
				dispose();
			}).start();
		});
	}
	private void setView() {
		setTitle("배달의 마왕");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
//	public static void main(String[] args) {
//		FrameExam frameExam = new FrameExam();
//		frameExam.setVisible(true);
//	}
}

