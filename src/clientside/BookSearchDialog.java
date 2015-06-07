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
	private MainFrame mainFrame; // ��frame
	private List listBooks = null; // ���ͼ����Ϣ
	private LibClient libClient; // �ͻ�����
	private LibraianInfo libInfo; // ����Ա��Ϣ
	private JButton printButton; // ��ӡ������ť
	private JButton exitButton; // �˳�������ť
	private JTextField searchCondition; // ��ѯ������ť
	private JButton searchButton; // ִ�в�ѯ��ť
	private DefaultTableModel tableModel; // ���ģ��
	private JTable bookTable; // ���

	// public static void main(String[] args) {
	// new BookSearchDialog();
	// }

	public BookSearchDialog(MainFrame mainFrame) {
		super(mainFrame,"ͼ���ѯ");
		this.mainFrame = mainFrame;
		ServerInfo serverInfo = new ServerInfo();
		libClient = new LibClient(serverInfo.getHost(),serverInfo.getPort());
		setTitle("ͼ���ѯ");
		setLayout(new BorderLayout());
		
		Border etched = BorderFactory.createEtchedBorder();

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridBagLayout());
		searchPanel.setBorder(BorderFactory.createTitledBorder(etched, "��ѯ����"));
		searchCondition = new JTextField(10);
		searchCondition.addKeyListener(new KeyBoardListener());
		searchButton = new JButton("��ʼ��ѯ");
		searchButton.addActionListener(new BookSearchActionListener());
		searchPanel.add(new JLabel("����������Ҫ��ѯ������: "), new GBC(0, 1).setFill(
				GBC.EAST).setInsets(5));
		searchPanel.add(searchCondition, new GBC(1, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 100).setInsets(5));

		searchPanel.add(searchButton, new GBC(2, 1).setFill(GBC.WEST)
				.setInsets(5));

		JPanel messagePanel = new JPanel();
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "��ѯ˵��"));
		messagePanel.add(new JLabel("<html>�������ѯ����:֧��ģ����ѯ��<br/></html>"));
		String[] name = { "ISBN���", "����", "������", "����", "������", "����", "ҳ��",
				"����", "���", "�����" };
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
		panel.setBorder(BorderFactory.createTitledBorder(etched, "��ѯ����б�"));
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
		 * �����������
		 */
		String text = searchCondition.getText();
		if (null == text || "".equals(text.trim())) {
			JOptionPane.showMessageDialog(null, "������������Ϊ�գ�");
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
			JOptionPane.showMessageDialog(null, "û�м�������Ҫ����Ϣ��");
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
	 *��ѯͼ����Ϣ
	 */
	class BookSearchActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			execCheck();
		}
	}

	/**
	 * �����¼�
	 * 
	 */
	//Enter���ļ�������
	class KeyBoardListener extends KeyAdapter {
			public void keyPressed(KeyEvent e) {
				// ��ü�����ĳ�����ļ�����ü������¡��û������ͷ�
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// public char getKeyChar()�ж��Ǹ��������¡��û����ͷ�
					// �÷������ؼ����ϵ��ַ�
					//JOptionPane.showMessageDialog(null, "hello");
					execCheck();
				}
			}
		}

	private void log(Object obj) {
		System.out.println("BookSearchDialog�ࣺ"+obj);
	}
}
