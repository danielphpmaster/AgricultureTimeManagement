package com.example.agriculturetimemanagement.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agriculturetimemanagement.R;
import com.example.agriculturetimemanagement.database.entity.EntryEntity;
import com.example.agriculturetimemanagement.util.OnAsyncEventListener;
import com.example.agriculturetimemanagement.viewmodel.EntryViewModel;

public class EntryDetails extends AppCompatActivity {
    private static final String TAG = "EntryDetails";

    private static final int CREATE_ENTRY = 0;
    private static final int EDIT_ENTRY = 1;
    private static final int DELETE_ENTRY = 2;

    private Toast statusToast;

    private boolean isEditable;

    private EditText etName;
    private Spinner sCategory;
    private EditText etTime;

    private EntryViewModel viewModel;

    private EntryEntity entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entry_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String entryId = getIntent().getStringExtra("entryId");
        initiateView();

        EntryViewModel.Factory factory = new EntryViewModel.Factory(getApplication(), entryId);
        viewModel = new ViewModelProvider(this, factory).get(EntryViewModel.class);
        viewModel.getEntry().observe(this, entryEntity -> {
            if (entryEntity != null) {
                entry = entryEntity;
                updateContent();
            }
        });

        if (entryId != null) {
            setTitle("Entry Details");
        } else {
            setTitle("Create Entry");
            switchEditableMode();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (entry != null) {
            menu.add(0, EDIT_ENTRY, Menu.NONE, "Edit").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, DELETE_ENTRY, Menu.NONE, "Delete").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, CREATE_ENTRY, Menu.NONE, "Create Entry").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == EDIT_ENTRY) {
            if (isEditable) {
                switchEditableMode();
            } else {
                switchEditableMode();
            }
        }
        if (item.getItemId() == DELETE_ENTRY) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Delete");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Do you really want to delete this entry?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", (dialog, which) -> {
                viewModel.deleteEntry(entry, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "deleteEntry: success");
                        onBackPressed();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "deleteEntry: failure", e);
                    }
                });
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
        }
        if (item.getItemId() == CREATE_ENTRY) {
            createEntry(
                    etName.getText().toString(),
                    sCategory.getSelectedItemPosition(),
                    etTime.getText().toString()
            );
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateView() {
        isEditable = false;
        etName = findViewById(R.id.nameID);
        sCategory = findViewById(R.id.categoryID);
        etTime = findViewById(R.id.timeID);

        etName.setFocusable(false);
        etName.setEnabled(false);

        sCategory.setFocusable(false);
        sCategory.setEnabled(false);

        etTime.setFocusable(false);
        etTime.setFocusable(false);
    }

    private void switchEditableMode() {
        if (!isEditable) {
            etName.setFocusable(true);
            etName.setEnabled(true);
            etName.setFocusableInTouchMode(true);

            sCategory.setFocusable(true);
            sCategory.setEnabled(true);
            sCategory.setFocusableInTouchMode(true);

            etTime.setFocusable(true);
            etTime.setEnabled(true);
            etTime.setFocusableInTouchMode(true);

            etName.requestFocus();
        } else {
            saveChanges(
                    etName.getText().toString(),
                    sCategory.getSelectedItemPosition(),
                    etTime.getText().toString()
            );

            etName.setFocusable(false);
            etName.setEnabled(false);

            sCategory.setFocusable(false);
            sCategory.setEnabled(false);

            etTime.setFocusable(false);
            etTime.setFocusable(false);
        }
        isEditable = !isEditable;
    }

    private void createEntry(String name, int category, String time) {
        entry = new EntryEntity();
        entry.setName(name);
        entry.setCategory(category);
        entry.setTime(time);

        viewModel.createEntry(entry, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "createEntry: success");
                onBackPressed();
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "createEntry: failure", e);
            }
        });
    }

    private void saveChanges(String name, int category, String time) {
        entry.setName(name);
        entry.setCategory(category);
        entry.setTime(time);

        viewModel.updateEntry(entry, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "updateEntry: success");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "updateEntry: failure", e);
                setResponse(false);
            }
        });
    }

    private void setResponse(Boolean response) {
        if (response) {
            statusToast = Toast.makeText(this, "Entry edited", Toast.LENGTH_LONG);
            statusToast.show();
            onBackPressed();
        } else {
            statusToast = Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG);
            statusToast.show();
        }
    }

    private void updateContent() {
        if (entry != null) {
            etName.setText(entry.getName());
            sCategory.setSelection(entry.getCategory());
            etTime.setText(entry.getTime());
        }
    }
}