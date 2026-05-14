package com.example.ondecomer.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeItem extends ItemTouchHelper.SimpleCallback {
    RestauranteAdapter restauranteAdapter;
    private final OnSwipeListener listener;
    public SwipeItem(RestauranteAdapter restauranteAdapter, OnSwipeListener listener) {
        super(0, ItemTouchHelper.RIGHT);
        this.restauranteAdapter = restauranteAdapter;
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAbsoluteAdapterPosition();
        listener.onItemSwipe(position);
    }

    public interface OnSwipeListener {
        void onItemSwipe(int position);
    }
}
