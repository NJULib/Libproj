package clientside;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import java.util.List;

import serverside.entity.BookDetails;
import serverside.entity.BookInLibrary;
import serverside.entity.BorrowInfo;
import serverside.entity.ParameterInfo;
import serverside.entity.ReaderInfo;
import util.CurrDateTime;

/**
 * @我的借阅主界面，可以实现查询当前借阅信息和历史借阅信息查询
 */
@SuppressWarnings("all")
public class MyBorrowPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = -4682596238240931448L;

	protected LibClient libClient;// 用于连接服务器的客户Socket
	//borrowInfoList(0)表示当前借阅信息，borrowInfoList(1)表示历史借阅信息
	private List[] borrowInfoList ;// 用于接收服务器传来的借阅者的借阅信息
	protected JTable borrowInfoTable;// 用于展示借阅信息的表格
	protected JScrollPane bookInLibScrollPane;// 存放借阅信息的面板
	protected JPanel topPanel;
	MainFrame parentFrame;
	private ReaderInfo readerInfo;
	//各种类型的读者可以借阅的最大量
	private int account ;
	public MyBorrowPanel(MainFrame parentFrame, LibClient libClient,ReaderInfo readerInfo){
		this.parentFrame = parentFrame;
		this.libClient = libClient;
		this.readerInfo = readerInfo;	
		this.setLayout(new BorderLayout());
		//取得读者的详细借阅信息
		borrowInfoList = libClient.getReaderBorrowInfo(readerInfo.getReadid());
		int type = readerInfo.getType();
		account = libClient.getAccount(type);
		buildGUI();// 建立主界面
		setVisible(true);
		validate();
	}

	protected void buildGUI() {
		/**
		 * @借阅选项栏，显示借阅选项
		 */
		TitledBorder titleBorder =	BorderFactory.createTitledBorder("学号："+readerInfo.getReadid()+"     姓名："+readerInfo.getName()+"        专业："+readerInfo.getMajor()+" / "+readerInfo.getDepart());
		titleBorder.setTitleFont(new Font("serial",Font.BOLD,16));
		titleBorder.setTitleColor(Color.MAGENTA);
		setBorder(titleBorder);
		bookInLibScrollPane=new JScrollPane();
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPanel.setBorder(BorderFactory.createTitledBorder("借阅查询选项"));
		JRadioButton currBorrowButton = new JRadioButton("当前借阅");
		currBorrowButton.setSelected(true);
		JRadioButton oldBorrowButton = new JRadioButton("历史借阅");
		JLabel borrowLabel = new JLabel();
		String borrowText = "                            已借阅：【  "+borrowInfoList[0].size()+" 】本，          最多可借：【  "+account+"  】本                                                                                  ";
		borrowLabel.setText(borrowText);
		topPanel.add(borrowLabel);
		topPanel.add(currBorrowButton);
		topPanel.add(oldBorrowButton);

		currBorrowButton.addActionListener(new CurrentBorrowInfoListener());
		oldBorrowButton.addActionListener(new OldBorrowInfoListener());

		/**
		 * 将2个RadioButton对象放进ButtonGroup中，以实现二选一
		 */
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(currBorrowButton);
		buttonGroup1.add(oldBorrowButton);
		this.add(BorderLayout.NORTH, topPanel);


		/**
		 * 显示借阅信息
		 */
		Iterator it = borrowInfoList[0].iterator();
		BorrowInfo borrowInfo;
		Vector allBorrowInfoVector = new Vector();// 存放所有的行的内容
		while (it.hasNext()) {
			borrowInfo = (BorrowInfo) it.next();
			Vector rowVector = new Vector();// 存放每一行内容的向量
			String barCode = borrowInfo.getBarCode();
			rowVector.add(barCode);
			BookDetails bookDetails = libClient.getBookDetails(barCode);
			rowVector.add(bookDetails.getIsbn());
			rowVector.add(bookDetails.getName());
			rowVector.add(bookDetails.getAuthors());
			rowVector.add(bookDetails.getPublisher());
			rowVector.add(bookDetails.getPrice());
			rowVector.add(borrowInfo.getBorrowDate());
			rowVector.add(borrowInfo.getDueDate());
			rowVector.add(borrowInfo.getOverduedays());
			log("BorrowPanel超期天数："+borrowInfo.getOverduedays());

			allBorrowInfoVector.add(rowVector);
		}
		Vector borrowHead = new Vector(); // 存储表头信息的向量
		borrowHead.add("图书条码");
		borrowHead.add("ISBN编号");
		borrowHead.add("书名");
		borrowHead.add("作者");
		borrowHead.add("出版社");
		borrowHead.add("价格");
		borrowHead.add("借阅日期");
		borrowHead.add("应还日期");
		borrowHead.add("超期天数");

		borrowInfoTable = new JTable(allBorrowInfoVector, borrowHead);// 生成具有内容和表头的表格
		borrowInfoTable.setEnabled(false);

		borrowInfoTable
				.setPreferredScrollableViewportSize(new Dimension(0, 120));

		bookInLibScrollPane.setViewportView(borrowInfoTable);
		bookInLibScrollPane.setBorder(BorderFactory.createTitledBorder("借阅信息"));
		this.add(BorderLayout.CENTER, bookInLibScrollPane);

		JPanel bottomPanel = new JPanel();
		JButton closeButton = new JButton("关闭窗口");
		closeButton.addActionListener(this);
		bottomPanel.add(closeButton);
		this.add(BorderLayout.SOUTH, bottomPanel);
		this.validate();
	}

	class CurrentBorrowInfoListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			buildCurrentInfoGUI(borrowInfoList[0]);
		}
	}

	class OldBorrowInfoListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			buildOldInfoGUI(borrowInfoList[1]);
		}
	}

	/**
	 * @借阅信息类，处理当前借阅信息内容的显示输出
	 */

	private void buildCurrentInfoGUI(List borrowInfoList) {

		Iterator it = borrowInfoList.iterator();
		BorrowInfo borrowInfo;
		Vector currVector = new Vector();
		while (it.hasNext()) {
			borrowInfo = (BorrowInfo) it.next();
			Vector rowVector = new Vector();
			String barCode = borrowInfo.getBarCode();
			rowVector.add(barCode);
			BookDetails bookDetails = libClient.getBookDetails(barCode);
			rowVector.add(bookDetails.getIsbn());
			rowVector.add(bookDetails.getName());
			rowVector.add(bookDetails.getAuthors());
			rowVector.add(bookDetails.getPublisher());
			rowVector.add(bookDetails.getPrice());
			rowVector.add(borrowInfo.getBorrowDate());
			rowVector.add(borrowInfo.getDueDate());			
			rowVector.add(borrowInfo.getOverduedays());
			currVector.add(rowVector);
		}
		Vector borrowHead = new Vector();
		borrowHead.add("图书条码");
		borrowHead.add("ISBN编号");
		borrowHead.add("书名");
		borrowHead.add("作者");
		borrowHead.add("出版社");
		borrowHead.add("价格");
		borrowHead.add("借阅日期");
		borrowHead.add("应还日期");
		borrowHead.add("超期天数");
		borrowInfoTable = new JTable(currVector, borrowHead);// 生成具有内容和表头的表格
		borrowInfoTable.setEnabled(false);

		borrowInfoTable
				.setPreferredScrollableViewportSize(new Dimension(0, 120));

		bookInLibScrollPane.setViewportView(borrowInfoTable);
		this.validate();
	}


	/**
	 * @借阅信息类，处理历史借阅信息内容的显示输出
	 */

	private void buildOldInfoGUI(List borrowInfoList) {
		
		Iterator it = borrowInfoList.iterator();
		BorrowInfo borrowInfo;
		Vector oldVector = new Vector();
		while (it.hasNext()) {
			borrowInfo = (BorrowInfo) it.next();
			Vector rowVector = new Vector();
			String barCode = borrowInfo.getBarCode();
			rowVector.add(barCode);
			BookDetails bookDetails = libClient.getBookDetails(barCode);
			rowVector.add(bookDetails.getIsbn());
			rowVector.add(bookDetails.getName());
			rowVector.add(bookDetails.getAuthors());
			rowVector.add(bookDetails.getPublisher());
			rowVector.add(bookDetails.getPrice());
			rowVector.add(borrowInfo.getBorrowDate());
			rowVector.add(borrowInfo.getReturnDate());
			rowVector.add(borrowInfo.getOverduedays());
			rowVector.add(borrowInfo.getFinedMoney());
			oldVector.add(rowVector);
		}
		Vector borrowHead = new Vector();
		borrowHead.add("图书条码");
		borrowHead.add("ISBN编号");
		borrowHead.add("书名");
		borrowHead.add("作者");
		borrowHead.add("出版社");
		borrowHead.add("价格");
		borrowHead.add("借阅日期");
		borrowHead.add("归还日期");
		borrowHead.add("超期天数");
		borrowHead.add("罚款金额");

		borrowInfoTable = new JTable(oldVector, borrowHead);// 生成具有内容和表头的表格
		borrowInfoTable.setEnabled(false);

		borrowInfoTable
				.setPreferredScrollableViewportSize(new Dimension(0, 120));

		bookInLibScrollPane.setViewportView(borrowInfoTable);
		this.validate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);	
		parentFrame.tabbedPane.remove(parentFrame.myBorrowPanel);
	}
	
	
	
	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "MyBorrowPanel类: "
				+ msg);
	}

}
