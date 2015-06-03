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
 * ��Ӧ�ó�����
 */
@SuppressWarnings("all")
public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -4823593259341103932L;
	private static final Desktop desktop = Desktop.getDesktop();
	public JTabbedPane tabbedPane; // ���ǩ�װ�
	protected BookRetrievalPanel bookQueryPanel; // ͼ���ѯ���
	private Library lib;
	// ���˵���
	protected JMenuBar menuBar;
	// �ݲؼ���(��Ŀ�������ҵĽ���)
	protected JMenu libMenu;
	protected JMenuItem libMenuItem,myBorrowMenuItem;
	// ���顢����
	protected JMenu lrMenu;
	// ϵͳά��
	protected JMenu sysMaintainMenu;
	protected JMenuItem bookMenuItem, readerMenuItem, librarianMenuItem,
	paraMenuItem;
	//����
	protected JMenu helpMenu ;
	protected JMenuItem personalCenter;
		
	// �߼�����
	private LibClient libClient;
	// ͼ��ά��
	protected  BookMaintainPanel bookMaintainPanel;
	// ����ά��
	protected ReaderMaintainPanel readerMaintainPanel;
	//����Աά��
	MasterManagePanel masterManagePanel;
	//ϵͳ����ά��
	ParaMaintainPanel paraMaintainPanel;
	//������Ϣ
	protected ReaderInfoPanel readerInfoMain;
	//�������
	protected BorrowBookPanel borrowBookPanel;
	//�������
	protected GiveBackBookPanel giveBackBook;
	//�ҵĽ������
	protected  MyBorrowPanel myBorrowPanel;
	// ��½�����ʵ��
	private Object obj;

	/**
	 * ���췽��
	 */
	public MainFrame(Library lib, LibClient libClient, Object obj) {
		this.setTitle("��ӭʹ��ͼ�����ϵͳ ");
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
		tabbedPane.addTab("��Ŀ����", bookQueryPanel);
		container.add(BorderLayout.CENTER, tabbedPane);

		// �����˵�
		menuBar = new JMenuBar();
		buildMainMenu();
		this.setJMenuBar(menuBar);

		// ������λ�÷�����Ļ����
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
		 * 1. �ļ��˵�
		 */
		JMenu fileMenu = new JMenu("�ļ�(F)");
		fileMenu.setMnemonic(KeyEvent.VK_F);// ���ļ��˵��������Ǽ�

		// b.ע����½
		JMenuItem releaseSourse = new JMenuItem("ע����½");

		releaseSourse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
		releaseSourse.addActionListener(this);

		fileMenu.add(releaseSourse);

		/**
		 * c.�˳�ϵͳ
		 */
		JMenuItem exitMenuItem = new JMenuItem("�˳�ϵͳ");

		// �����޸ļ�������ֱ�ӵ��ò˵���Ĳ�����������������ʾ�˵��Ĳ�νṹ��
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));// �趨��ݼ�
		exitMenuItem.addActionListener(new ExitActionListener());
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		// 2.�ݲؼ����˵�
		setupBookRetrievalMenu();
		// ����ͻ���
		setupLendReturnMenu();
		// ϵͳά��
		setupMaintainMenu();
		// 3.���Ѽ�԰�˵�
		//setupReaderHomeMenu();
		/**
		 * 4.���ѡ��˵�
		 */
		setupLookAndFeelMenu();

		/**
		 * 5.�����˵�
		 */
		setupHelpMenu();
	}

	/**
	 * 2.����ͼ����Ϣ��ѯ�˵�
	 * 
	 */
	protected void setupBookRetrievalMenu() {

		/**
		 * �ݲؼ���
		 */
		libMenu = new JMenu("�ݲؼ���(B)");
		libMenu.setMnemonic(KeyEvent.VK_B);// ��ͼ������˵��������Ǽ�

		libMenuItem = new JMenuItem("��Ŀ����");
		myBorrowMenuItem = new JMenuItem("�ҵĽ���");

		libMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.CTRL_MASK));// �趨��ݼ�
		libMenuItem.addActionListener(new PanelListener());

		myBorrowMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				ActionEvent.CTRL_MASK));// �趨��ݼ�
		myBorrowMenuItem.addActionListener(new PanelListener());
		libMenu.add(libMenuItem);
		menuBar.add(libMenu);
	}

	/**
	 * �������黹��˵�
	 * 
	 */
	protected void setupLendReturnMenu() {

		lrMenu = new JMenu("���黹��(E)");
		lrMenu.setEnabled(false);
		lrMenu.setMnemonic(KeyEvent.VK_E);// �����黹��˵��������Ǽ�

		JMenuItem lendMenuItem = new JMenuItem("�������");
		lendMenuItem.setActionCommand("borrow");
		lendMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.CTRL_MASK));// �趨��ݼ�
		lendMenuItem.addActionListener(new PanelListener());
		JMenuItem returnMenuItem = new JMenuItem("�������");
		returnMenuItem.setActionCommand("return");		
		returnMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				ActionEvent.CTRL_MASK));// �趨��ݼ�
		returnMenuItem.addActionListener(new PanelListener());
//		JMenuItem renewMenuItem  = new JMenuItem("�������");
//		renewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
//				ActionEvent.CTRL_MASK));// �趨��ݼ�
		lrMenu.add(lendMenuItem);
		lrMenu.addSeparator();
		lrMenu.add(returnMenuItem);
//		lrMenu.addSeparator();
//		lrMenu.add(renewMenuItem);
		menuBar.add(lrMenu);
	}

	/**
	 * ����ϵͳά���˵�
	 * 
	 */
	protected void setupMaintainMenu() {

		sysMaintainMenu = new JMenu("ϵͳά��(M)");
		sysMaintainMenu.setEnabled(false);
		sysMaintainMenu.setMnemonic(KeyEvent.VK_M);// �����黹��˵��������Ǽ�

		bookMenuItem = new JMenuItem("ͼ��ά��");
		bookMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
				ActionEvent.CTRL_MASK));
		readerMenuItem = new JMenuItem("����ά��");
		readerMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK));
		librarianMenuItem = new JMenuItem("����Աά��");
		librarianMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));
		paraMenuItem = new JMenuItem("ϵͳ����ά��");
		paraMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				ActionEvent.CTRL_MASK));
		bookMenuItem.addActionListener(new BookMaintainListener());
		readerMenuItem.addActionListener(new BookMaintainListener());
		librarianMenuItem.addActionListener(new BookMaintainListener());
		paraMenuItem.addActionListener(new BookMaintainListener());
		menuBar.add(sysMaintainMenu);
	}

//	/**
//	 * 3.�������Ѽ�԰�˵�
//	 */
//	private void setupReaderHomeMenu() {
//		JMenu readerMenu = new JMenu("���Ѽ�԰(R)");
//		readerMenu.setMnemonic(KeyEvent.VK_R);
//		/**
//		 * ����Ͷ�ߺͽ���
//		 */
//		JMenuItem suggestItem = new JMenuItem("�������");
//		suggestItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
//				ActionEvent.CTRL_MASK));
//		suggestItem.addActionListener(new TestListener());
//		readerMenu.add(suggestItem);
//		/**
//		 * ���߽���������
//		 */
//		JMenuItem connectionItem = new JMenuItem("���ѽ���");
//		connectionItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
//				ActionEvent.CTRL_MASK));
//		connectionItem.addActionListener(new TestListener());
//		readerMenu.add(connectionItem);
//		/**
//		 * �����ϴ����Ϲ���
//		 */
//		JMenuItem fileupItem = new JMenuItem("���Ϲ���");
//		fileupItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
//				ActionEvent.CTRL_MASK));
//		fileupItem.addActionListener(new TestListener());
//		readerMenu.add(fileupItem);
//		/**
//		 * ������ݳ���ӳͼ��ݵ�����Լ��������
//		 */
//		JMenuItem mailItem = new JMenuItem("�ݳ�����");
//		mailItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
//				ActionEvent.CTRL_MASK));
//		mailItem.addActionListener(new TestListener());
//		readerMenu.add(mailItem);
//		menuBar.add(readerMenu);
//	}

	/**
	 * 4.�������ѡ��˵�
	 * 
	 */
	private void setupLookAndFeelMenu() {
		/**
		 * UIManager ������� ָ����� ����һ��ָ����������ȫ�޶�����
		 * UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		 * �������������� LookAndFeel ��ʵ�����������ݸ� setLookAndFeel
		 * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		 * 
		 * UIManager.getInstalledLookAndFeels() ���ر�ʾ��ǰ���õ� LookAndFeel ʵ�ֵ�
		 * LookAndFeelInfo ����
		 */
		UIManager.LookAndFeelInfo[] lookAndFeelInfo = UIManager
				.getInstalledLookAndFeels();
		JMenu lookAndFeelMenu = new JMenu("���(V)");
		lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);// �����黹��˵��������Ǽ�
		JMenuItem anItem = null;
		LookAndFeelListener myListener = new LookAndFeelListener();

		try {
			for (int i = 0; i < lookAndFeelInfo.length; i++) {
				anItem = new JMenuItem(lookAndFeelInfo[i].getName() + " ���");
				// ���ô˰�ť�Ķ�������,�Ա��ں���ļ������¼���ȡ�õ�ǰ��۵�ȫ�޶���
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
	 * �����˵�
	 */

	private void setupHelpMenu() {
		helpMenu = new JMenu("����(H)");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		//JMenuItem welcome = new JMenuItem("��ӭ����");
		JMenuItem aboutMenuItem = new JMenuItem("����ϵͳ");
		//aboutMenuItem.setMnemonic(KeyEvent.VK_A);
		aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));		
		aboutMenuItem.addActionListener(new AboutActionListener());
		personalCenter = new JMenuItem("��������");
		//personalCenter.setMnemonic(KeyEvent.VK_P);
		personalCenter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.CTRL_MASK));
		personalCenter.addActionListener(new PanelListener());
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
	}

	/**
	 * �˳�ϵͳ
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

	// ע���û�
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int n = JOptionPane.showConfirmDialog(null, "ȷ��ע����ǰ�û���", "ȷ�϶Ի���",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			// ȷ��ע��
			lib.setVisible(true);
			this.setVisible(false);
		} else if (n == JOptionPane.NO_OPTION) {
			return;
		}
	}

	/**
	 * "�˳�"�¼������ڲ���.
	 */
	class ExitActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			int n = JOptionPane.showConfirmDialog(null, "ȷ���˳�ϵͳ��", "ȷ�϶Ի���",
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.NO_OPTION) {
				return;
			} else {
				exit();
			}
		}
	}

	/**
	 * "�رմ���"�¼������ڲ���.
	 */
	class WindowCloser extends WindowAdapter {
		// ���������Լ������涨���exit()����
		@Override
		public void windowClosing(WindowEvent e) {
			exit();
		}
	}

	/**
	 * "���"ѡ�������
	 * 
	 */
	class LookAndFeelListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			// ȡ�õ�ǰ������¼��������е�ȫ�޶������������
			String className = event.getActionCommand();
			try {
				UIManager.setLookAndFeel(className);
				// �򵥵���۸���
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
				tabbedPane.add("�������", borrowBookPanel);
			} else if ("return" == e.getActionCommand()) {
				tabbedPane.add("�������", giveBackBook);
			}else if(myBorrowMenuItem==e.getSource()){
				tabbedPane.addTab("�ҵĽ���", myBorrowPanel);
			}else if(personalCenter==e.getSource()){
				tabbedPane.add("������Ϣ",readerInfoMain);
			}else if(libMenuItem==e.getSource()){
				tabbedPane.add("��Ŀ����",bookQueryPanel);
			}
		}
	}
	/**
	 * "���ڰ����˵�������
	 */
	class AboutActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				desktop.open(new File("libraryHelp.CHM"));
			} catch (Exception ea) {
				System.out.println("�򿪰����ĵ�ʧ�ܣ�"+ea.getMessage());
			}
		}
	}

	// ����ͻ���ļ�������
//	class LenRetActionListener implements ActionListener {
//		@Override
//		public void actionPerformed(ActionEvent event) {
//			if ("borrow" == event.getActionCommand()) {
//				tabbedPane.add("����", lendPanel);
//			} else if ("return" == event.getActionCommand()) {
//				tabbedPane.add("����", lendPanel);
//			}
//		}
//	}

	// �ҵĽ���
//	class MyBorrowActionListener implements ActionListener {
//		public void actionPerformed(ActionEvent event) {
//			tabbedPane.addTab("�ҵĽ���", myBorrowPanel);
//		}
//	}

	// �ݲ���Ϣ
//	class BookInLibraryActionListener implements ActionListener {
//		public void actionPerformed(ActionEvent event) {
//			tabbedPane.addTab("��Ŀ����", bookQueryPanel);
//		}
//	}

	class BookMaintainListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (bookMenuItem == event.getSource()) {
				tabbedPane.addTab("ͼ��ά��", bookMaintainPanel);
				//lrMenu.setEnabled(true);
			}
			if (readerMenuItem == event.getSource()) {
				tabbedPane.addTab("����ά��", readerMaintainPanel);
			}
			if (librarianMenuItem == event.getSource()) {
				tabbedPane.addTab("����Աά��", masterManagePanel);
			}
			if (paraMenuItem == event.getSource()) {			
				tabbedPane.addTab("ϵͳ����ά��", paraMaintainPanel);
			}
		}
	}
	/**
	 * �������ģ������޸ĸ�����Ϣ
	 */
//	class PersonalActionListener implements ActionListener{
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			tabbedPane.add("������Ϣ",readerInfoMain);
//		}
//	}
	/**
	 * ������Ѽ�԰���¼�������
	 */
	class TestListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "û��ʵ��");
		}
	}
}
