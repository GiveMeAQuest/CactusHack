package com.makhrov.android.cactusapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makhrov.android.cactusapplication.WEB.Api;
import com.makhrov.android.cactusapplication.WEB.ApiService;
import com.makhrov.android.cactusapplication.WEB.dto.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    ApiService apiService;
    Api api;

    @Bind(R.id.login_text_view)
    protected TextView loginTextView;

    @Bind(R.id.auth_edit)
    protected EditText authEdit;

    @OnClick(R.id.login_button)
    public void submit() {
        if(validateInput(authEdit.getText().toString().trim())){
            apiService.getIdAndLogIn(authEdit.getText().toString().trim())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        System.out.println(user.getId() + user.getName());
                        Intent i = new Intent(getApplicationContext(), OrderActivity.class);
                        i.putExtra("username", user.getName());
                        i.putExtra("userid", user.getId());
                        startActivity(i);
                        finish();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(getApplicationContext(),"Something bad",Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        api = provideApi(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create());

        apiService = provideService(api);
    }


    void onLoginButtonClick() {
        if (!checkInternetConnection(this)) {
            System.out.println("You are offline. Connect your device to the Internet");
            return;
        }
        if (validateInput(authEdit.getText().toString().trim())) {
            apiService.getIdAndLogIn(authEdit.getText().toString().trim());
        }
    }

    private boolean validateInput(String userName) {
        String USER_NAME_PATTERN = "[0-9a-zA-Z_\\\\-]*";

        if(TextUtils.isEmpty(userName)) {
            return false;
        } else if(!userName.matches(USER_NAME_PATTERN)) {
            return false;
        } else {
            return true;
        }
    }

    public Api provideApi(Gson gson) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.55.33.11:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(Api.class);
    }

    public ApiService provideService(Api vidmeApi) {
        return new ApiService(vidmeApi);
    }

    public static boolean checkInternetConnection(@Nullable Context context) {
        boolean result = false;
        if (context != null) {
            ConnectivityManager cm
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            result = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return result;
    }
}
