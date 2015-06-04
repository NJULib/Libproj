package clientside;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import util.GBC;

public class BookMaintainPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame mainFrame;
	private LibClient libClient;
	//private LibraianInfo libInfo;

	public BookMaintainPanel(MainFrame mainFrame, LibClient libClient) {
		this.mainFrame = mainFrame;
		this.libClient = libClient;
		//this.libInfo = libInfo;
		//setTitle("ͼ��ά�������");
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		leftPanel.setBorder(BorderFactory
				.createTitledBorder(etched, "ͼ��ά��ѡ���"));
		// 1.�������
		Icon newbookIcon = new ImageIcon(this.getClass().getResource(
				"images/newbook.png"));
		JButton newbookinButton = new JButton("�������");
		newbookinButton.setActionCommand("newbookinButton");
		newbookinButton.addActionListener(this);
		if (newbookIcon != null) {
			newbookinButton.setIcon(newbookIcon);
		}
		leftPanel.add(newbookinButton, new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		// 2.ͼ���ѯ
		Icon searchbookIcon = new ImageIcon(this.getClass().getResource(
				"images/query.png"));
		JButton booksearchButton = new JButton("ͼ���ѯ");
		booksearchButton.setActionCommand("booksearchButton");
		booksearchButton.addActionListener(this);
		if (searchbookIcon != null) {
			booksearchButton.setIcon(searchbookIcon);
		}
		leftPanel.add(booksearchButton, new GBC(0, 1).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		// 3.ͼ���޸�
		Icon modifyIcon = new ImageIcon(this.getClass().getResource(
				"images/modifybook.png"));
		JButton bookmodifyButton = new JButton("ͼ���޸�");
		bookmodifyButton.setActionCommand("bookmodifyButton");
		bookmodifyButton.addActionListener(this);
		if (modifyIcon != null) {
			bookmodifyButton.setIcon(modifyIcon);
		}
		leftPanel.add(bookmodifyButton, new GBC(0, 2).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		// 4.����ͼ��
		Icon overIcon = new ImageIcon(this.getClass().getResource(
				"images/overdue.png"));
		JButton overduebookButton = new JButton("����ͼ��");
		overduebookButton.setActionCommand("overduebookButton");
		overduebookButton.addActionListener(this);
		if (overIcon != null) {
			overduebookButton.setIcon(overIcon);
		}
		leftPanel.add(overduebookButton, new GBC(0, 3).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));

		// 5.ͼ��ͳ��
		Icon countIcon = new ImageIcon(this.getClass().getResource(
				"images/statistics.png"));
		JButton bookcountButton = new JButton("ͼ��ͳ��");
		bookcountButton.setActionCommand("bookcountButton");
		bookcountButton.addActionListener(this);
		if (countIcon != null) {
			bookcountButton.setIcon(countIcon);
		}
		leftPanel.add(bookcountButton, new GBC(0, 4).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
//		// 6.ͼ����ʧ
//		Icon lostIcon = new ImageIcon(this.getClass().getResource(
//				"images/lostbook.png"));
//
//		JButton booklostButton = new JButton("ͼ����ʧ");
//		booklostButton.setActionCommand("booklostButton");
//		booklostButton.addActionListener(this);
//		if (lostIcon != null) {
//			booklostButton.setIcon(lostIcon);
//		}
//		leftPanel.add(booklostButton, new GBC(0, 5).setFill(GBC.BOTH)
//				.setWeight(100, 100).setInsets(5));
		// 7.�������
		Icon oldIcon = new ImageIcon(this.getClass().getResource(
				"images/storage.png"));
		JButton oldbookoutButton = new JButton("�������");
		oldbookoutButton.setActionCommand("oldbookoutButton");
		oldbookoutButton.addActionListener(this);
		if (oldIcon != null) {
			oldbookoutButton.setIcon(oldIcon);
		}
		leftPanel.add(oldbookoutButton, new GBC(0, 5).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		add(leftPanel, BorderLayout.WEST);

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"ͼ��ά����Ҫ˵����"));
		mainPanel.setLayout(new BorderLayout());
		String text = "<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>һ.������⣺</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;����Աָ�����ͼ���<font color='green'>�ݲص�</font>��ͬʱָ��<font color='green'>�������</font>����⵽�ض��ݲصص�<font color='green'>��ʼ������ֵ</font>�����ɹ�֮��ÿ��ͼ�����������Զ����ɣ���Χ��<font color='green'>��ʼֵ--->��ʼֵ+�������</font>��<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>��.ͼ���ѯ��</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ͼ���ѯ�����ṩ<font color='green'>ģ����ѯ</font>������Ա���Ը�������������ѯͼ�����ϸ��Ϣ��ͼ��Ĺݲ���Ϣ��<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>��.ͼ���޸ģ�</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;������Ա�������ͼ�����ϸ��Ϣ�в��ʱ������ͨ��<font color='green'>ͼ������</font>����<font color='green'>��׼ISBN</font>��ѯ����Ҫ�޸ĵ�ͼ�飬Ȼ���޸�ͼ����Ӧ�����Ϣ��<font color='black'><u>���ǣ�����������������Աֻ�ܽ����ͼ��ȫ�����⣬Ȼ���ٴ������</u></font>��<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>��.����ͼ�飺</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�ù�����Ҫ����ͼ��Ĳ�ѯ���磺���г���ͼ�����ϸ��Ϣ�ͽ����ߵĻ�����Ϣ��<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>��.ͼ��ͳ�ƣ�</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ͳ��ͼ��ݲ��������ִ�ͼ�������ڽ�ͼ����������ͼ��������ʧͼ��������Ϣ��<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>��.������⣺</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ɾ��ָ����ͼ�����ĳЩ����������ͼ����ʵ��ͼ�������⡣</html>";
		JLabel messageLabel = new JLabel();
		messageLabel.setFont(new Font("serial", Font.BOLD, 14));
		messageLabel.setForeground(Color.BLUE);
		messageLabel.setText(text);
		mainPanel.add(messageLabel, BorderLayout.CENTER);
		add(mainPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		JButton close = new JButton("�رմ���");
		close.setActionCommand("close");
		close.addActionListener(this);
		bottomPanel.add(close);
		add(bottomPanel,BorderLayout.SOUTH);

		setSize(800, 600);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {// �������
		if ("newbookinButton" == e.getActionCommand()) {
			NewBookDialog newBookDialog = new NewBookDialog(mainFrame);
			newBookDialog.setVisible(true);
		} else if ("booksearchButton" == e.getActionCommand()) {// ͼ���ѯ
			BookSearchDialog bookSearchDialog = new BookSearchDialog(mainFrame);
			bookSearchDialog.setVisible(true);
		} else if ("bookmodifyButton" == e.getActionCommand()) {// ͼ���޸�
			BookModifyDialog bookModify = new BookModifyDialog(mainFrame);
			bookModify.setVisible(true);
		}else if ("bookcountButton" == e.getActionCommand()) {// ͼ��ͳ��
			BookCountDialog bookCountDialog = new BookCountDialog(mainFrame);
			bookCountDialog.setVisible(true);
		} else if ("overduebookButton" == e.getActionCommand()) {// ����ͼ��
			OverdueBookDialog overdueBookDialog = new OverdueBookDialog(mainFrame);
			overdueBookDialog.setVisible(true);
		} else if ("oldbookoutButton" == e.getActionCommand()) {// �������
			BookOutLibDialog bookOutLibDialog = new BookOutLibDialog(mainFrame);
			bookOutLibDialog.setVisible(true);
		}else if("close"==e.getActionCommand()){
			this.setVisible(false);
			mainFrame.tabbedPane.remove(mainFrame.bookMaintainPanel);
		}
	}
}
