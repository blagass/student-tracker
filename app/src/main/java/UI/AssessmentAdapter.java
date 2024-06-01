package UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker.R;

import java.util.List;

import entities.Assessment;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder>{
    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflater;

    public AssessmentAdapter(Context context) {
        mInflater= LayoutInflater.from(context);
        this.context=context;
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.assessment_list_item,parent,false);
        return new AssessmentAdapter.AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {

        if (mAssessments != null && position < mAssessments.size()) {
            Assessment current = mAssessments.get(position);

            holder.assessmentNameView.setText(current.getAssessmentName());
            holder.assessmentStartView.setText("Start Date: " + current.getAssessmentStartDate());
            holder.assessmentEndView.setText("End Date: " + current.getAssessmentEndDate());

            holder.itemView.setOnClickListener(view ->{
                Assessment assessment =mAssessments.get(position);
                Intent intent = new Intent(context, AssessmentDetails.class);
                intent.putExtra("id", assessment.getAssessmentId());
                intent.putExtra("assessmentNameEdit", assessment.getAssessmentName());
                intent.putExtra("assessmentType", assessment.getAssessmentType());
                intent.putExtra("etStartDate", assessment.getAssessmentStartDate());
                intent.putExtra("etEndDate", assessment.getAssessmentEndDate());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mAssessments != null) {
            return mAssessments.size();
        } else return 0;
    }

    class AssessmentViewHolder extends RecyclerView.ViewHolder{
        private final TextView assessmentNameView;
        private final TextView assessmentStartView;
        private final TextView assessmentEndView;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            assessmentNameView = itemView.findViewById(R.id.assessmentListName);
            assessmentStartView = itemView.findViewById(R.id.assessmentListStart);
            assessmentEndView = itemView.findViewById(R.id.assessmentListEnd);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Assessment current = mAssessments.get(position);
                    Intent intent = new Intent(context, AssessmentDetails.class);
                    intent.putExtra("id", current.getAssessmentId());
                    intent.putExtra("assessmentNameEdit", current.getAssessmentName());
                    intent.putExtra("assessmentType", current.getAssessmentType());
                    intent.putExtra("etStartDate", current.getAssessmentStartDate());
                    intent.putExtra("etEndDate", current.getAssessmentEndDate());
                    context.startActivity(intent);
                }
            });

        }
    }

    public void setAssessments(List<Assessment> assessments){
        mAssessments=assessments;
        notifyDataSetChanged();
    }
}
