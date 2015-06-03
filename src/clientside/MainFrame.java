package clientside;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.peer.LightweightPeer;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import serverside.entity.LibraianInfo;
import serverside.entity.ReaderInfo;

/**
 * 主应用程序框架
 */
@SuppressWarnings("all")
public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -4823593259341103932L;
	private static final Desktop desktop = Desktop.getDesktop();
	public JTabbedPane tabbedPane; // 多标签底板
	protected BookRetrievalPanel bookQueryPanel; // 图书查询面板
	private Library lib;
	// 主菜单条
	protected JMenuBar menuBar;
	// 馆藏检索(书目检索和我的借阅)
	protected JMenu libMenu;
	protected JMenuItem libMenuItem,myBorrowMenuItem;
	// 借书、还书
	protected JMenu lrMenu;
	// 系统维护
	protected JMenu sysMaintainMenu;
	protected JMenuItem bookMenuItem, readerMenuItem, librarianMenuItem,
	paraMenuItem;
	//帮助
	protected JMenu helpMenu ;
	protected JMenuItem personalCenter;
		
	// 逻辑操作
	private LibClient libClient;
	// 图书维护
	protected  BookMaintainPanel bookMaintainPanel;
	// 读者维护
	protected ReaderMaintainPanel readerMaintainPanel;
	//管理员维护
	MasterManagePanel masterManagePanel;
	//系统参数维护
	ParaMaintainPanel paraMaintainPanel;
	//个人信息
	protected ReaderInfoPanel readerInfoMain;
	//借书面板
	protected BorrowBookPanel borrowBookPanel;
	//还书面板
	protected GiveBackBookPanel giveBackBook;
	//我的借阅面板
	protected  MyBorrowPanel myBorrowPanel;
	// 登陆对象的实例
	private Object obj;

	/**
	 * 构造方法
	 */
	public MainFrame(Library lib, LibClient libClient, Object obj) {
		this.setTitle("欢迎使用图书管理系统 ");
		this.lib = lib;
		this.libClient = libClient;
		this.obj = obj;
		if (obj instanceof LibraianInfo) {
			LibraianInfo libInfo = (LibraianInfo) obj;
			bookMaintainPanel = new BookMaintainPanel(this, libClient);
			readerMaintainPanel = new ReaderMaintainPanel(this, libClient);
			borrowBookPanel = new BorrowBookPanel(this, libClient);
			giveBackBook = new GiveBackBookPanel(this, libClient);
			masterManagePanel = new MasterManagePanel(this,libClient,((LibraianInfo) obj).getLibraianid());
			paraMaintainPanel = new ParaMaintainPanel(this, libClient, ((LibraianInfo) obj).getLibraianid());
			//personalInfoPanel = new PersonalInfoPanel(this,libClient,obj);
		} else if (obj instanceof ReaderInfo) {
			ReaderInfo readerInfo = (ReaderInfo) obj;
			myBorrowPanel = new MyBorrowPanel(this, libClient, readerInfo);
			readerInfoMain = new ReaderInfoPanel(this,libClient,readerInfo);
		}		
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();
		bookQueryPanel = new BookRetrievalPanel(this);
		tabbedPane.addTab("书目检索", bookQueryPanel);
		container.add(BorderLayout.CENTER, tabbedPane);

		// 建立菜单
		menuBar = new JMenuBar();
		buildMainMenu();
		this.setJMenuBar(menuBar);

		// 将窗口位置放在屏幕中央
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(900, 700);
		Dimension framesize = this.getSize();
		int x = (int) screensize.getWidth() / 2 - (int) framesize.getWidth()
				/ 2;
		int y = (int) screensize.getHeight() / 2 - (int) framesize.getHeight()
				/ 2;
		setLocation(x, y);
		this.addWindowListener(new WindowCloser());
	}

	
	protected void buildMainMenu() {
		/**
		 * 1. 文件菜单
		 */
		JMenu fileMenu = new JMenu("文件(F)");
		fileMenu.setMnemonic(KeyEvent.VK_F);// 给文件菜单定义助记键

		// b.注销登陆
		JMenuItem releaseSourse = new JMenuItem("注销登陆");

		releaseSourse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
		releaseSourse.addActionListener(this);

		fileMenu.add(releaseSourse);

		/**
		 * c.退出系统
		 */
		JMenuItem exitMenuItem = new JMenuItem("退出系统");

		// 设置修改键，它能直接调用菜单项的操作侦听器而不必显示菜单的层次结构。
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));// 设定快捷键
		exitMenuItem.addActionListener(new ExitActionListener());
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		// 2.馆藏检索菜单
		setupBookRetrievalMenu();
		// 借书和还书
		setupLendReturnMenu();
		// 系统维护
		setupMaintainMenu();
		// 3.读友家园菜单
		//setupReaderHomeMenu();
		/**
		 * 4.外观选项菜单
		 */
		setupLookAndFeelMenu();

		/**
		 * 5.帮助菜单
		 */
		setupHelpMenu();
	}

	/**
	 * 2.建立图书信息查询菜单
	 * 
	 */
	protected void setupBookRetrievalMenu() {

		/**
		 * 馆藏检索
		 */
		libMenu = new JMenu("馆藏检索(B)");
		libMenu.setMnemonic(KeyEvent.VK_B);// 给图书检索菜单定义助记键

		libMenuItem = new JMenuItem("书目检索");
		myBorrowMenuItem = new JMenuItem("我的借阅");

		libMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.CTRL_MASK));// 设定快捷键
		libMenuItem.addActionListener(new PanelListener());

		myBorrowMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				ActionEvent.CTRL_MASK));// 设定快捷键
		myBorrowMenuItem.addActionListener(new PanelListener());
		libMenu.add(libMenuItem);
		menuBar.add(libMenu);
	}

	/**
	 * 建立借书还书菜单
	 * 
	 */
	protected void setupLendReturnMenu() {

		lrMenu = new JMenu("借书还书(E)");
		lrMenu.setEnabled(false);
		lrMenu.setMnemonic(KeyEvent.VK_E);// 给借书还书菜单定义助记键

		JMenuItem lendMenuItem = new JMenuItem("借书管理");
		lendMenuItem.setActionCommand("borrow");
		lendMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.CTRL_MASK));// 设定快捷键
		lendMenuItem.addActionListener(new PanelListener());
		JMenuItem returnMenuItem = new JMenuItem("还书管理");
		returnMenuItem.setActionCommand("return");		
		returnMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				ActionEvent.CTRL_MASK));// 设定快捷键
		returnMenuItem.addActionListener(new PanelListener());
//		JMenuItem renewMenuItem  = new JMenuItem("续借管理");
//		renewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
//				ActionEvent.CTRL_MASK));// 设定快捷键
		lrMenu.add(lendMenuItem);
		lrMenu.addSeparator();
		lrMenu.add(returnMenuItem);
//		lrMenu.addSeparator();
//		lrMenu.add(renewMenuItem);
		menuBar.add(lrMenu);
	}

	/**
	 * 建立系统维护菜单
	 * 
	 */
	protected void setupMaintainMenu() {

		sysMaintainMenu = new JMenu("系统维护(M)");
		sysMaintainMenu.setEnabled(false);
		sysMaintainMenu.setMnemonic(KeyEvent.VK_M);// 给借书还书菜单定义助记键

		bookMenuItem = new JMenuItem("图书维护");
		bookMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
				ActionEvent.CTRL_MASK));
		readerMenuItem = new JMenuItem("读者维护");
		readerMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK));
		librarianMenuItem = new JMenuItem("管理员维护");
		librarianMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));
		paraMenuItem = new JMenuItem("系统参数维护");
		paraMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				ActionEvent.CTRL_MASK));
		bookMenuItem.addActionListener(new BookMaintainListener());
		readerMenuItem.addActionListener(new BookMaintainListener());
		librarianMenuItem.addActionListener(new BookMaintainListener());
		paraMenuItem.addActionListener(new BookMaintainListener());
		menuBar.add(sysMaintainMenu);
	}

//	/**
//	 * 3.建立读友家园菜单
//	 */
//	private void setupReaderHomeMenu() {
//		JMenu readerMenu = new JMenu("读友家园(R)");
//		readerMenu.setMnemonic(KeyEvent.VK_R);
//		/**
//		 * 读者投诉和建议
//		 */
//		JMenuItem suggestItem = new JMenuItem("意见建议");
//		suggestItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
//				ActionEvent.CTRL_MASK));
//		suggestItem.addActionListener(new TestListener());
//		readerMenu.add(suggestItem);
//		/**
//		 * 读者交流和讨论
//		 */
//		JMenuItem connectionItem = new JMenuItem("书友交流");
//		connectionItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
//				ActionEvent.CTRL_MASK));
//		connectionItem.addActionListener(new TestListener());
//		readerMenu.add(connectionItem);
//		/**
//		 * 读者上传资料共享
//		 */
//		JMenuItem fileupItem = new JMenuItem("资料共享");
//		fileupItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
//				ActionEvent.CTRL_MASK));
//		fileupItem.addActionListener(new TestListener());
//		readerMenu.add(fileupItem);
//		/**
//		 * 读者向馆长反映图书馆的情况以及提出建议
//		 */
//		JMenuItem mailItem = new JMenuItem("馆长信箱");
//		mailItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
//				ActionEvent.CTRL_MASK));
//		mailItem.addActionListener(new TestListener());
//		readerMenu.add(mailItem);
//		menuBar.add(readerMenu);
//	}

	/**
	 * 4.建立外观选项菜单
	 * 
	 */
	private void setupLookAndFeelMenu() {
		/**
		 * UIManager 管理外观 指定外观 方法一：指定外观类的完全限定名称
		 * UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		 * ，方法二：创建 LookAndFeel 的实例并将它传递给 setLookAndFeel
		 * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		 * 
		 * UIManager.getInstalledLookAndFeels() 返回表示当前可用的 LookAndFeel 实现的
		 * LookAndFeelInfo 数组
		 */
		UIManager.LookAndFeelInfo[] lookAndFeelInfo = UIManager
				.getInstalledLookAndFeels();
		JMenu lookAndFeelMenu = new JMenu("外观(V)");
		lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);// 给借书还书菜单定义助记键
		JMenuItem anItem = null;
		LookAndFeelListener myListener = new LookAndFeelListener();

		try {
			for (int i = 0; i < lookAndFeelInfo.length; i++) {
				anItem = new JMenuItem(lookAndFeelInfo[i].getName() + " 外观");
				// 设置此按钮的动作命令,以便在后面的监听器事件中取得当前外观的全限定名
				anItem.setActionCommand(lookAndFeelInfo[i].getClassName());
				anItem.addActionListener(myListener);

				lookAndFeelMenu.add(anItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		menuBar.add(lookAndFeelMenu);
	}

	/**
	 * 帮助菜单
	 */

	private void setupHelpMenu() {
		helpMenu = new JMenu("帮助(H)");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		//JMenuItem welcome = new JMenuItem("欢迎界面");
		JMenuItem aboutMenuItem = new JMenuItem("关于系统");
		//aboutMenuItem.setMnemonic(KeyEvent.VK_A);
		aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));		
		aboutMenuItem.addActionListener(new AboutActionListener());
		personalCenter = new JMenuItem("个人中心");
		//personalCenter.setMnemonic(KeyEvent.VK_P);
		personalCenter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.CTRL_MASK));
		personalCenter.addActionListener(new PanelListener());
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
	}

	/**
	 * 退出系统
	 */
	public void exit() {
		setVisible(false);
		if (lib.isVisible()) {
			lib.setVisible(false);
		}
		this.dispose();
		lib.dispose();
		System.exit(0);

	}

	// 注销用户
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int n = JOptionPane.showConfirmDialog(null, "确认注销当前用户吗？", "确认对话框",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			// 确认注销
			lib.setVisible(true);
			this.setVisible(false);
		} else if (n == JOptionPane.NO_OPTION) {
			return;
		}
	}

	/**
	 * "退出"事件处理内部类.
	 */
	class ExitActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			int n = JOptionPane.showConfirmDialog(null, "确认退出系统吗？", "确认对话框",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.NO_OPTION) {
				return;
			} else {
				exit();
			}
		}
	}

	/**
	 * "关闭窗口"事件处理内部类.
	 */
	class WindowCloser extends WindowAdapter {
		// 调用我们自己在上面定义的exit()方法
		@Override
		public void windowClosing(WindowEvent e) {
			exit();
		}
	}

	/**
	 * "外观"选择监听类
	 * 
	 */
	class LookAndFeelListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			// 取得当前外观在事件监听器中的全限定名（动作命令）
			String className = event.getActionCommand();
			try {
				UIManager.setLookAndFeel(className);
				// 简单的外观更改
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class PanelListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if ("borrow" == e.getActionCommand()) {
				tabbedPane.add("借书管理", borrowBookPanel);
			} else if ("return" == e.getActionCommand()) {
				tabbedPane.add("还书管理", giveBackBook);
			}else if(myBorrowMenuItem==e.getSource()){
				tabbedPane.addTab("我的借阅", myBorrowPanel);
			}else if(personalCenter==e.getSource()){
				tabbedPane.add("个人信息",readerInfoMain);
			}else if(libMenuItem==e.getSource()){
				tabbedPane.add("书目检索",bookQueryPanel);
			}
		}
	}
	/**
	 * "关于帮助菜单监听类
	 */
	class AboutActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				desktop.open(new File("libraryHelp.CHM"));
			} catch (Exception ea) {
				System.out.println("打开帮助文档失败："+ea.getMessage());
			}
		}
	}

	// 借书和还书的监听器类
//	class LenRetActionListener implements ActionListener {
//		@Override
//		public void actionPerformed(ActionEvent event) {
//			if ("borrow" == event.getActionCommand()) {
//				tabbedPane.add("借书", lendPanel);
//			} else if ("return" == event.getActionCommand()) {
//				tabbedPane.add("还书", lendPanel);
//			}
//		}
//	}

	// 我的借阅
//	class MyBorrowActionListener implements ActionListener {
//		public void actionPerformed(ActionEvent event) {
//			tabbedPane.addTab("我的借阅", myBorrowPanel);
//		}
//	}

	// 馆藏信息
//	class BookInLibraryActionListener implements ActionListener {
//		public void actionPerformed(ActionEvent event) {
//			tabbedPane.addTab("书目检索", bookQueryPanel);
//		}
//	}

	class BookMaintainListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (bookMenuItem == event.getSource()) {
				tabbedPane.addTab("图书维护", bookMaintainPanel);
				//lrMenu.setEnabled(true);
			}
			if (readerMenuItem == event.getSource()) {
				tabbedPane.addTab("读者维护", readerMaintainPanel);
			}
			if (librarianMenuItem == event.getSource()) {
				tabbedPane.addTab("管理员维护", masterManagePanel);
			}
			if (paraMenuItem == event.getSource()) {			
				tabbedPane.addTab("系统参数维护", paraMaintainPanel);
			}
		}
	}
	/**
	 * 个人中心，用来修改个人信息
	 */
//	class PersonalActionListener implements ActionListener{
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			tabbedPane.add("个人信息",readerInfoMain);
//		}
//	}
	/**
	 * 处理读友家园的事件监听器
	 */
	class TestListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "没有实现");
		}
	}
}
