package clientside;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import clientside.BookRetrievalPanel.BookListMouseClickListener;
import clientside.BookRetrievalPanel.BookSelectionListener;

import serverside.entity.BookDetails;
import util.GBC;


public class BookModifyDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 图书编号
	 */
	private JTextField isbnValue;
	/**
	 * 书名
	 */
	private JTextField nameValue;
	/**
	 * 丛书名
	 */
	private JTextField seriesValue;
	/**
	 * 作者
	 */
	private JTextField authorsValue;
	
	/**
	 * 出版社
	 */
	private JTextField publisherValue;
	/**
	 * 开本信息
	 */
	private JTextField sizesValue;
	/**
	 * 页数
	 */
	private JTextField pagesValue;
	
	/**
	 * 价格
	 */
	private JTextField priceValue;
	/**
	 * 图书分类
	 */
	private JTextField clssNumValue;
	/**
	 * 描述信息
	 */
	private JTextArea abstractInfo;

	/**
	 * 图片
	 */
	private JTextField imageValue;
	private MainFrame mainFrame;
	//检索条件
	private JTextField selectField;
	
	//存放检索记录的java.awt.JList
	private JList contentList;
	//存放检索内容的java.util.List
	private List listBooks ;
	//检索条件
	private String text;
	//确认按钮
	private JButton button;
	//修改和重置
	JButton modiButton,resButton;
	private LibClient libClient; // 客户端类
//	 public static void main(String [] args){
//	 new BookModifyDialog(new JFrame());
//	 }
	public BookModifyDialog(MainFrame mainFrame) {
		super(mainFrame,"图书修改");
		//this.mainFrame = mainFrame;
		ServerInfo serverInfo = new ServerInfo();
		libClient = new LibClient(serverInfo.getHost(),serverInfo.getPort());		
		setLayout(new BorderLayout());		
		isbnValue = new JTextField(10);
		isbnValue.setEditable(false);
		nameValue = new JTextField(10);
		nameValue.setEditable(false);
		seriesValue = new JTextField(10);
		seriesValue.setEditable(false);
		authorsValue = new JTextField(10);
		authorsValue.setEditable(false);
		publisherValue= new JTextField(10);
		publisherValue.setEditable(false);
		sizesValue= new JTextField(10);
		sizesValue.setEditable(false);
		pagesValue= new JTextField(10);
		pagesValue.setEditable(false);
		priceValue= new JTextField(10);
		priceValue.setEditable(false);
		clssNumValue= new JTextField(10);
		clssNumValue.setEditable(false);
		imageValue= new JTextField(10);	
		imageValue.setEditable(false);
		abstractInfo= new JTextArea(2,3);
		abstractInfo.setLineWrap(true);
		abstractInfo.setEditable(false);
		
		Border border = BorderFactory.createEtchedBorder();
		JPanel messagePanel = new JPanel();
		messagePanel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>1.</font>默认查询所有的图书。<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>2.</font>管理员选中图书在左<br/>侧进行修改。</html>"));
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(border, "友情提示"));
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());		
		panel.add(new JLabel("ISBN:"), new GBC(0, 0).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(isbnValue, new GBC(1, 0,2,1).setFill(GBC.HORIZONTAL).setWeight(100,
				0).setInsets(5));
		panel.add(new JLabel("书名:"), new GBC(0, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(nameValue, new GBC(1, 1,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("丛书名 :"), new GBC(0, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(seriesValue, new GBC(1, 2,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel(" 作者"), new GBC(0, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(authorsValue, new GBC(1, 3,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("出版社: "), new GBC(0, 4).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(publisherValue, new GBC(1, 4,2,1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		panel.add(new JLabel("开本: "), new GBC(0, 5).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(sizesValue, new GBC(1, 5,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("页数: "), new GBC(0, 6).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(pagesValue, new GBC(1, 6,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("价格: "), new GBC(0, 7).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(priceValue, new GBC(1,7,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("分类号: "), new GBC(0, 8).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(clssNumValue, new GBC(1, 8,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("图片: "), new GBC(0, 9).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(imageValue, new GBC(1, 9,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("简介: "), new GBC(0, 10).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(abstractInfo, new GBC(1, 10,2,3).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		modiButton = new JButton("修改");
		modiButton.setEnabled(false);
		modiButton.addActionListener(new ModifyBookInfoActionListener());
		panel.add(modiButton, new GBC(0, 14,1,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		resButton = new JButton("重置");
		resButton.setEnabled(false);
		resButton.addActionListener(new ResBookActionListener());
		panel.add(resButton, new GBC(1, 14,1,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));		
		panel.setBorder(BorderFactory.createTitledBorder(border,"执行修改:"));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(messagePanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(100,
				20).setInsets(2));
		mainPanel.add(panel, new GBC(0,1).setFill(GBC.BOTH).setWeight(100,
				100).setInsets(2));
		add(mainPanel, BorderLayout.WEST);

		
		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel("检索图书条件："));
		selectField = new JTextField(10);
		topPanel.add(selectField);
		JButton select = new JButton("检索");
		select.addActionListener(new SelectActionListener());
		topPanel.add(select);
		contentList = new JList();
		contentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//鼠标选中
		contentList.addListSelectionListener(new BookSelectionListener());
		//双击
		contentList.addMouseListener(new BookListMouseClickListener());
		//JPanel listPanel = new JPanel();
		//listPanel.add(contentList);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(topPanel,BorderLayout.NORTH);
		centerPanel.add(new JScrollPane(contentList),BorderLayout.CENTER);
		JPanel bottomPanel = new JPanel();
		button = new JButton("确认");
		button.setEnabled(false);
		button.addActionListener(new ListActionListener());
		bottomPanel.add(button);
		centerPanel.add(bottomPanel,BorderLayout.SOUTH);
		add(centerPanel,BorderLayout.CENTER);
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
		setVisible(true);
	}
	//修改
	class ModifyBookInfoActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String isbn = isbnValue.getText().trim();
			if(null==isbn||"".equals(isbn.trim())){
				JOptionPane.showMessageDialog(null, "ISBN号不能为空！");
				return;
			}
			String name = nameValue.getText().trim();
			if(null==name||"".equals(name.trim())){
				JOptionPane.showMessageDialog(null, "书名不能为空！");
				return;
			}
			String series = seriesValue.getText().trim();
			if(null==series||"".equals(series.trim())){
				JOptionPane.showMessageDialog(null, "从书名不能为空！");
				return;
			}
			String author = authorsValue.getText().trim();
			author = (null==author||"".equals(author.trim()))?"未知":author;
			
			String publisher = publisherValue.getText().trim();
			publisher = (null==publisher||"".equals(publisher.trim()))?"未知":publisher;
			
			String size = sizesValue.getText().trim();
			size = (null==size||"".equals(size.trim()))?"未知":size;
			
			String pages = pagesValue.getText().trim();
			pages = (null==pages||"".equals(pages.trim()))?"未知":pages;
			if(!(pages.matches("\\d+"))){
				JOptionPane.showMessageDialog(null, "页码必须为整数！");
				return;
			}
			int page = Integer.parseInt(pages);
			String prices = priceValue.getText().trim();
			prices = (null==prices||"".equals(prices.trim()))?"未知":prices;
			if(!(prices.matches("\\d+.*\\d+"))){
				JOptionPane.showMessageDialog(null, "价格必须为整数！");
				return;
			}
			double price = Double.parseDouble(prices);
			String clumn = clssNumValue.getText().trim();
			clumn = (null==clumn||"".equals(clumn.trim()))?"未知":clumn;
			
			String introduction =  abstractInfo.getText().trim();
			introduction = (null==introduction||"".equals(introduction.trim()))?"未知":introduction;
			String picture = imageValue.getText().trim();
			picture = (null==picture||"".equals(picture.trim()))?"未知":picture;
			BookDetails  bookDetails = new BookDetails(isbn,name,series,picture,publisher,size,page,price,introduction,picture,clumn);
			boolean mark = libClient.updateInfo(bookDetails);
			if(mark){
				JOptionPane.showMessageDialog(null, "图书信息修改成功");
				processSelect();
				isbnValue.setText("");
				nameValue.setText("");
				seriesValue.setText("");
				authorsValue.setText("");
				publisherValue.setText("");
				sizesValue.setText("");
				pagesValue.setText("");
				priceValue.setText("");
				clssNumValue.setText("");
				abstractInfo.setText("");
				imageValue.setText("");
				isbnValue.setEditable(false);
				nameValue.setEditable(false);
				seriesValue.setEditable(false);
				authorsValue.setEditable(false);
				publisherValue.setEditable(false);
				sizesValue.setEditable(false);
				pagesValue.setEditable(false);
				priceValue.setEditable(false);
				clssNumValue.setEditable(false);
				abstractInfo.setEditable(false);
				imageValue.setEditable(false);
				modiButton.setEnabled(false);
				resButton.setEnabled(false);
			}else{
				JOptionPane.showMessageDialog(null, "图书信息修改失败！");
			}
		}
	}
	//取消
	class  ResBookActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			isbnValue.setText("");
			nameValue.setText("");
			seriesValue.setText("");
			authorsValue.setText("");
			publisherValue.setText("");
			sizesValue.setText("");
			pagesValue.setText("");
			priceValue.setText("");
			clssNumValue.setText("");
			abstractInfo.setText("");
			imageValue.setText("");
			isbnValue.setEditable(false);
			nameValue.setEditable(false);
			seriesValue.setEditable(false);
			authorsValue.setEditable(false);
			publisherValue.setEditable(false);
			sizesValue.setEditable(false);
			pagesValue.setEditable(false);
			priceValue.setEditable(false);
			clssNumValue.setEditable(false);
			abstractInfo.setEditable(false);
			imageValue.setEditable(false);
			modiButton.setEnabled(false);
			resButton.setEnabled(false);
		}
	}
	//检索信息
	class  SelectActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			text = selectField.getText().trim();
			processSelect();
		}
	}
	private void processSelect(){
		if (null == text || "".equals(text.trim())) {
			text = "all";
		}
		listBooks = libClient.getAllInfos("bookdata", text);
		if(listBooks.size()>0){
			Object[] theData = listBooks.toArray();
			contentList.setListData(theData);
			
		}else{
			//没有检索到书的时候，应清空图书列表区
			Object[] noData = new Object[0];
			contentList.setListData(noData);
			JOptionPane.showMessageDialog(null, "对不起，没有找到您要的图书！");
		}
	}
	//确认按钮
	class ListActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			processFileList();
		}
	}
	//选中list上的内容
	class BookSelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent event) {

			if (contentList.isSelectionEmpty()) {
				button.setEnabled(false);
			} else {
				button.setEnabled(true);
			}
		}
	}
	//list的双击事件
	class BookListMouseClickListener extends  MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				processFileList();
			}
		}
	}
	
	//执行选中list上内容的方法
	private void processFileList(){
		int index = contentList.getSelectedIndex();
		BookDetails book = (BookDetails) listBooks.get(index);
		isbnValue.setText(book.getIsbn());
		nameValue.setText(book.getName());
		seriesValue.setText(book.getSeries());
		authorsValue.setText(book.getAuthors());
		publisherValue.setText(book.getPublisher());
		sizesValue.setText(book.getSize());
		pagesValue.setText(book.getPages()+"");
		priceValue.setText(book.getPrice()+"");
		clssNumValue.setText(book.getClnum());
		abstractInfo.setText(book.getIntroduction());
		imageValue.setText(book.getPicture());
		//isbnValue.setEditable(true);
		nameValue.setEditable(true);
		seriesValue.setEditable(true);
		authorsValue.setEditable(true);
		publisherValue.setEditable(true);
		sizesValue.setEditable(true);
		pagesValue.setEditable(true);
		priceValue.setEditable(true);
		clssNumValue.setEditable(true);
		abstractInfo.setEditable(true);
		imageValue.setEditable(true);
		modiButton.setEnabled(true);
		resButton.setEnabled(true);
	}
}