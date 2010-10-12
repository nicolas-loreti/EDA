package soko.solver;

public class MovString {
	private char[] movString;
	Board board;

	public MovString(Board board) {
		this.board = board;
		movString = new char[4];
		movString[0] = 'u';
		movString[1] = 'l';
		movString[2] = 'd';
		movString[3] = 'r';
	}

	/*
	 * public void getMovement(int playerID) {
	 * 
	 * 
	 * }
	 */

	public void getMovement(int playerID) {
		char aux = '\0';

		for (int i = 0; i < 4; i++) {
			int k = (int) (Math.random() * (4 - 0.00000001));
			aux = movString[k];
			movString[k] = movString[i];
			movString[i] = aux;
		}
	}

	public char get(int index) {
		return movString[index];
	}

	public int getMovementI(int index) {
		switch (movString[index]) {
		case 'u':
			return -1;
		case 'd':
			return +1;
		default:
			return 0;
		}
	}

	public int getMovementJ(int index) {
		switch (movString[index]) {
		case 'l':
			return -1;
		case 'r':
			return +1;
		default:
			return 0;
		}
	}
}
