package entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments")
public class Assessment {
    //PRIVATE VARIABLES
    @PrimaryKey(autoGenerate = true)
    private final int assessmentId;
    private final String assessmentName;
    private final String assessmentType;
    private final String assessmentStartDate;
    private final String assessmentEndDate;
    private final int assessmentCourseId;

    //CONSTRUCTOR
    public Assessment(int assessmentId, String assessmentName, String assessmentType, String assessmentStartDate, String assessmentEndDate, int assessmentCourseId) {
        this.assessmentId=assessmentId;
        this.assessmentName=assessmentName;
        this.assessmentType=assessmentType;
        this.assessmentStartDate = assessmentStartDate;
        this.assessmentEndDate = assessmentEndDate;
        this.assessmentCourseId = assessmentCourseId;
    }
    public String getAssessmentType() {
        return assessmentType;
    }

    public String getAssessmentStartDate() {
        return assessmentStartDate;
    }

    public String getAssessmentEndDate() {
        return assessmentEndDate;
    }


    //GETTERS
    public int getAssessmentId() {
        return assessmentId;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public int getAssessmentCourseId() {
        return assessmentCourseId;
    }
}
