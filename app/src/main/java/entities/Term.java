package entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//New Terms entity table
@Entity(tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int termID;
    private String termName;

    public Term(int termID, String termName) {
        this.termID = termID;
        this.termName = termName;
    }

    public int getTermID() {
        return termID;
    }

    public String toString(){
        return termName;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }
}
