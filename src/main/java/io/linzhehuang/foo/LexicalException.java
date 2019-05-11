package io.linzhehuang.foo;

public class LexicalException extends Exception {
	
	private static final long serialVersionUID = 1L;
	public LexicalException() {
        super();
    }
	
    public LexicalException(String message) {
        super(message);
    }
 
    public LexicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public LexicalException(Throwable cause) {
        super(cause);
    }
}
