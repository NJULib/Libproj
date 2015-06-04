package serverside.entity;

import java.util.Date;

/**
 * 读者借阅信息，包括读者所借过书的书名、作者、出版社、借书时间、还书时间、 应还时间、超期天数、罚款金额等
 */
public class BorrowInfo implements java.io.Serializable {

	private static final long serialVersionUID = 8729453305993405592L;
	protected String readerId;			//借阅人编号	
	protected String barCode;			//条形码
	protected Date borrowDate;			//借书日期
	protected Date dueDate;				//应还日期
	protected String returnDate;			//还书日期
	protected Integer renew;			//是否续借
	protected int overduedays;			//超期天数
	protected double finedMoney;		//罚金

	public BorrowInfo() {
	}

	public BorrowInfo(String readerId,String barCode,Date borrowDate, Date dueDate, String  returnDate,Integer renew, int overduedays,
			double finedMoney) {
		this.readerId = readerId;
		this.barCode = barCode;		
		this.borrowDate = borrowDate;
		this.dueDate = dueDate;
		this.returnDate = returnDate;
		this.renew = renew;
		this.overduedays = overduedays;
		this.finedMoney = finedMoney;
	}
	public BorrowInfo(String readerid, String bookcode, Date borrowDate,
			Date dueDate, String  returnDate, int renew,int overdays) {
		this.readerId = readerid;
		this.barCode = bookcode;
		this.borrowDate = borrowDate;
		this.dueDate = dueDate;
		this.returnDate = returnDate;
		this.renew = renew;
		this.overduedays = overdays;
	}

	public String getReaderId() {
		return readerId;
	}

	public void setReaderId(String readerId) {
		this.readerId = readerId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Date getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String  getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String  returnDate) {
		this.returnDate = returnDate;
	}

	public Integer getRenew() {
		return renew;
	}

	public void setRenew(Integer renew) {
		this.renew = renew;
	}

	public int getOverduedays() {
		return overduedays;
	}

	public void setOverduedays(int overduedays) {
		this.overduedays = overduedays;
	}

	public double getFinedMoney() {
		return finedMoney;
	}

	public void setFinedMoney(double finedMoney) {
		this.finedMoney = finedMoney;
	}

}
