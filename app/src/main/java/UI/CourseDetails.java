package UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import database.Repository;
import entities.Assessment;
import entities.Course;
import entities.Term;

public class CourseDetails extends AppCompatActivity {
    String name;
    int courseID;
    int termID;

    int assessmentID;
    EditText editName;
    EditText editNote;

    //custom adds
    String instructor;
    EditText editInstructor;
    Spinner statusSpinner;

    TextView dateDisplay;

    Repository repository;

    private EditText courseStart, courseEnd;
    private final Calendar calendar = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener startDatePickerListener;
    private DatePickerDialog.OnDateSetListener endDatePickerListener;

    ArrayAdapter<CharSequence> statusAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details);

        FloatingActionButton fab = findViewById(R.id.addAssessmentButton);

        repository = new Repository(getApplication());


        name = getIntent().getStringExtra("name");
        editName = findViewById(R.id.coursename);
        editName.setText(name);


        String courseStartDate = getIntent().getStringExtra("editStartDate");
        String courseEndDate = getIntent().getStringExtra("editEndDate");

        String courseStatus = getIntent().getStringExtra("status");


        statusSpinner = findViewById(R.id.statusSpinner);


        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.course_status_array,
                android.R.layout.simple_spinner_item
        );

        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        statusSpinner.setAdapter(statusAdapter);
        if (courseStatus != null) {
            int spinnerPosition = statusAdapter.getPosition(courseStatus);
            if (spinnerPosition >= 0) {
                statusSpinner.setSelection(spinnerPosition);
            } else {

                statusSpinner.setSelection(0);
            }
        }

        courseStart = findViewById(R.id.editStartDate);
        courseEnd = findViewById(R.id.editEndDate);

        courseStart.setText(courseStartDate);
        courseEnd.setText(courseEndDate);

        courseStart.setFocusable(false);
        courseStart.setClickable(true);
        courseEnd.setFocusable(false);
        courseEnd.setClickable(true);

        // Date Picker Setup
        startDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(courseStart);
            }
        };

        endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(courseEnd);
            }
        };

        courseStart.setOnClickListener(v -> new DatePickerDialog(CourseDetails.this, startDatePickerListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        courseEnd.setOnClickListener(v -> new DatePickerDialog(CourseDetails.this, endDatePickerListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        //Instructor
        instructor = getIntent().getStringExtra("instructor");
        editInstructor = findViewById(R.id.termEnd);
        editInstructor.setText(instructor);

        //Course and Term setup
        courseID = getIntent().getIntExtra("id,", -1);
        termID = getIntent().getIntExtra("termID", -1);
        assessmentID = getIntent().getIntExtra("assessmentID", -1);

        editNote = findViewById(R.id.note);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        statusSpinner = findViewById(R.id.statusSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (courseID > 0) {
                    Intent intent = new Intent(CourseDetails.this, AssessmentDetails.class);
                    intent.putExtra("courseID", courseID);
                    startActivity(intent);
                } else {
                    Toast.makeText(CourseDetails.this, "Please save the course before adding an assessment.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.assessmentRecycler);
        repository = new Repository(getApplication());
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Assessment> filteredAssessment = new ArrayList<>();
        for (Assessment assessment : repository.getmAllAssessments()) {
            if (assessment.getAssessmentID() == assessmentID) filteredAssessment.add(assessment);
        }
        assessmentAdapter.setAssessments(filteredAssessment);
    }

    private void updateDateLabel(EditText dateEditText) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEditText.setText(sdf.format(calendar.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coursedetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.coursesave) {

            Course course;
            if (courseID == -1) {
                if (repository.getmAllCourses().isEmpty())
                    courseID = 1;
                else
                    courseID = repository.getmAllCourses().get(repository.getmAllCourses().size() - 1).getCourseID() + 1;

                String selectedStatus = statusSpinner.getSelectedItem().toString();
                String start = courseStart.getText().toString();
                String end = courseEnd.getText().toString();
                course = new Course(courseID, editName.getText().toString(), editInstructor.getText().toString(), termID, selectedStatus, start, end);
                repository.insert(course);
                this.finish();
            } else {
                String selectedStatus = statusSpinner.getSelectedItem().toString();
                String start = courseStart.getText().toString();
                String end = courseEnd.getText().toString();
                course = new Course(courseID, editName.getText().toString(), editInstructor.getText().toString(), termID, selectedStatus, start, end);
                repository.update(course);
                this.finish();
            }
            return true;
        }

        if (item.getItemId() == R.id.sharenote) {
            Intent sentIntent = new Intent();

            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString() + "EXTRA_TEXT");
            sentIntent.putExtra(Intent.EXTRA_TITLE, editNote.getText().toString() + "EXTRA_TITLE");
            sentIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sentIntent, null);

            startActivity(shareIntent);
            return true;
        }
        if (item.getItemId() == R.id.notify) {
            String dateFromScreen = dateDisplay.getText().toString(); //not sure I'm pulling the right thing
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(CourseDetails.this, MyReceiver.class);
            intent.putExtra("key", "Heres a test message");
            PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //trying ths out
    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView recyclerView = findViewById(R.id.assessmentRecycler);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Assessment> filteredAssessment = new ArrayList<>();
        for (Assessment assessment : repository.getmAllAssessments()) {
            if (assessment.getAssessmentID() == assessmentID) filteredAssessment.add(assessment);
        }

        assessmentAdapter.setAssessments(filteredAssessment);

    }
}