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
import serverside.entity.ParameterInfo;
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
	
	//查看书本是佛能被借阅
	public boolean checkBarBookCanBorrow(String barCode) {
		boolean mark = false;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try{
			stmt = con.createStatement();
			String sql = "select * from bookinfo where barcode ='"+barCode+"' and status = "+0;
			System.out.print("指定条码的书是否可用："+sql);
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				mark = true;	//图书不可借
			}
			else{
				mark = false;
			}
		}catch(SQLException e){
			mark = false;
			System.out.print("修改密码异常"+e.getMessage());			
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				System.out.print("修改密码时关闭sql异常："+e.getMessage());
			}
		}
		return mark;
	}
	
	//根据条形码查看是否书籍存在
	public boolean checkBarBookExists(String barCode) {
		boolean mark = false;
		ResultSet rs = null;
		System.out.print("执行指定条码的图书是否存在");
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try{
			stmt = con.createStatement();
			String sql = "select * from bookinfo where barcode ='"+barCode+"'";
			System.out.print("检测指定条码图书是否存在："+sql);
			rs = stmt.executeQuery(sql);
			//指定条码的图书是否存在，如果rs.next()，则存在
			if(rs.next()){
				//存在
				mark = true;	
			}
			else{
				//不存在
				mark = false;
			}
			System.out.print("图书是否存在："+mark);
		}catch(SQLException e){
			mark = false;
			System.out.print("检测指定条码图书是否存在："+e.getMessage());			
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				System.out.print("修改密码时关闭sql异常："+e.getMessage());
			}
		}
			return mark;
	}
	
	//获取罚金
	public double getCanFireMoney(int type) {
		double money = 0.0;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from parameter where type =" + type;
			System.out.print(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				money = rs.getDouble("dailyfine");
			}
			System.out.print("每天罚金："+money);
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
		return money;
	}
	
	//还书
	public BorrowInfo returnBook(String barCode) {
		BorrowInfo  borrowInfo = null;
		Statement stmt1=null,stmt2=null,stmt3=null;
		ResultSet rs = null;
		String  readerID = null;
		Date  borrowDate=null,duedate=null;
		int overDueDays = 0;// 超期天数
		double finedMoney = 0;// 罚款金额
		int renew =0;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
		}
		try {
			stmt1 = con.createStatement();
			stmt2 = con.createStatement();
			stmt3 = con.createStatement();
			//更新bookinfo表，使借阅图书状态为可以借阅
			String upbookinfosql = "update bookinfo set status = "+1+",duedate = null where barcode = '"
				+ barCode + "'";
			System.out.print("更新bookinfo"+upbookinfosql);
			int booknum = stmt1.executeUpdate(upbookinfosql);
			System.out.print("更新bookinfo"+booknum+"条记录");
			
			//还书
			String sql = "select * from lendinfo where bookcode = '"+barCode+"'";
			System.out.print("还书"+sql);
			Calendar calendar = Calendar.getInstance();
			String currDate = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);//还书时间
			rs = stmt2.executeQuery(sql);
			if(rs.next()){
				 readerID = rs.getString("readerid");
				ReaderInfo readerInfo = getReaderInfo(readerID, "nullpass");
				System.out.print("读者bar"+readerInfo.getName());
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
			//更新lendinfo表
			String uplendinfosql = "update lendinfo set returndate = '" + currDate
				+ "',overduedays = '" + overDueDays + "',fine = '"
				+ finedMoney + "' WHERE bookcode ='" + barCode
				+ "' AND returndate is null";
			System.out.print("更新lendinfo"+uplendinfosql);
			int lendnum = stmt3.executeUpdate(uplendinfosql);
			System.out.print("更新lendinfo"+lendnum+"条记录");
			Date returndate = null;
//			try {
//				returndate = new SimpleDateFormat("yyyy-MM-dd").parse(currDate);
//			} catch (ParseException e) {
//				log("类型转换错误"+e.getMessage());
//			}
			borrowInfo = new BorrowInfo(readerID,barCode,borrowDate, duedate,currDate,renew,overDueDays, finedMoney);
			System.out.print("借阅信息："+borrowInfo.getReaderId());
		} catch (SQLException e1) {
			System.out.print("数据库读异常，" + e1.getMessage());
		}finally{
			try{
				rs.close();
				stmt1.close();
				stmt2.close();
				stmt3.close();
				con.close();
			}catch(SQLException e){
				System.out.print("修改密码时关闭sql异常："+e.getMessage());
			}
	}
		return borrowInfo;
	}
	
	public int getCanBorrowAccount(int type) {
		int account = 0;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String sql = "select amount from parameter where type =" + type;
			System.out.print(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				account = rs.getInt(1);
			}
			System.out.print(account);
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
		return account;
	}
	
	//根据书的isbn获得书的详细信息
	public BookDetails getBookIsbnDetails(String isbn) {
		BookDetails bookDetails = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String booksql = "select  * from bookdata where isbn ='" + isbn+"'";
			System.out.print(booksql);
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
				System.out.print("关闭结果集错误："+e.getMessage());
			}
		}
		return bookDetails;
	}
	
	//图书出库
	public boolean outLib(String isbn) {
		boolean mark = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			String booksql = "delete from bookdata where isbn ='" + isbn+"'";
			System.out.print(booksql);
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
				System.out.print("关闭结果集错误："+e.getMessage());
			}
		}
		return mark;
	}
	
	//获取超期的书的列表
	public List getOverDueBooks() {
		List<String> blist = new ArrayList<String>();
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
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
			System.out.print("数据库读异常，" + e1.getMessage());
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				System.out.print("修改密码时关闭sql异常："+e.getMessage());
			}
		}
		return blist;
	}
	
	//进行图书的统计
	public List getBookCountList() {
		List<Integer> countList = new ArrayList<Integer>();
		ResultSet countrs = null,commrs = null,borrowedrs = null,inlibrs = null,lostrs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
		}
		try {
			stmt = con.createStatement();
			//总馆藏数目
			String countsql = "select count(*) from bookinfo";
			countrs = stmt.executeQuery(countsql);
			if(countrs.next()){
				countList.add(Integer.parseInt(countrs.getString(1)));
			}else{
				countList.add(0);
			}
			//图书流通数量
			String commsql =" select count(*) from lendinfo";
			commrs = stmt.executeQuery(commsql);
			if(commrs.next()){
				countList.add(Integer.parseInt(commrs.getString(1)));
			}
			else{
				countList.add(0);
			}
			//被借走的图书数目
			String borrowedsql = "select count(*) from bookinfo where status = 0";
			borrowedrs = stmt.executeQuery(borrowedsql);
			if(borrowedrs.next()){
				countList.add(Integer.parseInt(borrowedrs.getString(1)));
			}else{
				countList.add(0);
			}
			//在馆藏的数目
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
			System.out.print("数据库读异常，" + e1);
		}finally{
			try{
				countrs.close();
			
			borrowedrs.close();
			inlibrs.close();
			lostrs.close();}catch(SQLException e){
				System.out.print("关闭数据库连接异常"+e.getMessage());
			}
		}
		return countList;
	}
	
	//图书的续借
	public boolean execRenewBook(String readerid, String barCode) {
		boolean mark = false;
		Statement stmt1 = null,stmt2 = null;
		ResultSet rs=null;
		Date duedate = null;
		String [] dateStr= {"0000","00","00"};
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from lendinfo where readerid = '"+readerid+"' and  bookcode ='"+barCode+"'";
			System.out.print("续借图书:"+sql);
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				//取出应还日期
				duedate = rs.getDate("duedate");
				System.out.print("应还日期:"+duedate);
			}else{
				mark = false;
				return mark;
			}
			if(null!=duedate){
				String duedateStr = new SimpleDateFormat("yyyy-MM-dd").format(duedate);
				System.out.print("转化日期："+duedateStr);
				if(null==duedateStr||!(duedateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}"))){
					mark = false;
				}
				String [] dueArray= new String[3];
				dueArray = duedateStr.split("-");
				dateStr[0] = dueArray[0];
				System.out.print("年："+dateStr[0]);
				dateStr[1] = dueArray[1];
				System.out.print("月："+dateStr[1]);
				dateStr[2] = dueArray[2];
				System.out.print("日："+dateStr[2]);
				int year = Integer.parseInt(dueArray[0]);
				System.out.print("年："+year);
				int month = Integer.parseInt(dueArray[1]);
				System.out.print("月："+month);
				int day = Integer.parseInt(dueArray[2]);
				System.out.print("日："+day);
				Calendar calendar = Calendar.getInstance();
				//Calendar设置年、月、日
				System.out.print("设置年、月、日");
				calendar.set(year, month, day);
				System.out.print("设置完成");
				//当前时间在月份的基础上加两个月
				calendar.add(Calendar.MONTH, 2);
				System.out.print("测试设置的年、月、日");
				String newduedate = calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE);
				System.out.print("续借后归还的日期："+newduedate);
				//更新lendinfo数据表
				String lendinfosql = "update lendinfo set duedate = '"+newduedate+"', renew = 1 where readerid = '"+readerid+"' and bookcode = '"+barCode+"'";
				//续借成功后更新lendinfo表
				System.out.print("续借成功后更新lendinfo表:"+lendinfosql);
				stmt1 = con.createStatement();
				int num = stmt1.executeUpdate(lendinfosql);
				System.out.print("更新lendinfo中记录数目："+num);
				//续借后更新bookinfo表，更新图书到期时间
				stmt2 = con.createStatement();
				String upsql = "update bookinfo set duedate = '"+newduedate+"' where barcode = '"+barCode+"'";
				System.out.print("upsql = "+upsql);
				int mun = stmt2.executeUpdate(upsql);
				System.out.print("更新bookinfo中记录数目："+mun);
				mark = true;
			}else{
				mark = false;
			}
		}catch(SQLException e){
			mark = false;
			System.out.print("检测指定条码的图书是否被续借过异常："+e.getMessage());
		}finally{
			try{
				rs.close();
				stmt1.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				System.out.print("修改密码时关闭sql异常："+e.getMessage());
			}
		}
		return mark;
	}
	
	//获取所有读者的信息
	public List getReadersInfos(String str) {
		List list = new ArrayList();
		String sql = null;
		ReaderInfo readerInfo = null;
		System.out.print("接收到：" + str);
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("con：");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
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
			System.out.print("读者SQL：" + sql);
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String readerid = rs.getString("readerid"); // 读者编号
				String passwd = rs.getString("passwd"); // 密码
				String name = rs.getString("name"); // 姓名
				Integer age = rs.getInt("age");
				String gender = rs.getString("gender"); // 性别
				String address = rs.getString("address"); // 住址
				String tel = rs.getString("tel"); // 电话
				String startdate = rs.getString("startdate"); // 开户日期
				String enddate = rs.getString("enddate"); // 结束日期
				String strtype = rs.getString("type"); // 读者类型
				int type = Integer.parseInt(strtype); //
				String major = rs.getString("major");
				String depart = rs.getString("depart");
				readerInfo = new ReaderInfo(readerid, passwd, name, age,
						gender, address, tel, startdate, enddate, type, major,
						depart);
				System.out.print("读者："+readerInfo.getName());
				list.add(readerInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//对图书信息进行修改
	public boolean execBookUpdate(BookDetails bookDetails) {
		System.out.print("执行图书修改");
		boolean mark = false;
		String isbn = bookDetails.getIsbn(); // ISBN号
		String name = bookDetails.getName(); // 书名
		String series = bookDetails.getSeries(); // 丛书名
		String authors = bookDetails.getAuthors(); // 作者
		String publisher = bookDetails.getPublisher(); // 出版信息,包括出版地点、出版社名、出版日期
		String size = bookDetails.getSize(); // 图书开本
		int pages = bookDetails.getPages(); // 页数
		double price = bookDetails.getPrice(); // 定价
		String introduction = bookDetails.getIntroduction(); // 图书简介
		String picture = bookDetails.getPicture(); // 封面图片
		String clnum = bookDetails.getClnum(); // 图书分类号
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try {
			Statement st = con.createStatement();
			String sql = "update bookdata set name='" + name + "',series = '"
					+ series + "',authors = '" + authors + "',publisher = '"
					+ publisher + "',size='" + size + "',pages =" + pages
					+ ",price = " + price + ",introduction='" + introduction
					+ "',picture = '" + picture + "',clnum= '" + clnum
					+ "' where isbn = '" + isbn + "'";
			System.out.print("执行的sql为：" + sql);
			int m = st.executeUpdate(sql);
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
	
	//修改读者信息
	public boolean execReaderUpdate(ReaderInfo readerInfo) {
		System.out.print("执行读者信息修改");
		boolean mark = false;
		String readerid = readerInfo.getReadid(); // 读者编号
		//String passwd = readerInfo.getPasswd(); // 密码
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
			String sql = "update reader set name = '"
					+ name + "',age = "+age+",gender = '" + gender + "',address = '"
					+ address + "',tel='" + tel + "',startdate =" + startdate
					+ ",enddate = " + enddate + ",type=" + type
					+ ",major = '"+major+"',depart = '"+depart+"' where readerid = '" + readerid + "'";
			System.out.print("执行的sql为：" + sql);
			int m = st.executeUpdate(sql);
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
	
	//馆藏中加入书籍
	public boolean execBookInfoInsert(BookInLibrary bookInLibrary) {
		System.out.print("服务器端添加馆藏信息");
		boolean mark = false;
		/**
		 * bookinfo表的信息
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
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
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
		System.out.print("执行的sqlBookInfo为：" + sqlBookInfo);
		int n = stmt.executeUpdate(sqlBookInfo);
		if(n>0){
			mark = true;
		}else{
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
		return  mark;
	}
	
	//读者进行密码修改
	public boolean execPassModify(String readerid, String pass) {
		System.out.print("接收到读者编号" + readerid + "\t密码：" + pass);
		boolean modify = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try{
			stmt = con.createStatement();
			String sql = "update reader set passwd = '"+pass+"' where readerid = '"+readerid+"'";
			System.out.print(sql);
			int num = stmt.executeUpdate(sql);
			System.out.print("受影响的行数："+num);
			if(num>0){
				modify = true;
			}
			else{
				modify = false;
			}
		}catch(SQLException e){
			System.out.print("修改密码异常"+e.getMessage());
			modify = false;
		}finally{
			try{stmt.close();
			con.close();
			}catch(SQLException e){
				System.out.print("修改密码时关闭sql异常："+e.getMessage());
			}
		}
		return modify;
	}
	
	//通过条形码获取阅读该书的读者信息
	public ReaderInfo getReaderInfoByBarcode(String barCode) {
		ReaderInfo readerInfo = null;
		ResultSet rs = null;
		String readerId = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
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
			System.out.print("根据条码取读者信息错误"+e.getMessage());
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				System.out.print("修改密码时关闭sql异常："+e.getMessage());
			}
		}
		return readerInfo;
	}
	
	//根绝条形码看书是否在馆藏中
	public Boolean getBarCodeIsBorrowed(String barCode) {
		boolean mark = false;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from bookinfo where barcode ='"+barCode+"' and status = "+1;
			System.out.print("检测指定条码图书是否仍在馆藏中"+sql);
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				mark = true;
			}else{
				mark = false;
			}
		}catch(SQLException e){
			mark = false;
			System.out.print("检测指定条码的图书是否仍在馆藏中："+e.getMessage());
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				System.out.print("修改密码时关闭sql异常："+e.getMessage());
			}
		}
		return mark;
	}
	
	//查看数是否被续借过
	public int getBarcodeIsRenewed(String barCode) {
		int renew = 0;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
		}
		try {
			stmt = con.createStatement();
			String sql = "select * from lendinfo where bookcode ='"+barCode+"'";
			System.out.print("检测指定条码的图书是否被续借过:"+sql);
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				renew  = rs.getInt("renew");
			}else{
				renew = 0;
			}
		}catch(SQLException e){
			renew = 0;
			System.out.print("检测指定条码的图书是否被续借过异常："+e.getMessage());
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				System.out.print("修改密码时关闭sql异常："+e.getMessage());
			}
		}
		return renew;
	}
	
	//删除指定编号的读者
	public boolean execReaderDelete(String readerid) {
		boolean mark = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			System.out.print("加载数据库连接成功");
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!" + ee.getMessage());
		}
		try {
			stmt = con.createStatement();
			//删除读者
			String ksql = "delete from reader where readerid ='" + readerid+"'";
			System.out.print("删除读者:"+ksql);
			int m = stmt.executeUpdate(ksql);
			//删除读者的借阅信息
			String rbsql = "delete from lendinfo where  readerid ='" + readerid+"'";
			System.out.print("删除读者的借阅信息"+rbsql);
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
				System.out.print("关闭结果集错误："+e.getMessage());
			}
		}
		return mark;
	}
	
	//增加管理员
	public boolean addMaster(String userid, String password, String name,
			int state1, int state2, int state3) {
		boolean b = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
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
			System.out.print("数据库读异常，" + e1.getMessage());
			return b;
		}
	}
	
	public ArrayList getMaster() {
		ArrayList<LibraianInfo>  managerList =null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
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
			System.out.print("数据库读异常，" + e1.getMessage());
			return managerList;
		}
	}
	
	public boolean modifyMaster(String userid, String password, String name,
			int state1, int state2, int state3) {
		boolean b = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
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
			System.out.print("数据库读异常，" + e1.getMessage());
			return b;
		}
	}

	public boolean dellMaster(String userid) {
		boolean b = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
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
			System.out.print("数据库读异常，" + e1.getMessage());
			return b;
		}
	}
	
	//获取所有参数信息
	public ArrayList getParamterInfo(String parameterID) {
		ArrayList paraList = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
		}
		try {
			stmt = con.createStatement();
			String pSql = "SELECT * FROM parameter";

			rs = stmt.executeQuery(pSql);
			paraList = new ArrayList();
			ParameterInfo parameterInfo = null;
			int id; // 参数的id号
			int type; // 读者类别
			int amount; // 借书数量
			int period; // 借书天数
			double dailyfine = 0; // 超期每日罚款金额
			while (rs.next()) // 输出每条记录
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
			System.out.print("数据库读异常，" + e1);
			return paraList;
		}
	}
	
	//更新全部参数
	public boolean updateParameter(int type, int amount, int period,double dailyfine) {
		boolean b = false;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		} catch (SQLException ee) {
			System.out.print("建立数据库连接失败!");
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
				System.out.print("用户读取信息异常！");
			}
			stmt.close();
			con.close();
			return b;
		} catch (SQLException e1) {
			System.out.print("数据库读异常，" + e1);
			return b;
		}
	}
}