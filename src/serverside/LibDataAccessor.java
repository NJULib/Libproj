package serverside;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.sql.*;

import serverside.entity.BookDetails;
import serverside.entity.BookInLibrary;
import serverside.entity.BorrowInfo;
import serverside.entity.LibraianInfo;
import serverside.entity.ReaderInfo;
import util.DaysInterval;

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
			System.out.print("找不到数据库驱动程序");
		}
	}
	
	public List getBookDetails(String theField, String theKeyword) {
		List bookDataList = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
		}
		try {
			stmt = con.createStatement();
			String pSql = "SELECT * FROM bookdata WHERE " + theField
					+ " LIKE '%" + theKeyword + "%'";

			ResultSet rs = stmt.executeQuery(pSql);
			bookDataList = new ArrayList();
			BookDetails bookDetails;
			String isbn; // ISBN号
			String name; // 书名
			String series; // 丛书名
			String authors; // 作者
			String publisher; // 出版信息,包括出版地点、出版社名、出版日期
			String size; // 图书开本
			int pages; // 页数
			double price; // 定价
			String introduction; // 图书简介
			String picture; // 封面图片
			String clnum; // 图书分类号
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
			System.out.print("数据库读异常，" + e1);
			return bookDataList;
		}
	}
	
	//读者登录查找数据库
	public ReaderInfo getReaderInfo(String readerID, String pass) {
		System.out.print("接收到：" + readerID);
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("con：");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
		}
		ReaderInfo readerInfo = null;// = new ReaderInfo();
		System.out.print("readerInfo：");
		String sql = null;
		if(null!=pass){
			if("nullpass".equals(pass.trim())){
				sql= "select * from reader where readerid ='" + readerID + "'";
			}else{
				sql= "select * from reader where readerid ='" + readerID + "' and  passwd = '" + pass + "'";
			}
		}
		System.out.print(sql);
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
			System.out.print("读者："+readerInfo.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return readerInfo;
	}
	
	public Object getLibarianInfo(String libid, String pass) {
		System.out.print("接收到--管理员Id:" + libid + "密码：" + pass);
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("con：");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
		}
		LibraianInfo libInfo = null;
		System.out.print("libInfo：");
		String sql = "select * from librarian where libraianid ='" + libid
				+ "' and passwd = '" + pass + "'";
		System.out.print("管理员登录："+sql);
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
				System.out.print("管理信息:" + libInfo.getBookp());
			} else {
				libInfo = new LibraianInfo();
			}
			System.out.print("管理员信息：姓名 "+libInfo.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return libInfo;
	}
	
	public List getBooksInfos(String str) {
		List list = new ArrayList();
		String sql = null;
		BookDetails bookDetails = null;
		System.out.print("接收到：" + str);
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("con：");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
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
			System.out.print(sql);
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
				 * log("书名："+name+"丛书号："+series+"作者："+authors+"出版社："+publisher+
				 * "开本："
				 * +size+"页码："+pages+"价格："+price+"出版说明："+introduction+"简介："+
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
	
	public List getBookLibInfo(String isbn) {
		List bookLibList = null;
		ResultSet rs = null;
		System.out.print("执行查看馆藏图书");
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("建立连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
		}
		try {
			stmt = con.createStatement();
			String pSql = "SELECT * FROM bookinfo WHERE bookinfo.isbn like '%"
					+ isbn + "%' ";
			System.out.print(pSql);
			rs = stmt.executeQuery(pSql);
			bookLibList = new ArrayList();
			BookInLibrary bookInLibrary = null;
			String barCode; // 图书条形码
			int status; // 是否在馆,1：在，0：不在
			String location; // 馆藏位置
			String dueReturnDate; // 应还日期

			while (rs.next()) // 输出每条记录
			{
				System.out.print("shu");
				barCode = rs.getString("barcode");
				System.out.print(barCode);
				status = rs.getInt("status");
				System.out.print(status);
				location = rs.getString("location");
				System.out.print(location);
				dueReturnDate =rs.getString("duedate");
				System.out.print(dueReturnDate);
				bookInLibrary = new BookInLibrary(barCode, status, location,
						dueReturnDate);
				System.out.print(bookInLibrary.getDueReturnDate());
				bookLibList.add(bookInLibrary);
			}
			rs.close();
			stmt.close();
			con.close();
			return bookLibList;
		} catch (SQLException e1) {
			System.out.print("数据库读异常，" + e1);
			return bookLibList;
		}
	}
	
	public List[] getBorrowInfo(String readerID) {
		// 建立具有两个list的列表
		List borrowDataList[] = new ArrayList[2];
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
		}
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 查询现在的借阅信息
		String borrowSql = "select * from lendinfo where lendinfo.readerid = '"
				+ readerID + "' and lendinfo.returndate is null";
		System.out.print("查询当前借阅信息：" + borrowSql);
		// 查询历史借阅信息
		String historySql = "select * from lendinfo where lendinfo.readerid = '"
				+ readerID + "' and lendinfo.returndate is not null";
		System.out.print("查询历史借阅信息：" + historySql);
		/**
		 * 1.##################################################. 当前的借阅信息
		 */
		BorrowInfo borrowInfo = null;
		ResultSet rs1 = null;
		try {
			rs1 = stmt.executeQuery(borrowSql);
			borrowDataList[0] = new ArrayList();
			System.out.print("准备从结果集rs1中取数据：");
			while (rs1.next()) {
				String readerid = rs1.getString("readerid");
				String bookcode = rs1.getString("bookcode");
				Date borrowDate = rs1.getDate("borrowdate");
				Date dueDate = rs1.getDate("duedate");
				String  returnDate = rs1.getString ("returndate");
				int renew = rs1.getInt("renew");
				int overdays = DaysInterval.getDays(new Date(), dueDate);
				if (overdays > 0) {
					// 读者查询自己的个人借阅信息时，判断是否超期，若超期则将取系统时间来更新lendinfo表，得出超期的天数
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
				System.out.print("超期天数" + borrowInfo.getOverduedays());
				borrowDataList[0].add(borrowInfo);
			}
			System.out.print("读者当前借阅图书：" + borrowDataList[0].size() + "本");
		} catch (SQLException e) {
			System.out.print("读取当前借阅图书信息错误：" + e.getMessage());
		} finally {
			try {
				rs1.close();
			} catch (SQLException e) {
				System.out.print("关闭结果集rs1错误：" + e.getMessage());
			}
		}
		ResultSet rs2 = null;
		try {
			rs2 = stmt.executeQuery(historySql);
			borrowDataList[1] = new ArrayList();
			System.out.print("准备从结果集rs2中取数据：");
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
			System.out.print("超期图书：" + borrowDataList[0].size() + "本");

		} catch (SQLException e1) {
			System.out.print("数据库读异常，" + e1);
		} finally {
			try {
				rs2.close();
			} catch (SQLException e) {
				System.out.print("关闭结果集rs2出错：" + e.getMessage());
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out.print("关闭处理语句stmt出错：" + e.getMessage());
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.print("关闭数据库连接con出错：" + e.getMessage());
			}
		}
		return borrowDataList;
	}
	
	public boolean execReaderInsert(ReaderInfo readerInfo) {
		System.out.print("服务器添加读者信息");
		boolean mark = false;
		String readerid = readerInfo.getReadid(); // 读者编号
		String passwd = readerInfo.getPasswd(); // 密码
		String name = readerInfo.getName(); // 姓名
		int age = readerInfo.getAge();
		String gender = readerInfo.getGender(); // 性别
		String address = readerInfo.getAddress(); // 住址
		String tel = readerInfo.getTel(); // 电话
		String startdate = readerInfo.getStartdate(); // 开户日期
		String enddate = readerInfo.getEnddate(); // 结束日期
		int type = readerInfo.getType(); // 读者类型
		String major = readerInfo.getMajor();
		String depart = readerInfo.getDepart();
		
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
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
			System.out.print("执行的sqlBookData为：" + sqlBookData);

			int m = st.executeUpdate(sqlBookData);
			if (m > 0) {
				mark = true;
			} else {
				mark = false;
			}
		} catch (SQLException e) {
			System.out.print("SQL错误" + e.getMessage());
			mark = false;
		}
		return mark;
	}
	
	//书籍信息插入
	public boolean execBookDataInsert(BookDetails bookDetails) {
		System.out.print("服务器端添加书籍信息");
		boolean mark = false;
		/**
		 * bookdata表的信息
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
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
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
			System.out.print("执行的sqlBookData为：" + sqlBookData);

			int m = stmt.executeUpdate(sqlBookData);
			if (m > 0) {
				mark = true;
			} else {
				mark = false;
			}
		} catch (SQLException e) {
			System.out.print("SQL错误" + e.getMessage());
			mark = false;
		}finally{
			try{
			stmt.close();
			con.close();
			}catch(SQLException e){
				System.out.print("关闭sql异常"+e.getMessage());
			}
		}
		return mark;
	}
	
	//根据isbn获取书本信息
	private BookDetails getIsbnBookDetails(String isbn) {
		System.out.print("根据isbn查找图书：" + isbn);
		ResultSet rs = null;
		BookDetails bookDetails = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String bookSql = "select * from bookdata where isbn like '%" + isbn
					+ "%'";
			System.out.print("bookSql = :" + bookSql);
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
			System.out.print("根据isbn取得图书详细信息出错："+e.getMessage());
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				System.out.print("关闭结果集错误：" + e.getMessage());
			}
		}
		return bookDetails;
	}
	
	//根据条形码获取书本信息
	public BookDetails getBookBarDetails(String barCode) {
		BookDetails bookDetails = null;
		ResultSet rs = null;
		String isbn = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from bookinfo where barcode = '"
					+ barCode + "'";
			System.out.print("取得指定条码的图书isbn：" + sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				isbn = rs.getString("isbn");
				System.out.print("isbn = :" + isbn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();

				stmt.close();
				con.close();
			} catch (Exception e) {
				System.out.print("关闭结果集错误：" + e.getMessage());
			}
		}
		bookDetails = getIsbnBookDetails(isbn);
		return bookDetails;
	}
	
	public int getCanBorrowMonths(int type) {
		int months = 0;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String sql = "select period from parameter where type =" + type;
			System.out.print(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				months = rs.getInt(1);
			}
			System.out.print(months);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				System.out.print("关闭结果集错误："+e.getMessage());
			}
		}
		return months;
	}
	
	//读者借阅书籍
	public boolean execReaderBorrowBook(String readerid, String barCode) {
		boolean mark = false;
		System.out.print("接收到读者编号" + readerid + "\t条形码：" + barCode);
		ReaderInfo readerInfo =(ReaderInfo) getReaderInfo(readerid, "nullpass");
		//读者类型
		int type = readerInfo.getType();
		System.out.print("读者类型："+type);
		//读者最多可以借阅图书的天数
		int months = getCanBorrowMonths(type);
		System.out.print("可以借阅的最长时间： "+months+" 个月");
		Calendar c = Calendar.getInstance();
		int yearOld = c.get(Calendar.YEAR);
		int monthOld = c.get(Calendar.MONTH)+1;
		int dateOld = c.get(Calendar.DATE);
		String borrowdate = yearOld+"-"+monthOld+"-"+dateOld;
		System.out.print("借书时间："+borrowdate);
		c.add(Calendar.MONTH,2);
		int yearNew =  c.get(Calendar.YEAR);
		int monthNew = c.get(Calendar.MONTH)+1;
		int dateNew = c.get(Calendar.DATE);
		String duedate = yearNew+"-"+monthNew+"-"+dateNew;
		System.out.print("还书时间："+duedate);
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try{
			stmt = con.createStatement();
			String upsql = "update bookinfo set status ="+0+",duedate = '"+duedate+"' where barcode = '"+barCode+"'";
			System.out.print("upsql = "+upsql);
			int mun = stmt.executeUpdate(upsql);
			String sql = "insert into lendinfo(readerid,bookcode,borrowdate,duedate) values ('"+readerid+"','"+barCode+"','"+borrowdate+"','"+duedate+"')";
			System.out.print("sql = "+sql);
			int num = stmt.executeUpdate(sql);
			System.out.print("受影响的行数："+num);
			if(num>0&&mun>0){
				mark = true;
			}
			else{
				mark = false;
			}
		}catch(SQLException e){
			mark = false;
			System.out.print("修改密码异常"+e.getMessage());			
		}finally{
			try{
				stmt.close();
				con.close();
			}catch(SQLException e){
				System.out.print("修改密码时关闭sql异常："+e.getMessage());
			}
		}
		return mark;
	}
}