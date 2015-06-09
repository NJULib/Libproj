package clientside;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import serverside.entity.ReaderInfo;
import util.GBC;
import util.LibProtocals;


public class ReaderRegisterDialog extends JDialog {
	private JTextField idField;	//���߱��
	private JTextField nameField;//����
	private JTextField passwdField;
	private JTextField ageField;//����
	private JRadioButton maleButton;//�Ա�
	private String sex = "��";
	private JRadioButton femaleButton;//
	private ButtonGroup group;//
	private JTextField addressField;//��ͥסַ
	private JTextField telField;//��ϵ�绰
	private JTextField typeField;//��ϵ�绰
	private JTextField majorField;//
	private JTextField departmentField;//
	private JTextArea summaryArea;//
	private JButton registerButton;//
	private JButton clearButton;//
	private LibClient	libClient ;
	List list ;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	public static void main(String [] args){
//		new ReaderRegisterDialog(new JFrame());
//	}
	public ReaderRegisterDialog(MainFrame frame) {
		super(frame);
		setLayout(new BorderLayout());
		ServerInfo serverInfo = new ServerInfo();
		libClient = new LibClient(serverInfo.getHost(),serverInfo.getPort());
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		idField = new JTextField("");
		passwdField = new JTextField("password");
		ageField= new JTextField("");
		nameField = new JTextField("");
		addressField= new JTextField("");
		telField= new JTextField("");
		typeField  = new JTextField("");
		majorField = new JTextField("");
		departmentField = new JTextField("");
		ageField = new JFormattedTextField(NumberFormat.getIntegerInstance());

		group = new ButtonGroup();
		maleButton = new JRadioButton("��");
		maleButton.addActionListener(new SexSelectActionListener());
		maleButton.setSelected(true);
		femaleButton = new JRadioButton("Ů");
		femaleButton.addActionListener(new SexSelectActionListener());
		group.add(maleButton);
		group.add(femaleButton);
		JPanel sexPanel = new JPanel();
		sexPanel.add(maleButton);
		sexPanel.add(femaleButton);
		sexPanel.setBorder(BorderFactory.createEtchedBorder());

		summaryArea = new JTextArea();
		summaryArea.setLineWrap(true);

		JPanel labelPanel = new JPanel();
		labelPanel.add(new JLabel("��ӭ����ע�᱾�ݻ�Ա, ����ȫ�����ʷ���"));
		labelPanel.setBorder(BorderFactory.createEtchedBorder());
		add(labelPanel, BorderLayout.NORTH);

		panel.add(new JLabel("���: "), new GBC(0, 0).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(idField, new GBC(1, 0).setFill(GBC.HORIZONTAL).setWeight(100,
				0).setInsets(5));
		
		panel.add(new JLabel("����: "), new GBC(2, 0).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(nameField, new GBC(3, 0).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		
		panel.add(new JLabel("��ʼ����: "), new GBC(0, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(passwdField, new GBC(1, 1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		
		panel.add(new JLabel("����: "), new GBC(2, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(ageField, new GBC(3, 1).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		
		panel.add(new JLabel("�Ա�: "), new GBC(0, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(sexPanel, new GBC(1, 2).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		
		panel.add(new JLabel("��ͥסַ: "), new GBC(2, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(addressField, new GBC(3, 2).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		
		panel.add(new JLabel("��ϵ�绰: "), new GBC(0, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(telField, new GBC(1, 3).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		

		panel.add(new JLabel("��������: "), new GBC(2, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(typeField, new GBC(3, 3).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		

		panel.add(new JLabel("רҵ: "), new GBC(0, 4).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(majorField, new GBC(1, 4).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

		panel.add(new JLabel("Ժϵ: "), new GBC(2, 4).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(departmentField, new GBC(3, 4).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));

//		panel.add(new JLabel(": "), new GBC(0, 5).setAnchor(GBC.EAST)
//				.setInsets(5));
		list = new List();
		panel.add(new JScrollPane(list), new GBC(0, 5, 4, 4).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));

		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		registerButton = new JButton("ע��");
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processReaderRegister();
			}
		});
		clearButton = new JButton("���");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initField();
			}
		});
//		cancelButton = new JButton("�˳�");
//		cancelButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				dialog.setVisible(false);
//			}
//		});
		buttonPanel.add(registerButton);
		buttonPanel.add(clearButton);
		//buttonPanel.add(cancelButton);
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		add(buttonPanel, BorderLayout.SOUTH);
		setSize(800, 600);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
		int screenwidth = dim.width;
		int screenheight = dim.height;
		int dialogwidth = this.getWidth();
		int dialogheight = this.getHeight();
		int x = screenwidth/2-dialogwidth/2;
		int y = screenheight/2 - dialogheight/2;
		setLocation(x, y);
		// setResizable(false);
//		addWindowListener(new WindowAdapter(){
//			@Override
//			public void windowClosing(WindowEvent e){
//				setVisible(false);
//				dispose();
//				System.exit(0);
//			}
//		});
		setVisible(false);
	}

	private void initField() {
		idField.setText("");
		passwdField.setText("");
		ageField.setText("");
		nameField.setText("");
		addressField.setText("");
		telField.setText("");
		typeField.setText("");
		majorField.setText("");
		departmentField.setText("");
	}
	private void processReaderRegister() {
		String readerid = idField.getText();
		if (null == readerid || "".equals(readerid)) {
			JOptionPane.showMessageDialog(null, "���߱�Ų���Ϊ��");
			return;
		}
		ReaderInfo reader =(ReaderInfo) libClient.getName(LibProtocals.OP_GET_READERINFO,readerid, "nullpass");
		if(!readerid.matches("\\d+")){
			JOptionPane.showMessageDialog(null, "���߱�Ÿ�ʽ���Ϸ�");
			return;
		}
		if(null!=reader.getName()){
			JOptionPane.showMessageDialog(null, "�ñ�Ŷ����Ѿ����ڣ�");
			return;
		}
		String name = nameField.getText();
		if (null == name || "".equals(name)) {
			JOptionPane.showMessageDialog(null, "����������Ϊ��");
			return;
		}
		String pass = passwdField.getText();
		pass = (null==pass||"".equals(pass.trim()))?"password":pass;
		String ages = ageField.getText();
		ages=(null==ages||"".equals(ages.trim()))?"0":ages;
		if(!(ages.matches("\\d+"))){
			JOptionPane.showMessageDialog(null, "�������Ϊ����");
			return;
		}
		int age = Integer.parseInt(ages);
		String major = majorField.getText();
		major = (null==major||"".equals(major.trim()))?"δ֪רҵ":major;
		String depart = departmentField.getText();
		depart = (null==depart||"".equals(depart.trim()))?"δ֪Ժϵ":depart;	
		String address = addressField.getText();
		address = (null == address || "".equals(address)) ? "δ֪" : address;
		String tel = telField.getText();
		if (!(tel.matches("\\d+"))) {
			JOptionPane.showMessageDialog(null,
					"��ϵ�绰�������������");
			return;
		}
		String typeStr = typeField.getText();
		typeStr = (null == typeStr || "".equals(typeStr)) ? "0" : typeStr;
		if (!(typeStr.matches("\\d{1}"))) {
			JOptionPane.showMessageDialog(null, "�������ͱ�����1-9�ĵ�������");
			return;
		}
		int type = Integer.parseInt(typeStr);
		String beginDate = null;
		beginDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		//String [] dateArray = beginDate.split("-");
		Calendar calendar = Calendar.getInstance();
		//int year = Integer.parseInt(dateArray[0]);
		calendar.add(calendar.YEAR, 4);
		calendar.set(Calendar.MONTH, 6);
		calendar.set(Calendar.DATE, 30);
		String endDate = calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE);
		ReaderInfo readerInfo = new ReaderInfo(readerid, pass, name,age,sex, address, tel,beginDate,endDate,type,major,depart);
		log("ReaderDetailsDialog��ʼ���Ӷ�����Ϣ");
		boolean mark = libClient.insertReaderInfo(readerInfo);
		if(mark){
			JOptionPane.showMessageDialog(null, "������Ϣ��ӳɹ���");
			list.add(readerid+"   "+name+"   "+"     "+age+"    "+major+"     "+depart);
			initField();
		}else{
			JOptionPane.showMessageDialog(null, "������Ϣ���ʧ�ܣ�");
		}
	}
	class SexSelectActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(maleButton==e.getSource()){
				sex = "��";
			}else if(femaleButton==e.getSource()){
				sex="Ů";
			}
		}
		
	}
	private void log(Object obj) {
		System.out.println(obj);
	}
}