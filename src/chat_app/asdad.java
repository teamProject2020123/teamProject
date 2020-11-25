package chat_app;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class asdad extends JFrame {
	private ArrayList<String> data = new ArrayList<>();
	private JTextField textField;
	public static void main(String[] args) {
		asdad frame = new asdad();
		frame.setVisible(true);
	}
	public asdad() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		data.add("치킨");
		data.add("치킨");
		data.add("ㅋㅋ");
		data.add("치킨");
		data.add("ㅋㅋ");
		data.add("치킨");
		data.add("ㅋㅋ");
		data.add("치킨");
		data.add("ㅋㅋ");
		data.add("치킨");
		data.add("ㅋㅋ");
		data.add("치킨");
		data.add("ㅋㅋ");
		data.add("치킨");
		data.add("ㅋㅋ");
		data.add("ㅋㅋ");
		getContentPane().setLayout(null);
		
		JList list = new JList();
		list.setBounds(31, 25, 199, 205);
		getContentPane().add(list);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(284, 59, 138, 23);
		getContentPane().add(btnNewButton);

		
	}
}
;
