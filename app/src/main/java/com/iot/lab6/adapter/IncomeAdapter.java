package com.iot.lab6.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iot.lab6.R;
import com.iot.lab6.activity.MainActivity;
import com.iot.lab6.entity.Income;
import com.iot.lab6.fragments.EditIncomeFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        Timestamp date = income.getDate();
        Date date1 = date.toDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormatString = dateFormat.format(date1);
        holder.dateItem.setText(dateFormatString);

        //borrar
        holder.btn2.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("income")
                    .document(income.getUserId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        list.remove(position);
                        notifyItemRemoved(position);
                      notifyItemRangeChanged(position,list.size());
                    })
                    .addOnFailureListener(e -> e.printStackTrace());

            Toast.makeText(holder.itemView.getContext(), "Ingreso eliminado", Toast.LENGTH_SHORT).show();
        });

        //editar
        holder.btn1.setOnClickListener(v -> {
            Fragment editIncomeFragment = new EditIncomeFragment();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("income")
                    .document(income.getUserId())
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            Income income1 = documentSnapshot.toObject(Income.class);
                            if (income1!=null){
                                Bundle bundle = new Bundle();
                                bundle.putString("userId", income.getUserId());
                                bundle.putString("tittle", income.getTittle());
                                bundle.putString("description", income.getDescription());
                                bundle.putDouble("amount", income.getAmount());
                                Log.d("msg-test" , "cantidad0: " + income.getAmount());
                                long seconds = income.getDate().getSeconds();
                                int nanoseconds = income.getDate().getNanoseconds();
                                bundle.putLong("seconds", seconds);
                                bundle.putInt("nanoseconds", nanoseconds);

                                editIncomeFragment.setArguments(bundle);
                                if (context instanceof MainActivity){
                                    ((MainActivity) context).replaceFragment(editIncomeFragment);
                                }
                            }
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class incomeViewHolder extends RecyclerView.ViewHolder{
        TextView tittleItem, descriptionItem, amountItem, dateItem;
        Button btn1, btn2;
        public incomeViewHolder(@NonNull View itemView) {
            super(itemView);
            tittleItem = itemView.findViewById(R.id.tittle);
            descriptionItem = itemView.findViewById(R.id.description);
            amountItem = itemView.findViewById(R.id.amount);
            dateItem = itemView.findViewById(R.id.date);
            btn1 = itemView.findViewById(R.id.btn1);
            btn2 = itemView.findViewById(R.id.btn2);
        }
    }
}
