package clientside;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import util.GBC;

public class BookOutLibDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField isbnField;
	private LibClient	libClient ;
	private DefaultTableModel outlibBookModel;
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		new BookOutLibInfo(new JFrame());
//	}
	public BookOutLibDialog(MainFrame frame){
		super(frame,"ͼ�����");
		setLayout(new BorderLayout());
		Border border = BorderFactory.createEtchedBorder();
		ServerInfo serverInfo = new ServerInfo();
		libClient = new LibClient(serverInfo.getHost(),serverInfo.getPort());
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());
		JPanel messagePanel = new JPanel();
		messagePanel.setBorder(BorderFactory.createTitledBorder(border,"������ʾ��"));
		messagePanel.add(new JLabel("<html>��������Ҫ�����ͼ��<br/>��<font color='red'>ISBN��</font>��ִ�г��⣬������<br/>�������ͼ�����ϸ��Ϣ����ʾ<br/>���·�</html>"));
		
		JPanel isbnPanel = new JPanel();
		isbnPanel.setBorder(BorderFactory.createTitledBorder(border,"����ͼ��ISBN�ţ�"));
		isbnPanel.setLayout(new GridBagLayout());
		isbnPanel.add(new JLabel("��������Ҫ�����ͼ���ISBN:"), new GBC(0, 0).setFill(
				GBC.EAST).setInsets(5));
		isbnField = new JTextField(20);		
		isbnPanel.add(isbnField,new GBC(1, 0).setFill(
				GBC.EAST).setInsets(5));
		JButton out = new JButton("����");
		out.addActionListener(new BookOutActionListener());
		isbnPanel.add(out,new GBC(2, 0).setFill(
				GBC.EAST).setInsets(5));
		topPanel.add(messagePanel,new GBC(0, 0,2,2).setFill(
				GBC.BOTH).setInsets(5));
		topPanel.add(isbnPanel,new GBC(2, 0,3,2).setFill(
				GBC.BOTH).setInsets(5));
		add(topPanel,BorderLayout.NORTH);
		JPanel mainPanel = new  JPanel();
		mainPanel.setLayout(new GridBagLayout());
		String [] clumns = { "ISBN���", "����", "������", "����", "������", "����", "ҳ��",
				"����", "���", "�����" };
		outlibBookModel = new DefaultTableModel(clumns,0);
		JTable outTable = new JTable(outlibBookModel);
		outTable.setGridColor(Color.BLUE);
		outTable.setEnabled(false);
		mainPanel.add(new JScrollPane(outTable),new GBC(0, 0).setFill(GBC.BOTH).setWeight(100, 100)
				.setInsets(5));
		mainPanel.setBorder(BorderFactory.createTitledBorder(border, "����ͼ�����ϸ��Ϣ��"));
		
		add(mainPanel,BorderLayout.CENTER);
		setSize(800,600);
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
	//ͼ�����
	class BookOutActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String isbn = isbnField.getText().trim();
			if(null==isbn||"".equals(isbn.trim())){
				JOptionPane.showMessageDialog(null, "ͼ��ISBN�Ų���Ϊ�գ�");
				return;
			}
			int rows = outlibBookModel.getRowCount();
			while(rows>0){
				rows = rows -1;
				outlibBookModel.removeRow(rows);
			}
			BookDetails bookDetails = libClient.getBookDetailsByIsbn(isbn);
			if (null == bookDetails.getName()) {
				JOptionPane.showMessageDialog(null, "û�м�����ָ��ISBN��     "+isbn+"  ��ͼ�飡");
				return;
			}
			boolean mark = libClient.outLib(isbn);
			if(mark){
				Object[] obj = new Object[10];
				 obj[0] = isbn;
				 obj[1] = bookDetails.getName();
				 obj[2] = bookDetails.getSeries();
				 obj[3] = bookDetails.getAuthors();
				 obj[4] = bookDetails.getPublisher();
				 obj[5] = bookDetails.getSize();
				 obj[6] = bookDetails.getPages();
				 obj[7] = bookDetails.getPrice();
				 obj[8] = bookDetails.getIntroduction();
				 obj[9] = bookDetails.getClnum();
				 outlibBookModel.addRow(obj);
			}
			else{
				System.out.println("ͼ�����ʧ��");
			}
		}
	}
}
