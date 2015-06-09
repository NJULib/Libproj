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
	protected JList manageList;// ����Ա�б�
	protected JScrollPane manageListScrollPane;// �������
	protected JLabel manageLabel;// ����Ա�б��ǩ
	protected JPanel topPanel;
	protected JPanel bottomPanel;
	protected JButton addButton;// ��ӹ���Ա��ť
	protected JButton modifyButton;// �޸İ�ť
	protected JButton dellButton;// ɾ����ť
	protected ArrayList managerArrayList;// ����Ա�б�
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
			JOptionPane.showMessageDialog(this, "�������ӹ���: " + e, "��������",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	void buildGUI() {
		this.setLayout(new BorderLayout());
		manageLabel = new JLabel("����Ա�б�");
		topPanel = new JPanel();
		topPanel.add(manageLabel);
		this.add(BorderLayout.NORTH, topPanel);

		manageList = new JList();
		// ����ѡ��ģʽ Ϊ��ѡ
		manageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// ѡ��ı�ʱ ������
		manageList.addListSelectionListener(new ManagerSelectionListener());
		// ˫�� ������
		manageList.addMouseListener(new ManagerListMouseClickListener());

		manageListScrollPane = new JScrollPane(manageList);

		ProcessManagerList();
		this.add(BorderLayout.CENTER, manageListScrollPane);

		bottomPanel = new JPanel();
		addButton = new JButton("��ӹ���Ա");
		addButton.addActionListener(new addActionListener());

		modifyButton = new JButton("�޸Ĺ���Ա");
		modifyButton.setEnabled(false);
		modifyButton.addActionListener(new ModifyActionListener());

		dellButton = new JButton("ɾ������Ա");
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
	 * ȡ�����й���Ա�б�
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
				// û�м��������ʱ��Ӧ���ͼ���б���
				Object[] noData = new Object[0];
				manageList.setListData(noData);
				modifyButton.setEnabled(false);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "�������: " + e, "��������",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * ��ӹ���Ա����
	 */
	private void AddMaster() {
		MasterManageAdd masterManageAdd = new MasterManageAdd(parentFrame,
				libClient, loginUserID);
		masterManageAdd.setVisible(true);
	}

	/**
	 * �г�����Ա����ϸ��Ϣ
	 */
	private void DisplayMasterDetailsDialog() {
		int index = manageList.getSelectedIndex();
		LibraianInfo libraianInfo = (LibraianInfo) managerArrayList.get(index);
		if (!loginUserID.equals(libraianInfo.getLibraianid())
				&& !loginUserID.equals("admini")) {
			JOptionPane.showMessageDialog(null, "�Բ����㲻�ǳ�������Ա����Ȩ�޸�������Ϣ��");
		} else {
			MasterModify masterModify = new MasterModify(parentFrame,
					libraianInfo, libClient, loginUserID);
			masterModify.setVisible(true);
			
		}
	}

	/**
	 * ��ӹ���Ա��Ť������
	 */
	class addActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			AddMaster();
		}
	}

	/**
	 * �༭ ��ť������
	 */
	class ModifyActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			DisplayMasterDetailsDialog();
		}
	}

	/**
	 * ɾ������Ա��ť������
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
				JOptionPane.showMessageDialog(null, "�ɹ�ɾ������Ա�˺ţ�");
				ProcessManagerList();
				//parentFrame.tabbedPane.remove(parentFrame.masterManagePanel);
				//parentFrame.tabbedPane.remove(3);
				//MasterManagePanel masterManagePanel =new MasterManagePanel(parentFrame, libClient, loginUserID);
				//parentFrame.tabbedPane.addTab("����Աά��", masterManagePanel);
//				ParaMaintainPanel paramMaintain = new ParaMaintainPanel(parentFrame, libClient, loginUserID);
//				parentFrame.tabbedPane.addTab("����ά��", paramMaintain);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// JListѡ��״̬�ı�ļ�����������
	class ManagerSelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent event) {
			// clearSelection()���������ѡ�е����ݣ���isSelectionEmpty()��Ϊtrue
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

	// ˫��ʱ��ʾ��ǵ���ϸ��Ϣ��ͬ����ϸ����ť
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
