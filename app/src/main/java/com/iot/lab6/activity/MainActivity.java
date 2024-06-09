package com.iot.lab6.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iot.lab6.R;
import com.iot.lab6.databinding.ActivityMainBinding;
import com.iot.lab6.fragments.GraphicsFragment;
import com.iot.lab6.fragments.IncomeFragment;
import com.iot.lab6.fragments.OutcomeFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;
        String userName = user.getDisplayName();

        //fragmentos
        replaceFragment(new IncomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.income){
                replaceFragment(new IncomeFragment());
            } else if (menuItem.getItemId() == R.id.outcome) {
                replaceFragment(new OutcomeFragment());
            } else if (menuItem.getItemId() == R.id.graphics) {
                replaceFragment(new GraphicsFragment());
            }
            return true;
        });

    }
    private  void  replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


    //menú cierre de sesión
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return logOutActionBar(item);
    }

    public boolean logOutActionBar(MenuItem item){
        if (item.getItemId() == R.id.logOut) {
            Toast.makeText(this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
            AuthUI.getInstance().signOut(MainActivity.this)
                    .addOnCompleteListener(task -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}