package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class NewFile {
	public static File file = new File("c://temp//test");
	public static File TxtFile = new File("c://temp//test//test001.txt");

	public static void mkDir() {
		System.out.println("新建文件夹!");
		if (file.isDirectory()) {
			System.out.println("该文件夹已存在！");
		} else {
			file.mkdir();
			System.out.println("成功创建一个文件夹！");
		}
	}

	public static void newFile() throws IOException {
		System.out.println("\n\n新建文件!");
		if (!TxtFile.exists()) {
			if (TxtFile.createNewFile()) {
				System.out.println("成功创建一个新文件");
			} else {
				System.out.println("创建新文件失败");
			}
		} else {
			System.out.println("该文件已经存在");
		}
	}

	public static void wrFile() {
		System.out.println("\n\n写入文件!");
		try {
			FileWriter fw = new FileWriter(TxtFile);
			String s = wrString();
			System.out.println(s);
			fw.write(s);
			System.out.println("写入成功");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String wrString() {
		String s = "";
		for (int i = 1; i <= 100; i++) {
			s = s + i;
			if (i % 10 == 0) {
				s = s + "\r\n";
			} else {
				s = s + "\t";
			}
		}
		return s;
	}

	private static void reFile() {
		System.out.println("\n\n读取文件!");
		try {
			FileReader fr = new FileReader(TxtFile);
			BufferedReader br = new BufferedReader(fr);
			br.mark((int) TxtFile.length() + 1);// 标记当前位置
			int i = 0;
			while (br.read() != -1) {
				i++;
			}
			int line = 0;
			br.reset();// 复位到最近的标记位
			String sr = null;
			while ((sr = br.readLine()) != null)
			{
				System.out.println(sr);
				/* 具体操作 */
				line++;
			}
			System.out.println("读取文件成功,字数一共为" + TxtFile.length() + ",i=" + i
					+ ",line=" + line);
			br.reset();// 复位到最近的标记位
			char data[] = new char[(int) TxtFile.length()];
			i = br.read(data);
			System.out.println("i = br.read(data)统计的字数为" + i);
			String s = new String(data, 0, i);
			System.out.println(s);
			System.out.println("读取文件成功,字数一共为" + i);
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		mkDir();// 新建文件夹
		newFile();// 新建文件
		
		wrFile();// 写入文件
		reFile();// 读取文件
	}
}
