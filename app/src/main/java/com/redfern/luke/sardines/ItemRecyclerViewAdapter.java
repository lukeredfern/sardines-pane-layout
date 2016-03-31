package com.redfern.luke.sardines;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;

/**
 * Created by luke on 22/03/16.
 */
public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.CustomViewHolder> {
    private ArrayList<Item> itemList;
    private Context mContext;

    public ItemRecyclerViewAdapter(Context context, ArrayList<Item> itemList) {
        this.itemList = itemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder vh, int i) {
        Item item = itemList.get(i);

        vh.description.setText(item.getDescription());

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(item.getQuantity()), MaterialColor.getColor(item.getColor(),7));
        vh.imageCircle.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView description;
        ImageView imageCircle;

        public CustomViewHolder(View view) {
            super(view);

            // description
            this.description = (TextView) view.findViewById(R.id.description);

            // quantity circle
            TextDrawable drawable = TextDrawable.builder()
                    .buildRect("A", Color.RED);

            this.imageCircle = (ImageView) view.findViewById(R.id.image_view);
            imageCircle.setImageDrawable(drawable);
        }
    }
}