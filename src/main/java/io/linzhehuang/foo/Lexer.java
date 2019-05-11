package io.linzhehuang.foo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;

public class Lexer {
	private final static HashSet<String> RESERVE = new HashSet<>(
			Arrays.asList(new String[]{
			"if", "else", "do", "while", "for", 
			"function", "var", "return", "new"
	}));
	
	private InputStreamReader reader;
	
	private char currentChar = '\0';
	private char nextChar = '\0';
	
	public Lexer(InputStream input, String charset) {
		reader = new InputStreamReader(input, Charset.forName(charset));
	}
	
	public Token nextToken() throws LexicalException {
		currentChar = (currentChar == '\0') ? readChar(): currentChar;
		while (currentChar != (char)-1) {
			nextChar = readChar();
			if (currentChar == '/' && nextChar == '/') {
				singleLineComment();
				continue;
			}
			if (currentChar == '/' && nextChar == '*') {
				multipleLinesComment();
				continue;
			}
			if ( (nextChar == '=') &&
				(currentChar == '+' || currentChar == '-' || 
				 currentChar == '*' || currentChar == '/' || 
				 currentChar == '%' || currentChar == '^' || 
				 currentChar == '|' || currentChar == '&' || 
				 currentChar == '!' || currentChar == '<' || 
				 currentChar == '>' || currentChar == '=' ) ) {
				return doubleSymbol();
			}
			if ((currentChar == '>' && nextChar == '>') ||
				(currentChar == '<' && nextChar == '<') ||
				(currentChar == '|' && nextChar == '|') ||
				(currentChar == '&' && nextChar == '&') ) {
				return doubleSymbol();
			}
			
			// 
			if (currentChar == '\r' || currentChar == '\n') {
				newLine();
				continue;
			}
			if (currentChar == '\u0020' || currentChar == '\u0009' ||
			    currentChar == '\u000b' || currentChar == '\u000c' ) {
				currentChar = readChar();
				continue;
			}
			if (Character.isDigit(currentChar)) {
				return number();
			}
			if (currentChar == '"') {
				return string();
			}
			if (Character.isLetter(currentChar) || currentChar == '_' ||
				currentChar == '$') {
				// return id or reverse token
				return word();
			}
			if (currentChar == '(' || currentChar == ')' ||
				currentChar == '[' || currentChar == ']' ||
				currentChar == '{' || currentChar == '}' ||
				currentChar == ',' || currentChar == '|' ||
				currentChar == '&' || currentChar == '^' ||
				currentChar == '=' || currentChar == '<' ||
				currentChar == '>' || currentChar == '!' ||
				currentChar == '?' || currentChar == ':' ||
				currentChar == ';' || currentChar == '+' ||
				currentChar == '-' || currentChar == '*' ||
				currentChar == '/' || currentChar == '%' ||
				currentChar == '.' ) {
				return singleSymbol();
			}
			throw new LexicalException("unkown token");
		}
		return new Token();
	}
	
	private char readChar() {
		try {
			int c = -1;
			if (nextChar == '\0') {
				c = reader.read();
			} else {
				c = nextChar;
				nextChar = '\0';
			}
			return ((char)c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ('\0');
	}
	
	private char readChar(int offset) {
		// offset equals 0 same as readChar()
		while (offset-- != 0) readChar();
		return readChar();
	}
	
	//
	private void newLine() {
		nextChar = readChar();
		if (nextChar == '\n') {
			currentChar = readChar();
		} else {
			currentChar = nextChar;
			nextChar = '\0';
		}
	}
	
	private void singleLineComment() {
		currentChar = readChar(1);  // skip "//" string
		while (currentChar != '\r' && currentChar != '\n') {
			if (currentChar == (char)-1) return;
			currentChar = readChar();
		}
		newLine();
	}
	
	private void multipleLinesComment() throws LexicalException {
		currentChar = readChar(1);  // skip "/*" string
		while (currentChar != (char)-1) {
			if (currentChar == '*') {
				nextChar = readChar();
				if (nextChar == '/') {
					currentChar = readChar(1); // skip "*/" string
					return;
				}
			}
			currentChar = readChar();
		}
		throw new LexicalException("illegal comment");
	}
	
	private Token number() throws LexicalException {
		StringBuilder number = new StringBuilder();
		Token token = new Token(Token.TOKEN_NUMBER);
		boolean isInteger = true;
		while (Character.isDigit(currentChar)) {
			number.append(currentChar);
			currentChar = readChar();
			if (currentChar == '.' && isInteger == true) {
				number.append(currentChar);
				currentChar = readChar();
				isInteger = false;
			} else if (currentChar == '.') {
				throw new LexicalException("illegal number");
			}
		}
		if (isInteger == true) {
			token.setValue(Integer.parseInt(number.toString()));
		} else {
			token.setValue(Double.parseDouble(number.toString()));
		}
		return token;
	}
	
	private Token string() throws LexicalException {
		StringBuilder string = new StringBuilder();
		Token token = new Token(Token.TOKEN_STRING);
		currentChar = readChar();
		while (currentChar != (char)-1) {
			if (currentChar == '"') {
				currentChar = readChar();
				token.setValue(string.toString());
				return token;
			}
			string.append(currentChar);
			currentChar = readChar();
		}
		throw new LexicalException("illegal string");
	}
	
	private Token word() throws LexicalException {
		StringBuilder word = new StringBuilder();
		word.append(currentChar);
		Token token = new Token(Token.TOKEN_ID);		
		currentChar = readChar();
		while (Character.isLetter(currentChar) || currentChar == '_' ||
			   Character.isDigit(currentChar) || currentChar == '$') {
			word.append(currentChar);
			currentChar = readChar();
		}
		if (RESERVE.contains(word.toString())) {
			// is reserve word
			token.setName(word.toString());
		} else {
			token.setValue(word.toString());
		}
		return token;
	}
	
	private Token doubleSymbol() {
		StringBuilder symbol = new StringBuilder();
		symbol.append(currentChar);
		symbol.append(nextChar);
		Token token = new Token(symbol.toString());
		currentChar = readChar(1);
		return token;
	}
	

	private Token singleSymbol() {
		StringBuilder symbol = new StringBuilder();
		symbol.append(currentChar);
		Token token = new Token(symbol.toString());
		currentChar = readChar();
		return token;
	}
}
