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
}
