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

    public String getAssessmentType() {
        return assessmentType;
    }

    //CONSTRUCTOR
    public Assessment(int assessmentId, String assessmentName, String assessmentType) {
        this.assessmentId=assessmentId;
        this.assessmentName=assessmentName;
        this.assessmentType=assessmentType;
    }
    //GETTERS
    public int getAssessmentId() {
        return assessmentId;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

}
