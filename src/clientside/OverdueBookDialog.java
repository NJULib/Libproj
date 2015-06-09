package clientside;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import serverside.entity.BookDetails;
import serverside.entity.ReaderInfo;
import util.GBC;

public class OverdueBookDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DefaultTableModel model;

	private JTable bookTable;
	private JButton printButton;
	private JButton exitButton;
//	public static void main(String [] args){
//		new OverdueBookDialog();
//	}
	public OverdueBookDialog(MainFrame mainFrame) {
		super((JFrame)SwingUtilities.getAncestorOfClass(JFrame.class,mainFrame));
		setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar("ToolBar");
		toolBar.setFloatable(false);
		printButton = new JButton("打印", new ImageIcon(this.getClass().getResource("images/print.png")));
		printButton.setBorderPainted(false);
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					bookTable.print();
				} catch (java.awt.print.PrinterException exception) {
					System.out.println("打印出错");
				}
			}
		});
		exitButton = new JButton("退出", new ImageIcon(this.getClass().getResource("images/exit.png")));
		exitButton.setBorderPainted(false);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
//				dispose();
//				System.exit(0);
			}
		});
		toolBar.add(printButton);
		toolBar.addSeparator();
		toolBar.add(exitButton);
		add(toolBar, BorderLayout.NORTH);
		Border etched = BorderFactory.createEtchedBorder();
		String[] columnNames = new String[] { "图书编号", "标准ISBN", "图书名称", "出版社", "读者编号",
				 "读者姓名", "系别","学院", "应还日期" };
		model = new DefaultTableModel(columnNames,0);
		bookTable = new JTable(model);
		bookTable.setEnabled(false);
		bookTable.setGridColor(Color.BLUE);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(etched, "超期图书"));
		panel.add(new JScrollPane(bookTable), new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));

		add(panel, BorderLayout.CENTER);
		ServerInfo serverInfo = new ServerInfo();
		LibClient libClient = new LibClient(serverInfo.getHost(),serverInfo.getPort());
		List<String> blist = libClient.getOverDuBooks();
		Object [] obj = new Object[9];
		for(String strList : blist){
			String [] s = strList.split(":");
			String str = s[0];
			String duedate = s[1];
			System.out.println("超期图书条码："+str);
			obj[0] = str;
			BookDetails bookDetails = libClient.getBookDetails(str);
			System.out.println(bookDetails.getIsbn());
			obj[1] = bookDetails.getIsbn();
			System.out.println(bookDetails.getName());
			obj[2] = bookDetails.getName();
			System.out.println(bookDetails.getPublisher());
			obj[3] =bookDetails.getPublisher();
			ReaderInfo readerInfo = libClient.getBarReader(str);
			System.out.println(readerInfo.getReadid());
			obj[4] =readerInfo.getReadid();
			System.out.println(readerInfo.getName());
			obj[5] =readerInfo.getName();
			System.out.println(readerInfo.getMajor());
			obj[6] =readerInfo.getMajor();
			System.out.println(readerInfo.getDepart());
			obj[7] =readerInfo.getDepart();
			System.out.println(duedate);
			obj[8] =duedate;
			model.addRow(obj);
		}
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
//		addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent e){
//				setVisible(false);
//				dispose();
//				System.exit(0);
//			}
//		});
		setVisible(false);
	}

}