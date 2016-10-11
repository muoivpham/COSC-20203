import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JTextArea;


public class Tester {

	public static void main(String[] args) {
		int[][] a = new int[9][9];
		/*
		 * for (int i = 0; i <9; i++){ for ( int j = 0; j<9 ; j++){ a[i][j]= 9*i
		 * + j; System.out.print(a[i][j]+ " "); } System.out.println("\n"); }
		 */
		char[][] matrix = getOriMatrix();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(matrix[i][j]);
			}
			System.out.println("");
		}
		
		matrix = getMatrix();

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(matrix[i][j]);
			}
			System.out.println("");
		}
		String resultS = new String("con ngua da");
		
		char I1 = resultS.charAt(0);
		char I2 = resultS.charAt(0 + 1);
		// get the position in the matrix
		position posI1 = getPosition(I1);
		int I1i = posI1.getI();
		int I1j = posI1.getJ();
		position posI2 = getPosition(I2);
		int I2i = posI2.getI();
		int I2j = posI2.getJ();
		System.out.println(I1i + " " + I1j + " " + I2i + " " + I2j);
	}

	static char[][] getMatrix() {
		char[][] matrix = new char[9][9];
		String secretKey = "Matrix";
		matrix = getNewMatrix1(secretKey);
		return matrix;
	}

	static char[][] getOriMatrix() {
		char[][] matrix = new char[9][9];
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '()*+,-./:;<=>[]^?";
		String[] arrCharacters = new String[81];
		for (int i = 0; i < 81; i++) {
			arrCharacters[i] = String.valueOf(characters.charAt(i));
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				matrix[i][j] = characters.charAt(9 * i + j);
			}

		}

		return matrix;
	}

	// make the new matrix when secret key is added
	static char[][] getNewMatrix1(String secretKey) {
		char[][] newMatrix = new char[9][9];
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '()*+,-./:;<=>[]^?";
		List<Character> listCharacters = new ArrayList<Character>();

		for (int i = 0; i < secretKey.length(); i++) {
			for (int j = 0; j < characters.length(); j++) {
				if (secretKey.charAt(i) == characters.charAt(j)) {
					String firstPart = characters.substring(0, j);
					String secPart = characters.substring(j + 1, characters.length());
					characters = firstPart + secPart;
				}
			}
		}
		String newCharacters = secretKey + characters;
		System.out.println(newCharacters);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				newMatrix[i][j] = newCharacters.charAt(9 * i + j);
			}
		}
		return newMatrix;
	}

	static char[][] getNewMatrix(String secretKey) {
		char[][] newMatrix = new char[9][9];
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '()*+,-./:;<=>[]^?";
		List<Character> listCharacters = new ArrayList<Character>();
		Set<Character> setCharacters = new HashSet<Character>();
		Queue<Character> queueCharacters1 = new LinkedList<Character>();

		for (int i = 0; i < secretKey.length(); i++) {
			listCharacters.add(secretKey.charAt(i));
		}
		System.out.println(listCharacters);

		for (int i = 0; i < secretKey.length(); i++) {
			for (int j = 0; j < characters.length(); j++) {
				if (secretKey.charAt(i) != characters.charAt(j))
					listCharacters.add(characters.charAt(j));
			}
		}
		System.out.println(listCharacters);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				newMatrix[i][j] = listCharacters.get(9 * i + j);
			}
		}

		return newMatrix;
	}

	final static class position {
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

	public static position getPosition(char character) {
		int iValue = 10, jValue = 10;
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
