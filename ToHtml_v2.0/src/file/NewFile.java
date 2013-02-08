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
		System.out.println("�½��ļ���!");
		if (file.isDirectory()) {
			System.out.println("���ļ����Ѵ��ڣ�");
		} else {
			file.mkdir();
			System.out.println("�ɹ�����һ���ļ��У�");
		}
	}

	public static void newFile() throws IOException {
		System.out.println("\n\n�½��ļ�!");
		if (!TxtFile.exists()) {
			if (TxtFile.createNewFile()) {
				System.out.println("�ɹ�����һ�����ļ�");
			} else {
				System.out.println("�������ļ�ʧ��");
			}
		} else {
			System.out.println("���ļ��Ѿ�����");
		}
	}

	public static void wrFile() {
		System.out.println("\n\nд���ļ�!");
		try {
			FileWriter fw = new FileWriter(TxtFile);
			String s = wrString();
			System.out.println(s);
			fw.write(s);
			System.out.println("д��ɹ�");
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
		System.out.println("\n\n��ȡ�ļ�!");
		try {
			FileReader fr = new FileReader(TxtFile);
			BufferedReader br = new BufferedReader(fr);
			br.mark((int) TxtFile.length() + 1);// ��ǵ�ǰλ��
			int i = 0;
			while (br.read() != -1) {
				i++;
			}
			int line = 0;
			br.reset();// ��λ������ı��λ
			String sr = null;
			while ((sr = br.readLine()) != null)
			{
				System.out.println(sr);
				/* ������� */
				line++;
			}
			System.out.println("��ȡ�ļ��ɹ�,����һ��Ϊ" + TxtFile.length() + ",i=" + i
					+ ",line=" + line);
			br.reset();// ��λ������ı��λ
			char data[] = new char[(int) TxtFile.length()];
			i = br.read(data);
			System.out.println("i = br.read(data)ͳ�Ƶ�����Ϊ" + i);
			String s = new String(data, 0, i);
			System.out.println(s);
			System.out.println("��ȡ�ļ��ɹ�,����һ��Ϊ" + i);
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		mkDir();// �½��ļ���
		newFile();// �½��ļ�
		
		wrFile();// д���ļ�
		reFile();// ��ȡ�ļ�
	}
}
