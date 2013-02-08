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
	private JTextArea jtf1, jtf2;                                  //����������д����
	private JScrollPane csrollPane1, csrollPane2;                  //���jtf1, jtf2
	private JLabel jlabel1, jlabel2;                               //��ʾ����
	private JButton button, button1, button2;                      //�������ܰ�ť��ת�����룬��գ�����ļ��У�
	private String outPut;                                         //����ļ���·��
	private String[] choice = { "��ѡ������","C ����", "C++", "Java" }; //ת����������
	private JComboBox choiceList;                                  //ת������ѡ���
	private int index = -1;                                        //��������
	private boolean flag = false;              
	
	/**  �ļ�����   */
	private FileOutputStream out = null;         
	private OutputStreamWriter ow;
	private BufferedWriter bo;
    
	private StringBuffer buffer = new StringBuffer();

	public CtransformUI() {
		
		this.setLayout(null);
		/*
		 * �ؼ�����
		 */
		csrollPane1 = new JScrollPane(jtf1 = new JTextArea());
		csrollPane1.setBounds(20, 30, 320, 340);
		csrollPane2 = new JScrollPane(jtf2 = new JTextArea());
		csrollPane2.setBounds(350, 30, 320, 340);
		
		choiceList = new JComboBox(choice);
//		choiceList.setBorder(BorderFactory.createTitledBorder("��ѡ������"));
		choiceList.setSelectedIndex(0);
		choiceList.setBounds(20,380,100,25);
		button = new JButton("ת����HTML����");
		button.setBounds(130,380,120,25);
		
		button1 = new JButton("������ļ���");
		button1.setBounds(550,380,120,25);
		
		button2 = new JButton("��շ�����");
		button2.setBounds(420,380,120,25);
		
		jlabel1 = new JLabel("������Ҫת����HTML���������:");
		jlabel2 = new JLabel("������:");
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
		
		button.addActionListener(this);                //ת����HTML���밴ť������
		
		button1.addActionListener(new ActionListener() {         //������ļ��а�ť������
			
			public void actionPerformed(ActionEvent e) {

				if(outPut != null){            //���·����Ϊ��ʱִ��
					try {
						String outputPath = selectedPath();
						outputPath += ".html";
						out = new FileOutputStream(outputPath);
						ow = new OutputStreamWriter(out);
						bo = new BufferedWriter(ow);
						ow.write(outPut);                   //д���ļ�
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	
					FileIO.close(bo);               //�ر��ļ�
				}
				else
					JOptionPane.showMessageDialog(null, "������Ϊ�գ�","��������",JOptionPane.WARNING_MESSAGE);	           //��ʾ����û�н�����
					
			}
		});
		
		button2.addActionListener(new ActionListener() {         //�����������ť������
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jtf2.setText("");
				jtf2.repaint();				
			}
		});
	}

	/*
	 * ת����HTML����
	 */
	public void actionPerformed(ActionEvent e) {
		try {

			if (e.getSource() == button) {
				
				String temp = new String();
				buffer = new StringBuffer();
				
				if (flag) {
					
					String typeString = jtf1.getText();
					Scanner token = new Scanner(typeString);
					
					String outString = new String();     //html�ı�body����
					
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
												
						buffer.setLength(0);					//���������
						String textName = "TmpText"; 			// ��ʱ�ı�����
						buffer.append(read.htmlHead(textName));	//���html�ļ�ͷ
						buffer.append(outString);				//���html��body����
						buffer.append(read.bottom);				//���html���ļ�β
						
						
						
						outPut = buffer.toString();				//����
						
						jtf2.setText(outPut);					//�ı�������										
						jtf2.repaint();                         //�����ı���ؼ�
						choiceList.repaint();
						choiceList.updateUI();			
						

					}

				} else {
					JOptionPane.showMessageDialog(null, "��ѡ��һ������");           //��ʾû��ѡ��Ҫת��������
				}
			}
			if (e.getSource() == choiceList) {
				
				index = choiceList.getSelectedIndex()-1;
				if(index<0){
					JOptionPane.showMessageDialog(null, "��ѡ��һ������");           //��ʾû��ѡ��Ҫת��������
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
	//	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// ֻ��ѡ��Ŀ¼
		
		String saveType[] = {"html"};                
		String defaultFileName = "�������ļ���";            
		
		fc.setFileFilter(new FileNameExtensionFilter(".html", saveType));        //ֻ��ʾhtml�ļ�
		fc.setSelectedFile(new File(defaultFileName)); 
		
		String path = null;
		File f = null;
		int flag;
		flag = fc.showDialog(fc, "�����ļ�");
		if (flag == JFileChooser.APPROVE_OPTION) {
		// ��ø��ļ�
			//System.out.println(outPut);
			
			f = fc.getSelectedFile();        
			path = f.getPath();              //����·��
		}
		return path;
		}

}