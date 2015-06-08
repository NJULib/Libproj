package clientside;

import java.awt.BorderLayout;

import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import serverside.entity.BookDetails;
import serverside.entity.BorrowInfo;
import serverside.entity.ReaderInfo;
import util.CurrDateTime;
import util.GBC;

public class ReaderInfoPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JButton clearButton, editButton, subButton, exitButton;

	private InnerReaderInfoPanel readerInfoPanel;
	
	//当前借书和超期图书
	private JTable borrowBookTable,overdueBookTable;
	private DefaultTableModel borrowBookTableModel,overdueBookTableModel;

	List[] borrowInfoList; // 当前借书信息

	private int account; // 各种类型的读者可以借阅的最大量

	private JPasswordField newPassword; // 新密码

	private JPasswordField rePassword; // 重复密码

	private JTextArea summaryArea; // 简介

	private MainFrame mainFrame;
	private LibClient libClient;
	private ReaderInfo readerInfo;

	private final Icon clearIcon = new ImageIcon(this.getClass().getResource(
			"images/clear.png"));
	private final Icon modifyIcon = new ImageIcon(this.getClass().getResource(
			"images/password.png"));
	private final Icon hangupIcon = new ImageIcon(this.getClass().getResource(
			"images/hangup.png"));
	private final Icon exitIcon = new ImageIcon(this.getClass().getResource(
			"images/exit.png"));

	public ReaderInfoPanel(MainFrame mainFrame, LibClient libClient,
			ReaderInfo readerInfo) {
		this.mainFrame = mainFrame;
		this.libClient = libClient;
		this.readerInfo = readerInfo;
		borrowInfoList = libClient.getReaderBorrowInfo(readerInfo.getReadid());
		int type = readerInfo.getType();
		account = libClient.getAccount(type);
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		JToolBar toolBar = new JToolBar("ToolBar");
		toolBar.setFloatable(false);
		clearButton = new JButton("取消", clearIcon);
		clearButton.setEnabled(false);
		clearButton.setBorderPainted(false);
		clearButton.addActionListener(new PasswordModifyActionListener());
		editButton = new JButton("修改密码", modifyIcon);
		editButton.addActionListener(new PasswordModifyActionListener());
		subButton = new JButton("提交", hangupIcon);
		subButton.addActionListener(new PasswordModifyActionListener());
		subButton.setEnabled(false);
		exitButton = new JButton("关闭", exitIcon);
		exitButton.addActionListener(this);
		toolBar.add(clearButton);
		toolBar.addSeparator();
		toolBar.add(editButton);
		toolBar.addSeparator();
		toolBar.add(subButton);
		toolBar.addSeparator();
		toolBar.add(exitButton);
		add(toolBar, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		readerInfoPanel = new InnerReaderInfoPanel();
		JPanel messagePanel = new JPanel();
		messagePanel.add(new JLabel("<html>书是人类的精神食粮, "
				+ "是人进步<br>的阶梯, 请保管并爱护好图书 !</html>"));
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "友情提示"));
		panel.add(readerInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		panel.add(messagePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100,
				20).setInsets(5));
		add(panel, BorderLayout.WEST);

		JPanel borrowBookPanel = new JPanel();
		borrowBookPanel.setLayout(new GridBagLayout());
		
		// 读者当前没有超期的图书的信息
		String[] columnNames = { "图书条码", "标准ISBN", "书名", "作者", "出版社", "价格","借阅日期", "应还日期" };
		borrowBookTableModel = new DefaultTableModel(columnNames, 0);
		borrowBookTable = new JTable(borrowBookTableModel);
		borrowBookTable.setEnabled(false);
		processBookInfo();
		log("没有到期图书："+borrowBookTableModel.getRowCount());
		borrowBookPanel.add(new JScrollPane(borrowBookTable), new GBC(0, 0)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));
		borrowBookPanel.setBorder(BorderFactory.createTitledBorder(etched,
		"未到期图书  [  "+borrowBookTableModel.getRowCount()+"  ] 本"));
		JPanel overdueBookPanel = new JPanel();
		overdueBookPanel.setLayout(new GridBagLayout());
		
		// 读者当前超期的图书的信息
		String[] colNames = { "图书条码", "标准ISBN", "书名", "价格", "借阅日期","应还日期","超期天数","罚款金额" };
		overdueBookTableModel = new DefaultTableModel(colNames, 0);
		overdueBookTable = new JTable(overdueBookTableModel);
		overdueBookTable.setEnabled(false);
		processOverdaysBookInfo();
		log("超期图书："+overdueBookTableModel.getRowCount());
		overdueBookPanel.add(new JScrollPane(overdueBookTable), new GBC(0, 0)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));
		overdueBookPanel.setBorder(BorderFactory.createTitledBorder(etched,
		"超期图书  [  "+overdueBookTableModel.getRowCount()+"  ] 本"));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(borrowBookPanel, new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		mainPanel.add(overdueBookPanel, new GBC(0, 1).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		add(mainPanel, BorderLayout.CENTER);
		setSize(700, 600);
		setVisible(true);
		validate();
	}

	// 读者当前没有超期的图书的借阅信息
	private void processBookInfo() {
		int row = borrowInfoList[0].size();		
		Object[] obj = new Object[8];
		for (int i = 0; i < row; i++) {
			BorrowInfo borrowInfo = (BorrowInfo) borrowInfoList[0].get(i);
			/**
			 *  如果当前时间早于图书的到期时间，说明该图书还没有到期
			 *  2009-1-2.before(2009-10-2)返回true
			 */
			if (new Date().before(borrowInfo.getDueDate())) {
				String barCode = borrowInfo.getBarCode();
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
	}

	private void processOverdaysBookInfo() {		
		int row = borrowInfoList[0].size();
		Object[] obj = new Object[8];
		for (int i = 0; i < row; i++) {
			BorrowInfo borrowInfo = (BorrowInfo) borrowInfoList[0].get(i);
			/**
			 *  如果当前时间在图书的到期时间后，说明该图书超期了
			 * 	2009-10-24.after(2009-8-10)返回true
			 */
			
			if (new Date().after(borrowInfo.getDueDate())) {
				String barCode = borrowInfo.getBarCode();
				BookDetails bookDetails = libClient.getBookDetails(barCode);
				obj[0] = barCode;
				obj[1] = bookDetails.getIsbn();
				obj[2] = bookDetails.getName();
				obj[3] = bookDetails.getPrice();
				obj[4] = borrowInfo.getBorrowDate();
				obj[5] = borrowInfo.getDueDate();
				obj[6] = borrowInfo.getOverduedays();
				obj[7] = borrowInfo.getFinedMoney();	
				overdueBookTableModel.addRow(obj);
			}
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		mainFrame.tabbedPane.remove(mainFrame.readerInfoMain);
	}
	protected void log(Object msg) {
		System.out
				.println(CurrDateTime.currDateTime() + "ReaderInfoPanel类: " + msg);
	}
	class InnerReaderInfoPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InnerReaderInfoPanel() {
			setLayout(new GridBagLayout());

			JTextField readerIdField = new JTextField(10);
			readerIdField.setText(readerInfo.getReadid());
			readerIdField.setEditable(false);
			JTextField nameField = new JTextField(10);
			nameField.setText(readerInfo.getName());
			nameField.setEditable(false);
			JTextField ageField = new JTextField(10);
			ageField.setText(readerInfo.getAge() + "");
			ageField.setEditable(false);
			JTextField sexField = new JTextField(10);
			sexField.setText(readerInfo.getGender());
			sexField.setEditable(false);
			JTextField majorField = new JTextField(10);
			majorField.setText(readerInfo.getMajor());
			majorField.setEditable(false);
			JTextField departmentField = new JTextField(10);
			departmentField.setText(readerInfo.getDepart());
			departmentField.setEditable(false);
			JTextField dateField = new JTextField(10);
			dateField.setText(readerInfo.getStartdate());
			dateField.setEditable(false);
			JTextField bookAmountField = new JTextField(10);
			bookAmountField.setText(borrowInfoList[0].size() + "");
			bookAmountField.setEditable(false);
			JTextField totalAmountField = new JTextField(10);
			totalAmountField.setText(account + "");
			totalAmountField.setEditable(false);
			newPassword = new JPasswordField(10);
			newPassword.setEditable(false);
			rePassword = new JPasswordField(10);
			rePassword.setEditable(false);
			summaryArea = new JTextArea();
			summaryArea.setText("好好学习，\n天天向上！");
			summaryArea.setEditable(false);
			summaryArea.setLineWrap(true);

			add(new JLabel("读者编号: "), new GBC(0, 0).setAnchor(GBC.EAST));
			add(readerIdField, new GBC(1, 0, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("姓名: "), new GBC(0, 1).setAnchor(GBC.EAST));
			add(nameField, new GBC(1, 1, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("年龄: "), new GBC(0, 2).setAnchor(GBC.EAST));
			add(ageField, new GBC(1, 2, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("性别: "), new GBC(0, 3).setAnchor(GBC.EAST));
			add(sexField, new GBC(1, 3, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("学院: "), new GBC(0, 4).setAnchor(GBC.EAST));
			add(majorField, new GBC(1, 4, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("系别: "), new GBC(0, 5).setAnchor(GBC.EAST));
			add(departmentField, new GBC(1, 5, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("注册日期: "), new GBC(0, 6).setAnchor(GBC.EAST));
			add(dateField, new GBC(1, 6, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("当前借书: "), new GBC(0, 7).setAnchor(GBC.EAST));
			add(bookAmountField, new GBC(1, 7, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("借书总量: "), new GBC(0, 8).setAnchor(GBC.EAST));
			add(totalAmountField, new GBC(1, 8, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("新密码: "), new GBC(0, 9).setAnchor(GBC.EAST));
			add(newPassword, new GBC(1, 9, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("重复密码: "), new GBC(0, 10).setAnchor(GBC.EAST));
			add(rePassword, new GBC(1, 10, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("简介: "), new GBC(0, 11).setAnchor(GBC.EAST));

			add(new JScrollPane(summaryArea), new GBC(1, 11, 2, 3).setFill(
					GBC.BOTH).setWeight(100, 100).setInsets(5));

			Border etched = BorderFactory.createEtchedBorder();
			setBorder(BorderFactory.createTitledBorder(etched, "读者基本信息"));
		}
	}
	
	/**
	 * 修改密码的监听器类
	 */
	class  PasswordModifyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//触发修改按钮
			if(editButton==e.getSource()){
				newPassword.setEditable(true);
				rePassword.setEditable(true);
				editButton.setEnabled(false);
				clearButton.setEnabled(true);
				subButton.setEnabled(true);
			}
			//触发取消按钮
			if(clearButton==e.getSource()){
				newPassword.setEditable(false);
				rePassword.setEditable(false);
				clearButton.setEnabled(false);
				subButton.setEnabled(false);
				editButton.setEnabled(true);
			}
			//触发提交按钮
			if(subButton==e.getSource()){			
				String newPass = new String(newPassword.getPassword());
				if(null==newPass||"".equals(newPass.trim())){
					JOptionPane.showMessageDialog(null, "新密码不能为空!");
					return;
				}
				else if(newPass.length()<6||newPass.length()>20){
					JOptionPane.showMessageDialog(null, "新密码长度必须在6-20之间!");
					return;
				}
				String rePass = new String(rePassword.getPassword());
				if(null==rePass||"".equals(rePass.trim())){
					JOptionPane.showMessageDialog(null, "重复密码不能为空!");
					return;
				}
				else if(rePass.length()<6||rePass.length()>20){
					JOptionPane.showMessageDialog(null, "重复密码长度必须在6-20之间!");
					return;
				}
				if(!(newPass.equals(rePass))){
					JOptionPane.showMessageDialog(null, "两次输入密码不一致!");
					return;
				}
				log("准备开始修改密码");
				newPassword.setEditable(false);
				rePassword.setEditable(false);
				subButton.setEnabled(false);
				clearButton.setEnabled(false);
				boolean modify = libClient.modifyPassword("reader",readerInfo.getReadid(),rePass);
				if(modify){
					JOptionPane.showMessageDialog(null, "密码修改成功");					
					newPassword.setText(null);
					rePassword.setText(null);
				}
				else{
					JOptionPane.showMessageDialog(null, "密码修改失败");
				}
				editButton.setEnabled(true);
			}
		}
	}
}