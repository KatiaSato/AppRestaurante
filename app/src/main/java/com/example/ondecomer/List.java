package com.example.ondecomer;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class List extends AppCompatActivity {

    private SQLiteDatabase dataSet;
    private FloatingActionButton botaoadd;
    private Button botaoSalvar;
    private TextView texto1, texto2;
    private TextInputEditText restauranteText, resumoText;
    private TextInputLayout restauranteLayout, resumoLayout;
    private RatingBar star;
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
        iniciarComponentes();
        criarBancoDados();


        botaoadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(List.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(List.this)
                        .inflate(R.layout.bottom_sheet,null);
            bottomSheetDialog.setContentView(bottomSheetView);
            formulario(bottomSheetView);
            bottomSheetDialog.show();
            }

        });
    }


    private void formulario(View bottomSheetView) {
        String card = getIntent().getStringExtra("card");
        Button botaoSalvar =
                bottomSheetView.findViewById(R.id.adicionar);

        TextView texto1 =
                bottomSheetView.findViewById(R.id.text1_sheet);

        TextView texto2 =
                bottomSheetView.findViewById(R.id.text2_sheet);


        if("quero_ir".equals(card)) {
            botaoSalvar.setBackgroundColor(getColor(R.color.laranja));
            texto1.setText("Quero ir");
            texto1.setTextColor(getColor(R.color.terracota));
            texto2.setText("Novo Restaurante");
        }
        else if("ja_fui".equals(card)) {
            botaoSalvar.setBackgroundColor(getColor(R.color.verde));
            texto1.setText("Já fui");
            texto1.setTextColor(getColor(R.color.verde));
            texto2.setText("Novo Restaurante");
        }
        else if("delivery".equals(card)) {
            botaoSalvar.setBackgroundColor(getColor(R.color.roxo_claro));
            texto1.setText("Delivery");
            texto1.setTextColor(getColor(R.color.roxo));
            texto2.setText("Novo Restaurante");
        }


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
    private void iniciarComponentes() {
        botaoadd=findViewById(R.id.add);
        botaoSalvar = findViewById(R.id.adicionar);
        texto1 = findViewById(R.id.text1_sheet);
        texto2 = findViewById(R.id.text2_sheet);
        restauranteText = findViewById(R.id.nomeRestauranteImput);
        resumoText = findViewById(R.id.resumoRestauranteInput);
        restauranteLayout = findViewById(R.id.nomeRestauranteImputLayout);
        resumoLayout = findViewById(R.id.resumoImputLayout);
        star = findViewById(R.id.ratingBar);
        toolbar = findViewById(R.id.toolbarLayout);
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