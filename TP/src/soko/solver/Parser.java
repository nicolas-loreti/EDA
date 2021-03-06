package soko.solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Parser {
	 String level;
	
	public Parser( String level ){
		this.level = level;
	}
	
	
	public Board parse() throws IOException {
		
		BufferedReader inStream = new BufferedReader( new FileReader(level));
		List<Integer> boxes= new ArrayList<Integer>();
		List<Integer> goals = new ArrayList<Integer>();
		List<String> lines = new ArrayList<String>();
		int cols=0;
		String line;
		while( (line = inStream.readLine()) != null ){
			lines.add(line);
			if ( line.length() > cols ){
				cols = line.length();
			}
		}
	
		inStream.close();
		Board board = new Board(lines.size(), cols);
		int idCount=0;
		for( int i=0; i<lines.size(); i++){
			for( int j=0; j<lines.get(i).length();j++){
				switch(lines.get(i).charAt(j)){
				case '#': board.nodes[i][j] = board.new Node(idCount, Contained.WALL); break;
				case ' ': board.nodes[i][j] = board.new Node(idCount, Contained.FREESPACE); break;
				case '.': board.nodes[i][j] = board.new Node(idCount, Contained.GOAL); 
						  goals.add(idCount); break;
				case '$': board.nodes[i][j] = board.new Node(idCount, Contained.BOX); 
						  boxes.add(idCount);break;
				case '+': board.nodes[i][j] = board.new Node(idCount, Contained.GOAL);
						  goals.add(idCount);board.player = new Player(idCount, i, j,0);break;
				case '@': board.nodes[i][j] = board.new Node(idCount, Contained.FREESPACE);
						  board.player = new Player(idCount, i, j,0);break;
				case '*': board.nodes[i][j] = board.new Node(idCount, Contained.BOXNGOAL);
						  boxes.add(idCount); goals.add(idCount); break;
				default: throw new IOException();
				}
				idCount++;
			}
			board.boxes = boxes;
			board.goals = goals;
		}
		
		return board;
		
	}

}
