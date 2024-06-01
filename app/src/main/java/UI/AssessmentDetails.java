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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import database.Repository;
import entities.Assessment;

public class AssessmentDetails extends AppCompatActivity {
    int assessmentId;
    String sAssessmentName;
    EditText etAssessmentName;
    Repository repository;
    private String assessmentType;
    private RadioGroup assessmentButtonGroup;
    private RadioButton objectiveButton;
    private RadioButton performanceButton;
    private EditText assessmentStart, assessmentEnd;
    private final Calendar calendar = Calendar.getInstance();
    int courseId;
    private DatePickerDialog.OnDateSetListener startDatePickerListener;
    private DatePickerDialog.OnDateSetListener endDatePickerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assessment_details);

        repository = new Repository(getApplication());

        //findbyviews
        etAssessmentName =findViewById(R.id.assessmentNameEdit);

        //gets
        assessmentId = getIntent().getIntExtra("id",-1);
        sAssessmentName =getIntent().getStringExtra("assessmentNameEdit");
        courseId = getIntent().getIntExtra("courseId", -1);

        String assessmentStartDate = getIntent().getStringExtra("startDate");
        String assessmentEndDate = getIntent().getStringExtra("endDate");

        assessmentStart=findViewById(R.id.etStartDate);
        assessmentEnd=findViewById(R.id.etEndDate);

        assessmentStart.setText(assessmentStartDate);
        assessmentEnd.setText(assessmentEndDate);

        assessmentStart.setFocusable(false);
        assessmentStart.setClickable(true);

        assessmentEnd.setFocusable(false);
        assessmentEnd.setClickable(true);

        //settexts
        etAssessmentName.setText(sAssessmentName);

        //NEW CODE
        assessmentButtonGroup = findViewById(R.id.assessmentButtonGroup);
        objectiveButton = findViewById(R.id.objectiveButton);
        performanceButton = findViewById(R.id.performanceButton);

        courseId = getIntent().getIntExtra("courseId", -1);
        assessmentId = getIntent().getIntExtra("id", -1);


        if (courseId == -1 && assessmentId == -1) {
            finish();
            return;
        }
        
        assessmentButtonGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.objectiveButton) {
                assessmentType = "Objective";
            } else if (checkedId == R.id.performanceButton) {
                assessmentType = "Performance";
            }
        });

        if (assessmentId != -1) {
            Assessment assessment = repository.getmAllAssessments().stream()
                    .filter(a -> a.getAssessmentId() == assessmentId)
                    .findFirst()
                    .orElse(null);

            if (assessment != null) {
                loadDataIntoViews(assessment);
            } else {
                Toast.makeText(this, "Assessment not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        
        startDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(assessmentStart);
            }
        };

        endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(assessmentEnd);
            }
        };

        assessmentStart.setOnClickListener(v -> new DatePickerDialog(AssessmentDetails.this, startDatePickerListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        assessmentEnd.setOnClickListener(v -> new DatePickerDialog(AssessmentDetails.this, endDatePickerListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());




        //TYPE
        assessmentButtonGroup = findViewById(R.id.assessmentButtonGroup);
        objectiveButton = findViewById(R.id.objectiveButton);
        performanceButton = findViewById(R.id.performanceButton);

        assessmentButtonGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.objectiveButton) {
                assessmentType = "Objective";
            } else if (checkedId == R.id.performanceButton) {
                assessmentType = "Performance";
            }
        });

        if (assessmentId != -1) {
            Assessment assessment = repository.getmAllAssessments().stream()
                    .filter(a -> a.getAssessmentId() == assessmentId)
                    .findFirst()
                    .orElse(null);

            if (assessment != null) {
                assessmentType = assessment.getAssessmentType();
                if (assessmentType.equals("Objective")) {
                    objectiveButton.setChecked(true);
                } else {
                    performanceButton.setChecked(true);
                }
            }
        }


    }

    private void loadDataIntoViews(Assessment assessment) {
        etAssessmentName.setText(assessment.getAssessmentName());
        assessmentStart.setText(assessment.getAssessmentStartDate());
        assessmentEnd.setText(assessment.getAssessmentEndDate());
        assessmentType = assessment.getAssessmentType();
        if (assessmentType.equals("Objective")) {
            objectiveButton.setChecked(true);
        } else {
            performanceButton.setChecked(true);
        }
    }

    private void updateDateLabel(EditText dateEditText) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEditText.setText(sdf.format(calendar.getTime()));
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessmentdetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.assessmentsave){

            Assessment assessment;

            if(assessmentId == -1){
                if(repository.getmAllAssessments().isEmpty())
                    assessmentId=1;
                else
                    assessmentId = repository.getmAllAssessments().get(repository.getmAllAssessments().size() -1).getAssessmentId() +1;
                String start = assessmentStart.getText().toString();
                String end = assessmentEnd.getText().toString();
                assessment = new Assessment(assessmentId, etAssessmentName.getText().toString(),assessmentType, start,end,courseId );
                repository.insert(assessment);
                dateSave();
                this.finish();
            }
            else{
                String start = assessmentStart.getText().toString();
                String end = assessmentEnd.getText().toString();
                assessment = new Assessment(assessmentId, etAssessmentName.getText().toString(),assessmentType,start , end,courseId);
                repository.update(assessment);
                dateSave();
                this.finish();
            }
            return true;
        }
//        if(item.getItemId()==R)
        return super.onOptionsItemSelected(item);
    }

    public void dateSave(){
        String startText = assessmentStart.getText().toString();
        String endText = assessmentEnd.getText().toString();
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date dStartDate = null;
        Date dEndDate = null;

        // Date parsing with error handling
        try {
            dStartDate = sdf.parse(startText);
            dEndDate = sdf.parse(endText);
        } catch (ParseException e) {
            Toast.makeText(AssessmentDetails.this, "Invalid date format", Toast.LENGTH_SHORT).show();
        }

        // Calculate alarm triggers (time since boot, including sleep)
        long triggerStart = SystemClock.elapsedRealtime() + dStartDate.getTime() - System.currentTimeMillis();
        long triggerEnd = SystemClock.elapsedRealtime() + dEndDate.getTime() - System.currentTimeMillis();

        // Create intents and pending intents with unique request codes
        Intent startIntent = new Intent(AssessmentDetails.this, MyReceiver.class);
        startIntent.putExtra("key", "Assessment '" + etAssessmentName.getText().toString() + "' starting");
        PendingIntent startSender = PendingIntent.getBroadcast(AssessmentDetails.this, MainActivity.numAlert++, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent endIntent = new Intent(AssessmentDetails.this, MyReceiver.class);
        endIntent.putExtra("key", "Assessment '" + etAssessmentName.getText().toString() + "' ending");
        PendingIntent endSender = PendingIntent.getBroadcast(AssessmentDetails.this, MainActivity.numAlert++, endIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarms
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerStart, startSender);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerEnd, endSender);
        Toast.makeText(AssessmentDetails.this, "Notifications are set for Assessment start and end dates", Toast.LENGTH_SHORT).show();

    }
}
