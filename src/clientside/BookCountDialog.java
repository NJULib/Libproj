package clientside;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class BookCountDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BookCountDialog(MainFrame frame){
		super(frame,"ͼ��ͳ��--����̵�");
		setLayout(new BorderLayout());
		JPanel centerPanel = new JPanel();
		Border etched = BorderFactory.createEtchedBorder();
		centerPanel.setBorder(BorderFactory.createTitledBorder(etched, "ͳ�ƽ��"));
		this.add(centerPanel,BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(5,1));
		JLabel [] labels = new JLabel[5];
		labels[0] = new JLabel("ͼ����ܲ��飺",JLabel.CENTER);
		labels[1] = new JLabel("��ͨͼ��������",JLabel.CENTER);
		labels[2] = new JLabel("�ڽ�ͼ��������",JLabel.CENTER);
		labels[3] = new JLabel("�ִ�ͼ��������",JLabel.CENTER);
		labels[4] = new JLabel("��ʧͼ��������",JLabel.CENTER);
		String [] libstr = {"ͼ����ܲ��飺","��ͨͼ��������","�ڽ�ͼ��������","�ִ�ͼ��������","��ʧͼ��������"};
		ServerInfo serverInfo = new ServerInfo();
		LibClient	libClient = new LibClient(serverInfo.getHost(),serverInfo.getPort());
		List<Integer> countList = libClient.getCountList();
		for(int i=0;i<countList.size();i++){
			labels[i].setText(libstr[i]+"     "+countList.get(i));
			centerPanel.add(labels[i]);
		}
		setSize(380,400);	
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
		int screenwidth = dim.width;
		int screenheight = dim.height;
		int dialogwidth = this.getWidth();
		int dialogheight = this.getHeight();
		int x = screenwidth/2-dialogwidth/2;
		int y = screenheight/2 - dialogheight/2;
		setLocation(x, y);
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
//	public static void main(String [] args){
//		new BookCountDialog(new JFrame());
//	}
}
