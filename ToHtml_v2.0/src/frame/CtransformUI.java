package frame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import color.Color;

import file.FileIO;
import file.Read;


class CtransformUI extends JPanel implements ActionListener {
	private JTextArea jtf1, jtf2;                                  //两个文字填写区域
	private JScrollPane csrollPane1, csrollPane2;                  //存放jtf1, jtf2
	private JLabel jlabel1, jlabel2;                               //提示文字
	private JButton button, button1, button2;                      //三个功能按钮（转换代码，清空，输出文件夹）
	private String outPut;                                         //输出文件夹路径
	private String[] choice = { "请选择语言","C 语言", "C++", "Java" }; //转换语言类型
	private JComboBox choiceList;                                  //转换语言选择框
	private int index = -1;                                        //代码类型
	private boolean flag = false;              
	
	/**  文件处理   */
	private FileOutputStream out = null;         
	private OutputStreamWriter ow;
	private BufferedWriter bo;
    
	private StringBuffer buffer = new StringBuffer();

	public CtransformUI() {
		
		this.setLayout(null);
		/*
		 * 控件设置
		 */
		csrollPane1 = new JScrollPane(jtf1 = new JTextArea());
		csrollPane1.setBounds(20, 30, 320, 340);
		csrollPane2 = new JScrollPane(jtf2 = new JTextArea());
		csrollPane2.setBounds(350, 30, 320, 340);
		
		choiceList = new JComboBox(choice);
//		choiceList.setBorder(BorderFactory.createTitledBorder("请选择语言"));
		choiceList.setSelectedIndex(0);
		choiceList.setBounds(20,380,100,25);
		button = new JButton("转换成HTML代码");
		button.setBounds(130,380,120,25);
		
		button1 = new JButton("输出到文件夹");
		button1.setBounds(550,380,120,25);
		
		button2 = new JButton("清空翻译结果");
		button2.setBounds(420,380,120,25);
		
		jlabel1 = new JLabel("请输入要转换成HTML代码的内容:");
		jlabel2 = new JLabel("翻译结果:");
		jlabel1.setBounds(22, 5, 200, 20);
		jlabel2.setBounds(353, 5, 200, 20);

		
		this.add(jlabel1);
		this.add(jlabel2);
		this.add(csrollPane1);
		this.add(choiceList);
		this.add(button);
		this.add(button1);
		this.add(button2);
		this.add(csrollPane2);
		jtf2.setEditable(false);
		this.setBounds(0, 0, 700, 500);
		choiceList.addActionListener(this);
		
		button.addActionListener(this);                //转换成HTML代码按钮监听器
		
		button1.addActionListener(new ActionListener() {         //输出到文件夹按钮监听器
			
			public void actionPerformed(ActionEvent e) {

				if(outPut != null){            //输出路径不为空时执行
					try {
						String outputPath = selectedPath();
						outputPath += ".html";
						out = new FileOutputStream(outputPath);
						ow = new OutputStreamWriter(out);
						bo = new BufferedWriter(ow);
						ow.write(outPut);                   //写入文件
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	
					FileIO.close(bo);               //关闭文件
				}
				else
					JOptionPane.showMessageDialog(null, "翻译结果为空！","操作错误",JOptionPane.WARNING_MESSAGE);	           //提示错误，没有结果输出
					
			}
		});
		
		button2.addActionListener(new ActionListener() {         //清空文字区域按钮监听器
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jtf2.setText("");
				jtf2.repaint();				
			}
		});
	}

	/*
	 * 转换成HTML代码
	 */
	public void actionPerformed(ActionEvent e) {
		try {

			if (e.getSource() == button) {
				
				String temp = new String();
				buffer = new StringBuffer();
				
				if (flag) {
					
					String typeString = jtf1.getText();
					Scanner token = new Scanner(typeString);
					
					String outString = new String();     //html文本body内容
					
					Read read = new Read();				
					Color co = new Color();				
									
					int type = index + 1;
					if (type >= 0) {
						while (token.hasNext()) {
							temp = token.nextLine().toString();
							buffer.append(temp);
							buffer.append("\r\n");
						}

					//	System.out.println(buffer.toString() + type + co);
						
						outString = read.readText(buffer.toString(), type, co);
												
						buffer.setLength(0);					//缓冲区清除
						String textName = "TmpText"; 			// 临时文本名称
						buffer.append(read.htmlHead(textName));	//添加html文件头
						buffer.append(outString);				//添加html的body内容
						buffer.append(read.bottom);				//添加html的文件尾
						
						
						
						outPut = buffer.toString();				//缓存
						
						jtf2.setText(outPut);					//文本框内容										
						jtf2.repaint();                         //更新文本框控件
						choiceList.repaint();
						choiceList.updateUI();			
						

					}

				} else {
					JOptionPane.showMessageDialog(null, "请选择一门语言");           //提示没有选择要转换的语言
				}
			}
			if (e.getSource() == choiceList) {
				
				index = choiceList.getSelectedIndex()-1;
				if(index<0){
					JOptionPane.showMessageDialog(null, "请选择一门语言");           //提示没有选择要转换的语言
					flag = false;
				}
				else
					flag = true;
				

			}
		} catch (Exception ex) {
			System.out.print("Error" + ex);
		}
	}
	
	String selectedPath() {
		JFileChooser fc = new JFileChooser();
	//	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只能选择目录
		
		String saveType[] = {"html"};                
		String defaultFileName = "请输入文件名";            
		
		fc.setFileFilter(new FileNameExtensionFilter(".html", saveType));        //只显示html文件
		fc.setSelectedFile(new File(defaultFileName)); 
		
		String path = null;
		File f = null;
		int flag;
		flag = fc.showDialog(fc, "保存文件");
		if (flag == JFileChooser.APPROVE_OPTION) {
		// 获得该文件
			//System.out.println(outPut);
			
			f = fc.getSelectedFile();        
			path = f.getPath();              //保存路径
		}
		return path;
		}

}