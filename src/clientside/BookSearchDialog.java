package clientside;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import serverside.entity.BookDetails;
import serverside.entity.LibraianInfo;
import util.GBC;


public class BookSearchDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame mainFrame; // 主frame
	private List listBooks = null; // 存放图书信息
	private LibClient libClient; // 客户端类
	private LibraianInfo libInfo; // 管理员信息
	private JButton printButton; // 打印操作按钮
	private JButton exitButton; // 退出操作按钮
	private JTextField searchCondition; // 查询条件按钮
	private JButton searchButton; // 执行查询按钮
	private DefaultTableModel tableModel; // 表格模型
	private JTable bookTable; // 表格

	// public static void main(String[] args) {
	// new BookSearchDialog();
	// }

	public BookSearchDialog(MainFrame mainFrame) {
		super(mainFrame,"图书查询");
		this.mainFrame = mainFrame;
		ServerInfo serverInfo = new ServerInfo();
		libClient = new LibClient(serverInfo.getHost(),serverInfo.getPort());
		setTitle("图书查询");
		setLayout(new BorderLayout());
		
		Border etched = BorderFactory.createEtchedBorder();

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridBagLayout());
		searchPanel.setBorder(BorderFactory.createTitledBorder(etched, "查询条件"));
		searchCondition = new JTextField(10);
		searchCondition.addKeyListener(new KeyBoardListener());
		searchButton = new JButton("开始查询");
		searchButton.addActionListener(new BookSearchActionListener());
		searchPanel.add(new JLabel("请输入您想要查询的内容: "), new GBC(0, 1).setFill(
				GBC.EAST).setInsets(5));
		searchPanel.add(searchCondition, new GBC(1, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 100).setInsets(5));

		searchPanel.add(searchButton, new GBC(2, 1).setFill(GBC.WEST)
				.setInsets(5));

		JPanel messagePanel = new JPanel();
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "查询说明"));
		messagePanel.add(new JLabel("<html>请输入查询条件:支持模糊查询！<br/></html>"));
		String[] name = { "ISBN编号", "书名", "丛书名", "作者", "出版社", "开本", "页数",
				"定价", "简介", "分类号" };
		tableModel = new DefaultTableModel(name, 0);
		bookTable = new JTable(tableModel);
		bookTable.setGridColor(Color.BLUE);
		bookTable.setEnabled(false);

		JPanel toppanel = new JPanel();
		toppanel.setLayout(new GridBagLayout());
		toppanel.add(messagePanel, new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(50, 0).setInsets(5));
		toppanel.add(searchPanel, new GBC(1, 0).setFill(GBC.BOTH).setWeight(80, 0)
				.setInsets(5));
		add(toppanel, BorderLayout.NORTH);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(etched, "查询结果列表："));
		panel.add(new JScrollPane(bookTable), new GBC(0, 0, 2, 2).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));
		add(panel, BorderLayout.CENTER);

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

	public void execCheck() {
		/**
		 * 首先清除内容
		 */
		String text = searchCondition.getText();
		if (null == text || "".equals(text.trim())) {
			JOptionPane.showMessageDialog(null, "检索条件不能为空！");
			return;
		}
		listBooks = libClient.getAllInfos("bookdata", text);
		drawResults(listBooks);
		// System.out.println((BookDetails)listBooks.get(2));
	}

	private void drawResults(List list) {
		int rows = tableModel.getRowCount();
		while(rows>0){
			rows = rows - 1;
			tableModel.removeRow(rows);
		}
		if (0 == list.size()) {
			JOptionPane.showMessageDialog(null, "没有检索到你要的信息！");
		}
		int rowNum = list.size();
		Object[] obj = new Object[10];
		for (int i = 0; i < rowNum; i++) {
			BookDetails bookDetails = (BookDetails) list.get(i);
			 obj[0] = bookDetails.getIsbn();
			 obj[1] = bookDetails.getName();
			 obj[2] = bookDetails.getSeries();
			 obj[3] = bookDetails.getAuthors();
			 obj[4] = bookDetails.getPublisher();
			 obj[5] = bookDetails.getSize();
			 obj[6] = bookDetails.getPages();
			 obj[7] = bookDetails.getPrice();
			 obj[8] = bookDetails.getIntroduction();
			 obj[9] = bookDetails.getClnum();
			 tableModel.addRow(obj);
		}
	}

	/**
	 *查询图书信息
	 */
	class BookSearchActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			execCheck();
		}
	}

	/**
	 * 键盘事件
	 * 
	 */
	//Enter键的监听器类
	class KeyBoardListener extends KeyAdapter {
			public void keyPressed(KeyEvent e) {
				// 获得键盘上某个键的键码表，该键被按下、敲击或者释放
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// public char getKeyChar()判断那个键被按下、敲击或释放
					// 该方法返回键盘上的字符
					//JOptionPane.showMessageDialog(null, "hello");
					execCheck();
				}
			}
		}

	private void log(Object obj) {
		System.out.println("BookSearchDialog类："+obj);
	}
}
