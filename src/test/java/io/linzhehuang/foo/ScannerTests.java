package io.linzhehuang.foo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class ScannerTests {
	@Test
	public void mainTest() throws IOException {
		File file = new File("src/test/resources/source.foo");
		InputStream input = new FileInputStream(file);
    	Scanner scanner = new Scanner(input, "utf-8");
    	Token current = scanner.scan();
    	while (current.category != Token.TOKEN_EOF) {
    		System.out.print(current.category);
    		System.out.print("");
    		System.out.println(current.value);
    		current = scanner.scan();
    	}
	}
}
