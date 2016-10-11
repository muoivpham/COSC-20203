
public class TheModel {
	private int row = 2, column = 2;
	//private int[][] a = new int[5][5];

	public void moveUp() {
		if (row > 0) {
			row = row - 1;
		}
	}

	public void moveDown() {
		if (row < 4) {
			row = row + 1;
		}
	}

	public void moveLeft() {
		if (column > 0) {
			column = column - 1;
		}
	}

	public void moveRight() {
		if (column < 4) {
			column = column + 1;
		}
	}

	public int getRow() {
		return row;
	}
	public int getColumn(){
		return column;
	}
}
