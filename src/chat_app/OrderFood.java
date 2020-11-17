package chat_app;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JOptionPane;
import java.awt.ScrollPane;
import java.awt.TextArea;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.Button;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class OrderFood extends JFrame {

	private JPanel contentPane;
	private UDPmsg udpmsg = new UDPmsg();
	private JButton btnNewButton;
	private JComboBox comboBox,comboBox1,comboBox2,comboBox3,cb;
	private ArrayList<String> main,sub1,sub2,sub3,list;
	private TextArea needText;
	public OrderFood() {
		setView();
		btnNewButton = new JButton("\uC8FC\uBB38\uD558\uAE30");
		btnNewButton.setFont(new Font("돋움", Font.PLAIN, 12));
		btnNewButton.setBounds(62, 200, 97, 23);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				list = new ArrayList<>();
				list.add(comboBox.getSelectedItem().toString());
				list.add(comboBox1.getSelectedItem().toString());
				list.add(comboBox2.getSelectedItem().toString());
				list.add(comboBox3.getSelectedItem().toString());
				
				String t = needText.getText();
				if(t.equals("")) {
					list.add("추가 주문사항 없음");
				} else list.add(needText.getText());
				
				JOptionPane.showConfirmDialog(null, "아래 내용이 맞나요?\n"
						+ list.get(0) +"\n"
						+ "+)"+ list.get(1) +"\n"
						+ "+)"+ list.get(2) +"\n"
						+ "+)"+ list.get(3) +"\n"
						+ "+)"+ list.get(4) +"\n", "CHECK_ORDER", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				udpmsg.sendMsg("SUCCESS");
				JOptionPane.showMessageDialog(null, "예상 소요 시간은 30~40분입니다.");
			}
		});
		comboBox.addItemListener(e-> {
			cb = (JComboBox) e.getSource();
			String index = (String) cb.getSelectedItem();
			if(index == "치킨") {
				sub1.clear();
				sub2.clear();
				sub3.clear();
				sub1.add("순살");
				sub1.add("뼈");
				sub2.add("콜라 500ml");
				sub2.add("사이다 500ml");
				sub2.add("콜라 1.5L");
				sub2.add("사이다 1.5L");
				sub3.add("선택 안함");
				sub3.add("양념소스 추가");
				setComboBox();
			}
			else if(index=="피자") {
				sub1.clear();
				sub2.clear();
				sub3.clear();
				sub1.add("없음");
				sub1.add("치즈크러스트");
				sub2.add("콜라 500ml");
				sub2.add("사이다 500ml");
				sub2.add("콜라 1.5L");
				sub2.add("사이다 1.5L");
				sub3.add("선택 안함");
				sub3.add("어니언소스 추가");
				setComboBox();
			} else {
				sub1.clear();
				sub2.clear();
				sub3.clear();
				sub1.add("보통맛");
				sub1.add("순한맛");
				sub1.add("매운맛");
				sub1.add("아주매운맛");
				sub2.add("콜라 500ml");
				sub2.add("사이다 500ml");
				sub2.add("콜라 1.5L");
				sub2.add("사이다 1.5L");
				sub3.add("선택 안함");
				setComboBox();
			}
		});
	}
	private void setView() {
		main = new ArrayList<>();
		sub1 = new ArrayList<>();
		sub2 = new ArrayList<>();
		sub3 = new ArrayList<>();
		main.add("치킨");
		main.add("피자");
		main.add("족발");
		sub1.add("순살");
		sub1.add("뼈");
		sub2.add("콜라 500ml");
		sub2.add("사이다 500ml");
		sub2.add("콜라 1.5L");
		sub2.add("사이다 1.5L");
		sub3.add("선택 안함");
		sub3.add("양념소스 추가");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("\uBA54\uC778 \uBA54\uB274");
		lblNewLabel.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel.setBounds(12, 50, 57, 15);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("\uCD94\uAC00 \uC635\uC1581");
		lblNewLabel_1.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(12, 85, 70, 15);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("\uCD94\uAC00 \uC635\uC1582");
		lblNewLabel_2.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(12, 120, 70, 15);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_2_1 = new JLabel("\uCD94\uAC00 \uC635\uC1583");
		lblNewLabel_2_1.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel_2_1.setBounds(12, 155, 70, 15);
		contentPane.add(lblNewLabel_2_1);

		comboBox = new JComboBox(main.toArray());
		comboBox.setBounds(92, 46, 138, 23);
		contentPane.add(comboBox);

		comboBox1 = new JComboBox(sub1.toArray());
		comboBox1.setBounds(92, 81, 138, 23);
		contentPane.add(comboBox1);

		comboBox2 = new JComboBox(sub2.toArray());
		comboBox2.setBounds(92, 116, 138, 23);
		contentPane.add(comboBox2);

		comboBox3 = new JComboBox(sub3.toArray());
		comboBox3.setBounds(92, 151, 138, 23);
		contentPane.add(comboBox3);

		JLabel lblNewLabel_3 = new JLabel("\uC8FC\uBB38\uC2DC \uC694\uAD6C\uC0AC\uD56D");
		lblNewLabel_3.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(242, 50, 118, 15);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("\uBB34\uC5C7\uC744 \uB3C4\uC640\uB4DC\uB9B4\uAE4C\uC694?");
		lblNewLabel_4.setFont(new Font("돋움", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(147, 10, 163, 15);
		contentPane.add(lblNewLabel_4);

		needText = new TextArea();
		needText.setBounds(238, 81, 186, 94);
		contentPane.add(needText);

		JButton clearBtn = new JButton("\uCD08\uAE30\uD654");
		clearBtn.addActionListener(e-> {
			main.clear();
			sub1.clear();
			sub2.clear();
			sub3.clear();
			main.add("치킨");
			main.add("피자");
			main.add("족발");
			sub1.add("순살");
			sub1.add("뼈");
			sub2.add("콜라 500ml");
			sub2.add("사이다 500ml");
			sub2.add("콜라 1.5L");
			sub2.add("사이다 1.5L");
			sub3.add("선택 안함");
			sub3.add("양념소스 추가");
			comboBox.setModel(new DefaultComboBoxModel(main.toArray()));
			setComboBox();
			needText.setText("");
		});
		clearBtn.setFont(new Font("돋움", Font.PLAIN, 12));
		clearBtn.setBounds(171, 200, 97, 23);
		contentPane.add(clearBtn);

		JButton cancelBtn = new JButton("\uC8FC\uBB38 \uCDE8\uC18C");
		cancelBtn.addActionListener(e-> {
			FrameExam frameExam = new FrameExam();
			frameExam.setVisible(true);
			dispose();
		});
		cancelBtn.setFont(new Font("돋움", Font.PLAIN, 12));
		cancelBtn.setBounds(280, 200, 97, 23);
		contentPane.add(cancelBtn);
	}
	private void setComboBox() {
		comboBox1.setModel(new DefaultComboBoxModel(sub1.toArray()));
		comboBox2.setModel(new DefaultComboBoxModel(sub2.toArray()));
		comboBox3.setModel(new DefaultComboBoxModel(sub3.toArray()));
	}
	
}
