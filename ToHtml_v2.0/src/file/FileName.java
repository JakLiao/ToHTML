package file;

/**
 * 识别文件名
 * 
 * @author Miku
 * 
 */
public class FileName {
	private int pos;
	private String fileName;
	private String filePath;

	/**
	 * 获得文件名
	 * 
	 * @param fileDir
	 * @return
	 */
	public String getFileName(String fileDir) {
		pos = fileDir.lastIndexOf('\\');// 最后一个'/'及其后的字符串
		fileName = fileDir.substring(pos + 1, fileDir.length());// 通过文件名判断文件类型
		return fileName;
	}

	/**
	 * 改变源文件路径
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
	 * 获取源文件路径
	 * 
	 * @param fileDir
	 * @return
	 */
	public String getFilePath() {
		return this.filePath;
	}
}
