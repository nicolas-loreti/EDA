package test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;


/*
 * En esta versión tenemos 1 solo tablero que guarda las paredes y los goals en la matriz y tiene
 * un set de goals y boxes y un player, pero las cajas son manejadas aparte por el algoritmo, en vez
 * de tener una pila de boards vamos a tener una pila de cajas y en validMove vamos a preguntar si
 * no se está repitiendo una posición pasada de las cajas al hacer ese movimiento.
 */
public class DFS {
	Board board;
	static Deque<Set<Integer>> boxes;
	static int moves;
	static int solMoves;
	static String solString;
	static int totMoves;
	
	public DFS(Board board) {
		boxes = new LinkedList<Set<Integer>>();
		boxes.push(board.boxes);
		moves = 0;
		solMoves = -1;
		solString = "";
		totMoves = 0;
	}
	
	public void dfs(Player player){
		dfs("", "", player, '\0');
	}
	
	public void dfs(String tree, String sol, Player player, char move) {
		if (!validMove(player, move, boards.peekFirst()) )
				return;
		if(totMoves % 50000 == 0 ){
			System.out.println("Movimientos: " + totMoves);
			System.out.println("Profundidad(cajas): " + boards.size());
		}
		moves++;
		totMoves++;
		if (boards.peekFirst().nodes[player.i][player.j].cont == Contained.BOX || boards.peekFirst().nodes[player.i][player.j].cont == Contained.BOXNGOAL) {
		boxMoved = true;
		boards.push(boards.peekFirst().copy(player));
		int id = boards.peekFirst().nodes[player.i][player.j].id;
		boards.peekFirst().nodes[player.i][player.j].hadBox = true;
		int newId = -1;
		if (move == 'r') { // muevo la caja
			newId = boards.peekFirst().nodes[player.i][player.j + 1].id;
			if( boards.peekFirst().nodes[player.i][player.j + 1].cont == Contained.GOAL )
				boards.peekFirst().nodes[player.i][player.j + 1].cont = Contained.BOXNGOAL;
			else
				boards.peekFirst().nodes[player.i][player.j + 1].cont = Contained.BOX;
			boards.peekFirst().boxes.add(newId);
		} else if (move == 'u') {
			newId = boards.peekFirst().nodes[player.i - 1][player.j].id;
			if( boards.peekFirst().nodes[player.i - 1][player.j].cont == Contained.GOAL )
				boards.peekFirst().nodes[player.i - 1][player.j].cont = Contained.BOXNGOAL;
			else
				boards.peekFirst().nodes[player.i - 1][player.j].cont = Contained.BOX;
			boards.peekFirst().boxes.add(newId);
		} else if (move == 'l') {
			newId = boards.peekFirst().nodes[player.i][player.j - 1].id;
			if( boards.peekFirst().nodes[player.i][player.j - 1].cont == Contained.GOAL )
				boards.peekFirst().nodes[player.i][player.j - 1].cont = Contained.BOXNGOAL;
			else
				boards.peekFirst().nodes[player.i][player.j - 1].cont = Contained.BOX;
			boards.peekFirst().boxes.add(newId);
		} else if (move == 'd') {
			newId = boards.peekFirst().nodes[player.i + 1][player.j].id;
			if( boards.peekFirst().nodes[player.i + 1][player.j].cont == Contained.GOAL )
				boards.peekFirst().nodes[player.i + 1][player.j].cont = Contained.BOXNGOAL;
			else
				boards.peekFirst().nodes[player.i + 1][player.j].cont = Contained.BOX;
			boards.peekFirst().boxes.add(newId);
		}
		if( boards.peekFirst().nodes[player.i][player.j].cont == Contained.BOXNGOAL )
			boards.peekFirst().nodes[player.i][player.j].cont = Contained.GOAL;
		else
			boards.peekFirst().nodes[player.i][player.j].cont = Contained.FREESPACE;
		boards.peekFirst().boxes.remove(Integer.valueOf(id));
		if( !boards.peekFirst().goals.contains(newId) && (isCorner(newId) || canChop(newId) ) ){ // chequeo que no esté en un borde y que no se pueda cortar la rama
			boards.pop();
			moves--;
			return;
		}
		if( findedSolution() ){
			System.out.println(sol);
			solString = sol;
			System.out.println(moves);
			solMoves = moves;
			boards.pop();
			moves--;
			return;
		}
	}
	
		boards.peekFirst().nodes[player.i][player.j].visited = true;
	
		MovString mov = new MovString(boards.peekFirst());
		mov.getMovement(player.id);
		
		
		dfs(tree, sol + mov.get(0), new Player((player.i+mov.getMovementI(0))*boards.peekFirst().cols+player.j+mov.getMovementJ(0), player.i+mov.getMovementI(0), player.j+mov.getMovementJ(0),0), mov.get(0));
		dfs(tree, sol + mov.get(1), new Player((player.i+mov.getMovementI(1))*boards.peekFirst().cols+player.j+mov.getMovementJ(1), player.i+mov.getMovementI(1), player.j+mov.getMovementJ(1),0), mov.get(1));
		dfs(tree, sol + mov.get(2), new Player((player.i+mov.getMovementI(2))*boards.peekFirst().cols+player.j+mov.getMovementJ(2), player.i+mov.getMovementI(2), player.j+mov.getMovementJ(2),0), mov.get(2));
		dfs(tree, sol + mov.get(3), new Player((player.i+mov.getMovementI(3))*boards.peekFirst().cols+player.j+mov.getMovementJ(3), player.i+mov.getMovementI(3), player.j+mov.getMovementJ(3),0), mov.get(3));
	
		boards.peekFirst().nodes[player.i][player.j].visited = false;
		moves--;
		if( boxMoved )
			boards.pop();
	}

	private boolean findedSolution() {
		return boards.peekFirst().boxes.containsAll(boards.peekFirst().goals);
	}

	
	private boolean validMove(Player player, char move, Board board) {
		if (board.nodes[player.i][player.j].visited || ( solMoves != -1 && moves >= solMoves)) {
			return false;
		}

		if (board.nodes[player.i][player.j].cont == Contained.BOX
			|| board.nodes[player.i][player.j].cont == Contained.BOXNGOAL) {
			if (move == 'r'
				&& (board.nodes[player.i][player.j + 1].cont == Contained.BOX
				|| board.nodes[player.i][player.j + 1].cont == Contained.WALL 
				|| board.nodes[player.i][player.j + 1].cont == Contained.BOXNGOAL
				|| board.nodes[player.i][player.j + 1].hadBox )) {
				return false;
			} else if (move == 'l'
				&& (board.nodes[player.i][player.j - 1].cont == Contained.BOX
				|| board.nodes[player.i][player.j - 1].cont == Contained.WALL 
				|| board.nodes[player.i][player.j - 1].cont == Contained.BOXNGOAL
				|| board.nodes[player.i][player.j - 1].hadBox )) {
				return false;
			} else if (move == 'u'
				&& (board.nodes[player.i - 1][player.j].cont == Contained.BOX
				|| board.nodes[player.i - 1][player.j].cont == Contained.WALL 
				|| board.nodes[player.i - 1][player.j].cont == Contained.BOXNGOAL
				|| board.nodes[player.i - 1][player.j].hadBox )) {
				return false;
			} else if (move == 'd'
				&& (board.nodes[player.i + 1][player.j].cont == Contained.BOX
				|| board.nodes[player.i + 1][player.j].cont == Contained.WALL 
				|| board.nodes[player.i + 1][player.j].cont == Contained.BOXNGOAL
				|| board.nodes[player.i + 1][player.j].hadBox )) {
				return false;
			}
		} else if (board.nodes[player.i][player.j].cont == Contained.WALL) {
			return false;
		}

		return true;

	}
	
	public boolean isCorner( int id ){
		int i =	id/boards.peekFirst().cols;
		int j = id%boards.peekFirst().cols;
		if((	boards.peekFirst().nodes[i+1][j].cont == Contained.BOX 
				|| boards.peekFirst().nodes[i+1][j].cont == Contained.WALL 
				|| boards.peekFirst().nodes[i+1][j].cont == Contained.BOXNGOAL ) 
				&&(boards.peekFirst().nodes[i][j+1].cont == Contained.BOX 
				|| boards.peekFirst().nodes[i][j+1].cont == Contained.WALL 
				|| boards.peekFirst().nodes[i][j+1].cont == Contained.BOXNGOAL ))
					return true;
		if(( 	boards.peekFirst().nodes[i+1][j].cont == Contained.BOX 
				|| boards.peekFirst().nodes[i+1][j].cont == Contained.WALL 
				|| boards.peekFirst().nodes[i+1][j].cont == Contained.BOXNGOAL ) 
				&&(boards.peekFirst().nodes[i][j-1].cont == Contained.BOX 
				|| boards.peekFirst().nodes[i][j-1].cont == Contained.WALL 
				|| boards.peekFirst().nodes[i][j-1].cont == Contained.BOXNGOAL ))
					return true;
		if(( 	boards.peekFirst().nodes[i-1][j].cont == Contained.BOX 
				|| boards.peekFirst().nodes[i-1][j].cont == Contained.WALL 
				|| boards.peekFirst().nodes[i-1][j].cont == Contained.BOXNGOAL ) 
				&&(boards.peekFirst().nodes[i][j+1].cont == Contained.BOX 
				|| boards.peekFirst().nodes[i][j+1].cont == Contained.WALL 
				|| boards.peekFirst().nodes[i][j+1].cont == Contained.BOXNGOAL ))
					return true;
		if(( 	boards.peekFirst().nodes[i-1][j].cont == Contained.BOX 
				|| boards.peekFirst().nodes[i-1][j].cont == Contained.WALL 
				|| boards.peekFirst().nodes[i-1][j].cont == Contained.BOXNGOAL ) 
				&&(boards.peekFirst().nodes[i][j-1].cont == Contained.BOX 
				|| boards.peekFirst().nodes[i][j-1].cont == Contained.WALL 
				|| boards.peekFirst().nodes[i][j-1].cont == Contained.BOXNGOAL ))
					return true;
		return false;
	}

	
	public boolean canChop( int id ){
		int i =	id/board.cols;
		int j = id%board.cols;
		boolean hasGoal = false;
		boolean free = true;
		if(	boxes.peek().contains(board.nodes[i+1][j].id) //if the box has a blocked tile down
			|| board.nodes[i+1][j].cont == Contained.WALL ){
			free = false;
			for( int k = 0; !hasGoal && k<board.cols; k++ ){
				if( board.nodes[i][k].cont == Contained.GOAL || board.nodes[i+1][k].cont == Contained.FREESPACE )
					hasGoal = true;
			}
		}
		if(	boards.peekFirst().nodes[i-1][j].cont == Contained.BOX //if the box has a blocked tile up
			|| boards.peekFirst().nodes[i-1][j].cont == Contained.WALL 
			|| boards.peekFirst().nodes[i-1][j].cont == Contained.BOXNGOAL ){
			free = false;
			for( int k = 0; !hasGoal && k<boards.peekFirst().cols; k++ ){
				if( board.nodes[i][k].cont == Contained.GOAL || board.nodes[i-1][k].cont == Contained.FREESPACE )
					hasGoal = true;
			}
		}
		if(	boards.peekFirst().nodes[i][j+1].cont == Contained.BOX //if the box has a blocked tile to the right
			|| boards.peekFirst().nodes[i][j+1].cont == Contained.WALL 
			|| boards.peekFirst().nodes[i][j+1].cont == Contained.BOXNGOAL ){
			free = false;
			for( int k = 0; !hasGoal && k<boards.peekFirst().rows; k++ ){
				if( board.nodes[k][j].cont == Contained.GOAL || board.nodes[k][j+1].cont == Contained.FREESPACE )
					hasGoal = true;
			}
		}
		if(	boards.peekFirst().nodes[i][j-1].cont == Contained.BOX //if the box has a blocked tile to the left
			|| boards.peekFirst().nodes[i][j-1].cont == Contained.WALL 
			|| boards.peekFirst().nodes[i][j-1].cont == Contained.BOXNGOAL ){
			free = false;
			for( int k = 0; !hasGoal && k<boards.peekFirst().rows; k++ ){
				if( board.nodes[k][j].cont == Contained.GOAL || board.nodes[k][j-1].cont == Contained.FREESPACE )
					hasGoal = true;
			}
		}
		return !free && !hasGoal;
	}
}
