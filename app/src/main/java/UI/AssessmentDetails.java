package UI;


import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker.R;

public class AssessmentDetails extends AppCompatActivity {
    int assessmentId;
    String assessmentName;
    EditText editAssessmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assessment_details);

        assessmentId = getIntent().getIntExtra("id",-1);
        assessmentName =getIntent().getStringExtra("name");

        editAssessmentName=findViewById(R.id.assessmentNameEdit);
        editAssessmentName.setText(assessmentName);


    }



}
