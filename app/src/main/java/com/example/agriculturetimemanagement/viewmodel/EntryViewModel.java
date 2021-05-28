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
import com.example.agriculturetimemanagement.util.OnAsyncEventListener;

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

            //observer the changes of the entry entity from the database and forward them
            observableEntry.addSource(entry, observableEntry::setValue);
        }
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final Application application;

        private final String id;

        private final EntryRepository repository;

        public Factory(@NonNull Application application, String id) {
            this.application = application;
            this.id = id;
            repository = EntryRepository.getInstance();
        }

        public <T extends ViewModel> T create(Class<T> modelClass) {
            //no inspection unchecked
            return (T) new EntryViewModel(application, id, repository);
        }
    }

    /**
     * Expose the LiveData EntryEntity query so the UI can observe it.
     */

    public LiveData<EntryEntity> getEntry() {
        return observableEntry;
    }

    public void createEntry(EntryEntity entry, OnAsyncEventListener callback) {
        repository.insert(entry, callback);
    }

    public void updateEntry(EntryEntity entry, OnAsyncEventListener callback) {
        repository.update(entry, callback);
    }

    public void deleteEntry(EntryEntity entry, OnAsyncEventListener callback) {
        repository.update(entry, callback);
    }
}
