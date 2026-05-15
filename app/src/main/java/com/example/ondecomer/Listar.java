package com.example.ondecomer;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ondecomer.adapter.RestauranteAdapter;
import com.example.ondecomer.adapter.SwipeItem;
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
    ItemTouchHelper itemTouchHelper;

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
        configurarSwipe();

        botaoadd.setOnClickListener(new View.OnClickListener() {
            Restaurante restaurante;
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Listar.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(Listar.this)
                        .inflate(R.layout.bottom_sheet,null);
            bottomSheetDialog.setContentView(bottomSheetView);
            formulario(bottomSheetView, bottomSheetDialog, restaurante);
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

   public void configurarSwipe() {

    itemTouchHelper = new ItemTouchHelper(new SwipeItem(restauranreAdapter, new SwipeItem.OnSwipeListener() {
        @Override
        public void onItemSwipe(int position) {
            // Verifica se a posição é válida para evitar o crash
            if (position >= 0 && position < list.size()) {
                Restaurante res = list.get(position);
                int idParaDeletar = res.getId();

                new AlertDialog.Builder(Listar.this)
                    .setTitle("Deletar")
                    .setMessage("Excluir " + res.getNome() + "?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        deleteRestaurante(idParaDeletar); // Deleta no banco
                        restauranreAdapter.deleteItem(position); // Remove da tela
                    })
                    .setNegativeButton("Não", (dialog, which) -> {
                        restauranreAdapter.notifyItemChanged(position); // Volta o item
                    })
                    .setCancelable(false)
                    .show();
            }
        }
    }));
    itemTouchHelper.attachToRecyclerView(recyclerView);
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
        restauranreAdapter = new RestauranteAdapter(list, new RestauranteAdapter.OnItemClickListener() {
            @Override
            public void onclick(Restaurante restaurante) {
                Log.i("Click", restaurante.getNome());
                abrirBottomSheet(restaurante);
            }
        });
        recyclerView.setAdapter(restauranreAdapter);
    }
    private void abrirBottomSheet(Restaurante restaurante) {

        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(Listar.this,
                        R.style.BottomSheetDialogTheme);

        View bottomSheetView = LayoutInflater.from(Listar.this)
                .inflate(R.layout.bottom_sheet, null);

        bottomSheetDialog.setContentView(bottomSheetView);

        formulario(bottomSheetView, bottomSheetDialog, restaurante);

        bottomSheetDialog.show();
    }
    @SuppressLint("ResourceType")
    private void carregarRestaurantes() {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        String usuarioid = usuario.getUid();
        String categoria = getIntent().getStringExtra("card");
        ConstraintLayout empty = findViewById(R.id.empty);

        try (SQLiteDatabase db = openOrCreateDatabase("restaurante", MODE_PRIVATE, null)) {
            Cursor cursor = db.rawQuery(
                    "SELECT * FROM ondecomer WHERE usuarioId = ? AND categoria = ?",
                    new String[]{usuarioid, categoria}
            );

            list.clear(); // Limpa a lista global (a que o adapter usa)

            if (cursor.getCount() == 0) {
                empty.setVisibility(VISIBLE);
            } else {
                empty.setVisibility(GONE);
                while (cursor.moveToNext()) {
                    list.add(new Restaurante(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getFloat(4),
                            cursor.getString(3),
                            cursor.getString(5)
                    ));
                }
            }
            cursor.close();

            // avise o que já existe que a lista mudou.
            if (restauranreAdapter != null) {
                restauranreAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void formulario(View bottomSheetView, BottomSheetDialog bottomSheetDialog, Restaurante restaurante) {
        String card = getIntent().getStringExtra("card");
        Button botaoSalvar = bottomSheetView.findViewById(R.id.adicionar);
        TextView texto1 = bottomSheetView.findViewById(R.id.text1_sheet);
        TextView texto2 = bottomSheetView.findViewById(R.id.text2_sheet);
        TextInputEditText restauranteText = bottomSheetView.findViewById(R.id.nomeRestauranteImput);
        TextInputEditText resumoText = bottomSheetView.findViewById(R.id.resumoRestauranteInput);
        TextInputLayout nomeLayout = bottomSheetView.findViewById(R.id.nomeRestauranteImputLayout);
        RatingBar star = bottomSheetView.findViewById(R.id.ratingBar);
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        // Configura as cores baseadas na categoria
        if("quero_ir".equals(card)) {
            botaoSalvar.setBackgroundColor(getColor(R.color.laranja));
            texto1.setText("Quero ir");
            texto1.setTextColor(getColor(R.color.terracota));
        } else if("ja_fui".equals(card)) {
            botaoSalvar.setBackgroundColor(getColor(R.color.verde));
            texto1.setText("Já fui");
            texto1.setTextColor(getColor(R.color.verde));
        } else if("delivery".equals(card)) {
            botaoSalvar.setBackgroundColor(getColor(R.color.roxo_claro));
            texto1.setText("Delivery");
            texto1.setTextColor(getColor(R.color.roxo));
        }

        // SE O RESTAURANTE NÃO FOR NULL, SIGNIFICA QUE É UMA EDIÇÃO!
        // Preenchemos os campos imediatamente antes do usuário interagir.
        if (restaurante != null) {
            texto2.setText("Editar Restaurante");
            botaoSalvar.setText("Atualizar");

            // Coloca os dados atuais do restaurante nos inputs da tela
            restauranteText.setText(restaurante.getNome());
            resumoText.setText(restaurante.getResumo());
            star.setRating(restaurante.getNota());
        } else {
            // Se for nulo, é um novo cadastro
            texto2.setText("Novo Restaurante");
            botaoSalvar.setText("Salvar");
        }

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeDigitado = restauranteText.getText().toString();
                String resumoDigitado = resumoText.getText().toString();
                float notaSelecionada = star.getRating();
                String categoria = getIntent().getStringExtra("card");
                String usuarioId = usuario.getUid();

                if (nomeDigitado.isEmpty()) {
                    nomeLayout.setError("O nome do Restaurante é obrigatório");
                    return; // Para a execução aqui se estiver vazio
                }

                if (restaurante != null) {
                    restaurante.setNome(nomeDigitado);
                    restaurante.setResumo(resumoDigitado);
                    restaurante.setNota(notaSelecionada);

                    editarRestaurante(nomeDigitado, resumoDigitado, notaSelecionada, categoria, usuarioId, restaurante.getId());
                    // Atualiza a listagem
                    carregarRestaurantes();

                } else {
                    salvarRestaurante(nomeDigitado, resumoDigitado, notaSelecionada, categoria, usuarioId);
                }
                // Fecha o formulário após salvar ou atualizar
                bottomSheetDialog.dismiss();
            }
        });
    }
    private void salvarRestaurante(String nome, String resumo, float nota, String categoria, String usuarioId) {

        Restaurante restaurante = new Restaurante(0, nome, resumo, nota, categoria, usuarioId);

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
            carregarRestaurantes();
            //restauranreAdapter.addItem(restaurante);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editarRestaurante(String nome, String resumo, float nota, String categoria, String usuarioId, int id) {
        try {
            SQLiteDatabase db = openOrCreateDatabase("restaurante", MODE_PRIVATE, null);
            ContentValues values = new ContentValues();

            values.put("nome", nome);
            values.put("resumo", resumo);
            values.put("nota", nota);
            values.put("categoria", categoria);
            values.put("usuarioId", usuarioId);
            int resultado =
            db.update("ondecomer", values, "id = ?",new String[]{String.valueOf(id)});
            if(resultado > 0) {
                Toast.makeText(this, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
            }
            db.close();
            carregarRestaurantes();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

   private void deleteRestaurante(int id) {
       try {
           SQLiteDatabase db = openOrCreateDatabase("restaurante", MODE_PRIVATE, null);

           int resultado = db.delete(
                   "ondecomer",
                   "id = ?",
                   new String[]{String.valueOf(id)}
           );

           if(resultado > 0) {
               Toast.makeText(this, "Removido com sucesso!", Toast.LENGTH_SHORT).show();
           }

           db.close();
           // A atualização da tela agora é feita pelo restauranreAdapter.deleteItem(position)
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}