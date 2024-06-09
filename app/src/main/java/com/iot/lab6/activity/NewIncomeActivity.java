package com.iot.lab6.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iot.lab6.databinding.ActivityNewIncomeBinding;
import com.iot.lab6.entity.Income;

import java.util.Date;

public class NewIncomeActivity extends AppCompatActivity {

    ActivityNewIncomeBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewIncomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(view -> {
            Intent intent = new Intent(NewIncomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        binding.create.setOnClickListener(v -> {
            String tittle = binding.tittle.getEditText().getText().toString().trim();
            String description = binding.description.getEditText().getText().toString().trim();
            if (description.isEmpty()) {
                description = " ";
            }
            String amountText = binding.amount.getEditText().getText().toString().trim();
            Double amount = Double.parseDouble(amountText);


            Income income = new Income();
            income.setTittle(tittle);
            income.setAmount(amount);
            income.setDescription(description);
            //hora actual
            long currentTimeMillis = System.currentTimeMillis();
            Date currentDate = new Date(currentTimeMillis);
            Timestamp timestamp = new Timestamp(currentDate);
            income.setDate(timestamp);
            //id
            FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;
            String userid = user.getUid();
            income.setUserId(userid);

            //id con la fecha
            long seconds = timestamp.getSeconds();
            String timestampString = String.valueOf(seconds);

            db = FirebaseFirestore.getInstance();
            db.collection("income")
                    .document(timestampString)
                    .set(income)
                    .addOnSuccessListener(unused -> {
                        Log. d("msg-test" ,"Data guardada exitosamente ");
                    })
                    .addOnFailureListener(e -> e.printStackTrace());

            Intent intent = new Intent(NewIncomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Toast.makeText(this, "ingreso exitosamente", Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        });


    }




}