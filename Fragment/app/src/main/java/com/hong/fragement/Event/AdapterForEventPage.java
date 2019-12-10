package com.hong.fragement.Event;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hong.fragement.R;

import java.util.List;

public class AdapterForEventPage extends RecyclerView.Adapter<AdapterForEventPage.EventViewHolder>
{

    private List<EventInfo> list;
    Context context;
    static private Uri uri;

    public AdapterForEventPage(List<EventInfo> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterForEventPage.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_event,parent,false);

        EventViewHolder eventViewHolder = new EventViewHolder(view);
        context=parent.getContext();


        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterForEventPage.EventViewHolder holder, final int position)
    {
        Glide.with(holder.itemView)
                .load(list.get(position).getImageUrl())
                .into(holder.eventImage);

        Glide.with(holder.itemView)
                .load(list.get(position).getOTTsiteLogoImage())
                .into(holder.OTTsiteLogoImage);
        holder.name.setText(list.get(position).getName());






    }

    @Override
    public int getItemCount() {

        return (list != null ? list.size() : 0);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView OTTsiteLogoImage;
        protected TextView name;
        protected ImageView eventImage;
        protected Button webUri;



        public EventViewHolder(@NonNull View itemView)
        {
            super(itemView);
            OTTsiteLogoImage = (ImageView)itemView.findViewById(R.id.OTT_site_Logo_Image);
            name = (TextView)itemView.findViewById(R.id.name_Logo);
            eventImage = (ImageView)itemView.findViewById(R.id.event_Image);
            webUri = (Button)itemView.findViewById(R.id.weburi_button);

            webUri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    uri = Uri.parse(list.get(position).getWebUrl());

                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    context.startActivity(intent);



                }
            });




        }
    }
}
