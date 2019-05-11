package io.linzhehuang.foo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class LexerTests {
	@Test
	public void mainTest() throws IOException, LexicalException {
		File file = new File("src/test/resources/source.js");
		InputStream input = new FileInputStream(file);
    	Lexer lexer  = new Lexer(input, "utf-8");
    	int width = 15;
    	
    	Token current = lexer.nextToken();
    	while (current.getName() != Token.TOKEN_EOF) {
    		String name = current.getName();
    		System.out.print(name);
    		int count = width - name.length();
    		count = (count < 0)? 0 : count;
    		while(count-- != 0) System.out.print(" ");
    		System.out.println(current.getValue());
    		current = lexer.nextToken();
    	}
	}
}
