package com.hong.fragement.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hong.fragement.R;

import java.util.List;

public class AdapterForEventPage extends RecyclerView.Adapter<AdapterForEventPage.EventViewHolder>
{

    private List<EventInfo> list;
    Context context;

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
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForEventPage.EventViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView OTTsiteLogoImage;
        protected TextView nameLogo;
        protected ImageView eventImage;


        public EventViewHolder(@NonNull View itemView)
        {
            super(itemView);
            OTTsiteLogoImage = (ImageView)itemView.findViewById(R.id.OTT_site_Logo_Image);
            nameLogo = (TextView)itemView.findViewById(R.id.name_Logo);
            eventImage = (ImageView)itemView.findViewById(R.id.event_Image);

        }
    }
}
