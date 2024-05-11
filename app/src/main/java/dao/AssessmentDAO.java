package dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entities.Assessment;

public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM assessments ORDER BY assessmentID ASC") // Adjust table name if different
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM assessments WHERE courseID = :courseID ORDER BY assessmentID ASC") // Filter by course
    List<Assessment> getAssociatedAssessments(int courseID);
}

6