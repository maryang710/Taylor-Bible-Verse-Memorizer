package edu.taylor.cse.sbrandle.biblemem.v001.global;

import java.util.Comparator;

import edu.taylor.cse.sbrandle.biblemem.v001.object.ProjectVerseObject;

public class ProjectVerseComparator implements Comparator<ProjectVerseObject> {

	@Override
	public int compare(ProjectVerseObject o1, ProjectVerseObject o2) {

		if(o1.getVerseId()>o2.getVerseId())
			return 1;
		else if(o1.getVerseId()<o2.getVerseId())
			return -1;
		else
			return 0;
	}
}
