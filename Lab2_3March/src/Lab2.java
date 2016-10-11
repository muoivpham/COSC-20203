/***************************************************************/
/*   Program Name:     Lab 2                                   */
/*                                                             */
/*   Student Name:     Muoi Pham			                   */
/*   Semester:         Spring, 2016		                       */
/*   Class-Section:    CoSc20203-55					           */
/*   Instructor:       Dr.J. Richard (Dick) Rinewalt           */
/*                                                             */
/*   Program Overview:                                         */
/*     This program allows a user to  encrypt and decrypt      */
/*     the Playfair’s Cipher                                   */
/*                                                             */
/*   Input:                                                    */
/*     The user will enter The secret key phrase, and then     */
/*      encode the letters of the English alphabet             */
/*                                                             */
/*   Output:                                                   */
/*     The input will be encoded or decoded				       */
/*                                                             */
/*   Program Limitations:    none                              */
/*                                                             */
/***************************************************************/

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Lab2 extends JFrame implements ActionListener {
	JTextArea result = new JTextArea(20, 54);
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
		setSize(700, 430);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		scroller.getViewport().add(result);
		result.setLineWrap(true);
		result.setWrapStyleWord(true);
		add(scroller, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new java.awt.GridLayout(3, 1));
		southPanel.add(buttonPanel);
		buttonPanel.add(openButton);
		openButton.addActionListener(this);
		buttonPanel.add(encryptButton);
		encryptButton.addActionListener(this);
		buttonPanel.add(decryptButton);
		decryptButton.addActionListener(this);
		southPanel.add(phrasePanel);
		phrasePanel.add(phraseLabel);
		phrasePanel.add(phraseTF);
		southPanel.add(errors);
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == openButton) {
			getFile();
		} else if (evt.getSource() == encryptButton) {
			encrypt();
		} else if (evt.getSource() == decryptButton) {
			decrypt();
		}
	}

	public static void main(String[] args) {
		Lab2 display = new Lab2();
		display.setVisible(true);
	}

	// display a file dialog
	String getFileName() {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile().getPath();
		else
			return null;
	}

	// read the file and display it in the text area
	void getFile() {
		String fileName = getFileName();
		if (fileName == null)
			return;
		result.setText("");
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line + "\n");
			}
			in.close();
		} catch (IOException ioe) {
			errors.setText("ERROR: " + ioe);
		}
	}

	/*******************************************************************************/

	void encrypt() {
		try {
			// set the matrix
			char[][] matrix = getMatrix();
			String resultS = result.getText();
			// resultS = resultS.replace("\\n", "");
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '()*+,-./:;<=>[]^?";
			// remove the character in input if it is not in the original list
			// first solution for this problem
			/*
			 * for (int i = 0; i < resultS.length(); i++) { if
			 * (!characters.contains(String.valueOf(resultS.charAt(i)))) {
			 * resultS = resultS.replace(String.valueOf(resultS.charAt(i)), "");
			 * } }
			 */
			// second solution by using regular expression
			resultS = resultS.replaceAll(
					"[^ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '\\()\\*\\+,-\\./:;<=>\\[\\]\\^\\?]",
					"");
			result.setText("");
			char I1, I2, O1, O2;
			for (int i = 0; i < resultS.length(); i = i + 2) {
				// take a pair of characters
				if (i + 1 < resultS.length()) {
					I1 = resultS.charAt(i);
					I2 = resultS.charAt(i + 1);
					// get its position in the matrix
					position posI1 = getPosition(I1);
					int I1i = posI1.getI();
					int I1j = posI1.getJ();
					position posI2 = getPosition(I2);
					int I2i = posI2.getI();
					int I2j = posI2.getJ();
					// if two characters are the same
					if (I1i == I2i && I1j == I2j) {
						O1 = I1;
						O2 = I2;
						String addToResult = Character.toString(O1) + Character.toString(O2);
						result.append(addToResult);

					}
					// if they are in the same row
					else if (I1i == I2i && I1j != I2j) {
						if (I1j == 8)
							I1j = 0;
						else
							I1j++;
						if (I2j == 8)
							I2j = 0;
						else
							I2j++;
						O1 = getChar(I1i, I1j);
						O2 = getChar(I2i, I2j);
						String addToResult = Character.toString(O1) + Character.toString(O2);
						result.append(addToResult);
					}
					// if they are in the same column
					else if (I1i != I2i && I1j == I2j) {
						if (I1i == 8)
							I1i = 0;
						else
							I1i++;
						if (I2i == 8)
							I2i = 0;
						else
							I2i++;
						O1 = getChar(I1i, I1j);
						O2 = getChar(I2i, I2j);
						String addToResult = Character.toString(O1) + Character.toString(O2);
						result.append(addToResult);
					}
					// if they are found at the corners of the rectangle
					else {
						O1 = getChar(I1i, I2j);
						O2 = getChar(I2i, I1j);
						String addToResult = Character.toString(O1) + Character.toString(O2);
						result.append(addToResult);
					}
				}
				// if the last character is the first of a pair
				else if (i == resultS.length() - 1) {
					I1 = resultS.charAt(i);
					O1 = I1;
					String addToResult = Character.toString(O1);
					result.append(addToResult);
				}
			}
		} catch (Exception e) {
			System.out.println("NumberFormatException");
		}
	}

	void decrypt() {
		try {
			char[][] matrix = getMatrix();
			String resultS = result.getText();
			resultS = resultS.replaceAll(
					"[^ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '\\()\\*\\+,-\\./:;<=>\\[\\]\\^\\?]",
					"");
			result.setText("");
			char I1, I2, O1, O2;
			for (int i = 0; i < resultS.length(); i = i + 2) {
				if (i + 1 < resultS.length()) {
					// take a pair of characters
					I1 = resultS.charAt(i);
					I2 = resultS.charAt(i + 1);
					// get its positions in the matrix
					position posI1 = getPosition(I1);
					int I1i = posI1.getI();
					int I1j = posI1.getJ();
					position posI2 = getPosition(I2);
					int I2i = posI2.getI();
					int I2j = posI2.getJ();
					// if two characters are the same
					if (I1i == I2i && I1j == I2j) {
						O1 = I1;
						O2 = I2;
						String addToResult = Character.toString(O1) + Character.toString(O2);
						result.append(addToResult);
					}
					// if they are in the same row
					else if (I1i == I2i && I1j != I2j) {
						if (I1j == 0)
							I1j = 8;
						else
							I1j--;
						if (I2j == 0)
							I2j = 8;
						else
							I2j--;
						O1 = getChar(I1i, I1j);
						O2 = getChar(I2i, I2j);
						String addToResult = Character.toString(O1) + Character.toString(O2);
						result.append(addToResult);
					}
					// if they are in the same column
					else if (I1i != I2i && I1j == I2j) {
						if (I1i == 0)
							I1i = 8;
						else
							I1i--;
						if (I2i == 0)
							I2i = 8;
						else
							I2i--;
						O1 = getChar(I1i, I1j);
						O2 = getChar(I2i, I2j);
						String addToResult = Character.toString(O1) + Character.toString(O2);
						result.append(addToResult);
					}
					// if they are found at the corners of the rectangle
					else {
						O1 = getChar(I1i, I2j);
						O2 = getChar(I2i, I1j);
						String addToResult = Character.toString(O1) + Character.toString(O2);
						result.append(addToResult);
					}
				}
				// if the last character is the first of a pair
				else if (i == resultS.length() - 1) {
					I1 = resultS.charAt(i);
					O1 = I1;
					String addToResult = Character.toString(O1);
					result.append(addToResult);
				}
			}
		} catch (Exception e) {
			System.out.println("NumberFormatException");
		}
	}

	// get matrix
	char[][] getMatrix() {
		char[][] matrix = new char[9][9];
		String secretKey = phraseTF.getText();
		secretKey = getSecretKey(secretKey);
		matrix = getNewMatrix1(secretKey);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
			}
		}
		return matrix;
	}

	String getSecretKey(String secretKey) {
		String newKey = "";
		// get rid of the unlisted character in the secret key
		secretKey = secretKey.replaceAll(
				"[^ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '\\()\\*\\+,-\\./:;<=>\\[\\]\\^\\?]",
				"");
		// omitting duplicated characters when the secret key phrase is entered
		// into the matrix
		for (int i = 0; i < secretKey.length(); i++) {
			if (!newKey.contains(String.valueOf(secretKey.charAt(i)))) {
				newKey = newKey + secretKey.charAt(i);
			}
		}
		return newKey;
	}

	// make the new matrix when secret key is added
	char[][] getNewMatrix1(String secretKey) {
		secretKey = getSecretKey(secretKey);
		char[][] newMatrix = new char[9][9];
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '()*+,-./:;<=>[]^?";
		for (int i = 0; i < secretKey.length(); i++) {
			for (int j = 0; j < characters.length(); j++) {
				// erase the duplicate character
				if (secretKey.charAt(i) == characters.charAt(j)) {
					String firstPart = characters.substring(0, j);
					String secPart = characters.substring(j + 1, characters.length());
					characters = firstPart + secPart;
				}
			}
		}
		// make the new matrix by adding the String with secret key
		String newCharacters = secretKey + characters;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				newMatrix[i][j] = newCharacters.charAt(9 * i + j);
			}
		}
		return newMatrix;
	}

	// get a character with its positions in the matrix
	char getChar(int i, int j) {
		char character;
		char[][] matrix = getMatrix();
		character = matrix[i][j];
		return character;
	}

	// create a object called position for each character
	final class position {
		private final int iValue;
		private final int jValue;

		public position(int iValue, int jValue) {
			this.iValue = iValue;
			this.jValue = jValue;
		}

		public int getI() {
			return iValue;
		}

		public int getJ() {
			return jValue;
		}
	}

	// get the position of character in the matrix
	public position getPosition(char character) {
		int iValue = 0, jValue = 0;
		char[][] matrix = getMatrix();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (character == matrix[i][j]) {
					iValue = i;
					jValue = j;
					return new position(iValue, jValue);
				}
			}
		}
		return new position(iValue, jValue);
	}
}