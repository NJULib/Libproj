package serverside.entity;

import java.util.Date;

/**
 * 每中本书在图书馆都有一个或多个副本。 本类描述每种书在图书馆的馆藏情况， 如书的条码、是否在馆、馆藏地、 应还日期（如果该副本被读者借去）等。 *
 */
public class BookInLibrary implements java.io.Serializable {

	private static final long serialVersionUID = 7386444304600938175L;

	protected String barCode; // 图书条形码
	protected String isbn;	//图示isbn号
	protected int status; // 是否在馆,1：在，0：不在
	protected String location; // 馆藏位置
	protected String dueReturnDate; // 应还日期
	protected String introducetime; // 入库时间

	public BookInLibrary() {
	}

	public BookInLibrary(String barCode,String isbn, int status, String location,
			String dueReturnDate, String introducetime) {

		this.barCode = barCode;
		this.isbn = isbn;
		this.status = status;
		this.location = location;
		this.dueReturnDate = dueReturnDate;
		this.introducetime = introducetime;
	}

	public BookInLibrary(String barcodeStr, String isbn, String location) {
		this.barCode = barcodeStr;
		this.isbn = isbn;
		this.location = location;
	}

	public BookInLibrary(String barCode, int status, String location,
			String dueReturnDate) {
		this.barCode = barCode;
		this.status = status;
		this.location = location;
		this.dueReturnDate = dueReturnDate;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String  getDueReturnDate() {
		return dueReturnDate;
	}

	public void setDueReturnDate(String  dueReturnDate) {
		this.dueReturnDate = dueReturnDate;
	}

	public String  getIntroducetime() {
		return introducetime;
	}

	public void setIntroducetime(String  introducetime) {
		this.introducetime = introducetime;
	}

//	public String toString() {
//		return barCode + "  -  " + status + "  -  " + location + dueReturnDate;
//	}
}
