package com.twitter.challenge.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twitter.challenge.R;
import com.twitter.challenge.model.Conditions;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Conditions> conditionsList = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_list_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MainViewHolder)holder).bind(conditionsList.get(position));
    }

    @Override
    public int getItemCount() {
        return conditionsList.size();
    }

    public void setConditionsList(List<Conditions> conditionsList) {
        this.conditionsList = conditionsList;
        notifyDataSetChanged();
    }
}
