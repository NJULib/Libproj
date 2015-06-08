 package clientside;

import java.awt.BorderLayout;
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
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import clientside.LibClient;
import clientside.MainFrame;
import clientside.ParaMaintainPanel;

import serverside.entity.ParameterInfo;

public class UpdateParameterDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected MainFrame parentFrame;
	protected int type;
	protected LibClient libClient;
	protected ParameterInfo parameterInfo;
	protected String parameterID;
	protected JTextField amountValue, periodValue, dailyfineValue;
	Box baseBox, boxV1, boxV2;
	NumberFormat numberFormatter = NumberFormat.getNumberInstance();
	

	public UpdateParameterDialog(MainFrame parentFrame,
			ParameterInfo parameterInfo, LibClient libClient, String parameterID) {
		this.parentFrame = parentFrame;
		this.parameterInfo = parameterInfo;
		this.libClient = libClient;
		this.parameterID = parameterID;

		buildGUI();

		this.pack();
		// 设置大小位置
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

		// 图书详细数据
		JPanel parameterPanel = new JPanel();
		parameterPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 10);
		JLabel typeLabel = new JLabel("读者类别:", JLabel.RIGHT);
		typeLabel.setForeground(Color.blue);
		parameterPanel.add(typeLabel, c);

		c.gridx = 1;
		c.gridy = 0;
		// c.gridwidth = 3;
		JLabel typeValue = new JLabel(String.valueOf(parameterInfo
				.getParameterType()), 10);
		parameterPanel.add(typeValue, c);

		c.gridx = 0;
		c.gridy = 1;
		// c.gridwidth = 1;
		JLabel amountLabel = new JLabel("借书数量:", JLabel.RIGHT);
		amountLabel.setForeground(Color.blue);
		parameterPanel.add(amountLabel, c);

		c.gridx = 1;
		c.gridy = 1;
		// c.gridwidth = 3;
		amountValue = new JTextField(String.valueOf(parameterInfo
				.getParameterAmount()), 10);
		parameterPanel.add(amountValue, c);

		c.gridx = 0;
		c.gridy = 2;
		// c.gridwidth = 1;
		JLabel periodLabel = new JLabel("借书天数:", JLabel.RIGHT);
		periodLabel.setForeground(Color.blue);
		parameterPanel.add(periodLabel, c);

		c.gridx = 1;
		c.gridy = 2;
		// c.gridwidth = 3;
		periodValue = new JTextField(String.valueOf(parameterInfo
				.getParameterPeriod()), 10);
		parameterPanel.add(periodValue, c);

		c.gridx = 0;
		c.gridy = 3;
		// c.gridwidth = 1;
		JLabel dailyfineLabel = new JLabel("超期罚金:", JLabel.RIGHT);
		dailyfineLabel.setForeground(Color.blue);
		parameterPanel.add(dailyfineLabel, c);

		numberFormatter.setMaximumFractionDigits(1);
		numberFormatter.format(parameterInfo
				.getParameterDailyfine());
		c.gridx = 1;
		c.gridy = 3;
		// c.gridwidth = 3;
		dailyfineValue = new JTextField(numberFormatter.format(parameterInfo
				.getParameterDailyfine()), 10);
		parameterPanel.add(dailyfineValue, c);

		container.add(BorderLayout.CENTER, parameterPanel);

		JButton updateButton = new JButton("修改");
		updateButton.addActionListener(new updateActionListener());
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(updateButton);
		container.add(BorderLayout.SOUTH, bottomPanel);
	}

	class updateActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			updateParameter();
		}
	}

	protected void updateParameter() {
		boolean b = false;
		int type = parameterInfo.getParameterType();
		int amount;
		int period;
		double dailyfine;
		
		String amount_1 = amountValue.getText();
		if (!(amount_1.matches("\\d+"))) {
			JOptionPane.showMessageDialog(null, "借书数量必须是数字组成");
			return;
		} else {
			amount = Integer.parseInt(amount_1);
		}
		String period_1 = periodValue.getText();
		if (!(period_1.matches("\\d+"))) {
			JOptionPane.showMessageDialog(null, "借书天数必须是数字组成");
			return;
		} else {
			period = Integer.parseInt(period_1);
		}

		String dailyfine_1 = dailyfineValue.getText();
		if (!(dailyfine_1.matches("\\d+\56\\d+"))) {
			JOptionPane.showMessageDialog(null, "超期罚金只能是精确到小数点后一位的数字");
			return;
		} else {
			dailyfine = Double.parseDouble(dailyfine_1);
		}

		try {
			b = libClient.updateParameter(type, amount, period, dailyfine);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (b) {
			JOptionPane.showMessageDialog(null, "成功修改参数");
			setVisible(false);
			this.dispose();
			parentFrame.paraMaintainPanel.processParameterSelect();
		}
		
//
//		parentFrame.tabbedPane.remove(parentFrame.paraMaintainPanel);
//		ParaMaintainPanel paraMaintainPanel = new ParaMaintainPanel(
//				parentFrame, libClient, parameterID);
//		parentFrame.tabbedPane.addTab("系统参数", paraMaintainPanel);
	}
}