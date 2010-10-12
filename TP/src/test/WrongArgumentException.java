package test;

public class WrongArgumentException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongArgumentException(){
		
	}
	
	public WrongArgumentException( String message){
		super(message);
	}

	
}
