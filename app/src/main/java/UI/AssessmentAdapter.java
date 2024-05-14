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

        private AssessmentViewHolder(View itemView) {
            super(itemView);
            assessmentTitleTextView = itemView.findViewById(R.id.assessmentTitle);
            startDateTextView = itemView.findViewById(R.id.startDate);
            endDateTextView = itemView.findViewById(R.id.endDate);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Assessment assessment = mAssessments.get(position);
                    Intent intent = new Intent(context, AssessmentDetails.class);
                    intent.putExtra("assessmentID", assessment.getAssessmentID());
                    // Put other assessment details in the intent
                    intent.putExtra("assessmentName", assessment.getAssessmentName());
                    intent.putExtra("assessmentType", assessment.isAssessmentType());
                    intent.putExtra("courseID", assessment.getCourseID());
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
        if (mAssessments != null && position < mAssessments.size()) {
            Assessment current = mAssessments.get(position);

            if (current != null) {
                holder.assessmentTitleTextView.setText(current.getAssessmentName() != null ? current.getAssessmentName() : "Assessment Name Unavailable");
                holder.startDateTextView.setText("Start Date: " + current.getStart());
                holder.endDateTextView.setText("End Date: " + current.getEnd());
            } else {
                Log.e(TAG, "Assessment object is null at position: " + position);
                holder.assessmentTitleTextView.setText("Error: Assessment data not found");
                holder.startDateTextView.setText("Error");
                holder.endDateTextView.setText("Error");

            }
        } else {
            Log.e(TAG, "Invalid assessment list or position: " + position);

        }
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
