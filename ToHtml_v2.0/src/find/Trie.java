package find;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;


import color.Color;

public class Trie {

	Color color = new Color();
	private String passage; // 用于操作后的字符串
	
	public void setcolor(Color co){
		this.color = co;
	}


	public String changeCode(String file, int type) {
		//System.out.println("input: "+file +"end");
		if(file.compareTo(";") == 0){
			       StringBuffer buffer1 = new StringBuffer(20);
			       //buffer1.append(";");
				    buffer1.append("<font color=\"");
					buffer1.append(color.getColor(0) + "\">");
					buffer1.append(";");
					buffer1.append("</font>");
					passage = buffer1.toString();
					 //System.out.println(passage);
					return passage;
			
		}
		StringBuffer temp3 = new StringBuffer(file);
		if (type == 1 || type == 2) {
			if (file.indexOf('#') == 0) {
				temp3.setLength(0);
				temp3.append("<font color=\"");
				temp3.append(color.getColor(9) + "\">");
				temp3.append(file);
				temp3.append("</font>");
				passage = temp3.toString();
				return passage;
			}

		}

		File file1 = new File("c.dat");
		File file2 = new File("c+.dat");
		File file3 = new File("java.dat");
		if (!file1.exists() || !file2.exists() || !file3.exists()) {
			new Create();
		}
		TrieTree root = new TrieTree();
		TrieTree rootKey = new TrieTree();
		// 创建关键字和保留字的字典树的树根
		addWord(type, root, rootKey);

		
		// 分词
		Scanner scanner = new Scanner(file);
		StringBuffer buffer = new StringBuffer(200);
        
		// 关键字匹配,以空格符为区隔
		while (scanner.hasNext()) {
			boolean add = false;
			String temp = scanner.next();
			// System.out.println(temp);
			String temp2 = temp.toString();
			
			//正则表达式：断句的符号
			String[] splitStr = null;
			String regEx = ";";
			 Pattern p =Pattern.compile(regEx);     
			 Matcher m = p.matcher(temp2);     
            //按照“；”分割字符串
			splitStr = p.split(temp2);
			//System.out.println("The Length:" + splitStr.length);
			
			//将分号连接到相应的句子后面
		//if(splitStr.length > 0)     
		  //    {     
			//	   int count = 0;     
				//    while(count < splitStr.length)     
				  //  {     
				    //    if(m.find())     
				      //  {     
				        //    splitStr[count] += m.group();     
				       // }     
				       // count++;     
				  //  }     
			//	}     

			for(int k=0;k<splitStr.length;k++){
				// System.out.println(splitStr[k]);

				 temp = splitStr[k];
				 if (find(root, temp)) {				//关键字加颜色标签
						buffer.append("<font color=\"");
						buffer.append(color.getColor(19) + "\">");
						buffer.append(temp);
						buffer.append("</font>");
					} else if (find(rootKey, temp)) {	//标示符加颜色标签
						// do something here;
						buffer.append("<font color=\"");
						buffer.append(color.getColor(9) + "\">");
						buffer.append(temp);
						buffer.append("</font>");
					} else if (isNumeric(temp)){	//数字加颜色标签
						buffer.append("<font color=\"");
						buffer.append(color.getColor(8) + "\">");
						buffer.append(temp);
						buffer.append("</font>");
					}else{							//其他加颜色标签
						buffer.append("<font color=\"");
						buffer.append(color.getColor(0) + "\">");
						buffer.append(temp);
						buffer.append("</font>");
					}
				 
				 if(m.find())     
			        {     
			            //splitStr[k] += m.group();
			            buffer.append("<font color=\"");
						buffer.append(color.getColor(0) + "\">");
						buffer.append(m.group());
						buffer.append("</font>");
			        }     
     	
			}

			if (scanner.hasNext())
				buffer.append("&nbsp;");
			
		}
		
		passage = buffer.toString();
		 //System.out.println(passage);
		return passage;
		// test

	}

	/*
	 * public static void main(String[] args) { String testWord = new String(
	 * "public static void main(String[] args){ double in = 0; doub Public}");
	 * Trie test = new Trie(); test.changeCode(testWord, 'j'); }
	 */
	class TrieTree { // 内部类（节点类）

		TrieTree[] child;
		char ch;
		boolean hasNext = false;
		boolean finished = false;

		public TrieTree() {
			child = new TrieTree[52];
		}
	}

	public void addWord(int type, TrieTree root, TrieTree rootKey) {
		try {

			ObjectInputStream input = new ObjectInputStream(
					new FileInputStream("c.dat"));
			// = new ObjectInputStream();
			ArrayList<String> keyWord;
			ArrayList<String> keyWord2;
			switch (type) {// 通过文件的类型，读取该语言的关键字文本
			case 1:
				input = new ObjectInputStream(new FileInputStream("c.dat"));
				break;
			case 2:
				input = new ObjectInputStream(new FileInputStream("c+.dat"));
				break;
			case 3:
				input = new ObjectInputStream(new FileInputStream("java.dat"));
				break;
			default:
				//System.out.print("Error");
				System.exit(0);
			}

			keyWord = (ArrayList<String>) (input.readObject());
			keyWord2 = (ArrayList<String>) (input.readObject());
			input.close();
			for (int i = 0; i < keyWord.size(); i++) {
				insert(root, keyWord.get(i));
			}
			for (int i = 0; i < keyWord2.size(); i++) {
				insert(rootKey, keyWord2.get(i));
			}

		} catch (IOException ex) {
			//System.out.print("Error " + ex);
			System.exit(0);
		} catch (ClassNotFoundException ex) {
			//System.out.print("Error" + ex);
			System.exit(0);
		}

	}

	public void insert(TrieTree root, String word) {
		TrieTree cur = root;
		for (char ch : word.toCharArray()) {
			int index = ch - 'a';
			if (index >= 0 && index < 52) {
				if (cur.child[index] == null) {
					cur.child[index] = new TrieTree();
				}
				cur = cur.child[index];
				cur.ch = ch;
			}

		}

		cur.finished = true;

	}

	public boolean find(TrieTree root, String word) {
		TrieTree cur = root;
		for (char ch : word.toCharArray()) {
			int index = ch - 'a';
			// if(index >= 0 && index <52){
			if (index < 0 || index >= 52 || cur.child[index] == null) {
				return false;
			}
			cur = cur.child[index];
		}
		if (cur.finished == true) {
			return true;
		}

		return false;

	}

	// 得到经操作后的字符串
	public String getString() {
		return passage;
	}
	
	/**
	 * 通过正则表达式匹配字符串，判断是否为数字
	 * @param str
	 * @return
	 * @author Miku
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("^[0-9].*");
		return pattern.matcher(str).matches();
	}
}
