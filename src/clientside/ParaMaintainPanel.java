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
 * @ϵͳ����ά�������棬����ʵ�ֲ�ѯ���ݿ��еĲ�����Ϣ
 */
@SuppressWarnings("all")
public class ParaMaintainPanel extends JPanel {

	private static final long serialVersionUID = 6629336739069350337L;

	protected LibClient libClient;// �������ӷ������Ŀͻ�Socket
	private ArrayList parameterInfoList = null;// ���ڽ��շ����������Ĳ�����Ϣ
	protected JTable parameterInfoTable;// ����չʾ������Ϣ�ı��
	protected DefaultTableModel tableModel;
	//protected JScrollPane parameterScrollPane;// ��Ų�����Ϣ�����
	protected JPanel topPanel;
	protected MainFrame parentFrame;
	protected JButton updateButton;
	protected String parameterID;// �������
	protected int type;// �������
	protected Vector parameterHead;
	NumberFormat numberFormatter = NumberFormat.getNumberInstance();

	public ParaMaintainPanel(MainFrame parentFrame, LibClient libClient,
			String parameterID) {
		this.parentFrame = parentFrame;
		this.parameterID = parameterID;
		this.libClient = libClient;
		this.setLayout(new BorderLayout());


		buildGUI();// ����������
	}

	protected void buildGUI() {

		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPanel.setBorder(BorderFactory.createTitledBorder("������Ϣ���"));

		this.add(BorderLayout.NORTH, topPanel);
		String [] params = {"�������","��������","��������","������"};
		tableModel = new DefaultTableModel(params,0);
		parameterInfoTable = new JTable(tableModel);
		parameterInfoTable.addMouseListener(new SelectedMouseListener());
		processParameterSelect();
		JScrollPane parameterScrollPane = new JScrollPane(parameterInfoTable);
		parameterScrollPane.setBorder(BorderFactory.createTitledBorder("������Ϣ"));
		this.add(BorderLayout.CENTER, parameterScrollPane);

		JPanel bottomPanel = new JPanel();

		JButton updateButton = new JButton("�޸�");

		updateButton.addActionListener(new UpdateButtonActionListener());
		bottomPanel.add(updateButton);

		this.add(BorderLayout.SOUTH, bottomPanel);
	}

	public void processParameterSelect() {
		/**
		 * ��ʾ������Ϣ
		 */
		int rows = tableModel.getRowCount();
		while(rows>0){
			rows = rows - 1;
			tableModel.removeRow(rows);
		}
		parameterInfoList = libClient.getParameterInfo(parameterID);
		int rowCont = parameterInfoList.size();
		if(0==rowCont){
			JOptionPane.showMessageDialog(null, "������������");
			return;
		}
		ParameterInfo parameterInfo;
		Object [] obj = new Object[4];
		for(int i=0;i<rowCont;i++){
			parameterInfo = (ParameterInfo)parameterInfoList.get(i);
			Vector rowVector = new Vector();// ���ÿһ�����ݵ�����
			
			numberFormatter.setMaximumFractionDigits(1);//С�����һλ
			
			int m = parameterInfo.getParameterType();
			String n = null;
			switch (m) {
			case 1:
				n = "��ѧ��";
				break;
			case 2:
				n = "�о���";
				break;
			case 3:
				n = "��ʦ";
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
	 * ����ť���ڲ���
	 */

	class UpdateButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			DisplayUpdateParameterDialog();
		}
	}

	public void DisplayUpdateParameterDialog() {
		int selectedRow = parameterInfoTable.getSelectedRow();
		if(selectedRow==-1){
			JOptionPane.showMessageDialog(null, "��ѡ�޼�¼");
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

	// ˫��ʱ��ʾ��ǵ���ϸ��Ϣ��ͬ����ϸ����ť
	class SelectedMouseListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			DisplayUpdateParameterDialog();
		}
	}
}
