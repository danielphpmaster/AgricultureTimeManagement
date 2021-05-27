package com.example.agriculturetimemanagement.database.repository;

import androidx.lifecycle.LiveData;

import com.example.agriculturetimemanagement.database.entity.EntryEntity;
import com.example.agriculturetimemanagement.database.firebase.EntryListLiveData;
import com.example.agriculturetimemanagement.database.firebase.EntryLiveData;
import com.example.agriculturetimemanagement.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EntryRepository {
    private static EntryRepository instance;

    public static EntryRepository getInstance() {
        if(instance == null) {
            synchronized (EntryEntity.class) {
                if(instance == null) {
                    instance = new EntryRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<EntryEntity> getEntry(final String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("entry").child(id);
        return new EntryLiveData(reference);
    }

    public LiveData<List<EntryEntity>> getAllEntries() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("entry");
        return new EntryListLiveData(reference);
    }

    public void insert(final EntryEntity entry, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("entry").push().getKey();
        FirebaseDatabase.getInstance().getReference("entry").child(id).setValue(entry, (databaseError, databaseReference) -> {
            if(databaseError != null) {
                callback.onFailure(databaseError.toException());
            } else {
                callback.onSuccess();
            }
        });
    }

    public void update(final EntryEntity entry, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance().getReference("entry").child(entry.getId()).updateChildren(entry.toMap(), (databaseError, databaseReference) -> {
            if(databaseError != null) {
                callback.onFailure(databaseError.toException());
            } else {
                callback.onSuccess();
            }
        });
    }

    public void delete(final EntryEntity entry, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance().getReference("entry").child(entry.getId()).removeValue((databaseError, databaseReference) -> {
            if(databaseError != null) {
                callback.onFailure(databaseError.toException());
            } else {
                callback.onSuccess();
            }
        });
    }
}
