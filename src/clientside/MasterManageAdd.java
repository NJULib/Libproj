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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MasterManageAdd extends JDialog {

	private static final long serialVersionUID = 5069557051147163390L;

	protected MainFrame frame;
	protected LibClient libClient;

	protected JTable messageTable;
	protected JScrollPane messageScrollPane;
	protected JTextField userValue;
	protected JPasswordField passwordValue;
	protected JPasswordField password2Value;
	protected JTextField nameValue;
	protected JTextField popedomLabel;
	protected Checkbox box1, box2, box3;
	protected int state1 = 0, state2 = 0, state3 = 0;// 三种权限的状态
	protected JPanel masterAddPanel;// 添加管理员面板
	protected String loginUserID;//登录用户

	public MasterManageAdd(MainFrame theParentFrame, LibClient theLibClient, String loginUserID) {
		frame = theParentFrame;
		this.libClient = theLibClient;
		this.loginUserID = loginUserID;
		buildGUI();
		this.pack();
		// 设置大小
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension framesize = this.getSize();
		int x = (int) screensize.getWidth() / 2 - (int) framesize.getWidth()
				/ 2;
		int y = (int) screensize.getHeight() / 2 - (int) framesize.getHeight()
				/ 2;
		this.setBounds(x, y, 400, 300);
	}

	void buildGUI() {
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		masterAddPanel = new JPanel();
		masterAddPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 10);
		JLabel userLabel = new JLabel("用户名:", JLabel.RIGHT);
		userLabel.setForeground(Color.blue);
		masterAddPanel.add(userLabel, c);

		c.gridx = 1;
		c.gridy = 0;
		// c.gridwidth = 3;
		userValue = new JTextField(10);
		masterAddPanel.add(userValue, c);

		c.gridx = 0;
		c.gridy = 1;
		JLabel password = new JLabel("密码:", JLabel.RIGHT);
		password.setForeground(Color.blue);
		masterAddPanel.add(password, c);

		c.gridx = 1;
		c.gridy = 1;
		passwordValue = new JPasswordField(10);
		masterAddPanel.add(passwordValue, c);

		c.gridx = 0;
		c.gridy = 2;
		JLabel password2 = new JLabel("确认密码:", JLabel.RIGHT);
		password2.setForeground(Color.blue);
		masterAddPanel.add(password2, c);

		c.gridx = 1;
		c.gridy = 2;
		password2Value = new JPasswordField(10);
		masterAddPanel.add(password2Value, c);

		c.gridx = 0;
		c.gridy = 3;
		JLabel nameLabel = new JLabel("姓名:", JLabel.RIGHT);
		nameLabel.setForeground(Color.blue);
		masterAddPanel.add(nameLabel, c);

		c.gridx = 1;
		c.gridy = 3;
		nameValue = new JTextField(10);
		masterAddPanel.add(nameValue, c);

		c.gridx = 0;
		c.gridy = 4;
		JLabel popedomLabel = new JLabel("权限:", JLabel.RIGHT);
		popedomLabel.setForeground(Color.blue);
		masterAddPanel.add(popedomLabel, c);

		box1 = new Checkbox("图书管理权限");
		box2 = new Checkbox("读者管理权限");
		box3 = new Checkbox("参数管理权限");
		c.gridx = 1;
		c.gridy = 4;
		masterAddPanel.add(box1, c);
		c.gridx = 1;
		c.gridy = 5;
		masterAddPanel.add(box2, c);
		c.gridx = 1;
		c.gridy = 6;
		masterAddPanel.add(box3, c);

		c.gridx = 1;
		c.gridy = 7;
		JButton addButton = new JButton("确认");
		addButton.addActionListener(new addButtonActionListener());
		masterAddPanel.add(addButton, c);
		this.add(BorderLayout.CENTER, masterAddPanel);
	}

	/**
	 *确认添加管理员按钮的监听类
	 */
	class addButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			addMaster();
		}
	}
	void addMaster() {
		String userid = userValue.getText();
		if(null==userid||"".equals(userid.trim())){
			JOptionPane.showMessageDialog(null, "管理员编号不可为空！");
			return;
		}
		String password = new String(passwordValue.getPassword());
		if(null==password||"".equals(password.trim())){
			JOptionPane.showMessageDialog(null, "密码必须设定");
			return;
		}
		String password2 = new String(password2Value.getPassword());
		if(null==password2||"".equals(password2.trim())){
			JOptionPane.showMessageDialog(null, "重复密码必须设定");
			return;
		}
		String name = nameValue.getText();
		if(null==name||"".equals(name.trim())){
			JOptionPane.showMessageDialog(null, "管理员姓名必须设定");
			return;
		}
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
			JOptionPane.showMessageDialog(null, "对不起，您两次输入的密码不同，请重新输入！");
			return;
		} else if (password.equals(password2)) {
			try {
				boolean b = libClient.addMaster(userid, password, name,
						state1, state2, state3);
				if (b) {
					JOptionPane.showMessageDialog(null, "添加管理员成功");
					setVisible(false);
					this.dispose();//释放资源
					frame.masterManagePanel.ProcessManagerList();
					//frame.tabbedPane.remove(frame.masterManagePanel);
					//frame.tabbedPane.remove(3);
					//MasterManagePanel masterManagePanel =new MasterManagePanel(frame, libClient, loginUserID);
					//frame.tabbedPane.addTab("管理员维护", masterManagePanel);
//					ParaMaintainPanel paramMaintain = new ParaMaintainPanel(frame, libClient, loginUserID);
//					frame.tabbedPane.addTab("参数维护", paramMaintain);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
