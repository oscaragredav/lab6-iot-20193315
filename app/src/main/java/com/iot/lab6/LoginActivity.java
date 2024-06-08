package com.iot.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iot.lab6.databinding.ActivityLoginBinding;

import java.lang.reflect.Array;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //validación user logueado
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() != null ){
            //está logueado
            Log.d("msg-test", "se conectó: "+ firebaseUser.getUid());
            goToMainActivity();
        }
        //config de auentificación
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        //autentificación con email y google
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                ))
                .setIsSmartLockEnabled(false)
                .build();
        signInLauncher.launch(intent);

    }

    ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract() ,
            result -> {
                if (result.getResultCode() == RESULT_OK) { //si sí se logueó
                    FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;

                    //verificar si existe el usuario
                    if(user != null){
                        Log.d("msg-test" , "Firebase uid: " + user.getUid());
                        Log.d("msg-test" , "Firebase name: " + user.getDisplayName());

                        //validar el mail
                        user.reload().addOnCompleteListener(task -> {
                            if (user.isEmailVerified()){
                                Log.d("msg-test" , "email validado" );
                                goToMainActivity();
                            }else {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(taskSendMail -> {
                                            Log.d("msg-test" , "mail de validación mandado" );
                                            Toast.makeText(LoginActivity.this, "Por favor, revise su correo y valide su cuenta", Toast.LENGTH_LONG).show();
                                        });
                            }
                        });

                    }else {
                        Log.e("msg-test" , "usuario nulo" );
                        recreate();
                    }

                } else {
                    Log.d("msg-test" , "Canceló el Log-in" ); //si canceló el logueo
                }
                binding.loginBtn.setEnabled(true);
            }
    );

    public void goToMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}