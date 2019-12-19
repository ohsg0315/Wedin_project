package com.hong.fragement.Event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hong.fragement.R;

import java.util.ArrayList;
import java.util.List;

public class EventPage extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private AdapterForEventPage mAdapterForEventPage;

    private List<EventInfo> eventInfoList;
    private EventInfo data;


    public EventPage() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eventpage,container,false);

        eventInfoList = new ArrayList<EventInfo>();

        recyclerView = view.findViewById(R.id.recyclerview_eventpage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        readDataForEvent();






        return view;




    }



    private void readDataForEvent()
    {
        data = new EventInfo();

        CollectionReference collectionReference
                = db.collection("Event");


        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        data = queryDocumentSnapshot.toObject(EventInfo.class);

                        data.setName(queryDocumentSnapshot.toObject(EventInfo.class).getName());


                       // data.setWebUrl(queryDocumentSnapshot.toObject(EventInfo.class).getWebUrl());
                        data.setWebUrl(queryDocumentSnapshot.get("webUri").toString());


                        data.setOTTsiteLogoImage(queryDocumentSnapshot.get("OTTsiteLogoImage").toString());
                        data.setImageUrl(queryDocumentSnapshot.get("imageUri").toString());
                        //data.setWebUrl(queryDocumentSnapshot.get("webUrl").toString());

                        eventInfoList.add(data);
                    }
                    mAdapterForEventPage = new AdapterForEventPage(eventInfoList, getActivity());
                    recyclerView.setAdapter(mAdapterForEventPage);
                }

            }
        });







    }
}
