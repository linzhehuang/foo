package io.linzhehuang.foo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;

public class Scanner {
	private final static HashSet<String> RESERVE = new HashSet<>(
			Arrays.asList(new String[]{
					"if", "else", "do", "while", "function", "var", "return"
			}));
	
	private InputStreamReader reader = null;
	private char current = '\0';
	private char next = '\0';
	
	public Scanner(InputStream input, String charset) {
		this.reader = new InputStreamReader(input,
				Charset.forName(charset));
	}
	
	/**
	 * Get the next character.
	 * @return return -1 if end
	 */
	private char next() {
		int n = -1;
		try {
			if (next != '\0') {
				n = next;
				next = '\0';
			} else {
				n = reader.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ((char)n);
	}
	
	private boolean isSpace(char c) {
		return (c == '\u0020' // SPACE
			 || c == '\u0009' // HT
			 || c == '\u000b' // VT
			 || c == '\u000c' // FF
			 );
	}
	
	private boolean isNewLine(char c) {
		return (c == '\r' 
			 || c == '\n');
	}
	
	private boolean isEnd(char c) {
		return (c == (char)-1);
	}
	
	private boolean isOperator(char c) {
		return (c == '+'
			 || c == '-'
			 || c == '*'
			 || c == '/'
			 || c == '%'
			 || c == '!'
			 || c == '='
			 || c == ';'
			 || c == ')'
			 || c == '('
			 || c == '{'
			 || c == '}'
			 || c == '['
			 || c == ']'
             || c == '^'
             || c == '|'
             || c == '&'
			 || c == ','
			 || c == '<'
			 || c == '>');
	}
	
	private Token getOperatorToken(char c, char n) {
		current = next();
		if (n == '=') {
			if (c == '=' 
			 || c == '!'
			 || c == '<'
			 || c == '>'
			 || c == '+'
			 || c == '-'
			 || c == '*'
			 || c == '/') {
				current = next();
				return new Token(new String(new char[] {c, n}));
			}
		}
		if ((c == '<' && n == '<') ||
			(c == '>' && n == '>') ||
			(c == '|' && n == '|') ||
			(c == '&' && n == '&')) {
			current = next();
			return new Token(new String(new char[] {c, n}));
		}
		
		return new Token(c);
	}
	
	private void newLine() {
		char next = next();
		if (next == '\n') {
			current = next();
		} else {
			current = next;
		}
	}
	
	private void comment() {
		do {
			current = next();
		} while (!isNewLine(current) && !isEnd(current));
		newLine();
	}
	
	private String word() {
		StringBuilder word = new StringBuilder();
		while (Character.isLetter(current) || Character.isDigit(current)) {
			word.append(Character.toString(current));
			current = next();
		}
		return word.toString();
	}
	
	private String number() {
		StringBuilder number = new StringBuilder();
		while (Character.isDigit(current)) {
			number.append(Character.toString(current));
			current = next();
		}
		return number.toString();
	}
	
	private Token getNumberToken(String number) {
		Token token = new Token(Token.TOKEN_NUMBER);
		token.value = Integer.parseInt(number);
		return token;
	}
	
	private Token getWordToken(String word) {
		Token token = new Token();
		if (RESERVE.contains(word)) {
			token.category = word;
		} else {
			token.category = Token.TOKEN_ID;
			token.value = word;
		}
		return token;
	}
	
	public Token scan() {
		
		if (current == '\0')
			current = next();
		
		while (!isEnd(current)) {
			// ignore comment
			if (current == '/' && ((next = next()) == '/')) {
				comment();
				continue;
			} else {
				// ignore space character
				if (isSpace(current)) {
					current = next();
					continue;
				}
				// next line
				if (isNewLine(current)) {
					newLine();
					continue;
				}
				
				if (isOperator(current)) {
					next = next();
					return getOperatorToken(current, next);
				}
				//
				if (Character.isLetter(current)) {
					return getWordToken(word());
				}
				else if (Character.isDigit(current)) {
					return getNumberToken(number());
				}
				else {
					current = next();
				}
			}
		}
		return new Token();
	}
	
}
