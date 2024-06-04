package database;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dao.AssessmentDAO;
import dao.CourseDAO;
import dao.TermDAO;
import entities.Assessment;
import entities.Course;
import entities.Term;

public class Repository {
    private CourseDAO mCourseDAO;
    private TermDAO mTermDAO;
    private AssessmentDAO mAssessmentDAO;

    private List<Term> mAllTerms;
    private List<Course> mAllCourses;
    private List<Assessment> mAllAssessments;

    private static int NUMBER_OF_THREADS=4;
    public static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public Repository(Application application){
        ScheduleDatabaseBuilder db=ScheduleDatabaseBuilder.getDatabase(application);
        mCourseDAO=db.courseDAO();
        mTermDAO=db.termDAO();
        mAssessmentDAO=db.assessmentDAO();
    }

    //TERMS
    public List<Term>getmAllTerms(){
     databaseExecutor.execute(()->{
         mAllTerms=mTermDAO.getAllTerms();
     });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllTerms;
    }

    public void insert(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.insert(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.update(term);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Term term){
        databaseExecutor.execute(()->{
            mTermDAO.delete(term);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //COURSE
    public Course getCourseById(int courseId) {
        for (Course course : getmAllCourses()) {
            if (course.getCourseID() == courseId) {
                return course;
            }
        }
        return null;
    }
    public List<Course>getmAllCourses(){
        databaseExecutor.execute(()->{
            mAllCourses=mCourseDAO.getAllCourses();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllCourses;
    }

    public List<Course>getAssociatedCourses(int termID){
        databaseExecutor.execute(()->{
            mAllCourses=mCourseDAO.getAssociatedCourses(termID);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllCourses;
    }
    public void insert(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.insert(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.update(course);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Course course){
        databaseExecutor.execute(()->{
            mCourseDAO.delete(course);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //ASSESSMENTS
    public void insert(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Assessment>getmAllAssessments(){
        databaseExecutor.execute(()->{
            mAllAssessments=mAssessmentDAO.getAllAssessments();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllAssessments;
    }

    public void update(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDAO.update(assessment);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Assessment> getmAssociatedAssessments(int courseID) {
        databaseExecutor.execute(() -> mAllAssessments = mAssessmentDAO.getAssessmentsForCourse(courseID));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllAssessments;
    }

    public void delete(Assessment assessment) {
        databaseExecutor.execute(() -> {
            mAssessmentDAO.delete(assessment);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
