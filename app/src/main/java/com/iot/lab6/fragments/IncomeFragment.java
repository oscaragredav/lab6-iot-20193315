package com.iot.lab6.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.iot.lab6.activity.NewIncomeActivity;
import com.iot.lab6.R;
import com.iot.lab6.adapter.IncomeAdapter;
import com.iot.lab6.databinding.FragmentIncomeBinding;
import com.iot.lab6.entity.Income;

import java.util.ArrayList;
import java.util.List;


public class IncomeFragment extends Fragment {

    FragmentIncomeBinding binding;
   List<Income> list;
   IncomeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       binding = FragmentIncomeBinding.inflate(inflater,container,false);
       list = new ArrayList<>();
       adapter = new IncomeAdapter(getContext(),list);
       binding.incomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       binding.incomeRecyclerView.setAdapter(adapter);
       //llamado a bd
       FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;
       String userId = user.getUid();
       FirebaseFirestore db = FirebaseFirestore.getInstance();
       db.collection("income")
               .whereEqualTo("userId", userId)
               .orderBy("date", Query.Direction.DESCENDING)
               .get()
               .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document : task.getResult()){
                           Income income = document.toObject(Income.class);
                           if (income != null){
                               income.setUserId(document.getId());
                               list.add(income);
                           }
                       }
                       Log.d("msg-test", "Se mandó la lista");
                       adapter.notifyDataSetChanged();
                   }else {
                       Log.e("msg-test","Error getting documents: ", task.getException());
                   }
               });

        //botón flotante para ir a Nuevo Ingreso
        FloatingActionButton floatingAdd = binding.getRoot().findViewById(R.id.floatingAdd);
        floatingAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NewIncomeActivity.class); // Reemplaza con tu actividad
            startActivity(intent);
        });


       return binding.getRoot();
   }
}