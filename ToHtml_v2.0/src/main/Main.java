package main;

import java.io.FileNotFoundException;
import java.io.IOException;

import file.FileType;
import file.Read;

/**
 * ������
 * 
 * @author Miku
 * 
 *
public class Main {

	public static void main(String[] args) {
		//String filename = "f://temp//test//test1.c";
		for(int i = 0;i < 19;i++){
			String filename = "f://temp//test//test"+i+".java";
			Read t = new Read();
			FileType ft = new FileType();
			int fileType = ft.getFileType(filename);// ʶ���ļ�����
			if (ft.isCorrectFileType(fileType)) { // �����java��C��C++�ļ�
				try {
					t.readfile(filename,fileType);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Wanning: Wrong File Type!");
			}
		}
	}
}*/

