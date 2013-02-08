package file;

/**
 * �б��ļ���ʽ
 * 
 * @author Miku
 * 
 */
public class FileType {
	private int pos;
	private int type;
	private String filetype;

	private final static int CLAN = 1;// C�����ļ���ʽ
	private final static int CPLUS = 2;// C++�ļ���ʽ
	private final static int JAVA = 3;// java�ļ���ʽ

	/**
	 * ����ļ���ʽ
	 * 
	 * @param filename
	 * @return
	 */
	public int getFileType(String filename) {
		// filename = "f://temp//test//test.java";
		pos = filename.lastIndexOf('.');// ���һ��'.'�������ַ���
		filetype = filename.substring(pos + 1, filename.length());// ͨ���ļ����ж��ļ�����
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
