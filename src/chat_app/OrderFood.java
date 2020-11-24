package chat_app;

import javax.swing.*;
import java.awt.TextArea;

import javax.swing.DefaultComboBoxModel;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class OrderFood extends JFrame {

	private JPanel contentPane;
	private UDPClient udp;
	private JButton orderButton,resetButton,backButton,cancelButton;
	private JComboBox<String> comboBox,comboBox1,comboBox2,comboBox3,cb;
	private ArrayList<String> main,sub1,sub2,sub3,list,recv;
	private TextArea needText;
	private String packet;

	public OrderFood() {
		udp = new UDPClient();
		udp.dataReceiver();
		main = new ArrayList<>();
		sub1 = new ArrayList<>();
		sub2 = new ArrayList<>();
		sub3 = new ArrayList<>();
		main.add("Chicken");
		main.add("Pizza");
		main.add("Pork");
		setChicken();
		setView();
		orderButton = new JButton("주문하기");
		orderButton.setFont(new Font("돋움", Font.PLAIN, 12));
		orderButton.setBounds(65, 200, 97, 23);
		contentPane.add(orderButton);
		actionListener();
	}
	private void actionListener() {
		orderButton.addActionListener(e-> {
			list = new ArrayList<>();
//			list.add(comboBox.getSelectedItem().toString() + "\n");
//			list.add(comboBox1.getSelectedItem().toString() + "\n");
//			list.add(comboBox2.getSelectedItem().toString() + "\n");
//			list.add(comboBox3.getSelectedItem().toString() + "\n");
			list.add(comboBox.getSelectedItem().toString());
			list.add(comboBox1.getSelectedItem().toString());
			list.add(comboBox2.getSelectedItem().toString());
			list.add(comboBox3.getSelectedItem().toString());

			String t = needText.getText();
			if(t.equals("")) {
				list.add("No Description");
			} else list.add(needText.getText());

//			sendPacket = "ORDER\n"+list.get(0)+list.get(1)+list.get(2)+list.get(3)+list.get(4); //ORDER ~~
			packet = "ORDER\n"+list.toString();

			int result = JOptionPane.showConfirmDialog(null, "아래 내용이 맞나요?\n"
					+ packet.substring(6) ,"CHECK_ORDER", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(result == JOptionPane.YES_OPTION) { //내용이 맞다고 확인 했을때
				udp.sendMsg(packet);//처음에 order보내는부분
				new Thread(()->{
					int i =0;
					while(true) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						recv = udp.getArrPacket(); //체크패킷 읽어와서 이쪽에 저장
						boolean isACKed = udp.checkPacket(packet,recv.get(i));
						i++;
						if(isACKed) break;
					}
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, recv.get(i));
					recv.clear();
				}).start();
			}
		});
		listener();
	}
	private void listener() {
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
		backButton.addActionListener(e-> {
			FrameExam frameExam = new FrameExam();
			frameExam.setVisible(true);
			dispose();
		});
	}

	private void setView() {
		setTitle("배달의 마왕 - 상세 주문");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("메인 메뉴");
		lblNewLabel.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel.setBounds(12, 50, 57, 15);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("추가 옵션1");
		lblNewLabel_1.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(12, 85, 70, 15);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("추가 옵션2");
		lblNewLabel_2.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(12, 120, 70, 15);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_2_1 = new JLabel("추가 옵션3");
		lblNewLabel_2_1.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel_2_1.setBounds(12, 155, 70, 15);
		contentPane.add(lblNewLabel_2_1);

		comboBox = new JComboBox(main.toArray());
		comboBox.setBounds(92, 45, 138, 23);
		comboBox.setFont(new Font("돋움",Font.PLAIN,12));
		contentPane.add(comboBox);

		comboBox1 = new JComboBox(sub1.toArray());
		comboBox1.setBounds(92, 80, 138, 23);
		comboBox1.setFont(new Font("돋움",Font.PLAIN,12));
		contentPane.add(comboBox1);

		comboBox2 = new JComboBox(sub2.toArray());
		comboBox2.setBounds(92, 115, 138, 23);
		comboBox2.setFont(new Font("돋움",Font.PLAIN,12));
		contentPane.add(comboBox2);

		comboBox3 = new JComboBox(sub3.toArray());
		comboBox3.setBounds(92, 150, 138, 23);
		comboBox3.setFont(new Font("돋움",Font.PLAIN,12));
		contentPane.add(comboBox3);

		JLabel lblNewLabel_3 = new JLabel("주문시 추가 요구사항");
		lblNewLabel_3.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(242, 50, 118, 15);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("무엇을 도와드릴까요?");
		lblNewLabel_4.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(147, 10, 163, 15);
		contentPane.add(lblNewLabel_4);

		needText = new TextArea();
		needText.setBounds(238, 81, 186, 94);
		contentPane.add(needText);

		cancelButton = new JButton("주문취소");
		cancelButton.setFont(new Font("돋움", Font.PLAIN, 12));
		cancelButton.setBounds(221, 200, 97, 23);
		contentPane.add(cancelButton);

		resetButton = new JButton("초기화");
		resetButton.setFont(new Font("돋움", Font.PLAIN, 12));
		resetButton.setBounds(65, 228, 97, 23);
		contentPane.add(resetButton);

		backButton = new JButton("종료하기");
		backButton.setFont(new Font("돋움", Font.PLAIN, 12));
		backButton.setBounds(221, 228, 97, 23);
		contentPane.add(backButton);
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

}
