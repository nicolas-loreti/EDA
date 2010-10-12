package soko.solver;

enum Contained {
	WALL('#'), PLAYER('@'), PLAYERNGOAL('+'), BOX('$'), BOXNGOAL('*'), GOAL('.'), FREESPACE(' ');

	private char contType;

	private Contained(char contType) {
		this.contType = contType;
	}

	public char getContType() {
		return contType;
	}
}
