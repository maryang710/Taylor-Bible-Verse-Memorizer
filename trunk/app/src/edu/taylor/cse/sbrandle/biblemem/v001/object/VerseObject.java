package edu.taylor.cse.sbrandle.biblemem.v001.object;

public class VerseObject {
	
	private int _id;
	private String vername;
	private int book;
	private int chapter;
	private int verse;
	private String contents;
	
	public VerseObject(int _id, String vername, int book, int chapter,
			int verse, String contents) {
		super();
		this._id = _id;
		this.vername = vername;
		this.book = book;
		this.chapter = chapter;
		this.verse = verse;
		this.contents = contents;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getVername() {
		return vername;
	}
	public void setVername(String vername) {
		this.vername = vername;
	}
	public int getBook() {
		return book;
	}
	public void setBook(int book) {
		this.book = book;
	}
	public int getChapter() {
		return chapter;
	}
	public void setChapter(int chapter) {
		this.chapter = chapter;
	}
	public int getVerse() {
		return verse;
	}
	public void setVerse(int verse) {
		this.verse = verse;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
}
