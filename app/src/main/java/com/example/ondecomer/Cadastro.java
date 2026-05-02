package com.example.ondecomer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Cadastro extends AppCompatActivity {

    private TextInputEditText edit_nome, edit_email, edit_senha;
    private TextInputLayout nome_layout, email_layot, senha_layout;
    private Button cadastrar;
    String usuarioId;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        iniciarComponentes();

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()) {
                    cadastrarUsuario();
                }
            }

            private void cadastrarUsuario() {
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            SalvarDadosUsuario();

                            Toast.makeText(Cadastro.this, "Usuario cadastrado com sucesso", Toast.LENGTH_LONG).show();

                            firebaseAuth.signOut();
                            Intent intent1 = new Intent(Cadastro.this, Login.class);
                            startActivity(intent1);
                        }else {
                            String erro;
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e) {
                                erro = "Digite uma senha forte";
                            }catch (FirebaseAuthUserCollisionException e) {
                                erro = "Este email já foi cadastrado";
                            }catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "Email inválido";
                            }catch (Exception e) {
                                erro = "Erro ao cadastrar usuário";
                            }
                            Toast.makeText(Cadastro.this, erro, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void SalvarDadosUsuario() {
        String nome = edit_nome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Utilizando Map para criar uma chave e valor
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);

        //Obter o usuario atual e pega o id
        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioId);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar dados");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_error", "Falha ao salvar dados" + e.toString());
                    }
                });
    }

    private void iniciarComponentes() {
        edit_nome = findViewById(R.id.nomeInput);
        edit_email = findViewById(R.id.emailInput);
        edit_senha = findViewById(R.id.senhaInput);
        cadastrar = findViewById(R.id.bt_cadastrar);
        nome_layout = findViewById(R.id.nomeLayout);
        email_layot = findViewById(R.id.emailLayout);
        senha_layout = findViewById(R.id.senhaLayout);
    }

    private boolean validarCampos() {
        String nome = edit_nome.getText().toString();
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        if (!nome.isEmpty()) {
            nome_layout.setError(null);

            if (!email.isEmpty()) {
                email_layot.setError(null);

                if(!senha.isEmpty()) {
                    senha_layout.setError(null);
                    return true;

                }else{
                    senha_layout.setError("Coloque uma senha");
                    return false;
                }

            }else{
                email_layot.setError("Preencha seu email");
                return false;
            }

        }else {
            nome_layout.setError("Preencha seu nome");
            return false;
        }
    }
}