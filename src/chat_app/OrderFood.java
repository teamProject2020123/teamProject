package chat_app;

import java.awt.Font;
import java.awt.TextArea;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

public class OrderFood extends JFrame implements Runnable {

	private static final int DEST_PORT = 1004;
	private static final String DEST_IP = "127.0.0.1";
	private int myOrderSeq;
	private JPanel contentPane;
	private JButton orderButton,resetButton,cancelButton,arriveButton;
	private JComboBox<String> cb;
	private JComboBox<String>[] comboBox = new JComboBox[4];
	private ArrayList<String> main,option1,option2,option3,list;
	private JLabel mainLabel,optionLabel1,optionLabel2,optionLabel3;
	private TextArea needText;
	private Menu menu;
	private String packet;
	private InetAddress ip;
	private DatagramSocket ds;
	private boolean cancel = true;
	
	private int hour,min;

	public OrderFood() {
		main = new ArrayList<>();
		option1 = new ArrayList<>();
		option2 = new ArrayList<>();
		option3 = new ArrayList<>();
		main.add("Chicken");
		main.add("Pizza");
		main.add("Pork");
		setView();
//		setChicken();
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
        this.setVisible(true);
	}
	private void actionListener() {
		orderButton.addActionListener(e-> {
			parsingJson();
			int result = JOptionPane.showConfirmDialog(null, "�ֹ��Ͻðڽ��ϱ�?"
					,"CHECK_ORDER", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(result == JOptionPane.YES_OPTION) { //������ �´ٰ� Ȯ�� ������
				getOrderTime();
				sendMsg(packet);
			}
		});
		comboBox[0].addItemListener(e-> {
			comboBox[0] = (JComboBox) e.getSource();
			String index = (String) comboBox[0].getSelectedItem();
			if(index == "Chicken") {
				setChicken();
				setComboBox();
				comboBox[3].setVisible(true);
			}
			else if(index=="Pizza") {
				setPizza();
				setComboBox();
				comboBox[3].setVisible(true);
				
			} else if(index=="Pork"){
				setPork();
				setComboBox();
				comboBox[3].setVisible(false);
			}
		});
		resetButton.addActionListener(e-> {
			main.clear();
			main.add("Chicken");
			main.add("Pizza");
			main.add("Pork");
			comboBox[0].setModel(new DefaultComboBoxModel(main.toArray()));
			setChicken();
			setComboBox();
			needText.setText("");
		});
		cancelButton.addActionListener(e-> {
			int result = JOptionPane.showConfirmDialog(null, "���� �ֹ��� ����Ͻðڽ��ϱ�?"
					,"CHECK_CANCEL", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(result == JOptionPane.YES_OPTION) {
				sendMsg("CANCEL="+myOrderSeq);
				new Thread(()->{
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(cancel) {
						System.exit(0);											
					} else {
						JOptionPane.showMessageDialog(this, "�丮�� �̹� ���۵Ʊ� ������ �ֹ��� ����Ͻ� �� �����ϴ�.","���о˸�",
								JOptionPane.CANCEL_OPTION);
					}
				}).start();
//				enableBtn();
			}
		});
		arriveButton.addActionListener(e->{
			sendMsg("TIME="+hour+":"+min+":"+comboBox[0].getSelectedItem().toString());
		});
	}
	private void parsingJson() {
		list = new ArrayList<>();
		for (int i = 0; i < comboBox.length; i++) {
			if(comboBox[i].getSelectedItem().equals("No")) {
				list.add("");
			} else {
				list.add(comboBox[i].getSelectedItem().toString());
			}
		}
		if(needText.getText().equals("")) {
			list.add("");
		} else {
			list.add(needText.getText());
		}
		Gson gson = new Gson();
		menu = new Menu(list.get(0),list.get(1),list.get(2),list.get(3),list.get(4));
		packet = "ORDER\n" + gson.toJson(menu);
	}
	private void setView() {
		setTitle("����� ���� - �� �ֹ�");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		mainLabel = new JLabel("���� �޴�");
		mainLabel.setFont(new Font("����", Font.PLAIN, 12));
		mainLabel.setBounds(12, 50, 57, 15);
		contentPane.add(mainLabel);

		optionLabel1 = new JLabel("�߰� �ɼ�1");
		optionLabel1.setFont(new Font("����", Font.PLAIN, 12));
		optionLabel1.setBounds(12, 85, 70, 15);
		contentPane.add(optionLabel1);

		optionLabel2 = new JLabel("�߰� �ɼ�2");
		optionLabel2.setFont(new Font("����", Font.PLAIN, 12));
		optionLabel2.setBounds(12, 120, 70, 15);
		contentPane.add(optionLabel2);

		optionLabel3 = new JLabel("�߰� �ɼ�3");
		optionLabel3.setFont(new Font("����", Font.PLAIN, 12));
		optionLabel3.setBounds(12, 155, 70, 15);
		contentPane.add(optionLabel3);

		setChicken();
		
		comboBox[0] = new JComboBox(main.toArray());
		comboBox[0].setBounds(92, 45, 138, 23);
		comboBox[0].setFont(new Font("����",Font.PLAIN,12));
		contentPane.add(comboBox[0]);

		comboBox[1] = new JComboBox(option1.toArray());
		comboBox[1].setBounds(92, 80, 138, 23);
		comboBox[1].setFont(new Font("����",Font.PLAIN,12));
		contentPane.add(comboBox[1]);

		comboBox[2] = new JComboBox(option2.toArray());
		comboBox[2].setBounds(92, 115, 138, 23);
		comboBox[2].setFont(new Font("����",Font.PLAIN,12));
		contentPane.add(comboBox[2]);

		comboBox[3] = new JComboBox(option3.toArray());
		comboBox[3].setBounds(92, 150, 138, 23);
		comboBox[3].setFont(new Font("����",Font.PLAIN,12));
		contentPane.add(comboBox[3]);

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
		
//		setChicken();
	}
	private void setComboBox() {
		comboBox[1].setModel(new DefaultComboBoxModel(option1.toArray()));
		comboBox[2].setModel(new DefaultComboBoxModel(option2.toArray()));
		comboBox[3].setModel(new DefaultComboBoxModel(option3.toArray()));
	}
	private void setPork() {

		option1.clear();
		option2.clear();
		option3.clear();
		optionLabel1.setText("�ʱ�");
		optionLabel2.setText("����");
		optionLabel3.setText("");
		option1.add("Normal");
		option1.add("No Hot");
		option1.add("Hot");
		option1.add("Very Hot");
		option2.add("Coke 500ml");
		option2.add("Cider 500ml");
		option2.add("Coke 1.5L");
		option2.add("Cider 1.5L");
		option3.add("No");
	}
	private void setPizza() {

		option1.clear();
		option2.clear();
		option3.clear();
		optionLabel1.setText("����");
		optionLabel2.setText("����");
		option1.add("Normal");
		option1.add("Cheese Crust");
		option2.add("Coke 500ml");
		option2.add("Cider 500ml");
		option2.add("Coke 1.5L");
		option2.add("Cider 1.5L");
		option3.add("No");
		option3.add("Add Onion Source");
	}
	private void setChicken() {

		option1.clear();
		option2.clear();
		option3.clear();
		optionLabel1.setText("��,���� ����");
		optionLabel2.setText("����");
		optionLabel3.setText("�ҽ� �߰�");
		option1.add("Boneless");
		option1.add("Bone");
		option2.add("Coke 500ml");
		option2.add("Cider 500ml");
		option2.add("Coke 1.5L");
		option2.add("Cider 1.5L");
		option3.add("No");
		option3.add("Add Hot Source");
	}
	private void disableBtn() {
		orderButton.setEnabled(false);
		resetButton.setEnabled(false);
		arriveButton.setEnabled(true);
		comboBox[0].setEnabled(false);
		comboBox[1].setEnabled(false);
		comboBox[2].setEnabled(false);
		comboBox[3].setEnabled(false);
		needText.setEnabled(false);
		cancelButton.setEnabled(true);
	}
	private void enableBtn() {
		orderButton.setEnabled(true);
		resetButton.setEnabled(true);
		arriveButton.setEnabled(false);
		comboBox[0].setEnabled(true);
		comboBox[1].setEnabled(true);
		comboBox[2].setEnabled(true);
		comboBox[3].setEnabled(true);
		needText.setEnabled(true);
		cancelButton.setEnabled(false);
	}
	
	public void ArriveTime(String msg) {
		int result = JOptionPane.showConfirmDialog(null, "���������ð��� "
				+ msg +"�� �Դϴ�.","Arrive_Time", JOptionPane.CLOSED_OPTION, JOptionPane.QUESTION_MESSAGE);
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
			JOptionPane.showMessageDialog(this, "�ֹ��� �����Ǿ����ϴ�.");
			disableBtn();
		} else if(recvData.startsWith("ORDER_NUMBER")) {
			recvData = recvData.substring(11);
			String temp[] = recvData.split("=");
			myOrderSeq = Integer.parseInt(temp[1]);
		} else if(recvData.startsWith("TIME")) {
			String temp[] = recvData.split("=");
			ArriveTime(temp[1]);
		} else if(recvData.startsWith("CANCEL_OK")) {
			JOptionPane.showMessageDialog(this, "�ֹ��Ͻ� �޴��� ��ҵǾ����ϴ�.","��Ҿ˸�",
					JOptionPane.INFORMATION_MESSAGE);
		} else if(recvData.startsWith("CANCEL_FAIL")) {
			cancel = false;
		} else if(recvData.startsWith("FAIL")) {
			JOptionPane.showMessageDialog(this, "�ֹ��� �з��־� �ȵ˴ϴ�.","���� �˸�",
					JOptionPane.CANCEL_OPTION);
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
	
	private void getOrderTime() {
		LocalDateTime timePoint = LocalDateTime.now();
		hour = timePoint.getHour();
		min = timePoint.getMinute();		
	}
	public static void main(String[] args) {
		OrderFood orderFood = new OrderFood();
	}

}