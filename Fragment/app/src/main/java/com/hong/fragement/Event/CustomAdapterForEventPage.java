package com.hong.fragement.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hong.fragement.Event.EventInfo;
import com.hong.fragement.R;

import java.util.List;

public class CustomAdapterForEventPage extends BaseAdapter {

    List<EventInfo> mEventInfo;
    private Context mContext;

    public CustomAdapterForEventPage(List<EventInfo> mEventInfo, Context mContext) {
        this.mEventInfo = mEventInfo;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mEventInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mEventInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView ==null){

            LayoutInflater inflater = LayoutInflater.from(mContext);
            inflater.inflate(R.layout.each_event, parent,false);

        }
        EventInfo event = (EventInfo) getItem(position);

        ImageView OTTsiteLogoImage = (ImageView)convertView.findViewById(R.id.OTT_site_Logo_Image);
        TextView nameLogo = (TextView)convertView.findViewById(R.id.name_Logo);
        ImageView eventImage= (ImageView)convertView.findViewById(R.id.event_Image);

        OTTsiteLogoImage.setImageResource(event.getOTTsiteLogoImage());
        nameLogo.setText(event.getNameLogo());
        eventImage.setImageResource(event.getEventImage());


        return convertView;
    }
}
