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

	private JTextField locationField; // ͼ��ݲص�
	private JTextField totalAmountField; // ���ͼ������
	private JTextField barCode; // ��ʼ������
	private JTextField isbnField; // ��׼ISBN��
	private JTextField nameField; // ����
	private JTextField seriesField; // ������
	private JTextField authorField; // ����
	private JTextField publisherField; // ������
	private JTextField sizeField; // ������Ϣ
	private JTextField pagesField; // ҳ��
	private JTextField priceField; // �۸�
	private JTextField pictureField; // ͼƬ
	private JTextField clumnField;	//�����
	private JTextArea introduction; // ͼ����
	private DefaultTableModel bookmodel, libbookmodel;
	private LibClient	libClient ;
//	public static void main(String[] args) {
//		new NewBookDialog(new JFrame());
//	}

	public NewBookDialog(MainFrame mainFrame) {
		super(mainFrame,"�������");
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
				.setBorder(BorderFactory.createTitledBorder(etched, "������ʾ"));
		messagePanel
				.add(
						new JLabel(
								"<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;����Աָ��ͼ��<font color='red'>�ݲص�</font>��<font color='red'>��<br/>������</font>�����ͼ��<font color='red'>��������ʼֵ</font>��<br/>���ͼ�����������Զ����ɣ���<br/>Χ��<font color='blue'>��ʼֵ--->��ʼֵ+�������</font></html>"),
						BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(etched, "ͼ�����"));
		locationField = new JTextField(10); // �ݲص�
		totalAmountField = new JTextField(10); // �������
		barCode = new JTextField(10); // ��ʼ����
		isbnField = new JTextField(10); // ISBN��
		nameField = new JTextField(10); // ����
		seriesField = new JTextField(10);// ������
		authorField = new JTextField(10); // ����
		publisherField = new JTextField(10); // ������
		sizeField = new JTextField(10); // ����
		pagesField = new JTextField(10); // ҳ��
		priceField = new JTextField(10); // �۸�
		clumnField = new JTextField(10); // �����
		pictureField = new JTextField(10); // ͼƬ
		introduction = new JTextArea(); // ͼ����
		introduction.setLineWrap(true);
		// 1.
		panel.add(new JLabel("�ݲص�: "), new GBC(0, 1).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(locationField, new GBC(1, 1, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("�������: "), new GBC(0, 2).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(totalAmountField, new GBC(1, 2, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("��ʼ����: "), new GBC(0, 3).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(barCode, new GBC(1, 3, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));

		// 2.
		panel.add(new JLabel("ISBN��: "), new GBC(0, 4).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(isbnField, new GBC(1, 4, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("����: "), new GBC(0, 5).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(nameField, new GBC(1, 5, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("������: "), new GBC(0, 6).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(seriesField, new GBC(1, 6, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		// 3.
		panel.add(new JLabel("����: "), new GBC(0, 7).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(authorField, new GBC(1, 7, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("������: "), new GBC(0, 8).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(publisherField, new GBC(1, 8, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		panel.add(new JLabel("����: "), new GBC(0, 9).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(sizeField, new GBC(1, 9, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		// 4.
		panel.add(new JLabel("ҳ��: "), new GBC(0, 10).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(pagesField, new GBC(1, 10, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		
		// 6.
		panel.add(new JLabel("�۸�: "), new GBC(0, 11).setAnchor(GBC.EAST)
				.setInsets(3));
		
		panel.add(priceField, new GBC(1, 11, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		// 5.
		panel.add(new JLabel("�����: "), new GBC(0, 12).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(clumnField, new GBC(1, 12, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		// 7.
		panel.add(new JLabel("ͼƬ: "), new GBC(0, 13).setAnchor(GBC.EAST)
				.setInsets(3));
		panel.add(pictureField, new GBC(1, 13, 2, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(3));
		// 8.
		panel.add(new JLabel("ͼ����: "), new GBC(0, 14).setAnchor(GBC.EAST));

		panel.add(introduction, new GBC(1, 14, 1, 2).setFill(GBC.BOTH)
				.setWeight(100, 20).setInsets(3));
		JButton sub = new JButton("���");
		sub.addActionListener(new BookReadedInActionListener());
		panel.add(sub, new GBC(0, 15).setAnchor(GBC.EAST).setInsets(3));
		JButton res = new JButton("����");
		res.addActionListener(new ResActionListener());
		panel.add(res, new GBC(1, 15).setAnchor(GBC.EAST).setInsets(3));
		leftPanel.add(messagePanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 20).setInsets(3));
		leftPanel.add(panel, new GBC(0, 1).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(3));
		add(leftPanel, BorderLayout.WEST);
		// ���ͼ�����ϸ��Ϣ
		String[] columnNames = { "ISBN��", "����", "������", "����", "������", "����", "ҳ��",
				"�۸�", "���ݼ��", "ͼƬ", "�����" };
		bookmodel = new DefaultTableModel(columnNames, 0);
		JTable bookTable = new JTable(bookmodel);
		JPanel bookdetailPanel = new JPanel();
		bookdetailPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"���ͼ�����ϸ��Ϣ��"));
		bookdetailPanel.setLayout(new GridBagLayout());
		bookdetailPanel.add(new JScrollPane(bookTable), new GBC(0, 0).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));
		// ͼ��Ĺݲ���Ϣ
		String[] libcolumns = { "��ʼ����ֵ", "��������ֵ", "ISBN��", "ͼ��״̬", "�ݲص�",
				"���ʱ��" };
		libbookmodel = new DefaultTableModel(libcolumns, 0);
		JTable libbooktable = new JTable(libbookmodel);
		libbooktable.setEnabled(false);
		JPanel libbookPanel = new JPanel();
		libbookPanel.setLayout(new GridBagLayout());
		libbookPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"���ͼ��ݲ���ϸ��Ϣ��"));
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
	//���
	class BookReadedInActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			processReadedIn();
		}
	}
	//����
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
	//ͼ�����
	private void processReadedIn(){
		//�ݲص�
		String location = locationField.getText().trim();
		if(null==location||"".equals(location.trim())){
			JOptionPane.showMessageDialog(null, "�ݲصر���ָ����");
			return;
		}
		//�������
		String totalNumStr = totalAmountField.getText().trim();
		if(null==totalNumStr||"".equals(totalNumStr.trim())){
			JOptionPane.showMessageDialog(null, "�����������ָ����");
			return;
		}
		if(!totalNumStr.matches("\\d+")){
			JOptionPane.showMessageDialog(null, "�����������Ϊ���֣�");
			return;
		}
		int totalNum = Integer.parseInt(totalNumStr);
		//��ʼ����
		String beginBarcode = barCode.getText().trim();
		if(null==beginBarcode||"".equals(beginBarcode.trim())){
			JOptionPane.showMessageDialog(null, "��ʼ�������ָ����");
			return;
		}
		if(!beginBarcode.matches("\\d+")){
			JOptionPane.showMessageDialog(null, "��ʼ�������Ϊ������");
			return;
		}
		int beginIntBarcode = Integer.parseInt(beginBarcode);
		//ISBN��
		String isbn = isbnField.getText().trim();
		if(null==isbn||"".equals(isbn.trim())){
			JOptionPane.showMessageDialog(null, "��׼ISBN��Ų���Ϊ�գ�");
			return;
		}
		//����
		String bookname =nameField.getText().trim();
		if(null==bookname||"".equals(bookname.trim())){
			JOptionPane.showMessageDialog(null, "��������Ϊ�գ�");
			return;
		}
		//������
		String seriesname =seriesField.getText().trim();
		if(null==seriesname||"".equals(seriesname.trim())){
			seriesname = "�޴�����";
		}
		//����
		String author = authorField.getText().trim();
		if(null==author||"".equals(author.trim())){
			author = "����δ֪";
		}
		//������
		String publisher = publisherField.getText().trim();
		if(null==publisher||"".equals(publisher.trim())){
			publisher = "������δ֪";
		}
		//����
		String size =sizeField.getText().trim();
		if(null==size||"".equals(size.trim())){
			size = "0/0";
		}
		//ҳ��
		String pagestr = pagesField.getText().trim();
		pagestr = (null==pagestr||"".equals(pagestr.trim()))?"0":pagestr;
		if(!(pagestr.matches("\\d+"))){
			JOptionPane.showMessageDialog(null, "ͼ��ҳ�����Ϊ����!");
			return;
		}
		int page = Integer.parseInt(pagestr);
		//�۸�
		String prices = priceField.getText().trim();
		prices = (null==prices||"".equals(prices.trim()))?"0.0":prices;
		if(!(prices.matches("\\d+.*\\d*"))){
			JOptionPane.showMessageDialog(null, "ͼ��۸����Ϊ����");
			return;
		}
		double price = Double.parseDouble(prices);
		//�����
		String clumn = clumnField.getText().trim();
		if(null==clumn||"".equals(clumn.trim())){
			JOptionPane.showMessageDialog(null, "����ű���ָ����");
			return;
		}
		//ͼƬ
		String image = pictureField.getText().trim();
		image = (null==image||"".equals(image.trim()))?"����ͼƬ":image;
		//���
		String text = introduction.getText();
		text = (null==text||"".equals(text.trim()))?"���޼��":text;
		/**
		 * ���ͼ�������Ƿ��Լ�����
		 */
		BookDetails  book = libClient.getBookDetailsByIsbn(isbn);
		if(null!=book.getName()){
			JOptionPane.showMessageDialog(null, "��ISBN��ͼ�����ڹݲ��У�");
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
					JOptionPane.showMessageDialog(null,"ָ����Χ�����У�"+barcodeStr+"�����Ѵ��ڣ�");
					return;
				}else{
					System.out.println(barCode+"���벻����");
				}
			}
		}
		BookDetails bookDetails = new BookDetails(isbn, bookname, seriesname,
				author, publisher, size, page, price, text,
				image, clumn);
		//ͼ�����
		boolean mark = libClient.insertBookData(bookDetails);
		if(mark){
			log("ͼ����ϸ��Ϣ���ɹ�");
			showReadedInBookDetails();
		}else{
			log("ͼ����ϸ��Ϣ���ʧ��");
		}
		for(int i=0;i<totalNum;i++){
			String barcodeStr = String.valueOf(beginIntBarcode+i);	
			//�ݲ���Ϣ
			BookInLibrary bookInfo = new BookInLibrary(barcodeStr,isbn,location);
			boolean success = libClient.insertBookInfo(bookInfo);
			if(success){
				log(""+barcodeStr+"�����ͼ�����ɹ�");
			}else{
				log(""+barcodeStr+"�����ͼ�����ʧ��");
			}
		}
		showBookReadedInLibInfo();
		initField();
	}
	
	//��ʾ���ͼ�����ϸ��Ϣ
	private void showReadedInBookDetails(){
		int rows = bookmodel.getRowCount();
		while(rows>0){
			rows = rows-1;
			bookmodel.removeRow(rows);
		}
		//"ISBN��", "����", "������", "����", "������", "����", "ҳ��",
		//"�۸�", "���ݼ��", "ͼƬ", "�����"
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
	
	//��ʾ��⵽�ݲصص�ͼ�����ϸ��Ϣ
	private void showBookReadedInLibInfo(){
		int rows = libbookmodel.getRowCount();
		while(rows>0){
			rows = rows-1;
			libbookmodel.removeRow(rows);
		}
		// "��ʼ����ֵ", "��������ֵ", "ISBN��", "ͼ��״̬", "�ݲص�","���ʱ��" };
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
		obj[3] = "1:�ɽ�";
		obj[4] = locationField.getText().trim();
		obj[5] = DateFormat.getDateInstance().format(new Date());
		libbookmodel.addRow(obj);
	}
	private void log(Object obj){
		System.out.println("[ "+new CurrDateTime().currDateTime()+" ]NewBookDialog ��  "+obj);
	}
}