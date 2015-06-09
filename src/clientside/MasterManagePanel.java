package clientside;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import serverside.entity.LibraianInfo;

public class MasterManagePanel extends JPanel {

	private static final long serialVersionUID = -1475406375198679673L;

	protected MainFrame parentFrame;
	protected LibClient libClient;
	protected JList manageList;// 管理员列表
	protected JScrollPane manageListScrollPane;// 滚动面板
	protected JLabel manageLabel;// 管理员列表标签
	protected JPanel topPanel;
	protected JPanel bottomPanel;
	protected JButton addButton;// 添加管理员按钮
	protected JButton modifyButton;// 修改按钮
	protected JButton dellButton;// 删除按钮
	protected ArrayList managerArrayList;// 管理员列表
	protected String userid;
	protected String loginUserID;

	public MasterManagePanel() {
	}

	public MasterManagePanel(MainFrame theParentFrame, LibClient libClient,
			String loginUserID) {
		try {
			parentFrame = theParentFrame;
			this.loginUserID = loginUserID;
			this.libClient = libClient;
			buildGUI();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "网络连接故障: " + e, "网络问题",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	void buildGUI() {
		this.setLayout(new BorderLayout());
		manageLabel = new JLabel("管理员列表");
		topPanel = new JPanel();
		topPanel.add(manageLabel);
		this.add(BorderLayout.NORTH, topPanel);

		manageList = new JList();
		// 设置选择模式 为单选
		manageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// 选择改变时 监听器
		manageList.addListSelectionListener(new ManagerSelectionListener());
		// 双击 监听器
		manageList.addMouseListener(new ManagerListMouseClickListener());

		manageListScrollPane = new JScrollPane(manageList);

		ProcessManagerList();
		this.add(BorderLayout.CENTER, manageListScrollPane);

		bottomPanel = new JPanel();
		addButton = new JButton("添加管理员");
		addButton.addActionListener(new addActionListener());

		modifyButton = new JButton("修改管理员");
		modifyButton.setEnabled(false);
		modifyButton.addActionListener(new ModifyActionListener());

		dellButton = new JButton("删除管理员");
		dellButton.setEnabled(false);
		dellButton.addActionListener(new DellActionListener());

		bottomPanel.add(addButton);
		bottomPanel.add(modifyButton);
		bottomPanel.add(dellButton);
		if (!loginUserID.equals("admini")) {
			dellButton.setEnabled(false);
			addButton.setEnabled(false);
		}
		this.add(BorderLayout.SOUTH, bottomPanel);
	}

	/**
	 * 取得所有管理员列表
	 */
	protected void ProcessManagerList() {
		try {
			managerArrayList = libClient.getManagerList();
			manageList.removeAll();
			manageList.repaint();
			if (managerArrayList.size() > 0) {
				Object[] theData = managerArrayList.toArray();
				manageList.setListData(theData);
			} else {
				// 没有检索到书的时候，应清空图书列表区
				Object[] noData = new Object[0];
				manageList.setListData(noData);
				modifyButton.setEnabled(false);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "网络故障: " + e, "网络问题",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * 添加管理员方法
	 */
	private void AddMaster() {
		MasterManageAdd masterManageAdd = new MasterManageAdd(parentFrame,
				libClient, loginUserID);
		masterManageAdd.setVisible(true);
	}

	/**
	 * 列出管理员的详细信息
	 */
	private void DisplayMasterDetailsDialog() {
		int index = manageList.getSelectedIndex();
		LibraianInfo libraianInfo = (LibraianInfo) managerArrayList.get(index);
		if (!loginUserID.equals(libraianInfo.getLibraianid())
				&& !loginUserID.equals("admini")) {
			JOptionPane.showMessageDialog(null, "对不起，你不是超级管理员，无权修改他人信息！");
		} else {
			MasterModify masterModify = new MasterModify(parentFrame,
					libraianInfo, libClient, loginUserID);
			masterModify.setVisible(true);
			
		}
	}

	/**
	 * 添加管理员按扭监听器
	 */
	class addActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			AddMaster();
		}
	}

	/**
	 * 编辑 按钮监听器
	 */
	class ModifyActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			DisplayMasterDetailsDialog();
		}
	}

	/**
	 * 删除管理员按钮监听器
	 */
	class DellActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			DellMaster();
		}
	}

	void DellMaster() {
		int index = manageList.getSelectedIndex();
		LibraianInfo librarianInfo = (LibraianInfo) managerArrayList.get(index);
		userid = librarianInfo.getLibraianid();
		try {
			boolean b = libClient.dellMaster(userid);
			if (b) {
				JOptionPane.showMessageDialog(null, "成功删除管理员账号！");
				ProcessManagerList();
				//parentFrame.tabbedPane.remove(parentFrame.masterManagePanel);
				//parentFrame.tabbedPane.remove(3);
				//MasterManagePanel masterManagePanel =new MasterManagePanel(parentFrame, libClient, loginUserID);
				//parentFrame.tabbedPane.addTab("管理员维护", masterManagePanel);
//				ParaMaintainPanel paramMaintain = new ParaMaintainPanel(parentFrame, libClient, loginUserID);
//				parentFrame.tabbedPane.addTab("参数维护", paramMaintain);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// JList选中状态改变的监听器处理类
	class ManagerSelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent event) {
			// clearSelection()方法将清除选中的内容，将isSelectionEmpty()置为true
			if (manageList.isSelectionEmpty()) {
				modifyButton.setEnabled(false);
				dellButton.setEnabled(false);
			} else if (loginUserID.equals("admini")) {
				modifyButton.setEnabled(true);
				dellButton.setEnabled(true);
			} else {
				modifyButton.setEnabled(true);
			}
		}
	}

	// 双击时显示书记的详细信息，同‘详细’按钮
	class ManagerListMouseClickListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			if (!modifyButton.isEnabled()) {
				return;
			}
			if (e.getClickCount() == 2) {
				DisplayMasterDetailsDialog();
			}
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}
}
