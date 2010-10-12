package test;

public enum Move {
	UP('u'), DOWN('d'), RIGHT('r'), LEFT('l');

	private char contType;

	private Move(char contType) {
		this.contType = contType;
	}

	private Move( String s ){
		this.contType = s.toCharArray()[0];
	}
	
	public char getContType() {
		return contType;
	}
}
