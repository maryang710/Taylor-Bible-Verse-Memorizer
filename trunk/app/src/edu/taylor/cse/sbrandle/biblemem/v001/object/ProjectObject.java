package edu.taylor.cse.sbrandle.biblemem.v001.object;


public class ProjectObject {
	
	private int projectId;
	private String projectName;
	private int projectPercent;
	

	public ProjectObject(int projectId, String projectName, int projectPercent) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.projectPercent = projectPercent;
	}

	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getProjectPercent() {
		return projectPercent;
	}
	public void setProjectPercent(int projectPercent) {
		this.projectPercent = projectPercent;
	}
}
