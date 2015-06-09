package clientside;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import serverside.entity.BookDetails;
import serverside.entity.BookInLibrary;
import util.CurrDateTime;
import util.GBC;

public class NewBookDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField locationField; // 图书馆藏地
	private JTextField totalAmountField; // 入库图书数量
	private JTextField barCode; // 初始条形码
	private JTextField isbnField; // 标准ISBN号
	private JTextField nameField; // 书名
	private JTextField seriesField; // 丛书名
	private JTextField authorField; // 作者
	private JTextField publisherField; // 丛书名
	private JTextField sizeField; // 开本信息
	private JTextField pagesField; // 页数
	private JTextField priceField; // 价格
	private JTextField pictureField; // 图片
	private JTextField clumnField;	//分类号
	private JTextArea introduction; // 图书简介
	private DefaultTableModel bookmodel, libbookmodel;
	private LibClient	libClient ;
//	public static void main(String[] args) {
//		new NewBookDialog(new JFrame());
//	}

	public NewBookDialog(MainFrame mainFrame) {
		super(mainFrame,"新书入库");
		//this.mainFrame = mainFrame;	
		ServerInfo serverInfo = new ServerInfo();
		libClient = new LibClient(serverInfo.getHost(),serverInfo.getPort());
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "友情提示"));
		messagePanel
				.add(
						new JLabel(
								"<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;管理员指定图书<font color='red'>馆藏地</font>、<font color='red'>入<br/>库数量</font>及入库图书<font color='red'>条形码起始值</font>，<br/>入库图书条形码则自动生成，范<br/>围：<font color='blue'>初始值--->初始值+入库数量</font></html>"),
						BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(etched, "图书入库"));
		locationField = new JTextField(10); // 馆藏地
		totalAmountField = new JTextField(10); // 入库数量
		barCode = new JTextField(10); // 起始条码
		isbnField = new JTextField(10); // ISBN号
		nameField = new JTextField(10); // 书名
		seriesField = new JTextField(10);// 丛书名
		authorField = new JTextField(10); // 作者
		publisherField = new JTextField(10); // 出版社
		sizeField = new JTextField(10); // 开本
		pagesField = new JTextField(10); // 页数
		priceField = new JTextField(10); // 价格
		clumnField = new JTextField(10); // 分类号
		pictureField = new JTextField(10); // 图片
		introduction = new JTextArea(); // 图书简介
		introduction.setLineWrap(true);
		// 1.
		panel.add(new JLabel("馆藏地: "), new GBC(0, 1).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(locationField, new GBC(1, 1, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("入库数量: "), new GBC(0, 2).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(totalAmountField, new GBC(1, 2, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("起始条码: "), new GBC(0, 3).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(barCode, new GBC(1, 3, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));

		// 2.
		panel.add(new JLabel("ISBN号: "), new GBC(0, 4).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(isbnField, new GBC(1, 4, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("书名: "), new GBC(0, 5).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(nameField, new GBC(1, 5, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("丛书名: "), new GBC(0, 6).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(seriesField, new GBC(1, 6, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		// 3.
		panel.add(new JLabel("作者: "), new GBC(0, 7).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(authorField, new GBC(1, 7, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("出版社: "), new GBC(0, 8).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(publisherField, new GBC(1, 8, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("开本: "), new GBC(0, 9).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(sizeField, new GBC(1, 9, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		// 4.
		panel.add(new JLabel("页数: "), new GBC(0, 10).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(pagesField, new GBC(1, 10, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		
		// 6.
		panel.add(new JLabel("价格: "), new GBC(0, 11).setAnchor(GBC.EAST)
				.setInsets(3));
		
		panel.add(priceField, new GBC(1, 11, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		// 5.
		panel.add(new JLabel("分类号: "), new GBC(0, 12).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(clumnField, new GBC(1, 12, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		// 7.
		panel.add(new JLabel("图片: "), new GBC(0, 13).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(pictureField, new GBC(1, 13, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		// 8.
		panel.add(new JLabel("图书简介: "), new GBC(0, 14).setAnchor(GBC.EAST));

		panel.add(introduction, new GBC(1, 14, 1, 2).setFill(GBC.BOTH)
				.setWeight(100, 20).setInsets(3));
		JButton sub = new JButton("入库");
		sub.addActionListener(new BookReadedInActionListener());
		panel.add(sub, new GBC(0, 15).setAnchor(GBC.EAST).setInsets(3));
		JButton res = new JButton("重置");
		res.addActionListener(new ResActionListener());
		panel.add(res, new GBC(1, 15).setAnchor(GBC.EAST).setInsets(3));
		leftPanel.add(messagePanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 20).setInsets(3));
		leftPanel.add(panel, new GBC(0, 1).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(3));
		add(leftPanel, BorderLayout.WEST);
		// 入库图书的详细信息
		String[] columnNames = { "ISBN号", "书名", "丛书名", "作者", "出版社", "开本", "页数",
				"价格", "内容简介", "图片", "分类号" };
		bookmodel = new DefaultTableModel(columnNames, 0);
		JTable bookTable = new JTable(bookmodel);
		JPanel bookdetailPanel = new JPanel();
		bookdetailPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"入库图书的详细信息："));
		bookdetailPanel.setLayout(new GridBagLayout());
		bookdetailPanel.add(new JScrollPane(bookTable), new GBC(0, 0).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));
		// 图书的馆藏信息
		String[] libcolumns = { "起始条码值", "结束条码值", "ISBN号", "图书状态", "馆藏地",
				"入库时间" };
		libbookmodel = new DefaultTableModel(libcolumns, 0);
		JTable libbooktable = new JTable(libbookmodel);
		libbooktable.setEnabled(false);
		JPanel libbookPanel = new JPanel();
		libbookPanel.setLayout(new GridBagLayout());
		libbookPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"入库图书馆藏详细信息："));
		libbookPanel.add(new JScrollPane(libbooktable), new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		mainPanel.add(bookdetailPanel, new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));

		mainPanel.add(libbookPanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		mainPanel.setBorder(BorderFactory.createEtchedBorder());
		add(mainPanel, BorderLayout.CENTER);

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
	//入库
	class BookReadedInActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			processReadedIn();
		}
	}
	//重置
	class ResActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			initField();
		}
	}
	private void initField(){
		locationField.setText("");
		totalAmountField.setText("");
		barCode.setText("");
		isbnField.setText("");
		nameField.setText("");
		seriesField.setText("");
		authorField.setText("");
		publisherField.setText("");
		sizeField.setText("");
		pagesField.setText("");
		clumnField.setText("");
		priceField.setText("");
		pictureField.setText("");
		introduction.setText("");
	}
	//图书入库
	private void processReadedIn(){
		//馆藏地
		String location = locationField.getText().trim();
		if(null==location||"".equals(location.trim())){
			JOptionPane.showMessageDialog(null, "馆藏地必须指定！");
			return;
		}
		//入库数量
		String totalNumStr = totalAmountField.getText().trim();
		if(null==totalNumStr||"".equals(totalNumStr.trim())){
			JOptionPane.showMessageDialog(null, "入库数量必须指定！");
			return;
		}
		if(!totalNumStr.matches("\\d+")){
			JOptionPane.showMessageDialog(null, "入库数量必须为数字！");
			return;
		}
		int totalNum = Integer.parseInt(totalNumStr);
		//起始条码
		String beginBarcode = barCode.getText().trim();
		if(null==beginBarcode||"".equals(beginBarcode.trim())){
			JOptionPane.showMessageDialog(null, "起始条码必须指定！");
			return;
		}
		if(!beginBarcode.matches("\\d+")){
			JOptionPane.showMessageDialog(null, "起始条码必须为整数！");
			return;
		}
		int beginIntBarcode = Integer.parseInt(beginBarcode);
		//ISBN号
		String isbn = isbnField.getText().trim();
		if(null==isbn||"".equals(isbn.trim())){
			JOptionPane.showMessageDialog(null, "标准ISBN编号不可为空！");
			return;
		}
		//书名
		String bookname =nameField.getText().trim();
		if(null==bookname||"".equals(bookname.trim())){
			JOptionPane.showMessageDialog(null, "书名不可为空！");
			return;
		}
		//丛书名
		String seriesname =seriesField.getText().trim();
		if(null==seriesname||"".equals(seriesname.trim())){
			seriesname = "无丛书名";
		}
		//作者
		String author = authorField.getText().trim();
		if(null==author||"".equals(author.trim())){
			author = "作者未知";
		}
		//出版社
		String publisher = publisherField.getText().trim();
		if(null==publisher||"".equals(publisher.trim())){
			publisher = "出版社未知";
		}
		//开本
		String size =sizeField.getText().trim();
		if(null==size||"".equals(size.trim())){
			size = "0/0";
		}
		//页数
		String pagestr = pagesField.getText().trim();
		pagestr = (null==pagestr||"".equals(pagestr.trim()))?"0":pagestr;
		if(!(pagestr.matches("\\d+"))){
			JOptionPane.showMessageDialog(null, "图书页码必须为数字!");
			return;
		}
		int page = Integer.parseInt(pagestr);
		//价格
		String prices = priceField.getText().trim();
		prices = (null==prices||"".equals(prices.trim()))?"0.0":prices;
		if(!(prices.matches("\\d+.*\\d*"))){
			JOptionPane.showMessageDialog(null, "图书价格必须为数字");
			return;
		}
		double price = Double.parseDouble(prices);
		//分类号
		String clumn = clumnField.getText().trim();
		if(null==clumn||"".equals(clumn.trim())){
			JOptionPane.showMessageDialog(null, "分类号必须指定！");
			return;
		}
		//图片
		String image = pictureField.getText().trim();
		image = (null==image||"".equals(image.trim()))?"暂无图片":image;
		//简介
		String text = introduction.getText();
		text = (null==text||"".equals(text.trim()))?"暂无简介":text;
		/**
		 * 检测图书条码是否以及存在
		 */
		BookDetails  book = libClient.getBookDetailsByIsbn(isbn);
		if(null!=book.getName()){
			JOptionPane.showMessageDialog(null, "该ISBN的图书已在馆藏中！");
			return;
		}
		//String [] barcodeStrs = new String[beginIntBarcode+totalNum];
		for(int i=0;i<totalNum;i++){			
			String barcodeStr = String.valueOf(beginIntBarcode+i);	
			if(barcodeStr.length()<5){
				int num = 5 - barcodeStr.length();
				for(int j=0;i<num;i++){
					barcodeStr = "0"+barcodeStr;
				}
				boolean mark = libClient.checkExists(barcodeStr);
				if(mark){
					JOptionPane.showMessageDialog(null,"指定范围条码中，"+barcodeStr+"条码已存在！");
					return;
				}else{
					System.out.println(barCode+"条码不存在");
				}
			}
		}
		BookDetails bookDetails = new BookDetails(isbn, bookname, seriesname,
				author, publisher, size, page, price, text,
				image, clumn);
		//图书入库
		boolean mark = libClient.insertBookData(bookDetails);
		if(mark){
			log("图书详细信息入库成功");
			showReadedInBookDetails();
		}else{
			log("图书详细信息入库失败");
		}
		for(int i=0;i<totalNum;i++){
			String barcodeStr = String.valueOf(beginIntBarcode+i);	
			//馆藏信息
			BookInLibrary bookInfo = new BookInLibrary(barcodeStr,isbn,location);
			boolean success = libClient.insertBookInfo(bookInfo);
			if(success){
				log(""+barcodeStr+"条码的图书插入成功");
			}else{
				log(""+barcodeStr+"条码的图书插入失败");
			}
		}
		showBookReadedInLibInfo();
		initField();
	}
	
	//显示入库图书的详细信息
	private void showReadedInBookDetails(){
		int rows = bookmodel.getRowCount();
		while(rows>0){
			rows = rows-1;
			bookmodel.removeRow(rows);
		}
		//"ISBN号", "书名", "丛书名", "作者", "出版社", "开本", "页数",
		//"价格", "内容简介", "图片", "分类号"
		Object [] obj = new Object[11];
		obj[0] = isbnField.getText().trim();
		obj[1] = nameField.getText().trim();
	    obj[2] = seriesField.getText().trim();
		obj[3] = authorField.getText().trim();
		obj[4] = publisherField.getText().trim();
		obj[5] = sizeField.getText().trim();
		obj[6] = pagesField.getText().trim();
		obj[7] = priceField.getText().trim();
	    obj[8] = introduction.getText().trim();
		obj[9] = pictureField.getText().trim();
		obj[10] = clumnField.getText().trim();
		bookmodel.addRow(obj);
	}
	
	//显示入库到馆藏地的图书的详细信息
	private void showBookReadedInLibInfo(){
		int rows = libbookmodel.getRowCount();
		while(rows>0){
			rows = rows-1;
			libbookmodel.removeRow(rows);
		}
		// "起始条码值", "结束条码值", "ISBN号", "图书状态", "馆藏地","入库时间" };
		Object [] obj = new Object[6];
		String beginBarCode =  barCode.getText().trim();
		if(beginBarCode.length()<5){
			int num = 5 - beginBarCode.length();
			for(int i=0;i<num;i++){
				beginBarCode = "0"+beginBarCode;
			}
		}
		obj[0] = beginBarCode;
		String endBarCode = String.valueOf(Integer.parseInt(barCode.getText().trim())+Integer.parseInt(totalAmountField.getText().trim())-1);
		if(endBarCode.length()<5){
			int num = 5 - endBarCode.length();
			for(int i=0;i<num;i++){
				endBarCode = "0"+endBarCode;
			}
		}
		obj[1] = endBarCode;
	    obj[2] = isbnField.getText().trim();
		obj[3] = "1:可借";
		obj[4] = locationField.getText().trim();
		obj[5] = DateFormat.getDateInstance().format(new Date());
		libbookmodel.addRow(obj);
	}
	private void log(Object obj){
		System.out.println("[ "+new CurrDateTime().currDateTime()+" ]NewBookDialog 类  "+obj);
	}
}