package entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments")
public class Assessment {
    //PRIVATE VARIABLES
    @PrimaryKey(autoGenerate = true)
    private final int assessmentId;
    private final String assessmentName;

    //CONSTRUCTOR
    public Assessment(int assessmentId, String assessmentName) {
        this.assessmentId=assessmentId;
        this.assessmentName=assessmentName;
    }
    //GETTERS
    public int getAssessmentId() {
        return assessmentId;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

}
