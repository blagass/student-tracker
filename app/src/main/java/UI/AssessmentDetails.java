package UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import database.Repository;
import entities.Assessment;
import entities.Course;
import entities.Term;

public class AssessmentDetails extends AppCompatActivity {
    int assessmentId;
    String assessmentName;
    Boolean assessmentType;
    String startDate;
    String endDate;

    Assessment currentAssessment;
    int courseID;

    EditText assessmentNameField;
    EditText startDateField;
    EditText endDateField;

    int numAssessments;

    Repository repository;

    private final Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assessment_details);

        courseID = getIntent().getIntExtra("courseID", -1);
        assessmentName = getIntent().getStringExtra("assessmentName");

        String assessmentStartDate = getIntent().getStringExtra("startDateField");
        String assessmentEndDate = getIntent().getStringExtra("endDateField");

        assessmentNameField = findViewById(R.id.assessmentNameField);
        startDateField = findViewById(R.id.startDateField);
        endDateField = findViewById(R.id.endDateField);

        startDateField.setText(assessmentStartDate);
        endDateField.setText(assessmentEndDate);
        assessmentNameField.setText(assessmentName);

        startDateField.setFocusable(false);
        endDateField.setFocusable(false);
        startDateField.setClickable(true);
        endDateField.setClickable(true);

        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(startDateField);
            }
        };

        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(endDateField);
            }
        };

        startDateField.setOnClickListener(v -> new DatePickerDialog(AssessmentDetails.this, startDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        endDateField.setOnClickListener(v -> new DatePickerDialog(AssessmentDetails.this, endDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new Repository(getApplication());
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.assessmentsave) {
            Assessment assessment;

            if(assessmentId == -1) {

                if (repository.getmAllAssessments().isEmpty())
                    assessmentId = 1;
                else
                    assessmentId = repository.getmAllAssessments().get(repository.getmAllAssessments().size() - 1).getAssessmentID() + 1;

                String start = startDateField.getText().toString();
                String end = endDateField.getText().toString();
                assessment = new Assessment(assessmentId, assessmentNameField.getText().toString(), false, courseID, start, end);
                repository.insert(assessment);
                this.finish();
            }else{
                String start = startDateField.getText().toString();
                String end = endDateField.getText().toString();
                assessment = new Assessment(assessmentId, assessmentNameField.getText().toString(), false, courseID, start, end);
                repository.update(assessment);
                this.finish();
            }
            return true;
        }


        if (item.getItemId() == R.id.assessmentdelete) {

        }
        return true;
    }
}