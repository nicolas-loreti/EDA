package soko.solver;

public class MovStringPrior {
	private char[] movString;
	Board board;

	public MovStringPrior(Board board) {
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
		int box=0;
		int curr =0;
		int min=0;
		for( int boxID: board.boxes ){
			if ( (curr = distance( boxID, playerID )) < min ){
				min = curr;
				box = boxID;
			}
		}	
		int j = box % board.cols;
		int i = box / board.cols;
		int playerJ = playerID % board.cols;
		int playerI = playerID / board.cols;
		if ( playerI < i ){
			if ( playerJ < j ){
				movString[0] = 'u';
				movString[1] = 'd';
				movString[2] = 'l';
				movString[3] = 'r';
			}else if ( playerJ > j ){
				movString[0] = 'u';
				movString[1] = 'l';
				movString[2] = 'd';
				movString[3] = 'r';
			}
		}else if ( playerI > i ){
			if ( playerJ < j ){
				movString[0] = 'd';
				movString[1] = 'r';
				movString[2] = 'u';
				movString[3] = 'l';
			}else if ( playerJ > j ){
				movString[0] = 'd';
				movString[1] = 'l';
				movString[2] = 'u';
				movString[3] = 'r';
			}
		}
	}
	
	private  int distance( int boxID, int playerID ){
		int i = boxID / board.cols;
		int j = boxID % board.cols;
		int playerI = playerID / board.cols;
		int playerJ = playerID % board.cols;
		return Math.abs(i-playerI) + Math.abs(j-playerJ);
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
