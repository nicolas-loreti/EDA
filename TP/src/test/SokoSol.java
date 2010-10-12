package test;


public class SokoSol {
	static Parser parser;
	static boolean withTree = false;

	public static void main(String[] args) throws Exception {
		Board board = (new Parser("/home/amarseil/level1")).parse();

		
		if ("bfs".equals(args[1])){
			(new BFS(board)).bfs();
			
		}
		else if ("dfs".equals(args[1]))
			(new DFS(board)).dfs(board.player);
		else
			throw new WrongArgumentException("wrong");

		/*if ("tree".equals(args[3])) {
			withTree = true;
		}*/
	}
}
