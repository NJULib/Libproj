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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import serverside.entity.ReaderInfo;
import util.GBC;


public class ReaderMaintainPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame mainFrame;
	private JTextField maintainCondition;
	
	private DefaultTableModel tableModel ;
	private JTable jtable ;
	private List listReaders ;
	private LibClient libClient;
//	private LibraianInfo libInfo;
//	public static void main(String [] args){
//		new ReaderMaintainPanel(new JFrame());
//	}
	public ReaderMaintainPanel(MainFrame mainFrame,LibClient libClient){
		//super(mainFrame,"ͼ���ѯ");
		this.mainFrame = mainFrame;
		this.libClient = libClient;
		//ServerInfo serverInfo = new ServerInfo();
		//libClient = new LibClient(serverInfo.getHost(),serverInfo.getPort());
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		/**
		 * ����
		 */
		JPanel topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createTitledBorder(etched, "�鿴������Ϣ��"));
		topPanel.add(new JLabel("������Ҫ��ѯ�Ķ������ݣ�֧��ģ����ѯ����"));
		maintainCondition = new JTextField(20);
		maintainCondition.addMouseListener(new TextSelectedMouseListener());
		maintainCondition.addKeyListener(new SelectionTextKeyListener());
		JButton check = new JButton("��ѯ");
		check.setActionCommand("check");
		check.addActionListener(this);
		topPanel.add(maintainCondition);
		topPanel.add(check);
		add(BorderLayout.NORTH, topPanel);
		/**
		 * �в�
		 */
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBorder(BorderFactory.createTitledBorder(etched, "������ϸ��Ϣ��"));
		
		String[] name = {"���", "����","����", "�Ա�", "��ͥסַ", "��ϵ�绰", "ע������", "����ʱ��","��������","רҵ","Ժϵ" };
		tableModel = new DefaultTableModel(name,0);		
		jtable = new JTable(tableModel);
		jtable.setGridColor(Color.BLUE);
		jtable.setEnabled(false);
		jtable.setSelectionBackground(Color.orange);
		mainPanel.add(new JScrollPane(jtable),new GBC(0, 0).setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));
		add(BorderLayout.CENTER, mainPanel);
		
		JPanel bottomPanel = new JPanel();
		JButton modify = new JButton("�����޸�");
		modify.setActionCommand("modify");
		modify.addActionListener(this);
		bottomPanel.add(modify);
		JButton register = new JButton("����ע��");
		register.setActionCommand("register");
		register.addActionListener(this);
		bottomPanel.add(register);
		JButton out = new JButton("����ɾ��");
		out.setActionCommand("out");
		out.addActionListener(this);
		bottomPanel.add(out);
		JButton close = new JButton("�رմ���");
		close.setActionCommand("close");
		close.addActionListener(this);
		bottomPanel.add(close);
		add(BorderLayout.SOUTH,bottomPanel);
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

	@Override
	public void actionPerformed(ActionEvent event) {
		if("check".equals(event.getActionCommand().trim())){
			execCheck();
		}else if("modify".equals(event.getActionCommand().trim())){
			//JOptionPane.showMessageDialog(null, "�޸Ķ�����Ϣ");
			ReaderInfoModifyDialog readerInfoModify = new ReaderInfoModifyDialog(mainFrame);
			readerInfoModify.setVisible(true);
		}else if("register".equals(event.getActionCommand().trim())){
			//JOptionPane.showMessageDialog(null, "ע�������Ϣ");
			ReaderRegisterDialog readerRegisterDialog = new ReaderRegisterDialog(mainFrame);
			readerRegisterDialog.setVisible(true);
		}else if("out".equals(event.getActionCommand().trim())){
			//JOptionPane.showMessageDialog(null, "ɾ��������Ϣ");
			ReaderDeleteDialog readerDeleteDialog = new ReaderDeleteDialog(mainFrame);
			readerDeleteDialog.setVisible(true);
		}else if("close".equals(event.getActionCommand())){
			setVisible(false);
			mainFrame.tabbedPane.remove(mainFrame.readerMaintainPanel);
		}
			
	}

	protected void execCheck() {
		String text = maintainCondition.getText();
		if (null == text || "".equals(text.trim())) {
			text = "all";
		}
		listReaders = libClient.getAllInfos("readerdata",text);
		drawResults(listReaders);
		// System.out.println((ReaderInfo)listBooks.get(2));
	}

	private void drawResults(List list) {
		int rows = tableModel.getRowCount();
		while(rows>0){
			rows = rows - 1;
			tableModel.removeRow(rows);
		}
		if (0 == list.size()) {
			JOptionPane.showMessageDialog(null, "û�м�������Ҫ����Ϣ��");
			return;
		}
		int rowNum = list.size();
		Object[] obj = new Object[11];
		for (int i = 0; i < rowNum; i++) {
			ReaderInfo readerInfo = (ReaderInfo) list.get(i);
			obj[0] = readerInfo.getReadid();
			obj[1] = readerInfo.getName();
			obj[2]  = readerInfo.getAge();
			obj[3] = readerInfo.getGender();
			obj[4] = readerInfo.getAddress();
			obj[5] = readerInfo.getTel();
			obj[6] = readerInfo.getStartdate();
			obj[7] = readerInfo.getEnddate();
			obj[8] = readerInfo.getType();
			obj[9] = readerInfo.getMajor();
			obj[10] = readerInfo.getDepart();
			tableModel.addRow(obj);
		}
	}

	private class SelectionTextKeyListener extends  KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			// ��ü�����ĳ�����ļ�����ü������¡��û������ͷ�
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// public char getKeyChar()�ж��Ǹ��������¡��û����ͷ�
				// �÷������ؼ����ϵ��ַ�
				execCheck();
			}
		}
	}

	private class TextSelectedMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent event) {
			if (event.getClickCount() == 2) {
				// ѡ�еĿ�ʼλ��
				maintainCondition.setSelectionStart(0);
				// ѡ�еĽ���λ��
				maintainCondition.setSelectionEnd(maintainCondition.getText()
						.length());
				// JOptionPane.showMessageDialog(null,
				// maintainCondition.getText());
			}
		}
	}

	private void log(Object obj) {
		System.out.println(obj);
	}
}
