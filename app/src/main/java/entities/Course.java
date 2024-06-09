package entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    private final String note;
    @PrimaryKey(autoGenerate = true)
    private int courseID;
    private String courseName;
    private String instructorName;
    private int termID;
    private String email;
    private String selectedStatus;
    private String startDate;
    private String endDate;
    private String phone;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Course(int courseID, String courseName, String instructorName, int termID, String selectedStatus, String startDate, String endDate, String note, String phone, String email) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.termID = termID;
        this.selectedStatus=selectedStatus;
        this.startDate=startDate;
        this.endDate=endDate;
        this.note=note;
        this.phone=phone;
        this.email=email;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }
    public String getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(String selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

}
