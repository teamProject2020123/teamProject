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
		btnNewButton.setFont(new Font("����", Font.PLAIN, 12));
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
					list.add("�߰� �ֹ����� ����");
				} else list.add(needText.getText());
				
				JOptionPane.showConfirmDialog(null, "�Ʒ� ������ �³���?\n"
						+ list.get(0) +"\n"
						+ "+)"+ list.get(1) +"\n"
						+ "+)"+ list.get(2) +"\n"
						+ "+)"+ list.get(3) +"\n"
						+ "+)"+ list.get(4) +"\n", "CHECK_ORDER", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				udpmsg.sendMsg("SUCCESS");
				JOptionPane.showMessageDialog(null, "���� �ҿ� �ð��� 30~40���Դϴ�.");
			}
		});
		comboBox.addItemListener(e-> {
			cb = (JComboBox) e.getSource();
			String index = (String) cb.getSelectedItem();
			if(index == "ġŲ") {
				sub1.clear();
				sub2.clear();
				sub3.clear();
				sub1.add("����");
				sub1.add("��");
				sub2.add("�ݶ� 500ml");
				sub2.add("���̴� 500ml");
				sub2.add("�ݶ� 1.5L");
				sub2.add("���̴� 1.5L");
				sub3.add("���� ����");
				sub3.add("���ҽ� �߰�");
				setComboBox();
			}
			else if(index=="����") {
				sub1.clear();
				sub2.clear();
				sub3.clear();
				sub1.add("����");
				sub1.add("ġ��ũ����Ʈ");
				sub2.add("�ݶ� 500ml");
				sub2.add("���̴� 500ml");
				sub2.add("�ݶ� 1.5L");
				sub2.add("���̴� 1.5L");
				sub3.add("���� ����");
				sub3.add("��Ͼ�ҽ� �߰�");
				setComboBox();
			} else {
				sub1.clear();
				sub2.clear();
				sub3.clear();
				sub1.add("�����");
				sub1.add("���Ѹ�");
				sub1.add("�ſ��");
				sub1.add("���ָſ��");
				sub2.add("�ݶ� 500ml");
				sub2.add("���̴� 500ml");
				sub2.add("�ݶ� 1.5L");
				sub2.add("���̴� 1.5L");
				sub3.add("���� ����");
				setComboBox();
			}
		});
	}
	private void setView() {
		main = new ArrayList<>();
		sub1 = new ArrayList<>();
		sub2 = new ArrayList<>();
		sub3 = new ArrayList<>();
		main.add("ġŲ");
		main.add("����");
		main.add("����");
		sub1.add("����");
		sub1.add("��");
		sub2.add("�ݶ� 500ml");
		sub2.add("���̴� 500ml");
		sub2.add("�ݶ� 1.5L");
		sub2.add("���̴� 1.5L");
		sub3.add("���� ����");
		sub3.add("���ҽ� �߰�");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("\uBA54\uC778 \uBA54\uB274");
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel.setBounds(12, 50, 57, 15);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("\uCD94\uAC00 \uC635\uC1581");
		lblNewLabel_1.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(12, 85, 70, 15);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("\uCD94\uAC00 \uC635\uC1582");
		lblNewLabel_2.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(12, 120, 70, 15);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_2_1 = new JLabel("\uCD94\uAC00 \uC635\uC1583");
		lblNewLabel_2_1.setFont(new Font("����", Font.PLAIN, 12));
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
		lblNewLabel_3.setFont(new Font("����", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(242, 50, 118, 15);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("\uBB34\uC5C7\uC744 \uB3C4\uC640\uB4DC\uB9B4\uAE4C\uC694?");
		lblNewLabel_4.setFont(new Font("����", Font.PLAIN, 12));
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
			main.add("ġŲ");
			main.add("����");
			main.add("����");
			sub1.add("����");
			sub1.add("��");
			sub2.add("�ݶ� 500ml");
			sub2.add("���̴� 500ml");
			sub2.add("�ݶ� 1.5L");
			sub2.add("���̴� 1.5L");
			sub3.add("���� ����");
			sub3.add("���ҽ� �߰�");
			comboBox.setModel(new DefaultComboBoxModel(main.toArray()));
			setComboBox();
			needText.setText("");
		});
		clearBtn.setFont(new Font("����", Font.PLAIN, 12));
		clearBtn.setBounds(171, 200, 97, 23);
		contentPane.add(clearBtn);

		JButton cancelBtn = new JButton("\uC8FC\uBB38 \uCDE8\uC18C");
		cancelBtn.addActionListener(e-> {
			FrameExam frameExam = new FrameExam();
			frameExam.setVisible(true);
			dispose();
		});
		cancelBtn.setFont(new Font("����", Font.PLAIN, 12));
		cancelBtn.setBounds(280, 200, 97, 23);
		contentPane.add(cancelBtn);
	}
	private void setComboBox() {
		comboBox1.setModel(new DefaultComboBoxModel(sub1.toArray()));
		comboBox2.setModel(new DefaultComboBoxModel(sub2.toArray()));
		comboBox3.setModel(new DefaultComboBoxModel(sub3.toArray()));
	}
	
}
