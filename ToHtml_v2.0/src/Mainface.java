

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
		 * �˵���
		 */	
		JMenuBar1 = new JMenuBar();
		this.setJMenuBar(JMenuBar1);
		JMenu1 = new JMenu("�˵�");
		JMenu2 = new JMenu("����");
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
		
		jmb11.setText("ת���ļ�");
		jmb12.setText("ת������");
		jmb21.setText("�����Ŷ�");
		jmb22.setText("��ϵά��");
		
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
				jp.setBorder(new TitledBorder("У����"));
				jp.add(new JLabel("��Ա������                   ��������"));
				jp.add(new JLabel("�κƽ�                     �����㷨��д"));
				jp.add(new JLabel("��չ��                       ���UI���"));
				jp.add(new JLabel("������                       �ؼ���ʶ��"));
				
				jd.setBounds(370, 250, 300, 150);
				jd.setLayout(null);
				jd.add(jp);
				jd.setTitle("�����Ŷ�");
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
				jd.add(new JLabel(" 24Сʱά�����ߣ�15915787826 "));
				jd.add(new JLabel("*****************************"));
				jd.add(new JLabel("���ǵ�����������֧�֣������ø��ã�"));
				jd.setTitle("��ϵά��");
				jd.setResizable(false);
				jd.setVisible(true);
			}
		});
		


		
		jtp = new JTabbedPane();
		pfile = new FtransformUI(this);
		pcode = new CtransformUI();
		jtp.add("ת���ļ�", pfile);
		jtp.add("ת������", pcode);
		jtp.setBounds(0, 0, 700, 500);
		

		font = new Font("΢���ź�", 0, 10);
		this.setFont(font);
		this.setLocation(200, 100);
		this.setSize(700, 500);
		this.setBackground(new Color(187, 255, 255));
		this.setTitle("Դ�����Զ�ת������");
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
	 * ʹ�ý�����񱾵�Ӧ��s
	 */
	static {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
