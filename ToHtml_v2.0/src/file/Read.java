package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import color.Color;
import find.Trie;

/**
 * 读取文件并转换代码
 * 
 * @author Miku
 * 
 */
public class Read {

	Trie kwchange = new Trie(); // 关键字匹配
	private String code; // 文件编码
	Color color;
	private String sourceCode; // 源代码
	private int fileType;

	/** EOT=End of text */
	private final static char EOT = (char) -1;

	private FileInputStream is;
	private FileOutputStream os;
	private InputStreamReader ir;
	private OutputStreamWriter ow;
	private BufferedReader br;
	private BufferedWriter bw;

	private int cnt = 1; // 行号计数
	private String line;
	private StringBuffer sb = new StringBuffer();

	/** 标志源代码中每个字符的类型 */
	private int[] sourceTypes;

	private int state = COD;
	private final static short PARSESTATE_FINISHED = -1;
	private final static short COD = 0; // CODE
	private final static short CAC = 1; // CODE AWAIT COMMENT
	private final static short CL = 2; // COMMENT LINE
	private final static short CBJ1 = 3; // COMMENT BLOCK or COMMENT JAVADOC 1
	private final static short CBJ2 = 4; // COMMENT BLOCK or COMMENT JAVADOC 1
	private final static short CB = 5; // COMMENT BLOCK
	private final static short CBA = 6; // COMMENT BLOCK AWAIT END
	private final static short CJ = 7; // COMMENT JAVADOC
	private final static short CJA = 8; // COMMENT JAVADOC AWAIT END
	private final static short QU = 9; // QUOTE
	private final static short QUA = 10; // QUOTE AWAIT \"
	private final static short CH1 = 11; //
	private final static short CH2 = 12; //
	private final static short CH3 = 13; //
	private final static short CH4 = 14; //
	private final static short CH5 = 15; //
	private final static short CH6 = 16; //

	public final static short CODE = 0;
	private final static short COMMENT_LINE = 1;
	private final static short JAVADOC = 2;
	private final static short COMMENT_BLOCK = 3;
	private final static short STRING = 4;
	private final static short CHAR_CONSTANT = 5;
	private final static short LINE_NUMBERS = 6;
	private final static short PARENTHESIS = 7;
	private final static short NUM_CONSTANT = 8;
	private final static short CODE_TYPE = 9;
	private final static short TAB_KEY = 10;
	private final static short JAVADOC_KEYWORD = 11;
	private final static short JAVADOC_HTML_TAG = 12;
	private final static short JAVADOC_LINKS = 13;
	private final static short SWITCHLINE = 14;
	private final static short UNDEFINED = 15;
	private final static short ANNOTATION = 16;
	private final static short BACKGROUND = 17;
	private final static short SPACE = 18;
	public final static short KEYWORD = 19;
	private final static short LT = 20;
	private final static short RT = 21;

	public String bottom = "\n</div></code></body></html>";
	FileName fn = new FileName();

	public void setcolor(Color co) {
		this.color = co;
	}

	public FileName getfn() {
		return this.fn;
	}

	/**
	 * 从文件读取源代码并转换写入
	 */
	public void readfile(String filename, int fileType, Color co)
			throws FileNotFoundException, IOException {
		checkCode(filename);

		this.color = co;

		String outputDir = new String();
		// String s = "f://temp//test//test.java"; //tmp for test
		if (outputDir == "") {
			outputDir = filename + ".html";// 如果目录为空，输出到源文件目录
		} else {
			// getfn().setFilePath(s);//tmp for test
			outputDir = fn.getFilePath() + fn.getFileName(filename) + ".html"; // 输出文件末尾添加".html"即为html文档
			// outputDir = fn.getFileName(filename)+".html";//tmp for test
		}
		// System.out.println(outputDir);
		try {
			is = new FileInputStream(filename);
			os = new FileOutputStream(outputDir);
			ir = new InputStreamReader(is, code);
			ow = new OutputStreamWriter(os, code);
			br = new BufferedReader(ir);
			bw = new BufferedWriter(ow);

			bw.write(htmlHead(filename)); // 写入html开头
			sb.setLength(0); // 清空缓冲区

			sourceCode = readSource(br); // 读取源代码
			sourceTypes = new int[sourceCode.length()];
			setsourceTypes(sourceCode); // 标志源代码中每个字符的类型
			sb.setLength(0); // 清空缓冲区
			setSourceColor(sourceCode, fileType); // 添加html颜色标签

			bw.write(sb.toString()); // 写入文件

			bw.write(bottom); // 写入html结尾
		} catch (FileNotFoundException e) { // 文件是否存在
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			cnt = 1;
			FileIO.close(br);// 关闭读文件
			FileIO.close(bw);// 关闭写文件
		}
	}

	public String readText(String text, int stringType, Color co) {
		setcolor(co);
		sb.setLength(0); // 清空缓冲区
		// sb.append(htmlHead(textName));
		sourceTypes = new int[text.length()];
		setsourceTypes(text); // 标志源代码中每个字符的类型
		setSourceColor(text, stringType); // 添加html颜色标签
		// sb.append(bottom);
		return sb.toString();
	}

	/**
	 * 添加html文件开头
	 */
	public String htmlHead(String filename) {
		sb = new StringBuffer(1000); // 临时缓冲区

		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
		sb.append("<html xmlns=\"zh\" lang=\"zh\">\n");
		sb.append("<head>\n");
		sb.append(
				" <meta http-equiv=\"Content-Type\" content=\"text/html; charset=")
				.append(code).append("\" />\n");
		sb.append("<title>").append(filename).append("</title>");
		sb.append("</head>\n");
		sb.append("<body bgcolor=\"#efefef\">\n");
		sb.append("<font face=\"Courier New\" color=\"" + color.getColor(0)
				+ "\">\n");
		sb.append("<code>\n");
		sb.append("<div align=\"{0}\" class=\"java\">\n");

		return sb.toString(); // 返回String类型
	}

	/**
	 * 一行一行读取文件源代码并放入缓冲区
	 */
	private String readSource(BufferedReader bw) throws IOException {

		while ((line = bw.readLine()) != null) { // 读取文件一行知道文件末尾
			sb.append(line); // 添加读取文件的一行到输出缓冲区
			sb.append("\r\n"); // 添加换行符
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);// 最后的换行去掉
		}
		return sb.toString();
	}

	/**
	 * 标志源代码中每个字符的类型在数组sourceTypes[]中
	 * 
	 * @param source
	 */
	private void setsourceTypes(String source) {
		int pos = 0; // 在用来标记源代码字符类型sourceTypes[]数组中的位置
		int sourcePos = 0; // 在输入字符串中位置
		int len = source.length();// 输入字符串的长度
		state = CODE; // 初始化状态
		char ch = EOT; // 初始化ch,每次读取的一个字符
		while (sourcePos < len) {// 循环读取整个字符串
			ch = source.charAt(sourcePos++);// 每次读取一个字符
			// System.out.print("state:"+state);
			switch (state) { // 根据状态转移标记源代码中每个字符的类型
			case COD:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '\n' || ch == '\r') {
					sourceTypes[pos++] = SWITCHLINE;
					break;
				}
				if (ch == '/') {
					state = CAC;
					break;
				}
				if (ch == '"') {
					sourceTypes[pos++] = STRING;
					state = QU;
					break;
				}
				if (ch == '\'') {
					state = CH1;
					break;
				}
				if (ch == ' ' || ch == '\t') {
					sourceTypes[pos++] = BACKGROUND;
					break;
				}
				if (isParenthesis(ch)) {
					sourceTypes[pos++] = PARENTHESIS; // 标记括号类型
					break;
				}
				if (ch == '<') {
					sourceTypes[pos++] = LT;
					break;
				}
				if (ch == '<') {
					sourceTypes[pos++] = RT;
					break;
				}
				sourceTypes[pos++] = CODE;
				break;
			case CAC:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					sourceTypes[pos++] = CODE;
					break;
				}
				if (ch == '/') {
					sourceTypes[pos++] = COMMENT_LINE;
					sourceTypes[pos++] = COMMENT_LINE;
					state = CL;
					break;
				}
				if (ch == '*') {
					state = CBJ1;
					break;
				}
				if (ch == '\n' || ch == '\r') {
					sourceTypes[pos++] = CODE;
					sourceTypes[pos++] = SWITCHLINE;
					state = COD;
					break;
				}
				if (ch == ' ' || ch == '\t') {
					sourceTypes[pos++] = CODE;
					sourceTypes[pos++] = BACKGROUND;
					state = COD;
					break;
				}
				sourceTypes[pos++] = CODE;
				sourceTypes[pos++] = CODE;
				state = COD;
				break;
			case CL:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '\n' || ch == '\r') {
					sourceTypes[pos++] = SWITCHLINE;
					state = COD;
					break;
				}
				if (ch == ' ' || ch == '\t') {
					sourceTypes[pos++] = BACKGROUND;
					break;
				}
				sourceTypes[pos++] = COMMENT_LINE;
				break;
			case CB:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '*') {
					sourceTypes[pos++] = COMMENT_BLOCK;
					state = CBA;
					break;
				}
				if (ch == '\n' || ch == '\r') {
					sourceTypes[pos++] = SWITCHLINE;
					break;
				}
				if (ch == ' ' || ch == '\t') {
					sourceTypes[pos++] = BACKGROUND;
					break;
				}
				sourceTypes[pos++] = COMMENT_BLOCK;
				break;
			case CBA:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '/') {
					sourceTypes[pos++] = COMMENT_BLOCK;
					state = COD;
					break;
				}
				if (ch == '*') {
					sourceTypes[pos++] = COMMENT_BLOCK;
					state = CBA;
					break;
				}
				if (ch == '\n' || ch == '\r') {
					sourceTypes[pos++] = SWITCHLINE;
					state = CB;
					break;
				}
				if (ch == ' ' || ch == '\t') {
					sourceTypes[pos++] = BACKGROUND;
					state = CB;
					break;
				}
				sourceTypes[pos++] = COMMENT_BLOCK;
				state = CB;
				break;
			case CJ:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '*') {
					sourceTypes[pos++] = JAVADOC;
					state = CJA;
					break;
				}
				if (ch == '\n' || ch == '\r') {
					sourceTypes[pos++] = SWITCHLINE;
					break;
				}
				if (ch == ' ' || ch == '\t') {
					sourceTypes[pos++] = BACKGROUND;
					break;
				}
				sourceTypes[pos++] = JAVADOC;
				break;
			case CJA:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '/') {
					sourceTypes[pos++] = JAVADOC;
					state = COD;
					break;
				}
				if (ch == '*') {
					sourceTypes[pos++] = JAVADOC;
					state = CJA;
					break;
				}
				if (ch == '\n' || ch == '\r') {
					sourceTypes[pos++] = SWITCHLINE;
					state = CJ;
					break;
				}
				if (ch == ' ' || ch == '\t') {
					sourceTypes[pos++] = BACKGROUND;
					state = CJ;
					break;
				}
				sourceTypes[pos++] = JAVADOC;
				state = CJ;
				break;
			case QU:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '"') {
					sourceTypes[pos++] = STRING;
					state = COD;
					break;
				}
				if (ch == '\\') {
					state = QUA;
					break;
				}
				if (ch == ' ' || ch == '\t') {
					sourceTypes[pos++] = BACKGROUND;
					break;
				}
				sourceTypes[pos++] = STRING;
				break;
			case QUA:
				if (ch == EOT) {
					sourceTypes[pos++] = STRING;
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '\\') {
					sourceTypes[pos++] = STRING;
					sourceTypes[pos++] = STRING;
					state = QU;
					break;
				}
				if (ch == ' ' || ch == '\t') {
					sourceTypes[pos++] = STRING;
					sourceTypes[pos++] = BACKGROUND;
					state = QU;
					break;
				}
				sourceTypes[pos++] = STRING;
				sourceTypes[pos++] = STRING;
				state = QU;
				break;
			case CBJ1:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					sourceTypes[pos++] = UNDEFINED;
					sourceTypes[pos++] = UNDEFINED;
					break;
				}
				if (ch == '*') {
					state = CBJ2;
					break;
				}
				if (ch == '\n' || ch == '\r') {
					sourceTypes[pos++] = COMMENT_BLOCK;
					sourceTypes[pos++] = COMMENT_BLOCK;
					sourceTypes[pos++] = SWITCHLINE;
					state = CB;
					break;
				}
				if (ch == ' ' || ch == '\t') {
					sourceTypes[pos++] = COMMENT_BLOCK;
					sourceTypes[pos++] = COMMENT_BLOCK;
					sourceTypes[pos++] = BACKGROUND;
					state = CB;
					break;
				}
				sourceTypes[pos++] = COMMENT_BLOCK;
				sourceTypes[pos++] = COMMENT_BLOCK;
				sourceTypes[pos++] = COMMENT_BLOCK;
				state = CB;
				break;
			case CBJ2:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					sourceTypes[pos++] = UNDEFINED;
					sourceTypes[pos++] = UNDEFINED;
					sourceTypes[pos++] = UNDEFINED;
					break;
				}
				if (ch == '/') {
					state = COD;
					sourceTypes[pos++] = COMMENT_BLOCK;
					sourceTypes[pos++] = COMMENT_BLOCK;
					sourceTypes[pos++] = COMMENT_BLOCK;
					sourceTypes[pos++] = COMMENT_BLOCK;
					break;
				}
				if (ch == '\n' || ch == '\r') {
					sourceTypes[pos++] = JAVADOC;
					sourceTypes[pos++] = JAVADOC;
					sourceTypes[pos++] = JAVADOC;
					sourceTypes[pos++] = SWITCHLINE;
					state = CJ;
					break;
				}
				if (ch == ' ' || ch == '\t') {
					sourceTypes[pos++] = JAVADOC;
					sourceTypes[pos++] = JAVADOC;
					sourceTypes[pos++] = JAVADOC;
					sourceTypes[pos++] = BACKGROUND;
					state = CJ;
					break;
				}
				sourceTypes[pos++] = JAVADOC;
				sourceTypes[pos++] = JAVADOC;
				sourceTypes[pos++] = JAVADOC;
				sourceTypes[pos++] = JAVADOC;
				state = CJ;
				break;
			case CH1:
				if (ch == EOT) {
					sourceTypes[pos++] = CODE;
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '\'') {
					sourceTypes[pos++] = CHAR_CONSTANT;
					sourceTypes[pos++] = CHAR_CONSTANT;
					state = COD;
					break;
				}
				if (ch == '\\') {
					state = CH3;
					break;
				}
				state = CH2;
				break;
			case CH2:
				if (ch == EOT) {
					sourceTypes[pos++] = CODE;
					sourceTypes[pos++] = CODE;
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '\'') {
					sourceTypes[pos++] = CHAR_CONSTANT;
					sourceTypes[pos++] = CHAR_CONSTANT;
					sourceTypes[pos++] = CHAR_CONSTANT;
					state = COD;
					break;
				}
				sourceTypes[pos++] = UNDEFINED;
				sourceTypes[pos++] = UNDEFINED;
				sourceTypes[pos++] = UNDEFINED;
				state = COD;
				break;
			case CH3:
				if (ch == EOT) {
					sourceTypes[pos++] = CODE;
					sourceTypes[pos++] = CODE;
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == 'u') {
					sourceTypes[pos++] = CHAR_CONSTANT;
					sourceTypes[pos++] = CHAR_CONSTANT;
					sourceTypes[pos++] = CHAR_CONSTANT;
					state = CH5;
					break;
				}
				if (ch >= '1' && ch <= '9') {
					sourceTypes[pos++] = CHAR_CONSTANT;
					sourceTypes[pos++] = CHAR_CONSTANT;
					sourceTypes[pos++] = CHAR_CONSTANT;
					state = CH6;
					break;
				}
				state = CH4;
				break;
			case CH4:
				if (ch == EOT) {
					sourceTypes[pos++] = CODE;
					sourceTypes[pos++] = CODE;
					sourceTypes[pos++] = CODE;
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '\'') {
					sourceTypes[pos++] = CHAR_CONSTANT;
					sourceTypes[pos++] = CHAR_CONSTANT;
					sourceTypes[pos++] = CHAR_CONSTANT;
					sourceTypes[pos++] = CHAR_CONSTANT;
					state = COD;
					break;
				}
				sourceTypes[pos++] = CODE;
				sourceTypes[pos++] = CODE;
				sourceTypes[pos++] = CODE;
				sourceTypes[pos++] = CODE;
				state = COD;
				break;
			case CH5:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '\'') {
					sourceTypes[pos++] = CHAR_CONSTANT;
					state = COD;
					break;
				}
				if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f')
						|| (ch >= 'A' && ch <= 'F')) {
					sourceTypes[pos++] = CHAR_CONSTANT;
					break;
				}
				sourceTypes[pos++] = UNDEFINED;
				state = COD;
				break;
			case CH6:
				if (ch == EOT) {
					state = PARSESTATE_FINISHED;
					break;
				}
				if (ch == '\'') {
					sourceTypes[pos++] = CHAR_CONSTANT;
					state = COD;
					break;
				}
				if (ch >= '0' && ch <= '9') {
					sourceTypes[pos++] = CHAR_CONSTANT;
					break;
				}
				sourceTypes[pos++] = UNDEFINED;
				state = COD;
				break;
			}
		}
	}

	// 测试源程序各部分类型

	// int count = 1;//测试输出带行数

	/**
	 * 源代码根据每个字符的类型添加html的颜色标签,放到输出缓冲区中
	 * 
	 * @param source
	 */
	private void setSourceColor(String source, int fileType) {
		// sb.setLength(0); //输入缓冲区清空
		int len = source.length(); // 输入字符串的长度
		for (int i = 0; i < len; i++) {
			if (sourceTypes[i] == BACKGROUND) {
				if (source.charAt(i) == '\t') {
					sourceTypes[i] = TAB_KEY; // 标记TAB键
				} else if (source.charAt(i) == ' ') {
					sourceTypes[i] = SPACE;
				} else {
					sourceTypes[i] = CODE;
				}
			}
		}

		/*
		 * int tot = 0; System.out.print(count+":");count++; for(int x :
		 * sourceTypes){ System.out.print(x+" "); tot++; }
		 * System.out.println("tot:"+tot);
		 */

		int start = 0;
		int end = 0;
		addLineNumber();// 添加第一行标号
		while (end < sourceTypes.length - 1) {
			while (end < (sourceTypes.length - 1)
					&& sourceTypes[end + 1] == sourceTypes[start])
				// 一段相同类型的字符一起加html的标签
				++end;
			// System.out.print(end+" ");
			// System.out.print("sourceType:"+sourceTypes[start]);
			switch (sourceTypes[start]) { // 判断字符类型并加html标签后放入输出缓冲区

			case TAB_KEY:
				for (int i = 0; i < end - start + 1; i++) {
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;"); // TAB键及四个空格
				}
				break;
			case LT:
				sb.append("&lt;");
				break;
			case RT:
				sb.append("&gt;");
				break;
			case CHAR_CONSTANT:
			case NUM_CONSTANT:
			case STRING:
			case JAVADOC:
			case COMMENT_LINE:
			case PARENTHESIS:
			case CODE_TYPE:
			case COMMENT_BLOCK:
				// sb.append("<font color=\"");
				// sb.append(color.getColor(sourceTypes[start]) +
				// "\">");//根据字符类型添加html字体颜色标签
				sb = addColor(sb, sourceTypes[start]);// 根据字符类型添加html字体颜色标签
				sb.append(source.substring(start, end + 1));
				sb.append("</font>");
				break;
			case CODE:
				kwchange.setcolor(color);
				sb.append(kwchange.changeCode(source.substring(start, end + 1),
						fileType));// 查找关键字并加标签
				break;
			case JAVADOC_KEYWORD:
				break;
			case JAVADOC_HTML_TAG:
				break;
			case JAVADOC_LINKS:
				break;
			case UNDEFINED:
				break;
			case ANNOTATION:
				break;
			case SWITCHLINE:
				for (int i = 0; i < (end - start + 1) / 2; i++) {
					sb.append("<br />");// html换行
					sb.append("\r\n");// 换行，转换后方便查看文本
					addLineNumber();// 添加行号
				}
				break;
			case SPACE:
				for (int i = 0; i < end - start + 1; i++) {
					sb.append("&nbsp;");// 添加空格
				}
				break;
			}
			start = end + 1;
			end = start;
		}
	}

	/**
	 * 检查文件的编码
	 * 
	 * @param filename
	 */
	private void checkCode(String filename) {
		code = "UTF-8"; // 文件编码默认为UTF-8
		String s; // 用于读入一行字符串的临时变量
		StringBuffer csb = new StringBuffer();
		int l = 0;
		int pos = 0;
		try {
			is = new FileInputStream(filename);
			ir = new InputStreamReader(is, code);
			br = new BufferedReader(ir);
			while ((s = br.readLine()) != null) {
				csb.append(s);
			}
			l = csb.length();
			while (pos < l) {
				int k = csb.charAt(pos++);
				if (k == 65533)
					code = "GBK";
			}
		} catch (FileNotFoundException e) {// 文件不存在出错
			e.printStackTrace();
		} catch (IOException e) {// 文件输入输出出错
			e.printStackTrace();
		} finally {
			FileIO.close(is);
			FileIO.close(br);// 关闭文件

		}
	}

	/**
	 * 输出时添加行号
	 */
	private void addLineNumber() {
		int t = cnt;
		int k = 0; // 数字的长度
		while (t > 0) {
			k++;
			t /= 10;
		}
		sb.append("<font color=\"");
		sb.append(color.getColor(LINE_NUMBERS) + "\">");
		for (int i = 0; i < 4 - k; i++)
			// 行号右对齐,左边空格补上
			sb.append("&nbsp;");
		sb.append(cnt++ + "</font>" + "&nbsp;");// 每次添加行号后,行号加一
	}

	public StringBuffer addColor(StringBuffer buff, int sourceType) {
		buff.append("<font color=\"");
		buff.append(color.getColor(sourceType) + "\">");// 根据字符类型添加html字体颜色标签
		return buff;
	}

	/**
	 * 判断是否为小括号,中括号,大括号
	 * 
	 * @param ch
	 * @return
	 */
	private final static boolean isParenthesis(char ch) {
		return ch == '{' || ch == '}' || ch == '[' || ch == ']' || ch == '('
				|| ch == ')';
	}

}