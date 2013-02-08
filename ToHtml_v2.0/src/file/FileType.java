package file;

/**
 * 判别文件格式
 * 
 * @author Miku
 * 
 */
public class FileType {
	private int pos;
	private int type;
	private String filetype;

	private final static int CLAN = 1;// C语言文件格式
	private final static int CPLUS = 2;// C++文件格式
	private final static int JAVA = 3;// java文件格式

	/**
	 * 获得文件格式
	 * 
	 * @param filename
	 * @return
	 */
	public int getFileType(String filename) {
		// filename = "f://temp//test//test.java";
		pos = filename.lastIndexOf('.');// 最后一个'.'及其后的字符串
		filetype = filename.substring(pos + 1, filename.length());// 通过文件名判断文件类型
		if (filetype.equals("c") || filetype.equals("c")) {
			type = CLAN;
		}
		if (filetype.equals("c++") || filetype.equals("cpp")) {
			type = CPLUS;
		}
		if (filetype.equals("JAVA") || filetype.equals("java")) {
			type = JAVA;
		}
		if (filetype.equals("JAVA") || filetype.equals("h")) {
			type = CLAN;
		}
		return type;
	}

	public boolean isCorrectFileType(int type) {
		if (type == 1 || type == 2 || type == 3)
			return true;
		else
			return false;
	}
}
