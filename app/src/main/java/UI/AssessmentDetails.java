package UI;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

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

        //settexts
        etAssessmentName.setText(sAssessmentName);

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

                assessment = new Assessment(assessmentId, etAssessmentName.getText().toString(),assessmentType);
                repository.insert(assessment);
                this.finish();;
            }
            else{
                assessment = new Assessment(assessmentId, etAssessmentName.getText().toString(),assessmentType);
                repository.update(assessment);
                this.finish();;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
