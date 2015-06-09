package clientside;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
import util.LibProtocals;

public class ReaderDeleteDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField readeridField;
	private LibClient	libClient ;
	private DefaultTableModel outReaderModel;
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		new ReaderDeleteDialog(new JFrame());
//	}
	public ReaderDeleteDialog(MainFrame jFrame){
		super(jFrame,"ͼ�����");
		setLayout(new BorderLayout());
		Border border = BorderFactory.createEtchedBorder();
		ServerInfo serverInfo = new ServerInfo();
		libClient = new LibClient(serverInfo.getHost(),serverInfo.getPort());
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());
		JPanel messagePanel = new JPanel();
		messagePanel.setBorder(BorderFactory.createTitledBorder(border,"������ʾ��"));
		messagePanel.add(new JLabel("<html>��������Ҫɾ�����ߵ�<font color='red'>���</font>��ִ<br/>��ɾ����ɾ���ɹ���ɾ���Ķ�<br/>����Ϣ����ʾ���·�</html>"));
		
		JPanel idPanel = new JPanel();
		idPanel.setBorder(BorderFactory.createTitledBorder(border,"ɾ�����ߵı�ţ�"));
		idPanel.setLayout(new GridBagLayout());
		idPanel.add(new JLabel("������ɾ���Ķ��ߵı��:"), new GBC(0, 0).setFill(
				GBC.EAST).setInsets(5));
		readeridField = new JTextField(20);		
		idPanel.add(readeridField,new GBC(1, 0).setFill(
				GBC.EAST).setInsets(5));
		JButton out = new JButton("ɾ��");
		out.addActionListener(new BookOutActionListener());
		idPanel.add(out,new GBC(2, 0).setFill(
				GBC.EAST).setInsets(5));
		topPanel.add(messagePanel,new GBC(0, 0,2,2).setFill(
				GBC.BOTH).setInsets(5));
		topPanel.add(idPanel,new GBC(2, 0,3,2).setFill(
				GBC.BOTH).setInsets(5));
		add(topPanel,BorderLayout.NORTH);
		JPanel mainPanel = new  JPanel();
		mainPanel.setLayout(new GridBagLayout());
		String[] name = {"���", "����","����", "�Ա�", "��ͥסַ", "��ϵ�绰", "ע������", "����ʱ��","��������","רҵ","Ժϵ" };
		outReaderModel = new DefaultTableModel(name,0);
		JTable outTable = new JTable(outReaderModel);
		outTable.setGridColor(Color.BLUE);
		outTable.setEnabled(false);
		mainPanel.add(new JScrollPane(outTable),new GBC(0, 0).setFill(GBC.BOTH).setWeight(100, 100)
				.setInsets(5));
		mainPanel.setBorder(BorderFactory.createTitledBorder(border, "ɾ���Ķ��ߵ���ϸ��Ϣ��"));
		
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
			String readerid = readeridField.getText().trim();
			if(null==readerid||"".equals(readerid.trim())){
				JOptionPane.showMessageDialog(null, "���߱�Ų���Ϊ�գ�");
				return;
			}
			int rows = outReaderModel.getRowCount();
			while(rows>0){
				rows = rows -1;
				outReaderModel.removeRow(rows);
			}
			ReaderInfo readerInfo =(ReaderInfo) libClient.getName(LibProtocals.OP_GET_READERINFO, readerid, "nullpass");
			if (null == readerInfo.getName()) {
				JOptionPane.showMessageDialog(null, "û�м�����ָ�����߱��     "+readerid+"  �Ķ��ߣ�");
				return;
			}
			boolean mark = libClient.delReader(readerid);
			if(mark){
				Object[] obj = new Object[11];
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
				outReaderModel.addRow(obj);
			}
			else{
				System.out.println("ͼ�����ʧ��");
			}
		}
	}
}
