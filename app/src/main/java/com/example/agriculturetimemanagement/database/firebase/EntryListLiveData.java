package com.example.agriculturetimemanagement.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.agriculturetimemanagement.database.entity.EntryEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EntryListLiveData extends LiveData<List<EntryEntity>> {
    private static final String TAG = "";

    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    public EntryListLiveData(DatabaseReference ref) { reference = ref; }

    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    protected void onInactive() {Log.d(TAG, "onInactive");}

    private class MyValueEventListener implements ValueEventListener {
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toEntryList(dataSnapshot));  //toEntryList ?!
        }

        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<EntryEntity> toEntryList(DataSnapshot snapshot) {
        List<EntryEntity> entries = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            EntryEntity entity = childSnapshot.getValue(EntryEntity.class);
            entity.setId(childSnapshot.getKey());
            entries.add(entity);
        }
        return entries;
    }
}
