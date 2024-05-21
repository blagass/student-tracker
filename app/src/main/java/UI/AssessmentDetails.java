package UI;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker.R;

import database.Repository;
import entities.Assessment;

public class AssessmentDetails extends AppCompatActivity {
    int assessmentId;
    String sAssessmentName;
    EditText etAssessmentName;
    Repository repository;

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

                assessment = new Assessment(assessmentId, etAssessmentName.getText().toString());
                repository.insert(assessment);
                this.finish();;
            }
            else{
                assessment = new Assessment(assessmentId, etAssessmentName.getText().toString());
                repository.update(assessment);
                this.finish();;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
