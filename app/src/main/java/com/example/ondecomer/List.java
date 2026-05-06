package com.example.ondecomer;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class List extends AppCompatActivity {

    private SQLiteDatabase dataSet;
    private FloatingActionButton botaoadd;
    private MaterialToolbar toolbar;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_quero_ir);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        iniciarToolbar();
        criarBancoDados();
        botaoadd=findViewById(R.id.add);
        toolbar=findViewById(R.id.toolbarLayout);

        botaoadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(List.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(List.this)
                        .inflate(R.layout.bottom_sheet,null);
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
            }

        });
    }

    public void iniciarToolbar() {
        String card = getIntent().getStringExtra("card");
        MaterialToolbar toolbar = findViewById(R.id.toolbarLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if("quero_ir".equals(card)) {
            toolbar.setTitle("Lista de desejos");
            toolbar.setSubtitle("Restaurantes que quero ir");
            toolbar.setBackground(getDrawable(R.drawable.queroir_gradiente));
        }
        else if("ja_fui".equals(card)) {
            toolbar.setTitle("Memorias");
            toolbar.setSubtitle("Lugares que já fui");
            toolbar.setBackground(getDrawable(R.drawable.ja_fui_gradiente));

        }
        else if("delivery".equals(card)) {
            toolbar.setTitle("Direto na porta");
            toolbar.setTitle("Ja pedi no Delivery");
            toolbar.setBackground(getDrawable(R.drawable.delivery_gradiente));
        }

    }

    //Funcao para criar o banco de dados
    public void criarBancoDados(){
        try {
            dataSet = openOrCreateDatabase("restaurante", MODE_PRIVATE, null);
            dataSet.execSQL("CREATE TABLE IF NOT EXISTS ondecomer (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", nome VARCHAR" +
                    ", observacoes VARCHAR" +
                    ", categoria VARCHAR)" );
            dataSet.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}