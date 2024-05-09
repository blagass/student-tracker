package UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import database.Repository;
import entities.Course;
import entities.Term;

public class TermDetails extends AppCompatActivity {
    String name;
    int termId;
    EditText editName;

    Repository repository;
    Term currentTerm;
    int numCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_term_details);

        FloatingActionButton fab = findViewById(R.id.addTermDetails);

        editName=findViewById(R.id.titletext);
        termId = getIntent().getIntExtra("id",-1);
        name =getIntent().getStringExtra("name");
        editName.setText(name);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(termId > 0) {
                    Intent intent = new Intent(TermDetails.this, CourseDetails.class);
                    intent.putExtra("termID", termId);
                    startActivity(intent);
                }else{
                    Toast.makeText(TermDetails.this, "Please save the term before adding a course.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView recyclerView = findViewById(R.id.partrecyclerview);
        repository = new Repository(getApplication());
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Course> filteredCourse = new ArrayList<>();
        for(Course course:repository.getmAllCourses()){
            if(course.getTermID()==termId)filteredCourse.add(course);
        }
        courseAdapter.setCourses(filteredCourse);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_termdetails,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.termsave){

            Term term;
            if (termId==-1){
                if(repository.getmAllTerms().size()==0) termId =1;
                else termId=repository.getmAllTerms().get(repository.getmAllTerms().size() - 1).getTermID() +1;
                term = new Term(termId,editName.getText().toString());
                repository.insert(term);
                this.finish();
            }
            else {
                term = new Term(termId,editName.getText().toString());
                repository.update(term);
                this.finish();
            }
        }
        if(item.getItemId()==R.id.termdelete){
            for(Term term: repository.getmAllTerms()){
                if(term.getTermID()==termId)currentTerm=term;
            }
            numCourses=0;
            for(Course course: repository.getmAllCourses()){
                if(course.getTermID()==termId)++numCourses;
            }
            if(numCourses==0){{
                repository.delete(currentTerm);
                Toast.makeText(this, "Term Deleted", Toast.LENGTH_LONG).show();
                TermDetails.this.finish();
            }

        }else{
                Toast.makeText(TermDetails.this, "Can't delete a term with a course", Toast.LENGTH_SHORT).show();

            }

    }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView recyclerView = findViewById(R.id.partrecyclerview);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Course> filteredCourse = new ArrayList<>();
        for(Course course:repository.getmAllCourses()){
            if(course.getTermID() == termId) filteredCourse.add(course);
        }

        courseAdapter.setCourses(filteredCourse);

    }}
