package com.ringit.sdk.demo;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import classes.Preferabli;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<String> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private ShouldWeShowListener shouldWeShowListener;

    RecyclerViewAdapter(Context context, ShouldWeShowListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.shouldWeShowListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.textView.setText(animal);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if (shouldWeShowListener.shouldWeShow(getBindingAdapterPosition())) {
                menu.setHeaderTitle("Select Product Action");
                menu.add(getBindingAdapterPosition(), R.id.wtb, 0, "Where To Buy");//groupId, itemId, order, title
                if (Preferabli.isPreferabliUserLoggedIn() || Preferabli.isCustomerLoggedIn()) {
                    menu.add(getBindingAdapterPosition(), R.id.wishlist, 0, "Toggle Wishlist");
                    menu.add(getBindingAdapterPosition(), R.id.rate, 0, "Rate");
                }
                menu.add(getBindingAdapterPosition(), R.id.lttt, 0, "LTTT");
                if (Preferabli.isPreferabliUserLoggedIn() || Preferabli.isCustomerLoggedIn()) {
                    menu.add(getBindingAdapterPosition(), R.id.score, 0, "Get Preferabli Score");
                }
            }
        }
    }

    public void updateData(ArrayList<String> strings) {
        mData = strings;
        notifyDataSetChanged();
    }

    public interface ShouldWeShowListener {
        boolean shouldWeShow(int position);
    }
}