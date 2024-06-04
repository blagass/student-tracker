package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entities.Assessment;
import entities.Course;

@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);
    @Update
    void update(Assessment assessment);
    @Delete
    void delete(Assessment assessment);
    @Query("SELECT * FROM ASSESSMENTS ORDER BY assessmentId ASC")
    List<Assessment> getAllAssessments();
    @Query("SELECT * FROM assessments WHERE assessmentCourseId = :courseId ORDER BY assessmentID ASC")
    List<Assessment> getAssessmentsForCourse(int courseId);

}
