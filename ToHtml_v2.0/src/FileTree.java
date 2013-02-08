

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;




public class FileTree extends JPanel{
	JPanel p1, p2;                                        //p1Ϊѡ���ļ���p2Ϊ��ת���ļ��б�	
	private JTree tree;                                   //ϵͳ�ļ���
	private JTree ftree;                                  //��ת���ļ���
	private DefaultMutableTreeNode root, froot;           //���ڵ�
	private FileTreeNode node;
	DefaultTreeModel ftreeModel;
	
	String [] files = new String[200];
	
	public int i=0;
	
	public JTree gettree(){
		return this.tree;
	}
	public DefaultMutableTreeNode getfroot(){
		return this.froot;
	}
	
	public void setfroot(DefaultMutableTreeNode froot){
		this.froot = froot;
	}
	
	public DefaultTreeModel getftreeModel(){
		return this.ftreeModel;
	}
	
	public FileTree() {
		
		tree = new JTree();	
		root = new DefaultMutableTreeNode("�����");	               //�½�ϵͳ�ļ���Ŀ¼
		froot = new DefaultMutableTreeNode("�ļ�");	               //�½���ת���ļ���Ŀ¼	
		ftreeModel = new DefaultTreeModel(froot);			
		ftree = new JTree(ftreeModel);	

		initTree();            //ϵͳ�ļ��Ķ�ȡ
		
		/**      p1��������         */
		p1 = new JPanel(new BorderLayout());
		p1.add(new  JScrollPane(tree));
		p1.setSize(300, 320);
		p1.setLocation(20, 10);
		p1.setBorder(new TitledBorder("��ѡ���ļ���"));
		
		/**      p2��������         */
		p2 = new JPanel(new BorderLayout());
		p2.setSize(300, 320);
		p2.setLocation(350, 10);
		p2.add(new JScrollPane(ftree));
		p2.setBorder(new TitledBorder("��Ҫת���ļ�"));
		
		this.add(p1);
		this.add(p2);
		
	}
	
	/*
	 * ɨ��ϵͳ�ļ�������ʾ�����ؼ�
	 */
	void initTree() {
					
			File[] roots = File.listRoots();                //ϵͳ�ļ�����
			
			/**   ��ӽ�㵽���ؼ�      */
	 		for (int i=0; i<roots.length; i++){				
				String dir = roots[i].toString();
				node = (FileTreeNode) buildTree(dir);
				root.add(node);
			}
					
			
			DefaultTreeModel treeModel = new DefaultTreeModel(root);			
			tree = new JTree(treeModel);						
			MyTreeExpansionListener listener = new MyTreeExpansionListener(tree, root, treeModel, this);	
			tree.addTreeExpansionListener(listener);	      //������ڵ�չ��������
			
			tree.addMouseListener(new MouseAdapter() {			//�����������
				public void mouseClicked(MouseEvent e) {
					
					JTree tree = (JTree) e.getSource();					
		            int selRow = tree.getRowForLocation(e.getX(), e.getY());		       
		            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());   // ֻȡ�ӽڵ�·��

		            
		            if(selPath != null) {                      //���ӽڵ㲻Ϊ��ʱ
		            	
		            	TreeNode node = (TreeNode) selPath.getLastPathComponent();
			            if(node.isLeaf())                     //�ж��Ƿ�Ϊ�ӽڵ�
	                	{
			            	if(e.getClickCount()==2){          //˫��������ӵ���ת������
			            	
					             FileTreeNode k=(FileTreeNode)selPath.getLastPathComponent(); 
			            		 files[i] = k.getFileObject().toString();                      //����Ҫת�����ļ�·���洢��files��
			            	//	 System.out.println(files[i]);
			            		 
			            		 DefaultMutableTreeNode childNode  =   new  DefaultMutableTreeNode();
			            		 childNode.setUserObject(files[i]);
			            		 froot.add(childNode); 		            		 		      //����Ҫת�����ļ�ͬʱ��ӵ��ļ����ؼ��������û��鿴      		 
			            		 ftreeModel.reload();                                         //ˢ�����ؼ�
			            	//	 System.out.println(i);
			            		 i++;                            //�ļ�����
			            	}			            	
			            }
		            }
				}
			});

		}
		
	/*
	 *   ��·��ת�������ڵ�	
	 */
	public TreeNode buildTree(String dir) {
			FileTreeNode root = new FileTreeNode(new File(dir));
			root.readTree(false);
			return (TreeNode)root;
		}
		
	/*
	 * ���ؼ��ļ�չ��������
	 */
	public class MyTreeExpansionListener implements TreeExpansionListener {
			

			DefaultMutableTreeNode root;
			JTree jtree;
			DefaultTreeModel treeModel;
			JPanel jp;
			
			public MyTreeExpansionListener(JTree tree, DefaultMutableTreeNode node, DefaultTreeModel tmodel, JPanel thejp) {
				root = node;
				jtree = tree;
				treeModel = tmodel;
				jp = thejp;
			}


			public void treeExpanded(TreeExpansionEvent event) {
				
				TreePath path = event.getPath();
				FileTreeNode node = (FileTreeNode)path.getLastPathComponent();
				jp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				jp.setEnabled(false);
				
				if(node.readTree()) {
					
					int childrenIdx[] = new int[node.getChildCount()];
					int i = 0;
					for(Enumeration e = node.children(); e.hasMoreElements();) {    
						Object obj = e.nextElement();
						childrenIdx[i] = node.getIndex((TreeNode)obj);
						i ++;
					}
					
					treeModel.nodesWereInserted(node, childrenIdx);
				}
				
				jp.setEnabled(true);
				jp.setCursor(Cursor.getDefaultCursor());
				
			}


			public void treeCollapsed(TreeExpansionEvent event) {
				
			}
		}

	
}