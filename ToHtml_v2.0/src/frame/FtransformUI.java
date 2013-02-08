package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sql.rowset.JdbcRowSet;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


import file.FileType;
import file.Read;


public class FtransformUI extends JPanel {
	
	private JPanel p1,p2;                                                //p1�����ݰ棬p2������ļ���������ʾ
	private JButton p1Add, p1Delete, p1Start, p1Set, p1File;             //������ť
	private FileTree filetree;                                           //���ؼ����
	private JLabel jlabel;                                               //����ļ�����ʾ
	private colorDialog codialog;                                        //��ɫ�ؼ�
	private String outputpath = null;                                    //���·��
	private color.Color co = new color.Color();                          //��ɫ����

	/**  ͼ������   */
	private ImageIcon addIcon = new ImageIcon("src/icos/add.png");                 //���ͼ��
	private ImageIcon deleteIcon = new ImageIcon("src/icos/delete.png");           //���ͼ��
	private ImageIcon fileIcon = new ImageIcon("src/icos/fileopen.png");           //����ļ���ͼ��	
	private ImageIcon setIcon = new ImageIcon("src/icos/set.png");                 //��ɫ����ͼ��
	private ImageIcon startIcon = new ImageIcon("src/icos/start.png");             //��ʼͼ��
		

                             
	/*
	 * ��ȡ��ɫ
	 */
	public color.Color getcolor(){
		return co;
	}

	public FtransformUI(JFrame MainFrame) {
		
		/*
		 * ������
		 */		
		final Read t = new Read();                //��ȡ�ļ�
		
		p1 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,6));          //����p1���֣���С��λ��
		p1.setBounds(30,5,700,50);
		
		filetree = new FileTree();                       //�½����ؼ����
		filetree.setLayout(null);
		filetree.setBounds(10, 55, 700, 350);
				
		/**  ��Ӱ�ť  */
		p1Add = new JButton("���", addIcon);
		p1Delete = new JButton("����б�", deleteIcon);
		p1Start = new JButton("��ʼ", startIcon);
		p1File = new JButton("����ļ���", fileIcon);
		p1Set = new JButton("��ɫ����", setIcon);
		
		p1Add.setBorderPainted(false);
		p1Delete.setBorderPainted(false);
		p1Start.setBorderPainted(false);
		p1File.setBorderPainted(false);
		p1Set.setBorderPainted(false);

		p1Add.setBackground(null);
		p1Delete.setBackground(null);
		p1Start.setBackground(null);
		p1File.setBackground(null);
		p1Set.setBackground(null);
			
		p1.add(p1Add);
		p1.add(p1Delete);
		p1.add(p1File);
		p1.add(p1Set);
		p1.add(p1Start);
		
		/*
		 * ��ť����
		 * */
		
		/**  ���  */		
		p1Add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				TreePath [] files = new TreePath[200];               //�ļ�·��
				files = filetree.gettree().getSelectionPaths();      //�����ڵ��ȡ�ļ�·��

				
				for(int j = 0;j<files.length; j++){                  //����ڵ����
					
					String s;                                            //�ַ�����ȡ����
					DefaultMutableTreeNode childNode = new  DefaultMutableTreeNode();          //�ӽڵ�temp
					FileTreeNode k;
					
					k = (FileTreeNode)files[j].getLastPathComponent();	    //��ȡ�ӽڵ�
					
					if(k.isLeaf()){               //�жϽڵ��Ƿ�Ϊ�ļ������ļ�����ӵ��ļ�����
						s = k.getFileObject().toString();				 
	            		childNode.setUserObject(s);          		 
	            		filetree.getfroot().add(childNode); 
	            		filetree.files[filetree.i] = s;
	            		filetree.i++;             //���м�����
					}
				}				 	          		 		            		 
				filetree.getftreeModel().reload();     //ˢ�����ؼ�
			}
		});
		
		/**  ���  */
		DeleteListener Dlistener = new DeleteListener();		
		p1Delete.addActionListener(Dlistener);	//ɾ���б�
	
		
		/**  ����ļ���    */		
		p1File.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				outputpath = selectedPath();             //�����ļ��пؼ��������ļ����·��
				t.getfn().setFilePath(outputpath);
				jlabel.setText("��ǰ����ļ��У�" + outputpath);
//				System.out.println(outputpath);
			}
		});
		
		
		/**  ��ɫ����  */		
		p1Set.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				codialog = new colorDialog();			//������ɫѡȡ�ؼ�
			}
		});
		

		/**  ת���ļ�   */
		p1Start.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e){
				
				if(filetree.i==0)             //�ж��Ƿ�����Ҫת�����ļ�
				{      
					JOptionPane.showMessageDialog(null, "û����Ҫת���ļ���","��������",JOptionPane.WARNING_MESSAGE);					//ת���ļ�Ϊ��ʱ��ʾ
				}
				else{
					if(outputpath == null)     //�ж��Ƿ����������·��
					{
						JOptionPane.showMessageDialog(null, "����������ļ��У�","��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);	       //û���������·��ʱ��ʾ
						outputpath = selectedPath();                 //�����û������������·��
						t.getfn().setFilePath(outputpath);           
						jlabel.setText("��ǰ����ļ��У�" + outputpath);    //������ʾ·��
					}
					else{	   
						
						/**    ��ʽת���ļ�        */
						String filename;            //�ļ���temp
						FileType ft;                //�ļ�����temp
						int fileType;               //�ļ�����temp
						
						for(int j=0; j<filetree.i; j++)           //������
						{
							filename = filetree.files[j];
							ft = new FileType();
							fileType = ft.getFileType(filename);// ʶ���ļ�����
							
							if (ft.isCorrectFileType(fileType)) {      // �����java��C��C++�ļ�
								try {
									t.readfile(filename, fileType, co);
								} catch (FileNotFoundException e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							} else {
								System.out.println("Wanning: Wrong File Type!");
							}
						}
						JOptionPane.showMessageDialog(null, "ת������ɣ�","��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);	        //ת���ɹ���ʾ��
						
						/** ת����ɺ�����б��Ա�������ʹ�á� */
						filetree.getfroot().removeAllChildren();           
						filetree.i = 0;
						filetree.ftreeModel.reload();
					}
				}
			}
		});
		

	    /*
		 * ����ļ�����ʾ
		 */		
		jlabel = new JLabel("��ǰ����ļ��У���");
		p2 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,6));
		p2.setBounds(10, 400, 400, 30);
		p2.add(jlabel);
		
		/**   ��ӿؼ�    */
		this.add(p1);
		this.add(filetree);	
		this.add(p2);
		this.setLayout(null);
		this.setBounds(0, 0, 700, 500);
		
	}	
	
	/*
	 * ����б������
	 */
	public class DeleteListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e){		
			if(filetree.i == 0)         //�ж��б��Ƿ���Ϊ��
				JOptionPane.showMessageDialog(null, "�б��ѿգ�","��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);	
			filetree.getfroot().removeAllChildren();
			filetree.i = 0;
			filetree.ftreeModel.reload();
		}
	}	
	
	/*
	 * ��ɫ���ð��
	 */
	public class colorDialog extends JDialog {
		
		/** ������ɫ���� */
		JPanel p3 = new JPanel(new GridLayout(8,3,10,5));
		JPanel p4 = new JPanel();
		JPanel[] a = new JPanel[8];
		JButton[] jb = new JButton[8];
		JButton   ok = new JButton("ȷ��");
		SBlistener[] sb = new SBlistener[8];		
		int[] s = new int[3];
		
		/** ��Ӱ�ť������  */
		colorDialog(){			
			for(int i=0; i<8; i++){					
				a[i] = new JPanel();
				s = co.ggetColor(i);
				a[i].setBackground(new Color(s[0],s[1],s[2]));
				jb[i] = new JButton("�ı�");			
				sb[i] = new SBlistener(this,i);
				jb[i].addActionListener(sb[i]);
			}
					
			p3.add(new JLabel("    ������ɫ"));
			p3.add(a[0]);
			p3.add(jb[0]);
			
			p3.add(new JLabel("    //ע��"));
			p3.add(a[1]);
			p3.add(jb[1]);
			
			p3.add(new JLabel("   ˫�Ǻ�ע��"));
			p3.add(a[2]);
			p3.add(jb[2]);
			
			p3.add(new JLabel("   ���Ǻ�ע��"));
			p3.add(a[3]);
			p3.add(jb[3]);
						
			p3.add(new JLabel("     �ַ���"));
			p3.add(a[4]);
			p3.add(jb[4]);
			
			p3.add(new JLabel("  ''�������ַ�"));
			p3.add(a[5]);
			p3.add(jb[5]);
			
			p3.add(new JLabel("      �к�"));
			p3.add(a[6]);
			p3.add(jb[6]);
			
			p3.add(new JLabel("      ����"));
			p3.add(a[7]);
			p3.add(jb[7]);
		
			
			ok.addActionListener(new ActionListener() {				
				public void actionPerformed(ActionEvent e) {
					codialog.setVisible(false);
				}
			});
			p4.add(ok);
			
			this.setTitle("������ɫ");
			this.setBounds(400,200,300,300);
			this.setLayout(new FlowLayout(FlowLayout.CENTER,0,10));
			this.add(p3);
			this.add(p4);
			this.setResizable(false);
			this.setVisible(true);
			
		}
	}
	
	/*
	 * ��ɫ���ÿؼ�������
	 */
	public class SBlistener implements ActionListener {
		JDialog j;    //��������
		int i;        //��¼ת����ɫ����
		
		public void actionPerformed(ActionEvent e){
			
			JColorChooser jColorChooser1 = new JColorChooser();
			Color c = jColorChooser1.showDialog(j, "choose", Color.GRAY);
			if(c != null){
				int r,g,b;
				r = c.getRed();
				g = c.getGreen();
				b = c.getBlue();
				co.setColor(i, RGB(r,g,b));				
				codialog.a[i].setBackground(new Color(r,g,b));
			}
	//		System.out.println(RGB(r,g,b));			
		}
		
		/** ��¼Ҫת����ɫ�Ĵ�������  */
		SBlistener(JDialog jf, int i){         
			this.j = jf;
			this.i = i;
		}
		
		/**  ת���ַ���   */
		String RGB(int r,int g,int b)
		{
			StringBuffer str = new StringBuffer();
			str.append("#");
			str.append(charRGB(r));	
			str.append(charRGB(g));	
			str.append(charRGB(b));
//System.out.println(str.toString());
			return str.toString();
		}
		
		char[] charRGB(int r){
			
			char[] s = new char[2];	
			
			s[0] = (char) (r/16 + 48);
			s[1] = (char) (r%16 + 48);
			
			if(r/16>9) 	s[0] = (char) (r/16 + 55);
			if(r/16<=9) s[0] = (char) (r%16 + 48);		
			if(r/16>9)  s[1] = (char) (r/16 + 55);
			if(r/16<=9) s[1] = (char) (r%16 + 48);		
			return s;
		}
	}
	
	
	/*
	 * ����ļ��д��ڣ����������·��
	 */	
	String selectedPath() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// ֻ��ѡ��Ŀ¼
		String path = null;
		File f = null;
		int flag;
		flag = fc.showDialog(fc, "ȷ��");
		if (flag == JFileChooser.APPROVE_OPTION) {
		// ��ø��ļ�
		f = fc.getSelectedFile();
		path = f.getPath();
		return path;
		}else
			return null;

		}
}




