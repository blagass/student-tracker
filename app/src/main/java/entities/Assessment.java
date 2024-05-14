package entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments")
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    private int assessmentID;
    private String assessmentName;
    private boolean assessmentType;
    //Foreign key
    private int courseID;
    private final String start;
    private final String end;

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public boolean isAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(boolean assessmentType) {
        this.assessmentType = assessmentType;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Assessment(int assessmentID, String assessmentName, boolean assessmentType, int courseID, String start, String end) {
        this.assessmentID = assessmentID;
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.courseID = courseID;
        this.start = start;
        this.end = end;
    }
}
