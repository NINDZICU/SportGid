package com.kpfu.khlopunov.sportgid.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.databinding.ActivityAddEventBinding;
import com.kpfu.khlopunov.sportgid.firebase.FireBaseCallback;
import com.kpfu.khlopunov.sportgid.firebase.UploadImage;
import com.kpfu.khlopunov.sportgid.fragments.ApiCallback;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ActiveSystemService;
import com.kpfu.khlopunov.sportgid.service.ActiveSystemServiceInt;
import com.kpfu.khlopunov.sportgid.service.ApiService;
import com.kpfu.khlopunov.sportgid.service.PermissionService;

import java.util.List;

import static com.kpfu.khlopunov.sportgid.activities.AddPlaceActivity.KEY_URL;
import static com.kpfu.khlopunov.sportgid.activities.AddPlaceActivity.REQUEST_CODE_INTERESTS;
import static com.kpfu.khlopunov.sportgid.service.ServiceConstants.GALLERY_REQUEST;

public class AddEventActivity extends AppCompatActivity implements ApiCallback, FireBaseCallback {
    public static final String NOT_SELECTED_IMAGE = "NOT_SELECTED_IMAGE";
    private ActivityAddEventBinding r;

    private KindSport kindSport = null;
    private String photoUrlFirebase = null;
    private Uri selectedImage = null;
    private ActiveSystemServiceInt activeSystemService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        r = DataBindingUtil.setContentView(this, R.layout.activity_add_event);
        bind();
        activeSystemService = new ActiveSystemService(this);

        if (savedInstanceState != null) {
            String sUri = savedInstanceState.getString(KEY_URL);
            if (sUri != null) {
                selectedImage = Uri.parse(sUri);
                fillImage(selectedImage);
            }
        }

        r.includeAddEvent.btnEventDownload.setOnClickListener(v -> {
            activeSystemService.runPhotoPicker();
        });
        r.includeAddEvent.btnEventSave.setOnClickListener(v -> {
            uploadImage();
        });
        r.includeAddEvent.tvEventKindSport.setOnClickListener(v -> {
            Intent intent = new Intent(AddEventActivity.this, SelectInterestsActivtiy.class);
            intent.putExtra(SelectInterestsActivtiy.TYPE_CHECK_BUTTON, SelectInterestsActivtiy.RADIO);
            startActivityForResult(intent, REQUEST_CODE_INTERESTS);
        });
    }

    private void bind() {
        setSupportActionBar(r.includeAddEvent.include.myToolbarSet);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        r.includeAddEvent.include.toolbarTitleSet.setText("Добавить мероприятие");
    }

    @Override
    public void setFirebaseLink(String firebaseLink) {
        this.photoUrlFirebase = firebaseLink;
        uploadDataToServer();
        setVisibleProgressBar(false);
    }


    public boolean checkCorrectData() {
        if (r.includeAddEvent.etEventName.length() == 0 || r.includeAddEvent.etEventAddress.length() == 0 ||
                r.includeAddEvent.etEventDescription.length() == 0 || r.includeAddEvent.tvEventKindSport.length() == 0 ||
                r.includeAddEvent.etEventPrice.length() == 0 || r.includeAddEvent.etEventCountMembers.length() == 0) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return false;
        } else if (photoUrlFirebase == null) {
            Toast.makeText(this, "Не удалось загрузить фото на сервер", Toast.LENGTH_SHORT).show();
            return false;
        } else if (photoUrlFirebase.equals(NOT_SELECTED_IMAGE)) {
            photoUrlFirebase = null;
        }
        return true;
    }

    private void uploadDataToServer() {
        if (checkCorrectData()) {
            ApiService apiService = new ApiService(this);
            //todo возвращать координаты
            apiService.addEvent(r.includeAddEvent.etEventName.getText().toString(), r.includeAddEvent.etEventDescription.getText().toString(),
                    Integer.valueOf(r.includeAddEvent.etEventCountMembers.getText().toString()),
                    r.includeAddEvent.etEventPrice.getText().toString(), SharedPreferencesProvider.getInstance(this).getUserTokken(),
                    //Todo норм place и kindSport
                    photoUrlFirebase, String.valueOf(kindSport.getId()), null, 12, 12, AddEventActivity.this);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedImage != null) {
            outState.putString(KEY_URL, selectedImage.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_INTERESTS:
                    Bundle bundle = data.getExtras();
                    List<KindSport> kindSports = (List<KindSport>) bundle.getSerializable("selected");
                    kindSport = kindSports.get(0);
                    r.includeAddEvent.tvEventKindSport.setText(kindSport.getName());
                    break;
                case GALLERY_REQUEST:
                    selectedImage = data.getData();
                    fillImage(selectedImage);
                    break;
            }
            System.out.println("SELECTED IMAGE@@@@ " + selectedImage);
//            if (selectedImage != null) {
//                fillImage(selectedImage);
//            }
        }
    }

    private void fillImage(Uri selectedImage) {
        Glide.
                with(this)
                .load(selectedImage)
                .apply(RequestOptions.fitCenterTransform().apply(RequestOptions.overrideOf(500)))
                .into(r.includeAddEvent.btnEventDownload);
    }

    @Override
    public void callback(Object object) {
        if ((Boolean) object) {
            Toast.makeText(this, "Добавление успешно выполнено!", Toast.LENGTH_SHORT).show();
            finish();
        } else Toast.makeText(this, "Не удалось добавить объект", Toast.LENGTH_SHORT).show();
    }

    private void uploadImage() {
        setVisibleProgressBar(true);
        UploadImage uploadImage = UploadImage.getInstance();
        uploadImage.uploadImageEvent(this, selectedImage);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionService.REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    activeSystemService.runPhotoPicker();
                } else {
                    Toast.makeText(AddEventActivity.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }


    private void setVisibleProgressBar(boolean bool) {
        if (bool) {
            r.includeAddEvent.pbAddEvent.setVisibility(View.VISIBLE);
            r.includeAddEvent.scrollviewAddEvent.setVisibility(View.GONE);
        } else {
            r.includeAddEvent.pbAddEvent.setVisibility(View.GONE);
            r.includeAddEvent.scrollviewAddEvent.setVisibility(View.VISIBLE);

        }
    }


}
