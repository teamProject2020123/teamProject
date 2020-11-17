package chat_app;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class FrameExam extends JFrame {

	private JPanel contentPane;
	private OrderFood orderFood = new OrderFood();
	private UDPmsg udpmsg = new UDPmsg();
	
	public FrameExam() {
		setView();
		JButton OrderBtn = new JButton("\uC8FC\uBB38");
		OrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				udpmsg.sendMsg("CALL to Store by client");
				orderFood.setVisible(true);
				dispose();
			}
		});
		OrderBtn.setBounds(40, 94, 168, 134);
		contentPane.add(OrderBtn);
		
		JButton exitBtn = new JButton("\uC885\uB8CC");
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				dispose();
				System.exit(0);
			}
		});
		exitBtn.setBounds(248, 94, 162, 134);
		contentPane.add(exitBtn);
		
		JLabel lblNewLabel = new JLabel("IP : 127.0.0.1/UDP");
		lblNewLabel.setBounds(49, 10, 326, 49);
		contentPane.add(lblNewLabel);
		
	}
	private void setView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameExam frame = new FrameExam();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
