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
	 * socket����
	 */
	protected Socket hostSocket;

	/**
	 * �����������
	 */
	protected ObjectOutputStream outputToServer;

	/**
	 * ������������
	 */
	protected ObjectInputStream inputFromServer;

	/**
	 * �����������Ͷ˿ںŵĹ��췽��
	 */
	public LibClient(String host, int port){

		log("�������ݷ�����..." + host + ":" + port);

		try {
		hostSocket = new Socket(host, port);
		outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
		inputFromServer = new ObjectInputStream(hostSocket.getInputStream());
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "���ӷ�������������servinfo.txt�����ļ�");
		} catch (IOException e) {
			log("IO����"+e.getMessage());
		}
		log("���ӳɹ�.");
	}

	/**
	 * ����һ��ȡ����ؼ���ƥ������ͼ�鼯��
	 */
	public ArrayList getBookList(String field, String keyword)
			throws IOException {

		ArrayList bookList = null;

		try {
			log("��������: OP_GET_BOOK_DETAILS");
			outputToServer.writeInt(LibProtocals.OP_GET_BOOK_DETAILS);
			outputToServer.writeObject(field);
			outputToServer.writeObject(keyword);
			outputToServer.flush();

			log("��������...");
			bookList = (ArrayList) inputFromServer.readObject();
			log("�յ�  " + bookList.size() + " �����ͼ��.");
		} catch (ClassNotFoundException exc) {
			log("ȡͼ���쳣: " + exc);
			throw new IOException("�Ҳ��������");
		}
		return bookList;
	}

	/**
	 * ��������ȡ��ÿ�������ݲ����
	 */
	public ArrayList getBookInLibrary(String isbn) throws IOException {

		ArrayList bookInLibraryInfo = null;

		try {
			log("��������: OP_GET_BOOK_LIBINFO�� ���  = " + isbn);
			outputToServer.writeInt(LibProtocals.OP_GET_BOOK_LIBINFO);
			outputToServer.writeObject(isbn);
			outputToServer.flush();

			log("��������...");
			bookInLibraryInfo = (ArrayList) inputFromServer.readObject();
			log("�յ�  " + bookInLibraryInfo.size() + " ��ݲؼ�¼.");
		} catch (ClassNotFoundException exc) {
			log("ȡ�ݲ���Ϣ�쳣: " + exc);
			throw new IOException("�Ҳ��������");
		}
		return bookInLibraryInfo;
	}

	/**
	 * ��������ȡ���û���ͼ��ݵĽ�����Ϣ
	 */
	public List[] getReaderBorrowInfo(String readerid) {

		List[] readerBorrownInfoList = null;

		try {
			log("��������: OP_GET_BORROWINFO�� ����  = " + readerid);
			outputToServer.writeInt(LibProtocals.OP_GET_BORROWINFO);
			outputToServer.writeObject(readerid);
			outputToServer.flush();

			log("��������...");
			readerBorrownInfoList= (List[]) inputFromServer.readObject();
			log("�յ�  " + readerBorrownInfoList.length + " ����ļ�¼");
		} catch (ClassNotFoundException exc) {
			log("ȡ������Ϣ�쳣: " + exc);
		}catch(IOException e){
			log("IO����:"+e.getMessage());
		}
		return readerBorrownInfoList;
	}

	/**
	 * 
	 * �����ģ�ȡ�ù���Ա�����û��������ȵ���ϸ��Ϣ
	 */
	public Object getName(int mark, String ID,String passwd) {
		Object obj = null;
		log("������ȡ�û���Ϣ������getName" + ID + "" + mark);
		try {
			if (OP_GET_READERINFO == mark) {
				log("���ߵĸ�����Ϣ");
				outputToServer.writeInt(OP_GET_READERINFO);
				outputToServer.writeObject(ID+","+passwd);
				outputToServer.flush();
				log("�������ݡ�������");
				obj = inputFromServer.readObject();				
			}
			if (OP_GET_LIBRAIANINFO == mark) {
				log("����Ա�ĸ�����Ϣ");
				log("OP_GET_LIBRAIANINFO=" + OP_GET_LIBRAIANINFO + "mark="+ mark);
				outputToServer.writeInt(OP_GET_LIBRAIANINFO);
				log("д��������");
				outputToServer.writeObject(ID+","+passwd);
				outputToServer.flush();
				log("�������ݡ�������");
				obj = inputFromServer.readObject();
			}
		} catch (IOException e) {
			log("����" + e.getMessage());
		} catch (ClassNotFoundException exc) {
			log("ȡ������Ϣ�쳣: " + exc);
		}
		log("�����û���Ϣ:"+obj);
		return obj;
	}

	/**
	 * �����壺��ȡ�鼮�����ߡ�����Ա���߲�����Ϣ��
	 */
	public List getAllInfos(String str, String condition) {
		List list = null;
		/**
		 * �鼮ά��ʱ���鼮����ϸ��Ϣ
		 */
		log("������ȡ��Ϣ������" + str);
		try {
			if ("bookdata".equals(str)) {
				outputToServer.writeInt(OP_BOOK_MAINTAIN);
				log(condition);
				outputToServer.writeObject(condition);
				outputToServer.flush();
				log("�������ݡ�������");
				list = (List) inputFromServer.readObject();
				log("����list:" + list.size());
				return list;
			}
			/**
			 * ������Ϣά��ʱ���ߵ���ϸ��Ϣ
			 */
			else if ("readerdata".equals(str)) {
				outputToServer.writeInt(OP_READER_MAINTAIN);
				log(condition);
				outputToServer.writeObject(condition);
				outputToServer.flush();
				log("�������ݡ�������");
				list = (List) inputFromServer.readObject();
				log("����list:" + list.size());
				return list;
			}
			/**
			 * ����Ա��Ϣά��ʱ����Ա����ϸ��Ϣ
			 
			else if ("librarian".equals(str)) {

			}
			/**
			 * ������Ϣά��ʱ��ϸ�Ĳ�����Ϣ
			
			else if ("parameter".equals(str)) {

			} */ else {
				JOptionPane.showMessageDialog(null, "��ѯά����Ϣû�ж�Ӧ�����ݱ�");
				return null;
			}
		}catch (IOException e) {
			log("����" + e.getMessage());
		}catch(ClassNotFoundException ef){
			log("û���ҵ���"+ef.getMessage());
		}
		return list;
	}
	
	/**
	 * ���������޸���Ϣ---ϵͳά��
	 */
	public boolean updateInfo(Object obj) {
		boolean mark = false;
		/**
		 * �޸��鼮��Ϣ
		 */
		//JOptionPane.showMessageDialog(null, obj.getClass()); ==>class serverside.BookDetails
		//JOptionPane.showMessageDialog(null, obj.getClass().getName()); ==>serverside.BookDetails
		//JOptionPane.showMessageDialog(null, obj.getClass()==BookDetails.class); ==>true
		
		if (obj.getClass() == BookDetails.class) {			
			log("ͼ����ϸ��Ϣ���޸�");
			try {
				outputToServer.writeInt(OP_EXEC_BOOK_MODIFY);
				outputToServer.writeObject(obj);
				outputToServer.flush();
				log("������Ϣ������");
				mark = inputFromServer.readBoolean();
				log("���յ�mark="+mark);
			} catch (IOException e) {
				mark = false;
				log("IO����"+e.getMessage());
			}
		}
		/**
		 * �޸Ķ�����Ϣ
		 */
		if (obj.getClass() == ReaderInfo.class) {			
			log("������ϸ��Ϣ���޸�");
			try {
				outputToServer.writeInt(OP_EXEC_READER_MODIFY);
				outputToServer.writeObject(obj);
				outputToServer.flush();
				log("������Ϣ������");
				mark = inputFromServer.readBoolean();
				log("���յ�mark="+mark);
			} catch (IOException e) {
				mark = false;
				log("IO����"+e.getMessage());
			}
		}
		/**
		 * �޸Ĺ���Ա��Ϣ
		 
		if (obj.getClass() == LibraianInfo.class) {			
			log("����Ա��Ϣ���޸�");
			try {
				outputToServer.writeInt(OP_EXEC_LIBRARIAN_MODIFY);
				outputToServer.writeObject(obj);
				outputToServer.flush();
				log("������Ϣ������");
				mark = inputFromServer.readBoolean();
				log("���յ�mark="+mark);
			} catch (IOException e) {
				mark = false;
				log("IO����"+e.getMessage());
			}
		}
		*/
		
		/**
		 * �޸Ĳ�����Ϣ
		 */
		
		return mark;
	}
	
	/**
	 * �����ߣ����ͼ�顢���ߡ�����Ա��������Ϣ
	 */
	public boolean insertBookData(BookDetails bookDetails){
		boolean mark = false;
		/**
		 * ����ͼ����Ϣ
		 */
			log("����ͼ����Ϣ��ʼ");			
			try {
				outputToServer.writeInt(OP_EXEC_BOOK_INSERT);
				outputToServer.writeObject(bookDetails);
				outputToServer.flush();
				log("������Ϣ������");
				mark = inputFromServer.readBoolean();
				log("���յ�mark="+mark);
			} catch (IOException e) {
				mark = false;
				log("IO����"+e.getMessage());
			}
		return mark;
	}
	//�ݲ���Ϣ
	public boolean insertBookInfo(BookInLibrary bookInfo){
		boolean mark = false;
		/**
		 * ����ͼ����Ϣ
		 */
			log("����ͼ����Ϣ��ʼ");			
			try {
				outputToServer.writeInt(OP_EXEC_BOOK_IN_LIB_INSERT);
				outputToServer.writeObject(bookInfo);
				outputToServer.flush();
				log("������Ϣ������");
				mark = inputFromServer.readBoolean();
				log("���յ�mark="+mark);
			} catch (IOException e) {
				mark = false;
				log("IO����"+e.getMessage());
			}
		return mark;
	}
	/**
	 * �����ˣ����������Ϣ
	 * @param readerInfo
	 * @return
	 */
	public boolean insertReaderInfo(ReaderInfo readerInfo){
		boolean mark = false;
		try {
			outputToServer.writeInt(OP_EXEC_READER_INSERT);
			outputToServer.writeObject(readerInfo);
			outputToServer.flush();
			log("������Ϣ������");
			mark = inputFromServer.readBoolean();
			log("���յ�mark="+mark);
		} catch (IOException e) {
			mark = false;
			log("IO����"+e.getMessage());
		}
		return mark;
	}
	
	/**
	 * �����ţ�ȡ��ָ�����͵Ķ��߿��Խ��ĵ����ͼ����Ŀ
	 * @param type
	 * @return
	 */
	public int  getAccount(int type) {
		int account = 0;
		try {
			outputToServer.writeInt(OP_GET_PARAM_ACCOUNT);
			outputToServer.writeInt(type);
			outputToServer.flush();
			log("������Ϣ������");
			account = inputFromServer.readInt();
			log("���յ�account="+account);
		} catch (IOException e) {
			account = 0;
			log("IO����"+e.getMessage());
		}
		return account;
	}
	
	/**
	 * ����ʮ������������ȡ��ָ��ͼ�����ϸ��Ϣ
	 * @param barCode
	 * @return
	 */
	public BookDetails getBookDetails(String barCode) {
		log("�ͻ��˽��յ�barCode"+barCode);
		BookDetails bookDetails = null;
		try{
			outputToServer.writeInt(OP_GET_BARCODE_BOOK_DETAILS);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			log("������Ϣ������");
			bookDetails =(BookDetails) inputFromServer.readObject();
			log("ȡ��ͼ��ı�ţ�"+bookDetails.getIsbn());		
		}catch(ClassNotFoundException e){
			log("�ļ�û���ҵ�"+e.getMessage());
		}catch(IOException e){
			log("IO�쳣"+e.getMessage());
		}
		return bookDetails;
	}
	
	/**
	 * ����ʮһ�����߻��߹���Ա�޸��Լ�������
	 * @param flag
	 * @param id
	 * @param rePass
	 * @return
	 */
	public boolean modifyPassword(String flag, String id, String rePass) {
		log("���յ���־��"+flag+"\t��ţ�"+id+"\t���룺"+rePass);
		boolean modify = false;
		if("reader"==flag.trim()){
			log("���յ���ʹreader");
			//�����޸��Լ�������
			try{
				outputToServer.writeInt(READER_MODIFY_PASSWORD);
				outputToServer.writeObject(id);
				outputToServer.writeObject(rePass);
				outputToServer.flush();
				modify = inputFromServer.readBoolean();
				log("�޸� modify = :"+modify);
			}catch(IOException e){
				log("�޸Ķ�������IO�쳣"+e.getMessage());
			}
		}
		else if("libraian"==flag.trim()){
			log("����Ա������");
		}
		return modify;
	}
	
	/**
	 *  ����ʮ��:���ָ������������Ƿ����
	 * @param barCode
	 * @return
	 */
	public boolean checkExists(String barCode) {
		boolean mark = false;
		log("���յ������룺"+barCode);
			try{
				outputToServer.writeInt(BARCODE_BOOK_EXISTS);
				outputToServer.writeObject(barCode.trim());
				outputToServer.flush();
				mark = inputFromServer.readBoolean();
				log(" mark = :"+mark);
			}catch(IOException e){
				log("�޸Ķ�������IO�쳣"+e.getMessage());
			}
		return mark;
	}
	
	/**
	 *  ����ʮ��:���ָ������������Ƿ�ɽ�
	 * @param barCode
	 * @return
	 */
	public boolean checkCanBorrow(String barCode) {
		boolean mark = false;
		log("���յ�barCode "+barCode);
		try{
			outputToServer.writeInt(BARCODE_BOOK_BORROW);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			mark = inputFromServer.readBoolean();
			log("�޸� mark = :"+mark);
		}catch(IOException e){
			mark = false;
			log("ָ������ͼ���Ƿ�ɽ�IO�쳣"+e.getMessage());
		}
		return mark;
	}
	
	/**
	 * ����ʮ�ģ�����
	 * @param readerid
	 * @param keyWord
	 * @return
	 */
	public boolean getLendBookInfo(String readerid,String barCode) {
		boolean mark = false;
		log("��ʼ���飺"+readerid+"barcode :"+barCode);
		try {
			outputToServer.writeInt(LibProtocals.OP_BORROW_BOOK);
			outputToServer.writeObject(readerid);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			mark  = inputFromServer.readBoolean();
		} catch (IOException e) {
			mark = false;
			log("����ʧ�ܣ�io����"+e.getMessage());
		}
		return mark;
	}
	
	/**
	 * ����ʮ�壺����
	 */
	public BorrowInfo returnBook(String barCode){

		BorrowInfo borrowInfo = null;
		try {
			log("��������: OP_RETURN_BOOK�� ������  = " + barCode);
			outputToServer.writeInt(LibProtocals.OP_RETURN_BOOK);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			log("��������...");
			borrowInfo = (BorrowInfo) inputFromServer.readObject();
			log("�յ�" + borrowInfo.getBarCode() + "����Ϣ");
		} catch (ClassNotFoundException exc) {
			log("ȡ������Ϣ�쳣: " + exc);
		}catch (IOException exc) {
			log("ȡ��������ȡָ������ͼ������ߵ���Ϣ�쳣: " + exc);
		}
		return borrowInfo;
	}

	/**
	 * ����ʮ����ָ�������ͼ�鱻�ĸ����߽���
	 */
	public ReaderInfo getBarReader(String barCode) {
		ReaderInfo readerInfo = null;
		try {
			log("��������ȡָ������ͼ������ߵ���Ϣ: �� ������  = " + barCode);
			outputToServer.writeInt(LibProtocals.BARCODE_BOOK_BORROW_READER);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			log("��������...");
			readerInfo = (ReaderInfo) inputFromServer.readObject();
			log("�յ�" + readerInfo.getName());			
		} catch (IOException exc) {
			log("ȡ��������ȡָ������ͼ������ߵ���Ϣ�쳣: " + exc);
		}catch (ClassNotFoundException exc) {
			log("ȡ��������ȡָ������ͼ������ߵ���Ϣ�쳣: " + exc);
		}
		return readerInfo;
	}
	
	/**
	 * ����ʮ�ߣ����ָ�������ͼ���Ƿ����ڹݲ���
	 */
	public boolean checkIsBorrowed(String barCode) {
		boolean mark = false;
		try{
			log("���ָ�������ͼ���Ƿ����ڹݲ���:  ������  = " + barCode);
			outputToServer.writeInt(LibProtocals.BARCODE_BOOK_IN_LIB);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			log("��������...");
			mark =  inputFromServer.readBoolean();
			log("�յ�" + mark);	
		} catch (IOException exc) {
			log("ȡ��������ȡָ������ͼ������ߵ���Ϣ�쳣: " + exc.getMessage());
		}
		return mark;
	}
	
	/**
	 * ����ʮ�ˣ����ָ�������ͼ���Ƿ������
	 */
	public int bookIsRenewed(String barCode) {
		int renew = 0;
		log("���ָ��ͼ���Ƿ��������");
		try{
			outputToServer.writeInt(LibProtocals.BARCODE_BOOK_BE_BORROWED);
			outputToServer.writeObject(barCode);
			outputToServer.flush();
			log("��������...");
			renew =  inputFromServer.readInt();
			log("�յ�" + renew);	
		}catch (IOException exc) {
			log("���ָ������ͼ���Ƿ�����: " + exc.getMessage());
		}
		return renew;
	}
/**
 * ����ʮ�ţ�����ͼ��
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
			log("��������...");
			mark =  inputFromServer.readBoolean();
			log("�յ�" + mark);	
		}catch (IOException exc) {
			log("���ָ������ͼ���Ƿ�����: " + exc.getMessage());
		}
		return mark;
	}
	
	/**
	 * ������ʮ��ͼ��ͳ��
	 * 
	 */
	public List<Integer> getCountList(){
		List<Integer> countList = null;
		log("ͼ��ͳ��");
		try{
			outputToServer.writeInt(LibProtocals.GET_COUNT_INFO);
			outputToServer.flush();
			log("��������...");
			countList = (List) inputFromServer.readObject();
			log("�յ�" + countList.size()+"����¼");	
		}catch (IOException exc) {
			log("���ָ������ͼ���Ƿ�����: " + exc.getMessage());
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return countList;
	}
	
	/**
	 * ������ʮһ������ͼ��
	 * @return
	 */
	public List<String> getOverDuBooks() {
		List<String> bList = null;
		log("����ͼ��");
		try{
			outputToServer.writeInt(LibProtocals.OP_GET_OVER_DUE_BOOKS);
			outputToServer.flush();
			log("��������...");
			bList = (List) inputFromServer.readObject();
			log("�յ�" + bList.size()+"����¼");	
		}catch (IOException exc) {
			log("���ָ������ͼ���Ƿ�����: " + exc.getMessage());
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return bList;
	}
	
	/**
	 * ������ʮ����ͼ�����
	 */

	public boolean outLib(String isbn) {
		boolean mark = false;
		log("ͼ�����");
		try{
			outputToServer.writeInt(LibProtocals.BOOK_OUT_LIB);
			outputToServer.writeObject(isbn);
			outputToServer.flush();
			log("��������...");
			mark = inputFromServer.readBoolean();
			log("�յ�" + mark);	
		}catch (IOException exc) {
			log("ͼ�����: " + exc.getMessage());
		}
		return mark;
	}
	/**
	 * ������ʮ������isbn��ѯͼ��
	 * @param isbn
	 * @return
	 */
	public BookDetails getBookDetailsByIsbn(String isbn) {
		BookDetails bookDetails = null;
		try{
			outputToServer.writeInt(LibProtocals.OP_GET_ISBN_BOOK_DETAILS);
			outputToServer.writeObject(isbn);
			outputToServer.flush();
			log("��������...");
			bookDetails = (BookDetails)inputFromServer.readObject();
			log("�յ�" + bookDetails.getName());	
		}catch (IOException exc) {
			log("ͼ����ϸ��Ϣ: " + exc.getMessage());
		}catch (ClassNotFoundException e) {
			log("��ÿ�ҵ�:"+e.getMessage());
		}
		return bookDetails;
	}
	/**
	 *������ʮ�ģ� ɾ��ָ����ŵĶ���READER_OUT_LIB
	 */
	public boolean delReader(String readerid) {
		boolean mark = false;
		log("����ɾ��");
		try{
			outputToServer.writeInt(LibProtocals.READER_OUT_LIB);
			outputToServer.writeObject(readerid);
			outputToServer.flush();
			log("��������...");
			mark = inputFromServer.readBoolean();
			log("�յ�" + mark);	
		}catch (IOException exc) {
			log("����ɾ��: " + exc.getMessage());
		}
		return mark;
	}
	/**
	 * ��־����.
	 */
	protected void log(Object msg) {
		System.out.println(CurrDateTime.currDateTime() + "LibClient��: " + msg);
	}
	/**
	 * ��ӹ���Ա
	 */
	public boolean addMaster(String userid, String password, String name,
			int state1, int state2, int state3) throws IOException {

		boolean b = false;
		log("��������: OP_LIBRAIAN_ADDMASTER,��ӹ���Ա");
		outputToServer.writeInt(LibProtocals.OP_LIBRAIAN_ADDMASTER);
		outputToServer.writeObject(userid);
		outputToServer.writeObject(password);
		outputToServer.writeObject(name);
		outputToServer.writeInt(state1);
		outputToServer.writeInt(state2);
		outputToServer.writeInt(state3);
		outputToServer.flush();

		log("��������...");
		b = inputFromServer.readBoolean();
		log("�ɹ�������ӹ���Ա��Ϣ");
		return b;
	}

	/**
	 * ȡ�ù���Ա���
	 */
	public ArrayList getManagerList() throws IOException {

		ArrayList managerList = null;
		try {
			log("��������: OP_LIBRAIAN_GETMASTER,ȡ�ù���Ա�б�");
			outputToServer.writeInt(LibProtocals.OP_LIBRAIAN_GETMASTER);
			outputToServer.flush();

			log("��������...");
			managerList = (ArrayList) inputFromServer.readObject();
			log("�ɹ�������ӹ���Ա��Ϣ");
		} catch (ClassNotFoundException exc) {
			log("ȡ������Ϣ�쳣: " + exc);
			throw new IOException("�Ҳ��������");
		}
		return managerList;
	}

	/**
	 * �޸Ĺ���Ա��Ϣ
	 */
	public boolean modifyMaster(String userid, String password, String name,
			int state1, int state2, int state3) throws IOException {

		boolean b = false;

		log("��������: OP_LIBRAIAN_MODIFYMASTER,�޸Ĺ���Ա��Ϣ");
		outputToServer.writeInt(LibProtocals.OP_LIBRAIAN_MODIFYMASTER);
		outputToServer.writeObject(userid);
		outputToServer.writeObject(password);
		outputToServer.writeObject(name);
		outputToServer.writeInt(state1);
		outputToServer.writeInt(state2);
		outputToServer.writeInt(state3);
		outputToServer.flush();

		log("��������...");
		b = inputFromServer.readBoolean();
		log("�ɹ������޸Ĺ���Ա��Ϣ");
		return b;
	}

	/**
	 * ɾ������Ա��Ϣ
	 */
	public boolean dellMaster(String userid) throws IOException {

		boolean b = false;

		log("��������: OP_LIBRAIAN_DELLMASTER,ɾ������Ա��Ϣ");
		outputToServer.writeInt(LibProtocals.OP_LIBRAIAN_DELLMASTER);
		outputToServer.writeObject(userid);
		outputToServer.flush();

		log("��������...");
		b = inputFromServer.readBoolean();
		log("�ɹ�����ɾ������Ա��Ϣ");
		return b;
	}
	/**
	 * ���²�����Ϣ
	 */
	public boolean updateParameter(int type, int amount, int period,
			double dailyfine) throws IOException {
		boolean b = false;
		log("��������: OP_PARAMETER_UPDATE�� �������  = " + type);
		outputToServer.writeInt(LibProtocals.OP_PARAMETER_UPDATE);
		outputToServer.writeInt(type);
		outputToServer.writeInt(amount);
		outputToServer.writeInt(period);
		outputToServer.writeDouble(dailyfine);
		outputToServer.flush();

		log("��������...");
		b = (boolean) inputFromServer.readBoolean();
		log("�յ� ����Ա��Ϣ��¼");
		return b;
	}
	

	/**
	 * ��ò�����Ϣ
	 * 
	 * @param parameterID
	 * @return
	 * @throws IOException
	 */
	public ArrayList getParameterInfo(String parameterID) {

		ArrayList parameterInfoList = null;

		try {
			log("��������: OP_PARAMETER_MAINTAIN");
			outputToServer.writeInt(LibProtocals.OP_PARAMETER_MAINTAIN);
			outputToServer.writeObject(parameterID);
			// outputToServer.writeObject(keyword);
			outputToServer.flush();

			log("��������...");
			parameterInfoList = (ArrayList) inputFromServer.readObject();
			log("�յ�  " + parameterInfoList.size() + " ���������Ϣ.");
		} catch (IOException e) {
			log("IO�쳣: " + e.getMessage());
		} catch (ClassNotFoundException exc) {
			log("ȡ�����쳣: " + exc.getMessage());
		}
		return parameterInfoList;

	}

	
}
