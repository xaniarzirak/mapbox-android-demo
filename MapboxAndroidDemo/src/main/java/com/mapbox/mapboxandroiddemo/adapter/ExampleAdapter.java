package com.mapbox.mapboxandroiddemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapbox.mapboxandroiddemo.MainActivity;
import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxandroiddemo.model.ExampleItemModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ExampleItemModel> dataSource;
    private Context mContext;

    public ExampleAdapter(Context context, List<ExampleItemModel> dataSource) {
        this.dataSource = dataSource;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

            return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ExampleItemModel detailItem = dataSource.get(position);
                ViewHolder viewHolder = (ViewHolder) holder;

                String imageUrl = mContext.getString(detailItem.getImageUrl());

                /*if (!imageUrl.isEmpty()) {
                    Picasso.with(mContext)
                            .load(new File(imageUrl))
                            .into(viewHolder.imageView);
                } else {
                    viewHolder.imageView.setImageDrawable(null);
                }*/

                try
                {
                    // get input stream
                    InputStream ims = mContext.getAssets().open(imageUrl);
                    // load image as Drawable
                    Drawable d = Drawable.createFromStream(ims, null);
                    // set image to ImageView
                    viewHolder.imageView.setImageDrawable(d);
                }
                catch(IOException ex)
                {
                    return;
                }

                if(detailItem.getShowNewIcon()){
                    viewHolder.newIconImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.new_icon));
                }else{
                    viewHolder.newIconImageView.setImageDrawable(null);
                }

                viewHolder.titleTextView.setText(mContext.getString(detailItem.getTitle()));
                viewHolder.descriptionTextView.setText(mContext.getString(detailItem.getDescription()));
        }
    }

    @Override
    public int getItemCount() {
        return (null != dataSource ? dataSource.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView descriptionTextView;
        public ImageView imageView;
        public ImageView newIconImageView;

        public ViewHolder(final View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.example_image);
            titleTextView = (TextView) itemView.findViewById(R.id.example_title);
            descriptionTextView = (TextView) itemView.findViewById(R.id.example_description);
            newIconImageView = (ImageView) itemView.findViewById(R.id.new_icon_image_view);
        }
    }

    public static class ViewHolderDescription extends RecyclerView.ViewHolder {

        public ViewHolderDescription(final View itemView) {
            super(itemView);
        }
    }
}