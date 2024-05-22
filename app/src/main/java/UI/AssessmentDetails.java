package UI;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import database.Repository;
import entities.Assessment;
import entities.Course;

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

        String assessmentStartDate = getIntent().getStringExtra("etStartDate");
        String assessmentEndDate = getIntent().getStringExtra("etEndDate");

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
                assessmentType = assessment.getAssessmentType(); // Get the assessment type
                if (assessmentType.equals("Objective")) {
                    objectiveButton.setChecked(true);
                } else {
                    performanceButton.setChecked(true);
                }
            }
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
                assessment = new Assessment(assessmentId, etAssessmentName.getText().toString(),assessmentType, start,end );
                repository.insert(assessment);
                this.finish();;
            }
            else{
                String start = assessmentStart.getText().toString();
                String end = assessmentEnd.getText().toString();
                assessment = new Assessment(assessmentId, etAssessmentName.getText().toString(),assessmentType,start , end);
                repository.update(assessment);
                this.finish();;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
