package com.kpfu.khlopunov.sportgid.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.adapters.InterestSelect.InteresSelect;
import com.kpfu.khlopunov.sportgid.adapters.InterestSelect.InteresSelectAdapter;
import com.kpfu.khlopunov.sportgid.adapters.InterestSelect.InteresSelectAdapterRadio;
import com.kpfu.khlopunov.sportgid.fragments.ApiCallback;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;

import java.io.Serializable;
import java.util.List;

public class SelectInterestsActivtiy extends AppCompatActivity implements ApiCallback {
    public static final String TYPE_CHECK_BUTTON = "TYPE_CHECK_BUTTON";
    public static final String CHECKBOX = "CHECKBOX";
    public static final String RADIO = "RADIO";
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private RecyclerView rvInterests;
    private Button btnSave;
    private InteresSelect adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interests);
        rvInterests = findViewById(R.id.rv_select_interests);
        btnSave = findViewById(R.id.btn_save_interests);
        toolbar = findViewById(R.id.my_toolbar_set);
        toolbarTitle = findViewById(R.id.toolbar_title_set);
        progressBar = findViewById(R.id.pb_select_interests);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarTitle.setText("Виды спорта");

        rvInterests.setLayoutManager(new LinearLayoutManager(this));
        if (getIntent().getStringExtra(TYPE_CHECK_BUTTON).equals(CHECKBOX)) {
            adapter = new InteresSelectAdapter(SelectInterestsActivtiy.this);
            adapter.setKindSports(SharedPreferencesProvider.getInstance(this).getKindsSports());
            rvInterests.setAdapter(adapter);
        } else if (getIntent().getStringExtra(TYPE_CHECK_BUTTON).equals(RADIO)) {
            adapter = new InteresSelectAdapterRadio(SelectInterestsActivtiy.this);
            adapter.setKindSports(SharedPreferencesProvider.getInstance(this).getKindsSports());
            rvInterests.setAdapter(adapter);
        }
        /**
         * Подгрузка видов спорта с сервера, вернуть если нужно
         */
//        onVisibleProgBar();
//        ApiService apiService = new ApiService(SelectInterestsActivtiy.this);
//        apiService.getKindSports(SelectInterestsActivtiy.this);

        btnSave.setOnClickListener(v -> {
            List<KindSport> selected = adapter.getSelected();
            Intent intent = null;
            if (getIntent().getStringExtra(TYPE_CHECK_BUTTON).equals(CHECKBOX)) {
                intent = new Intent(this, AddPlaceActivity.class);
            } else if (getIntent().getStringExtra(TYPE_CHECK_BUTTON).equals(RADIO)) {
                intent = new Intent(this, AddEventActivity.class);
            }
            Bundle bundle = new Bundle();
            System.out.println("SEND SIZE " + selected.size());
            bundle.putSerializable("selected", (Serializable) selected);
            if (intent != null) intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void callback(Object object) {
        if (object instanceof String) {
            if (((String) object).equals("ERROR"))
                Toast.makeText(this, "Не удалось загрузить", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setKindSports((List<KindSport>) object);
            adapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
        rvInterests.setVisibility(View.VISIBLE);
    }

    public void onVisibleProgBar() {
        rvInterests.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }
}
