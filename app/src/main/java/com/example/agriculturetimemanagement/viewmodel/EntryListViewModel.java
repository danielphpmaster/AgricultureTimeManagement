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

import java.util.List;

public class EntryListViewModel extends AndroidViewModel {
    private EntryRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<EntryEntity>> observableEntries;

    public EntryListViewModel(@NonNull Application application, EntryRepository entryRepository) {
        super(application);

        repository = entryRepository;

        observableEntries = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableEntries.setValue(null);

        LiveData<List<EntryEntity>> entries = repository.getAllEntries();

        // observe the changes of the entities from the database and forward them
        observableEntries.addSource(entries, observableEntries::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;

        private final EntryRepository entryRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            entryRepository = EntryRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new EntryListViewModel(application, entryRepository);
        }
    }

    public LiveData<List<EntryEntity>> getEntries() {
        return observableEntries;
    }

    public void deleteEntry(EntryEntity entry) {
        repository.delete(entry, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onFailure(Exception e) {}
        });
    }
}
