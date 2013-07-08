package edu.taylor.cse.sbrandle.biblemem.v001.setting;

public class BibleEditionObject {
	private String bibleEditionName;
	private String bibleEditionCode;
	
	public BibleEditionObject(String bibleEditionName, String bibleEditionCode) {
		super();
		this.bibleEditionName = bibleEditionName;
		this.bibleEditionCode = bibleEditionCode;
	}
	public String getBibleEditionName() {
		return bibleEditionName;
	}
	public void setBibleEditionName(String bibleEditionName) {
		this.bibleEditionName = bibleEditionName;
	}
	public String getBibleEditionCode() {
		return bibleEditionCode;
	}
	public void setBibleEditionCode(String bibleEditionCode) {
		this.bibleEditionCode = bibleEditionCode;
	}
}
