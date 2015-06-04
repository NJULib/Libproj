package clientside;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.IOException;
import javax.swing.table.TableColumnModel;
import serverside.entity.BookDetails;
import serverside.entity.BookInLibrary;
import util.CurrDateTime;

public class BookDetailsDialog extends JDialog {

	private static final long serialVersionUID = 4649094248915733473L;

	protected BookDetails book;
	protected Frame parent;
	protected LibClient libClient;
	protected ArrayList bookInLibArray;
	protected JTable bookInLibTable;

	public BookDetailsDialog(Frame theParentFrame, BookDetails theBook,
			LibClient theLibClient) {

		this(theParentFrame, "图书详细信息 " + theBook.toString(), theBook,
				theLibClient);
	}

	public BookDetailsDialog(Frame theParentFrame, String theTitle,
			BookDetails theMusicRecording, LibClient theLibClient) {

		super(theParentFrame, theTitle, true);

		book = theMusicRecording;
		parent = theParentFrame;
		libClient = theLibClient;
		buildGUI();
		this.pack();

		// Point parentLocation = parent.getLocation();
		// this.setLocation(parentLocation.x + 50, parentLocation.y + 50);

		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension framesize = this.getSize();
		int x = (int) screensize.getWidth() / 2 - (int) framesize.getWidth()
				/ 2;
		int y = (int) screensize.getHeight() / 2 - (int) framesize.getHeight()
				/ 2;
		this.setLocation(x, y);
	}

	private void buildGUI() {

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		// 图书详细数据
		JPanel bookDataPanel = new JPanel();
		bookDataPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 10);
		JLabel nameLabel = new JLabel("书名:", JLabel.RIGHT);
		nameLabel.setForeground(Color.blue);
		bookDataPanel.add(nameLabel, c);

		c.gridx = 1;
		c.gridy = 0;
		// c.gridwidth = 3;
		JLabel nameValue = new JLabel(book.getName());
		bookDataPanel.add(nameValue, c);

		c.gridx = 0;
		c.gridy = 1;
		// c.gridwidth = 1;
		JLabel authorsLabel = new JLabel("作者:", JLabel.RIGHT);
		authorsLabel.setForeground(Color.blue);
		bookDataPanel.add(authorsLabel, c);

		c.gridx = 1;
		c.gridy = 1;
		// c.gridwidth = 3;
		JLabel authorsValue = new JLabel(book.getAuthors());
		bookDataPanel.add(authorsValue, c);

		c.gridx = 0;
		c.gridy = 2;
		// c.gridwidth = 1;
		JLabel seriesLabel = new JLabel("丛书名:", JLabel.RIGHT);
		seriesLabel.setForeground(Color.blue);
		bookDataPanel.add(seriesLabel, c);

		c.gridx = 1;
		c.gridy = 2;
		// c.gridwidth = 3;
		JLabel seriesValue = new JLabel(book.getSeries());
		bookDataPanel.add(seriesValue, c);

		c.gridx = 0;
		c.gridy = 3;
		// c.gridwidth = 1;
		JLabel publisherLabel = new JLabel("出版发行:", JLabel.RIGHT);
		publisherLabel.setForeground(Color.blue);
		bookDataPanel.add(publisherLabel, c);

		c.gridx = 1;
		c.gridy = 3;
		// c.gridwidth = 3;
		JLabel publisherValue = new JLabel(book.getPublisher());
		bookDataPanel.add(publisherValue, c);

		c.gridx = 0;
		c.gridy = 4;
		// c.gridwidth = 1;
		JLabel mediaLabel = new JLabel("载体信息:", JLabel.RIGHT);
		mediaLabel.setForeground(Color.blue);
		bookDataPanel.add(mediaLabel, c);

		c.gridx = 1;
		c.gridy = 4;
		// c.gridwidth = 3;
		JLabel meidaValue = new JLabel(book.getPages() + "页，" + book.getSize()
				+ "，" + new DecimalFormat("#.0").format(book.getPrice()) + "元");
		bookDataPanel.add(meidaValue, c);

		c.gridx = 0;
		c.gridy = 5;
		// c.gridwidth = 1;
		JLabel clssNumLabel = new JLabel("中图分类:", JLabel.RIGHT);
		clssNumLabel.setForeground(Color.blue);
		bookDataPanel.add(clssNumLabel, c);

		c.gridx = 1;
		c.gridy = 5;
		// c.gridwidth = 3;
		JLabel clssNumValue = new JLabel(book.getClnum());
		bookDataPanel.add(clssNumValue, c);

		c.gridx = 0;
		c.gridy = 6;
		// c.gridheight = 3;
		JLabel introLabel = new JLabel("图书简介:", JLabel.RIGHT);
		introLabel.setForeground(Color.blue);
		bookDataPanel.add(introLabel, c);

		c.gridx = 1;
		c.gridy = 6;
		// c.gridheight = 3;
		JTextArea abstractInfo = new JTextArea(book.getIntroduction(), 3, 20);
		abstractInfo.setEditable(false);
		abstractInfo.setLineWrap(true);
		// JScrollPane abstractPane = new JScrollPane(abstractInfo);
		bookDataPanel.add(abstractInfo, c);

		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 8;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(5, 5, 10, 20);
		String imageName = book.getPicture();
		ImageIcon bookPicIcon = null;
		JLabel bookPicLabel = null;
		// 读取图书封面图片
		try {
			if (imageName.trim().length() == 0) {
				bookPicLabel = new JLabel(" 图片暂不存在  ");
				bookPicLabel.setForeground(Color.red);
			} else {
				bookPicIcon = new ImageIcon(getClass().getResource(
						"images/" + imageName));
				// recordingIcon = new ImageIcon("images/" + imageName);
				bookPicLabel = new JLabel(bookPicIcon);
			}
		} catch (Exception exc) {
			bookPicLabel = new JLabel(" 图片暂不存在  ");
			bookPicLabel.setForeground(Color.red);
		}

		bookPicLabel.setToolTipText(book.getName());
		bookDataPanel.add(bookPicLabel, c);

		container.add(BorderLayout.NORTH, bookDataPanel);
		try {
			bookInLibArray = libClient.getBookInLibrary(book.getIsbn());
		} catch (IOException e) {
			log("没找到相关信息");
		}
		int bookAvailable = 0;
		int bookTotal = bookInLibArray.size();

		String[] bookLendHead = { "图书条形码", "图书馆藏地", "图书状态" };
		String[][] bookLibInfo = new String[bookTotal][3];
		Iterator it = bookInLibArray.iterator();
		BookInLibrary bookInLibrary = null;
		String bookStatus;

		int i = 0;
		while (it.hasNext()) {
			bookInLibrary = (BookInLibrary) it.next();

			bookLibInfo[i][0] = bookInLibrary.getBarCode();
			bookLibInfo[i][1] = bookInLibrary.getLocation();
			switch (bookInLibrary.getStatus()) {
			case 0:
				bookStatus = "已借出，应还日期：\n" + bookInLibrary.getDueReturnDate();
				break;
			case 1:
				bookStatus = "可借";
				bookAvailable++;
				break;
			default:
				bookStatus = "不可借，其他情况";
			}
			bookLibInfo[i][2] = bookStatus;
			i++;
		}
		bookInLibTable = new JTable(bookLibInfo, bookLendHead);
		bookInLibTable.setEnabled(false);

		bookInLibTable
				.setPreferredScrollableViewportSize(new Dimension(0, 120));

		JScrollPane bookInLibScrollPane = new JScrollPane(bookInLibTable);

		// 根据每列内容的实际宽度设置每列的宽度
		// TableUtilities.fixTableColumnWidth(bookInLibTable);

		// 按表的中宽度设置每列的宽度
		double tableWidth = bookInLibTable.getPreferredSize().getWidth();
		TableColumnModel tcm = bookInLibTable.getColumnModel();
		tcm.getColumn(0).setPreferredWidth((int) (tableWidth / 6));
		tcm.getColumn(1).setPreferredWidth((int) (tableWidth / 3));
		tcm.getColumn(2).setPreferredWidth((int) (tableWidth / 2));

		TitledBorder tableTitleBorder = BorderFactory
				.createTitledBorder("图书馆藏情况");
		tableTitleBorder.setTitleColor(Color.black);
		bookInLibScrollPane.setBorder(tableTitleBorder);

		JPanel statisticPanel = new JPanel();
		statisticPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel totalLabel = new JLabel("图书总数量： ");
		totalLabel.setForeground(Color.black);
		JLabel totalValueLabel = new JLabel("" + bookTotal);
		totalValueLabel.setForeground(Color.blue);

		statisticPanel.add(totalLabel);
		statisticPanel.add(totalValueLabel);

		JLabel whiteSpace = new JLabel("        ");
		statisticPanel.add(whiteSpace);

		JLabel vailLabel = new JLabel("目前可借量： ");
		vailLabel.setForeground(Color.black);
		JLabel vailValueLabel = new JLabel("" + bookAvailable);
		vailValueLabel.setForeground(Color.red);
		statisticPanel.add(vailLabel);
		statisticPanel.add(vailValueLabel);

		JPanel bookInLibraryInfo = new JPanel();
		bookInLibraryInfo.setLayout(new BorderLayout());
		bookInLibraryInfo.add(BorderLayout.CENTER, bookInLibScrollPane);
		bookInLibraryInfo.add(BorderLayout.SOUTH, statisticPanel);

		container.add(BorderLayout.CENTER, bookInLibraryInfo);

		JPanel bottomPanel = new JPanel();
		JButton okButton = new JButton("确定");
		okButton.addActionListener(new OkButtonActionListener());
		bottomPanel.add(okButton);
		container.add(BorderLayout.SOUTH, bottomPanel);
	}

	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "BookDetailsDialog类: "
				+ msg);
	}

	/**
	 * 处理按钮的内部类
	 */
	class OkButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			setVisible(false);
		}
	}

}
