package com.kpfu.khlopunov.sportgid.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.api.SportGidApi;
import com.kpfu.khlopunov.sportgid.fragments.ApiCallback;
import com.kpfu.khlopunov.sportgid.fragments.RegistrationFragment;
import com.kpfu.khlopunov.sportgid.models.User;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;

import org.json.JSONException;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hlopu on 30.10.2017.
 */

public class AuthentificationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ApiCallback {

    private static final int RC_SIGN_IN = 9001;
    public static final String FRAGMENT_TAG = "my_super_fragment_tag";

    private String[] scope = new String[]{VKScope.FRIENDS};
    private VKAccessToken access_token;
    private GoogleApiClient mGoogleApiClient;
    private Button btnEnter;
    private Button btnVK;
    private Button googleSignInButton;
    //    private SignInButton googleSignInButton;
    private EditText etPassword;
    private TextView tvPolicy;
    private EditText etLogin;
    private TextView tvForgetPassword;
    private TextView tvRegistration;
    private TextView tvErrorPassword;
    private TextView tvErrorEmail;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentification);

        btnEnter = findViewById(R.id.btn_aut);
        btnVK = findViewById(R.id.btn_aut_vk);
        googleSignInButton = findViewById(R.id.btn_aut_google);
//        googleSignInButton = findViewById(R.id.aut_google);
//        googleSignInButton.setSize(SignInButton.SIZE_WIDE);
        etPassword = findViewById(R.id.et_aut_password);
        etLogin = findViewById(R.id.et_aut_email);
        tvPolicy = findViewById(R.id.tv_policy);
        tvForgetPassword = findViewById(R.id.tv_forget_password);
        tvRegistration = findViewById(R.id.tv_registration);
        tvErrorEmail = findViewById(R.id.tv_error_aut_email);
        tvErrorPassword = findViewById(R.id.tv_error_aut_password);
        progressBar = findViewById(R.id.pb_auth);

        tvRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthentificationActivity.this, RegistrationActivity.class);
                startActivity(intent);
//                RegistrationFragment regFragment = new RegistrationFragment();
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container, regFragment, FRAGMENT_TAG).commit();

            }
        });
        tvPolicy.setPaintFlags(tvPolicy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvPolicy.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sportgid.herokuapp.com/start"));
            startActivity(browserIntent);
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService apiService = new ApiService(AuthentificationActivity.this);
                if (etLogin.getText().length() == 0 || etPassword.length() == 0) {
                    Toast.makeText(AuthentificationActivity.this, "Заполните поля", Toast.LENGTH_SHORT).show();
                } else{
                    setVisibilityPB(View.VISIBLE);
                    apiService.authentification(etLogin.getText().toString(), etPassword.getText().toString(), AuthentificationActivity.this);
                }
            }
        });

        btnVK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!VKSdk.isLoggedIn()) {
                    VKSdk.login(AuthentificationActivity.this, scope);
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался
                System.out.println("SUCUCUCEESSSSSSSSSSSSSSSSSSSS");
                access_token = res;

                ApiService apiService = new ApiService(AuthentificationActivity.this);
//                access_token.saveTokenToSharedPreferences(getApplicationContext(), VKAccessToken.ACCESS_TOKEN);
                //TODO отправление id соц сетей на сервер и получение токена от сервера
                SharedPreferencesProvider.getInstance(AuthentificationActivity.this).saveUserTokken("97d2df1c7d0e9906a7ba74ba6ccb9ace");

//                SharedPreferencesProvider.getInstance(AuthentificationActivity.this).saveUserTokken(access_token.toString()); //TODO Переделать на запрос к серверу

                final VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.ACCESS_TOKEN, access_token.accessToken));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onError(VKError error) {

                    }

                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList list = (VKList) response.parsedModel;
                        for (int i = 0; i < list.size(); i++) {
                            try {
                                System.out.println("ID " + list.get(i).fields.get("id"));
//                                ApiService apiService = new ApiService(AuthentificationActivity.this);
                                String vkID = list.get(i).fields.get("id").toString();
//                                apiService.authentificationSocial(list.get(i).fields.get("first_name").toString(),
//                                        list.get(i).fields.get("last_name").toString(),  vkID,
//                                        SharedPreferencesProvider.getInstance(AuthentificationActivity.this).getCity(),
//                                        AuthentificationActivity.this);

                                Log.d("VKID", vkID);
                                SharedPreferencesProvider.getInstance(AuthentificationActivity.this).saveVkId(vkID);
                                SharedPreferencesProvider.getInstance(AuthentificationActivity.this)
                                        .saveVkName(list.get(i).fields.get("first_name").toString() + " " + list.get(i).fields.get("last_name"));
                                SharedPreferencesProvider.getInstance(AuthentificationActivity.this)
                                        .saveUser(new User(list.get(i).fields.get("first_name").toString(), list.get(i).fields.get("last_name").toString()));

                                successAuthorisation(list.get(i).fields.get("first_name").toString(), list.get(i).fields.get("last_name").toString());
//                                apiService = new ApiService(getApplicationContext());
//                                apiService.saveUser(vkID, list.get(i).fields.get("first_name").toString()+" "+list.get(i).fields.get("last_name"), "kazan" );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        finish();
                    }

                });
            }

            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                System.out.println("ERRRRRROROROOR SUCUCUEUUEUSU");
                Toast.makeText(getApplicationContext(), "Error authentification", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GOOGLE_AUT_ERROR", "onConnectionFailed:" + connectionResult);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("GOOGLE AUTH:  ", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            System.out.println("DISPLAY NAME    " + acct.getDisplayName());
            SharedPreferencesProvider.getInstance(AuthentificationActivity.this).saveUserTokken("97d2df1c7d0e9906a7ba74ba6ccb9ace");
            Log.d("GOOGLEID",             acct.getId());
//            SharedPreferencesProvider.getInstance(AuthentificationActivity.this).saveUserTokken(acct.getId());//Todo через сервер
            SharedPreferencesProvider.getInstance(AuthentificationActivity.this)
                    .saveUser(new User(acct.getDisplayName(), ""));
            successAuthorisation(acct.getDisplayName(), "");
            finish();
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }

    @Override
    public void callback(Object object) {
        setVisibilityPB(View.GONE);
        System.out.println("TOKKKKEKEN OT NURIKA " + object);
        if (((String) object).equals("ERROR")) Toast.makeText(AuthentificationActivity.this,
                "Не удалось авторизоваться", Toast.LENGTH_SHORT).show();
        else {
            SharedPreferencesProvider.getInstance(AuthentificationActivity.this).saveUserTokken((String) object);
            SportGidApi.getInstance().getmSportGidApiRequests().getUser((String) object)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userApiResult -> {
                        if(userApiResult.getCode() == 0){
                            successAuthorisation(userApiResult.getBody().getName(), userApiResult.getBody().getSurname());
                        }
                        else {
                            Toast.makeText(this, "Не удалось получить данные пользователя", Toast.LENGTH_SHORT).show();
                            //TODO убрать
//                            successAuthorisation("Анатолий", "Хлопунов");
                        }
                    });
            }
    }

    public void successAuthorisation(String name, String surname) {
        SharedPreferencesProvider.getInstance(AuthentificationActivity.this)
                .saveUser(new User(name, surname));
        Intent intent = new Intent(AuthentificationActivity.this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setVisibilityPB(int visibility) {
        switch (visibility) {
            case View.VISIBLE:
                progressBar.setVisibility(View.VISIBLE);
                btnEnter.setVisibility(View.GONE);
                break;
            case View.GONE:
                progressBar.setVisibility(View.GONE);
                btnEnter.setVisibility(View.VISIBLE);
                break;
        }
    }
}
