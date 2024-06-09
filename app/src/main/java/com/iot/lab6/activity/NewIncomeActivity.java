package com.iot.lab6.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iot.lab6.databinding.ActivityNewIncomeBinding;
import com.iot.lab6.entity.Income;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class NewIncomeActivity extends AppCompatActivity {

    ActivityNewIncomeBinding binding;
    FirebaseFirestore db;
    String dateCalendar;
    TextView tvCalendar;
    Timestamp timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewIncomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvCalendar = binding.tvCalendar;

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


            if (tittle.isEmpty() || amountText.isEmpty() ||  dateCalendar == null || dateCalendar.isEmpty()){
                Toast.makeText(NewIncomeActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            }else {
                Double amount = Double.parseDouble(amountText);
                BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
                amount = bd.doubleValue();

                Income income = new Income();
                income.setTittle(tittle);
                income.setAmount(amount);
                income.setDescription(description);
                //hora actual
                long currentTimeMillis = System.currentTimeMillis();
                Date currentDate = new Date(currentTimeMillis);
                Timestamp timestampCurrent = new Timestamp(currentDate);
                income.setDate(timestamp);
                //id
                FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;
                String userid = user.getUid();
                income.setUserId(userid);

                //id con la fecha
                long seconds = timestampCurrent.getSeconds();
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
                Toast.makeText(this, "Ingreso guardado", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }


        });
    }

    public void openCalendar (View view){
        Calendar calendar = Calendar.getInstance();
        int yearCalendar = calendar.get(Calendar.YEAR);
        int monthCalendar = calendar.get(Calendar.MONTH);
        int dayCalendar = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(NewIncomeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateCalendar = dayOfMonth + "/" + (month+1) + "/" + year;
                tvCalendar.setText(dateCalendar);
                tvCalendar.setVisibility(View.VISIBLE);

                // Convertir la fecha seleccionada a un objeto Date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                Date date = selectedDate.getTime();

                // Convertir el objeto Date a un Timestamp
                timestamp = new Timestamp(date);
            }
        }, yearCalendar, monthCalendar, dayCalendar);
        datePickerDialog.show();
    }
}