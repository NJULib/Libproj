package clientside;

import java.awt.BorderLayout;

import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;

import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.JPanel;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;


import serverside.entity.BookDetails;
import serverside.entity.BorrowInfo;
import serverside.entity.ReaderInfo;
import util.CurrDateTime;
import util.GBC;
import util.LibProtocals;

public class BorrowBookPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JButton renewButton,borrowButton;

	private InnerReaderInfoPanel readerInfoPanel;
	JTextField readerIdField; // 读者编号
	JTextField nameField; // 读者姓名
	JTextField ageField; // 年龄
	JTextField sexField; // 性别
	JTextField majorField; // 专业
	JTextField departmentField; // 系别
	JTextField bookAmountField; // 借书总数量
	JTextField totalAmountField; // 可以借阅总数量
	JTextField dateField; // 注册日期
	// 当前借书和超期图书
	private JTable borrowBookTable;
	private DefaultTableModel borrowBookTableModel;

	// 图书条码
	private JTextField barCodeField;
	String readerid = null;


	private MainFrame mainFrame;
	private LibClient libClient;
	public BorrowBookPanel(MainFrame mainFrame, LibClient libClient) {
		this.mainFrame = mainFrame;
		this.libClient = libClient;
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		// 左边的面板
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		// 左边的（上部）读者信息面板+（下部）消息信息
		readerInfoPanel = new InnerReaderInfoPanel();
		JPanel messagePanel = new JPanel();
		messagePanel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;管理员输入需借书<br/>读者的<font color='red'>读者编号</font>,然后按<br/><font color='green'>Enter键</font>或<font color='green'>确认</font>按钮进行<br/>确认，实现读者信息的<br/>显示.</html>"));
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "友情提示"));
		panel.add(readerInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		panel.add(messagePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100,
				20).setInsets(5));
		add(panel, BorderLayout.WEST);

		// 执行借书的借阅面板
		JPanel centerpanel = new JPanel();
		centerpanel.setLayout(new BorderLayout());
		JPanel borrowBookPanel = new JPanel();
		borrowBookPanel.add(new JLabel("借阅图书的条码："));
		barCodeField = new JTextField(20);
		barCodeField.setEditable(false);
		borrowBookPanel.add(barCodeField);
		borrowButton = new JButton("借阅");
		borrowButton.setEnabled(false);
		borrowButton.setActionCommand("borrow");
		borrowButton.addActionListener(new BorrowActionListener());
		renewButton = new JButton("续借");
		renewButton.setEnabled(false);
		renewButton.setActionCommand("renew");
		renewButton.addActionListener(new RenewActionListener());
		borrowBookPanel.add(borrowButton);
		borrowBookPanel.add(renewButton);
		borrowBookPanel.add(new JScrollPane(borrowBookTable), new GBC(0, 0)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));
		borrowBookPanel.setBorder(BorderFactory
				.createTitledBorder(etched, "借书"));

		// 以借阅图书列表
		JPanel borrowedBookPanel = new JPanel();
		borrowedBookPanel.setLayout(new GridBagLayout());

		// 读者当前没有超期的图书的信息
		String[] columnNames = { "图书条码", "标准ISBN", "书名", "作者", "出版社", "价格",
				"借阅日期", "应还日期" };
		borrowBookTableModel = new DefaultTableModel(columnNames, 0);
		borrowBookTable = new JTable(borrowBookTableModel);
		borrowBookTable.setEnabled(false);
		borrowedBookPanel.add(new JScrollPane(borrowBookTable), new GBC(0, 0)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));
		borrowedBookPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"借阅图书列表"));

		centerpanel.add(borrowBookPanel,BorderLayout.NORTH);
		centerpanel.add(borrowedBookPanel,BorderLayout.CENTER);
		add(centerpanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel();
		JButton close = new JButton("关闭窗口");
		close.addActionListener(this);
		bottomPanel.add(close);
		add(bottomPanel, BorderLayout.SOUTH);
		setSize(800, 600);
		setVisible(true);		
	}

	 //读者当前没有超期的图书的借阅信息
	private void processBorrowBook() {
		int rows = borrowBookTableModel.getRowCount();
		while(rows>0){
			rows = rows - 1;
			borrowBookTableModel.removeRow(rows);			
		}
		readerid = readerIdField.getText().trim();
		if (null == readerid || "".equals(readerid)) {
			JOptionPane.showMessageDialog(null, "读者编号不能为空！");
			return;
		}
		if(!readerid.matches("\\d+")){
			
			JOptionPane.showMessageDialog(null, "读者编号格式不正确！");
			return;
		}
		nameField.setText(""); // 读者姓名
		ageField.setText(""); // 年龄
		sexField.setText(""); // 性别
		majorField.setText(""); // 专业
		departmentField.setText(""); // 系别		
		bookAmountField.setText(""); // 借书总数量
		totalAmountField.setText("");	//可以借阅图书的最大数量
		dateField.setText(""); // 注册日期
		ReaderInfo readerInfo = (ReaderInfo) libClient.getName(
				LibProtocals.OP_GET_READERINFO, readerid,
				"nullpass");
		if(null==readerInfo.getName()){
			if(barCodeField.isEditable()){
				barCodeField.setEditable(false);
			}
			if(borrowButton.isEnabled()){
				borrowButton.setEnabled(false);
			}
			if(renewButton.isEnabled()){
				renewButton.setEnabled(false);
			}
			JOptionPane.showMessageDialog(null, "该读者不存在，请确认后从新输入！");			
			return;
		}
		if(!barCodeField.isEditable()){
			barCodeField.setEditable(true);
		}
		if(!borrowButton.isEnabled()){
			borrowButton.setEnabled(true);
		}
		if(!renewButton.isEnabled()){
			renewButton.setEnabled(true);
		}
		List[] borrowInfoList = libClient.getReaderBorrowInfo(readerInfo.getReadid());
		//当前借阅图书数量
		int num = borrowInfoList[0].size();
		//读者类型
		int type = readerInfo.getType();
		//根据读者类型取回最多可以借阅的图书数
		int account = libClient.getAccount(type);
		nameField.setText(readerInfo.getName());
		ageField.setText(readerInfo.getAge() + "");
		sexField.setText(readerInfo.getGender());
		majorField.setText(readerInfo.getMajor());
		departmentField.setText(readerInfo.getDepart());
		bookAmountField.setText(num + "");
		totalAmountField.setText(account + "");
		dateField.setText(readerInfo.getStartdate());
		

		Object[] obj = new Object[8];
		for (int i = 0; i < num; i++) {
			BorrowInfo borrowInfo = (BorrowInfo) borrowInfoList[0].get(i);
			String barCode = borrowInfo.getBarCode();
			log("借书面板："+barCode);
			BookDetails bookDetails = libClient.getBookDetails(barCode);
			obj[0] = barCode;
			obj[1] = bookDetails.getIsbn();
			obj[2] = bookDetails.getName();
			obj[3] = bookDetails.getAuthors();
			obj[4] = bookDetails.getPublisher();
			obj[5] = bookDetails.getPrice();
			obj[6] = borrowInfo.getBorrowDate();
			obj[7] = borrowInfo.getDueDate();
			borrowBookTableModel.addRow(obj);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		mainFrame.tabbedPane.remove(mainFrame.borrowBookPanel);
	}

	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "ReaderInfoPanel类: "
				+ msg);
	}

	class InnerReaderInfoPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InnerReaderInfoPanel() {
			setLayout(new GridBagLayout());

			readerIdField = new JTextField(10);
			readerIdField.addKeyListener(new KeyBoardListener());
			nameField = new JTextField(10);
			nameField.setEditable(false);
			ageField = new JTextField(10);
			ageField.setEditable(false);
			sexField = new JTextField(10);
			sexField.setEditable(false);
			majorField = new JTextField(10);
			majorField.setEditable(false);
			departmentField = new JTextField(10);
			departmentField.setEditable(false);
			bookAmountField = new JTextField("0");
			bookAmountField.setEditable(false);
			totalAmountField = new JTextField("0");
			totalAmountField.setEditable(false);
			dateField = new JTextField(10);
			dateField.setEditable(false);
			add(new JLabel("读者编号: "), new GBC(0, 0).setAnchor(GBC.EAST));
			add(readerIdField, new GBC(1, 0, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("姓名: "), new GBC(0, 1).setAnchor(GBC.EAST));
			add(nameField, new GBC(1, 1, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("年龄: "), new GBC(0, 2).setAnchor(GBC.EAST));
			add(ageField, new GBC(1, 2, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("性别: "), new GBC(0, 3).setAnchor(GBC.EAST));
			add(sexField, new GBC(1, 3, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("学院: "), new GBC(0, 4).setAnchor(GBC.EAST));
			add(majorField, new GBC(1, 4, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("系别: "), new GBC(0, 5).setAnchor(GBC.EAST));
			add(departmentField, new GBC(1, 5, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("当前借书: "), new GBC(0, 6).setAnchor(GBC.EAST));
			add(bookAmountField, new GBC(1, 6, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("借书总量: "), new GBC(0, 7).setAnchor(GBC.EAST));
			add(totalAmountField, new GBC(1, 7, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("注册日期: "), new GBC(0, 8).setAnchor(GBC.EAST));
			add(dateField, new GBC(1, 8, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			
			JButton button = new JButton("确认");
			button.addActionListener(new SureActionListener());
			add(button, new GBC(1, 9, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			Border etched = BorderFactory.createEtchedBorder();
			setBorder(BorderFactory.createTitledBorder(etched, "读者基本信息"));
		}
	}
	//确认按钮
	class SureActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			processBorrowBook();			
		}
	}
	//Enter键的监听器类
	class KeyBoardListener extends KeyAdapter {
			public void keyPressed(KeyEvent e) {
				// 获得键盘上某个键的键码表，该键被按下、敲击或者释放
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// public char getKeyChar()判断那个键被按下、敲击或释放
					// 该方法返回键盘上的字符
					//JOptionPane.showMessageDialog(null, "hello");
					processBorrowBook();
				}
			}
		}
	//读者借书
	class BorrowActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(null==readerIdField.getText()||"".equals(readerIdField.getText().trim())){
				if(barCodeField.isEditable()){
					barCodeField.setEditable(false);
				}
				if(borrowButton.isEnabled()){
					borrowButton.setEnabled(false);
				}
				if(renewButton.isEnabled()){
					renewButton.setEnabled(false);
				}
				JOptionPane.showMessageDialog(null, "必须确认读者才可借书！");
				return;
			}
			String barCode = barCodeField.getText().trim();
			if(null==barCode||"".equals(barCode.trim())){
				JOptionPane.showMessageDialog(null, "借阅图书条形码不能为空！");
				return;
			}
			if(!barCode.matches("\\d+")){
				JOptionPane.showMessageDialog(null, "借阅图书条形码格式不正确！");
				return;
			}			
			String countStr = bookAmountField.getText().trim();
			int count = 0;
			if(countStr.matches("\\d+")){
				count = Integer.parseInt(countStr);
			}
			else{
				JOptionPane.showMessageDialog(null, "系统错误，请联系维护人员！");
				return;
			}
			String amountStr = totalAmountField.getText().trim();
			int amount = 0;
			if(amountStr.matches("\\d+")){
				amount = Integer.parseInt(amountStr);
			}else{
				JOptionPane.showMessageDialog(null, "系统错误，请联系维护人员！");
				return;
			}
			if(count>=amount){
				JOptionPane.showMessageDialog(null,"已达到最大借阅量，不可再借！");
				return;
			}
			boolean exists = libClient.checkExists(barCode);
			//如果查询到指定条码的图书则返回true
			if(!exists){
				JOptionPane.showMessageDialog(null, "馆藏无此条码的图书，请重新借阅！");
				return;
			}
			boolean canborrow = libClient.checkCanBorrow(barCode);
			//canborrow为true表示图书不可借阅
			if(canborrow){
				JOptionPane.showMessageDialog(null, "图书已被借出！");
				return;
			}
			boolean mark = libClient.getLendBookInfo(readerid, barCode);
			if(mark){
				count=count+1;
				bookAmountField.setText("");
				bookAmountField.setText(count+"");
				processBorrowBook();
			}else{
				JOptionPane.showMessageDialog(null, "借书失败，请联系维护人员");
			}
		}
	}
	//续借图书
	class RenewActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(null==readerIdField.getText()||"".equals(readerIdField.getText().trim())){
				if(barCodeField.isEditable()){
					barCodeField.setEditable(false);
				}
				if(borrowButton.isEnabled()){
					borrowButton.setEnabled(false);
				}
				if(renewButton.isEnabled()){
					renewButton.setEnabled(false);
				}
				JOptionPane.showMessageDialog(null, "必须确认读者才可借书！");
				return;
			}
			String barCode = barCodeField.getText().trim();
			if(null==barCode||"".equals(barCode.trim())){
				JOptionPane.showMessageDialog(null, "借阅图书条形码不能为空！");
				return;
			}
			if(!barCode.matches("\\d+")){
				JOptionPane.showMessageDialog(null, "借阅图书条形码格式不正确！");
				return;
			}	
			boolean exists = libClient.checkExists(barCode);
			//如果查询到指定条码的图书则返回true
			if(!exists){
				JOptionPane.showMessageDialog(null, "馆藏无此条码的图书,请重新借阅！");
				return;
			}
			//检测图书是否已经被续借过
			int renew = libClient.bookIsRenewed(barCode);
			if(1==renew){
				JOptionPane.showMessageDialog(null, "只能续借一次,请您及时还书");
				return;
			}
			boolean isBorrowed = libClient.checkIsBorrowed(barCode);
			if(isBorrowed){
				JOptionPane.showMessageDialog(null, "该条码的图书仍在馆藏中！");
				return;
			}
			//续借图书
			boolean mark = libClient.renewBook(readerid,barCode);
			if(mark){
				processBorrowBook();
			}else{
				JOptionPane.showMessageDialog(null, "借阅失败，请归还图书！");
				return;
			}
		}
	}
}