package file;

/**
 * ʶ���ļ���
 * 
 * @author Miku
 * 
 */
public class FileName {
	private int pos;
	private String fileName;
	private String filePath;

	/**
	 * ����ļ���
	 * 
	 * @param fileDir
	 * @return
	 */
	public String getFileName(String fileDir) {
		pos = fileDir.lastIndexOf('\\');// ���һ��'/'�������ַ���
		fileName = fileDir.substring(pos + 1, fileDir.length());// ͨ���ļ����ж��ļ�����
		return fileName;
	}

	/**
	 * �ı�Դ�ļ�·��
	 * 
	 * @param fileDir
	 * @return
	 */
	public void setFilePath(String fileDir) {
		/*
		 * if ((fileDir.lastIndexOf('/')) != fileDir.length()) { fileDir =
		 * fileDir + "//"; }
		 */
		if (fileDir != null) {
			if ((fileDir.lastIndexOf('\\')) != fileDir.length()) {
				fileDir = fileDir + "\\";
			}
		}
		filePath = fileDir;
	}

	/**
	 * ��ȡԴ�ļ�·��
	 * 
	 * @param fileDir
	 * @return
	 */
	public String getFilePath() {
		return this.filePath;
	}
}
