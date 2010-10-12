package soko.solver;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class BFS {

	static boolean withTree;
	static List<Board> boards;
	static int moves;
	static int solMoves;
	static String solString;
	
	public BFS(Board board) {
		withTree = false;
		boards = new ArrayList<Board>();
		boards.add(board);
		moves = 0;
		solMoves = -1;
		solString = "";
	}
	
	public void bfs(){
		bfs("");
	}
	
	public void bfs(String tree) { 
		Deque<Player> queue = new LinkedList<Player>();
		Player player = boards.get(0).player;
		queue.offer(player);
		
		while( !queue.isEmpty() ){
			Player currPlayer = queue.poll();
			Board currBoard =  boards.get(currPlayer.inBoard);
			
			System.out.println(currBoard.print(currPlayer));//test
			if ( validMove(currPlayer)){
				Boolean enqueue=true;
				moves++;
				String currSol = currBoard.nodes[currPlayer.i][currPlayer.j].sol;
				if ( currBoard.nodes[currPlayer.i][currPlayer.j].cont == Contained.BOX ){
					System.out.println("box movement");//BORRAR
					int newId=0;
					int id = currBoard.nodes[player.i][player.j].id;
					boards.add(boards.get(player.inBoard).copy(currPlayer));
					currPlayer = new Player( currPlayer.id, currPlayer.i, currPlayer.j, currPlayer.inBoard+1); //se pone que el player esta en otro tablero
					Board newBoard = boards.get(currPlayer.inBoard);
					if (currSol.endsWith("u")){
						newId = newBoard.nodes[currPlayer.i-1][currPlayer.j].id;
						if( newBoard.nodes[currPlayer.i-1 ][currPlayer.j].cont == Contained.GOAL )
							newBoard.nodes[currPlayer.i-1 ][currPlayer.j].cont = Contained.BOXNGOAL;
						else
							newBoard.nodes[currPlayer.i-1 ][currPlayer.j].cont = Contained.BOX;
						newBoard.boxes.add(newId);
					}else if (currSol.endsWith("l")){
						newId = newBoard.nodes[currPlayer.i][currPlayer.j-1].id;
						if( newBoard.nodes[currPlayer.i][currPlayer.j-1].cont == Contained.GOAL )
							newBoard.nodes[currPlayer.i][currPlayer.j-1].cont = Contained.BOXNGOAL;
						else
							newBoard.nodes[currPlayer.i][currPlayer.j-1].cont = Contained.BOX;
						newBoard.boxes.add(newId);
					}else if (currSol.endsWith("d")){
						newId = newBoard.nodes[currPlayer.i+1][currPlayer.j].id;
						if( newBoard.nodes[currPlayer.i+1 ][currPlayer.j].cont == Contained.GOAL )
							newBoard.nodes[currPlayer.i+1 ][currPlayer.j].cont = Contained.BOXNGOAL;
						else
							newBoard.nodes[currPlayer.i+1 ][currPlayer.j].cont = Contained.BOX;
						newBoard.boxes.add(newId);
					}else if (currSol.endsWith("r")){
						newId = newBoard.nodes[currPlayer.i ][currPlayer.j+1].id;
						if( newBoard.nodes[currPlayer.i ][currPlayer.j+1].cont == Contained.GOAL )
							newBoard.nodes[currPlayer.i ][currPlayer.j+1].cont = Contained.BOXNGOAL;
						else
							newBoard.nodes[currPlayer.i][currPlayer.j+1].cont = Contained.BOX;
						newBoard.boxes.add(newId);
					}
					
					if( newBoard.nodes[currPlayer.i][currPlayer.j].cont == Contained.BOXNGOAL )
						currBoard.nodes[currPlayer.i][currPlayer.j].cont = Contained.GOAL;
					else
						newBoard.nodes[currPlayer.i][currPlayer.j].cont = Contained.FREESPACE;
					newBoard.boxes.remove(Integer.valueOf(id));
					
					if ( findedSolution( currPlayer ) ){
						System.out.println( newBoard.nodes[currPlayer.i][currPlayer.j].sol );
						boards.remove(currPlayer.inBoard);
						currPlayer = new Player( currPlayer.id, currPlayer.i, currPlayer.j, currPlayer.inBoard-1);
						moves--;
						enqueue = false;
						
					}

					if( !newBoard.goals.contains(newId) && (isCorner(newId) || canChop(newId, newBoard) ) ){ // chequeo que no est� en un borde y que no se pueda cortar la rama
						boards.remove(currPlayer.inBoard);
						System.out.println( "board" + currPlayer.inBoard + "deleted"); //BORRAR
						currPlayer = new Player( currPlayer.id, currPlayer.i, currPlayer.j, currPlayer.inBoard-1);
						//vuelvo la caja al board anterior, en la posicion en la que estaba
						//boards.get(currPlayer.inBoard).nodes[currPlayer.i][currPlayer.j].cont = Contained.BOX;
						moves--;
						enqueue = false;
					}
				}
				
				boards.get(currPlayer.inBoard).nodes[currPlayer.i][currPlayer.j].visited = true;
				
				if ( findedSolution( currPlayer ) ){
					System.out.println( currBoard.nodes[currPlayer.i][currPlayer.j].sol );
				}
				
				if ( enqueue ){
					if ( !(boards.get(currPlayer.inBoard).nodes[currPlayer.i][currPlayer.j-1].cont == Contained.WALL)){
						boards.get(currPlayer.inBoard).nodes[currPlayer.i][currPlayer.j-1].sol = currSol+"u";
						queue.offer(new Player( currPlayer.id, currPlayer.i, currPlayer.j-1, currPlayer.inBoard));
					}
					if ( !(boards.get(currPlayer.inBoard).nodes[currPlayer.i-1][currPlayer.j].cont == Contained.WALL)){
						boards.get(currPlayer.inBoard).nodes[currPlayer.i-1][currPlayer.j].sol = currSol+"l";
						queue.offer(new Player( currPlayer.id, currPlayer.i-1, currPlayer.j, currPlayer.inBoard));
					}
					if ( !(boards.get(currPlayer.inBoard).nodes[currPlayer.i][currPlayer.j+1].cont == Contained.WALL)){
						boards.get(currPlayer.inBoard).nodes[currPlayer.i][currPlayer.j+1].sol = currSol+"d";
						queue.offer(new Player( currPlayer.id, currPlayer.i, currPlayer.j+1, currPlayer.inBoard));
					}
					if ( !(boards.get(currPlayer.inBoard).nodes[currPlayer.i+1][currPlayer.j].cont == Contained.WALL)){
						boards.get(currPlayer.inBoard).nodes[currPlayer.i+1][currPlayer.j].sol = currSol+"r";
						queue.offer(new Player( currPlayer.id, currPlayer.i+1, currPlayer.j, currPlayer.inBoard));
					}
					
					
				}
					
			
			}
	
		}
	}
	
	private boolean findedSolution(Player player) {
		return boards.get( player.inBoard).boxes.equals(boards.get(player.inBoard).goals);
	}

	private boolean validMove(Player player) {
		Board currBoard =  boards.get(player.inBoard);
		String currSol = currBoard.nodes[player.i][player.j].sol;
		if (currBoard.nodes[player.i][player.j].visited) {
			return false;
		}

		if (currBoard.nodes[player.i][player.j].cont == Contained.BOX || currBoard.nodes[player.i][player.j].cont == Contained.BOXNGOAL) {
			if (currSol.endsWith("r")
					&& (currBoard.nodes[player.i ][player.j+1].cont == Contained.BOX
							|| currBoard.nodes[player.i ][player.j+1].cont == Contained.WALL || currBoard.nodes[player.i][player.j+1].cont == Contained.BOXNGOAL)) {
				return false;
			} else if (currSol.endsWith("l")
					&& (currBoard.nodes[player.i ][player.j-1].cont == Contained.BOX
							|| currBoard.nodes[player.i ][player.j-1].cont == Contained.WALL || currBoard.nodes[player.i][player.j-1].cont == Contained.BOXNGOAL)) {
				return false;
			} else if (currSol.endsWith("u")
					&& (currBoard.nodes[player.i-1][player.j].cont == Contained.BOX
							|| currBoard.nodes[player.i-1][player.j].cont == Contained.WALL || currBoard.nodes[player.i-1][player.j].cont == Contained.BOXNGOAL)) {
				return false;
			} else if (currSol.endsWith("d")
					&& (currBoard.nodes[player.i][player.j + 1].cont == Contained.BOX
							|| currBoard.nodes[player.i][player.j + 1].cont == Contained.WALL || currBoard.nodes[player.i][player.j + 1].cont == Contained.BOXNGOAL)) {
				return false;
			}
		} else if (currBoard.nodes[player.i][player.j].cont == Contained.WALL) {
			return false;
		}

		return true;

	}
	
	public boolean isCorner( int id ){
		int i =	id/boards.get(0).cols;
		int j = id%boards.get(0).cols;
		if((	boards.get(0).nodes[i+1][j].cont == Contained.BOX 
				|| boards.get(0).nodes[i+1][j].cont == Contained.WALL 
				|| boards.get(0).nodes[i+1][j].cont == Contained.BOXNGOAL ) 
				&&(boards.get(0).nodes[i][j+1].cont == Contained.BOX 
				|| boards.get(0).nodes[i][j+1].cont == Contained.WALL 
				|| boards.get(0).nodes[i][j+1].cont == Contained.BOXNGOAL ))
					return true;
		if(( 	boards.get(0).nodes[i+1][j].cont == Contained.BOX 
				|| boards.get(0).nodes[i+1][j].cont == Contained.WALL 
				|| boards.get(0).nodes[i+1][j].cont == Contained.BOXNGOAL ) 
				&&(boards.get(0).nodes[i][j-1].cont == Contained.BOX 
				|| boards.get(0).nodes[i][j-1].cont == Contained.WALL 
				|| boards.get(0).nodes[i][j-1].cont == Contained.BOXNGOAL ))
					return true;
		if(( 	boards.get(0).nodes[i-1][j].cont == Contained.BOX 
				|| boards.get(0).nodes[i-1][j].cont == Contained.WALL 
				|| boards.get(0).nodes[i-1][j].cont == Contained.BOXNGOAL ) 
				&&(boards.get(0).nodes[i][j+1].cont == Contained.BOX 
				|| boards.get(0).nodes[i][j+1].cont == Contained.WALL 
				|| boards.get(0).nodes[i][j+1].cont == Contained.BOXNGOAL ))
					return true;
		if(( 	boards.get(0).nodes[i-1][j].cont == Contained.BOX 
				|| boards.get(0).nodes[i-1][j].cont == Contained.WALL 
				|| boards.get(0).nodes[i-1][j].cont == Contained.BOXNGOAL ) 
				&&(boards.get(0).nodes[i][j-1].cont == Contained.BOX 
				|| boards.get(0).nodes[i][j-1].cont == Contained.WALL 
				|| boards.get(0).nodes[i][j-1].cont == Contained.BOXNGOAL ))
					return true;
		return false;
	}
	
	public boolean canChop( int id , Board board){
		int i =	id/board.cols;
		int j = id%board.cols;
		boolean hasGoal = false;
		boolean free = true;
		if(	board.nodes[i+1][j].cont == Contained.BOX //if the box has a blocked tile down
			|| board.nodes[i+1][j].cont == Contained.WALL 
			|| board.nodes[i+1][j].cont == Contained.BOXNGOAL ){
			free = false;
			for( int k = 0; !hasGoal && k<board.cols; k++ ){
				if( board.nodes[i][k].cont == Contained.GOAL || board.nodes[i+1][k].cont == Contained.FREESPACE )
					hasGoal = true;
			}
		}
		if(	board.nodes[i-1][j].cont == Contained.BOX //if the box has a blocked tile up
			|| board.nodes[i-1][j].cont == Contained.WALL 
			|| board.nodes[i-1][j].cont == Contained.BOXNGOAL ){
			free = false;
			for( int k = 0; !hasGoal && k<board.cols; k++ ){
				if( board.nodes[i][k].cont == Contained.GOAL || board.nodes[i-1][k].cont == Contained.FREESPACE )
					hasGoal = true;
			}
		}
		if(	board.nodes[i][j+1].cont == Contained.BOX //if the box has a blocked tile to the right
			|| board.nodes[i][j+1].cont == Contained.WALL 
			|| board.nodes[i][j+1].cont == Contained.BOXNGOAL ){
			free = false;
			for( int k = 0; !hasGoal && k<board.rows; k++ ){
				if( board.nodes[k][j].cont == Contained.GOAL || board.nodes[k][j+1].cont == Contained.FREESPACE )
					hasGoal = true;
			}
		}
		if(	board.nodes[i][j-1].cont == Contained.BOX //if the box has a blocked tile to the left
			|| board.nodes[i][j-1].cont == Contained.WALL 
			|| board.nodes[i][j-1].cont == Contained.BOXNGOAL ){
			free = false;
			for( int k = 0; !hasGoal && k<board.rows; k++ ){
				if( board.nodes[k][j].cont == Contained.GOAL || board.nodes[k][j-1].cont == Contained.FREESPACE )
					hasGoal = true;
			}
		}
		return !free && !hasGoal;
	}
}
