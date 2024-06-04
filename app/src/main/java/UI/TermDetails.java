package UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private EditText termStart, termEnd;
    private final Calendar calendar = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener startDatePickerListener;
    private DatePickerDialog.OnDateSetListener endDatePickerListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_term_details);

        FloatingActionButton fab = findViewById(R.id.addTermDetails);

        termId = getIntent().getIntExtra("id",-1);
        name =getIntent().getStringExtra("name");

        String termStartDate = getIntent().getStringExtra("termStart");
        String termEndDate = getIntent().getStringExtra("termEnd");

        termStart = findViewById(R.id.termStart);
        termEnd = findViewById(R.id.termEnd);
        editName=findViewById(R.id.titletext);

        termStart.setText(termStartDate);
        termEnd.setText(termEndDate);
        editName.setText(name);

        termStart.setFocusable(false);
        termStart.setClickable(true);
        termEnd.setFocusable(false);
        termEnd.setClickable(true);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                finish();
            }
        });
        startDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(termStart);
            }
        };

        endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(termEnd);
            }
        };

        termStart.setOnClickListener(v -> new DatePickerDialog(TermDetails.this, startDatePickerListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        termEnd.setOnClickListener(v -> new DatePickerDialog(TermDetails.this, endDatePickerListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(termId > 0) {
                    Intent intent = new Intent(TermDetails.this, CourseDetails.class);
                    intent.putExtra("termID", termId);
                    startActivity(intent);
                }else{
                    Toast.makeText(TermDetails.this, "You must save the term before adding courses.", Toast.LENGTH_SHORT).show();
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
    private void updateDateLabel(EditText dateEditText) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEditText.setText(sdf.format(calendar.getTime()));
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
                String start = termStart.getText().toString();
                String end = termEnd.getText().toString();
                term = new Term(termId,editName.getText().toString(),start , end);
                repository.insert(term);
                this.finish();
            }
            else {
                String start = termStart.getText().toString();
                String end = termEnd.getText().toString();
                term = new Term(termId,editName.getText().toString(),start ,end );
                repository.update(term);
                this.finish();
            }
        }

        if(item.getItemId()==R.id.termcancel){
            TermDetails.this.finish();
            return true;
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

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {

        super.onResume();

        List<Course> filteredCourse = new ArrayList<>();
        for(Course course:repository.getmAllCourses()){
            if(course.getTermID() == termId)
                filteredCourse.add(course);
        }

        RecyclerView recyclerView = findViewById(R.id.partrecyclerview);
        final CourseAdapter courseAdapter = new CourseAdapter(this);

        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        courseAdapter.setCourses(filteredCourse);
        };

    }


