package find;

import java.io.*;
import java.util.*;

public class Create {
	public Create() {

		// java的关键字
		String[] listJWord = { "abstract", "boolean", "byte", "char", "class",
				"const", "double", "enum", "final", "float", "int",
				"interface", "long", "native", "package", "private",
				"protected", "public", "short", "static", "strictfp",
				"synchronized", "transient", "void", "volatile" };

		String[] listJWord2 = { "if", "for", "else", "break", "new", "throws",
				"while", "try", "throw", "this", "switch", "super", "return",
				"instanceof", "import", "implement", "finally", "extends",
				"do", "default", "continue", "catch", "case", "assert",
				"false", "true", "goto", "null" };

		// C语言中的关键字
		String[] listCWord = { "auto", "char", "const", "double", "enum",
				"extern", "float", "int", "long", "register", "short",
				"signed", "static", "struct", "union", "unsigned", "void",
				"volatile" };
		String[] listCWord2 = { "break", "case", "continue", "default", "do",
				"else", "for", "goto", "if", "return", "sizeof", "switch",
				"typedef", "while" };

		// C++的关键字
		String[] listCPWord = { "asm", "auto", "bool", "char", "class",
				"const", "double", "enum", "explicit", "extern", "float",
				"friend", "inline", "int", "long", "mutable", "private",
				"protected", "public", "register", "short", "signed", "struct",
				"static", "template", "typename", "union", "unsigned",
				"virtual", "void", "volatile" };
		String[] listCPWord2 = { "and", "and_eq", "bitand", "bitor", "break",
				"case", "catch", "compl", "const_cast", "continue", "default",
				"delete", "do", "dynamic_cast", "else", "false", "for", "goto",
				"if", "namespace", "new", "not", "not_eq", "operator", "or",
				"or_eq", "reinterpret_cast", "return", "sizeof", "static_cast",
				"throw", "switch", "this", "typeid", "true", "try", "typedef",
				"using", "while", "xor" };

		ArrayList<String> jWord = new ArrayList<String>();
		ArrayList<String> jWord2 = new ArrayList<String>();
		ArrayList<String> cWord = new ArrayList<String>();
		ArrayList<String> cWord2 = new ArrayList<String>();
		ArrayList<String> cPWord = new ArrayList<String>();
		ArrayList<String> cPWord2 = new ArrayList<String>();

		for (int i = 0; i < listJWord.length; i++) {
			jWord.add(listJWord[i]);
		}

		for (int i = 0; i < listJWord2.length; i++) {
			jWord2.add(listJWord2[i]);
		}

		for (int i = 0; i < listCWord.length; i++) {
			cWord.add(listCWord[i]);
		}

		for (int i = 0; i < listCWord2.length; i++) {
			cWord2.add(listCWord2[i]);
		}

		for (int i = 0; i < listCPWord.length; i++) {
			cPWord.add(listCPWord[i]);
		}

		for (int i = 0; i < listCPWord2.length; i++) {
			cPWord2.add(listCPWord2[i]);
		}

		try {
			ObjectOutputStream output = new ObjectOutputStream(
					new FileOutputStream("java.dat"));
			output.writeObject(jWord);
			output.writeObject(jWord2);
			output.close();

			output = new ObjectOutputStream(new FileOutputStream("c.dat"));
			output.writeObject(cWord);
			output.writeObject(cWord2);
			output.close();

			output = new ObjectOutputStream(new FileOutputStream("c+.dat"));
			output.writeObject(cPWord);
			output.writeObject(cPWord2);
			output.close();

		} catch (IOException ex) {
			System.out.print("Error " + ex);
			System.exit(0);
		}
		// catch(ClassNotFoundException ex){
		// System.out.print("Error " + ex);
		// System.exit(0);

		// }
	}
}