package clientside;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;

import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.JPanel;

import javax.swing.JDialog;
import javax.swing.JFrame;
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

public class ReaderInfoModifyDialog extends JDialog{
	private static final long serialVersionUID = 1L;

	private InnerReaderInfoPanel readerInfoPanel;
	JTextField readerIdField; // 读者编号
	JTextField passField ;
	JTextField nameField; // 读者姓名
	JTextField ageField; // 年龄
	JTextField sexField; // 性别
	JTextField addressField; // 家庭住址
	JTextField telField; // 联系电话
	JTextField dateField; // 注册日期
	JTextField endField; // 过期时间
	JTextField typeField; // 读者类型
	JTextField majorField; // 专业
	JTextField departmentField; // 院系
	
	
	// 当前借书和超期图书
	private JTable borrowBookTable;
	private DefaultTableModel borrowBookTableModel;

	String readerid = null;


	private MainFrame mainFrame;
	private LibClient libClient;
//	public static void main(String [] args){
//		new ReaderInfoModifyDialog(new JFrame());
//	}
	public ReaderInfoModifyDialog(MainFrame mainFrame){
		super(mainFrame);
		//, LibClient libClient) {
		//this.mainFrame = mainFrame;
		//this.libClient = libClient;
		ServerInfo serverInfo = new ServerInfo();
		libClient = new LibClient(serverInfo.getHost(),serverInfo.getPort());
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		// 左边的面板
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		// 左边的（上部）读者信息面板+（下部）消息信息
		readerInfoPanel = new InnerReaderInfoPanel();
		panel.add(readerInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		add(panel, BorderLayout.WEST);

		// 执行借书的借阅面板
		JPanel centerpanel = new JPanel();
		centerpanel.setLayout(new BorderLayout());

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

		centerpanel.add(borrowedBookPanel,BorderLayout.CENTER);
		add(centerpanel, BorderLayout.CENTER);

		setSize(800, 600);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
		int screenwidth = dim.width;
		int screenheight = dim.height;
		int dialogwidth = this.getWidth();
		int dialogheight = this.getHeight();
		int x = screenwidth/2-dialogwidth/2;
		int y = screenheight/2 - dialogheight/2;
		setLocation(x, y);
		// setResizable(false);
//		addWindowListener(new WindowAdapter(){
//			@Override
//			public void windowClosing(WindowEvent e){
//				setVisible(false);
//				dispose();
//				System.exit(0);
//			}
//		});
		setVisible(false);		
	}

	private void processBorrowBook() {
		readerid = readerIdField.getText().trim();
		if (null == readerid || "".equals(readerid)) {
			JOptionPane.showMessageDialog(null, "读者编号不能为空！");
			return;
		}
		if(!readerid.matches("\\d+")){
			JOptionPane.showMessageDialog(null, "读者编号格式不正确！");
			return;
		}
		int rows = borrowBookTableModel.getRowCount();
		while(rows>0){
			rows = rows - 1;
			borrowBookTableModel.removeRow(rows);			
		}
		readerIdField.setEditable(false);
		nameField.setText(""); // 读者姓名
		passField.setText("");
		ageField.setText(""); // 年龄
		sexField.setText(""); // 性别
		addressField.setText(""); 
		telField.setText(""); 
		typeField.setText(""); 
		majorField.setText(""); // 专业
		departmentField.setText(""); // 系别		
		dateField.setText(""); // 注册日期
		endField.setText(""); 
		ReaderInfo readerInfo = (ReaderInfo) libClient.getName(
				LibProtocals.OP_GET_READERINFO, readerid,
				"nullpass");
		if(null==readerInfo.getName()){
			JOptionPane.showMessageDialog(null, "该读者不存在，请确认后从新输入！");
			readerIdField.setEditable(true);
			return;
		}
		List[] borrowInfoList = libClient.getReaderBorrowInfo(readerInfo.getReadid());
		//当前借阅图书数量
		int num = borrowInfoList[0].size();
		//读者类型
		int type = readerInfo.getType();
		//根据读者类型取回最多可以借阅的图书数
		int account = libClient.getAccount(type);
		passField.setText(readerInfo.getPasswd());
		nameField.setText(readerInfo.getName());
		ageField.setText(readerInfo.getAge() + "");
		sexField.setText(readerInfo.getGender());
		addressField.setText(readerInfo.getAddress());
		telField.setText(readerInfo.getTel());
		typeField.setText(readerInfo.getType()+"");
		majorField.setText(readerInfo.getMajor());
		departmentField.setText(readerInfo.getDepart());
		dateField.setText(readerInfo.getStartdate());
		endField.setText(readerInfo.getEnddate());

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
			passField = new JTextField(10);
			//nameField.setEditable(false);
			ageField = new JTextField(10);
			//ageField.setEditable(false);
			sexField = new JTextField(10);
			addressField  = new JTextField(10);
			telField = new JTextField(10);

			dateField = new JTextField(10);
				//dateField.setEditable(false);	
			endField  = new JTextField(10);
			typeField = new JTextField(10);
			//sexField.setEditable(false);
			majorField = new JTextField(10);
			//majorField.setEditable(false);
			departmentField = new JTextField(10);
			//departmentField.setEditable(false);
			
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
			
			
			add(new JLabel("家庭住址: "), new GBC(0, 4).setAnchor(GBC.EAST));
			add(addressField, new GBC(1, 4, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			
			add(new JLabel("联系电话: "), new GBC(0, 5).setAnchor(GBC.EAST));
			add(telField, new GBC(1, 5, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			
			add(new JLabel("读者类型: "), new GBC(0, 6).setAnchor(GBC.EAST));
			add(typeField, new GBC(1, 6, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("专业: "), new GBC(0, 7).setAnchor(GBC.EAST));
			add(majorField, new GBC(1, 7, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("院系: "), new GBC(0, 8).setAnchor(GBC.EAST));
			add(departmentField, new GBC(1, 8, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			
			add(new JLabel("注册日期: "), new GBC(0, 9).setAnchor(GBC.EAST));
			add(dateField, new GBC(1, 9, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			

			add(new JLabel("过期日期: "), new GBC(0, 10).setAnchor(GBC.EAST));
			add(endField, new GBC(1,10, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));			
			
			JButton modify = new JButton("修改");
			modify.addActionListener(new ModifyActionListener());
			add(modify, new GBC(0, 11, 1, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			JButton res = new JButton("重置");
			res.addActionListener(new ReActinListener());
			add(res, new GBC(1, 11, 1, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			Border etched = BorderFactory.createEtchedBorder();
			setBorder(BorderFactory.createTitledBorder(etched, "读者基本信息"));
		}
	}
	//修改按钮
	class ModifyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String name = nameField.getText();
			if (null == name || "".equals(name)) {
				JOptionPane.showMessageDialog(null, "用户名不能为空");
				return;
			}
			String ages = ageField.getText();
			ages=(null==ages||"".equals(ages.trim()))?"0":ages;
			if(!(ages.matches("\\d+"))){
				JOptionPane.showMessageDialog(null, "年龄必须为整数");
				return;
			}
			int age = Integer.parseInt(ages);
			String major = majorField.getText();
			major = (null==major||"".equals(major.trim()))?"未知专业":major;
			String depart = departmentField.getText();
			depart = (null==depart||"".equals(depart.trim()))?"未知专业":depart;	
			String gender = sexField.getText();
			if (!("男".equals(gender) || "女".equals(gender))) {
				JOptionPane.showMessageDialog(null, "读者性别必须为’男‘或者’女‘");
				return;
			}
			String address = addressField.getText();
			address = (null == address || "".equals(address)) ? "未知" : address;
			String tel = telField.getText();
			if (!(tel.matches("\\d+"))) {
				JOptionPane.showMessageDialog(null,
						"联系电话必须是数字组成");
				return;
			}
			String typeStr = typeField.getText();
			typeStr = (null == typeStr || "".equals(typeStr)) ? "0" : typeStr;
			if (!(typeStr.matches("\\d{1}"))) {
				JOptionPane.showMessageDialog(null, "读者类型必须是1-9的单个数字");
				return;
			}
			int type = Integer.parseInt(typeStr);
			String start = dateField.getText();
			start = (null == start || "".equals(start)) ? "1971-00-00" : start;
			String end = endField.getText();
				end = (null == end || "".equals(end.trim())) ? "0000-00-00" : end;
				ReaderInfo reader = new ReaderInfo(readerid, passField.getText().trim(), name,age,gender, address, tel, start, end, type,major,depart);
				log("ReaderDetailsDialog开始修改");
				boolean mark = libClient.updateInfo(reader);
				if (mark) {
					JOptionPane.showMessageDialog(null, "读者信息修改成功");
					return;
				} else {
					JOptionPane.showMessageDialog(null, "读者信息修改失败");
				}	
			
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
	//重置
	class ReActinListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			readerIdField.setEditable(true);
			nameField.setText(""); // 读者姓名
			ageField.setText(""); // 年龄
			sexField.setText(""); // 性别
			addressField.setText(""); 
			telField.setText(""); 
			typeField.setText(""); 
			majorField.setText(""); // 专业
			departmentField.setText(""); // 系别		
			dateField.setText(""); // 注册日期
			endField.setText(""); 
			
		}
		
	}
}
