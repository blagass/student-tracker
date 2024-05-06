package UI;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tracker.R;

import database.Repository;
import java.util.ArrayList;
import java.util.List;

import database.Repository;
import entities.Course;
import entities.Term;

public class CourseDetails extends AppCompatActivity {
    String name;
    int courseID;
    int termID;
    EditText editName;
    EditText editNote;
    TextView editDate;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details);

        repository=new Repository(getApplication());

        name= getIntent().getStringExtra("name");
        //editName=findViewById(R.id.course);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}