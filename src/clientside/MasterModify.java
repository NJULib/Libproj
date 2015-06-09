package clientside;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import serverside.entity.LibraianInfo;

public class MasterModify extends JDialog {

	private static final long serialVersionUID = 4953133279074510924L;

	protected MainFrame frame;
	protected LibClient libClient;
	protected LibraianInfo librarianInfo;
	
	protected JLabel userValue;
	protected JPasswordField passwordValue;
	protected JPasswordField password2Value;
	protected JTextField nameValue;
	protected JTextField popedomLabel;
	protected Checkbox box1, box2, box3;
	protected int st1 = 0, st2 = 0, st3 = 0;// ����Ȩ�޵�״̬
	protected int state1 = 0, state2 = 0, state3 = 0;// ����Ȩ�޵�״̬
	protected JPanel masterInfoPanel;
	protected String loginUserID;//��½�û���

	public MasterModify(MainFrame theParentFrame, LibraianInfo librarianInfo,
			LibClient theLibClient ,String loginUserID) {
		this.frame = theParentFrame;
		this.libClient = theLibClient;
		this.librarianInfo = librarianInfo;
		this.loginUserID = loginUserID;
		System.out.println(loginUserID);
		buildGUI();
		this.pack();

		// ���ô�С
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension framesize = this.getSize();
		int x = (int) screensize.getWidth() / 2 - (int) framesize.getWidth()
				/ 2;
		int y = (int) screensize.getHeight() / 2 - (int) framesize.getHeight()
				/ 2;
		this.setBounds(x, y, 400, 300);
	}

	protected void buildGUI() {
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		// ����Ա��ϸ����
		JPanel masterInfoPanel = new JPanel();
		masterInfoPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 10);
		JLabel userLabel = new JLabel("�û���:", JLabel.RIGHT);
		userLabel.setForeground(Color.blue);
		masterInfoPanel.add(userLabel, c);

		c.gridx = 1;
		c.gridy = 0;
		// c.gridwidth = 3;
		userValue = new JLabel(librarianInfo.getLibraianid());
		masterInfoPanel.add(userValue, c);

		c.gridx = 0;
		c.gridy = 1;
		JLabel password = new JLabel("������:", JLabel.RIGHT);
		password.setForeground(Color.blue);
		masterInfoPanel.add(password, c);

		c.gridx = 1;
		c.gridy = 1;
		passwordValue = new JPasswordField(10);
		masterInfoPanel.add(passwordValue, c);

		c.gridx = 0;
		c.gridy = 2;
		JLabel password2 = new JLabel("ȷ������:", JLabel.RIGHT);
		password2.setForeground(Color.blue);
		masterInfoPanel.add(password2, c);

		c.gridx = 1;
		c.gridy = 2;
		password2Value = new JPasswordField(10);
		masterInfoPanel.add(password2Value, c);

		c.gridx = 0;
		c.gridy = 3;
		JLabel nameLabel = new JLabel("����:", JLabel.RIGHT);
		nameLabel.setForeground(Color.blue);
		masterInfoPanel.add(nameLabel, c);

		c.gridx = 1;
		c.gridy = 3;
		nameValue = new JTextField(librarianInfo.getName());
		masterInfoPanel.add(nameValue, c);

		c.gridx = 0;
		c.gridy = 4;
		JLabel popedomLabel = new JLabel("Ȩ��:", JLabel.RIGHT);
		popedomLabel.setForeground(Color.blue);
		masterInfoPanel.add(popedomLabel, c);

		st1 = librarianInfo.getBookp();
		st2 = librarianInfo.getReaderp();
		st3 = librarianInfo.getParameterp();

		box1 = new Checkbox("ͼ�����Ȩ��");
		box2 = new Checkbox("���߹���Ȩ��");
		box3 = new Checkbox("��������Ȩ��");
		if (st1 == 1) {
			box1.setState(true);
		}
		if (st2 == 1) {
			box2.setState(true);
		}
		if (st3 == 1) {
			box3.setState(true);
		}
		c.gridx = 1;
		c.gridy = 4;
		masterInfoPanel.add(box1, c);
		c.gridx = 1;
		c.gridy = 5;
		masterInfoPanel.add(box2, c);
		c.gridx = 1;
		c.gridy = 6;
		masterInfoPanel.add(box3, c);

		if (!loginUserID.equals("admini")) {
			box1.setEnabled(false);
			box2.setEnabled(false);
			box3.setEnabled(false);
		}
		c.gridx = 1;
		c.gridy = 7;
		JButton addButton = new JButton("ȷ��");
		addButton.addActionListener(new modifyButtonActionListener());
		masterInfoPanel.add(addButton, c);
		// setVisible(false);
		this.add(BorderLayout.CENTER, masterInfoPanel);
	}

	/**
	 *�޸Ĺ���Ա��Ϣ��ť�ļ�����
	 */
	class modifyButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			flushPanel();
		}
	}
	protected void flushPanel(){
		String userid = userValue.getText();
		String password = new String(passwordValue.getPassword());
		String password2 = new String(password2Value.getPassword());
		String name = nameValue.getText();
		boolean b1 = box1.getState();
		boolean b2 = box2.getState();
		boolean b3 = box3.getState();
		if (b1) {
			state1 = 1;
		}
		if (b2) {
			state2 = 1;
		}
		if (b3) {
			state3 = 1;
		}
		if (!password.equals(password2)) {
			JOptionPane.showMessageDialog(null, "�Բ�����������������벻ͬ�����������룡");
		} else if (password.equals(password2)) {
			try {
				boolean b = libClient.modifyMaster(userid, password, name,
						state1, state2, state3);
				if (b) {
					//new MasterManagePanel(frame,libClient,loginUserID).buildGUI();
					JOptionPane.showMessageDialog(null, "�޸Ĺ���Ա��Ϣ�ɹ�");
					setVisible(false);
					this.dispose();//�ͷ���Դ	
					frame.masterManagePanel.ProcessManagerList();
//					frame.tabbedPane.remove(frame.masterManagePanel);
//					frame.validate();
//					//frame.tabbedPane.remove(3);
//					MasterManagePanel masterManagePanel =new MasterManagePanel(frame, libClient, loginUserID);
//					frame.tabbedPane.addTab("����Աά��", masterManagePanel);
//					ParaMaintainPanel paramMaintain = new ParaMaintainPanel(frame, libClient, loginUserID);
//					frame.tabbedPane.addTab("����ά��", paramMaintain);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
