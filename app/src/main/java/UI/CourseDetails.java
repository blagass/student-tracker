package UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.tracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import database.Repository;
import entities.Course;

public class CourseDetails extends AppCompatActivity {
    String name;
    int courseID;
    int termID;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details);
        FloatingActionButton fab = findViewById(R.id.addAssessmentButton);

        repository = new Repository(getApplication());
        editName = findViewById(R.id.coursename);
        courseStart = findViewById(R.id.editStartDate);
        courseEnd = findViewById(R.id.editEndDate);

        //Setting intents
        String courseStartDate = getIntent().getStringExtra("editStartDate");
        String courseEndDate = getIntent().getStringExtra("editEndDate");
        name = getIntent().getStringExtra("name");
        instructor = getIntent().getStringExtra("instructor");
        courseID = getIntent().getIntExtra("id,", -1);
        termID = getIntent().getIntExtra("termID", -1);

        courseStart.setText(courseStartDate);
        courseEnd.setText(courseEndDate);
        editInstructor = findViewById(R.id.termEnd);
        editNote = findViewById(R.id.note);

        courseStart.setFocusable(false);
        courseStart.setClickable(true);
        courseEnd.setFocusable(false);
        courseEnd.setClickable(true);

        editInstructor.setText(instructor);
        editName.setText(name);


        //Spinner
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.course_status_array,
                android.R.layout.simple_spinner_item
        );
        String courseStatus = getIntent().getStringExtra("status");
        statusSpinner = findViewById(R.id.statusSpinner);
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
        statusSpinner = findViewById(R.id.statusSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);




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
                    intent.putExtra("courseID",courseID);
                    startActivity(intent);
                }else{
                    Toast.makeText(CourseDetails.this,"Please save the course before adding an assessment",Toast.LENGTH_LONG).show();
                }
            }
        });

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
        if (item.getItemId() == R.id.assessmentsave) {

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


}