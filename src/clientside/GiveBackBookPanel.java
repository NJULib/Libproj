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
	private JButton returnButton;//����
	protected BorrowInfo borrowInfo;
	private JLabel readerLabel;//��ʾ����Ķ�����Ϣ

	public GiveBackBookPanel(MainFrame mainFrame, LibClient libClient) {
		this.mainFrame = mainFrame;
		this.libClient = libClient;
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		JPanel messagePanel = new JPanel();
		messagePanel.setBorder(BorderFactory.createTitledBorder(etched, "˵����"));
		messagePanel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;����Ա����<font color='red'>ͼ������</font>�黹ͼ��!</html>"));
		// ͼ��黹���ϲ�
		JPanel bookInfoPanel = new JPanel();
		barCodeText = new JTextField(20);
		barCodeText.addKeyListener(new ReaderIDKeyListener());
		returnButton = new JButton("����");
		returnButton.addActionListener(new ReturnBookActionListener());
		
		bookInfoPanel.setLayout(new GridBagLayout());
		bookInfoPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"�黹ͼ�飺"));
		bookInfoPanel.add(new JLabel("ͼ������: "), new GBC(0, 0).setAnchor(
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
		// �в�����ʾ���ĸ�������ͼ��Ķ��ߵ���Ϣ(������Ϣ�ͽ�����Ϣ)
		JPanel readerPanel = new JPanel();
		readerPanel.setLayout(new GridBagLayout());
		readerPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"����ͼ��Ķ�����Ϣ��"));
		readerLabel = new JLabel();
		readerLabel.setText("û�ж���");
		readerPanel.add(readerLabel);

		// �黹��ͼ���б�
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridBagLayout());
		tablePanel.setBorder(BorderFactory
				.createTitledBorder(etched, "���߻�����Ϣ��"));
		String[] columnNames = { "���߱��","ͼ������", "ͼ������", "ͼ��۸�", "��������","Ӧ������","��������","��������","������" };
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
		JButton close = new JButton("�رմ���");
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
	 * �س�ȷ��
	 */
	class ReaderIDKeyListener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				processReturnBook();
			}
		}
	}

	/**
	 * �黹ͼ�鰴ť������
	 */
	class ReturnBookActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			processReturnBook();
		}
	}
	void processReturnBook() {
		barCode = barCodeText.getText();
		if(null==barCode||"".equals(barCode.trim())){
			JOptionPane.showMessageDialog(null, "�������벻��Ϊ�գ�");
			return;
		}
		if(!barCode.matches("\\d+")){
			JOptionPane.showMessageDialog(null, "���������ʽ�������");
			return;
		}
		//���ָ������ͼ���Ƿ����
		boolean exists = libClient.checkExists(barCode);
		if(!exists){
			JOptionPane.showMessageDialog(null, "�ݲ��޴������ͼ�飬���ѯ���ٴ����룡");
			return;
		}
		boolean isBorrowed = libClient.checkIsBorrowed(barCode);
		if(isBorrowed){
			JOptionPane.showMessageDialog(null, "�������ͼ�����ڹݲ��У�");
			return;
		}
		//����ָ������ͼ��Ķ�����Ϣ
		ReaderInfo readerInfo = libClient.getBarReader(barCode);
		if(null==readerInfo.getName()){
			JOptionPane.showMessageDialog(null, "�鿴����ָ������ͼ����ߴ��󣬸ö�����Ϣ�����ѱ�ɾ����");
		}
		int type = readerInfo.getType();
		int account = libClient.getAccount(type);
		String readerid = readerInfo.getReadid();
		List [] borrowList = libClient.getReaderBorrowInfo(readerid);
		String readertext = "<html>&nbsp;&nbsp;ѧ�ţ�<font color='red'>"+readerid+"</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;������<font color='red'>"+readerInfo.getName()+"</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;רҵ/Ժϵ��<font color='red'>"+readerInfo.getMajor()+"/"+readerInfo.getDepart()+"</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��ǰ���ģ�<font color='red'>"+(borrowList[0].size()-1)+"/"+account+"</font></html>";
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
	}//"���߱��","ͼ������", "ͼ������", "ͼ��۸�", "��������","Ӧ������","��������","��������","������" 
}