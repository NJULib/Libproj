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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import serverside.entity.BookDetails;
import serverside.entity.BorrowInfo;
import serverside.entity.ReaderInfo;
import util.GBC;

public class GiveBackBookPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame mainFrame;
	private LibClient libClient;
	private JTable borrowBookTable;
	private DefaultTableModel borrowBookModel;
	private JTextField barCodeText;
	private String barCode;
	private JButton returnButton;//还书
	protected BorrowInfo borrowInfo;
	private JLabel readerLabel;//显示借书的读者信息

	public GiveBackBookPanel(MainFrame mainFrame, LibClient libClient) {
		this.mainFrame = mainFrame;
		this.libClient = libClient;
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		JPanel messagePanel = new JPanel();
		messagePanel.setBorder(BorderFactory.createTitledBorder(etched, "说明："));
		messagePanel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;管理员根据<font color='red'>图书条码</font>归还图书!</html>"));
		// 图书归还的上部
		JPanel bookInfoPanel = new JPanel();
		barCodeText = new JTextField(20);
		barCodeText.addKeyListener(new ReaderIDKeyListener());
		returnButton = new JButton("还书");
		returnButton.addActionListener(new ReturnBookActionListener());
		
		bookInfoPanel.setLayout(new GridBagLayout());
		bookInfoPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"归还图书："));
		bookInfoPanel.add(new JLabel("图书条码: "), new GBC(0, 0).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(barCodeText, new GBC(1, 0).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));
		bookInfoPanel.add(returnButton, new GBC(2, 0).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));
//		bookInfoPanel.add(renewButton, new GBC(3, 0).setFill(GBC.HORIZONTAL)
//				.setWeight(30, 0).setInsets(5));
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());
		topPanel.add(messagePanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				30, 2).setInsets(5));
		topPanel.add(bookInfoPanel, new GBC(1, 0, 1, 1).setFill(GBC.BOTH).setWeight(
				70, 2).setInsets(5));
		add(topPanel, BorderLayout.NORTH);
		// 中部，显示借阅该条形码图书的读者的信息(读者信息和借阅信息)
		JPanel readerPanel = new JPanel();
		readerPanel.setLayout(new GridBagLayout());
		readerPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"借阅图书的读者信息："));
		readerLabel = new JLabel();
		readerLabel.setText("没有读者");
		readerPanel.add(readerLabel);

		// 归还的图书列表
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridBagLayout());
		tablePanel.setBorder(BorderFactory
				.createTitledBorder(etched, "读者还书信息："));
		String[] columnNames = { "读者编号","图书条码", "图书名称", "图书价格", "借书日期","应还日期","还书日期","超期天数","罚款金额" };
		borrowBookModel = new DefaultTableModel(columnNames, 0);
		borrowBookTable = new JTable(borrowBookModel);
		borrowBookTable.setEnabled(false);
		borrowBookTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		tablePanel.add(new JScrollPane(borrowBookTable), new GBC(0, 0).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(readerPanel, new GBC(0, 0, 2, 1).setFill(GBC.BOTH).setWeight(
				100, 2).setInsets(5));
		panel.add(tablePanel, new GBC(0, 1, 2, 2).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		add(panel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		JButton close = new JButton("关闭窗口");
		close.addActionListener(this);
		bottomPanel.add(close);
		add(bottomPanel,BorderLayout.SOUTH);
		
		setSize(800, 600);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		mainFrame.tabbedPane.remove(mainFrame.giveBackBook);
	}
	/**
	 * 回车确认
	 */
	class ReaderIDKeyListener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				processReturnBook();
			}
		}
	}

	/**
	 * 归还图书按钮监听类
	 */
	class ReturnBookActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			processReturnBook();
		}
	}
	void processReturnBook() {
		barCode = barCodeText.getText();
		if(null==barCode||"".equals(barCode.trim())){
			JOptionPane.showMessageDialog(null, "还书条码不能为空！");
			return;
		}
		if(!barCode.matches("\\d+")){
			JOptionPane.showMessageDialog(null, "还书条码格式输入错误！");
			return;
		}
		//检测指定条码图书是否存在
		boolean exists = libClient.checkExists(barCode);
		if(!exists){
			JOptionPane.showMessageDialog(null, "馆藏无此条码的图书，请查询后再次输入！");
			return;
		}
		boolean isBorrowed = libClient.checkIsBorrowed(barCode);
		if(isBorrowed){
			JOptionPane.showMessageDialog(null, "该条码的图书仍在馆藏中！");
			return;
		}
		//借阅指定条码图书的读者信息
		ReaderInfo readerInfo = libClient.getBarReader(barCode);
		if(null==readerInfo.getName()){
			JOptionPane.showMessageDialog(null, "查看借阅指定条码图书读者错误，该读者信息可能已被删除！");
		}
		int type = readerInfo.getType();
		int account = libClient.getAccount(type);
		String readerid = readerInfo.getReadid();
		List [] borrowList = libClient.getReaderBorrowInfo(readerid);
		String readertext = "<html>&nbsp;&nbsp;学号：<font color='red'>"+readerid+"</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;姓名：<font color='red'>"+readerInfo.getName()+"</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;专业/院系：<font color='red'>"+readerInfo.getMajor()+"/"+readerInfo.getDepart()+"</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前借阅：<font color='red'>"+(borrowList[0].size()-1)+"/"+account+"</font></html>";
		readerLabel.setText("");
		readerLabel.setText(readertext);
		borrowInfo = libClient.returnBook(barCode);	
		int rows = borrowBookModel.getRowCount();
		while(rows>0){
			rows = rows - 1;
			borrowBookModel.removeRow(rows);
		}
		BookDetails bookDetails = libClient.getBookDetails(barCode);
		Object[] obj = new Object[9];
		obj[0] = readerid;
		obj[1] = barCode;
		obj[2] = bookDetails.getName();
		obj[3] = bookDetails.getPrice();
		obj[4] = borrowInfo.getBorrowDate();
		obj[5] = borrowInfo.getDueDate();
		obj[6] = borrowInfo.getReturnDate();
		obj[7] = borrowInfo.getOverduedays();
		obj[8] = borrowInfo.getFinedMoney();
		borrowBookModel.addRow(obj);
	}//"读者编号","图书条码", "图书名称", "图书价格", "借书日期","应还日期","还书日期","超期天数","罚款金额" 
}