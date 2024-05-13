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

import entities.Term;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {
    private List<Term> mTerms;
    private final Context context;
    private final LayoutInflater mInflater;

    public TermAdapter (Context context){
        mInflater= LayoutInflater.from(context);
        this.context=context;
    }

    public class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termItemView;
        public TermViewHolder(@NonNull View itemView) {
            super(itemView);
            termItemView=itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    final Term current=mTerms.get(position);
                    Intent intent=new Intent(context,TermDetails.class);
                    intent.putExtra("id",current.getTermID());
                    intent.putExtra("name", current.getTermName());
                    intent.putExtra("termStart",current.getStartDate());
                    intent.putExtra("termEnd",current.getEndDate());
                    context.startActivity(intent);

                }
            });
        }
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.term_list_item,parent,false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        if(mTerms!=null){
            Term current=mTerms.get(position);
            String name=current.getTermName();
            holder.termItemView.setText(name);
        }
        else{
            holder.termItemView.setText("No Term Name");
        }
    }

    @Override
    public int getItemCount() {
        if (mTerms != null) {
            return mTerms.size();
        } else return 0;
    }

    public void setTerms(List<Term> terms){
        mTerms=terms;
        notifyDataSetChanged();
    }

}
