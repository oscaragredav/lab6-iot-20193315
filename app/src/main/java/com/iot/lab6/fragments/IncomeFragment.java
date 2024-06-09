package com.iot.lab6.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.iot.lab6.activity.NewIncomeActivity;
import com.iot.lab6.R;
import com.iot.lab6.databinding.FragmentIncomeBinding;
import com.iot.lab6.entity.Income;


public class IncomeFragment extends Fragment {
    FragmentIncomeBinding binding;
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       binding = FragmentIncomeBinding.inflate(inflater,container,false);

       //botón flotante para ir a Nuevo Ingreso
       FloatingActionButton floatingAdd = binding.getRoot().findViewById(R.id.floatingAdd);
       floatingAdd.setOnClickListener(v -> {
           Intent intent = new Intent(getContext(), NewIncomeActivity.class); // Reemplaza con tu actividad
           startActivity(intent);
       });

       FirebaseFirestore db = FirebaseFirestore.getInstance();
       db.collection("income")
               .get()
               .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document : task.getResult()){
                           Income income = document.toObject(Income.class);
                       }
                   }else {
                       Log.e("msg-test","Error getting documents: ", task.getException());
                   }
               });


       return binding.getRoot();
   }
}