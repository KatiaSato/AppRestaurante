package com.example.ondecomer;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ondecomer.adapter.RestauranteAdapter;
import com.example.ondecomer.model.Restaurante;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Listar extends AppCompatActivity {
    private SQLiteDatabase dataset;
    private FloatingActionButton botaoadd;
    private RecyclerView recyclerView;
    private RestauranteAdapter restauranreAdapter;
    private List<Restaurante> list = new ArrayList<>();

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

        //layout manager, adapter, dataset

        iniciarToolbar();
        iniciarComponentes();
        criarBancoDados();

        botaoadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Listar.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(Listar.this)
                        .inflate(R.layout.bottom_sheet,null);
            bottomSheetDialog.setContentView(bottomSheetView);
            formulario(bottomSheetView, bottomSheetDialog);
            bottomSheetDialog.show();
            }
        });
    }
    protected void onStart() {
        carregarDados();
        carregarRestaurantes();
        super.onStart();
    }
    private void iniciarComponentes() {
        botaoadd=findViewById(R.id.add);
        recyclerView = findViewById(R.id.recyclerview);
    }
    public void iniciarToolbar() {
        String card = getIntent().getStringExtra("card");
        MaterialToolbar toolbar = findViewById(R.id.toolbarLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if("quero_ir".equals(card)) {
            getSupportActionBar().setTitle("Lista de desejos");
            toolbar.setSubtitle("Restaurantes que quero ir");
            toolbar.setBackground(getDrawable(R.drawable.queroir_gradiente));
        }
        else if("ja_fui".equals(card)) {
            getSupportActionBar().setTitle("Memorias");
            toolbar.setSubtitle("Lugares que já fui");
            toolbar.setBackground(getDrawable(R.drawable.ja_fui_gradiente));
        }
        else if("delivery".equals(card)) {
            getSupportActionBar().setTitle("Direto na porta");
            toolbar.setSubtitle("Ja pedi no Delivery");
            toolbar.setBackground(getDrawable(R.drawable.delivery_gradiente));
        }
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
    public void carregarDados() {
        //configurar adapter
        //acessa as configuracoes do layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        //fixa o tamanho da RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        restauranreAdapter = new RestauranteAdapter(list);
        recyclerView.setAdapter(restauranreAdapter);
    }

    @SuppressLint("ResourceType")
    private void carregarRestaurantes() {
        //abre banco
        //faz query
        //percorre cursor
        //cria objetos
        //adiciona na lista
        //conecta adapter
        ConstraintLayout empty;
        empty= findViewById(R.id.empty);
        SQLiteDatabase db = null;
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        String usuarioid = usuario.getUid();
        String categoria = getIntent().getStringExtra("card");
        try {
            db = openOrCreateDatabase(
                    "restaurante",
                    MODE_PRIVATE,
                    null
            );

            Cursor cursor = db.rawQuery(
                    "SELECT * FROM ondecomer WHERE usuarioId = ? AND categoria = ?",
                    new String[]{usuarioid, categoria}
            );

            if(cursor.getCount()==0) {

                empty.setVisibility(VISIBLE);
            }else {

                empty.setVisibility(GONE);

            ArrayList<Restaurante> listaRestaurantes = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String nome = cursor.getString(1);
                String resumo = cursor.getString(2);
                String categoriaBanco = cursor.getString(3);
                float nota = cursor.getFloat(4);
                String usuarioId = cursor.getString(5);

                Restaurante restaurante = new Restaurante(
                        id,
                        nome,
                        resumo,
                        nota,
                        categoriaBanco,
                        usuarioId
                );
                listaRestaurantes.add(restaurante);

                RestauranteAdapter adapter = new RestauranteAdapter(listaRestaurantes);

                recyclerView.setLayoutManager(new LinearLayoutManager(this)
                );

                recyclerView.setAdapter(adapter);
                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        db.close();

    }
    
    private void formulario(View bottomSheetView, BottomSheetDialog bottomSheetDialog) {
        String card = getIntent().getStringExtra("card");
        Button botaoSalvar = bottomSheetView.findViewById(R.id.adicionar);
        TextView texto1 = bottomSheetView.findViewById(R.id.text1_sheet);
        TextView texto2 = bottomSheetView.findViewById(R.id.text2_sheet);
        TextInputEditText restauranteText = bottomSheetView.findViewById(R.id.nomeRestauranteImput);
        TextInputEditText resumoText = bottomSheetView.findViewById(R.id.resumoRestauranteInput);
        TextInputLayout nomeLayout = bottomSheetView.findViewById(R.id.nomeRestauranteImputLayout);
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

                if(nome.isEmpty()){
                    nomeLayout.setError("O nome do Restaurante é obrigatorio");
                }else {
                    salvarRestaurante(nome, resumo, nota, categoria, usuarioId);
                    bottomSheetDialog.dismiss();
                }
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

                Toast.makeText(Listar.this,
                        "Salvo com sucesso", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(Listar.this,
                        "Erro ao salvar", Toast.LENGTH_SHORT).show();
            }
            db.close();


        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    private void editarRestaurante() {

    }

    private void deletarRestaurante() {

    }



}