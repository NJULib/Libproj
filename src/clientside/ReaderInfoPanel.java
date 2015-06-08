package clientside;

import java.awt.BorderLayout;

import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import serverside.entity.BookDetails;
import serverside.entity.BorrowInfo;
import serverside.entity.ReaderInfo;
import util.CurrDateTime;
import util.GBC;

public class ReaderInfoPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JButton clearButton, editButton, subButton, exitButton;

	private InnerReaderInfoPanel readerInfoPanel;
	
	//��ǰ����ͳ���ͼ��
	private JTable borrowBookTable,overdueBookTable;
	private DefaultTableModel borrowBookTableModel,overdueBookTableModel;

	List[] borrowInfoList; // ��ǰ������Ϣ

	private int account; // �������͵Ķ��߿��Խ��ĵ������

	private JPasswordField newPassword; // ������

	private JPasswordField rePassword; // �ظ�����

	private JTextArea summaryArea; // ���

	private MainFrame mainFrame;
	private LibClient libClient;
	private ReaderInfo readerInfo;

	private final Icon clearIcon = new ImageIcon(this.getClass().getResource(
			"images/clear.png"));
	private final Icon modifyIcon = new ImageIcon(this.getClass().getResource(
			"images/password.png"));
	private final Icon hangupIcon = new ImageIcon(this.getClass().getResource(
			"images/hangup.png"));
	private final Icon exitIcon = new ImageIcon(this.getClass().getResource(
			"images/exit.png"));

	public ReaderInfoPanel(MainFrame mainFrame, LibClient libClient,
			ReaderInfo readerInfo) {
		this.mainFrame = mainFrame;
		this.libClient = libClient;
		this.readerInfo = readerInfo;
		borrowInfoList = libClient.getReaderBorrowInfo(readerInfo.getReadid());
		int type = readerInfo.getType();
		account = libClient.getAccount(type);
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		JToolBar toolBar = new JToolBar("ToolBar");
		toolBar.setFloatable(false);
		clearButton = new JButton("ȡ��", clearIcon);
		clearButton.setEnabled(false);
		clearButton.setBorderPainted(false);
		clearButton.addActionListener(new PasswordModifyActionListener());
		editButton = new JButton("�޸�����", modifyIcon);
		editButton.addActionListener(new PasswordModifyActionListener());
		subButton = new JButton("�ύ", hangupIcon);
		subButton.addActionListener(new PasswordModifyActionListener());
		subButton.setEnabled(false);
		exitButton = new JButton("�ر�", exitIcon);
		exitButton.addActionListener(this);
		toolBar.add(clearButton);
		toolBar.addSeparator();
		toolBar.add(editButton);
		toolBar.addSeparator();
		toolBar.add(subButton);
		toolBar.addSeparator();
		toolBar.add(exitButton);
		add(toolBar, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		readerInfoPanel = new InnerReaderInfoPanel();
		JPanel messagePanel = new JPanel();
		messagePanel.add(new JLabel("<html>��������ľ���ʳ��, "
				+ "���˽���<br>�Ľ���, �뱣�ܲ�������ͼ�� !</html>"));
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "������ʾ"));
		panel.add(readerInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		panel.add(messagePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100,
				20).setInsets(5));
		add(panel, BorderLayout.WEST);

		JPanel borrowBookPanel = new JPanel();
		borrowBookPanel.setLayout(new GridBagLayout());
		
		// ���ߵ�ǰû�г��ڵ�ͼ�����Ϣ
		String[] columnNames = { "ͼ������", "��׼ISBN", "����", "����", "������", "�۸�","��������", "Ӧ������" };
		borrowBookTableModel = new DefaultTableModel(columnNames, 0);
		borrowBookTable = new JTable(borrowBookTableModel);
		borrowBookTable.setEnabled(false);
		processBookInfo();
		log("û�е���ͼ�飺"+borrowBookTableModel.getRowCount());
		borrowBookPanel.add(new JScrollPane(borrowBookTable), new GBC(0, 0)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));
		borrowBookPanel.setBorder(BorderFactory.createTitledBorder(etched,
		"δ����ͼ��  [  "+borrowBookTableModel.getRowCount()+"  ] ��"));
		JPanel overdueBookPanel = new JPanel();
		overdueBookPanel.setLayout(new GridBagLayout());
		
		// ���ߵ�ǰ���ڵ�ͼ�����Ϣ
		String[] colNames = { "ͼ������", "��׼ISBN", "����", "�۸�", "��������","Ӧ������","��������","������" };
		overdueBookTableModel = new DefaultTableModel(colNames, 0);
		overdueBookTable = new JTable(overdueBookTableModel);
		overdueBookTable.setEnabled(false);
		processOverdaysBookInfo();
		log("����ͼ�飺"+overdueBookTableModel.getRowCount());
		overdueBookPanel.add(new JScrollPane(overdueBookTable), new GBC(0, 0)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));
		overdueBookPanel.setBorder(BorderFactory.createTitledBorder(etched,
		"����ͼ��  [  "+overdueBookTableModel.getRowCount()+"  ] ��"));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(borrowBookPanel, new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		mainPanel.add(overdueBookPanel, new GBC(0, 1).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		add(mainPanel, BorderLayout.CENTER);
		setSize(700, 600);
		setVisible(true);
		validate();
	}

	// ���ߵ�ǰû�г��ڵ�ͼ��Ľ�����Ϣ
	private void processBookInfo() {
		int row = borrowInfoList[0].size();		
		Object[] obj = new Object[8];
		for (int i = 0; i < row; i++) {
			BorrowInfo borrowInfo = (BorrowInfo) borrowInfoList[0].get(i);
			/**
			 *  �����ǰʱ������ͼ��ĵ���ʱ�䣬˵����ͼ�黹û�е���
			 *  2009-1-2.before(2009-10-2)����true
			 */
			if (new Date().before(borrowInfo.getDueDate())) {
				String barCode = borrowInfo.getBarCode();
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
	}

	private void processOverdaysBookInfo() {		
		int row = borrowInfoList[0].size();
		Object[] obj = new Object[8];
		for (int i = 0; i < row; i++) {
			BorrowInfo borrowInfo = (BorrowInfo) borrowInfoList[0].get(i);
			/**
			 *  �����ǰʱ����ͼ��ĵ���ʱ���˵����ͼ�鳬����
			 * 	2009-10-24.after(2009-8-10)����true
			 */
			
			if (new Date().after(borrowInfo.getDueDate())) {
				String barCode = borrowInfo.getBarCode();
				BookDetails bookDetails = libClient.getBookDetails(barCode);
				obj[0] = barCode;
				obj[1] = bookDetails.getIsbn();
				obj[2] = bookDetails.getName();
				obj[3] = bookDetails.getPrice();
				obj[4] = borrowInfo.getBorrowDate();
				obj[5] = borrowInfo.getDueDate();
				obj[6] = borrowInfo.getOverduedays();
				obj[7] = borrowInfo.getFinedMoney();	
				overdueBookTableModel.addRow(obj);
			}
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		mainFrame.tabbedPane.remove(mainFrame.readerInfoMain);
	}
	protected void log(Object msg) {
		System.out
				.println(CurrDateTime.currDateTime() + "ReaderInfoPanel��: " + msg);
	}
	class InnerReaderInfoPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InnerReaderInfoPanel() {
			setLayout(new GridBagLayout());

			JTextField readerIdField = new JTextField(10);
			readerIdField.setText(readerInfo.getReadid());
			readerIdField.setEditable(false);
			JTextField nameField = new JTextField(10);
			nameField.setText(readerInfo.getName());
			nameField.setEditable(false);
			JTextField ageField = new JTextField(10);
			ageField.setText(readerInfo.getAge() + "");
			ageField.setEditable(false);
			JTextField sexField = new JTextField(10);
			sexField.setText(readerInfo.getGender());
			sexField.setEditable(false);
			JTextField majorField = new JTextField(10);
			majorField.setText(readerInfo.getMajor());
			majorField.setEditable(false);
			JTextField departmentField = new JTextField(10);
			departmentField.setText(readerInfo.getDepart());
			departmentField.setEditable(false);
			JTextField dateField = new JTextField(10);
			dateField.setText(readerInfo.getStartdate());
			dateField.setEditable(false);
			JTextField bookAmountField = new JTextField(10);
			bookAmountField.setText(borrowInfoList[0].size() + "");
			bookAmountField.setEditable(false);
			JTextField totalAmountField = new JTextField(10);
			totalAmountField.setText(account + "");
			totalAmountField.setEditable(false);
			newPassword = new JPasswordField(10);
			newPassword.setEditable(false);
			rePassword = new JPasswordField(10);
			rePassword.setEditable(false);
			summaryArea = new JTextArea();
			summaryArea.setText("�ú�ѧϰ��\n�������ϣ�");
			summaryArea.setEditable(false);
			summaryArea.setLineWrap(true);

			add(new JLabel("���߱��: "), new GBC(0, 0).setAnchor(GBC.EAST));
			add(readerIdField, new GBC(1, 0, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("����: "), new GBC(0, 1).setAnchor(GBC.EAST));
			add(nameField, new GBC(1, 1, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("����: "), new GBC(0, 2).setAnchor(GBC.EAST));
			add(ageField, new GBC(1, 2, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("�Ա�: "), new GBC(0, 3).setAnchor(GBC.EAST));
			add(sexField, new GBC(1, 3, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("ѧԺ: "), new GBC(0, 4).setAnchor(GBC.EAST));
			add(majorField, new GBC(1, 4, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("ϵ��: "), new GBC(0, 5).setAnchor(GBC.EAST));
			add(departmentField, new GBC(1, 5, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("ע������: "), new GBC(0, 6).setAnchor(GBC.EAST));
			add(dateField, new GBC(1, 6, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("��ǰ����: "), new GBC(0, 7).setAnchor(GBC.EAST));
			add(bookAmountField, new GBC(1, 7, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("��������: "), new GBC(0, 8).setAnchor(GBC.EAST));
			add(totalAmountField, new GBC(1, 8, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("������: "), new GBC(0, 9).setAnchor(GBC.EAST));
			add(newPassword, new GBC(1, 9, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("�ظ�����: "), new GBC(0, 10).setAnchor(GBC.EAST));
			add(rePassword, new GBC(1, 10, 2, 1).setFill(GBC.HORIZONTAL)
					.setWeight(100, 0).setInsets(5));

			add(new JLabel("���: "), new GBC(0, 11).setAnchor(GBC.EAST));

			add(new JScrollPane(summaryArea), new GBC(1, 11, 2, 3).setFill(
					GBC.BOTH).setWeight(100, 100).setInsets(5));

			Border etched = BorderFactory.createEtchedBorder();
			setBorder(BorderFactory.createTitledBorder(etched, "���߻�����Ϣ"));
		}
	}
	
	/**
	 * �޸�����ļ�������
	 */
	class  PasswordModifyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//�����޸İ�ť
			if(editButton==e.getSource()){
				newPassword.setEditable(true);
				rePassword.setEditable(true);
				editButton.setEnabled(false);
				clearButton.setEnabled(true);
				subButton.setEnabled(true);
			}
			//����ȡ����ť
			if(clearButton==e.getSource()){
				newPassword.setEditable(false);
				rePassword.setEditable(false);
				clearButton.setEnabled(false);
				subButton.setEnabled(false);
				editButton.setEnabled(true);
			}
			//�����ύ��ť
			if(subButton==e.getSource()){			
				String newPass = new String(newPassword.getPassword());
				if(null==newPass||"".equals(newPass.trim())){
					JOptionPane.showMessageDialog(null, "�����벻��Ϊ��!");
					return;
				}
				else if(newPass.length()<6||newPass.length()>20){
					JOptionPane.showMessageDialog(null, "�����볤�ȱ�����6-20֮��!");
					return;
				}
				String rePass = new String(rePassword.getPassword());
				if(null==rePass||"".equals(rePass.trim())){
					JOptionPane.showMessageDialog(null, "�ظ����벻��Ϊ��!");
					return;
				}
				else if(rePass.length()<6||rePass.length()>20){
					JOptionPane.showMessageDialog(null, "�ظ����볤�ȱ�����6-20֮��!");
					return;
				}
				if(!(newPass.equals(rePass))){
					JOptionPane.showMessageDialog(null, "�����������벻һ��!");
					return;
				}
				log("׼����ʼ�޸�����");
				newPassword.setEditable(false);
				rePassword.setEditable(false);
				subButton.setEnabled(false);
				clearButton.setEnabled(false);
				boolean modify = libClient.modifyPassword("reader",readerInfo.getReadid(),rePass);
				if(modify){
					JOptionPane.showMessageDialog(null, "�����޸ĳɹ�");					
					newPassword.setText(null);
					rePassword.setText(null);
				}
				else{
					JOptionPane.showMessageDialog(null, "�����޸�ʧ��");
				}
				editButton.setEnabled(true);
			}
		}
	}
}