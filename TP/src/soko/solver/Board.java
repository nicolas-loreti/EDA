package soko.solver;

import java.util.ArrayList;
import java.util.List;

public class Board {

	List<Integer> boxes;
	List<Integer> goals;
	Player player;
	Node[][] nodes;
	int rows;
	int cols;
	
	class Node{
		int id;
		Contained cont;
		boolean visited;
		boolean hadBox;
		String sol;
		public Node( ) {
			this.cont = Contained.FREESPACE;
			visited = false;
			hadBox = false;
			sol = "";
		}
		public Node( int id, Contained cont){
			this.cont = cont;
			this.id = id;
			this.visited = false;
			hadBox = false;
		}
	}
	
	
	public Board( int rows, int cols){
		nodes = new Node[rows][cols];
		this.rows = rows;
		this.cols = cols;
		boxes = new ArrayList<Integer>();
		goals = new ArrayList<Integer>();
		for( int i=0; i<rows; i++ ){
			for( int j=0; j<cols; j++ ){
				nodes[i][j] = new Node();
				nodes[i][j].id = i*cols + j;
			}
		}
	}
	
	public Board copy( Player player ){
		Board ans = new Board(rows,  cols);
		for ( int i=0; i<rows; i++ ){
			for ( int j=0; j <cols; j++ ){
				ans.nodes[i][j] = new Node(nodes[i][j].id, nodes[i][j].cont);
			}
		}
		ans.boxes = copy(this.boxes);
		ans.goals = copy(this.goals);
		ans.player = player;
		return ans;
	}
	
	private List<Integer> copy( List<Integer> lst ){
		List<Integer> newLst = new ArrayList<Integer>();
		for( Integer i: lst )
			newLst.add(i);
		return newLst;
	}
	
	public String print(Player p){
		String resp = "";
		for ( int i=0; i<rows; i++ ){
			for ( int j=0; j <cols; j++ ){
				if( p.i != i || p.j != j )
					resp += String.valueOf(nodes[i][j].cont.getContType());
				else
					resp += '@';
			}
			resp += '\n';
		}
		return resp;
	}
	
	
}
