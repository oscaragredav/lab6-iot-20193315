package com.iot.lab6.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iot.lab6.R;
import com.iot.lab6.entity.Income;

import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.incomeViewHolder> {

    Context context;
    List<Income> list;

    public IncomeAdapter(Context context, List<Income> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public incomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_income,parent,false);
        return new incomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull incomeViewHolder holder, int position) {
        Income income = list.get(position);

        holder.tittleItem.setText(income.getTittle());
        holder.descriptionItem.setText(income.getDescription());
        String amountString = String.valueOf(income.getAmount());
        holder.amountItem.setText(String.format("s/ %s", amountString));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class incomeViewHolder extends RecyclerView.ViewHolder{
        TextView tittleItem, descriptionItem, amountItem;
        public incomeViewHolder(@NonNull View itemView) {
            super(itemView);
            tittleItem = itemView.findViewById(R.id.tittle);
            descriptionItem = itemView.findViewById(R.id.description);
            amountItem = itemView.findViewById(R.id.amount);
        }
    }
}
