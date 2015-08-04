package common;

public class fileNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public String getMessage(){
		
		return "File not found \n";
		
	}
}
