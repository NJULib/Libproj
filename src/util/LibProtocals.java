package util;

/**
 * 图书存取操作协议
 */
public interface LibProtocals {
	/**
	 * 取得书籍的详细信息
	 */
	public static final int OP_GET_BOOK_DETAILS = 100;
	/**
	 * 取得书籍的馆藏信息
	 */
	public static final int OP_GET_BOOK_LIBINFO = 101;
	/**
	 * 取得用户的借阅信息
	 */
	public static final int OP_GET_BORROWINFO = 102;
	/**
	 * 读取读者的个人详细信息
	 */
	public static final int OP_GET_READERINFO = 103;
	/**
	 * 读取管理员的个人详细信息
	 */
	public static final int OP_GET_LIBRAIANINFO = 104;
	/**
	 * 借书
	 */
	public static final int OP_BORROW_BOOK = 105;
	/**
	 * 还书
	 */
	public static final int OP_RETURN_BOOK = 106;
	/**
	 * 图书维护
	 */
	public static final int OP_BOOK_MAINTAIN = 107;
	/**
	 * 读者维护
	 */
	public static final int OP_READER_MAINTAIN = 108;
	/**
	 * 管理员维护
	 */
	public static final int OP_LIBRAIAN_MAINTAIN = 109;
	/**
	 * 系统参数维护
	 */
	public static final int OP_PARAMETER_MAINTAIN = 110;
	/**
	 * 执行图书信息的修改
	 */
	public static final int  OP_EXEC_BOOK_MODIFY = 111;
	/**
	 * 执行 读者信息的修改
	 */
	public static final int  OP_EXEC_READER_MODIFY = 112;
	/**
	 * 执行 管理员信息的修改
	 */
	public static final int  OP_EXEC_LIBRARIAN_MODIFY = 113;
	/**
	 * 执行 参数信息的修改
	 */
	public static final int  OP_EXEC_PARAMETER_MODIFY = 114;
	/**
	 * 超期馆藏图书信息
	 */
	public static final int  OP_GET_OVER_DUE_BOOKS = 118;
	/**
	 * 入库馆藏信息
	 */
	public static final int  OP_EXEC_BOOK_IN_LIB_INSERT = 119;
	/**
	 * 添加图书信息
	 */
	public static final int  OP_EXEC_BOOK_INSERT = 120;
	/**
	 * 添加读者信息
	 */
	public static final int  OP_EXEC_READER_INSERT = 121;
	/**
	 * 取得指定类型读者可以借阅图书的最大数量
	 */
	public static final int  OP_GET_PARAM_ACCOUNT = 130;
	
	/**
	 * 取得指定条码的书籍的详细信息
	 */
	public static final int  OP_GET_BARCODE_BOOK_DETAILS = 131;
	/**
	 * 取得指定ISBN的书籍的详细信息
	 */
	public static final int  OP_GET_ISBN_BOOK_DETAILS = 132;
	
	
	
	/**
	 * 用户修改密码
	 */
	public static final int  READER_MODIFY_PASSWORD = 141;
	/**
	 * 管理员修改密码
	 */
	public static final int  LIBRARAIAN_MODIFY_PASSWORD = 142;
	/**
	 * 检测指定条码的图书是否存在
	 */
	public static final int  BARCODE_BOOK_EXISTS = 143;
	/**
	 * 检测指定条码的图书是否可借
	 */
	public static final int  BARCODE_BOOK_BORROW = 144;
	/**
	 * 取得借阅指定条码图书的读者的基本信息
	 */
	public static final int  BARCODE_BOOK_BORROW_READER = 145;
	/**
	 * 取得借阅指定条码图书是否仍在馆藏中
	 */
	public static final int  BARCODE_BOOK_IN_LIB = 146;
	/**
	 * 检测指定条码的图书是否被续借过
	 */
	public static final int  BARCODE_BOOK_BE_BORROWED = 147;
	/**
	 * 图书续借
	 */
	public static final int  RENEW_BOOK = 148;
	/**
	 * 图书入库时，检测指定的条码是否已存在
	 */
	public static final int  BARCODE_HAVE_BEEN_EXIST = 149;
	/**
	 * 图书的统计信息
	 */
	public static final int GET_COUNT_INFO = 150;
	/**
	 * 图书出库
	 */
	public static final int BOOK_OUT_LIB = 151;
	/**删除读者信息
	 * 
	 */
	public static final int READER_OUT_LIB = 152;
	/**
	 * 添加管理员
	 */
	public static final int OP_LIBRAIAN_ADDMASTER = 201;
	/**
	 * 取得管理员列表
	 */
	public static final int OP_LIBRAIAN_GETMASTER = 202;
	/**
	 * 修改管理员信息
	 */
	public static final int OP_LIBRAIAN_MODIFYMASTER = 203;
	/**
	 * 删除管理员信息
	 */
	public static final int OP_LIBRAIAN_DELLMASTER = 204;
	/**
	 * 更新参数
	 */
	public static final int OP_PARAMETER_UPDATE = 301;
	
}
