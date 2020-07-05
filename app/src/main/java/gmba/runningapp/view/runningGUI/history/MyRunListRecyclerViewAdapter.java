package gmba.runningapp.view.runningGUI.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import gmba.runningapp.R;
import gmba.runningapp.model.datenspeicher.classes.Run;

import java.util.List;

public class MyRunListRecyclerViewAdapter extends RecyclerView.Adapter<MyRunListRecyclerViewAdapter.ViewHolder> {

    private List<Run> history;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRunListRecyclerViewAdapter(Context context, List<Run> data) {
        this.mInflater = LayoutInflater.from(context);
        this.history = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String runId = String.valueOf(history.get(position).getId());
        String runDistance = String.valueOf((int)history.get(position).getDistance());
        String runTime = String.valueOf(history.get(position).getTime());
        String runDate = history.get(position).getDate();
        holder.myTextViewRunId.setText("ID: "+runId+"  ");
        holder.myTextViewRunDistance.setText("Distance: "+ runDistance+"m  ");
        holder.myTextViewRunTime.setText("Time: "+runTime+" sek  ");
        holder.myTextViewRunDate.setText("Date: "+runDate+"  ");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return history.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextViewRunId;
        TextView myTextViewRunDistance;
        TextView myTextViewRunTime;
        TextView myTextViewRunDate;

        ViewHolder(View itemView) {
            super(itemView);
            myTextViewRunId = itemView.findViewById(R.id.tvRunId);
            myTextViewRunDistance = itemView.findViewById(R.id.tvRunDistance);
            myTextViewRunTime = itemView.findViewById(R.id.tvRunTime);
            myTextViewRunDate = itemView.findViewById(R.id.tvRunDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    void removeItem(int position){
        history.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    // convenience method for getting data at click position
    Run getItem(int id) {
        return history.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }




}

