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
 * @�ҵĽ��������棬����ʵ�ֲ�ѯ��ǰ������Ϣ����ʷ������Ϣ��ѯ
 */
@SuppressWarnings("all")
public class MyBorrowPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = -4682596238240931448L;

	protected LibClient libClient;// �������ӷ������Ŀͻ�Socket
	//borrowInfoList(0)��ʾ��ǰ������Ϣ��borrowInfoList(1)��ʾ��ʷ������Ϣ
	private List[] borrowInfoList ;// ���ڽ��շ����������Ľ����ߵĽ�����Ϣ
	protected JTable borrowInfoTable;// ����չʾ������Ϣ�ı��
	protected JScrollPane bookInLibScrollPane;// ��Ž�����Ϣ�����
	protected JPanel topPanel;
	MainFrame parentFrame;
	private ReaderInfo readerInfo;
	//�������͵Ķ��߿��Խ��ĵ������
	private int account ;
	public MyBorrowPanel(MainFrame parentFrame, LibClient libClient,ReaderInfo readerInfo){
		this.parentFrame = parentFrame;
		this.libClient = libClient;
		this.readerInfo = readerInfo;	
		this.setLayout(new BorderLayout());
		//ȡ�ö��ߵ���ϸ������Ϣ
		borrowInfoList = libClient.getReaderBorrowInfo(readerInfo.getReadid());
		int type = readerInfo.getType();
		account = libClient.getAccount(type);
		buildGUI();// ����������
		setVisible(true);
		validate();
	}

	protected void buildGUI() {
		/**
		 * @����ѡ��������ʾ����ѡ��
		 */
		TitledBorder titleBorder =	BorderFactory.createTitledBorder("ѧ�ţ�"+readerInfo.getReadid()+"     ������"+readerInfo.getName()+"        רҵ��"+readerInfo.getMajor()+" / "+readerInfo.getDepart());
		titleBorder.setTitleFont(new Font("serial",Font.BOLD,16));
		titleBorder.setTitleColor(Color.MAGENTA);
		setBorder(titleBorder);
		bookInLibScrollPane=new JScrollPane();
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPanel.setBorder(BorderFactory.createTitledBorder("���Ĳ�ѯѡ��"));
		JRadioButton currBorrowButton = new JRadioButton("��ǰ����");
		currBorrowButton.setSelected(true);
		JRadioButton oldBorrowButton = new JRadioButton("��ʷ����");
		JLabel borrowLabel = new JLabel();
		String borrowText = "                            �ѽ��ģ���  "+borrowInfoList[0].size()+" ������          ���ɽ裺��  "+account+"  ����                                                                                  ";
		borrowLabel.setText(borrowText);
		topPanel.add(borrowLabel);
		topPanel.add(currBorrowButton);
		topPanel.add(oldBorrowButton);

		currBorrowButton.addActionListener(new CurrentBorrowInfoListener());
		oldBorrowButton.addActionListener(new OldBorrowInfoListener());

		/**
		 * ��2��RadioButton����Ž�ButtonGroup�У���ʵ�ֶ�ѡһ
		 */
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(currBorrowButton);
		buttonGroup1.add(oldBorrowButton);
		this.add(BorderLayout.NORTH, topPanel);


		/**
		 * ��ʾ������Ϣ
		 */
		Iterator it = borrowInfoList[0].iterator();
		BorrowInfo borrowInfo;
		Vector allBorrowInfoVector = new Vector();// ������е��е�����
		while (it.hasNext()) {
			borrowInfo = (BorrowInfo) it.next();
			Vector rowVector = new Vector();// ���ÿһ�����ݵ�����
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
			log("BorrowPanel����������"+borrowInfo.getOverduedays());

			allBorrowInfoVector.add(rowVector);
		}
		Vector borrowHead = new Vector(); // �洢��ͷ��Ϣ������
		borrowHead.add("ͼ������");
		borrowHead.add("ISBN���");
		borrowHead.add("����");
		borrowHead.add("����");
		borrowHead.add("������");
		borrowHead.add("�۸�");
		borrowHead.add("��������");
		borrowHead.add("Ӧ������");
		borrowHead.add("��������");

		borrowInfoTable = new JTable(allBorrowInfoVector, borrowHead);// ���ɾ������ݺͱ�ͷ�ı��
		borrowInfoTable.setEnabled(false);

		borrowInfoTable
				.setPreferredScrollableViewportSize(new Dimension(0, 120));

		bookInLibScrollPane.setViewportView(borrowInfoTable);
		bookInLibScrollPane.setBorder(BorderFactory.createTitledBorder("������Ϣ"));
		this.add(BorderLayout.CENTER, bookInLibScrollPane);

		JPanel bottomPanel = new JPanel();
		JButton closeButton = new JButton("�رմ���");
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
	 * @������Ϣ�࣬����ǰ������Ϣ���ݵ���ʾ���
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
		borrowHead.add("ͼ������");
		borrowHead.add("ISBN���");
		borrowHead.add("����");
		borrowHead.add("����");
		borrowHead.add("������");
		borrowHead.add("�۸�");
		borrowHead.add("��������");
		borrowHead.add("Ӧ������");
		borrowHead.add("��������");
		borrowInfoTable = new JTable(currVector, borrowHead);// ���ɾ������ݺͱ�ͷ�ı��
		borrowInfoTable.setEnabled(false);

		borrowInfoTable
				.setPreferredScrollableViewportSize(new Dimension(0, 120));

		bookInLibScrollPane.setViewportView(borrowInfoTable);
		this.validate();
	}


	/**
	 * @������Ϣ�࣬������ʷ������Ϣ���ݵ���ʾ���
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
		borrowHead.add("ͼ������");
		borrowHead.add("ISBN���");
		borrowHead.add("����");
		borrowHead.add("����");
		borrowHead.add("������");
		borrowHead.add("�۸�");
		borrowHead.add("��������");
		borrowHead.add("�黹����");
		borrowHead.add("��������");
		borrowHead.add("������");

		borrowInfoTable = new JTable(oldVector, borrowHead);// ���ɾ������ݺͱ�ͷ�ı��
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
		System.out.println(CurrDateTime.currDateTime() + "MyBorrowPanel��: "
				+ msg);
	}

}
