package com.example.ondecomer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private TextView cadastro;
    private TextInputLayout email_layout, password_layout;
    private TextInputEditText email_login, password_login;
    private Button login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iniciarComponentes();


        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validarCampos()) {
                    autenticarUsuario();
                }
            }
        });
    }

    private void autenticarUsuario() {
        String email = email_login.getText().toString();
        String password = password_login.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    Toast.makeText(Login.this, "Usuario logado com sucesso", Toast.LENGTH_LONG).show();
                    telaPrincipal();

                }else {
                    String erro;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) {
                        erro = "Usuario nao cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email inválido ou senha inválido";
                    }catch (Exception e) {
                        erro = "Erro ao cadastrar usuário";
                    }
                    Toast.makeText(Login.this, erro, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void telaPrincipal() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void iniciarComponentes() {
        email_login = findViewById(R.id.emailLogin);
        password_login = findViewById(R.id.passawordLogin);
        email_layout = findViewById(R.id.emailLoginLayout);
        password_layout = findViewById(R.id.passawordLoginLayout);
        login = findViewById(R.id.bt_entrar);
        cadastro = findViewById(R.id.cadastrar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null) {
            telaPrincipal();
        }
    }

    private boolean validarCampos() {
        String email = email_login.getText().toString();
        String senha = password_login.getText().toString();

            if (!email.isEmpty()) {
                email_layout.setError(null);

                if(!senha.isEmpty()) {
                    password_layout.setError(null);
                    return true;

                }else{
                    password_layout.setError("Coloque uma senha");
                    return false;
                }

            }else{
                email_layout.setError("Preencha seu email");
                return false;
            }
    }
}