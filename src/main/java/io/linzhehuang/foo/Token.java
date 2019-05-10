package io.linzhehuang.foo;

public class Token {
	public static final String TOKEN_EOF = "eof",
			                   TOKEN_ID = "<id>",
			                   TOKEN_NUMBER = "<number>",
			                   TOKEN_ADD = "+",
			                   TOKEN_SEMICOLON = ";",
			                   TOKEN_ASSIGN = "=",
			                   TOKEN_IF = "if";
	
	public String category;
	public Object value = null;
	
	public Token() {
		this.category = TOKEN_EOF;
	}
	
	public Token(String category) {
		this.category = category;
	}
	
	public Token(char symbol) {
		this.category = Character.toString(symbol);
	}
}
