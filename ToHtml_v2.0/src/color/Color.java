package color;

/**
 * 设置字体颜色
 * 
 * @author Miku
 * 
 */
public class Color {
	private String[] col = new String[30];

	
	public Color() {
		setColor(0, "#000000");// CODE
		setColor(1, "#2E8B57");// COMMENT_LINE
		setColor(2, "#00C957");// JAVADOC
		setColor(3, "#2E8B57");// COMMENT_BLOCK
		setColor(4, "#808A87");// STRING
		setColor(5, "#FF6100");// CHAR_CONSTANT
		setColor(6, "#C0C0C0");// LINE_NUMBERS
		setColor(7, "#FF6100");// PARENTHESIS
		setColor(8, "#FF6100");// NUM_CONSTANT
		setColor(9, "#6A5ACD");// CODE_TYPE
		setColor(10, "#000000");// TAB_KEY
		setColor(11, "#4169E1");// JAVADOC_KEYWORD
		setColor(12, "#4169E1");// JAVADOC_HTML_TAG
		setColor(13, "#4169E1");// JAVADOC_LINKS
		setColor(14, "#6A5ACD");// SWITCHLINE
		setColor(15, "#FF6100");// UNDEFINED
		setColor(16, "#708069");// ANNOTATION
		setColor(17, "#000000");// BACKGROUND
		setColor(18, "#2E8B57");// SPACE
		setColor(19, "#872657");// KEYWORD
	}

	/**
	 * 设置源代码中不同字符类型的颜色
	 * 
	 * @param type
	 * @param col
	 */
	public void setColor(int type, String col) {
		this.col[type] = col;
		
//		System.out.println(type);
//		System.out.println(this.col[type]);
	}

	/**
	 * 获取源代码中不同字符类型的颜色
	 * 
	 * @param type
	 * @return
	 */
	public String getColor(int type) {
		return col[type];
	}
	/**
	 * 16位颜色转换RGB
	 * @param type
	 * @return
	 */
	public int[] ggetColor(int type) {
		
		String[] rbg = new String[3];
		String s = col[type];
		int[] r = new int[3];
		for(int i=0,j=1; i<3; i++,j++){
			rbg[i] = s.substring(i+j, i+j+2);
	//		System.out.println(rbg[i]);
			
			r[i] = 0;

			if(rbg[i].charAt(0)>64)
				r[i] += (rbg[i].charAt(0) - 55)*16;
			else if(rbg[i].charAt(0)<64)
				r[i] += (rbg[i].charAt(0) - '0')*16;
			if(rbg[i].charAt(1)>64)
				r[i] += rbg[i].charAt(1) - 55;
			else if(rbg[i].charAt(0)<64)
				r[i] += rbg[i].charAt(1) - '0';
			
	//		System.out.println(r[i]);
		}
		return r;
	}

}
