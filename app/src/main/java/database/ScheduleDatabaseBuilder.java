package database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dao.CourseDAO;
import dao.TermDAO;
import entities.Course;
import entities.Term;

@Database(entities = {Term.class, Course.class}, version = 4, exportSchema = false) //Needs update everytime changes made to db
public abstract class ScheduleDatabaseBuilder extends RoomDatabase {
    public abstract TermDAO termDAO(); //Term dao
    public abstract CourseDAO courseDAO(); //Course dao
    private static volatile ScheduleDatabaseBuilder INSTANCE;

    static ScheduleDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (ScheduleDatabaseBuilder.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),ScheduleDatabaseBuilder.class,"MyScheduleDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
