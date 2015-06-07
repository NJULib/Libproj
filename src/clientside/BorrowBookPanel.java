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

import javax.swing.JPanel;

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

public class BorrowBookPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JButton renewButton,borrowButton;

	private InnerReaderInfoPanel readerInfoPanel;
	JTextField readerIdField; // ���߱��
	JTextField nameField; // ��������
	JTextField ageField; // ����
	JTextField sexField; // �Ա�
	JTextField majorField; // רҵ
	JTextField departmentField; // ϵ��
	JTextField bookAmountField; // ����������
	JTextField totalAmountField; // ���Խ���������
	JTextField dateField; // ע������
	// ��ǰ����ͳ���ͼ��
	private JTable borrowBookTable;
	private DefaultTableModel borrowBookTableModel;

	// ͼ������
	private JTextField barCodeField;
	String readerid = null;


	private MainFrame mainFrame;
	private LibClient libClient;
	public BorrowBookPanel(MainFrame mainFrame, LibClient libClient) {
		this.mainFrame = mainFrame;
		this.libClient = libClient;
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		// ��ߵ����
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		// ��ߵģ��ϲ���������Ϣ���+���²�����Ϣ��Ϣ
		readerInfoPanel = new InnerReaderInfoPanel();
		JPanel messagePanel = new JPanel();
		messagePanel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;����Ա���������<br/>���ߵ�<font color='red'>���߱��</font>,Ȼ��<br/><font color='green'>Enter��</font>��<font color='green'>ȷ��</font>��ť����<br/>ȷ�ϣ�ʵ�ֶ�����Ϣ��<br/>��ʾ.</html>"));
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "������ʾ"));
		panel.add(readerInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		panel.add(messagePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100,
				20).setInsets(5));
		add(panel, BorderLayout.WEST);

		// ִ�н���Ľ������
		JPanel centerpanel = new JPanel();
		centerpanel.setLayout(new BorderLayout());
		JPanel borrowBookPanel = new JPanel();
		borrowBookPanel.add(new JLabel("����ͼ������룺"));
		barCodeField = new JTextField(20);
		barCodeField.setEditable(false);
		borrowBookPanel.add(barCodeField);
		borrowButton = new JButton("����");
		borrowButton.setEnabled(false);
		borrowButton.setActionCommand("borrow");
		borrowButton.addActionListener(new BorrowActionListener());
		renewButton = new JButton("����");
		renewButton.setEnabled(false);
		renewButton.setActionCommand("renew");
		renewButton.addActionListener(new RenewActionListener());
		borrowBookPanel.add(borrowButton);
		borrowBookPanel.add(renewButton);
		borrowBookPanel.add(new JScrollPane(borrowBookTable), new GBC(0, 0)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));
		borrowBookPanel.setBorder(BorderFactory
				.createTitledBorder(etched, "����"));

		// �Խ���ͼ���б�
		JPanel borrowedBookPanel = new JPanel();
		borrowedBookPanel.setLayout(new GridBagLayout());

		// ���ߵ�ǰû�г��ڵ�ͼ�����Ϣ
		String[] columnNames = { "ͼ������", "��׼ISBN", "����", "����", "������", "�۸�",
				"��������", "Ӧ������" };
		borrowBookTableModel = new DefaultTableModel(columnNames, 0);
		borrowBookTable = new JTable(borrowBookTableModel);
		borrowBookTable.setEnabled(false);
		borrowedBookPanel.add(new JScrollPane(borrowBookTable), new GBC(0, 0)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));
		borrowedBookPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"����ͼ���б�"));

		centerpanel.add(borrowBookPanel,BorderLayout.NORTH);
		centerpanel.add(borrowedBookPanel,BorderLayout.CENTER);
		add(centerpanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel();
		JButton close = new JButton("�رմ���");
		close.addActionListener(this);
		bottomPanel.add(close);
		add(bottomPanel, BorderLayout.SOUTH);
		setSize(800, 600);
		setVisible(true);		
	}

	 //���ߵ�ǰû�г��ڵ�ͼ��Ľ�����Ϣ
	private void processBorrowBook() {
		int rows = borrowBookTableModel.getRowCount();
		while(rows>0){
			rows = rows - 1;
			borrowBookTableModel.removeRow(rows);			
		}
		readerid = readerIdField.getText().trim();
		if (null == readerid || "".equals(readerid)) {
			JOptionPane.showMessageDialog(null, "���߱�Ų���Ϊ�գ�");
			return;
		}
		if(!readerid.matches("\\d+")){
			
			JOptionPane.showMessageDialog(null, "���߱�Ÿ�ʽ����ȷ��");
			return;
		}
		nameField.setText(""); // ��������
		ageField.setText(""); // ����
		sexField.setText(""); // �Ա�
		majorField.setText(""); // רҵ
		departmentField.setText(""); // ϵ��		
		bookAmountField.setText(""); // ����������
		totalAmountField.setText("");	//���Խ���ͼ����������
		dateField.setText(""); // ע������
		ReaderInfo readerInfo = (ReaderInfo) libClient.getName(
				LibProtocals.OP_GET_READERINFO, readerid,
				"nullpass");
		if(null==readerInfo.getName()){
			if(barCodeField.isEditable()){
				barCodeField.setEditable(false);
			}
			if(borrowButton.isEnabled()){
				borrowButton.setEnabled(false);
			}
			if(renewButton.isEnabled()){
				renewButton.setEnabled(false);
			}
			JOptionPane.showMessageDialog(null, "�ö��߲����ڣ���ȷ�Ϻ�������룡");			
			return;
		}
		if(!barCodeField.isEditable()){
			barCodeField.setEditable(true);
		}
		if(!borrowButton.isEnabled()){
			borrowButton.setEnabled(true);
		}
		if(!renewButton.isEnabled()){
			renewButton.setEnabled(true);
		}
		List[] borrowInfoList = libClient.getReaderBorrowInfo(readerInfo.getReadid());
		//��ǰ����ͼ������
		int num = borrowInfoList[0].size();
		//��������
		int type = readerInfo.getType();
		//���ݶ�������ȡ�������Խ��ĵ�ͼ����
		int account = libClient.getAccount(type);
		nameField.setText(readerInfo.getName());
		ageField.setText(readerInfo.getAge() + "");
		sexField.setText(readerInfo.getGender());
		majorField.setText(readerInfo.getMajor());
		departmentField.setText(readerInfo.getDepart());
		bookAmountField.setText(num + "");
		totalAmountField.setText(account + "");
		dateField.setText(readerInfo.getStartdate());
		

		Object[] obj = new Object[8];
		for (int i = 0; i < num; i++) {
			BorrowInfo borrowInfo = (BorrowInfo) borrowInfoList[0].get(i);
			String barCode = borrowInfo.getBarCode();
			log("������壺"+barCode);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		mainFrame.tabbedPane.remove(mainFrame.borrowBookPanel);
	}

	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "ReaderInfoPanel��: "
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
			nameField.setEditable(false);
			ageField = new JTextField(10);
			ageField.setEditable(false);
			sexField = new JTextField(10);
			sexField.setEditable(false);
			majorField = new JTextField(10);
			majorField.setEditable(false);
			departmentField = new JTextField(10);
			departmentField.setEditable(false);
			bookAmountField = new JTextField("0");
			bookAmountField.setEditable(false);
			totalAmountField = new JTextField("0");
			totalAmountField.setEditable(false);
			dateField = new JTextField(10);
			dateField.setEditable(false);
			add(new JLabel("���߱��: "), new GBC(0, 0).setAnchor(GBC.EAST));
			add(readerIdField, new GBC(1, 0, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("����: "), new GBC(0, 1).setAnchor(GBC.EAST));
			add(nameField, new GBC(1, 1, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("����: "), new GBC(0, 2).setAnchor(GBC.EAST));
			add(ageField, new GBC(1, 2, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("�Ա�: "), new GBC(0, 3).setAnchor(GBC.EAST));
			add(sexField, new GBC(1, 3, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("ѧԺ: "), new GBC(0, 4).setAnchor(GBC.EAST));
			add(majorField, new GBC(1, 4, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("ϵ��: "), new GBC(0, 5).setAnchor(GBC.EAST));
			add(departmentField, new GBC(1, 5, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("��ǰ����: "), new GBC(0, 6).setAnchor(GBC.EAST));
			add(bookAmountField, new GBC(1, 6, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("��������: "), new GBC(0, 7).setAnchor(GBC.EAST));
			add(totalAmountField, new GBC(1, 7, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("ע������: "), new GBC(0, 8).setAnchor(GBC.EAST));
			add(dateField, new GBC(1, 8, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			
			JButton button = new JButton("ȷ��");
			button.addActionListener(new SureActionListener());
			add(button, new GBC(1, 9, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			Border etched = BorderFactory.createEtchedBorder();
			setBorder(BorderFactory.createTitledBorder(etched, "���߻�����Ϣ"));
		}
	}
	//ȷ�ϰ�ť
	class SureActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			processBorrowBook();			
		}
	}
	//Enter���ļ�������
	class KeyBoardListener extends KeyAdapter {
			public void keyPressed(KeyEvent e) {
				// ��ü�����ĳ�����ļ�����ü������¡��û������ͷ�
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// public char getKeyChar()�ж��Ǹ��������¡��û����ͷ�
					// �÷������ؼ����ϵ��ַ�
					//JOptionPane.showMessageDialog(null, "hello");
					processBorrowBook();
				}
			}
		}
	//���߽���
	class BorrowActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(null==readerIdField.getText()||"".equals(readerIdField.getText().trim())){
				if(barCodeField.isEditable()){
					barCodeField.setEditable(false);
				}
				if(borrowButton.isEnabled()){
					borrowButton.setEnabled(false);
				}
				if(renewButton.isEnabled()){
					renewButton.setEnabled(false);
				}
				JOptionPane.showMessageDialog(null, "����ȷ�϶��߲ſɽ��飡");
				return;
			}
			String barCode = barCodeField.getText().trim();
			if(null==barCode||"".equals(barCode.trim())){
				JOptionPane.showMessageDialog(null, "����ͼ�������벻��Ϊ�գ�");
				return;
			}
			if(!barCode.matches("\\d+")){
				JOptionPane.showMessageDialog(null, "����ͼ���������ʽ����ȷ��");
				return;
			}			
			String countStr = bookAmountField.getText().trim();
			int count = 0;
			if(countStr.matches("\\d+")){
				count = Integer.parseInt(countStr);
			}
			else{
				JOptionPane.showMessageDialog(null, "ϵͳ��������ϵά����Ա��");
				return;
			}
			String amountStr = totalAmountField.getText().trim();
			int amount = 0;
			if(amountStr.matches("\\d+")){
				amount = Integer.parseInt(amountStr);
			}else{
				JOptionPane.showMessageDialog(null, "ϵͳ��������ϵά����Ա��");
				return;
			}
			if(count>=amount){
				JOptionPane.showMessageDialog(null,"�Ѵﵽ���������������ٽ裡");
				return;
			}
			boolean exists = libClient.checkExists(barCode);
			//�����ѯ��ָ�������ͼ���򷵻�true
			if(!exists){
				JOptionPane.showMessageDialog(null, "�ݲ��޴������ͼ�飬�����½��ģ�");
				return;
			}
			boolean canborrow = libClient.checkCanBorrow(barCode);
			//canborrowΪtrue��ʾͼ�鲻�ɽ���
			if(canborrow){
				JOptionPane.showMessageDialog(null, "ͼ���ѱ������");
				return;
			}
			boolean mark = libClient.getLendBookInfo(readerid, barCode);
			if(mark){
				count=count+1;
				bookAmountField.setText("");
				bookAmountField.setText(count+"");
				processBorrowBook();
			}else{
				JOptionPane.showMessageDialog(null, "����ʧ�ܣ�����ϵά����Ա");
			}
		}
	}
	//����ͼ��
	class RenewActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(null==readerIdField.getText()||"".equals(readerIdField.getText().trim())){
				if(barCodeField.isEditable()){
					barCodeField.setEditable(false);
				}
				if(borrowButton.isEnabled()){
					borrowButton.setEnabled(false);
				}
				if(renewButton.isEnabled()){
					renewButton.setEnabled(false);
				}
				JOptionPane.showMessageDialog(null, "����ȷ�϶��߲ſɽ��飡");
				return;
			}
			String barCode = barCodeField.getText().trim();
			if(null==barCode||"".equals(barCode.trim())){
				JOptionPane.showMessageDialog(null, "����ͼ�������벻��Ϊ�գ�");
				return;
			}
			if(!barCode.matches("\\d+")){
				JOptionPane.showMessageDialog(null, "����ͼ���������ʽ����ȷ��");
				return;
			}	
			boolean exists = libClient.checkExists(barCode);
			//�����ѯ��ָ�������ͼ���򷵻�true
			if(!exists){
				JOptionPane.showMessageDialog(null, "�ݲ��޴������ͼ��,�����½��ģ�");
				return;
			}
			//���ͼ���Ƿ��Ѿ��������
			int renew = libClient.bookIsRenewed(barCode);
			if(1==renew){
				JOptionPane.showMessageDialog(null, "ֻ������һ��,������ʱ����");
				return;
			}
			boolean isBorrowed = libClient.checkIsBorrowed(barCode);
			if(isBorrowed){
				JOptionPane.showMessageDialog(null, "�������ͼ�����ڹݲ��У�");
				return;
			}
			//����ͼ��
			boolean mark = libClient.renewBook(readerid,barCode);
			if(mark){
				processBorrowBook();
			}else{
				JOptionPane.showMessageDialog(null, "����ʧ�ܣ���黹ͼ�飡");
				return;
			}
		}
	}
}