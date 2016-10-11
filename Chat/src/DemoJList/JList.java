package DemoJList;

import java.awt.BorderLayout;
import java.awt.Panel;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class JList {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("frame");
		javax.swing.JList<String> list;
		Panel panel = new Panel();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DefaultListModel<String> names = new DefaultListModel<>();
		names.addElement("1");
		names.addElement("2");
		names.addElement("3");
		names.remove(0);
		list = new javax.swing.JList<>(names);
		
		JScrollPane scroll = new JScrollPane(list);
		panel.add(scroll, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout());
		panel.add(list, BorderLayout.CENTER);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
}
