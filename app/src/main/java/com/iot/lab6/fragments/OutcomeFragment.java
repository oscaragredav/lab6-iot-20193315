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
import com.iot.lab6.R;
import com.iot.lab6.activity.NewIncomeActivity;
import com.iot.lab6.activity.NewOutcomeActivity;
import com.iot.lab6.adapter.IncomeAdapter;
import com.iot.lab6.adapter.OutcomeAdapter;
import com.iot.lab6.databinding.FragmentOutcomeBinding;
import com.iot.lab6.entity.Income;
import com.iot.lab6.entity.Outcome;

import java.util.ArrayList;
import java.util.List;


public class OutcomeFragment extends Fragment {

    FragmentOutcomeBinding binding;
    List<Outcome> list;
    OutcomeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOutcomeBinding.inflate(inflater,container,false);
        list = new ArrayList<>();
        adapter = new OutcomeAdapter(getContext(),list);
        binding.outcomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.outcomeRecyclerView.setAdapter(adapter);
        //llamado a bd
        FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("outcome")
                .whereEqualTo("userId", userId)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Outcome outcome = document.toObject(Outcome.class);
                            if (outcome != null){
                                outcome.setUserId(document.getId());
                                list.add(outcome);
                            }
                        }
                        Log.d("msg-test", "Se mandó la lista");
                        adapter.notifyDataSetChanged();
                    }else {
                        Log.e("msg-test","Error getting documents: ", task.getException());
                    }
                });

        //botón flotante para ir a Nuevo egreso
        FloatingActionButton floatingAdd = binding.getRoot().findViewById(R.id.floatingAdd1);
        floatingAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NewOutcomeActivity.class); // Reemplaza con tu actividad
            startActivity(intent);
        });


        return binding.getRoot();
    }
}