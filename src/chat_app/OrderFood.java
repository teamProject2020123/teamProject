package chat_app;

import javax.swing.*;
import java.awt.TextArea;

import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;

public class OrderFood extends JFrame implements Runnable {

	private static final int DEST_PORT = 1004;
	private static final String DEST_IP = "127.0.0.1";
	private int myOrderSeq;
	private JPanel contentPane;
	private JButton orderButton,resetButton,cancelButton,arriveButton;
	private JComboBox<String> comboBox,comboBox1,comboBox2,comboBox3,cb;
	private ArrayList<String> main,sub1,sub2,sub3,list;
	private TextArea needText;
	private String packet;
	private InetAddress ip;
	private DatagramSocket ds;

	public OrderFood() {
		main = new ArrayList<>();
		sub1 = new ArrayList<>();
		sub2 = new ArrayList<>();
		sub3 = new ArrayList<>();
		main.add("Chicken");
		main.add("Pizza");
		main.add("Pork");
		setChicken();
		setView();
		orderButton = new JButton("�ֹ��ϱ�");
		orderButton.setFont(new Font("����", Font.PLAIN, 12));
		orderButton.setBounds(65, 200, 97, 23);
		contentPane.add(orderButton);
		actionListener();
		
		
        try {
            ip = InetAddress.getByName(DEST_IP);
            ds = new DatagramSocket();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Thread th = new Thread(this);
        th.start();
	}
	private void actionListener() {
		orderButton.addActionListener(e-> {
			list = new ArrayList<>();
			list.add(comboBox.getSelectedItem().toString());
			list.add(comboBox1.getSelectedItem().toString());
			list.add(comboBox2.getSelectedItem().toString());
			list.add(comboBox3.getSelectedItem().toString());

			String t = needText.getText();
			if(t.equals("")) {
				list.add("No Description");
			} else list.add(needText.getText());

			packet = "ORDER\n"+list.toString();

			int result = JOptionPane.showConfirmDialog(null, "�Ʒ� ������ �³���?\n"
					+ packet.substring(6) ,"CHECK_ORDER", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(result == JOptionPane.YES_OPTION) { //������ �´ٰ� Ȯ�� ������
				sendMsg(packet);
			}
		});
		comboBox.addItemListener(e-> {
			cb = (JComboBox) e.getSource();
			String index = (String) cb.getSelectedItem();
			if(index == "Chicken") {
				setChicken();
				setComboBox();
			}
			else if(index=="Pizza") {
				setPizza();
				setComboBox();
			} else if(index=="Pork"){
				setPork();
				setComboBox();
			}
		});
		resetButton.addActionListener(e-> {
			main.clear();
			main.add("Chicken");
			main.add("Pizza");
			main.add("Pork");
			comboBox.setModel(new DefaultComboBoxModel(main.toArray()));
			setChicken();
			setComboBox();
			needText.setText("");
		});
		cancelButton.addActionListener(e-> {
			int result = JOptionPane.showConfirmDialog(null, "���� �ֹ��� ����Ͻðڽ��ϱ�?"
					,"CHECK_CANCEL", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(result == JOptionPane.YES_OPTION) {
				sendMsg("CANCEL="+myOrderSeq);
				System.exit(0);
//				enableBtn();
			}
		});
		arriveButton.addActionListener(e->{
			sendMsg("TIME");
		});
	}
	private void setView() {
		setTitle("����� ���� - �� �ֹ�");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("���� �޴�");
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel.setBounds(12, 50, 57, 15);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("�߰� �ɼ�1");
		lblNewLabel_1.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(12, 85, 70, 15);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("�߰� �ɼ�2");
		lblNewLabel_2.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(12, 120, 70, 15);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_2_1 = new JLabel("�߰� �ɼ�3");
		lblNewLabel_2_1.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel_2_1.setBounds(12, 155, 70, 15);
		contentPane.add(lblNewLabel_2_1);

		comboBox = new JComboBox(main.toArray());
		comboBox.setBounds(92, 45, 138, 23);
		comboBox.setFont(new Font("����",Font.PLAIN,12));
		contentPane.add(comboBox);

		comboBox1 = new JComboBox(sub1.toArray());
		comboBox1.setBounds(92, 80, 138, 23);
		comboBox1.setFont(new Font("����",Font.PLAIN,12));
		contentPane.add(comboBox1);

		comboBox2 = new JComboBox(sub2.toArray());
		comboBox2.setBounds(92, 115, 138, 23);
		comboBox2.setFont(new Font("����",Font.PLAIN,12));
		contentPane.add(comboBox2);

		comboBox3 = new JComboBox(sub3.toArray());
		comboBox3.setBounds(92, 150, 138, 23);
		comboBox3.setFont(new Font("����",Font.PLAIN,12));
		contentPane.add(comboBox3);

		JLabel lblNewLabel_3 = new JLabel("�ֹ��� �߰� �䱸����");
		lblNewLabel_3.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(242, 50, 118, 15);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("������ ���͵帱���?");
		lblNewLabel_4.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(147, 10, 163, 15);
		contentPane.add(lblNewLabel_4);

		needText = new TextArea();
		needText.setBounds(238, 81, 186, 94);
		contentPane.add(needText);

		arriveButton = new JButton("�����ð�");
		arriveButton.setFont(new Font("����", Font.PLAIN, 12));
		arriveButton.setBounds(221, 200, 97, 23);
		contentPane.add(arriveButton);
		arriveButton.setEnabled(false);

		resetButton = new JButton("�ʱ�ȭ");
		resetButton.setFont(new Font("����", Font.PLAIN, 12));
		resetButton.setBounds(65, 228, 97, 23);
		contentPane.add(resetButton);

		cancelButton = new JButton("�ֹ����");
		cancelButton.setFont(new Font("����", Font.PLAIN, 12));
		cancelButton.setBounds(221, 228, 97, 23);
		contentPane.add(cancelButton);
		cancelButton.setEnabled(false);
	}
	private void setComboBox() {
		comboBox1.setModel(new DefaultComboBoxModel(sub1.toArray()));
		comboBox2.setModel(new DefaultComboBoxModel(sub2.toArray()));
		comboBox3.setModel(new DefaultComboBoxModel(sub3.toArray()));
	}
	private void setPork() {
		sub1.clear();
		sub2.clear();
		sub3.clear();
		sub1.add("Normal");
		sub1.add("No Hot");
		sub1.add("Hot");
		sub1.add("Very Hot");
		sub2.add("Coke 500ml");
		sub2.add("Cider 500ml");
		sub2.add("Coke 1.5L");
		sub2.add("Cider 1.5L");
		sub3.add("No");
	}
	private void setPizza() {
		sub1.clear();
		sub2.clear();
		sub3.clear();
		sub1.add("No");
		sub1.add("Cheese Crust");
		sub2.add("Coke 500ml");
		sub2.add("Cider 500ml");
		sub2.add("Coke 1.5L");
		sub2.add("Cider 1.5L");
		sub3.add("No");
		sub3.add("Add Onion Source");
	}
	private void setChicken() {
		sub1.clear();
		sub2.clear();
		sub3.clear();
		sub1.add("Boneless");
		sub1.add("Bone");
		sub2.add("Coke 500ml");
		sub2.add("Cider 500ml");
		sub2.add("Coke 1.5L");
		sub2.add("Cider 1.5L");
		sub3.add("NO");
		sub3.add("Add Hot Source");
	}
	private void disableBtn() {
		orderButton.setEnabled(false);
		resetButton.setEnabled(false);
		arriveButton.setEnabled(true);
		comboBox.setEnabled(false);
		comboBox1.setEnabled(false);
		comboBox2.setEnabled(false);
		comboBox3.setEnabled(false);
		needText.setEnabled(false);
		cancelButton.setEnabled(true);
	}
	private void enableBtn() {
		orderButton.setEnabled(true);
		resetButton.setEnabled(true);
		arriveButton.setEnabled(false);
		comboBox.setEnabled(true);
		comboBox1.setEnabled(true);
		comboBox2.setEnabled(true);
		comboBox3.setEnabled(true);
		needText.setEnabled(true);
		cancelButton.setEnabled(false);
	}
	
	public void ArriveTime(String msg) {
		int result = JOptionPane.showConfirmDialog(null, "���������ð�\n"
				+ msg ,"Arrive_Time", JOptionPane.CLOSED_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(result == JOptionPane.CLOSED_OPTION) { //������ �´ٰ� Ȯ�� ������
			dispose();
		}
	}
	
	@Override
	public void run() {
		while( true ) { 
			byte[] buf = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				ds.receive(packet);
				String recvData = new String(packet.getData(), 0, packet.getLength());
				recvMsg(recvData);
			} catch (Exception e) {
				ds.close();
				e.printStackTrace();
				break;
			} 
		}
	}
	private void recvMsg(String recvData) {
		if(recvData.startsWith("SUCCESS")) {
			recvData = recvData.substring(7);
			String temp[] = recvData.split("=");
			JOptionPane.showMessageDialog(this, "���� �ҿ� �ð��� "+temp[1]+"�� �Դϴ�.");
			disableBtn();
		} else if(recvData.startsWith("ORDER_NUMBER")) {
			recvData = recvData.substring(11);
			String temp[] = recvData.split("=");
			myOrderSeq = Integer.parseInt(temp[1]);
			System.out.println("�� �ֹ� ��ȣ�� : "+myOrderSeq);
		} else if(recvData.startsWith("TIME")) {
			String temp[] = recvData.split("=");
			ArriveTime(temp[1]);
		} else if(recvData.startsWith("CANCEL")) {
			JOptionPane.showMessageDialog(this, "�ֹ��Ͻ� �޴��� ��ҵǾ����ϴ�.","��Ҿ˸�",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void sendMsg(String msg) {
		try {
			byte[] buffer = msg.getBytes();
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length, ip, DEST_PORT);
			ds.send(dp);
			System.out.println("[Client -> Server] : "+msg);
		} catch (Exception e) {
			ds.close();
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		OrderFood orderFood = new OrderFood();
		orderFood.setVisible(true);
	}

}