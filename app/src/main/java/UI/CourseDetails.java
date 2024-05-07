package UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tracker.R;

import database.Repository;
import entities.Course;
import entities.Term;

public class CourseDetails extends AppCompatActivity {
    String name;
    int courseID;
    int termID;
    EditText editName;
    EditText editNote;

    //custom adds
    String instructor;
    EditText editInstructor;

    TextView editDate;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details);

        repository=new Repository(getApplication());

        //Name setup // do the same for other parameters
        name= getIntent().getStringExtra("name");
        editName=findViewById(R.id.coursename);
        editName.setText(name);

        //Instructor
        instructor= getIntent().getStringExtra("instructor");
        editInstructor=findViewById(R.id.instructorename);
        editInstructor.setText(instructor);

        //Course and Term setup
        courseID = getIntent().getIntExtra("id,", -1);
        termID = getIntent().getIntExtra("termID",-1);

        editNote=findViewById(R.id.note);
       // editDate=findViewById(R.id.date);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_coursedetails,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }

        if(item.getItemId()==R.id.coursesave){
            Course course;
            if (courseID==-1){
                if(repository.getmAllCourses().isEmpty())
                    courseID =1;
                else
                    courseID=repository.getmAllCourses().get(repository.getmAllCourses().size() - 1).getCourseID() +1;

                course = new Course(courseID,editName.getText().toString(),editInstructor.getText().toString(),termID);
                repository.insert(course);
                this.finish();
            }
            else {
                course = new Course(courseID,editName.getText().toString(),editInstructor.getText().toString(),termID);
                repository.update(course);
                this.finish();
            }
            return true;
        }

        if(item.getItemId()== R.id.sharenote){
            Intent sentIntent= new Intent();

            sentIntent.setAction(Intent.ACTION_SEND );
            sentIntent.putExtra(Intent.EXTRA_TEXT,editNote.getText().toString()+"EXTRA_TEXT");
            sentIntent.putExtra(Intent.EXTRA_TITLE,editNote.getText().toString()+ "EXTRA_TITLE");
            sentIntent.setType("text/plain");

            Intent shareIntent=Intent.createChooser(sentIntent,null);

            startActivity(shareIntent);
            return true;
        }
        if (item.getItemId()==R.id.notify){
            return true;
        };

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onResume() {
        super.onResume();

        //termID = getIntent().getIntExtra("termID", this.termID);

//I cant get the term to not be -1 when I update a part

    }

}