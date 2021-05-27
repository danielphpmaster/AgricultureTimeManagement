package com.example.agriculturetimemanagement.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.agriculturetimemanagement.database.entity.EntryEntity;
import com.example.agriculturetimemanagement.database.repository.EntryRepository;

public class EntryViewModel extends AndroidViewModel {


    private EntryRepository repository;

    private final MediatorLiveData<EntryEntity> observableEntry;

    public EntryViewModel(@NonNull Application application, final String id, EntryRepository entryRepository) {
        super(application);

        repository = entryRepository;

        observableEntry = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableEntry.setValue(null);

        if (id != null) {
            LiveData<EntryEntity> entry = repository.getEntry(id);

            //observer the changes of the client entity from the database and forward them
            observableEntry.addSource(entry, observableEntry::setValue);
        }
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final Application application;

        private final String email;

        private final EntryRepository repository;

        public Factory(@NonNull Application) {

        }
    }
}
