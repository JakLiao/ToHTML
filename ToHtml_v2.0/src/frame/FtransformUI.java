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
	
	private JPanel p1,p2;                                                //p1是内容版，p2是输出文件夹文字提示
	private JButton p1Add, p1Delete, p1Start, p1Set, p1File;             //操作按钮
	private FileTree filetree;                                           //树控件框架
	private JLabel jlabel;                                               //输出文件夹提示
	private colorDialog codialog;                                        //颜色控件
	private String outputpath = null;                                    //输出路径
	private color.Color co = new color.Color();                          //颜色参数

	/**  图标设置   */
	private ImageIcon addIcon = new ImageIcon("src/icos/add.png");                 //添加图标
	private ImageIcon deleteIcon = new ImageIcon("src/icos/delete.png");           //清空图标
	private ImageIcon fileIcon = new ImageIcon("src/icos/fileopen.png");           //输出文件夹图标	
	private ImageIcon setIcon = new ImageIcon("src/icos/set.png");                 //颜色设置图标
	private ImageIcon startIcon = new ImageIcon("src/icos/start.png");             //开始图标
		

                             
	/*
	 * 获取颜色
	 */
	public color.Color getcolor(){
		return co;
	}

	public FtransformUI(JFrame MainFrame) {
		
		/*
		 * 工具栏
		 */		
		final Read t = new Read();                //读取文件
		
		p1 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,6));          //设置p1布局，大小，位置
		p1.setBounds(30,5,700,50);
		
		filetree = new FileTree();                       //新建树控件框架
		filetree.setLayout(null);
		filetree.setBounds(10, 55, 700, 350);
				
		/**  添加按钮  */
		p1Add = new JButton("添加", addIcon);
		p1Delete = new JButton("清空列表", deleteIcon);
		p1Start = new JButton("开始", startIcon);
		p1File = new JButton("输出文件夹", fileIcon);
		p1Set = new JButton("颜色设置", setIcon);
		
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
		 * 按钮监听
		 * */
		
		/**  添加  */		
		p1Add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				TreePath [] files = new TreePath[200];               //文件路径
				files = filetree.gettree().getSelectionPaths();      //从树节点获取文件路径

				
				for(int j = 0;j<files.length; j++){                  //多个节点添加
					
					String s;                                            //字符串读取缓存
					DefaultMutableTreeNode childNode = new  DefaultMutableTreeNode();          //子节点temp
					FileTreeNode k;
					
					k = (FileTreeNode)files[j].getLastPathComponent();	    //读取子节点
					
					if(k.isLeaf()){               //判断节点是否为文件，把文件名添加到文件队列
						s = k.getFileObject().toString();				 
	            		childNode.setUserObject(s);          		 
	            		filetree.getfroot().add(childNode); 
	            		filetree.files[filetree.i] = s;
	            		filetree.i++;             //队列计数器
					}
				}				 	          		 		            		 
				filetree.getftreeModel().reload();     //刷新树控件
			}
		});
		
		/**  清空  */
		DeleteListener Dlistener = new DeleteListener();		
		p1Delete.addActionListener(Dlistener);	//删除列表
	
		
		/**  输出文件夹    */		
		p1File.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				outputpath = selectedPath();             //调用文件夹控件并返回文件输出路径
				t.getfn().setFilePath(outputpath);
				jlabel.setText("当前输出文件夹：" + outputpath);
//				System.out.println(outputpath);
			}
		});
		
		
		/**  颜色设置  */		
		p1Set.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				codialog = new colorDialog();			//调用颜色选取控件
			}
		});
		

		/**  转换文件   */
		p1Start.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e){
				
				if(filetree.i==0)             //判断是否有需要转换的文件
				{      
					JOptionPane.showMessageDialog(null, "没有需要转换文件！","操作错误",JOptionPane.WARNING_MESSAGE);					//转换文件为空时提示
				}
				else{
					if(outputpath == null)     //判断是否设置了输出路径
					{
						JOptionPane.showMessageDialog(null, "请设置输出文件夹！","温馨提示",JOptionPane.INFORMATION_MESSAGE);	       //没有设置输出路径时提示
						outputpath = selectedPath();                 //并让用户立刻设置输出路径
						t.getfn().setFilePath(outputpath);           
						jlabel.setText("当前输出文件夹：" + outputpath);    //更新显示路径
					}
					else{	   
						
						/**    正式转换文件        */
						String filename;            //文件名temp
						FileType ft;                //文件类型temp
						int fileType;               //文件类型temp
						
						for(int j=0; j<filetree.i; j++)           //批处理
						{
							filename = filetree.files[j];
							ft = new FileType();
							fileType = ft.getFileType(filename);// 识别文件类型
							
							if (ft.isCorrectFileType(fileType)) {      // 如果是java、C、C++文件
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
						JOptionPane.showMessageDialog(null, "转换已完成！","温馨提示",JOptionPane.INFORMATION_MESSAGE);	        //转换成功提示！
						
						/** 转换完成后清空列表，以便程序继续使用。 */
						filetree.getfroot().removeAllChildren();           
						filetree.i = 0;
						filetree.ftreeModel.reload();
					}
				}
			}
		});
		

	    /*
		 * 输出文件夹显示
		 */		
		jlabel = new JLabel("当前输出文件夹：无");
		p2 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,6));
		p2.setBounds(10, 400, 400, 30);
		p2.add(jlabel);
		
		/**   添加控件    */
		this.add(p1);
		this.add(filetree);	
		this.add(p2);
		this.setLayout(null);
		this.setBounds(0, 0, 700, 500);
		
	}	
	
	/*
	 * 清空列表监听器
	 */
	public class DeleteListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e){		
			if(filetree.i == 0)         //判断列表是否已为空
				JOptionPane.showMessageDialog(null, "列表已空！","温馨提示",JOptionPane.INFORMATION_MESSAGE);	
			filetree.getfroot().removeAllChildren();
			filetree.i = 0;
			filetree.ftreeModel.reload();
		}
	}	
	
	/*
	 * 颜色设置版块
	 */
	public class colorDialog extends JDialog {
		
		/** 设置颜色界面 */
		JPanel p3 = new JPanel(new GridLayout(8,3,10,5));
		JPanel p4 = new JPanel();
		JPanel[] a = new JPanel[8];
		JButton[] jb = new JButton[8];
		JButton   ok = new JButton("确定");
		SBlistener[] sb = new SBlistener[8];		
		int[] s = new int[3];
		
		/** 添加按钮并布局  */
		colorDialog(){			
			for(int i=0; i<8; i++){					
				a[i] = new JPanel();
				s = co.ggetColor(i);
				a[i].setBackground(new Color(s[0],s[1],s[2]));
				jb[i] = new JButton("改变");			
				sb[i] = new SBlistener(this,i);
				jb[i].addActionListener(sb[i]);
			}
					
			p3.add(new JLabel("    代码颜色"));
			p3.add(a[0]);
			p3.add(jb[0]);
			
			p3.add(new JLabel("    //注释"));
			p3.add(a[1]);
			p3.add(jb[1]);
			
			p3.add(new JLabel("   双星号注释"));
			p3.add(a[2]);
			p3.add(jb[2]);
			
			p3.add(new JLabel("   单星号注释"));
			p3.add(a[3]);
			p3.add(jb[3]);
						
			p3.add(new JLabel("     字符串"));
			p3.add(a[4]);
			p3.add(jb[4]);
			
			p3.add(new JLabel("  ''单引号字符"));
			p3.add(a[5]);
			p3.add(jb[5]);
			
			p3.add(new JLabel("      行号"));
			p3.add(a[6]);
			p3.add(jb[6]);
			
			p3.add(new JLabel("      括号"));
			p3.add(a[7]);
			p3.add(jb[7]);
		
			
			ok.addActionListener(new ActionListener() {				
				public void actionPerformed(ActionEvent e) {
					codialog.setVisible(false);
				}
			});
			p4.add(ok);
			
			this.setTitle("设置颜色");
			this.setBounds(400,200,300,300);
			this.setLayout(new FlowLayout(FlowLayout.CENTER,0,10));
			this.add(p3);
			this.add(p4);
			this.setResizable(false);
			this.setVisible(true);
			
		}
	}
	
	/*
	 * 颜色设置控件监听器
	 */
	public class SBlistener implements ActionListener {
		JDialog j;    //创建窗口
		int i;        //记录转换颜色类型
		
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
		
		/** 记录要转换颜色的代码类型  */
		SBlistener(JDialog jf, int i){         
			this.j = jf;
			this.i = i;
		}
		
		/**  转换字符串   */
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
	 * 输出文件夹窗口，并返回输出路径
	 */	
	String selectedPath() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只能选择目录
		String path = null;
		File f = null;
		int flag;
		flag = fc.showDialog(fc, "确定");
		if (flag == JFileChooser.APPROVE_OPTION) {
		// 获得该文件
		f = fc.getSelectedFile();
		path = f.getPath();
		return path;
		}else
			return null;

		}
}




