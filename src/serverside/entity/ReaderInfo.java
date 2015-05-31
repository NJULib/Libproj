package serverside.entity;

import java.io.Serializable;

public class ReaderInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String readid;		//���߱��
	private String passwd;		//����
	private String name;		//����
	private Integer age;			//����
	
	private String gender;		//�Ա�
	private String address;		//��ͥסַ
	private String tel;			//��ϵ�绰
	private String startdate;	//��������
	private String enddate;		//�ʻ�����ʱ��
	private Integer type;		//��������
	private String major;		//רҵ
	

	private String depart;	//���Ż�����Ժϵ
	//public ReaderInfo(String a){}
	public ReaderInfo(){}
	public ReaderInfo(String readid,String passwd,String name,Integer age,String gender,String address,String tel,String startdate,String enddate,Integer type,String major,String depart){
		this.readid = readid;
		this.passwd = passwd;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.address = address;
		this.tel = tel;
		this.startdate = startdate;
		this.enddate = enddate;
		this.type = type;
		this.major = major;
		this.depart = depart;
	}
	public String getReadid() {
		return readid;
	}
	
	public void setReadid(String readid) {
		this.readid = readid;
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
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}
//	@Override
//	public String toString(){
//		return readid+"  "+name;
//	}
}
