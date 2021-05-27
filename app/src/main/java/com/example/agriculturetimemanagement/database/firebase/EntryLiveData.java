package com.example.agriculturetimemanagement.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.agriculturetimemanagement.database.entity.EntryEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class EntryLiveData extends LiveData<EntryEntity> {

    private static final String TAG = "EntryLiveData";

    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    public EntryLiveData(DatabaseReference ref){ reference = ref; }

    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    protected void onInactive() {Log.d(TAG, "onInactive");}

    private class MyValueEventListener implements ValueEventListener {
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                EntryEntity entity = dataSnapshot.getValue(EntryEntity.class);
                entity.setId(dataSnapshot.getKey());
                setValue(entity);
            }
        }

        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
