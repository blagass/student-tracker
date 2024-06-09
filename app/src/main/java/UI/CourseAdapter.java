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

import entities.Course;
import entities.Term;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflater;

    class CourseViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseItemView;
        private final TextView courseItemView2;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.textView3);
            courseItemView2 = itemView.findViewById(R.id.textView4);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Course current = mCourses.get(position);
                    Intent intent = new Intent(context, CourseDetails.class);
                    intent.putExtra("id", current.getCourseID());
                    intent.putExtra("name", current.getCourseName());
                    intent.putExtra("editInstructorName", current.getInstructorName());
                    intent.putExtra("statusSpinner",current.getSelectedStatus());
                    intent.putExtra("editStartDate", current.getStartDate());
                    intent.putExtra("editEndDate", current.getEndDate());
                    intent.putExtra("note",current.getNote());
                    intent.putExtra("editPhoneField",current.getPhone());
                    intent.putExtra("editEmailField",current.getEmail());
                    context.startActivity(intent);
                }
            });
        }
    }
    public CourseAdapter (Context context){
        mInflater= LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.course_list_item,parent,false);
        return new CourseAdapter.CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position){
        if(mCourses!=null){

            Course current=mCourses.get(position);
            String name= current.getCourseName();
            int termID= current.getTermID();
            holder.courseItemView.setText(name);
            holder.courseItemView2.setText(Integer.toString(termID));
            int courseId = current.getCourseID();
            holder.courseItemView.setText("Course ID: " + courseId);
            holder.courseItemView2.setText(current.getCourseName());
        }


        holder.itemView.setOnClickListener(view -> {
            Course course = mCourses.get(position);

            Intent intent = new Intent(context, CourseDetails.class);
            intent.putExtra("id,", course.getCourseID());
            intent.putExtra("name", course.getCourseName());
            intent.putExtra("editInstructorName", course.getInstructorName());
            intent.putExtra("termID", course.getTermID());
            intent.putExtra("editStartDate", course.getStartDate());
            intent.putExtra("editEndDate", course.getEndDate());
            intent.putExtra("statusSpinner", course.getSelectedStatus());
            intent.putExtra("note",course.getNote());
            intent.putExtra("editPhoneField",course.getPhone());
            intent.putExtra("editEmailField",course.getEmail());
            context.startActivity(intent);
        });

        if (mCourses != null && position < mCourses.size()) {
            Course current = mCourses.get(position);

            int courseId = current.getCourseID();
            holder.courseItemView.setText("Course ID: " + courseId);
            holder.courseItemView2.setText(current.getCourseName());

        } else {
            holder.courseItemView.setText("No Course ID");
            holder.courseItemView2.setText("No Course Name");
        }

    }

    public void setCourses(List<Course> courses){
        mCourses=courses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCourses != null) {
            return mCourses.size();
        } else return 0;
    }
}
