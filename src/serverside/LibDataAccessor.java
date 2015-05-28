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
import serverside.entity.LibraianInfo;
import serverside.entity.ReaderInfo;

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
}


