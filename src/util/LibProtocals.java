package util;

/**
 * ͼ���ȡ����Э��
 */
public interface LibProtocals {
	/**
	 * ȡ���鼮����ϸ��Ϣ
	 */
	public static final int OP_GET_BOOK_DETAILS = 100;
	/**
	 * ȡ���鼮�Ĺݲ���Ϣ
	 */
	public static final int OP_GET_BOOK_LIBINFO = 101;
	/**
	 * ȡ���û��Ľ�����Ϣ
	 */
	public static final int OP_GET_BORROWINFO = 102;
	/**
	 * ��ȡ���ߵĸ�����ϸ��Ϣ
	 */
	public static final int OP_GET_READERINFO = 103;
	/**
	 * ��ȡ����Ա�ĸ�����ϸ��Ϣ
	 */
	public static final int OP_GET_LIBRAIANINFO = 104;
	/**
	 * ����
	 */
	public static final int OP_BORROW_BOOK = 105;
	/**
	 * ����
	 */
	public static final int OP_RETURN_BOOK = 106;
	/**
	 * ͼ��ά��
	 */
	public static final int OP_BOOK_MAINTAIN = 107;
	/**
	 * ����ά��
	 */
	public static final int OP_READER_MAINTAIN = 108;
	/**
	 * ����Աά��
	 */
	public static final int OP_LIBRAIAN_MAINTAIN = 109;
	/**
	 * ϵͳ����ά��
	 */
	public static final int OP_PARAMETER_MAINTAIN = 110;
	/**
	 * ִ��ͼ����Ϣ���޸�
	 */
	public static final int  OP_EXEC_BOOK_MODIFY = 111;
	/**
	 * ִ�� ������Ϣ���޸�
	 */
	public static final int  OP_EXEC_READER_MODIFY = 112;
	/**
	 * ִ�� ����Ա��Ϣ���޸�
	 */
	public static final int  OP_EXEC_LIBRARIAN_MODIFY = 113;
	/**
	 * ִ�� ������Ϣ���޸�
	 */
	public static final int  OP_EXEC_PARAMETER_MODIFY = 114;
	/**
	 * ���ڹݲ�ͼ����Ϣ
	 */
	public static final int  OP_GET_OVER_DUE_BOOKS = 118;
	/**
	 * ���ݲ���Ϣ
	 */
	public static final int  OP_EXEC_BOOK_IN_LIB_INSERT = 119;
	/**
	 * ���ͼ����Ϣ
	 */
	public static final int  OP_EXEC_BOOK_INSERT = 120;
	/**
	 * ��Ӷ�����Ϣ
	 */
	public static final int  OP_EXEC_READER_INSERT = 121;
	/**
	 * ȡ��ָ�����Ͷ��߿��Խ���ͼ����������
	 */
	public static final int  OP_GET_PARAM_ACCOUNT = 130;
	
	/**
	 * ȡ��ָ��������鼮����ϸ��Ϣ
	 */
	public static final int  OP_GET_BARCODE_BOOK_DETAILS = 131;
	/**
	 * ȡ��ָ��ISBN���鼮����ϸ��Ϣ
	 */
	public static final int  OP_GET_ISBN_BOOK_DETAILS = 132;
	
	
	
	/**
	 * �û��޸�����
	 */
	public static final int  READER_MODIFY_PASSWORD = 141;
	/**
	 * ����Ա�޸�����
	 */
	public static final int  LIBRARAIAN_MODIFY_PASSWORD = 142;
	/**
	 * ���ָ�������ͼ���Ƿ����
	 */
	public static final int  BARCODE_BOOK_EXISTS = 143;
	/**
	 * ���ָ�������ͼ���Ƿ�ɽ�
	 */
	public static final int  BARCODE_BOOK_BORROW = 144;
	/**
	 * ȡ�ý���ָ������ͼ��Ķ��ߵĻ�����Ϣ
	 */
	public static final int  BARCODE_BOOK_BORROW_READER = 145;
	/**
	 * ȡ�ý���ָ������ͼ���Ƿ����ڹݲ���
	 */
	public static final int  BARCODE_BOOK_IN_LIB = 146;
	/**
	 * ���ָ�������ͼ���Ƿ������
	 */
	public static final int  BARCODE_BOOK_BE_BORROWED = 147;
	/**
	 * ͼ������
	 */
	public static final int  RENEW_BOOK = 148;
	/**
	 * ͼ�����ʱ�����ָ���������Ƿ��Ѵ���
	 */
	public static final int  BARCODE_HAVE_BEEN_EXIST = 149;
	/**
	 * ͼ���ͳ����Ϣ
	 */
	public static final int GET_COUNT_INFO = 150;
	/**
	 * ͼ�����
	 */
	public static final int BOOK_OUT_LIB = 151;
	/**ɾ��������Ϣ
	 * 
	 */
	public static final int READER_OUT_LIB = 152;
	/**
	 * ��ӹ���Ա
	 */
	public static final int OP_LIBRAIAN_ADDMASTER = 201;
	/**
	 * ȡ�ù���Ա�б�
	 */
	public static final int OP_LIBRAIAN_GETMASTER = 202;
	/**
	 * �޸Ĺ���Ա��Ϣ
	 */
	public static final int OP_LIBRAIAN_MODIFYMASTER = 203;
	/**
	 * ɾ������Ա��Ϣ
	 */
	public static final int OP_LIBRAIAN_DELLMASTER = 204;
	/**
	 * ���²���
	 */
	public static final int OP_PARAMETER_UPDATE = 301;
	
}
