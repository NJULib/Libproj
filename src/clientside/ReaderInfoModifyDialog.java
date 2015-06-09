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
	JTextField readerIdField; // ���߱��
	JTextField passField ;
	JTextField nameField; // ��������
	JTextField ageField; // ����
	JTextField sexField; // �Ա�
	JTextField addressField; // ��ͥסַ
	JTextField telField; // ��ϵ�绰
	JTextField dateField; // ע������
	JTextField endField; // ����ʱ��
	JTextField typeField; // ��������
	JTextField majorField; // רҵ
	JTextField departmentField; // Ժϵ
	
	
	// ��ǰ����ͳ���ͼ��
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
		// ��ߵ����
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		// ��ߵģ��ϲ���������Ϣ���+���²�����Ϣ��Ϣ
		readerInfoPanel = new InnerReaderInfoPanel();
		panel.add(readerInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		add(panel, BorderLayout.WEST);

		// ִ�н���Ľ������
		JPanel centerpanel = new JPanel();
		centerpanel.setLayout(new BorderLayout());

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
			JOptionPane.showMessageDialog(null, "���߱�Ų���Ϊ�գ�");
			return;
		}
		if(!readerid.matches("\\d+")){
			JOptionPane.showMessageDialog(null, "���߱�Ÿ�ʽ����ȷ��");
			return;
		}
		int rows = borrowBookTableModel.getRowCount();
		while(rows>0){
			rows = rows - 1;
			borrowBookTableModel.removeRow(rows);			
		}
		readerIdField.setEditable(false);
		nameField.setText(""); // ��������
		passField.setText("");
		ageField.setText(""); // ����
		sexField.setText(""); // �Ա�
		addressField.setText(""); 
		telField.setText(""); 
		typeField.setText(""); 
		majorField.setText(""); // רҵ
		departmentField.setText(""); // ϵ��		
		dateField.setText(""); // ע������
		endField.setText(""); 
		ReaderInfo readerInfo = (ReaderInfo) libClient.getName(
				LibProtocals.OP_GET_READERINFO, readerid,
				"nullpass");
		if(null==readerInfo.getName()){
			JOptionPane.showMessageDialog(null, "�ö��߲����ڣ���ȷ�Ϻ�������룡");
			readerIdField.setEditable(true);
			return;
		}
		List[] borrowInfoList = libClient.getReaderBorrowInfo(readerInfo.getReadid());
		//��ǰ����ͼ������
		int num = borrowInfoList[0].size();
		//��������
		int type = readerInfo.getType();
		//���ݶ�������ȡ�������Խ��ĵ�ͼ����
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
			
			
			add(new JLabel("��ͥסַ: "), new GBC(0, 4).setAnchor(GBC.EAST));
			add(addressField, new GBC(1, 4, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			
			add(new JLabel("��ϵ�绰: "), new GBC(0, 5).setAnchor(GBC.EAST));
			add(telField, new GBC(1, 5, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			
			add(new JLabel("��������: "), new GBC(0, 6).setAnchor(GBC.EAST));
			add(typeField, new GBC(1, 6, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("רҵ: "), new GBC(0, 7).setAnchor(GBC.EAST));
			add(majorField, new GBC(1, 7, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));

			add(new JLabel("Ժϵ: "), new GBC(0, 8).setAnchor(GBC.EAST));
			add(departmentField, new GBC(1, 8, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			
			add(new JLabel("ע������: "), new GBC(0, 9).setAnchor(GBC.EAST));
			add(dateField, new GBC(1, 9, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			

			add(new JLabel("��������: "), new GBC(0, 10).setAnchor(GBC.EAST));
			add(endField, new GBC(1,10, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));			
			
			JButton modify = new JButton("�޸�");
			modify.addActionListener(new ModifyActionListener());
			add(modify, new GBC(0, 11, 1, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			JButton res = new JButton("����");
			res.addActionListener(new ReActinListener());
			add(res, new GBC(1, 11, 1, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(7));
			Border etched = BorderFactory.createEtchedBorder();
			setBorder(BorderFactory.createTitledBorder(etched, "���߻�����Ϣ"));
		}
	}
	//�޸İ�ť
	class ModifyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String name = nameField.getText();
			if (null == name || "".equals(name)) {
				JOptionPane.showMessageDialog(null, "�û�������Ϊ��");
				return;
			}
			String ages = ageField.getText();
			ages=(null==ages||"".equals(ages.trim()))?"0":ages;
			if(!(ages.matches("\\d+"))){
				JOptionPane.showMessageDialog(null, "�������Ϊ����");
				return;
			}
			int age = Integer.parseInt(ages);
			String major = majorField.getText();
			major = (null==major||"".equals(major.trim()))?"δ֪רҵ":major;
			String depart = departmentField.getText();
			depart = (null==depart||"".equals(depart.trim()))?"δ֪רҵ":depart;	
			String gender = sexField.getText();
			if (!("��".equals(gender) || "Ů".equals(gender))) {
				JOptionPane.showMessageDialog(null, "�����Ա����Ϊ���С����ߡ�Ů��");
				return;
			}
			String address = addressField.getText();
			address = (null == address || "".equals(address)) ? "δ֪" : address;
			String tel = telField.getText();
			if (!(tel.matches("\\d+"))) {
				JOptionPane.showMessageDialog(null,
						"��ϵ�绰�������������");
				return;
			}
			String typeStr = typeField.getText();
			typeStr = (null == typeStr || "".equals(typeStr)) ? "0" : typeStr;
			if (!(typeStr.matches("\\d{1}"))) {
				JOptionPane.showMessageDialog(null, "�������ͱ�����1-9�ĵ�������");
				return;
			}
			int type = Integer.parseInt(typeStr);
			String start = dateField.getText();
			start = (null == start || "".equals(start)) ? "1971-00-00" : start;
			String end = endField.getText();
				end = (null == end || "".equals(end.trim())) ? "0000-00-00" : end;
				ReaderInfo reader = new ReaderInfo(readerid, passField.getText().trim(), name,age,gender, address, tel, start, end, type,major,depart);
				log("ReaderDetailsDialog��ʼ�޸�");
				boolean mark = libClient.updateInfo(reader);
				if (mark) {
					JOptionPane.showMessageDialog(null, "������Ϣ�޸ĳɹ�");
					return;
				} else {
					JOptionPane.showMessageDialog(null, "������Ϣ�޸�ʧ��");
				}	
			
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
	//����
	class ReActinListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			readerIdField.setEditable(true);
			nameField.setText(""); // ��������
			ageField.setText(""); // ����
			sexField.setText(""); // �Ա�
			addressField.setText(""); 
			telField.setText(""); 
			typeField.setText(""); 
			majorField.setText(""); // רҵ
			departmentField.setText(""); // ϵ��		
			dateField.setText(""); // ע������
			endField.setText(""); 
			
		}
		
	}
}
