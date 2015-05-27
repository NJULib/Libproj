package clientside;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import serverside.entity.LibraianInfo;
import serverside.entity.ReaderInfo;
import util.CurrDateTime;
import util.LibProtocals;


@SuppressWarnings("all")
public class Library extends JFrame {

	private static final long serialVersionUID = -3333506539922251414L;

	private MainFrame mainFrame  ;

	private Checkbox adminBox, readerBox, guestBox; //

	private JTextField readerAccount;

	private JPasswordField passwdField;

	private JButton login, reset;

	private String status = "guest";

	private LibClient libClient;
	
	Icon okicon = new ImageIcon(getClass().getResource("images/admin.png"));
	Icon cancelicon = new ImageIcon(getClass().getResource("images/cancel.png"));
	/**
	 * 接收到维护条件
	 * 
	 * @param status表示是系统维护还是读者的我的借阅
	 * @param condition表示是管理员登陆还是读者登陆
	 * @param parentFrame
	 */
	public Library() {
		setTitle("登陆窗口");

		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(400, 260);
		Dimension framesize = this.getSize();
		int x = (int) screensize.getWidth() / 2 - (int) framesize.getWidth()/ 2;
		int y = (int) screensize.getHeight() / 2 - (int) framesize.getHeight()/ 2;
		this.setLocation(x, y);
		ServerInfo serverInfo = new ServerInfo();
		libClient = new LibClient(serverInfo.getHost(), serverInfo.getPort());
		buildGUI();
		setVisible(true);
	}

	protected void buildGUI() {
		
		Box boxbase, box1, box2, box3, box4, box5, box6, box7, box8;
		setLayout(new FlowLayout());
		boxbase = Box.createVerticalBox(); // 获得一个具有列型盒式布局的盒式容器
		box1 = Box.createHorizontalBox(); //	
		box2 = Box.createHorizontalBox(); // 获得一个具有行型盒式布局的盒式容器
		box3 = Box.createVerticalBox(); //
		box4 = Box.createVerticalBox(); //
		box5 = Box.createVerticalBox(); //
		box6 = Box.createHorizontalBox(); //
		box7 = Box.createVerticalBox(); //
		box8 = Box.createHorizontalBox(); //

		JLabel sysLabel = new JLabel();
		sysLabel.setText("图书管理系统");
		sysLabel.setFont(new Font("宋体", Font.PLAIN, 25));
		sysLabel.setForeground(Color.red);

		CheckboxGroup groupBox  = new CheckboxGroup();
		adminBox = new Checkbox("管理员", false, groupBox);
		adminBox.addItemListener(new CheckboxItemListener());
		readerBox = new Checkbox("读者", false, groupBox);
		readerBox.addItemListener(new CheckboxItemListener());
		guestBox = new Checkbox("匿名", true, groupBox);
		guestBox.addItemListener(new CheckboxItemListener());

		JLabel nameLabel = new JLabel("用户名：", JLabel.RIGHT);
		readerAccount = new JTextField("guest", 20);
		readerAccount.setEditable(false);
		readerAccount.addKeyListener(new KeyBoardListener());

		JLabel passLabel = new JLabel("密    码：", JLabel.RIGHT);
		passwdField = new JPasswordField("guest", 20);
		passwdField.setEditable(false);
		passwdField.addKeyListener(new KeyBoardListener());

		login = new JButton("登录",okicon);
		login.addActionListener(new LogActionListener());

		reset = new JButton("重置",cancelicon);
		reset.setEnabled(false);
		reset.addActionListener(new LogActionListener());

		box1.add(Box.createHorizontalStrut(45));
		box1.add(sysLabel);
		
//		TitledBorder titleBorder =	BorderFactory.createTitledBorder("请选择登陆身份：");
//		titleBorder.setTitleFont(new Font("serial",Font.BOLD,16));
//		titleBorder.setTitleColor(Color.BLUE);
//		box2.setBorder(titleBorder);
		box2.add(Box.createHorizontalStrut(50));
		box2.add(adminBox);
		box2.add(Box.createHorizontalStrut(1));
		box2.add(readerBox);
		box2.add(Box.createHorizontalStrut(1));
		box2.add(guestBox);

		box3.add(Box.createVerticalStrut(70));
		box3.add(box1);
		box3.add(Box.createVerticalStrut(50));
		box3.add(box2);

		box4.add(nameLabel);
		box4.add(Box.createVerticalStrut(5));
		box4.add(passLabel);

		
		box5.add(readerAccount);
		box5.add(Box.createVerticalStrut(5));
		box5.add(passwdField);

		box6.add(Box.createHorizontalStrut(50));
		box6.add(box4);
		box6.add(Box.createHorizontalStrut(5));
		box6.add(box5);
		box6.add(Box.createHorizontalStrut(50));

		box7.add(box3);
		box7.add(Box.createVerticalStrut(30));
		box7.add(box6);

		box8.add(Box.createHorizontalStrut(100));
		box8.add(login);
		box8.add(Box.createHorizontalStrut(50));
		box8.add(reset);
		box8.add(Box.createHorizontalStrut(100));

		boxbase.add(box7);
		boxbase.add(Box.createVerticalStrut(50));
		boxbase.add(box8);

		add(boxbase);

		setBounds(421, 225, 600, 450);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);
		validate();
		setResizable(false);
	}

	/**
	 * 读者登陆
	 */
	private void handleReaderLogin(String readerID, String passwd) 
	{
		ReaderInfo readerInfo = (ReaderInfo) libClient.getName(LibProtocals.OP_GET_READERINFO, readerID, passwd);
		if (null == readerInfo.getName()) 
		{
			JOptionPane.showMessageDialog(null, "用户名或密码错误,请验证后重新登陆");
			passwdField.setText("");
			return;
		}
		passwdField.setText("");
		this.setVisible(false);
		mainFrame = new MainFrame(this,libClient,readerInfo);
		//馆藏检索
		mainFrame.libMenu.setEnabled(true);
		mainFrame.libMenu.add(mainFrame.myBorrowMenuItem);
		mainFrame.helpMenu.add(mainFrame.personalCenter);
		mainFrame.setVisible(true);
	}

	/**
	 * 管理员登陆
	 */

	private void handleAdminLogin(String adminID, String passwd) 
	{
		LibraianInfo libInfo = (LibraianInfo) libClient.getName(LibProtocals.OP_GET_LIBRAIANINFO, adminID, passwd);
		if (null == libInfo.getName()) 
		{
			JOptionPane.showMessageDialog(null, "用户名或密码错误,请验证后重新登陆");
			passwdField.setText("");
			return;
		}
		passwdField.setText("");
		log(libInfo);
		log("图书维护权限:" + libInfo.getBookp());
		log("读者维护权限:" + libInfo.getReaderp());
		log("系统参数维护权限:" + libInfo.getParameterp());
		passwdField.setText("");
		mainFrame = new MainFrame(this,libClient,libInfo);
		// 图书维护
		if (1 == libInfo.getBookp()) 
		{
			this.setVisible(false);
			//借书还书lrMenu
			mainFrame.lrMenu.setEnabled(true);			
			mainFrame.sysMaintainMenu.setEnabled(true);
			mainFrame.sysMaintainMenu.add(mainFrame.bookMenuItem);
			mainFrame.setVisible(true);
		}
		// 读者维护
		else if (1 == libInfo.getReaderp()) 
		{
			this.setVisible(false);
			mainFrame.sysMaintainMenu.setEnabled(true);
			mainFrame.sysMaintainMenu.add(mainFrame.readerMenuItem);
			mainFrame.setVisible(true);
		}
		// 系统参数维护,超级管理员
		else if (1 == libInfo.getParameterp()) 
		{
			this.setVisible(false);
			mainFrame.sysMaintainMenu.setEnabled(true);
			mainFrame.sysMaintainMenu.add(mainFrame.bookMenuItem);
			mainFrame.sysMaintainMenu.add(mainFrame.readerMenuItem);
			mainFrame.sysMaintainMenu.add(mainFrame.librarianMenuItem);
			mainFrame.sysMaintainMenu.add(mainFrame.paraMenuItem);
			mainFrame.setVisible(true);
		} else 
		{
			JOptionPane.showMessageDialog(null, "用户名或密码错误,请验证后重新登陆");
			passwdField.setText("");
		}
	}
	
	/**
	 * 管理员或者用户登陆
	 * 
	 */
	class LogActionListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(login==e.getSource())
			{
				processLogin();
			}
			else if(reset==e.getSource())
			{
				readerAccount.setText("");
				passwdField.setText("");
			}
		}
	}

	/**
	 *单选按钮监听器。
	 */
	public class CheckboxItemListener implements ItemListener 
	{
		public void itemStateChanged(ItemEvent e) 
		{
			// 为不同的用户登录设置标志值。
			if (adminBox.getState()) {
				readerAccount.setEditable(true);
				passwdField.setEditable(true);
				reset.setEnabled(true);
				readerAccount.setText("");
				passwdField.setText("");
				status = "libraian";
			}
			if (readerBox.getState()) 
			{
				readerAccount.setEditable(true);
				passwdField.setEditable(true);
				reset.setEnabled(true);
				readerAccount.setText("");
				passwdField.setText("");
				status = "reader";
			}
			if (guestBox.getState()) 
			{

				readerAccount.setEditable(false);
				passwdField.setEditable(false);
				reset.setEnabled(false);
				readerAccount.setText("guest");
				passwdField.setText("guest");
				status = "guest";
			}
		}
	}

	// 键盘上的某个功能键被按下
	class KeyBoardListener extends  KeyAdapter 
	{
		public void keyPressed(KeyEvent e) 
		{
			// 获得键盘上某个键的键码表，该键被按下、敲击或者释放
			if (e.getKeyCode() == KeyEvent.VK_ENTER) 
			{
				// public char getKeyChar()判断那个键被按下、敲击或释放
				// 该方法返回键盘上的字符
				 processLogin();
			}
		}
	}

	/**
	 * 处理登陆
	 */
	private void processLogin() 
	{
		String ID = readerAccount.getText().trim();
		if (null == ID || "".equals(ID.trim())) {
			JOptionPane.showMessageDialog(null, "帐号不能为空");
			return;
		}
//		if (!(ID.matches("\\w{5,15}"))) {
//			JOptionPane.showMessageDialog(null, "帐号格式不正确");
//			return;
//		}

		String password = new String(passwdField.getPassword());
		String pass = password.trim();
		if (null==pass||"".equals(pass)) 
		{
			JOptionPane.showMessageDialog(null, "密码不能为空");
			return;
		}
		if ("libraian" == status) 
		{
			log("管理员登陆");
			handleAdminLogin(ID, pass);
		}
		if ("reader" == status) 
		{
			log("读者登陆");
			handleReaderLogin(ID, pass);
			return;
		}
		if ("guest" == status) {
			mainFrame = new MainFrame(this,libClient,null);
			mainFrame.setVisible(true);
			setVisible(false);
		}
	}

	protected void log(Object msg) 
	{
		System.out.println(CurrDateTime.currDateTime() + "LoginDialog类: " + msg);
	}

	public static void main(String[] arg) 
	{
		new Library();
	}
}
