package com.mapbox.mapboxandroiddemo.account;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.mapbox.mapboxandroiddemo.MainActivity;
import com.mapbox.mapboxandroiddemo.model.usermodel.UserResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Background service which retrieves Mapbox account information
 */

public class AccountRetrievalService extends IntentService {

  private static final String BASE_URL = "https://api.mapbox.com/api/";
  private static final String ACCESS_TOKEN_URL = "https://api.mapbox.com/oauth/access_token/";

  //  TODO: Add CLIENT_SECRET before running app. For now, NEVER commit changes to Github with CLIENT_SECRET!
  private static final String CLIENT_SECRET = "";

  private String clientId;
  private String redirectUri;
  private String username;

  public AccountRetrievalService() {
    super("AccountRetrievalService");
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    if (intent != null) {
      String authCode = intent.getStringExtra("AUTHCODE");
      clientId = intent.getStringExtra("clientId");
      redirectUri = intent.getStringExtra("redirectUri");
      getAccessToken(authCode);
    } else {
      Log.d("AccountRetrievalService", "onHandleIntent: intent == null");
    }
  }

  private void getAccessToken(String code) {
    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(ACCESS_TOKEN_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build();
    MapboxAccountRetrofitService service = retrofit.create(MapboxAccountRetrofitService.class);
    retrofit2.Call<Object> requestRequest = service.getAccessToken("Android Dev Preview",
      clientId, CLIENT_SECRET, redirectUri, code);

    requestRequest.enqueue(new retrofit2.Callback<Object>() {
      @Override
      public void onResponse(retrofit2.Call<Object> call, retrofit2.Response<Object> response) {
        Log.d("RetrievalService", "onResponse: response received");
        String json = response.toString();
        String jsonString = response.raw().body().toString();
        Log.d("RetrievalService", "onResponse: json = " + json);
        Log.d("RetrievalService", "onResponse: jsonString = " + jsonString);
        JSONObject data = null;
        try {
          data = new JSONObject(json);
          String accessToken = data.optString("access_token");
          try {
            getUsernameFromJwt(accessToken);
          } catch (Exception exception) {
            exception.printStackTrace();
          }
          getUserInfo(username, accessToken);
        } catch (JSONException exception) {
          exception.printStackTrace();
        }
      }

      @Override
      public void onFailure(retrofit2.Call<Object> call, Throwable throwable) {
        throwable.printStackTrace();
      }
    });
  }

  private void getUsernameFromJwt(String jwtEncoded) throws Exception {
    try {
      String[] split = jwtEncoded.split("\\.");
      String jwtBody = getJson(split[1]);
      username = jwtBody.substring(6, jwtBody.length() - 34);
    } catch (UnsupportedEncodingException exception) {
      //Error
      exception.printStackTrace();
    }
  }

  private void getUserInfo(final String userName, final String token) {
    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build();
    MapboxAccountRetrofitService service = retrofit.create(MapboxAccountRetrofitService.class);
    retrofit2.Call<UserResponse> request = service.getUserAccount(userName, token);

    request.enqueue(new retrofit2.Callback<UserResponse>() {
      @Override
      public void onResponse(retrofit2.Call<UserResponse> call, retrofit2.Response<UserResponse> response) {

        String userId = response.body().getId();
        Log.d("RetrievalService", "onResponse: userId = " + userId);
        String emailAddress = response.body().getId();
        Log.d("RetrievalService", "onResponse: emailAddress = " + emailAddress);
        String avatarUrl = response.body().getId();
        Log.d("RetrievalService", "onResponse: avatarUrl = " + avatarUrl);

        saveUserInfoToSharedPref(userId, emailAddress, avatarUrl, token);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
      }

      @Override
      public void onFailure(retrofit2.Call<UserResponse> call, Throwable throwable) {
        throwable.printStackTrace();
      }
    });
  }

  private static String getJson(String strEncoded) throws UnsupportedEncodingException {
    byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
    return new String(decodedBytes, "UTF-8");
  }

  private void saveUserInfoToSharedPref(String userId, String emailAddress, String avatarUrl, String token) {
    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
      .putBoolean("TOKEN_SAVED", true)
      .putString("USERNAME", userId)
      .putString("EMAIL", emailAddress)
      .putString("AVATAR_IMAGE_URL", avatarUrl)
      .putString("TOKEN", token)
      .apply();
  }
}
