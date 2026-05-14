package com.example.ondecomer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ondecomer.R;
import com.example.ondecomer.model.Restaurante;
import java.util.ArrayList;
import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.MyViewHolder> implements View.OnClickListener {
    private List<Restaurante> listaRestaurante;

    public RestauranteAdapter(List<Restaurante> lista) {
        this.listaRestaurante = lista;
    }

    @Override
    public void onClick(View v) {
        
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView restaurante, observacoes;
        RatingBar nota;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurante = itemView.findViewById(R.id.card_restaurante);
            observacoes = itemView.findViewById(R.id.card_comentario);
            nota = itemView.findViewById(R.id.starRatingBar);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View items = LayoutInflater.from(parent.getContext()).inflate(R.layout.conteudo_lista_quero_ir, parent, false);
        return new MyViewHolder(items);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Restaurante restaurante = listaRestaurante.get(position);
        holder.restaurante.setText(restaurante.getNome());
        holder.observacoes.setText(restaurante.getResumo());
        holder.nota.setRating(restaurante.getNota());
    }

    //quantidade de tarefas
    @Override
    public int getItemCount() {
        return this.listaRestaurante.size();
    }

    public void updateItem(int position, Restaurante restaurante) {
        this.listaRestaurante.set(position, restaurante);
        notifyItemChanged(position);
    }

    public void deleteItem(int position) {
        if (position >= 0 && position < listaRestaurante.size()) {
            listaRestaurante.remove(position);
            notifyItemRemoved(position);
            //notifyItemRangeChanged(position, listaRestaurante.size());
        }
    }
    
    //notifyItemRangeChanged(); alterado em sequencia
    public void addItem(Restaurante restaurante) {
        int nextIndex = listaRestaurante.toArray().length;
        listaRestaurante.add(restaurante);
        //notifyItemInserted();
        //notifyDataSetChanged(restaurante); atualiza o data set
    }
}
//notifyItemInserted(listaRestaurante.size()-1); //atualiza unico dado alterado