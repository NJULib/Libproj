package clientside;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import serverside.entity.BookDetails;
import serverside.entity.BookInLibrary;
import serverside.entity.BorrowInfo;
import serverside.entity.LibraianInfo;
import serverside.entity.ParameterInfo;
import serverside.entity.ReaderInfo;
import util.*;

/**
 * 
 * 
 */
@SuppressWarnings("all")
public class LibClient implements LibProtocals {

	/**
	 * socket引用
	 */
	protected Socket hostSocket;

	/**
	 * 输出流的引用
	 */
	protected ObjectOutputStream outputToServer;

	/**
	 * 输入流的引用
	 */
	protected ObjectInputStream inputFromServer;

	/**
	 * 接受主机名和端口号的构造方法
	 */
	public LibClient(String host, int port){

		log("连接数据服务器..." + host + ":" + port);

		try {
		hostSocket = new Socket(host, port);
		outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
		inputFromServer = new ObjectInputStream(hostSocket.getInputStream());
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "连接服务器错误，请检查servinfo.txt配置文件");
		} catch (IOException e) {
			log("IO错误"+e.getMessage());
		}
		log("连接成功.");
	}

	/**
	 * 方法一：取得与关键字匹配的相关图书集合
	 */
	public ArrayList getBookList(String field, String keyword)
			throws IOException {

		ArrayList bookList = null;

		try {
			log("发送请求: OP_GET_BOOK_DETAILS");
			outputToServer.writeInt(LibProtocals.OP_GET_BOOK_DETAILS);
			outputToServer.writeObject(field);
			outputToServer.writeObject(keyword);
			outputToServer.flush();

			log("接收数据...");
			bookList = (ArrayList) inputFromServer.readObject();
			log("收到  " + bookList.size() + " 本相关图书.");
		} catch (ClassNotFoundException exc) {
			log("取图书异常: " + exc);
			throw new IOException("找不到相关类");
		}
		return bookList;
	}

	/**
	 * 方法二：取得每本书具体馆藏情况
	 */
	public ArrayList getBookInLibrary(String isbn) throws IOException {

		ArrayList bookInLibraryInfo = null;

		try {
			log("发送请求: OP_GET_BOOK_LIBINFO， 书号  = " + isbn);
			outputToServer.writeInt(LibProtocals.OP_GET_BOOK_LIBINFO);
			outputToServer.writeObject(isbn);
			outputToServer.flush();

			log("接收数据...");
			bookInLibraryInfo = (ArrayList) inputFromServer.readObject();
			log("收到  " + bookInLibraryInfo.size() + " 项馆藏记录.");
		} catch (ClassNotFoundException exc) {
			log("取馆藏信息异常: " + exc);
			throw new IOException("找不到相关类");
		}
		return bookInLibraryInfo;
	}

	/**
	 * 方法三：取回用户在图书馆的借阅信息
	 */
	public List[] getReaderBorrowInfo(String readerid) {

		List[] readerBorrownInfoList = null;

		try {
			log("发送请求: OP_GET_BORROWINFO， 读者  = " + readerid);
			outputToServer.writeInt(LibProtocals.OP_GET_BORROWINFO);
			outputToServer.writeObject(readerid);
			outputToServer.flush();

			log("接收数据...");
			readerBorrownInfoList= (List[]) inputFromServer.readObject();
			log("收到  " + readerBorrownInfoList.length + " 项借阅记录");
		} catch (ClassNotFoundException exc) {
			log("取借阅信息异常: " + exc);
		}catch(IOException e){
			log("IO错误:"+e.getMessage());
		}
		return readerBorrownInfoList;
	}

	/**
	 * 
	 * 方法四：取得管理员或者用户的姓名等的详细信息
	 */
	public Object getName(int mark, String ID,String passwd) {
		Object obj = null;
		log("发出读取用户信息的命令getName" + ID + "" + mark);
		try {
			if (OP_GET_READERINFO == mark) {
				log("读者的个人信息");
				outputToServer.writeInt(OP_GET_READERINFO);
				outputToServer.writeObject(ID+","+passwd);
				outputToServer.flush();
				log("接收数据。。。。");
				obj = inputFromServer.readObject();				
			}
			if (OP_GET_LIBRAIANINFO == mark) {
				log("管理员的个人信息");
				log("OP_GET_LIBRAIANINFO=" + OP_GET_LIBRAIANINFO + "mark="+ mark);
				outputToServer.writeInt(OP_GET_LIBRAIANINFO);
				log("写到服务器");
				outputToServer.writeObject(ID+","+passwd);
				outputToServer.flush();
				log("接收数据。。。。");
				obj = inputFromServer.readObject();
			}
		} catch (IOException e) {
			log("错误" + e.getMessage());
		} catch (ClassNotFoundException exc) {
			log("取借阅信息异常: " + exc);
		}
		log("返回用户信息:"+obj);
		return obj;
	}

	/**
	 * 方法五：获取书籍、读者、管理员或者参数信息等
	 */
	public List getAllInfos(String str, String condition) {
		List list = null;
		/**
		 * 书籍维护时，书籍的详细信息
		 */
		log("发出读取信息的命令" + str);
		try {
			if ("bookdata".equals(str)) {
				outputToServer.writeInt(OP_BOOK_MAINTAIN);
				log(condition);
				outputToServer.writeObject(condition);
				outputToServer.flush();
				log("接收数据。。。。");
				list = (List) inputFromServer.readObject();
				log("接收list:" + list.size());
				return list;
			}
			/**
			 * 读者信息维护时读者的详细信息
			 */
			else if ("readerdata".equals(str)) {
				outputToServer.writeInt(OP_READER_MAINTAIN);
				log(condition);
				outputToServer.writeObject(condition);
				outputToServer.flush();
				log("接收数据。。。。");
				list = (List) inputFromServer.readObject();
				log("接收list:" + list.size());
				return list;
			}
			/**
			 * 管理员信息维护时管理员的详细信息
			 
			else if ("librarian".equals(str)) {

			}
			/**
			 * 参数信息维护时详细的参数信息
			
			else if ("parameter".equals(str)) {

			} */ else {
				JOptionPane.showMessageDialog(null, "查询维护信息没有对应的数据表");
				return null;
			}
		}catch (IOException e) {
			log("错误" + e.getMessage());
		}catch(ClassNotFoundException ef){
			log("没有找到类"+ef.getMessage());
		}
		return list;
	}
	
	/**
	 * 方法六：修改信息---系统维护
	 */
	public boolean updateInfo(Object obj) {
		boolean mark = false;
		/**
		 * 修改书籍信息
		 */
		//JOptionPane.showMessageDialog(null, obj.getClass()); ==>class serverside.BookDetails
		//JOptionPane.showMessageDialog(null, obj.getClass().getName()); ==>serverside.BookDetails
		//JOptionPane.showMessageDialog(null, obj.getClass()==BookDetails.class); ==>true
		
		if (obj.getClass() == BookDetails.class) {			
			log("图书详细信息的修改");
			try {
				outputToServer.writeInt(OP_EXEC_BOOK_MODIFY);
				outputToServer.writeObject(obj);
				outputToServer.flush();
				log("接收信息。。。");
				mark = inputFromServer.readBoolean();
				log("接收到mark="+mark);
			} catch (IOException e) {
				mark = false;
				log("IO错误"+e.getMessage());
			}
		}
		/**
		 * 修改读者信息
		 */
		if (obj.getClass() == ReaderInfo.class) {			
			log("读者详细信息的修改");
			try {
				outputToServer.writeInt(OP_EXEC_READER_MODIFY);
				outputToServer.writeObject(obj);
				outputToServer.flush();
				log("接收信息。。。");
				mark = inputFromServer.readBoolean();
				log("接收到mark="+mark);
			} catch (IOException e) {
				mark = false;
				log("IO错误"+e.getMessage());
			}
		}
		/**
		 * 修改管理员信息
		 
		if (obj.getClass() == LibraianInfo.class) {			
			log("管理员信息的修改");
			try {
				outputToServer.writeInt(OP_EXEC_LIBRARIAN_MODIFY);
				outputToServer.writeObject(obj);
				outputToServer.flush();
				log("接收信息。。。");
				mark = inputFromServer.readBoolean();
				log("接收到mark="+mark);
			} catch (IOException e) {
				mark = false;
				log("IO错误"+e.getMessage());
			}
		}
		*/
		
		/**
		 * 修改参数信息
		 */
		
		return mark;
	}
	
	/**
	 * 方法七：添加图书、读者、管理员、参数信息
	 */
	public boolean insertBookData(BookDetails bookDetails){
		boolean mark = false;
		/**
		 * 保存图书信息
		 */
			log("保存图书信息开始");			
			try {
				outputToServer.writeInt(OP_EXEC_BOOK_INSERT);
				outputToServer.writeObject(bookDetails);
				outputToServer.flush();
				log("接收信息。。。");
				mark = inputFromServer.readBoolean();
				log("接收到mark="+mark);
			} catch (IOException e) {
				mark = false;
				log("IO错误"+e.getMessage());
			}
		return mark;
	}
	//馆藏信息
	public boolean insertBookInfo(BookInLibrary bookInfo){
		boolean mark = false;
		/**
		 * 保存图书信息
		 */
			log("保存图书信息开始");			
			try {
				outputToServer.writeInt(OP_EXEC_BOOK_IN_LIB_INSERT);
				outputToServer.writeObject(bookInfo);
				outputToServer.flush();
				log("接收信息。。。");
				mark = inputFromServer.readBoolean();
				log("接收到mark="+mark);
			} catch (IOException e) {
				mark = false;
				log("IO错误"+e.getMessage());
			}
		return mark;
	}
	/**
	 * 方法八：插入读者信息
	 * @param readerInfo
	 * @return
	 */
	public boolean insertReaderInfo(ReaderInfo readerInfo){
		boolean mark = false;
		try {
			outputToServer.writeInt(OP_EXEC_READER_INSERT);
			outputToServer.writeObject(readerInfo);
			outputToServer.flush();
			log("接收信息。。。");
			mark = inputFromServer.readBoolean();
			log("接收到mark="+mark);
		} catch (IOException e) {
			mark = false;
			log("IO错误"+e.getMessage());
		}
		return mark;
	}
	
	/**
	 * 方法九：取得指定类型的读者可以借阅的最大图书数目
	 * @param type
	 * @return
	 */
	public int  getAccount(int type) {
		int account = 0;
		try {
			outputToServer.writeInt(OP_GET_PARAM_ACCOUNT);
			outputToServer.writeInt(type);
			outputToServer.flush();
			log("接收信息。。。");
			account = inputFromServer.readInt();
			log("接收到account="+account);
		} catch (IOException e) {
			account = 0;
			log("IO错误"+e.getMessage());
		}
		return account;
	}
	
	/**
	 * 方法十：根据条形码取得指定图书的详细信息
	 * @param barCode
	 * @return
	 */
	public BookDetails getBookDetails(String barCode) {
		log("客户端接收到barCode"+barCode);
		BookDetails bookDetails = null;
		try{
			outputToServer.writeInt(OP_GET_BARCODE_BOOK_DETAILS);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			log("接收信息。。。");
			bookDetails =(BookDetails) inputFromServer.readObject();
			log("取得图书的编号："+bookDetails.getIsbn());		
		}catch(ClassNotFoundException e){
			log("文件没有找到"+e.getMessage());
		}catch(IOException e){
			log("IO异常"+e.getMessage());
		}
		return bookDetails;
	}
	
	/**
	 * 方法十一：读者或者管理员修改自己的密码
	 * @param flag
	 * @param id
	 * @param rePass
	 * @return
	 */
	public boolean modifyPassword(String flag, String id, String rePass) {
		log("接收到标志："+flag+"\t编号："+id+"\t密码："+rePass);
		boolean modify = false;
		if("reader"==flag.trim()){
			log("接收到的使reader");
			//读者修改自己的密码
			try{
				outputToServer.writeInt(READER_MODIFY_PASSWORD);
				outputToServer.writeObject(id);
				outputToServer.writeObject(rePass);
				outputToServer.flush();
				modify = inputFromServer.readBoolean();
				log("修改 modify = :"+modify);
			}catch(IOException e){
				log("修改读者密码IO异常"+e.getMessage());
			}
		}
		else if("libraian"==flag.trim()){
			log("管理员改密码");
		}
		return modify;
	}
	
	/**
	 *  方法十二:检测指定条形码的书是否存在
	 * @param barCode
	 * @return
	 */
	public boolean checkExists(String barCode) {
		boolean mark = false;
		log("接收到的条码："+barCode);
			try{
				outputToServer.writeInt(BARCODE_BOOK_EXISTS);
				outputToServer.writeObject(barCode.trim());
				outputToServer.flush();
				mark = inputFromServer.readBoolean();
				log(" mark = :"+mark);
			}catch(IOException e){
				log("修改读者密码IO异常"+e.getMessage());
			}
		return mark;
	}
	
	/**
	 *  方法十三:检测指定条形码的书是否可借
	 * @param barCode
	 * @return
	 */
	public boolean checkCanBorrow(String barCode) {
		boolean mark = false;
		log("接收到barCode "+barCode);
		try{
			outputToServer.writeInt(BARCODE_BOOK_BORROW);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			mark = inputFromServer.readBoolean();
			log("修改 mark = :"+mark);
		}catch(IOException e){
			mark = false;
			log("指定条码图书是否可借IO异常"+e.getMessage());
		}
		return mark;
	}
	
	/**
	 * 方法十四：借书
	 * @param readerid
	 * @param keyWord
	 * @return
	 */
	public boolean getLendBookInfo(String readerid,String barCode) {
		boolean mark = false;
		log("开始借书："+readerid+"barcode :"+barCode);
		try {
			outputToServer.writeInt(LibProtocals.OP_BORROW_BOOK);
			outputToServer.writeObject(readerid);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			mark  = inputFromServer.readBoolean();
		} catch (IOException e) {
			mark = false;
			log("借书失败，io错误："+e.getMessage());
		}
		return mark;
	}
	
	/**
	 * 方法十五：还书
	 */
	public BorrowInfo returnBook(String barCode){

		BorrowInfo borrowInfo = null;
		try {
			log("发送请求: OP_RETURN_BOOK， 条形码  = " + barCode);
			outputToServer.writeInt(LibProtocals.OP_RETURN_BOOK);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			log("接收数据...");
			borrowInfo = (BorrowInfo) inputFromServer.readObject();
			log("收到" + borrowInfo.getBarCode() + "条信息");
		} catch (ClassNotFoundException exc) {
			log("取借阅信息异常: " + exc);
		}catch (IOException exc) {
			log("取发送请求取指定条码图书借阅者的信息异常: " + exc);
		}
		return borrowInfo;
	}

	/**
	 * 方法十六：指定条码的图书被哪个读者借阅
	 */
	public ReaderInfo getBarReader(String barCode) {
		ReaderInfo readerInfo = null;
		try {
			log("发送请求取指定条码图书借阅者的信息: ， 条形码  = " + barCode);
			outputToServer.writeInt(LibProtocals.BARCODE_BOOK_BORROW_READER);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			log("接收数据...");
			readerInfo = (ReaderInfo) inputFromServer.readObject();
			log("收到" + readerInfo.getName());			
		} catch (IOException exc) {
			log("取发送请求取指定条码图书借阅者的信息异常: " + exc);
		}catch (ClassNotFoundException exc) {
			log("取发送请求取指定条码图书借阅者的信息异常: " + exc);
		}
		return readerInfo;
	}
	
	/**
	 * 方法十七：检测指定条码的图书是否仍在馆藏中
	 */
	public boolean checkIsBorrowed(String barCode) {
		boolean mark = false;
		try{
			log("检测指定条码的图书是否仍在馆藏中:  条形码  = " + barCode);
			outputToServer.writeInt(LibProtocals.BARCODE_BOOK_IN_LIB);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			log("接收数据...");
			mark =  inputFromServer.readBoolean();
			log("收到" + mark);	
		} catch (IOException exc) {
			log("取发送请求取指定条码图书借阅者的信息异常: " + exc.getMessage());
		}
		return mark;
	}
	
	/**
	 * 方法十八：检测指定条码的图书是否被续借过
	 */
	public int bookIsRenewed(String barCode) {
		int renew = 0;
		log("检测指定图书是否被续借过？");
		try{
			outputToServer.writeInt(LibProtocals.BARCODE_BOOK_BE_BORROWED);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			log("接收数据...");
			renew =  inputFromServer.readInt();
			log("收到" + renew);	
		}catch (IOException exc) {
			log("检测指定条码图书是否被续借: " + exc.getMessage());
		}
		return renew;
	}
/**
 * 方法十九：续借图书
 * @param readerid
 * @param barCode
 * @return
 */
	public boolean renewBook(String readerid,String barCode) {
		boolean mark = false;
		try{
			outputToServer.writeInt(LibProtocals.RENEW_BOOK);
			outputToServer.writeObject(readerid);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			log("接收数据...");
			mark =  inputFromServer.readBoolean();
			log("收到" + mark);	
		}catch (IOException exc) {
			log("检测指定条码图书是否被续借: " + exc.getMessage());
		}
		return mark;
	}
	
	/**
	 * 方法二十：图书统计
	 * 
	 */
	public List<Integer> getCountList(){
		List<Integer> countList = null;
		log("图书统计");
		try{
			outputToServer.writeInt(LibProtocals.GET_COUNT_INFO);
			outputToServer.flush();
			log("接收数据...");
			countList = (List) inputFromServer.readObject();
			log("收到" + countList.size()+"条记录");	
		}catch (IOException exc) {
			log("检测指定条码图书是否被续借: " + exc.getMessage());
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return countList;
	}
	
	/**
	 * 方法二十一：超期图书
	 * @return
	 */
	public List<String> getOverDuBooks() {
		List<String> bList = null;
		log("超期图书");
		try{
			outputToServer.writeInt(LibProtocals.OP_GET_OVER_DUE_BOOKS);
			outputToServer.flush();
			log("接收数据...");
			bList = (List) inputFromServer.readObject();
			log("收到" + bList.size()+"条记录");	
		}catch (IOException exc) {
			log("检测指定条码图书是否被续借: " + exc.getMessage());
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return bList;
	}
	
	/**
	 * 方法二十二：图书出库
	 */

	public boolean outLib(String isbn) {
		boolean mark = false;
		log("图书出库");
		try{
			outputToServer.writeInt(LibProtocals.BOOK_OUT_LIB);
			outputToServer.writeObject(isbn);
			outputToServer.flush();
			log("接收数据...");
			mark = inputFromServer.readBoolean();
			log("收到" + mark);	
		}catch (IOException exc) {
			log("图书出库: " + exc.getMessage());
		}
		return mark;
	}
	/**
	 * 方法二十三：按isbn查询图书
	 * @param isbn
	 * @return
	 */
	public BookDetails getBookDetailsByIsbn(String isbn) {
		BookDetails bookDetails = null;
		try{
			outputToServer.writeInt(LibProtocals.OP_GET_ISBN_BOOK_DETAILS);
			outputToServer.writeObject(isbn);
			outputToServer.flush();
			log("接收数据...");
			bookDetails = (BookDetails)inputFromServer.readObject();
			log("收到" + bookDetails.getName());	
		}catch (IOException exc) {
			log("图书详细信息: " + exc.getMessage());
		}catch (ClassNotFoundException e) {
			log("类每找到:"+e.getMessage());
		}
		return bookDetails;
	}
	/**
	 *方法二十四： 删除指定编号的读者READER_OUT_LIB
	 */
	public boolean delReader(String readerid) {
		boolean mark = false;
		log("读者删除");
		try{
			outputToServer.writeInt(LibProtocals.READER_OUT_LIB);
			outputToServer.writeObject(readerid);
			outputToServer.flush();
			log("接收数据...");
			mark = inputFromServer.readBoolean();
			log("收到" + mark);	
		}catch (IOException exc) {
			log("读者删除: " + exc.getMessage());
		}
		return mark;
	}
	/**
	 * 日志方法.
	 */
	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "LibClient类: " + msg);
	}
	/**
	 * 添加管理员
	 */
	public boolean addMaster(String userid, String password, String name,
			int state1, int state2, int state3) throws IOException {

		boolean b = false;
		log("发送请求: OP_LIBRAIAN_ADDMASTER,添加管理员");
		outputToServer.writeInt(LibProtocals.OP_LIBRAIAN_ADDMASTER);
		outputToServer.writeObject(userid);
		outputToServer.writeObject(password);
		outputToServer.writeObject(name);
		outputToServer.writeInt(state1);
		outputToServer.writeInt(state2);
		outputToServer.writeInt(state3);
		outputToServer.flush();

		log("接收数据...");
		b = inputFromServer.readBoolean();
		log("成功返回添加管理员信息");
		return b;
	}

	/**
	 * 取得管理员类表
	 */
	public ArrayList getManagerList() throws IOException {

		ArrayList managerList = null;
		try {
			log("发送请求: OP_LIBRAIAN_GETMASTER,取得管理员列表");
			outputToServer.writeInt(LibProtocals.OP_LIBRAIAN_GETMASTER);
			outputToServer.flush();

			log("接收数据...");
			managerList = (ArrayList) inputFromServer.readObject();
			log("成功返回添加管理员信息");
		} catch (ClassNotFoundException exc) {
			log("取借阅信息异常: " + exc);
			throw new IOException("找不到相关类");
		}
		return managerList;
	}

	/**
	 * 修改管理员信息
	 */
	public boolean modifyMaster(String userid, String password, String name,
			int state1, int state2, int state3) throws IOException {

		boolean b = false;

		log("发送请求: OP_LIBRAIAN_MODIFYMASTER,修改管理员信息");
		outputToServer.writeInt(LibProtocals.OP_LIBRAIAN_MODIFYMASTER);
		outputToServer.writeObject(userid);
		outputToServer.writeObject(password);
		outputToServer.writeObject(name);
		outputToServer.writeInt(state1);
		outputToServer.writeInt(state2);
		outputToServer.writeInt(state3);
		outputToServer.flush();

		log("接收数据...");
		b = inputFromServer.readBoolean();
		log("成功返回修改管理员信息");
		return b;
	}

	/**
	 * 删除管理员信息
	 */
	public boolean dellMaster(String userid) throws IOException {

		boolean b = false;

		log("发送请求: OP_LIBRAIAN_DELLMASTER,删除管理员信息");
		outputToServer.writeInt(LibProtocals.OP_LIBRAIAN_DELLMASTER);
		outputToServer.writeObject(userid);
		outputToServer.flush();

		log("接收数据...");
		b = inputFromServer.readBoolean();
		log("成功返回删除管理员信息");
		return b;
	}
	/**
	 * 更新参数信息
	 */
	public boolean updateParameter(int type, int amount, int period,
			double dailyfine) throws IOException {
		boolean b = false;
		log("发送请求: OP_PARAMETER_UPDATE， 读者类别  = " + type);
		outputToServer.writeInt(LibProtocals.OP_PARAMETER_UPDATE);
		outputToServer.writeInt(type);
		outputToServer.writeInt(amount);
		outputToServer.writeInt(period);
		outputToServer.writeDouble(dailyfine);
		outputToServer.flush();

		log("接收数据...");
		b = (boolean) inputFromServer.readBoolean();
		log("收到 管理员信息记录");
		return b;
	}
	

	/**
	 * 获得参数信息
	 * 
	 * @param parameterID
	 * @return
	 * @throws IOException
	 */
	public ArrayList getParameterInfo(String parameterID) {

		ArrayList parameterInfoList = null;

		try {
			log("发送请求: OP_PARAMETER_MAINTAIN");
			outputToServer.writeInt(LibProtocals.OP_PARAMETER_MAINTAIN);
			outputToServer.writeObject(parameterID);
			// outputToServer.writeObject(keyword);
			outputToServer.flush();

			log("接收数据...");
			parameterInfoList = (ArrayList) inputFromServer.readObject();
			log("收到  " + parameterInfoList.size() + " 参数相关信息.");
		} catch (IOException e) {
			log("IO异常: " + e.getMessage());
		} catch (ClassNotFoundException exc) {
			log("取参数异常: " + exc.getMessage());
		}
		return parameterInfoList;

	}

	
}
