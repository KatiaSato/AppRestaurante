package com.example.ondecomer;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class List extends AppCompatActivity {

    private SQLiteDatabase dataSet;
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

        criarBancoDados();
        botaoadd=findViewById(R.id.add);

        botaoadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(List.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.bottom_sheet, (RelativeLayout)findViewById(R.id.bottom_sheet_container)
                );
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
            }
        });
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