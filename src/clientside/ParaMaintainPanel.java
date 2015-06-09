package clientside;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import clientside.MasterManagePanel.ManagerSelectionListener;
import clientside.UpdateParameterDialog;

import serverside.entity.BookDetails;
import serverside.entity.BookInLibrary;

import serverside.entity.ParameterInfo;
import util.CurrDateTime;

/**
 * @系统参数维护主界面，可以实现查询数据库中的参数信息
 */
@SuppressWarnings("all")
public class ParaMaintainPanel extends JPanel {

	private static final long serialVersionUID = 6629336739069350337L;

	protected LibClient libClient;// 用于连接服务器的客户Socket
	private ArrayList parameterInfoList = null;// 用于接收服务器传来的参数信息
	protected JTable parameterInfoTable;// 用于展示参数信息的表格
	protected DefaultTableModel tableModel;
	//protected JScrollPane parameterScrollPane;// 存放参数信息的面板
	protected JPanel topPanel;
	protected MainFrame parentFrame;
	protected JButton updateButton;
	protected String parameterID;// 惨数编号
	protected int type;// 读者类别
	protected Vector parameterHead;
	NumberFormat numberFormatter = NumberFormat.getNumberInstance();

	public ParaMaintainPanel(MainFrame parentFrame, LibClient libClient,
			String parameterID) {
		this.parentFrame = parentFrame;
		this.parameterID = parameterID;
		this.libClient = libClient;
		this.setLayout(new BorderLayout());


		buildGUI();// 建立主界面
	}

	protected void buildGUI() {

		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPanel.setBorder(BorderFactory.createTitledBorder("参数信息情况"));

		this.add(BorderLayout.NORTH, topPanel);
		String [] params = {"读者类别","借书数量","借书天数","罚款金额"};
		tableModel = new DefaultTableModel(params,0);
		parameterInfoTable = new JTable(tableModel);
		parameterInfoTable.addMouseListener(new SelectedMouseListener());
		processParameterSelect();
		JScrollPane parameterScrollPane = new JScrollPane(parameterInfoTable);
		parameterScrollPane.setBorder(BorderFactory.createTitledBorder("参数信息"));
		this.add(BorderLayout.CENTER, parameterScrollPane);

		JPanel bottomPanel = new JPanel();

		JButton updateButton = new JButton("修改");

		updateButton.addActionListener(new UpdateButtonActionListener());
		bottomPanel.add(updateButton);

		this.add(BorderLayout.SOUTH, bottomPanel);
	}

	public void processParameterSelect() {
		/**
		 * 显示借阅信息
		 */
		int rows = tableModel.getRowCount();
		while(rows>0){
			rows = rows - 1;
			tableModel.removeRow(rows);
		}
		parameterInfoList = libClient.getParameterInfo(parameterID);
		int rowCont = parameterInfoList.size();
		if(0==rowCont){
			JOptionPane.showMessageDialog(null, "参数表无数据");
			return;
		}
		ParameterInfo parameterInfo;
		Object [] obj = new Object[4];
		for(int i=0;i<rowCont;i++){
			parameterInfo = (ParameterInfo)parameterInfoList.get(i);
			Vector rowVector = new Vector();// 存放每一行内容的向量
			
			numberFormatter.setMaximumFractionDigits(1);//小数点后一位
			
			int m = parameterInfo.getParameterType();
			String n = null;
			switch (m) {
			case 1:
				n = "大学生";
				break;
			case 2:
				n = "研究生";
				break;
			case 3:
				n = "教师";
				break;
			}
			int account = parameterInfo.getParameterAmount();
			int perid = parameterInfo.getParameterPeriod();
			double fine = parameterInfo.getParameterDailyfine();
			obj[0] = n;
			obj[1] = account;
			obj[2] = perid;
			obj[3] = fine;
			tableModel.addRow(obj);
		}
	}

	/**
	 * 处理按钮的内部类
	 */

	class UpdateButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			DisplayUpdateParameterDialog();
		}
	}

	public void DisplayUpdateParameterDialog() {
		int selectedRow = parameterInfoTable.getSelectedRow();
		if(selectedRow==-1){
			JOptionPane.showMessageDialog(null, "所选无记录");
			return;
		}
		int[] selection = parameterInfoTable.getSelectedRows();
		for (int i = 0; i < selection.length; i++) {
			selection[i] = parameterInfoTable
					.convertRowIndexToModel(selection[i]);

			System.out.println(selection[i]);
			ParameterInfo parameterInfo = (ParameterInfo) parameterInfoList
					.get(selection[i]);
			UpdateParameterDialog updateParameterDialog = new UpdateParameterDialog(
					parentFrame, parameterInfo, libClient, parameterID);
			updateParameterDialog.setVisible(true);
		}
	}

	// 双击时显示书记的详细信息，同‘详细’按钮
	class SelectedMouseListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			DisplayUpdateParameterDialog();
		}
	}
}
