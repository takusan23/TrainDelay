package io.github.takusan23.traindelay;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrainDelayRecyclerViewAdapter extends RecyclerView.Adapter<TrainDelayRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ArrayList> arrayListArrayAdapter;

    public TrainDelayRecyclerViewAdapter(ArrayList<ArrayList> recyclerViewList) {
        arrayListArrayAdapter = recyclerViewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<String> item = arrayListArrayAdapter.get(position);

        String title = item.get(1);
        String company = item.get(2);

        holder.TitleTextView.setText(title);
        holder.CompanyTextView.setText(company);
    }

    @Override
    public int getItemCount() {
        return arrayListArrayAdapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView TitleTextView;
        public TextView CompanyTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleTextView = itemView.findViewById(R.id.recyclerview_title_textview);
            CompanyTextView = itemView.findViewById(R.id.recyclerview_company_textview);
        }
    }
}