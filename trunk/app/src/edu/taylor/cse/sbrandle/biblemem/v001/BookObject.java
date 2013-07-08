package edu.taylor.cse.sbrandle.biblemem.v001;


/**
 * Book Object
 * 
 * Date : May 21, 2013
 * @author Seungmin Lee, rfrost77@gmail.com
 * @since 1.0v
 * @version "%I%, %G%"
 *
 */
public class BookObject  {

	private int bookId;
	private String name;
	private int chapterCount;

	public BookObject(int bookId, String name, int chapterCount) {
		super();
		this.bookId = bookId;
		this.name = name;
		this.chapterCount = chapterCount;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getChapterCount() {
		return chapterCount;
	}
	public void setChapterCount(int chapterCount) {
		this.chapterCount = chapterCount;
	}
}
