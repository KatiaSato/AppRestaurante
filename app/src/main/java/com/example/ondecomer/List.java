package com.example.ondecomer;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class List extends AppCompatActivity {
    private SQLiteDatabase dataset;
    private FloatingActionButton botaoadd;

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
            formulario(bottomSheetView, bottomSheetDialog);
            bottomSheetDialog.show();
            }
        });
    }

    private void formulario(View bottomSheetView, BottomSheetDialog bottomSheetDialog) {
        String card = getIntent().getStringExtra("card");
        Button botaoSalvar = bottomSheetView.findViewById(R.id.adicionar);
        TextView texto1 = bottomSheetView.findViewById(R.id.text1_sheet);
        TextView texto2 = bottomSheetView.findViewById(R.id.text2_sheet);
        TextInputEditText restauranteText = bottomSheetView.findViewById(R.id.nomeRestauranteImput);
        TextInputEditText resumoText = bottomSheetView.findViewById(R.id.resumoRestauranteInput);
        RatingBar star = bottomSheetView.findViewById(R.id.ratingBar);
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = restauranteText.getText().toString();
                String resumo = resumoText.getText().toString();
                float nota = star.getRating();
                String categoria = getIntent().getStringExtra("card");
                String usuarioId = usuario.getUid();

                salvarRestaurante(nome, resumo, nota, categoria, usuarioId);
                bottomSheetDialog.dismiss();
            }
        });

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
    private void salvarRestaurante(String nome, String resumo, float nota, String categoria, String usuarioId) {
        try {
            SQLiteDatabase db = openOrCreateDatabase(
                    "restaurante",
                    MODE_PRIVATE,
                    null
            );
            ContentValues values = new ContentValues();

            values.put("nome", nome);
            values.put("resumo", resumo);
            values.put("nota", nota);
            values.put("categoria", categoria);
            values.put("usuarioId", usuarioId);

            long resultado = db.insert("ondecomer", null, values);
            if (resultado != -1) {

                Toast.makeText(List.this,
                        "Salvo com sucesso", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(List.this,
                        "Erro ao salvar", Toast.LENGTH_SHORT).show();
            }
            db.close();


        } catch (Exception e) {

            e.printStackTrace();
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
    }
    //Funcao para criar o banco de dados
    public void criarBancoDados(){

        try {
            dataset = openOrCreateDatabase("restaurante", MODE_PRIVATE, null);
            dataset.execSQL("CREATE TABLE IF NOT EXISTS ondecomer (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", nome TEXT" +
                    ", resumo TEXT" +
                    ", categoria TEXT" +
                    ", nota REAL" +
                    ", usuarioId TEXT)");
            dataset.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}