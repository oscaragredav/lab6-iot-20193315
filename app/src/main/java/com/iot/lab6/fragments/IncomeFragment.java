package com.iot.lab6.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iot.lab6.NewIncomeActivity;
import com.iot.lab6.R;
import com.iot.lab6.databinding.FragmentIncomeBinding;


public class IncomeFragment extends Fragment {
    FragmentIncomeBinding binding;
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       binding = FragmentIncomeBinding.inflate(inflater,container,false);

       FloatingActionButton floatingAdd = binding.getRoot().findViewById(R.id.floatingAdd);
       floatingAdd.setOnClickListener(v -> {
           Intent intent = new Intent(getContext(), NewIncomeActivity.class); // Reemplaza con tu actividad
           startActivity(intent);
       });

       return binding.getRoot();
   }
}