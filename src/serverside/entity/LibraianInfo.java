package serverside.entity;

import java.io.Serializable;

public class LibraianInfo implements Serializable{
	/**
	 * ����Ա��Ϣ����š����������롢Ȩ�ޣ�Ϊ1ʱ��ʾӵ��Ȩ��)
	 */
	private static final long serialVersionUID = 1L;
	private String libraianid;		//����Ա���
	
	private String passwd;			//����Ա����
	private String name;			//����Ա����
	private int bookp;				//ͼ��ά��Ȩ��
	private int readerp;			//����ά��Ȩ��
	private int parameterp;			//����ά��ά��Ȩ��
	public LibraianInfo(){}
	public LibraianInfo(String libid,String passwd, String name,int bookp,int readerp,int parameterp) {
		this.libraianid = libid;
		this.passwd = passwd;
		this.name = name;
		this.bookp = bookp;
		this.readerp = readerp;
		this.parameterp = parameterp;
	}
	public String getLibraianid() {
		return libraianid;
	}
	public void setLibraianid(String libraianid) {
		this.libraianid = libraianid;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBookp() {
		return bookp;
	}
	public void setBookp(int bookp) {
		this.bookp = bookp;
	}
	public int getReaderp() {
		return readerp;
	}
	public void setReaderp(int readerp) {
		this.readerp = readerp;
	}
	public int getParameterp() {
		return parameterp;
	}
	public void setParameterp(int parameterp) {
		this.parameterp = parameterp;
	}
	@Override
	public String toString(){
		return libraianid+"  "+name;
	}
}
