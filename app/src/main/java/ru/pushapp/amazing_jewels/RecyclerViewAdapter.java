package ru.pushapp.amazing_jewels;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecycleViewAdapterHolder> {


    class RecycleViewAdapterHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView score;

        RecycleViewAdapterHolder (View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.leader_name_tv);
            score = itemView.findViewById(R.id.leader_score_tv);
        }
    }


    private LayoutInflater inflater;
    private static ArrayList<LeaderUnit> list_items;

    public RecyclerViewAdapter(Context context, ArrayList<LeaderUnit> items) {
        this.list_items = items;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecycleViewAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.leader_item, parent, false);
        return new RecycleViewAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterHolder holder, int position) {
        String name = 1 + position + ". " + list_items.get(position).getName();
        String score = String.valueOf(list_items.get(position).getScore());

        holder.name.setText(name);
        holder.score.setText(score);
    }


    @Override
    public int getItemCount() {
        return list_items.size();
    }

}
