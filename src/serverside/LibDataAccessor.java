package serverside;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.sql.*;

import serverside.entity.BookDetails;
import serverside.entity.BookInLibrary;
import serverside.entity.BorrowInfo;
import serverside.entity.LibraianInfo;
import serverside.entity.ParameterInfo;
import serverside.entity.ReaderInfo;
import util.CurrDateTime;
import util.DaysInterval;

/**
 * ���ݴ�ȡ�������ڴ����ݿ��ж�ȡ�����Ϣ
 */
public class LibDataAccessor {

	private Connection con = null;
	private Statement stmt = null;
	// private ResultSet rs = null;
	private String dbDriver;
	private String dbURL;
	private String dbUser;
	private String dbPassword;

	public LibDataAccessor() {
		DatabaseInfo dbInfo = new DatabaseInfo();
		dbDriver = dbInfo.getDbDriver();
		dbURL = dbInfo.getDbURL();
		dbUser = dbInfo.getDbUser();
		dbPassword = dbInfo.getDbPassword();
		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			log("�Ҳ������ݿ���������");
		}
	}

	/**
	 * ����һ������ָ������ȡ���鼮����ϸ��Ϣ
	 * 
	 * @param args
	 */
	public List getBookDetails(String theField, String theKeyword) {
		List bookDataList = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String pSql = "SELECT * FROM bookdata WHERE " + theField
					+ " LIKE '%" + theKeyword + "%'";

			ResultSet rs = stmt.executeQuery(pSql);
			bookDataList = new ArrayList();
			BookDetails bookDetails;
			String isbn; // ISBN��
			String name; // ����
			String series; // ������
			String authors; // ����
			String publisher; // ������Ϣ,��������ص㡢������������������
			String size; // ͼ�鿪��
			int pages; // ҳ��
			double price; // ����
			String introduction; // ͼ����
			String picture; // ����ͼƬ
			String clnum; // ͼ������
			while (rs.next()) {
				isbn = rs.getString("isbn");
				name = rs.getString("name");
				series = rs.getString("series");
				authors = rs.getString("authors");
				publisher = rs.getString("publisher");
				size = rs.getString("size");
				pages = rs.getInt("pages");
				price = rs.getDouble("price");
				introduction = rs.getString("introduction");
				picture = rs.getString("picture");
				clnum = rs.getString("clnum");
				bookDetails = new BookDetails(isbn, name, series, authors,
						publisher, size, pages, price, introduction, picture,
						clnum);
				bookDataList.add(bookDetails);
			}
			rs.close();
			stmt.close();
			con.close();
			return bookDataList;
		} catch (SQLException e1) {
			log("���ݿ���쳣��" + e1);
			return bookDataList;
		}
	}

	/**
	 * ��������ȡ��ͼ��Ĺݲ���Ϣ
	 * 
	 * @param isbn
	 * @return
	 */
	public List getBookLibInfo(String isbn) {
		List bookLibList = null;
		ResultSet rs = null;
		log("ִ�в鿴�ݲ�ͼ��");
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String pSql = "SELECT * FROM bookinfo WHERE bookinfo.isbn like '%"
					+ isbn + "%' ";
			log(pSql);
			rs = stmt.executeQuery(pSql);
			bookLibList = new ArrayList();
			BookInLibrary bookInLibrary = null;
			String barCode; // ͼ��������
			int status; // �Ƿ��ڹ�,1���ڣ�0������
			String location; // �ݲ�λ��
			String dueReturnDate; // Ӧ������

			while (rs.next()) // ���ÿ����¼
			{
				log("shu");
				barCode = rs.getString("barcode");
				log(barCode);
				status = rs.getInt("status");
				log(status);
				location = rs.getString("location");
				log(location);
				dueReturnDate =rs.getString("duedate");
				log(dueReturnDate);
				bookInLibrary = new BookInLibrary(barCode, status, location,
						dueReturnDate);
				log(bookInLibrary.getDueReturnDate());
				bookLibList.add(bookInLibrary);
			}
			rs.close();
			stmt.close();
			con.close();
			return bookLibList;
		} catch (SQLException e1) {
			log("���ݿ���쳣��" + e1);
			return bookLibList;
		}
	}

	/**
	 * ��������ȡ���û��Ľ�����Ϣ, �������н�����Ϣ ��ʷ������Ϣ ��ǰ������Ϣ
	 */
	public List[] getBorrowInfo(String readerID) {
		// ������������list���б�
		List borrowDataList[] = new ArrayList[2];
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// ��ѯ���ڵĽ�����Ϣ
		String borrowSql = "select * from lendinfo where lendinfo.readerid = '"
				+ readerID + "' and lendinfo.returndate is null";
		log("��ѯ��ǰ������Ϣ��" + borrowSql);
		// ��ѯ��ʷ������Ϣ
		String historySql = "select * from lendinfo where lendinfo.readerid = '"
				+ readerID + "' and lendinfo.returndate is not null";
		log("��ѯ��ʷ������Ϣ��" + historySql);
		/**
		 * 1.##################################################. ��ǰ�Ľ�����Ϣ
		 */
		BorrowInfo borrowInfo = null;
		ResultSet rs1 = null;
		try {
			rs1 = stmt.executeQuery(borrowSql);
			borrowDataList[0] = new ArrayList();
			log("׼���ӽ����rs1��ȡ���ݣ�");
			while (rs1.next()) {
				String readerid = rs1.getString("readerid");
				String bookcode = rs1.getString("bookcode");
				Date borrowDate = rs1.getDate("borrowdate");
				Date dueDate = rs1.getDate("duedate");
				String  returnDate = rs1.getString ("returndate");
				int renew = rs1.getInt("renew");
				int overdays = DaysInterval.getDays(new Date(), dueDate);
				if (overdays > 0) {
					// ���߲�ѯ�Լ��ĸ��˽�����Ϣʱ���ж��Ƿ��ڣ���������ȡϵͳʱ��������lendinfo���ó����ڵ�����
					String overdaySql = "update lendinfo set overduedays ='"
							+ overdays + "' where readerid = '" + readerid
							+ "' and bookcode = '" + bookcode + "'";
					Statement upstmt = con.createStatement();
					upstmt.executeUpdate(overdaySql);
					if (null == upstmt) {
						upstmt.close();
					}
				}
				borrowInfo = new BorrowInfo(readerid, bookcode, borrowDate,
						dueDate, returnDate, renew, overdays);
				log("��������" + borrowInfo.getOverduedays());
				borrowDataList[0].add(borrowInfo);
			}
			log("���ߵ�ǰ����ͼ�飺" + borrowDataList[0].size() + "��");
		} catch (SQLException e) {
			log("��ȡ��ǰ����ͼ����Ϣ����" + e.getMessage());
		} finally {
			try {
				rs1.close();
			} catch (SQLException e) {
				log("�رս����rs1����" + e.getMessage());
			}
		}

		/**
		 * 2.��ʷ������Ϣ
		 */
		ResultSet rs2 = null;
		try {
			rs2 = stmt.executeQuery(historySql);
			borrowDataList[1] = new ArrayList();
			log("׼���ӽ����rs2��ȡ���ݣ�");
			while (rs2.next()) {
				String readerid = rs2.getString("readerid");
				String bookcode = rs2.getString("bookcode");
				Date borrowDate = rs2.getDate("borrowdate");
				Date dueDate = rs2.getDate("duedate");
				String  returnDate = rs2.getString ("returndate");
				int renew = rs2.getInt("renew");
				int overDueDays = rs2.getInt("overduedays");
				double finedMoney = rs2.getDouble("fine");
				borrowInfo = new BorrowInfo(readerid, bookcode, borrowDate,
						dueDate, returnDate, renew, overDueDays, finedMoney);
				borrowDataList[1].add(borrowInfo);
			}
			log("����ͼ�飺" + borrowDataList[0].size() + "��");

		} catch (SQLException e1) {
			log("���ݿ���쳣��" + e1);
		} finally {
			try {
				rs2.close();
			} catch (SQLException e) {
				log("�رս����rs2����" + e.getMessage());
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				log("�رմ������stmt����" + e.getMessage());
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				log("�ر����ݿ�����con����" + e.getMessage());
			}
		}
		return borrowDataList;
	}

	/**
	 * �����ģ����ݹ���Ա��ID�������ѯ�û��Ƿ���ڣ������򷵻������û���
	 * 
	 * @param libid
	 * @param pass
	 * @return
	 */
	public Object getLibarianInfo(String libid, String pass) {
		log("���յ�--����ԱId:" + libid + "���룺" + pass);
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("con��");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		LibraianInfo libInfo = null;
		log("libInfo��");
		String sql = "select * from librarian where libraianid ='" + libid
				+ "' and passwd = '" + pass + "'";
		log("����Ա��¼��"+sql);
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String name = rs.getString("name");
				int bookp = rs.getInt("bookp");
				int readerp = rs.getInt("readerp");
				int parameterp = rs.getInt("parameterp");
				libInfo = new LibraianInfo(libid, pass, name, bookp, readerp,
						parameterp);
				log("������Ϣ:" + libInfo.getBookp());
			} else {
				libInfo = new LibraianInfo();
			}
			log("����Ա��Ϣ������ "+libInfo.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return libInfo;
	}

	/**
	 * �����壺���ݶ��ߵĵ�ID�������ѯ�û��Ƿ���ڣ������򷵻������û���
	 * 
	 * @param readerID
	 * @param pass
	 * @return
	 */
	public ReaderInfo getReaderInfo(String readerID, String pass) {
		log("���յ���" + readerID);
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("con��");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		ReaderInfo readerInfo = null;// = new ReaderInfo();
		log("readerInfo��");
		String sql = null;
		if(null!=pass){
			if("nullpass".equals(pass.trim())){
				sql= "select * from reader where readerid ='" + readerID + "'";
			}else{
				sql= "select * from reader where readerid ='" + readerID + "' and  passwd = '" + pass + "'";
			}
		}
		log(sql);
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String name = rs.getString("name");
				Integer age = rs.getInt("age");
				String gender = rs.getString("gender");
				String address = rs.getString("address");
				String tel = rs.getString("tel");
				String startdate = rs.getString("startdate");
				String enddate = rs.getString("enddate");
				int type = rs.getInt("type");
				String major = rs.getString("major");
				String depart = rs.getString("depart");
				readerInfo = new ReaderInfo(readerID, pass, name, age, gender,
						address, tel, startdate, enddate, type, major, depart);

			} else{
				readerInfo = new ReaderInfo();
			}
			log("���ߣ�"+readerInfo.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return readerInfo;
	}

	/**
	 * ��������ȡ�������鼮����ϸ��Ϣ
	 */
	public List getBooksInfos(String str) {
		List list = new ArrayList();
		String sql = null;
		BookDetails bookDetails = null;
		log("���յ���" + str);
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("con��");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			if ("all".equals(str)) {
				sql = "select * from bookdata";
			} else {
				sql = "select * from bookdata where isbn like '%" + str
						+ "%' or name like '%" + str + "%' or series like '%"
						+ str + "%' or authors like '%" + str
						+ "%' or publisher like '%" + str
						+ "%' or size like '%" + str + "%' or pages  like '%"
						+ str + "%' or price like '%" + str
						+ "%' or introduction like '%" + str
						+ "%' or clnum like '%" + str + "%'";
			}
			log(sql);
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String isbn = rs.getString("isbn");

				String name = rs.getString("name");
				String series = rs.getString("series");
				String authors = rs.getString("authors");
				String publisher = rs.getString("publisher");
				String size = rs.getString("size");
				int pages = Integer.parseInt(rs.getString("pages"));
				double price = Double.parseDouble(rs.getString("price"));
				String introduction = rs.getString("introduction");
				String picture = rs.getString("picture");
				String clnum = rs.getString("clnum");
				/**
				 * log("������"+name+"����ţ�"+series+"���ߣ�"+authors+"�����磺"+publisher+
				 * "������"
				 * +size+"ҳ�룺"+pages+"�۸�"+price+"����˵����"+introduction+"��飺"+
				 * clnum);
				 */
				bookDetails = new BookDetails(isbn, name, series, authors,
						publisher, size, pages, price, introduction, picture,
						clnum);
				list.add(bookDetails);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * �����ߣ�ȡ�ö��ߵ���ϸ��Ϣ
	 * 
	 * @param msg
	 */
	public List getReadersInfos(String str) {
		List list = new ArrayList();
		String sql = null;
		ReaderInfo readerInfo = null;
		log("���յ���" + str);
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("con��");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			if ("all".equals(str)) {
				sql = "select * from reader";
			} else {
				sql = "select * from reader where readerid like '%" + str
						+ "%' or name like '%" + str + "%' or gender like '%"
						+ str + "%' or address like '%" + str
						+ "%' or tel like '%" + str + "%' or startdate like '%"
						+ str + "%' or enddate  like '%" + str
						+ "%' or type like '%" + str + "%'";
			}
			log("����SQL��" + sql);
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String readerid = rs.getString("readerid"); // ���߱��
				String passwd = rs.getString("passwd"); // ����
				String name = rs.getString("name"); // ����
				Integer age = rs.getInt("age");
				String gender = rs.getString("gender"); // �Ա�
				String address = rs.getString("address"); // סַ
				String tel = rs.getString("tel"); // �绰
				String startdate = rs.getString("startdate"); // ��������
				String enddate = rs.getString("enddate"); // ��������
				String strtype = rs.getString("type"); // ��������
				int type = Integer.parseInt(strtype); //
				String major = rs.getString("major");
				String depart = rs.getString("depart");
				readerInfo = new ReaderInfo(readerid, passwd, name, age,
						gender, address, tel, startdate, enddate, type, major,
						depart);
				log("���ߣ�"+readerInfo.getName());
				list.add(readerInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * �����ˣ�ִ��ͼ����Ϣ���޸�
	 */
	public boolean execBookUpdate(BookDetails bookDetails) {
		log("ִ��ͼ���޸�");
		boolean mark = false;
		String isbn = bookDetails.getIsbn(); // ISBN��
		String name = bookDetails.getName(); // ����
		String series = bookDetails.getSeries(); // ������
		String authors = bookDetails.getAuthors(); // ����
		String publisher = bookDetails.getPublisher(); // ������Ϣ,��������ص㡢������������������
		String size = bookDetails.getSize(); // ͼ�鿪��
		int pages = bookDetails.getPages(); // ҳ��
		double price = bookDetails.getPrice(); // ����
		String introduction = bookDetails.getIntroduction(); // ͼ����
		String picture = bookDetails.getPicture(); // ����ͼƬ
		String clnum = bookDetails.getClnum(); // ͼ������
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			Statement st = con.createStatement();
			String sql = "update bookdata set name='" + name + "',series = '"
					+ series + "',authors = '" + authors + "',publisher = '"
					+ publisher + "',size='" + size + "',pages =" + pages
					+ ",price = " + price + ",introduction='" + introduction
					+ "',picture = '" + picture + "',clnum= '" + clnum
					+ "' where isbn = '" + isbn + "'";
			log("ִ�е�sqlΪ��" + sql);
			int m = st.executeUpdate(sql);
			if (m > 0) {
				mark = true;
			} else {
				mark = false;
			}
		} catch (SQLException e) {
			log("SQL����" + e.getMessage());
			mark = false;
		}
		return mark;
	}

	/**
	 * �����ţ��޸Ķ�����Ϣ
	 * 
	 * @return
	 */
	public boolean execReaderUpdate(ReaderInfo readerInfo) {
		log("ִ�ж�����Ϣ�޸�");
		boolean mark = false;
		String readerid = readerInfo.getReadid(); // ���߱��
		//String passwd = readerInfo.getPasswd(); // ����
		String name = readerInfo.getName(); // ����
		int age = readerInfo.getAge();
		String gender = readerInfo.getGender(); // �Ա�
		String address = readerInfo.getAddress(); // סַ
		String tel = readerInfo.getTel(); // �绰
		String startdate = readerInfo.getStartdate(); // ��������
		String enddate = readerInfo.getEnddate(); // ��������
		int type = readerInfo.getType(); // ��������
		String major = readerInfo.getMajor();
		String depart = readerInfo.getDepart();
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			Statement st = con.createStatement();
			String sql = "update reader set name = '"
					+ name + "',age = "+age+",gender = '" + gender + "',address = '"
					+ address + "',tel='" + tel + "',startdate =" + startdate
					+ ",enddate = " + enddate + ",type=" + type
					+ ",major = '"+major+"',depart = '"+depart+"' where readerid = '" + readerid + "'";
			log("ִ�е�sqlΪ��" + sql);
			int m = st.executeUpdate(sql);
			if (m > 0) {
				mark = true;
			} else {
				mark = false;
			}
		} catch (SQLException e) {
			log("SQL����" + e.getMessage());
			mark = false;
		}
		return mark;
	}

	/**
	 * ����ʮ�����ͼ����Ϣ
	 * 
	 */
	public boolean execBookDataInsert(BookDetails bookDetails) {
		log("������������鼮��Ϣ");
		boolean mark = false;
		/**
		 * bookdata�����Ϣ
		 */
		String isbn = bookDetails.getIsbn();
		String name = bookDetails.getName();
		String authors = bookDetails.getAuthors();
		String series = bookDetails.getSeries();
		String publisher = bookDetails.getPublisher();
		int pages = bookDetails.getPages();
		String sizes = bookDetails.getSize();
		double price = bookDetails.getPrice();
		String clunm = bookDetails.getClnum();
		String introduction = bookDetails.getIntroduction();
		String image = bookDetails.getPicture();

		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String sqlBookData = "insert into bookdata(isbn,name,series,authors,publisher,size,pages,price,introduction,picture,clnum) values ('"
					+ isbn
					+ "','"
					+ name
					+ "','"
					+ series
					+ "','"
					+ authors
					+ "','"
					+ publisher
					+ "','"
					+ sizes
					+ "',"
					+ pages
					+ ","
					+ price
					+ ",'"
					+ introduction
					+ "','"
					+ image
					+ "','"
					+ clunm + "')";
			log("ִ�е�sqlBookDataΪ��" + sqlBookData);

			int m = stmt.executeUpdate(sqlBookData);
			if (m > 0) {
				mark = true;
			} else {
				mark = false;
			}
		} catch (SQLException e) {
			log("SQL����" + e.getMessage());
			mark = false;
		}finally{
			try{
			stmt.close();
			con.close();
			}catch(SQLException e){
				log("�ر�sql�쳣"+e.getMessage());
			}
		}
		return mark;
	}
	//�ݲز���
	public boolean execBookInfoInsert(BookInLibrary bookInLibrary) {
		log("����������ӹݲ���Ϣ");
		boolean mark = false;
		/**
		 * bookinfo�����Ϣ
		 */
		String barCode = bookInLibrary.getBarCode();
		if(barCode.length()<5){
			int num = 5 - barCode.length();
			for(int i=0;i<num;i++){
				barCode = "0"+barCode;
			}
		}
		String isbn = bookInLibrary.getIsbn();
		String location = bookInLibrary.getLocation();
		String introdate =DateFormat.getDateInstance().format(new Date());
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
		String sqlBookInfo = "insert into bookinfo(barcode,isbn,status,location,introducetime) values ('"
			+ barCode
			+ "','"
			+ isbn
			+ "',"
			+ 1
			+ ",'"
			+ location+ "','"+introdate+"')";
		log("ִ�е�sqlBookInfoΪ��" + sqlBookInfo);
		int n = stmt.executeUpdate(sqlBookInfo);
		if(n>0){
			mark = true;
		}else{
			mark = false;
		}
	} catch (SQLException e) {
		log("SQL����" + e.getMessage());
		mark = false;
	}finally{
		try{
			stmt.close();
			con.close();
			}catch(SQLException e){
				log("�ر�sql�쳣"+e.getMessage());
			}
	}
		return  mark;
	}
	
	/**
	 * ����ʮһ����Ӷ�����Ϣ
	 */
	public boolean execReaderInsert(ReaderInfo readerInfo) {
		log("��������Ӷ�����Ϣ");
		boolean mark = false;
		String readerid = readerInfo.getReadid(); // ���߱��
		String passwd = readerInfo.getPasswd(); // ����
		String name = readerInfo.getName(); // ����
		int age = readerInfo.getAge();
		String gender = readerInfo.getGender(); // �Ա�
		String address = readerInfo.getAddress(); // סַ
		String tel = readerInfo.getTel(); // �绰
		String startdate = readerInfo.getStartdate(); // ��������
		String enddate = readerInfo.getEnddate(); // ��������
		int type = readerInfo.getType(); // ��������
		String major = readerInfo.getMajor();
		String depart = readerInfo.getDepart();
		
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			Statement st = con.createStatement();
			String sqlBookData = "insert into reader(readerid,passwd,name,age,gender,address,tel,startdate,enddate,type,major,depart) values ('"
					+ readerid
					+ "','"
					+ passwd
					+ "','"
					+ name
					+ "',"+age+",'"
					+ gender
					+ "','"
					+ address
					+ "','"
					+ tel
					+ "','"
					+ startdate
					+ "','"
					+ enddate + "'," + type + ",'"+major+"','"+depart+"')";
			log("ִ�е�sqlBookDataΪ��" + sqlBookData);

			int m = st.executeUpdate(sqlBookData);
			if (m > 0) {
				mark = true;
			} else {
				mark = false;
			}
		} catch (SQLException e) {
			log("SQL����" + e.getMessage());
			mark = false;
		}
		return mark;
	}

	/**
	 * ����ʮ��������ͼ���������ȡ��ͼ�����ϸ��Ϣ
	 */
	public BookDetails getBookBarDetails(String barCode) {
		BookDetails bookDetails = null;
		ResultSet rs = null;
		String isbn = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from bookinfo where barcode = '"
					+ barCode + "'";
			log("ȡ��ָ�������ͼ��isbn��" + sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				isbn = rs.getString("isbn");
				log("isbn = :" + isbn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();

				stmt.close();
				con.close();
			} catch (Exception e) {
				log("�رս��������" + e.getMessage());
			}
		}
		bookDetails = getIsbnBookDetails(isbn);
		return bookDetails;
	}

	/**
	 * ����ʮ��������ͼ��isbn����ͼ����Ϣ
	 * 
	 * @param msg
	 */
	private BookDetails getIsbnBookDetails(String isbn) {
		log("����isbn����ͼ�飺" + isbn);
		ResultSet rs = null;
		BookDetails bookDetails = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String bookSql = "select * from bookdata where isbn like '%" + isbn
					+ "%'";
			log("bookSql = :" + bookSql);
			rs = stmt.executeQuery(bookSql);
			if (rs.next()) {
				String name = rs.getString("name");
				String series = rs.getString("series");
				String authors = rs.getString("authors");
				String publisher = rs.getString("publisher");
				String size = rs.getString("size");
				int pages = Integer.parseInt(rs.getString("pages"));
				double price = Double.parseDouble(rs.getString("price"));
				String introduction = rs.getString("introduction");
				String picture = rs.getString("picture");
				String clnum = rs.getString("clnum");
				bookDetails = new BookDetails(isbn, name, series, authors,
						publisher, size, pages, price, introduction, picture,
						clnum);
			}
		} catch (SQLException e) {
			log("����isbnȡ��ͼ����ϸ��Ϣ����"+e.getMessage());
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				log("�رս��������" + e.getMessage());
			}
		}
		return bookDetails;
	}

	/**
	 * ����ʮ�ģ������޸�����
	 * 
	 * @param readerid
	 * @param pass
	 * @return
	 */
	public boolean execPassModify(String readerid, String pass) {
		log("���յ����߱��" + readerid + "\t���룺" + pass);
		boolean modify = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try{
			stmt = con.createStatement();
			String sql = "update reader set passwd = '"+pass+"' where readerid = '"+readerid+"'";
			log(sql);
			int num = stmt.executeUpdate(sql);
			log("��Ӱ���������"+num);
			if(num>0){
				modify = true;
			}
			else{
				modify = false;
			}
		}catch(SQLException e){
			log("�޸������쳣"+e.getMessage());
			modify = false;
		}finally{
			try{stmt.close();
			con.close();
			}catch(SQLException e){
				log("�޸�����ʱ�ر�sql�쳣��"+e.getMessage());
			}
		}
		return modify;
	}

	
	/**
	 * ����ʮ�壺ʵ�ֶ��߽���
	 */
	public boolean execReaderBorrowBook(String readerid, String barCode) {
		boolean mark = false;
		log("���յ����߱��" + readerid + "\t�����룺" + barCode);
		ReaderInfo readerInfo =(ReaderInfo) getReaderInfo(readerid, "nullpass");
		//��������
		int type = readerInfo.getType();
		log("�������ͣ�"+type);
		//���������Խ���ͼ�������
		int months = getCanBorrowMonths(type);
		log("���Խ��ĵ��ʱ�䣺 "+months+" ����");
		Calendar c = Calendar.getInstance();
		int yearOld = c.get(Calendar.YEAR);
		int monthOld = c.get(Calendar.MONTH)+1;
		int dateOld = c.get(Calendar.DATE);
		String borrowdate = yearOld+"-"+monthOld+"-"+dateOld;
		log("����ʱ�䣺"+borrowdate);
		c.add(Calendar.MONTH,2);
		int yearNew =  c.get(Calendar.YEAR);
		int monthNew = c.get(Calendar.MONTH)+1;
		int dateNew = c.get(Calendar.DATE);
		String duedate = yearNew+"-"+monthNew+"-"+dateNew;
		log("����ʱ�䣺"+duedate);
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try{
			stmt = con.createStatement();
			String upsql = "update bookinfo set status ="+0+",duedate = '"+duedate+"' where barcode = '"+barCode+"'";
			log("upsql = "+upsql);
			int mun = stmt.executeUpdate(upsql);
			String sql = "insert into lendinfo(readerid,bookcode,borrowdate,duedate) values ('"+readerid+"','"+barCode+"','"+borrowdate+"','"+duedate+"')";
			log("sql = "+sql);
			int num = stmt.executeUpdate(sql);
			log("��Ӱ���������"+num);
			if(num>0&&mun>0){
				mark = true;
			}
			else{
				mark = false;
			}
		}catch(SQLException e){
			mark = false;
			log("�޸������쳣"+e.getMessage());			
		}finally{
			try{
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�޸�����ʱ�ر�sql�쳣��"+e.getMessage());
			}
		}
		return mark;
	}
	
	/**
	 * ����ʮ�������ָ�������ͼ���Ƿ�ɽ�
	 * 
	 * @param type
	 * @return
	 */
	public boolean checkBarBookCanBorrow(String barCode) {
		boolean mark = false;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try{
			stmt = con.createStatement();
			String sql = "select * from bookinfo where barcode ='"+barCode+"' and status = "+0;
			log("ָ����������Ƿ���ã�"+sql);
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				mark = true;	//ͼ�鲻�ɽ�
			}
			else{
				mark = false;
			}
		}catch(SQLException e){
			mark = false;
			log("�޸������쳣"+e.getMessage());			
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�޸�����ʱ�ر�sql�쳣��"+e.getMessage());
			}
		}
		return mark;
	}
	
	/**
	 * ����ʮ�ߣ����ָ�������ͼ���Ƿ����
	 * 
	 */
	public boolean checkBarBookExists(String barCode) {
		boolean mark = false;
		ResultSet rs = null;
		log("ִ��ָ�������ͼ���Ƿ����");
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try{
			stmt = con.createStatement();
			String sql = "select * from bookinfo where barcode ='"+barCode+"'";
			log("���ָ������ͼ���Ƿ���ڣ�"+sql);
			rs = stmt.executeQuery(sql);
			//ָ�������ͼ���Ƿ���ڣ����rs.next()�������
			if(rs.next()){
				//����
				mark = true;	
			}
			else{
				//������
				mark = false;
			}
			log("ͼ���Ƿ���ڣ�"+mark);
		}catch(SQLException e){
			mark = false;
			log("���ָ������ͼ���Ƿ���ڣ�"+e.getMessage());			
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�޸�����ʱ�ر�sql�쳣��"+e.getMessage());
			}
	}
		return mark;
}
	
	
	
	/**
	 * ����ʮ�ˣ�����
	 * 
	 */
	public BorrowInfo returnBook(String barCode) {
		BorrowInfo  borrowInfo = null;
		Statement stmt1=null,stmt2=null,stmt3=null;
		ResultSet rs = null;
		String  readerID = null;
		Date  borrowDate=null,duedate=null;
		int overDueDays = 0;// ��������
		double finedMoney = 0;// ������
		int renew =0;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt1 = con.createStatement();
			stmt2 = con.createStatement();
			stmt3 = con.createStatement();
			//����bookinfo��ʹ����ͼ��״̬Ϊ���Խ���
			String upbookinfosql = "update bookinfo set status = "+1+",duedate = null where barcode = '"
				+ barCode + "'";
			log("����bookinfo"+upbookinfosql);
			int booknum = stmt1.executeUpdate(upbookinfosql);
			log("����bookinfo"+booknum+"����¼");
			
			//����
			String sql = "select * from lendinfo where bookcode = '"+barCode+"'";
			log("����"+sql);
			Calendar calendar = Calendar.getInstance();
			String currDate = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);//����ʱ��
			rs = stmt2.executeQuery(sql);
			if(rs.next()){
				 readerID = rs.getString("readerid");
				ReaderInfo readerInfo = getReaderInfo(readerID, "nullpass");
				log("����bar"+readerInfo.getName());
				borrowDate = rs.getDate("borrowdate");
				duedate = rs.getDate("duedate");
				int type = readerInfo.getType();
				renew = rs.getInt("renew");
				double dailyfine = getCanFireMoney(type);
				overDueDays = DaysInterval.getDays(new Date(), duedate);
				if(overDueDays==0){
					finedMoney = 0.0;
				}
				else{
					finedMoney = overDueDays*dailyfine;
				}
			}	
			//����lendinfo��
			String uplendinfosql = "update lendinfo set returndate = '" + currDate
				+ "',overduedays = '" + overDueDays + "',fine = '"
				+ finedMoney + "' WHERE bookcode ='" + barCode
				+ "' AND returndate is null";
			log("����lendinfo"+uplendinfosql);
			int lendnum = stmt3.executeUpdate(uplendinfosql);
			log("����lendinfo"+lendnum+"����¼");
			Date returndate = null;
//			try {
//				returndate = new SimpleDateFormat("yyyy-MM-dd").parse(currDate);
//			} catch (ParseException e) {
//				log("����ת������"+e.getMessage());
//			}
			borrowInfo = new BorrowInfo(readerID,barCode,borrowDate, duedate,currDate,renew,overDueDays, finedMoney);
			log("������Ϣ��"+borrowInfo.getReaderId());
		} catch (SQLException e1) {
			log("���ݿ���쳣��" + e1.getMessage());
		}finally{
			try{
				rs.close();
				stmt1.close();
				stmt2.close();
				stmt3.close();
				con.close();
			}catch(SQLException e){
				log("�޸�����ʱ�ر�sql�쳣��"+e.getMessage());
			}
	}
		return borrowInfo;
	}
	/**
	 *  ����ʮ�ţ�����ָ������鿴���ĸ���Ķ��ߵ���Ϣ
	 */
	public ReaderInfo getReaderInfoByBarcode(String barCode) {
		ReaderInfo readerInfo = null;
		ResultSet rs = null;
		String readerId = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from lendinfo where bookcode = '"+barCode+"'";
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				readerId = rs.getString("readerid");
				readerInfo = getReaderInfo(readerId, "nullpass");
			}else{
				readerInfo = new ReaderInfo();
			}
		}catch(SQLException e){
			log("��������ȡ������Ϣ����"+e.getMessage());
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�޸�����ʱ�ر�sql�쳣��"+e.getMessage());
			}
		}
		return readerInfo;
	}
	
	/**
	 * ������ʮ�����ָ�������ͼ���Ƿ����ڹݲ���
	 * @param barCode
	 * @return
	 */
	public Boolean getBarCodeIsBorrowed(String barCode) {
		boolean mark = false;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from bookinfo where barcode ='"+barCode+"' and status = "+1;
			log("���ָ������ͼ���Ƿ����ڹݲ���"+sql);
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				mark = true;
			}else{
				mark = false;
			}
		}catch(SQLException e){
			mark = false;
			log("���ָ�������ͼ���Ƿ����ڹݲ��У�"+e.getMessage());
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�޸�����ʱ�ر�sql�쳣��"+e.getMessage());
			}
		}
		return mark;
	}

	/**
	 * ������ʮһ�����ָ�������ͼ���Ƿ������
	 * @param barCode
	 * @return
	 */
	public int getBarcodeIsRenewed(String barCode) {
		int renew = 0;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from lendinfo where bookcode ='"+barCode+"'";
			log("���ָ�������ͼ���Ƿ������:"+sql);
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				renew  = rs.getInt("renew");
			}else{
				renew = 0;
			}
		}catch(SQLException e){
			renew = 0;
			log("���ָ�������ͼ���Ƿ�������쳣��"+e.getMessage());
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�޸�����ʱ�ر�sql�쳣��"+e.getMessage());
			}
		}
		return renew;
	}
	
	/**
	 * ������ʮ��������ͼ��
	 * 
	 */
	public boolean execRenewBook(String readerid, String barCode) {
		boolean mark = false;
		Statement stmt1 = null,stmt2 = null;
		ResultSet rs=null;
		Date duedate = null;
		String [] dateStr= {"0000","00","00"};
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from lendinfo where readerid = '"+readerid+"' and  bookcode ='"+barCode+"'";
			log("����ͼ��:"+sql);
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				//ȡ��Ӧ������
				duedate = rs.getDate("duedate");
				log("Ӧ������:"+duedate);
			}else{
				mark = false;
				return mark;
			}
			if(null!=duedate){
				String duedateStr = new SimpleDateFormat("yyyy-MM-dd").format(duedate);
				log("ת�����ڣ�"+duedateStr);
				if(null==duedateStr||!(duedateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}"))){
					mark = false;
				}
				String [] dueArray= new String[3];
				dueArray = duedateStr.split("-");
				dateStr[0] = dueArray[0];
				log("�꣺"+dateStr[0]);
				dateStr[1] = dueArray[1];
				log("�£�"+dateStr[1]);
				dateStr[2] = dueArray[2];
				log("�գ�"+dateStr[2]);
				int year = Integer.parseInt(dueArray[0]);
				log("�꣺"+year);
				int month = Integer.parseInt(dueArray[1]);
				log("�£�"+month);
				int day = Integer.parseInt(dueArray[2]);
				log("�գ�"+day);
				Calendar calendar = Calendar.getInstance();
				//Calendar�����ꡢ�¡���
				log("�����ꡢ�¡���");
				calendar.set(year, month, day);
				log("�������");
				//��ǰʱ�����·ݵĻ����ϼ�������
				calendar.add(Calendar.MONTH, 2);
				log("�������õ��ꡢ�¡���");
				String newduedate = calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE);
				log("�����黹�����ڣ�"+newduedate);
				//����lendinfo���ݱ�
				String lendinfosql = "update lendinfo set duedate = '"+newduedate+"', renew = 1 where readerid = '"+readerid+"' and bookcode = '"+barCode+"'";
				//����ɹ������lendinfo��
				log("����ɹ������lendinfo��:"+lendinfosql);
				stmt1 = con.createStatement();
				int num = stmt1.executeUpdate(lendinfosql);
				log("����lendinfo�м�¼��Ŀ��"+num);
				//��������bookinfo������ͼ�鵽��ʱ��
				stmt2 = con.createStatement();
				String upsql = "update bookinfo set duedate = '"+newduedate+"' where barcode = '"+barCode+"'";
				log("upsql = "+upsql);
				int mun = stmt2.executeUpdate(upsql);
				log("����bookinfo�м�¼��Ŀ��"+mun);
				mark = true;
			}else{
				mark = false;
			}
		}catch(SQLException e){
			mark = false;
			log("���ָ�������ͼ���Ƿ�������쳣��"+e.getMessage());
		}finally{
			try{
				rs.close();
				stmt1.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�޸�����ʱ�ر�sql�쳣��"+e.getMessage());
			}
		}
		return mark;
	}
	
	/**
	 * ������ʮ����ͼ��ͳ��
	 * @param userid
	 * @param password
	 * @return
	 */
	public List getBookCountList() {
		List<Integer> countList = new ArrayList<Integer>();
		ResultSet countrs = null,commrs = null,borrowedrs = null,inlibrs = null,lostrs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			//�ܹݲ���Ŀ
			String countsql = "select count(*) from bookinfo";
			countrs = stmt.executeQuery(countsql);
			if(countrs.next()){
				countList.add(Integer.parseInt(countrs.getString(1)));
			}else{
				countList.add(0);
			}
			//ͼ����ͨ����
			String commsql =" select count(*) from lendinfo";
			commrs = stmt.executeQuery(commsql);
			if(commrs.next()){
				countList.add(Integer.parseInt(commrs.getString(1)));
			}
			else{
				countList.add(0);
			}
			//�����ߵ�ͼ����Ŀ
			String borrowedsql = "select count(*) from bookinfo where status = 0";
			borrowedrs = stmt.executeQuery(borrowedsql);
			if(borrowedrs.next()){
				countList.add(Integer.parseInt(borrowedrs.getString(1)));
			}else{
				countList.add(0);
			}
			//�ڹݲص���Ŀ
			String inlibsql = "select count(*) from bookinfo where status = 1 ";
			inlibrs = stmt.executeQuery(inlibsql);
			if(inlibrs.next()){
				countList.add(Integer.parseInt(inlibrs.getString(1)));
			}else{
				countList.add(0);
			}
			String lostsql = "select count(*) from bookinfo where status = -1 ";
			lostrs = stmt.executeQuery(lostsql);
			if(lostrs.next()){
				countList.add(Integer.parseInt(lostrs.getString(1)));
			}else{
				countList.add(0);
			}
		} catch (SQLException e1) {
			log("���ݿ���쳣��" + e1);
		}finally{
			try{
				countrs.close();
			
			borrowedrs.close();
			inlibrs.close();
			lostrs.close();}catch(SQLException e){
				log("�ر����ݿ������쳣"+e.getMessage());
			}
		}
		return countList;
	}
	
	/**
	 * ������ʮ�ģ�����ͼ��
	 */
	public List getOverDueBooks() {
		List<String> blist = new ArrayList<String>();
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from lendinfo";
			System.out.println("sql = "+sql);
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				Date returnDate = rs.getDate("returndate");
				Date dutDate = rs.getDate("duedate");
				if(null==returnDate){
					if(dutDate.before(new Date())){
						blist.add(rs.getString("bookcode")+":"+dutDate);
					}
				}
			}
		}catch (SQLException e1) {
			log("���ݿ���쳣��" + e1.getMessage());
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�޸�����ʱ�ر�sql�쳣��"+e.getMessage());
			}
		}
		return blist;
	}
	
	/**
	 * ������ʮ�壺ͼ�����
	 * @param flag
	 */

	public boolean outLib(String isbn) {
		boolean mark = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String booksql = "delete from bookdata where isbn ='" + isbn+"'";
			log(booksql);
			int m  = stmt.executeUpdate(booksql);
			String lendsql = "delete from bookinfo where isbn ='" + isbn+"'";
			int n  = stmt.executeUpdate(lendsql);
			if (m>0&&n>0) {
				mark = true;
			}else{
				mark = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�رս��������"+e.getMessage());
			}
		}
		return mark;
	}

	/**
	 * ������ʮ��������isbnȡ��ͼ����ϸ��Ϣ
	 */
	public BookDetails getBookIsbnDetails(String isbn) {
		BookDetails bookDetails = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String booksql = "select  * from bookdata where isbn ='" + isbn+"'";
			log(booksql);
			rs = stmt.executeQuery(booksql);
			if(rs.next()){
				String name = rs.getString("name");
				String series = rs.getString("series");
				String authors = rs.getString("authors");
				String publisher = rs.getString("publisher");
				String size= rs.getString("size");
				int pages= rs.getInt("pages");
				double 	price= rs.getDouble("price");
				String 	introduction= rs.getString("introduction");
				String picture= rs.getString("picture");
				String clnum= rs.getString("clnum");		
				bookDetails  = new BookDetails(isbn, name, series, authors, publisher, size, pages, price, introduction, picture, clnum);
			}else{
				bookDetails = new BookDetails();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�رս��������"+e.getMessage());
			}
		}
		return bookDetails;
	}
	

	/**
	 * ������ʮ�ߣ�ɾ��ָ����ŵĶ���
	 */

	public boolean execReaderDelete(String readerid) {
		boolean mark = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			//ɾ������
			String ksql = "delete from reader where readerid ='" + readerid+"'";
			log("ɾ������:"+ksql);
			int m = stmt.executeUpdate(ksql);
			//ɾ�����ߵĽ�����Ϣ
			String rbsql = "delete from lendinfo where  readerid ='" + readerid+"'";
			log("ɾ�����ߵĽ�����Ϣ"+rbsql);
			stmt.executeUpdate(rbsql);
			if(m>0){
				mark = true;
			}else{
				mark = false;
			}
				
		}catch (SQLException e) {
			mark = false;
			e.printStackTrace();
		}finally{
			try{
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�رս��������"+e.getMessage());
			}
		}
		return mark;
	}
	/**
	 * ����M��ȡ�ÿ��Խ��ĵ�ͼ����Ŀ
	 * 
	 * @param type
	 * @return
	 */
	public int getCanBorrowAccount(int type) {
		int account = 0;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String sql = "select amount from parameter where type =" + type;
			log(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				account = rs.getInt(1);
			}
			log(account);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�رս��������"+e.getMessage());
			}
		}
		return account;
	}

	/**
	 * ����M+1��ȡ��ÿ�յķ���
	 * 
	 * @param type
	 * @return
	 */
	public double getCanFireMoney(int type) {
		double money = 0.0;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from parameter where type =" + type;
			log(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				money = rs.getDouble("dailyfine");
			}
			log("ÿ�췣��"+money);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�رս��������"+e.getMessage());
			}
		}
		return money;
	}
	
	/**
	 * ����M+2��ȡ������Խ��ĵ�����
	 * 
	 * @param type
	 * @return
	 */
	public int getCanBorrowMonths(int type) {
		int months = 0;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			log("�������ݿ����ӳɹ�");
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String sql = "select period from parameter where type =" + type;
			log(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				months = rs.getInt(1);
			}
			log(months);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				log("�رս��������"+e.getMessage());
			}
		}
		return months;
	}
	
	
	
	
	
	public boolean addMaster(String userid, String password, String name,
			int state1, int state2, int state3) {
		boolean b = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String Isql = "INSERT INTO librarian(libraianid,passwd,name,bookp,readerp,parameterp) values('"
					+ userid
					+ "','"
					+ password
					+ "','"
					+ name
					+ "',"
					+ state1
					+ "," + state2 + "," + state3 + ")";
			System.out.println(Isql);
			int m = stmt.executeUpdate(Isql);
			if (m == 1) {
				b = true;
			}
			stmt.close();
			con.close();
			return b;
		} catch (SQLException e1) {
			log("���ݿ���쳣��" + e1.getMessage());
			return b;
		}
	}

	public ArrayList getMaster() {
		ArrayList<LibraianInfo>  managerList =null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String psql = "select * from librarian";
			rs = stmt.executeQuery(psql);
			managerList = new ArrayList<LibraianInfo>();
			LibraianInfo librarianInfo = null;
			String userid;
			String passwd;
			String name;
			int bookp;
			int readerp;
			int parameterp;
			while (rs.next()) {
				userid = rs.getString("libraianid");
				passwd = rs.getString("passwd");
				name = rs.getString("name");
				bookp = rs.getInt("bookp");
				readerp = rs.getInt("readerp");
				parameterp = rs.getInt("parameterp");
				librarianInfo = new LibraianInfo(userid, passwd, name, bookp, readerp, parameterp);
				managerList.add(librarianInfo);
			}
			rs.close();
			stmt.close();
			con.close();
			return managerList;
		} catch (SQLException e1) {
			log("���ݿ���쳣��" + e1.getMessage());
			return managerList;
		}
	}

	public boolean modifyMaster(String userid, String password, String name,
			int state1, int state2, int state3) {
		boolean b = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String UPsql;
			String passwd = password;
			if (passwd == null || passwd.equals("")) {
				UPsql = "UPDATE librarian SET name = '" + name + "',bookp = "
						+ state1 + ",readerp = " + state2 + ",parameterp = "
						+ state3 + " WHERE libraianid = '" + userid + "'";
			} else {
				UPsql = "UPDATE librarian SET passwd = '" + password
						+ "',name = '" + name + "',bookp = " + state1
						+ ",readerp = " + state2 + ",parameterp = " + state3
						+ " WHERE libraianid = '" + userid + "'";
			}
			int m = stmt.executeUpdate(UPsql);
			if (m == 1) {
				b = true;
			}
			stmt.close();
			con.close();
			return b;
		} catch (SQLException e1) {
			log("���ݿ���쳣��" + e1.getMessage());
			return b;
		}
	}

	public boolean dellMaster(String userid) {
		boolean b = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String Dsql = "DELETE FROM librarian WHERE libraianid = '" + userid+ "'";
			System.out.println(Dsql);
			int m = stmt.executeUpdate(Dsql);
			if (m == 1) {
				b = true;
			}
			stmt.close();
			con.close();
			return b;
		} catch (SQLException e1) {
			log("���ݿ���쳣��" + e1.getMessage());
			return b;
		}
	}
	/**
	 * ȡ�����еĲ�����Ϣ
	 * @param parameterID
	 * @return
	 */
	public ArrayList getParamterInfo(String parameterID) {
		ArrayList paraList = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String pSql = "SELECT * FROM parameter";

			rs = stmt.executeQuery(pSql);
			paraList = new ArrayList();
			ParameterInfo parameterInfo = null;
			int id; // ������id��
			int type; // �������
			int amount; // ��������
			int period; // ��������
			double dailyfine = 0; // ����ÿ�շ�����
			while (rs.next()) // ���ÿ����¼
			{
				id = rs.getInt("id");
				type = rs.getInt("type");
				amount = rs.getInt("amount");
				period = rs.getInt("period");
				dailyfine = rs.getDouble("dailyfine");
				parameterInfo = new ParameterInfo(id, type, amount, period,
						dailyfine);

				paraList.add(parameterInfo);

			}
			System.out.println(String.valueOf(dailyfine));

			rs.close();
			stmt.close();
			con.close();
			return paraList;
		} catch (SQLException e1) {
			log("���ݿ���쳣��" + e1);
			return paraList;
		}
	}
	
	/**
	 * ���ҹ���Ա��Ϣ
	 * @param userID
	 * @param librarianPW
	 * @return
	 */
//	public LibraianInfo getLibrariaInfo(String userID, String librarianPW) {
//		LibraianInfo librariaInfo = null;
//		ResultSet rs = null;
//		try {
//			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
//		} catch (SQLException ee) {
//			log("�������ݿ�����ʧ��!");
//		}
//		try {
//			stmt = con.createStatement();
//			String pSqlSel = "SELECT librarian.userid, librarian.passwd";
//			String pSqlFrom = " FROM librarian";
//			String pSqlWhere = " WHERE librarian.userid = '" + userID
//					+ "' AND librarian.passwd = '" + librarianPW + "'";
//			String pSql = pSqlSel + pSqlFrom + pSqlWhere;
//			rs = stmt.executeQuery(pSql);
//
//			if (rs.next()) {
//				librariaInfo = new LibraianInfo(userID, librarianPW);
//			} else {
//				log("�û���ȡ��Ϣ�쳣��");
//			}
//			rs.close();
//			stmt.close();
//			con.close();
//			return librariaInfo;
//		} catch (SQLException e1) {
//			log("���ݿ���쳣��" + e1);
//			librariaInfo = null;
//			return librariaInfo;
//		}
//	}

	/**
	 * ���²�����Ϣ
	 */
	public boolean updateParameter(int type, int amount, int period,double dailyfine) {
		boolean b = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			log("�������ݿ�����ʧ��!");
		}
		try {
			stmt = con.createStatement();
			String UPsql = "UPDATE parameter SET amount = " + amount
					+ ",period = " + period + ",dailyfine = " + dailyfine
					+ " WHERE type = " + type;
			int m = stmt.executeUpdate(UPsql);

			if (m ==1) {

				b= true;
			} else {
				log("�û���ȡ��Ϣ�쳣��");
			}
			stmt.close();
			con.close();
			return b;
		} catch (SQLException e1) {
			log("���ݿ���쳣��" + e1);
			return b;
		}
	}
//��¼��־�ķ���
protected void log(Object msg) {
	System.out.println(CurrDateTime.currDateTime() + "LibDataAccessorr�� ��"
			+ msg);
}



}
