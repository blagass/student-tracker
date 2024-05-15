
package UI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker.R;

import java.util.List;

import entities.Assessment;
import entities.Course;
import entities.Term;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {
    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflater;

    private static final String TAG = "AssessmentAdapter";

    public AssessmentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentTitleTextView;
        private final TextView startDateTextView;
        private final TextView endDateTextView;

        private AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            assessmentTitleTextView = itemView.findViewById(R.id.assessmentTitle);
            startDateTextView = itemView.findViewById(R.id.startDate);
            endDateTextView = itemView.findViewById(R.id.endDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    Assessment assessment = mAssessments.get(position);
                    Intent intent = new Intent(context, AssessmentDetails.class);
                    intent.putExtra("assessmentID", assessment.getAssessmentID());
                    intent.putExtra("assessmentName", assessment.getAssessmentName());
                    intent.putExtra("startDate", assessment.getStart());
                    intent.putExtra("endDate", assessment.getEnd());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        if (mAssessments != null) {
            Assessment current = mAssessments.get(position);
            String name=current.getAssessmentName();
            holder.assessmentTitleTextView.setText(name);
        }
        else{
            holder.assessmentTitleTextView.setText("No Assessment");
        }

        holder.itemView.setOnClickListener(view -> {
            Assessment assessment = mAssessments.get(position);

            //DON'T REMEMBERT WHAT I WAS DOING HERE, BUT FIXING THESE REDS AND MAKE SURE THE GREENS EXIST SOMEWHERE
            Intent intent = new Intent(context, CourseDetails.class);
            intent.putExtra("id,", assessment.getCourseID());
            intent.putExtra("name", assessment.getCourseName());
            intent.putExtra("courseID", assessment.getCourseID());
            intent.putExtra("editStartDate", assessment.getStartDate());
            intent.putExtra("editEndDate", assessment.getEndDate());


            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mAssessments != null ? mAssessments.size() : 0;
    }

    public void setAssessments(List<Assessment> assessments) {
        mAssessments = assessments;
        notifyDataSetChanged();
    }
}

