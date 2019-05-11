package io.linzhehuang.foo;

public class Token {
	public static final String TOKEN_EOF = "<eof>",
			                   TOKEN_ID = "<id>",
			                   TOKEN_NUMBER = "<number>",
			                   TOKEN_STRING = "<string>",
			                   TOKEN_ADD = "+",
			                   TOKEN_SEMICOLON = ";",
			                   TOKEN_ASSIGN = "=",
			                   TOKEN_IF = "if";
	
	private String name = TOKEN_EOF;
	private Object value = null;

	public Token() {
		
	}
	
	public Token(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
