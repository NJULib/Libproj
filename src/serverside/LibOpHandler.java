package serverside;

import serverside.entity.BookDetails;
import serverside.entity.BookInLibrary;
import serverside.entity.BorrowInfo;
import serverside.entity.ReaderInfo;
import util.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 服务器端线程，监视并处理来自客户端的请求
 * 
 */
public class LibOpHandler extends Thread implements LibProtocals {

	protected Socket clientSocket;
	protected ObjectOutputStream outputToClient;
	protected ObjectInputStream inputFromClient;

	protected LibDataAccessor libDataAccessor;

	protected boolean done;

	public LibOpHandler(Socket theClinetSocket,
			LibDataAccessor theLibDataAccessor) throws IOException {
		clientSocket = theClinetSocket;
		outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
		inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
		libDataAccessor = theLibDataAccessor;
		done = false;
	}

	/**
	 * @param args
	 */

	public void run() {

		try {
			while (!done) {

				log("等待命令...");

				int opCode = inputFromClient.readInt();
				log("opCode = " + opCode);

				switch (opCode) {
			    	/**
		    	    * 1.取得书籍的详细信息
			     	*/
				case LibProtocals.OP_GET_BOOK_DETAILS:
					opGetBookDetails();
					break;
					/**
					 * 2.取得书籍的馆藏信息
					 */
				case LibProtocals.OP_GET_BOOK_LIBINFO:
					opGetBookLibInfo();
					break;
					/**
					 * 3.取得用户的借阅信息
					 */
				case LibProtocals.OP_GET_BORROWINFO:
					opGetBorrowInfo();
					break;
					/**
					 * 4.读取读者的个人详细信息
					 */
				case LibProtocals.OP_GET_READERINFO:
					opGetReaderInfo();
					break;
					/**
					 * 5.读取管理员的个人详细信息
					 */
				case LibProtocals.OP_GET_LIBRAIANINFO:
					opGetLibarianInfo();
					break;
					/**
					 * 6.图书维护
					 */
				case LibProtocals.OP_BOOK_MAINTAIN:
					opGetBookMaintainInfo();
					break;
					/**
					 *7.读者维护 --取得所有读者信息
					 */
				case LibProtocals.OP_READER_MAINTAIN:
					opGetReaderMaintainInfo();
					break;
					/**
					 * 8.图书信息修改
					 */
				case LibProtocals.OP_EXEC_BOOK_MODIFY:
					opModifyBookInfo();
					break;
					/**
					 * 9.读者信息修改
					 */
				case LibProtocals.OP_EXEC_READER_MODIFY:
					opModifyReaderInfo();
					break;
					/**
					 * 10.添加图书
					 */
				case LibProtocals.OP_EXEC_BOOK_INSERT:
					opInsertBookData();
					break;
					//馆藏信息
				case LibProtocals.OP_EXEC_BOOK_IN_LIB_INSERT:
					opInsertBookInfo();
					break;
					/**
					 * 11.添加读者信息
					 */
				case LibProtocals.OP_EXEC_READER_INSERT:
					opInsertReaderInfo();
					break;
					/**
					 * 12.读者借阅的最大数量
					 */
				case LibProtocals.OP_GET_PARAM_ACCOUNT:
					opGetAccount();
					break;
					/**
					 * 13.取得指定条码的图书的详细信息
					 */
				case LibProtocals.OP_GET_BARCODE_BOOK_DETAILS:
					opGetBarBook();
					break;
					/**
					 * 14.读者修改密码
					 */
				case LibProtocals.READER_MODIFY_PASSWORD:
					execReaderModPass();
					break;
					/**
					 * 15.读者借书
					 */
				case LibProtocals.OP_BORROW_BOOK:
					execReaderBorrowBook();
					break;
					/**
					 * 16.还书
					 */
				case LibProtocals.OP_RETURN_BOOK:
					opReturnBook();
					break;
					/**
					 * 17.检测指定条码的图书是否可借
					 */
				case LibProtocals.BARCODE_BOOK_BORROW:
					checkBarcodeBookBorrow();
					break;
					/**
					 * 18.检测指定条码的图书是否存在
					 */
				case LibProtocals.BARCODE_BOOK_EXISTS:
					checkBarcodeExists();
					break;
					/**
					 * 19.查看指定条码的书谁借了
					 */
				case LibProtocals.BARCODE_BOOK_BORROW_READER:
					execGetReaderInfoByBarcode();
					break;
					/**
					 * 20.检测指定条码的图书是否仍在馆藏中
					 */
				case LibProtocals.BARCODE_BOOK_IN_LIB:
					checkIsBorrowed();
					break;
					/**
					 * 22.检测指定条码的图书是否被续借过
					 */
				case LibProtocals.BARCODE_BOOK_BE_BORROWED:
					checkBarcodeBookIsRenewed();
					break;
					/**
					 * 23.续借图书
					 */
				case LibProtocals.RENEW_BOOK:
					renewBook();
					break;
					/**
					 * 24.图书的统计信息
					 */
				case LibProtocals.GET_COUNT_INFO:
					opGetCountList();
					break;
					/**
					 * 25.超期图书
					 */
				case LibProtocals.OP_GET_OVER_DUE_BOOKS:
					opGetOverDueBooks();
					break;
					/**
					 * 26.图书出库
					 */
				case LibProtocals.BOOK_OUT_LIB:
					opBookOutLib();
					break;
					/**
					 *27. 删除指定编号的读者
					 */
				case LibProtocals.READER_OUT_LIB:
					execReaderByReaderId();
					break;
					/**
					 * 28.根据sibn取得读者详细信息
					 */
				case LibProtocals.OP_GET_ISBN_BOOK_DETAILS:
					opGetBookDetailsByIsbn();
					break;
					/**
					 * 整合：添加管理员
					 */
				case LibProtocals.OP_LIBRAIAN_ADDMASTER:
					opAddMaster();
					break;
					/**
					 * 获取管理员列表
					 */
				case LibProtocals.OP_LIBRAIAN_GETMASTER:
					opGetMaster();
					break;
					/**
					 * 修改管理员信息
					 */
				case LibProtocals.OP_LIBRAIAN_MODIFYMASTER:
					opModifyMaster();
					break;
					/**
					 * 删除管理员
					 */
				case LibProtocals.OP_LIBRAIAN_DELLMASTER:
					opDellMaster();
					break;
				case LibProtocals.OP_PARAMETER_MAINTAIN:	
					opGetParameterInfo();
					break;
				case LibProtocals.OP_PARAMETER_UPDATE:
					opUpdateParameter();
					break;

				default:
					log("错误代码");
				}
			}
		} catch (IOException e1) {
			try {
				clientSocket.close();
			} catch (Exception e2) {
				log("run异常" + e2);
			}

			log(clientSocket.getInetAddress() + "客户离开了");
			// log("异常" + e1);
		}
	}

	/**
	 * 方法一：根据条形码取得图书信息
	 */
	private void opGetBarBook() {
		try{
			String barCode = (String)inputFromClient.readObject();
			log("图书barcode："+barCode);
			BookDetails bookDetails = libDataAccessor.getBookBarDetails(barCode);
			log("处理根据指定barcode查找图书的事件："+bookDetails.getName());
			outputToClient.writeObject(bookDetails);
			outputToClient.flush();
		}catch(ClassNotFoundException e){
			log("类没有找到"+e.getMessage());
		}catch(IOException e){
			log("IO错误"+e.getMessage());
		}
	}

	/**
	 * 方法二：根据读者类型取得可借阅图书的最大量
	 */
	private void opGetAccount() {
		try {
			int type = inputFromClient.readInt();
			int account = libDataAccessor.getCanBorrowAccount(type);
			outputToClient.writeInt(account);
			outputToClient.flush();
			log(account);
		} catch (IOException exc) {
			log("发生I/O异常:  " + exc);
		} 
	}

	/**
	 * 方法三：取得特定条件的书籍的详细信息
	 */
	private void opGetBookDetails() {

		try {
			log("读图书基本数据");
			String field = (String) inputFromClient.readObject();
			String keyword = (String) inputFromClient.readObject();

			List bookList = libDataAccessor.getBookDetails(field, keyword);
			outputToClient.writeObject(bookList);
			outputToClient.flush();
			log("发出 " + bookList.size() + " 本图书信息到客户端.");
		} catch (IOException exc) {
			log("发生I/O异常:  " + exc);
		} catch (ClassNotFoundException exc) {
			log("发生找不到类异常:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * 方法四：取得图书的馆藏 的详细信息
	 */
	private void opGetBookLibInfo() {

		try {
			log("读图书馆藏情况");
			String isbn = (String) inputFromClient.readObject();
			log("书ISBN号是 : " + isbn);
			List bookLibList = libDataAccessor.getBookLibInfo(isbn);
			outputToClient.writeObject(bookLibList);
			outputToClient.flush();
			log("发出 " + bookLibList.size() + " 条藏书信息到客户端.");
		} catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * 方法五：取得图书的借阅信息
	 */
	private void opGetBorrowInfo() {

		try {
			log("读用户借阅情况");
			String readerid = (String) inputFromClient.readObject();
			log("读者ID : " + readerid);

			List[] borrowList = libDataAccessor.getBorrowInfo(readerid);
			outputToClient.writeObject(borrowList);
			outputToClient.flush();
			log("发出 " + borrowList.length + " 条借阅信息到客户端.");
		} catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * 方法六：取得特定的读者的详细信息
	 */
	private void opGetReaderInfo(){
		try{
			log("读取读者的个人信息");
			String  idAndpass = (String) inputFromClient.readObject();
			String [] str = idAndpass.split(",");
			String readerID = str[0];
			String pass = str[1];
			log("读者编号："+readerID+"\t读者密码："+pass);
			Object obj = libDataAccessor.getReaderInfo(readerID,pass);
			outputToClient.writeObject(obj);
			outputToClient.flush();
			log("obj="+obj);
		}catch(IOException e){
			log("发生异常:  " + e);
			e.printStackTrace();
		}catch(ClassNotFoundException ee){
			log("发生异常:  " + ee);
			ee.printStackTrace();
		}
	}
	/**
	 * 方法七：取得特定的管理员的详细信息
	 */
	private void opGetLibarianInfo(){
		try {
			log("读管理员信息");
			String idAndpass = (String) inputFromClient.readObject();
			String [] str = idAndpass.split(",");
			String libarianID = str[0];
			String pass = str[1];
			log("管理员ID : " + libarianID);
			Object obj = libDataAccessor.getLibarianInfo(libarianID,pass);
			outputToClient.writeObject(obj);
			outputToClient.flush();
		} catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * 方法八：取得图书维护所需要的图书信息
	 */
	private void opGetBookMaintainInfo(){
		log("读信息");
		try{
			log("读图书信息");
			String str = (String)inputFromClient.readObject();
			log("条件"+str);
			List list = libDataAccessor.getBooksInfos(str);	
			log("list:"+list);
			outputToClient.writeObject(list);
			outputToClient.flush();
		}catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * 方法九：取得读者维护所需要的读者信息
	 */
	private void opGetReaderMaintainInfo(){
		log("读信息");
		try{
			log("读读者信息");
			String str = (String)inputFromClient.readObject();
			log("条件"+str);
			List list = libDataAccessor.getReadersInfos(str);	
			log("list:"+list);
			outputToClient.writeObject(list);
			outputToClient.flush();
		}catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * 方法十：方法修改图书信息
	 */
	private void opModifyBookInfo(){
		log("服务器端准备修改数据");
		try{
		BookDetails bookDetails = (BookDetails) inputFromClient.readObject();
		log("接收到图书信息");
		boolean mark = libDataAccessor.execBookUpdate(bookDetails);
		log("执行图书信息："+mark);
		outputToClient.writeBoolean(mark);
		outputToClient.flush();
		}catch(IOException ei){
			log("IO错误");
		}catch(ClassNotFoundException eb){
			log("文件没有找到");
		}
	}
	/**
	 * 方法十一：修改读者信息
	 */
	private void opModifyReaderInfo(){
		log("服务器端准备修改数据");
		try{
		ReaderInfo readerInfo = (ReaderInfo) inputFromClient.readObject();
		log("接收到读者信息");
		boolean mark = libDataAccessor.execReaderUpdate(readerInfo);
		log("执行读者信息修改："+mark);
		outputToClient.writeBoolean(mark);
		outputToClient.flush();
		}catch(IOException ei){
			log("IO错误");
		}catch(ClassNotFoundException eb){
			log("文件没有找到");
		}
	}
	/**
	 * 方法十二：添加图书信息
	 */
	private void opInsertBookData(){
		log("开始添加图书信息");
		try{
			BookDetails bookDetails = (BookDetails) inputFromClient.readObject();
			log("接收到图书信息"+bookDetails);
//			BookInLibrary bookInLibrary = (BookInLibrary) inputFromClient.readObject();
//			log("接收到图书信息馆藏地信息"+bookInLibrary);
			boolean mark = libDataAccessor.execBookDataInsert(bookDetails);
			log("执行图书插入："+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(IOException e){
			log("IO错误"+e.getMessage());
		}catch(ClassNotFoundException eb){
			log("文件没有找到");
		}
	}
	
	private void opInsertBookInfo(){
		log("开始添加馆藏信息");
		try{
			BookInLibrary bookInLibrary = (BookInLibrary) inputFromClient.readObject();
			log("接收到图书信息馆藏地信息"+bookInLibrary.getLocation());
			boolean mark = libDataAccessor.execBookInfoInsert(bookInLibrary);
			log("执行馆藏插入："+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(IOException e){
			log("IO错误"+e.getMessage());
		}catch(ClassNotFoundException eb){
			log("文件没有找到");
		}
	}
	
	/**
	 * 方法十三：添加读者信息
	 */
	private void opInsertReaderInfo(){
		log("开始添加读者信息");
		try{
			ReaderInfo readerInfo = (ReaderInfo) inputFromClient.readObject();
			log("接收到读者信息"+readerInfo);
			boolean mark = libDataAccessor.execReaderInsert(readerInfo);
			log("执行查询："+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(IOException e){
			log("IO错误"+e.getMessage());
		}catch(ClassNotFoundException eb){
			log("文件没有找到");
		}
	}
	
	/**
	 * 方法十四：读者修改自己的密码
	 */
	private void execReaderModPass(){
		log("准备执行修改密码");
		try{
		String readerid = (String)inputFromClient.readObject();
		
		String pass = (String)inputFromClient.readObject();
		log("接收到用户编号:"+readerid+"\t密码："+pass);
		boolean modify = libDataAccessor.execPassModify(readerid,pass);
		outputToClient.writeBoolean(modify);
		outputToClient.flush();
		}catch(ClassNotFoundException e){
			log("文件没找到:"+e.getMessage());
		}catch(IOException e){
			log("IO异常:"+e.getMessage());
		}
	}
	
	/**
	 * 方法十五：读者借书
	 */
	private void execReaderBorrowBook(){
		log("准备执行借书");
		try{
			String readerid = (String)inputFromClient.readObject();
			log("读者编号："+readerid);
			String barCode = (String)inputFromClient.readObject();
			log("条形码："+barCode);
			boolean mark = libDataAccessor.execReaderBorrowBook(readerid,barCode);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(ClassNotFoundException e){
			log("文件没找到:"+e.getMessage());
		}catch(IOException e){
			log("IO异常:"+e.getMessage());
		}
	}
	
	/**
	 *  方法十六：检测指定条码的图书是否存在
	 */
	private void checkBarcodeExists(){
		log("执行检测指定条码的图书是否存在");
		try{
			String barCode = (String)inputFromClient.readObject();
			log("条形码："+barCode);
			boolean mark = libDataAccessor.checkBarBookExists(barCode);
			log("接收到的mark："+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(ClassNotFoundException e){
			log("文件没找到:"+e.getMessage());
		}catch(IOException e){
			log("IO异常:"+e.getMessage());
		}		
	}
	
	/**
	 *  方法十七：检测指定条码的图书是否可借
	 */
	private void checkBarcodeBookBorrow(){
		log("执行检测指定条码的图书是否可借");
		try{
			String barCode = (String)inputFromClient.readObject();
			log("条形码："+barCode);
			boolean mark = libDataAccessor.checkBarBookCanBorrow(barCode);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(ClassNotFoundException e){
			log("文件没找到:"+e.getMessage());
		}catch(IOException e){
			log("IO异常:"+e.getMessage());
		}		
	}
	
	/**
	 *  方法十八：还书
	 */
	protected void opReturnBook() {
		log("开始执行还书：");
		try {
			log("读取相应图书");
			String barCode = (String) inputFromClient.readObject();
			log("条形码 : " + barCode);
			BorrowInfo borrowInfo = libDataAccessor.returnBook(barCode);
			log("借阅"+borrowInfo.getReaderId());
			outputToClient.writeObject(borrowInfo);
			outputToClient.flush();
			log("发出还书信息到客户端.");
		} catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 *  方法十九：根据指定条码查看借阅该书的读者的信息
	 */
	private void execGetReaderInfoByBarcode() {
		log("开始读取条码");
		try{
			log("读取相应图书");
			String barCode = (String) inputFromClient.readObject();
			log("条形码 : " + barCode);
			ReaderInfo readerInfo = libDataAccessor.getReaderInfoByBarcode(barCode);
			log("读者"+readerInfo.getName());
			outputToClient.writeObject(readerInfo);
			outputToClient.flush();
			log("发出读者信息到客户端.");
		}catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 * 方法二十：检测指定条码的图书是否仍在馆藏中
	 */
	private void checkIsBorrowed() {
		log("检测指定条码的图书是否仍在馆藏中：");
		try{
			String barCode = (String) inputFromClient.readObject();
			log("条形码 : " + barCode);
			boolean mark = libDataAccessor.getBarCodeIsBorrowed(barCode);
			log("馆藏图书是否可以还："+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 * 方法二十一：检测指定条码的图书是否已被续借过
	 */
	private void checkBarcodeBookIsRenewed() {
		log("检测指定条码的图书是否被续借过?");
		try{
			String barCode = (String) inputFromClient.readObject();
			log("条形码 : " + barCode);
			int renew = libDataAccessor.getBarcodeIsRenewed(barCode);
			log("馆藏图书是否被续借："+renew);
			outputToClient.writeInt(renew);
			outputToClient.flush();
		}catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}

	/**
	 * 方法二十二：续借图书
	 * @param flag
	 */
	private void renewBook() {
		log("续借图书");
		try{
			String readerid = (String)inputFromClient.readObject();
			log("读者编号："+readerid);
			String barCode = (String) inputFromClient.readObject();
			log("条形码 : " + barCode);
			boolean mark = libDataAccessor.execRenewBook(readerid,barCode);
			log("续借图书："+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 * 方法二十三：图书统计
	 * @param flag
	 */
	private void opGetCountList() {
		try {
			log("读图书基本数据");			
			List countList = libDataAccessor.getBookCountList();
			outputToClient.writeObject(countList);
			outputToClient.flush();
			log("发出 " + countList.size() + " 条信息到客户端.");
		} catch (IOException exc) {
			log("发生I/O异常:  " + exc);
		} 
	}
	/**
	 * 方法二十四：超期图书
	 */
	private void opGetOverDueBooks() {
		try{
			log("超期图书查询");			
			List bList = libDataAccessor.getOverDueBooks();
			outputToClient.writeObject(bList);
			outputToClient.flush();
			log("发出 " + bList.size() + " 条信息到客户端.");
		}catch (IOException exc) {
			log("发生I/O异常:  " + exc);
		} 
	}
	
	/**
	 * 方法二十五：图书出库
	 * @param flag
	 */


	private void opBookOutLib() {
		log("图书出库");
		try{
			String isbn = (String)inputFromClient.readObject();
			log(isbn);
			boolean mark = libDataAccessor.outLib(isbn);
			log("图书出库："+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
		
	}
	
	/**
	 * 方法二十六：根据isbn取得图书详细信息
	 */
	private void opGetBookDetailsByIsbn() {
	log("根据isbn取得图书详细信息");
	try{
		String isbn = (String)inputFromClient.readObject();
		log(isbn);
		BookDetails bookDetails = libDataAccessor.getBookIsbnDetails(isbn);
		log("图书："+bookDetails.getName());
		outputToClient.writeObject(bookDetails);
		outputToClient.flush();
	}catch (IOException exc) {
		log("发生异常:  " + exc);
		exc.printStackTrace();
	} catch (ClassNotFoundException exc) {
		log("发生异常:  " + exc);
		exc.printStackTrace();
	}
	}

	/**
	 * 方法二十七：删除指定编号的读者
	 */
		private void execReaderByReaderId() {
			log("根据readerid删除读者详细信息");
			try{
				String readerid = (String)inputFromClient.readObject();
				log(readerid);
				boolean mark = libDataAccessor.execReaderDelete(readerid);
				log("读者："+mark);
				outputToClient.writeBoolean(mark);
				outputToClient.flush();
			}catch (IOException exc) {
				log("发生异常:  " + exc);
			} catch (ClassNotFoundException exc) {
				log("发生异常:  " + exc);
			}
		}
	//多线程的标志
	public void setDone(boolean flag) {
		done = flag;
	}

	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "LibOpHandler类 ："
				+ msg);
	}
	
	/**
	 * 添加管理员
	 */
	protected void opAddMaster(){
		try{
			log("读取管理员信息");
			String userid = (String) inputFromClient.readObject();
			String password = (String) inputFromClient.readObject();
			String name = (String) inputFromClient.readObject();
			int state1 = (int)inputFromClient.readInt();
			int state2 = (int)inputFromClient.readInt();
			int state3 = (int)inputFromClient.readInt();
			log("用户id:"+userid);
			
			boolean b = libDataAccessor.addMaster(userid,password,name,state1,state2,state3);
			outputToClient.writeBoolean(b);
			outputToClient.flush();
		}catch(IOException exc){
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}catch(ClassNotFoundException exc){
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}

	/**
	 * 取得管理员列表
	 * @param flag
	 */
	protected void opGetMaster(){
		try{
			log("获取管理员列表");
			ArrayList masterList = libDataAccessor.getMaster();
			outputToClient.writeObject(masterList);
			outputToClient.flush();
			log("成功发送管理员信息");
		}catch(IOException exc){
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * 修改管理员信息
	 */
	protected void opModifyMaster(){
		try{
			log("读取管理员信息");
			String userid = (String) inputFromClient.readObject();
			String password = (String) inputFromClient.readObject();
			String name = (String) inputFromClient.readObject();
			int state1 = (int)inputFromClient.readInt();
			int state2 = (int)inputFromClient.readInt();
			int state3 = (int)inputFromClient.readInt();
			log("用户id:"+userid);
			
			boolean b = libDataAccessor.modifyMaster(userid,password,name,state1,state2,state3);
			outputToClient.writeBoolean(b);
			outputToClient.flush();
		}catch(IOException exc){
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}catch(ClassNotFoundException exc){
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 * 删除管理员信息
	 */
	protected void opDellMaster(){
		try{
			log("读取管理员信息");
			String userid = (String) inputFromClient.readObject();
			log("用户id:"+userid);
			
			boolean b = libDataAccessor.dellMaster(userid);
			outputToClient.writeBoolean(b);
			outputToClient.flush();
		}catch(IOException exc){
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}catch(ClassNotFoundException exc){
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 * 获取管理员信息
	 */
//	protected void opGetLibraiaInfo() {
//
//		try {
//			log("获取管理员信息");
//			String userID = (String) inputFromClient.readObject();
//			String librarianPW = (String) inputFromClient.readObject();
//			log("管理员ID : " + userID);
//
//			LibraianInfo librariaInfo = libDataAccessor.getLibraianInfo(userID,
//					librarianPW);
//			outputToClient.writeObject(librariaInfo);
//			outputToClient.flush();
//			log("发管理员信息到客户端.");
//		} catch (IOException exc) {
//			log("发生异常:  " + exc);
//			exc.printStackTrace();
//		} catch (ClassNotFoundException exc) {
//			log("发生异常:  " + exc);
//			exc.printStackTrace();
//		}
//	}
	/**
	 * 查询参数信息
	 */
	protected void opGetParameterInfo(){
		try {
			log("参数信息情况");
			String parameterID = (String) inputFromClient.readObject();
	//		String keyword = (String) inputFromClient.readObject();

			ArrayList paraList = libDataAccessor.getParamterInfo(parameterID);
			outputToClient.writeObject(paraList);
			outputToClient.flush();
			log("发出 " + paraList.size() + " 该参数信息到客户端.");
		} catch (IOException exc) {
			log("发生I/O异常:  " + exc);
		} catch (ClassNotFoundException exc) {
			log("发生找不到类异常:  " + exc);
			exc.printStackTrace();
		}
    	
    	
    }
	
	/**
	 * 更新参数信息
	 */
	protected void opUpdateParameter() {
		boolean b = false;

		try {
			int  type = (int) inputFromClient.readInt();
			int  amount = (int) inputFromClient.readInt();
			int  period = (int) inputFromClient.readInt();
			double  dailyfine = (double) inputFromClient.readDouble();
			log("读者类别 : " + type);

			b = libDataAccessor.updateParameter(type,amount,period,dailyfine);
			outputToClient.writeBoolean(b);
			outputToClient.flush();
		} catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}


}
