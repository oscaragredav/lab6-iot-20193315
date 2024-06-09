package com.iot.lab6.adapter;

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
import com.iot.lab6.entity.Outcome;
import com.iot.lab6.fragments.EditIncomeFragment;
import com.iot.lab6.fragments.EditOutcomeFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OutcomeAdapter extends RecyclerView.Adapter<OutcomeAdapter.outcomeViewHolder> {

    Context context;
    List<Outcome> list;

    public OutcomeAdapter(Context context, List<Outcome> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public outcomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_outcome,parent,false);
        return new outcomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull outcomeViewHolder holder, int position) {
        Outcome outcome = list.get(position);

        holder.tittleItem.setText(outcome.getTittle());
        holder.descriptionItem.setText(outcome.getDescription());
        String amountString = String.valueOf(outcome.getAmount());
        holder.amountItem.setText(String.format("s/ %s", amountString));
        Timestamp date = outcome.getDate();
        Date date1 = date.toDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormatString = dateFormat.format(date1);
        holder.dateItem.setText(dateFormatString);

        //borrar
        holder.btn2.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("outcome")
                    .document(outcome.getUserId())
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
            Fragment editOutcomeFragment = new EditOutcomeFragment();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("outcome")
                    .document(outcome.getUserId())
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            Outcome outcome1 = documentSnapshot.toObject(Outcome.class);
                            if (outcome1!=null){
                                Bundle bundle = new Bundle();
                                bundle.putString("userId", outcome.getUserId());
                                bundle.putString("tittle", outcome.getTittle());
                                bundle.putString("description", outcome.getDescription());
                                bundle.putDouble("amount", outcome.getAmount());
                                Log.d("msg-test" , "cantidad0: " + outcome.getAmount());
                                long seconds = outcome.getDate().getSeconds();
                                int nanoseconds = outcome.getDate().getNanoseconds();
                                bundle.putLong("seconds", seconds);
                                bundle.putInt("nanoseconds", nanoseconds);

                                editOutcomeFragment.setArguments(bundle);
                                if (context instanceof MainActivity){
                                    ((MainActivity) context).replaceFragment(editOutcomeFragment);
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


    public class outcomeViewHolder extends RecyclerView.ViewHolder{
        TextView tittleItem, descriptionItem, amountItem, dateItem;
        Button btn1, btn2;
        public outcomeViewHolder(@NonNull View itemView) {
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
