package serverside.entity;

/**
 * ����ͼ����ϸ��Ϣ����ͼ��������������硢���ߡ��������������顢���ۡ�ҳ��������ժҪ�ȡ�
 */
public class BookDetails implements java.io.Serializable {
	
	private static final long serialVersionUID = -8792355134417983448L;
	
	private String isbn; // ISBN��
	private String name; // ����
	private String series; // ������
	private String authors; // ����
	private String publisher; // ������Ϣ,��������ص㡢������������������
	private String size; // ͼ�鿪��
	private int pages; // ҳ��
	private double price; // ����
	private String introduction; // ͼ����
	private String picture; // ����ͼƬ
	private String clnum; // ͼ������

	public BookDetails() {
	}

	public BookDetails(String isbn, String name, String series, String authors,
			String publisher, String size, int pages, double price,
			String introduction, String picture, String clnum) {
		super();
		this.isbn = isbn;
		this.name = name;
		this.series = series;
		this.authors = authors;
		this.publisher = publisher;
		this.size = size;
		this.pages = pages;
		this.price = price;
		this.introduction = introduction;
		this.picture = picture;
		this.clnum = clnum;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getClnum() {
		return clnum;
	}

	public void setClnum(String clnum) {
		this.clnum = clnum;
	}

	public String toString() {
		return name + "  -  " + authors + "  -  " + publisher;
	}

	public int compareTo(Object object) {
		BookDetails book = (BookDetails) object;
		String targetBook = book.getName();
		return name.compareTo(targetBook);
	}
}
