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
	 * ͼ����
	 */
	private JTextField isbnValue;
	/**
	 * ����
	 */
	private JTextField nameValue;
	/**
	 * ������
	 */
	private JTextField seriesValue;
	/**
	 * ����
	 */
	private JTextField authorsValue;
	
	/**
	 * ������
	 */
	private JTextField publisherValue;
	/**
	 * ������Ϣ
	 */
	private JTextField sizesValue;
	/**
	 * ҳ��
	 */
	private JTextField pagesValue;
	
	/**
	 * �۸�
	 */
	private JTextField priceValue;
	/**
	 * ͼ�����
	 */
	private JTextField clssNumValue;
	/**
	 * ������Ϣ
	 */
	private JTextArea abstractInfo;

	/**
	 * ͼƬ
	 */
	private JTextField imageValue;
	private MainFrame mainFrame;
	//��������
	private JTextField selectField;
	
	//��ż�����¼��java.awt.JList
	private JList contentList;
	//��ż������ݵ�java.util.List
	private List listBooks ;
	//��������
	private String text;
	//ȷ�ϰ�ť
	private JButton button;
	//�޸ĺ�����
	JButton modiButton,resButton;
	private LibClient libClient; // �ͻ�����
//	 public static void main(String [] args){
//	 new BookModifyDialog(new JFrame());
//	 }
	public BookModifyDialog(MainFrame mainFrame) {
		super(mainFrame,"ͼ���޸�");
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
		messagePanel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>1.</font>Ĭ�ϲ�ѯ���е�ͼ�顣<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>2.</font>����Աѡ��ͼ������<br/>������޸ġ�</html>"));
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(border, "������ʾ"));
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());		
		panel.add(new JLabel("ISBN:"), new GBC(0, 0).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(isbnValue, new GBC(1, 0,2,1).setFill(GBC.HORIZONTAL).setWeight(100,
				0).setInsets(5));
		panel.add(new JLabel("����:"), new GBC(0, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(nameValue, new GBC(1, 1,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("������ :"), new GBC(0, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(seriesValue, new GBC(1, 2,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel(" ����"), new GBC(0, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(authorsValue, new GBC(1, 3,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("������: "), new GBC(0, 4).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(publisherValue, new GBC(1, 4,2,1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		panel.add(new JLabel("����: "), new GBC(0, 5).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(sizesValue, new GBC(1, 5,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("ҳ��: "), new GBC(0, 6).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(pagesValue, new GBC(1, 6,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("�۸�: "), new GBC(0, 7).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(priceValue, new GBC(1,7,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("�����: "), new GBC(0, 8).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(clssNumValue, new GBC(1, 8,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("ͼƬ: "), new GBC(0, 9).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(imageValue, new GBC(1, 9,2,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("���: "), new GBC(0, 10).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(abstractInfo, new GBC(1, 10,2,3).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		modiButton = new JButton("�޸�");
		modiButton.setEnabled(false);
		modiButton.addActionListener(new ModifyBookInfoActionListener());
		panel.add(modiButton, new GBC(0, 14,1,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		resButton = new JButton("����");
		resButton.setEnabled(false);
		resButton.addActionListener(new ResBookActionListener());
		panel.add(resButton, new GBC(1, 14,1,1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));		
		panel.setBorder(BorderFactory.createTitledBorder(border,"ִ���޸�:"));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(messagePanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(100,
				20).setInsets(2));
		mainPanel.add(panel, new GBC(0,1).setFill(GBC.BOTH).setWeight(100,
				100).setInsets(2));
		add(mainPanel, BorderLayout.WEST);

		
		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel("����ͼ��������"));
		selectField = new JTextField(10);
		topPanel.add(selectField);
		JButton select = new JButton("����");
		select.addActionListener(new SelectActionListener());
		topPanel.add(select);
		contentList = new JList();
		contentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//���ѡ��
		contentList.addListSelectionListener(new BookSelectionListener());
		//˫��
		contentList.addMouseListener(new BookListMouseClickListener());
		//JPanel listPanel = new JPanel();
		//listPanel.add(contentList);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(topPanel,BorderLayout.NORTH);
		centerPanel.add(new JScrollPane(contentList),BorderLayout.CENTER);
		JPanel bottomPanel = new JPanel();
		button = new JButton("ȷ��");
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
	//�޸�
	class ModifyBookInfoActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String isbn = isbnValue.getText().trim();
			if(null==isbn||"".equals(isbn.trim())){
				JOptionPane.showMessageDialog(null, "ISBN�Ų���Ϊ�գ�");
				return;
			}
			String name = nameValue.getText().trim();
			if(null==name||"".equals(name.trim())){
				JOptionPane.showMessageDialog(null, "��������Ϊ�գ�");
				return;
			}
			String series = seriesValue.getText().trim();
			if(null==series||"".equals(series.trim())){
				JOptionPane.showMessageDialog(null, "����������Ϊ�գ�");
				return;
			}
			String author = authorsValue.getText().trim();
			author = (null==author||"".equals(author.trim()))?"δ֪":author;
			
			String publisher = publisherValue.getText().trim();
			publisher = (null==publisher||"".equals(publisher.trim()))?"δ֪":publisher;
			
			String size = sizesValue.getText().trim();
			size = (null==size||"".equals(size.trim()))?"δ֪":size;
			
			String pages = pagesValue.getText().trim();
			pages = (null==pages||"".equals(pages.trim()))?"δ֪":pages;
			if(!(pages.matches("\\d+"))){
				JOptionPane.showMessageDialog(null, "ҳ�����Ϊ������");
				return;
			}
			int page = Integer.parseInt(pages);
			String prices = priceValue.getText().trim();
			prices = (null==prices||"".equals(prices.trim()))?"δ֪":prices;
			if(!(prices.matches("\\d+.*\\d+"))){
				JOptionPane.showMessageDialog(null, "�۸����Ϊ������");
				return;
			}
			double price = Double.parseDouble(prices);
			String clumn = clssNumValue.getText().trim();
			clumn = (null==clumn||"".equals(clumn.trim()))?"δ֪":clumn;
			
			String introduction =  abstractInfo.getText().trim();
			introduction = (null==introduction||"".equals(introduction.trim()))?"δ֪":introduction;
			String picture = imageValue.getText().trim();
			picture = (null==picture||"".equals(picture.trim()))?"δ֪":picture;
			BookDetails  bookDetails = new BookDetails(isbn,name,series,picture,publisher,size,page,price,introduction,picture,clumn);
			boolean mark = libClient.updateInfo(bookDetails);
			if(mark){
				JOptionPane.showMessageDialog(null, "ͼ����Ϣ�޸ĳɹ�");
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
				JOptionPane.showMessageDialog(null, "ͼ����Ϣ�޸�ʧ�ܣ�");
			}
		}
	}
	//ȡ��
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
	//������Ϣ
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
			//û�м��������ʱ��Ӧ���ͼ���б���
			Object[] noData = new Object[0];
			contentList.setListData(noData);
			JOptionPane.showMessageDialog(null, "�Բ���û���ҵ���Ҫ��ͼ�飡");
		}
	}
	//ȷ�ϰ�ť
	class ListActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			processFileList();
		}
	}
	//ѡ��list�ϵ�����
	class BookSelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent event) {

			if (contentList.isSelectionEmpty()) {
				button.setEnabled(false);
			} else {
				button.setEnabled(true);
			}
		}
	}
	//list��˫���¼�
	class BookListMouseClickListener extends  MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				processFileList();
			}
		}
	}
	
	//ִ��ѡ��list�����ݵķ���
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