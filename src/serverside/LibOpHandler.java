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
 * ���������̣߳����Ӳ��������Կͻ��˵�����
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

				log("�ȴ�����...");

				int opCode = inputFromClient.readInt();
				log("opCode = " + opCode);

				switch (opCode) {
			    	/**
		    	    * 1.ȡ���鼮����ϸ��Ϣ
			     	*/
				case LibProtocals.OP_GET_BOOK_DETAILS:
					opGetBookDetails();
					break;
					/**
					 * 2.ȡ���鼮�Ĺݲ���Ϣ
					 */
				case LibProtocals.OP_GET_BOOK_LIBINFO:
					opGetBookLibInfo();
					break;
					/**
					 * 3.ȡ���û��Ľ�����Ϣ
					 */
				case LibProtocals.OP_GET_BORROWINFO:
					opGetBorrowInfo();
					break;
					/**
					 * 4.��ȡ���ߵĸ�����ϸ��Ϣ
					 */
				case LibProtocals.OP_GET_READERINFO:
					opGetReaderInfo();
					break;
					/**
					 * 5.��ȡ����Ա�ĸ�����ϸ��Ϣ
					 */
				case LibProtocals.OP_GET_LIBRAIANINFO:
					opGetLibarianInfo();
					break;
					/**
					 * 6.ͼ��ά��
					 */
				case LibProtocals.OP_BOOK_MAINTAIN:
					opGetBookMaintainInfo();
					break;
					/**
					 *7.����ά�� --ȡ�����ж�����Ϣ
					 */
				case LibProtocals.OP_READER_MAINTAIN:
					opGetReaderMaintainInfo();
					break;
					/**
					 * 8.ͼ����Ϣ�޸�
					 */
				case LibProtocals.OP_EXEC_BOOK_MODIFY:
					opModifyBookInfo();
					break;
					/**
					 * 9.������Ϣ�޸�
					 */
				case LibProtocals.OP_EXEC_READER_MODIFY:
					opModifyReaderInfo();
					break;
					/**
					 * 10.���ͼ��
					 */
				case LibProtocals.OP_EXEC_BOOK_INSERT:
					opInsertBookData();
					break;
					//�ݲ���Ϣ
				case LibProtocals.OP_EXEC_BOOK_IN_LIB_INSERT:
					opInsertBookInfo();
					break;
					/**
					 * 11.��Ӷ�����Ϣ
					 */
				case LibProtocals.OP_EXEC_READER_INSERT:
					opInsertReaderInfo();
					break;
					/**
					 * 12.���߽��ĵ��������
					 */
				case LibProtocals.OP_GET_PARAM_ACCOUNT:
					opGetAccount();
					break;
					/**
					 * 13.ȡ��ָ�������ͼ�����ϸ��Ϣ
					 */
				case LibProtocals.OP_GET_BARCODE_BOOK_DETAILS:
					opGetBarBook();
					break;
					/**
					 * 14.�����޸�����
					 */
				case LibProtocals.READER_MODIFY_PASSWORD:
					execReaderModPass();
					break;
					/**
					 * 15.���߽���
					 */
				case LibProtocals.OP_BORROW_BOOK:
					execReaderBorrowBook();
					break;
					/**
					 * 16.����
					 */
				case LibProtocals.OP_RETURN_BOOK:
					opReturnBook();
					break;
					/**
					 * 17.���ָ�������ͼ���Ƿ�ɽ�
					 */
				case LibProtocals.BARCODE_BOOK_BORROW:
					checkBarcodeBookBorrow();
					break;
					/**
					 * 18.���ָ�������ͼ���Ƿ����
					 */
				case LibProtocals.BARCODE_BOOK_EXISTS:
					checkBarcodeExists();
					break;
					/**
					 * 19.�鿴ָ���������˭����
					 */
				case LibProtocals.BARCODE_BOOK_BORROW_READER:
					execGetReaderInfoByBarcode();
					break;
					/**
					 * 20.���ָ�������ͼ���Ƿ����ڹݲ���
					 */
				case LibProtocals.BARCODE_BOOK_IN_LIB:
					checkIsBorrowed();
					break;
					/**
					 * 22.���ָ�������ͼ���Ƿ������
					 */
				case LibProtocals.BARCODE_BOOK_BE_BORROWED:
					checkBarcodeBookIsRenewed();
					break;
					/**
					 * 23.����ͼ��
					 */
				case LibProtocals.RENEW_BOOK:
					renewBook();
					break;
					/**
					 * 24.ͼ���ͳ����Ϣ
					 */
				case LibProtocals.GET_COUNT_INFO:
					opGetCountList();
					break;
					/**
					 * 25.����ͼ��
					 */
				case LibProtocals.OP_GET_OVER_DUE_BOOKS:
					opGetOverDueBooks();
					break;
					/**
					 * 26.ͼ�����
					 */
				case LibProtocals.BOOK_OUT_LIB:
					opBookOutLib();
					break;
					/**
					 *27. ɾ��ָ����ŵĶ���
					 */
				case LibProtocals.READER_OUT_LIB:
					execReaderByReaderId();
					break;
					/**
					 * 28.����sibnȡ�ö�����ϸ��Ϣ
					 */
				case LibProtocals.OP_GET_ISBN_BOOK_DETAILS:
					opGetBookDetailsByIsbn();
					break;
					/**
					 * ���ϣ���ӹ���Ա
					 */
				case LibProtocals.OP_LIBRAIAN_ADDMASTER:
					opAddMaster();
					break;
					/**
					 * ��ȡ����Ա�б�
					 */
				case LibProtocals.OP_LIBRAIAN_GETMASTER:
					opGetMaster();
					break;
					/**
					 * �޸Ĺ���Ա��Ϣ
					 */
				case LibProtocals.OP_LIBRAIAN_MODIFYMASTER:
					opModifyMaster();
					break;
					/**
					 * ɾ������Ա
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
					log("�������");
				}
			}
		} catch (IOException e1) {
			try {
				clientSocket.close();
			} catch (Exception e2) {
				log("run�쳣" + e2);
			}

			log(clientSocket.getInetAddress() + "�ͻ��뿪��");
			// log("�쳣" + e1);
		}
	}

	/**
	 * ����һ������������ȡ��ͼ����Ϣ
	 */
	private void opGetBarBook() {
		try{
			String barCode = (String)inputFromClient.readObject();
			log("ͼ��barcode��"+barCode);
			BookDetails bookDetails = libDataAccessor.getBookBarDetails(barCode);
			log("�������ָ��barcode����ͼ����¼���"+bookDetails.getName());
			outputToClient.writeObject(bookDetails);
			outputToClient.flush();
		}catch(ClassNotFoundException e){
			log("��û���ҵ�"+e.getMessage());
		}catch(IOException e){
			log("IO����"+e.getMessage());
		}
	}

	/**
	 * �����������ݶ�������ȡ�ÿɽ���ͼ��������
	 */
	private void opGetAccount() {
		try {
			int type = inputFromClient.readInt();
			int account = libDataAccessor.getCanBorrowAccount(type);
			outputToClient.writeInt(account);
			outputToClient.flush();
			log(account);
		} catch (IOException exc) {
			log("����I/O�쳣:  " + exc);
		} 
	}

	/**
	 * ��������ȡ���ض��������鼮����ϸ��Ϣ
	 */
	private void opGetBookDetails() {

		try {
			log("��ͼ���������");
			String field = (String) inputFromClient.readObject();
			String keyword = (String) inputFromClient.readObject();

			List bookList = libDataAccessor.getBookDetails(field, keyword);
			outputToClient.writeObject(bookList);
			outputToClient.flush();
			log("���� " + bookList.size() + " ��ͼ����Ϣ���ͻ���.");
		} catch (IOException exc) {
			log("����I/O�쳣:  " + exc);
		} catch (ClassNotFoundException exc) {
			log("�����Ҳ������쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * �����ģ�ȡ��ͼ��Ĺݲ� ����ϸ��Ϣ
	 */
	private void opGetBookLibInfo() {

		try {
			log("��ͼ��ݲ����");
			String isbn = (String) inputFromClient.readObject();
			log("��ISBN���� : " + isbn);
			List bookLibList = libDataAccessor.getBookLibInfo(isbn);
			outputToClient.writeObject(bookLibList);
			outputToClient.flush();
			log("���� " + bookLibList.size() + " ��������Ϣ���ͻ���.");
		} catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * �����壺ȡ��ͼ��Ľ�����Ϣ
	 */
	private void opGetBorrowInfo() {

		try {
			log("���û��������");
			String readerid = (String) inputFromClient.readObject();
			log("����ID : " + readerid);

			List[] borrowList = libDataAccessor.getBorrowInfo(readerid);
			outputToClient.writeObject(borrowList);
			outputToClient.flush();
			log("���� " + borrowList.length + " ��������Ϣ���ͻ���.");
		} catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * ��������ȡ���ض��Ķ��ߵ���ϸ��Ϣ
	 */
	private void opGetReaderInfo(){
		try{
			log("��ȡ���ߵĸ�����Ϣ");
			String  idAndpass = (String) inputFromClient.readObject();
			String [] str = idAndpass.split(",");
			String readerID = str[0];
			String pass = str[1];
			log("���߱�ţ�"+readerID+"\t�������룺"+pass);
			Object obj = libDataAccessor.getReaderInfo(readerID,pass);
			outputToClient.writeObject(obj);
			outputToClient.flush();
			log("obj="+obj);
		}catch(IOException e){
			log("�����쳣:  " + e);
			e.printStackTrace();
		}catch(ClassNotFoundException ee){
			log("�����쳣:  " + ee);
			ee.printStackTrace();
		}
	}
	/**
	 * �����ߣ�ȡ���ض��Ĺ���Ա����ϸ��Ϣ
	 */
	private void opGetLibarianInfo(){
		try {
			log("������Ա��Ϣ");
			String idAndpass = (String) inputFromClient.readObject();
			String [] str = idAndpass.split(",");
			String libarianID = str[0];
			String pass = str[1];
			log("����ԱID : " + libarianID);
			Object obj = libDataAccessor.getLibarianInfo(libarianID,pass);
			outputToClient.writeObject(obj);
			outputToClient.flush();
		} catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * �����ˣ�ȡ��ͼ��ά������Ҫ��ͼ����Ϣ
	 */
	private void opGetBookMaintainInfo(){
		log("����Ϣ");
		try{
			log("��ͼ����Ϣ");
			String str = (String)inputFromClient.readObject();
			log("����"+str);
			List list = libDataAccessor.getBooksInfos(str);	
			log("list:"+list);
			outputToClient.writeObject(list);
			outputToClient.flush();
		}catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * �����ţ�ȡ�ö���ά������Ҫ�Ķ�����Ϣ
	 */
	private void opGetReaderMaintainInfo(){
		log("����Ϣ");
		try{
			log("��������Ϣ");
			String str = (String)inputFromClient.readObject();
			log("����"+str);
			List list = libDataAccessor.getReadersInfos(str);	
			log("list:"+list);
			outputToClient.writeObject(list);
			outputToClient.flush();
		}catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * ����ʮ�������޸�ͼ����Ϣ
	 */
	private void opModifyBookInfo(){
		log("��������׼���޸�����");
		try{
		BookDetails bookDetails = (BookDetails) inputFromClient.readObject();
		log("���յ�ͼ����Ϣ");
		boolean mark = libDataAccessor.execBookUpdate(bookDetails);
		log("ִ��ͼ����Ϣ��"+mark);
		outputToClient.writeBoolean(mark);
		outputToClient.flush();
		}catch(IOException ei){
			log("IO����");
		}catch(ClassNotFoundException eb){
			log("�ļ�û���ҵ�");
		}
	}
	/**
	 * ����ʮһ���޸Ķ�����Ϣ
	 */
	private void opModifyReaderInfo(){
		log("��������׼���޸�����");
		try{
		ReaderInfo readerInfo = (ReaderInfo) inputFromClient.readObject();
		log("���յ�������Ϣ");
		boolean mark = libDataAccessor.execReaderUpdate(readerInfo);
		log("ִ�ж�����Ϣ�޸ģ�"+mark);
		outputToClient.writeBoolean(mark);
		outputToClient.flush();
		}catch(IOException ei){
			log("IO����");
		}catch(ClassNotFoundException eb){
			log("�ļ�û���ҵ�");
		}
	}
	/**
	 * ����ʮ�������ͼ����Ϣ
	 */
	private void opInsertBookData(){
		log("��ʼ���ͼ����Ϣ");
		try{
			BookDetails bookDetails = (BookDetails) inputFromClient.readObject();
			log("���յ�ͼ����Ϣ"+bookDetails);
//			BookInLibrary bookInLibrary = (BookInLibrary) inputFromClient.readObject();
//			log("���յ�ͼ����Ϣ�ݲص���Ϣ"+bookInLibrary);
			boolean mark = libDataAccessor.execBookDataInsert(bookDetails);
			log("ִ��ͼ����룺"+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(IOException e){
			log("IO����"+e.getMessage());
		}catch(ClassNotFoundException eb){
			log("�ļ�û���ҵ�");
		}
	}
	
	private void opInsertBookInfo(){
		log("��ʼ��ӹݲ���Ϣ");
		try{
			BookInLibrary bookInLibrary = (BookInLibrary) inputFromClient.readObject();
			log("���յ�ͼ����Ϣ�ݲص���Ϣ"+bookInLibrary.getLocation());
			boolean mark = libDataAccessor.execBookInfoInsert(bookInLibrary);
			log("ִ�йݲز��룺"+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(IOException e){
			log("IO����"+e.getMessage());
		}catch(ClassNotFoundException eb){
			log("�ļ�û���ҵ�");
		}
	}
	
	/**
	 * ����ʮ������Ӷ�����Ϣ
	 */
	private void opInsertReaderInfo(){
		log("��ʼ��Ӷ�����Ϣ");
		try{
			ReaderInfo readerInfo = (ReaderInfo) inputFromClient.readObject();
			log("���յ�������Ϣ"+readerInfo);
			boolean mark = libDataAccessor.execReaderInsert(readerInfo);
			log("ִ�в�ѯ��"+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(IOException e){
			log("IO����"+e.getMessage());
		}catch(ClassNotFoundException eb){
			log("�ļ�û���ҵ�");
		}
	}
	
	/**
	 * ����ʮ�ģ������޸��Լ�������
	 */
	private void execReaderModPass(){
		log("׼��ִ���޸�����");
		try{
		String readerid = (String)inputFromClient.readObject();
		
		String pass = (String)inputFromClient.readObject();
		log("���յ��û����:"+readerid+"\t���룺"+pass);
		boolean modify = libDataAccessor.execPassModify(readerid,pass);
		outputToClient.writeBoolean(modify);
		outputToClient.flush();
		}catch(ClassNotFoundException e){
			log("�ļ�û�ҵ�:"+e.getMessage());
		}catch(IOException e){
			log("IO�쳣:"+e.getMessage());
		}
	}
	
	/**
	 * ����ʮ�壺���߽���
	 */
	private void execReaderBorrowBook(){
		log("׼��ִ�н���");
		try{
			String readerid = (String)inputFromClient.readObject();
			log("���߱�ţ�"+readerid);
			String barCode = (String)inputFromClient.readObject();
			log("�����룺"+barCode);
			boolean mark = libDataAccessor.execReaderBorrowBook(readerid,barCode);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(ClassNotFoundException e){
			log("�ļ�û�ҵ�:"+e.getMessage());
		}catch(IOException e){
			log("IO�쳣:"+e.getMessage());
		}
	}
	
	/**
	 *  ����ʮ�������ָ�������ͼ���Ƿ����
	 */
	private void checkBarcodeExists(){
		log("ִ�м��ָ�������ͼ���Ƿ����");
		try{
			String barCode = (String)inputFromClient.readObject();
			log("�����룺"+barCode);
			boolean mark = libDataAccessor.checkBarBookExists(barCode);
			log("���յ���mark��"+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(ClassNotFoundException e){
			log("�ļ�û�ҵ�:"+e.getMessage());
		}catch(IOException e){
			log("IO�쳣:"+e.getMessage());
		}		
	}
	
	/**
	 *  ����ʮ�ߣ����ָ�������ͼ���Ƿ�ɽ�
	 */
	private void checkBarcodeBookBorrow(){
		log("ִ�м��ָ�������ͼ���Ƿ�ɽ�");
		try{
			String barCode = (String)inputFromClient.readObject();
			log("�����룺"+barCode);
			boolean mark = libDataAccessor.checkBarBookCanBorrow(barCode);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch(ClassNotFoundException e){
			log("�ļ�û�ҵ�:"+e.getMessage());
		}catch(IOException e){
			log("IO�쳣:"+e.getMessage());
		}		
	}
	
	/**
	 *  ����ʮ�ˣ�����
	 */
	protected void opReturnBook() {
		log("��ʼִ�л��飺");
		try {
			log("��ȡ��Ӧͼ��");
			String barCode = (String) inputFromClient.readObject();
			log("������ : " + barCode);
			BorrowInfo borrowInfo = libDataAccessor.returnBook(barCode);
			log("����"+borrowInfo.getReaderId());
			outputToClient.writeObject(borrowInfo);
			outputToClient.flush();
			log("����������Ϣ���ͻ���.");
		} catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 *  ����ʮ�ţ�����ָ������鿴���ĸ���Ķ��ߵ���Ϣ
	 */
	private void execGetReaderInfoByBarcode() {
		log("��ʼ��ȡ����");
		try{
			log("��ȡ��Ӧͼ��");
			String barCode = (String) inputFromClient.readObject();
			log("������ : " + barCode);
			ReaderInfo readerInfo = libDataAccessor.getReaderInfoByBarcode(barCode);
			log("����"+readerInfo.getName());
			outputToClient.writeObject(readerInfo);
			outputToClient.flush();
			log("����������Ϣ���ͻ���.");
		}catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 * ������ʮ�����ָ�������ͼ���Ƿ����ڹݲ���
	 */
	private void checkIsBorrowed() {
		log("���ָ�������ͼ���Ƿ����ڹݲ��У�");
		try{
			String barCode = (String) inputFromClient.readObject();
			log("������ : " + barCode);
			boolean mark = libDataAccessor.getBarCodeIsBorrowed(barCode);
			log("�ݲ�ͼ���Ƿ���Ի���"+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 * ������ʮһ�����ָ�������ͼ���Ƿ��ѱ������
	 */
	private void checkBarcodeBookIsRenewed() {
		log("���ָ�������ͼ���Ƿ������?");
		try{
			String barCode = (String) inputFromClient.readObject();
			log("������ : " + barCode);
			int renew = libDataAccessor.getBarcodeIsRenewed(barCode);
			log("�ݲ�ͼ���Ƿ����裺"+renew);
			outputToClient.writeInt(renew);
			outputToClient.flush();
		}catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}

	/**
	 * ������ʮ��������ͼ��
	 * @param flag
	 */
	private void renewBook() {
		log("����ͼ��");
		try{
			String readerid = (String)inputFromClient.readObject();
			log("���߱�ţ�"+readerid);
			String barCode = (String) inputFromClient.readObject();
			log("������ : " + barCode);
			boolean mark = libDataAccessor.execRenewBook(readerid,barCode);
			log("����ͼ�飺"+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 * ������ʮ����ͼ��ͳ��
	 * @param flag
	 */
	private void opGetCountList() {
		try {
			log("��ͼ���������");			
			List countList = libDataAccessor.getBookCountList();
			outputToClient.writeObject(countList);
			outputToClient.flush();
			log("���� " + countList.size() + " ����Ϣ���ͻ���.");
		} catch (IOException exc) {
			log("����I/O�쳣:  " + exc);
		} 
	}
	/**
	 * ������ʮ�ģ�����ͼ��
	 */
	private void opGetOverDueBooks() {
		try{
			log("����ͼ���ѯ");			
			List bList = libDataAccessor.getOverDueBooks();
			outputToClient.writeObject(bList);
			outputToClient.flush();
			log("���� " + bList.size() + " ����Ϣ���ͻ���.");
		}catch (IOException exc) {
			log("����I/O�쳣:  " + exc);
		} 
	}
	
	/**
	 * ������ʮ�壺ͼ�����
	 * @param flag
	 */


	private void opBookOutLib() {
		log("ͼ�����");
		try{
			String isbn = (String)inputFromClient.readObject();
			log(isbn);
			boolean mark = libDataAccessor.outLib(isbn);
			log("ͼ����⣺"+mark);
			outputToClient.writeBoolean(mark);
			outputToClient.flush();
		}catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
		
	}
	
	/**
	 * ������ʮ��������isbnȡ��ͼ����ϸ��Ϣ
	 */
	private void opGetBookDetailsByIsbn() {
	log("����isbnȡ��ͼ����ϸ��Ϣ");
	try{
		String isbn = (String)inputFromClient.readObject();
		log(isbn);
		BookDetails bookDetails = libDataAccessor.getBookIsbnDetails(isbn);
		log("ͼ�飺"+bookDetails.getName());
		outputToClient.writeObject(bookDetails);
		outputToClient.flush();
	}catch (IOException exc) {
		log("�����쳣:  " + exc);
		exc.printStackTrace();
	} catch (ClassNotFoundException exc) {
		log("�����쳣:  " + exc);
		exc.printStackTrace();
	}
	}

	/**
	 * ������ʮ�ߣ�ɾ��ָ����ŵĶ���
	 */
		private void execReaderByReaderId() {
			log("����readeridɾ��������ϸ��Ϣ");
			try{
				String readerid = (String)inputFromClient.readObject();
				log(readerid);
				boolean mark = libDataAccessor.execReaderDelete(readerid);
				log("���ߣ�"+mark);
				outputToClient.writeBoolean(mark);
				outputToClient.flush();
			}catch (IOException exc) {
				log("�����쳣:  " + exc);
			} catch (ClassNotFoundException exc) {
				log("�����쳣:  " + exc);
			}
		}
	//���̵߳ı�־
	public void setDone(boolean flag) {
		done = flag;
	}

	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "LibOpHandler�� ��"
				+ msg);
	}
	
	/**
	 * ��ӹ���Ա
	 */
	protected void opAddMaster(){
		try{
			log("��ȡ����Ա��Ϣ");
			String userid = (String) inputFromClient.readObject();
			String password = (String) inputFromClient.readObject();
			String name = (String) inputFromClient.readObject();
			int state1 = (int)inputFromClient.readInt();
			int state2 = (int)inputFromClient.readInt();
			int state3 = (int)inputFromClient.readInt();
			log("�û�id:"+userid);
			
			boolean b = libDataAccessor.addMaster(userid,password,name,state1,state2,state3);
			outputToClient.writeBoolean(b);
			outputToClient.flush();
		}catch(IOException exc){
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}catch(ClassNotFoundException exc){
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}

	/**
	 * ȡ�ù���Ա�б�
	 * @param flag
	 */
	protected void opGetMaster(){
		try{
			log("��ȡ����Ա�б�");
			ArrayList masterList = libDataAccessor.getMaster();
			outputToClient.writeObject(masterList);
			outputToClient.flush();
			log("�ɹ����͹���Ա��Ϣ");
		}catch(IOException exc){
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	/**
	 * �޸Ĺ���Ա��Ϣ
	 */
	protected void opModifyMaster(){
		try{
			log("��ȡ����Ա��Ϣ");
			String userid = (String) inputFromClient.readObject();
			String password = (String) inputFromClient.readObject();
			String name = (String) inputFromClient.readObject();
			int state1 = (int)inputFromClient.readInt();
			int state2 = (int)inputFromClient.readInt();
			int state3 = (int)inputFromClient.readInt();
			log("�û�id:"+userid);
			
			boolean b = libDataAccessor.modifyMaster(userid,password,name,state1,state2,state3);
			outputToClient.writeBoolean(b);
			outputToClient.flush();
		}catch(IOException exc){
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}catch(ClassNotFoundException exc){
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 * ɾ������Ա��Ϣ
	 */
	protected void opDellMaster(){
		try{
			log("��ȡ����Ա��Ϣ");
			String userid = (String) inputFromClient.readObject();
			log("�û�id:"+userid);
			
			boolean b = libDataAccessor.dellMaster(userid);
			outputToClient.writeBoolean(b);
			outputToClient.flush();
		}catch(IOException exc){
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}catch(ClassNotFoundException exc){
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ����Ա��Ϣ
	 */
//	protected void opGetLibraiaInfo() {
//
//		try {
//			log("��ȡ����Ա��Ϣ");
//			String userID = (String) inputFromClient.readObject();
//			String librarianPW = (String) inputFromClient.readObject();
//			log("����ԱID : " + userID);
//
//			LibraianInfo librariaInfo = libDataAccessor.getLibraianInfo(userID,
//					librarianPW);
//			outputToClient.writeObject(librariaInfo);
//			outputToClient.flush();
//			log("������Ա��Ϣ���ͻ���.");
//		} catch (IOException exc) {
//			log("�����쳣:  " + exc);
//			exc.printStackTrace();
//		} catch (ClassNotFoundException exc) {
//			log("�����쳣:  " + exc);
//			exc.printStackTrace();
//		}
//	}
	/**
	 * ��ѯ������Ϣ
	 */
	protected void opGetParameterInfo(){
		try {
			log("������Ϣ���");
			String parameterID = (String) inputFromClient.readObject();
	//		String keyword = (String) inputFromClient.readObject();

			ArrayList paraList = libDataAccessor.getParamterInfo(parameterID);
			outputToClient.writeObject(paraList);
			outputToClient.flush();
			log("���� " + paraList.size() + " �ò�����Ϣ���ͻ���.");
		} catch (IOException exc) {
			log("����I/O�쳣:  " + exc);
		} catch (ClassNotFoundException exc) {
			log("�����Ҳ������쳣:  " + exc);
			exc.printStackTrace();
		}
    	
    	
    }
	
	/**
	 * ���²�����Ϣ
	 */
	protected void opUpdateParameter() {
		boolean b = false;

		try {
			int  type = (int) inputFromClient.readInt();
			int  amount = (int) inputFromClient.readInt();
			int  period = (int) inputFromClient.readInt();
			double  dailyfine = (double) inputFromClient.readDouble();
			log("������� : " + type);

			b = libDataAccessor.updateParameter(type,amount,period,dailyfine);
			outputToClient.writeBoolean(b);
			outputToClient.flush();
		} catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}


}
