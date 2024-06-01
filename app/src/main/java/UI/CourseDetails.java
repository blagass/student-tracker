package UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class CourseDetails extends AppCompatActivity {
    String name;
    int courseID;
    int termID;

    String note;

    EditText editName;
    EditText editNote;

    //custom adds
    String instructor;
    EditText editInstructor;
    Spinner statusSpinner;
    Repository repository;

    private EditText courseStart, courseEnd;
    private final Calendar calendar = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener startDatePickerListener;
    private DatePickerDialog.OnDateSetListener endDatePickerListener;

    ArrayAdapter<CharSequence> statusAdapter;
    RecyclerView recyclerView;
    Course currentCourse;
    int numAssessments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details);
        FloatingActionButton fab = findViewById(R.id.addAssessmentButton);

        //recyclerView=findViewById(R.id.assessmentRecycler); //ADDED THIS NEW

        repository = new Repository(getApplication());
        editName = findViewById(R.id.coursename);
        courseStart = findViewById(R.id.editStartDate);
        courseEnd = findViewById(R.id.editEndDate);
        editInstructor = findViewById(R.id.termEnd);
        editNote = findViewById(R.id.note);

        //Setting intents
        String courseStartDate = getIntent().getStringExtra("editStartDate");
        String courseEndDate = getIntent().getStringExtra("editEndDate");
        name = getIntent().getStringExtra("name");
        instructor = getIntent().getStringExtra("instructor");
        courseID = getIntent().getIntExtra("id,", -1);
        termID = getIntent().getIntExtra("termID", -1);
        note = getIntent().getStringExtra("note");

        courseStart.setText(courseStartDate);
        courseEnd.setText(courseEndDate);
        editInstructor.setText(instructor);
        editName.setText(name);
        editNote.setText(note);

        courseStart.setFocusable(false);
        courseStart.setClickable(true);
        courseEnd.setFocusable(false);
        courseEnd.setClickable(true);


        //Spinner
//        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
//                this,
//                R.array.course_status_array,
//                android.R.layout.simple_spinner_item
//        );
//        String courseStatus = getIntent().getStringExtra("statusSpinner");
//        statusSpinner = findViewById(R.id.statusSpinner);
//        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        statusSpinner.setAdapter(statusAdapter);
//        if (courseStatus != null) {
//            int spinnerPosition = statusAdapter.getPosition(courseStatus);
//            if (spinnerPosition >= 0) {
//                statusSpinner.setSelection(spinnerPosition);
//            } else {
//
//                statusSpinner.setSelection(0);
//            }
//        }

        // Spinner Setup
        statusSpinner = findViewById(R.id.statusSpinner);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.course_status_array, // Your string-array resource containing statuses
                android.R.layout.simple_spinner_item
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        // Get initial status from intent and set selection (only in onCreate)
        String initialCourseStatus = getIntent().getStringExtra("statusSpinner");
        if (initialCourseStatus != null) {
            int spinnerPosition = statusAdapter.getPosition(initialCourseStatus);
            // Ensure the position is valid to prevent crashes
            if (spinnerPosition >= 0 && spinnerPosition < statusAdapter.getCount()) {
                statusSpinner.setSelection(spinnerPosition);
            } else {
                // Handle invalid position (e.g., set to a default value)
                statusSpinner.setSelection(0); // Select the first item by default
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


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

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        courseStart.setOnClickListener(v -> new DatePickerDialog(CourseDetails.this, startDatePickerListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        courseEnd.setOnClickListener(v -> new DatePickerDialog(CourseDetails.this, endDatePickerListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(courseID>0){
                    Intent intent = new Intent(CourseDetails.this,AssessmentDetails.class);
                    intent.putExtra("courseId",courseID);
                    startActivity(intent);
                }else{
                    Toast.makeText(CourseDetails.this,"Please save the course before adding an assessment",Toast.LENGTH_LONG).show();
                }
            }
        });

        recyclerView = findViewById(R.id.assessmentRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);

        repository = new Repository(getApplication());

        List<Assessment> filteredAssessments = new ArrayList<>();
        for(Assessment assessment:repository.getmAllAssessments()){
            if(assessment.getAssessmentCourseId()==courseID)filteredAssessments.add(assessment);
        }
        assessmentAdapter.setAssessments(filteredAssessments);
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
                String note =  editNote.getText().toString();
                course = new Course(courseID, editName.getText().toString(), editInstructor.getText().toString(), termID, selectedStatus, start, end,note);
                repository.insert(course);
                dateSave();
                this.finish();
            } else {
                String selectedStatus = statusSpinner.getSelectedItem().toString();
                String start = courseStart.getText().toString();
                String end = courseEnd.getText().toString();
                String note =  editNote.getText().toString();
                course = new Course(courseID, editName.getText().toString(), editInstructor.getText().toString(), termID, selectedStatus, start, end, note);
                repository.update(course);
                dateSave();
                this.finish();
            }
            return true;
        }

        if (item.getItemId() == R.id.coursedelete) {
            for (Course course : repository.getmAllCourses()) {
                if (course.getCourseID() == courseID) {
                    currentCourse = course;
                }
            }

            numAssessments = repository.getmAssociatedAssessments(courseID).size();
            if (numAssessments == 0) {
                repository.delete(currentCourse);
                Toast.makeText(this, "Course Deleted", Toast.LENGTH_LONG).show();
                CourseDetails.this.finish();
            } else {
                Toast.makeText(CourseDetails.this, "Cannot delete a course with an assessment", Toast.LENGTH_SHORT).show();
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
            String startText = courseStart.getText().toString();
            String endText = courseEnd.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date dStartDate = null;
            Date dEndDate = null;


            try {
                dStartDate = sdf.parse(startText);
                dEndDate = sdf.parse(endText);
            } catch (ParseException e) {
                Toast.makeText(CourseDetails.this, "Invalid date format", Toast.LENGTH_SHORT).show();
                return true;
            }


            long triggerStart = SystemClock.elapsedRealtime() + dStartDate.getTime() - System.currentTimeMillis();
            long triggerEnd = SystemClock.elapsedRealtime() + dEndDate.getTime() - System.currentTimeMillis();


            Intent startIntent = new Intent(CourseDetails.this, MyReceiver.class);
            startIntent.putExtra("key", "Course '" + editName.getText().toString() + "' starting");
            PendingIntent startSender = PendingIntent.getBroadcast(CourseDetails.this, MainActivity.numAlert++, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent endIntent = new Intent(CourseDetails.this, MyReceiver.class);
            endIntent.putExtra("key", "Course '" + editName.getText().toString() + "' ending");
            PendingIntent endSender = PendingIntent.getBroadcast(CourseDetails.this, MainActivity.numAlert++, endIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerStart, startSender);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerEnd, endSender);
            Toast.makeText(CourseDetails.this, "Notifications set for course start and end dates", Toast.LENGTH_SHORT).show();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.assessmentRecycler);
       // repository = new Repository(getApplication());
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Assessment> filteredAssessments = new ArrayList<>();
        for(Assessment assessment:repository.getmAllAssessments()){
            if(assessment.getAssessmentCourseId()==courseID)filteredAssessments.add(assessment);
        }
        assessmentAdapter.setAssessments(filteredAssessments);

    }

    public void dateSave(){
        String startText = courseStart.getText().toString();
        String endText = courseEnd.getText().toString();
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date dStartDate = null;
        Date dEndDate = null;

        try {
            dStartDate = sdf.parse(startText);
            dEndDate = sdf.parse(endText);
        } catch (ParseException e) {
            Toast.makeText(CourseDetails.this, "Invalid date format", Toast.LENGTH_SHORT).show();
        }


        long triggerStart = SystemClock.elapsedRealtime() + dStartDate.getTime() - System.currentTimeMillis();
        long triggerEnd = SystemClock.elapsedRealtime() + dEndDate.getTime() - System.currentTimeMillis();


        Intent startIntent = new Intent(CourseDetails.this, MyReceiver.class);
        startIntent.putExtra("key", "Course '" + editName.getText().toString() + "' starting");
        PendingIntent startSender = PendingIntent.getBroadcast(CourseDetails.this, MainActivity.numAlert++, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent endIntent = new Intent(CourseDetails.this, MyReceiver.class);
        endIntent.putExtra("key", "Course '" + editName.getText().toString() + "' ending");
        PendingIntent endSender = PendingIntent.getBroadcast(CourseDetails.this, MainActivity.numAlert++, endIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerStart, startSender);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerEnd, endSender);
        Toast.makeText(CourseDetails.this, "Notifications set for course start and end dates", Toast.LENGTH_SHORT).show();

    }
}