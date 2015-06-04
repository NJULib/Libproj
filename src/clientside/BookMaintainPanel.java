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
		//setTitle("图书维护主面板");
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		leftPanel.setBorder(BorderFactory
				.createTitledBorder(etched, "图书维护选项卡："));
		// 1.新书入库
		Icon newbookIcon = new ImageIcon(this.getClass().getResource(
				"images/newbook.png"));
		JButton newbookinButton = new JButton("新书入库");
		newbookinButton.setActionCommand("newbookinButton");
		newbookinButton.addActionListener(this);
		if (newbookIcon != null) {
			newbookinButton.setIcon(newbookIcon);
		}
		leftPanel.add(newbookinButton, new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		// 2.图书查询
		Icon searchbookIcon = new ImageIcon(this.getClass().getResource(
				"images/query.png"));
		JButton booksearchButton = new JButton("图书查询");
		booksearchButton.setActionCommand("booksearchButton");
		booksearchButton.addActionListener(this);
		if (searchbookIcon != null) {
			booksearchButton.setIcon(searchbookIcon);
		}
		leftPanel.add(booksearchButton, new GBC(0, 1).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		// 3.图书修改
		Icon modifyIcon = new ImageIcon(this.getClass().getResource(
				"images/modifybook.png"));
		JButton bookmodifyButton = new JButton("图书修改");
		bookmodifyButton.setActionCommand("bookmodifyButton");
		bookmodifyButton.addActionListener(this);
		if (modifyIcon != null) {
			bookmodifyButton.setIcon(modifyIcon);
		}
		leftPanel.add(bookmodifyButton, new GBC(0, 2).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		// 4.超期图书
		Icon overIcon = new ImageIcon(this.getClass().getResource(
				"images/overdue.png"));
		JButton overduebookButton = new JButton("超期图书");
		overduebookButton.setActionCommand("overduebookButton");
		overduebookButton.addActionListener(this);
		if (overIcon != null) {
			overduebookButton.setIcon(overIcon);
		}
		leftPanel.add(overduebookButton, new GBC(0, 3).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));

		// 5.图书统计
		Icon countIcon = new ImageIcon(this.getClass().getResource(
				"images/statistics.png"));
		JButton bookcountButton = new JButton("图书统计");
		bookcountButton.setActionCommand("bookcountButton");
		bookcountButton.addActionListener(this);
		if (countIcon != null) {
			bookcountButton.setIcon(countIcon);
		}
		leftPanel.add(bookcountButton, new GBC(0, 4).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
//		// 6.图书遗失
//		Icon lostIcon = new ImageIcon(this.getClass().getResource(
//				"images/lostbook.png"));
//
//		JButton booklostButton = new JButton("图书遗失");
//		booklostButton.setActionCommand("booklostButton");
//		booklostButton.addActionListener(this);
//		if (lostIcon != null) {
//			booklostButton.setIcon(lostIcon);
//		}
//		leftPanel.add(booklostButton, new GBC(0, 5).setFill(GBC.BOTH)
//				.setWeight(100, 100).setInsets(5));
		// 7.旧书出库
		Icon oldIcon = new ImageIcon(this.getClass().getResource(
				"images/storage.png"));
		JButton oldbookoutButton = new JButton("旧书出库");
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
				"图书维护简要说明："));
		mainPanel.setLayout(new BorderLayout());
		String text = "<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>一.新书入库：</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;管理员指定入库图书的<font color='green'>馆藏地</font>，同时指定<font color='green'>入库数量</font>和入库到特定馆藏地的<font color='green'>起始条形码值</font>，入库成功之后，每本图书的条形码会自动生成，范围：<font color='green'>起始值--->起始值+入库数量</font>。<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>二.图书查询：</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;图书查询功能提供<font color='green'>模糊查询</font>，管理员可以根据任意条件查询图书的详细信息和图书的馆藏信息。<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>三.图书修改：</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当管理员发现入库图书的详细信息有差错时，可以通过<font color='green'>图书条码</font>或者<font color='green'>标准ISBN</font>查询出想要修改的图书，然后修改图书相应项的信息；<font color='black'><u>但是，若条形码错误，则管理员只能将入库图书全部出库，然后再从新入库</u></font>！<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>四.超期图书：</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;该功能主要超期图书的查询，如：所有超期图书的详细信息和借阅者的基本信息。<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>五.图书统计：</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;统计图书馆藏总数、现存图书数、在借图书数、超期图书数、丢失图书数等信息。<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>六.旧书出库：</font><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;删除指定的图书或者某些条形码错误的图书来实现图书从新入库。</html>";
		JLabel messageLabel = new JLabel();
		messageLabel.setFont(new Font("serial", Font.BOLD, 14));
		messageLabel.setForeground(Color.BLUE);
		messageLabel.setText(text);
		mainPanel.add(messageLabel, BorderLayout.CENTER);
		add(mainPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		JButton close = new JButton("关闭窗口");
		close.setActionCommand("close");
		close.addActionListener(this);
		bottomPanel.add(close);
		add(bottomPanel,BorderLayout.SOUTH);

		setSize(800, 600);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {// 新书入库
		if ("newbookinButton" == e.getActionCommand()) {
			NewBookDialog newBookDialog = new NewBookDialog(mainFrame);
			newBookDialog.setVisible(true);
		} else if ("booksearchButton" == e.getActionCommand()) {// 图书查询
			BookSearchDialog bookSearchDialog = new BookSearchDialog(mainFrame);
			bookSearchDialog.setVisible(true);
		} else if ("bookmodifyButton" == e.getActionCommand()) {// 图书修改
			BookModifyDialog bookModify = new BookModifyDialog(mainFrame);
			bookModify.setVisible(true);
		}else if ("bookcountButton" == e.getActionCommand()) {// 图书统计
			BookCountDialog bookCountDialog = new BookCountDialog(mainFrame);
			bookCountDialog.setVisible(true);
		} else if ("overduebookButton" == e.getActionCommand()) {// 超期图书
			OverdueBookDialog overdueBookDialog = new OverdueBookDialog(mainFrame);
			overdueBookDialog.setVisible(true);
		} else if ("oldbookoutButton" == e.getActionCommand()) {// 旧书出库
			BookOutLibDialog bookOutLibDialog = new BookOutLibDialog(mainFrame);
			bookOutLibDialog.setVisible(true);
		}else if("close"==e.getActionCommand()){
			this.setVisible(false);
			mainFrame.tabbedPane.remove(mainFrame.bookMaintainPanel);
		}
	}
}
