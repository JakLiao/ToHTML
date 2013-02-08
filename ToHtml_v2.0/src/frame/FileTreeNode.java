package frame;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import file.FileType;

public class FileTreeNode extends DefaultMutableTreeNode {
	boolean isAdded;

	public FileTreeNode(File file) {
		super(file);
		isAdded = false;
	}

	public boolean readTree() {
		return readTree(false);
	}

	/*
	 * 扫描树节点
	 */
	public boolean readTree(boolean b) {
		if (isAdded)
			return false;

		String list[] = getFileObject().list();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				FileTreeNode subnode = new FileTreeNode(new File(
						getFileObject(), list[i]));
				if (!subnode.isLeaf())
					add(subnode);
				else {

					FileType ft = new FileType();
					int fileType = ft.getFileType(subnode.toString()); // 识别文件类型
					if (ft.isCorrectFileType(fileType))
						add(subnode);
				}
				if (b)
					subnode.readTree(b);
			}
		}

		isAdded = true;
		return true;
	}

	public File getFileObject() {
		return (File) getUserObject();
	}

	public String toString() {
		File file = getFileObject();
		return file.getName().length() > 0 ? file.getName() : file.getPath();
	}

	public boolean isLeaf() {
		return ((File) userObject).isFile();
	}
}
