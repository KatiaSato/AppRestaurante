package com.example.ondecomer.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeItem extends ItemTouchHelper.SimpleCallback {
    RestauranteAdapter restauranteAdapter;
    public SwipeItem(RestauranteAdapter restauranteAdapter) {
        super(0, ItemTouchHelper.RIGHT);
        this.restauranteAdapter = restauranteAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAbsoluteAdapterPosition();
        this.restauranteAdapter.deleteItem(position);
    }
}
