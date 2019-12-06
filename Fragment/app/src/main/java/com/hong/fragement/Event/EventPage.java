package com.hong.fragement.Event;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hong.fragement.MovieObj;
import com.hong.fragement.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        DocumentReference documentReference
                = db.collection("Event")
                .document("wavveEvent");

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if (documentSnapshot.exists())
                {
                    data = documentSnapshot.toObject(EventInfo.class);
                    Log.d("TAG", "successFire");

                     data.setEventUrl(documentSnapshot.get("imageUri").toString());
                     //data.setWebUrl(documentSnapshot.get("webUri").toString());

                    eventInfoList.add(data);

                }
                mAdapterForEventPage = new AdapterForEventPage(eventInfoList, getActivity());
                recyclerView.setAdapter(mAdapterForEventPage);
            }
        });




    }
}
