package com.example.ondecomer;

import android.content.Intent;
import android.media.RouteListingPreference;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    public static Object View;
    private  CardView queroIr, jaFui, delivery;
    private FirebaseAuth firebaseAuth;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar();
        Toolbar toolbarMain = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarMain);

        queroIr = findViewById(R.id.card1);
        jaFui = findViewById(R.id.card2);
        delivery = findViewById(R.id.card3);

        queroIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, Listar.class);
                intent1.putExtra("card","quero_ir");

                startActivity(intent1);
            }
        });
        jaFui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Listar.class);
                intent.putExtra("card", "ja_fui");

                startActivity(intent);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Listar.class);
                intent.putExtra("card", "delivery");

                startActivity(intent);
            }
        });
    }

    //funcao cria o menu de opcoes e infla o menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //funcao que faz o item do menu ser seleciona e faz a acao dele
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.id_logout) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Deslogado com sucesso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

    }
}