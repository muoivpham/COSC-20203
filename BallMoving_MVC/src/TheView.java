import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class TheView extends JFrame {

	private JPanel contentPane;
	private JPanel pnlGround;
	private JButton[][] buttons;
	private JButton buttonUp;
	private JButton buttonDown;
	private JButton buttonLeft;
	private JButton buttonRight;
	private int row = 2, column = 2;

	public TheView() {
		buttons = new JButton[5][5];
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBounds(5, 5, 5, 5);
		setContentPane(contentPane);

		pnlGround = new JPanel();
		pnlGround.setBorder(new TitledBorder(null, "Ground", TitledBorder.LEADING, TitledBorder.TOP));
		pnlGround.setLayout(new GridLayout(5, 5));
		initButton();

		buttonUp = new JButton("UP");
		buttonDown = new JButton("DOWN");
		buttonLeft = new JButton("LEFT");
		buttonRight = new JButton("RIGHT");

		contentPane.add(pnlGround, BorderLayout.CENTER);
		contentPane.add(buttonDown, BorderLayout.SOUTH);
		contentPane.add(buttonLeft, BorderLayout.WEST);
		contentPane.add(buttonUp, BorderLayout.NORTH);
		contentPane.add(buttonRight, BorderLayout.EAST);
	}

	public void initButton() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[0].length; j++) {
				buttons[i][j] = new JButton("");
				if (i == row && j == column) {
					buttons[i][j].setText("o");
				}
				pnlGround.add(buttons[i][j]);
			}
		}
	}

	public void updateButton() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[0].length; j++) {
				buttons[i][j].setText("");
				if (i == row & j == column) {
					buttons[i][j].setText("o");
				}
			}
		}
	}

	public int getRowPosition() {
		return row;
	}

	public int getColumnPosition() {
		return column;
	}
	public void addUpActionListener( ActionListener u){
		buttonUp.addActionListener(u);
		System.out.println("test 1");
	}
	public void addDownActionListener( ActionListener d){
		buttonDown.addActionListener(d);
	}
	public void addLeftActionListener( ActionListener l){
		buttonLeft.addActionListener(l);
	}
	public void addRightActionListener( ActionListener r){
		buttonRight.addActionListener(r);
	}

	public void setRow(int row2) {
		// TODO Auto-generated method stub
		row = row2;
	}

	public void setcolumn(int column2) {
		// TODO Auto-generated method stub
		column = column2;
	}
}
