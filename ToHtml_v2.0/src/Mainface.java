

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class Mainface  extends JFrame {
	private JMenu JMenu1, JMenu2;
	private JMenuBar JMenuBar1;
	private JMenuBar JMenuBar2;
	private JMenuItem jmb11, jmb12, jmb21, jmb22;
	private JPanel pfile, pcode;
	private JTabbedPane jtp;
	private JTextField jtf;
	private Font font;
	
	
public void lauchFrame(){
	
		/*
		 * 菜单栏
		 */	
		JMenuBar1 = new JMenuBar();
		this.setJMenuBar(JMenuBar1);
		JMenu1 = new JMenu("菜单");
		JMenu2 = new JMenu("帮助");
		JMenuBar1.add(JMenu1);
		JMenuBar1.add(JMenu2);

		jmb11 = new JMenuItem();
		jmb12 = new JMenuItem();
		jmb21 = new JMenuItem();
		jmb22 = new JMenuItem();
		
		JMenu1.add(jmb11);
		JMenu1.add(jmb12);
		JMenu2.add(jmb21);
		JMenu2.add(jmb22);
		
		jmb11.setText("转换文件");
		jmb12.setText("转换代码");
		jmb21.setText("制作团队");
		jmb22.setText("联系维护");
		
		jmb11.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				jtp.setSelectedIndex(0);
			}
		});
		
		jmb12.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				jtp.setSelectedIndex(1);
			}
		});
		
		jmb21.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				
				JDialog jd = new JDialog();
				JPanel jp = new JPanel();
								

				jp.setLayout(new FlowLayout());
				jp.setBounds(5, 5, 280, 110);
				jp.setBorder(new TitledBorder("校长队"));
				jp.add(new JLabel("成员姓名：                   负责工作："));
				jp.add(new JLabel("廖浩杰                     核心算法编写"));
				jp.add(new JLabel("邓展峰                       软件UI设计"));
				jp.add(new JLabel("林晓阳                       关键字识别"));
				
				jd.setBounds(370, 250, 300, 150);
				jd.setLayout(null);
				jd.add(jp);
				jd.setTitle("制作团队");
				jd.setResizable(false);
				jd.setVisible(true);
			}
		});
		
		jmb22.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				
				JDialog jd = new JDialog();
								
				jd.setBounds(370, 250, 300, 120);
				jd.setLayout(new FlowLayout());
				jd.add(new JLabel("*****************************"));
				jd.add(new JLabel(" 24小时维护热线：15915787826 "));
				jd.add(new JLabel("*****************************"));
				jd.add(new JLabel("我们的软件，有你的支持，会做得更好！"));
				jd.setTitle("联系维护");
				jd.setResizable(false);
				jd.setVisible(true);
			}
		});
		


		
		jtp = new JTabbedPane();
		pfile = new FtransformUI(this);
		pcode = new CtransformUI();
		jtp.add("转换文件", pfile);
		jtp.add("转换代码", pcode);
		jtp.setBounds(0, 0, 700, 500);
		

		font = new Font("微软雅黑", 0, 10);
		this.setFont(font);
		this.setLocation(200, 100);
		this.setSize(700, 500);
		this.setBackground(new Color(187, 255, 255));
		this.setTitle("源代码自动转换程序");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.add(jtp);

		
		setLayout(null);

}
	
	public static void main(String[] args) {
		Mainface to = new Mainface();
		to.lauchFrame();
	}

	/**
	 * 使得界面更像本地应用s
	 */
	static {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
