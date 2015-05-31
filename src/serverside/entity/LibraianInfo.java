package serverside.entity;

import java.io.Serializable;

public class LibraianInfo implements Serializable{
	/**
	 * 管理员信息，编号、姓名、密码、权限（为1时表示拥有权限)
	 */
	private static final long serialVersionUID = 1L;
	private String libraianid;		//管理员编号
	
	private String passwd;			//管理员密码
	private String name;			//管理员姓名
	private int bookp;				//图书维护权限
	private int readerp;			//读者维护权限
	private int parameterp;			//参数维护维护权限
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
