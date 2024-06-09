package com.iot.lab6.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iot.lab6.R;
import com.iot.lab6.databinding.ActivityNewIncomeBinding;
import com.iot.lab6.databinding.ActivityNewOutcomeBinding;
import com.iot.lab6.entity.Outcome;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class NewOutcomeActivity extends AppCompatActivity {

    ActivityNewOutcomeBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewOutcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.back.setOnClickListener(view -> {
            Intent intent = new Intent(NewOutcomeActivity.this, MainActivity.class);
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


            if (tittle.isEmpty() || amountText.isEmpty()){
                Toast.makeText(NewOutcomeActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            }else {
                Double amount = Double.parseDouble(amountText);
                BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
                amount = bd.doubleValue();

                Outcome outcome = new Outcome();
                outcome.setTittle(tittle);
                outcome.setAmount(amount);
                outcome.setDescription(description);
                //hora actual
                long currentTimeMillis = System.currentTimeMillis();
                Date currentDate = new Date(currentTimeMillis);
                Timestamp timestamp = new Timestamp(currentDate);
                outcome.setDate(timestamp);
                //id
                FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;
                String userid = user.getUid();
                outcome.setUserId(userid);

                //id con la fecha
                long seconds = timestamp.getSeconds();
                String timestampString = String.valueOf(seconds);

                db = FirebaseFirestore.getInstance();
                db.collection("outcome")
                        .document(timestampString)
                        .set(outcome)
                        .addOnSuccessListener(unused -> {
                            Log. d("msg-test" ,"Data guardada exitosamente ");
                        })
                        .addOnFailureListener(e -> e.printStackTrace());

                Intent intent = new Intent(NewOutcomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(this, "Egreso guardado", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }


        });


    }


}