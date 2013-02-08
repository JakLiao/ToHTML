

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
	JPanel p1, p2;                                        //p1为选择文件框；p2为待转换文件列表	
	private JTree tree;                                   //系统文件树
	private JTree ftree;                                  //待转换文件树
	private DefaultMutableTreeNode root, froot;           //树节点
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
		root = new DefaultMutableTreeNode("计算机");	               //新建系统文件根目录
		froot = new DefaultMutableTreeNode("文件");	               //新建待转化文件根目录	
		ftreeModel = new DefaultTreeModel(froot);			
		ftree = new JTree(ftreeModel);	

		initTree();            //系统文件的读取
		
		/**      p1窗口设置         */
		p1 = new JPanel(new BorderLayout());
		p1.add(new  JScrollPane(tree));
		p1.setSize(300, 320);
		p1.setLocation(20, 10);
		p1.setBorder(new TitledBorder("请选择文件夹"));
		
		/**      p2窗口设置         */
		p2 = new JPanel(new BorderLayout());
		p2.setSize(300, 320);
		p2.setLocation(350, 10);
		p2.add(new JScrollPane(ftree));
		p2.setBorder(new TitledBorder("需要转换文件"));
		
		this.add(p1);
		this.add(p2);
		
	}
	
	/*
	 * 扫描系统文件，并显示到树控件
	 */
	void initTree() {
					
			File[] roots = File.listRoots();                //系统文件队列
			
			/**   添加结点到树控件      */
	 		for (int i=0; i<roots.length; i++){				
				String dir = roots[i].toString();
				node = (FileTreeNode) buildTree(dir);
				root.add(node);
			}
					
			
			DefaultTreeModel treeModel = new DefaultTreeModel(root);			
			tree = new JTree(treeModel);						
			MyTreeExpansionListener listener = new MyTreeExpansionListener(tree, root, treeModel, this);	
			tree.addTreeExpansionListener(listener);	      //添加树节点展开监听器
			
			tree.addMouseListener(new MouseAdapter() {			//添加鼠标监听器
				public void mouseClicked(MouseEvent e) {
					
					JTree tree = (JTree) e.getSource();					
		            int selRow = tree.getRowForLocation(e.getX(), e.getY());		       
		            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());   // 只取子节点路径

		            
		            if(selPath != null) {                      //当子节点不为空时
		            	
		            	TreeNode node = (TreeNode) selPath.getLastPathComponent();
			            if(node.isLeaf())                     //判断是否为子节点
	                	{
			            	if(e.getClickCount()==2){          //双击处理，添加到待转换队列
			            	
					             FileTreeNode k=(FileTreeNode)selPath.getLastPathComponent(); 
			            		 files[i] = k.getFileObject().toString();                      //把需要转换的文件路径存储在files里
			            	//	 System.out.println(files[i]);
			            		 
			            		 DefaultMutableTreeNode childNode  =   new  DefaultMutableTreeNode();
			            		 childNode.setUserObject(files[i]);
			            		 froot.add(childNode); 		            		 		      //把需要转换的文件同时添加到文件树控件，方便用户查看      		 
			            		 ftreeModel.reload();                                         //刷新树控件
			            	//	 System.out.println(i);
			            		 i++;                            //文件计数
			            	}			            	
			            }
		            }
				}
			});

		}
		
	/*
	 *   把路径转换成树节点	
	 */
	public TreeNode buildTree(String dir) {
			FileTreeNode root = new FileTreeNode(new File(dir));
			root.readTree(false);
			return (TreeNode)root;
		}
		
	/*
	 * 树控件文件展开监听器
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