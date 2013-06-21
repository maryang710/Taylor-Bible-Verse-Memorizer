package edu.taylor.cse.sbrandle.biblemem.v001.object;

public class ProjectVerseObject {
	
	private int projectVerseId;
	private int projectId;
	private int verseId;
	private int percent;
	
	public ProjectVerseObject(int projectVerseId, int projectId, int verseId,
			int percent) {
		super();
		this.projectVerseId = projectVerseId;
		this.projectId = projectId;
		this.verseId = verseId;
		this.percent = percent;
	}
	public int getProjectVerseId() {
		return projectVerseId;
	}
	public void setProjectVerseId(int projectVerseId) {
		this.projectVerseId = projectVerseId;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public int getVerseId() {
		return verseId;
	}
	public void setVerseId(int verseId) {
		this.verseId = verseId;
	}
	public int getPercent() {
		return percent;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
}
