package Solution;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Lab2 extends JFrame implements ActionListener {
	JTextArea result = new JTextArea(20,54);
	JLabel errors = new JLabel();
	JScrollPane scroller = new JScrollPane();
	JButton openButton = new JButton("Get File");
	JButton encryptButton = new JButton("Encrypt");
	JButton decryptButton = new JButton("Decrypt");
	JPanel buttonPanel = new JPanel();
	JLabel phraseLabel = new JLabel("Secret Phrase:");
	JTextField phraseTF = new JTextField(40);
	JPanel phrasePanel = new JPanel();
	JPanel southPanel = new JPanel();
	
	public Lab2() {
		setLayout(new java.awt.BorderLayout());
		setSize(700,430);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		scroller.getViewport().add(result);
		result.setLineWrap(true);
		result.setWrapStyleWord(true);
		add(scroller, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new java.awt.GridLayout(3,1));
		southPanel.add(buttonPanel);
		buttonPanel.add(openButton); openButton.addActionListener(this);
		buttonPanel.add(encryptButton); encryptButton.addActionListener(this);
		buttonPanel.add(decryptButton); decryptButton.addActionListener(this);
		southPanel.add(phrasePanel);
		phrasePanel.add(phraseLabel);
		phrasePanel.add(phraseTF);
		southPanel.add(errors);
	}
	
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == openButton) {
			getFile();
		}
		else if (evt.getSource() == encryptButton) {
			encrypt();
		}
		else if (evt.getSource() == decryptButton) {
			decrypt();
		}
	}
	
	public static void main(String[] args) {
		Lab2 display = new Lab2();
		display.setVisible(true);
	}
	
	//display a file dialog
	String getFileName() {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile().getPath();
		else
			return null;
	}
	
	//read the file and display it in the text area
	void getFile() {
		String fileName = getFileName();
		if (fileName == null)
			return;
		result.setText("");
		try{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = in.readLine()) != null) {
				result.append(line + "\n");
			}
			in.close();
		} catch(IOException ioe) {
			errors.setText("ERROR: " + ioe);
		}
	}
	
/*******************************************************************************/
	String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '()*+,-./:;<=>[]^?";
	boolean encrypt;
	
	void encrypt() {
		encrypt = true;
		cryptIt();
	}

	void decrypt() {
		encrypt = false;
		cryptIt();
	}

	void cryptIt() {
		String phrase = phraseTF.getText();
		String alphaCopy = alphabet;
		String matrix = "";
		int pos;
		//for each character in the secret phrase
		//    add it to the string representing the matrix
		//    and delete it from the copy of the alphabet
		for(int i=0; i<phrase.length(); i++) {
			pos = alphaCopy.indexOf(phrase.charAt(i));
			if (pos >= 0) {
				matrix += phrase.charAt(i);
				alphaCopy = alphaCopy.substring(0,pos) + alphaCopy.substring(pos+1);
			}
		}
		//add the remaining characters to the string representing the matrix
		matrix += alphaCopy;

		String clearText = result.getText();
		result.setText("");
		int index = 0;
		int pos1 = -1, pos2 = -1;
		//as long as there are unprocessed characters in the text
		while (index < clearText.length()) {
			char I1, I2;
			int i1,j1,i2,j2;
			//get the next character in the text that is in the matrix
			while (pos1 < 0 && index < clearText.length()) {
				I1 = clearText.charAt(index);
				pos1 = matrix.indexOf(I1);
				index++;
			}
			//get another character in the text that is in the matrix
			while (pos2 < 0 && index < clearText.length()) {
				I2 = clearText.charAt(index);
				pos2 = matrix.indexOf(I2);
				index++;
			}
			//if there isn't one, break out of the loop
			if (pos2 < 0) break;
			char O1, O2;
			//find the row & column for each of the characters
			i1 = pos1 / 9;   j1 = pos1 % 9;
			i2 = pos2 / 9;   j2 = pos2 % 9;
			//determine which rule applies and encrypt/decrypt accordingly
			if (pos1 == pos2) {              //case d
				O1 = matrix.charAt(pos1);
				O2 = O1;
			}
			else if (i1 == i2) {             //case b row 
				if (encrypt) {
					O1 = matrix.charAt(i1*9 + (j1+1)%9);
					O2 = matrix.charAt(i2*9 + (j2+1)%9);
				}
				else {
					O1 = matrix.charAt(i1*9 + (j1+8)%9);
					O2 = matrix.charAt(i2*9 + (j2+8)%9);
				}
			}
			else if (j1 == j2) {             //case c columne 
				if (encrypt) {
					O1 = matrix.charAt(((i1+1)%9)*9 + j1%9);
					O2 = matrix.charAt(((i2+1)%9)*9 + j2%9);
				}
				else {
					O1 = matrix.charAt(((i1+8)%9)*9 + j1%9);
					O2 = matrix.charAt(((i2+8)%9)*9 + j2%9);
				}
			}
			else {                           //case a rectangle 
				O1 = matrix.charAt(i1*9 + j2);
				O2 = matrix.charAt(i2*9 + j1);
			}
			result.append("" + O1 + O2);
			pos1 = -1;
			pos2 = -1;
		}
		if (pos1 >= 0)   //case e
			result.append("" + matrix.charAt(pos1));
	}
	
}