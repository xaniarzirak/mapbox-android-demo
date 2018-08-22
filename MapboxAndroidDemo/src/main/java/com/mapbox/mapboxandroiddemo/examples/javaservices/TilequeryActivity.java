package com.mapbox.mapboxandroiddemo.examples.javaservices;

// #-code-snippet: tilequery-activity full-java

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.style.layers.Layer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * The most basic example of adding a map to an activity.
 */
public class TilequeryActivity extends AppCompatActivity implements
  OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener {

  private static String TAG = "TilequeryActivity";
  private PermissionsManager permissionsManager;
  private MapboxMap mapboxMap;
  private MapView mapView;
  private LocationLayerPlugin locationLayerPlugin;
  private TextView tilequeryResponseTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_javaservices_tilequery);

    tilequeryResponseTextView = findViewById(R.id.tilequery_response_info_textview);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    TilequeryActivity.this.mapboxMap = mapboxMap;
    for (Layer singleLayer : mapboxMap.getLayers()) {
      Log.d(TAG, "onMapReady: layer id = " + singleLayer.getId());
    }
    enableLocationPlugin();
    mapboxMap.addOnMapClickListener(this);
  }

  @SuppressWarnings( {"MissingPermission"})
  private void enableLocationPlugin() {
    // Check if permissions are enabled and if not request
    if (PermissionsManager.areLocationPermissionsGranted(this)) {

      // Create an instance of the plugin. Adding in LocationLayerOptions is also an optional
      // parameter
      locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap);

      // Set the plugin's camera mode
      locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
      getLifecycle().addObserver(locationLayerPlugin);

      makeTilequeryApiCall(new LatLng(locationLayerPlugin.getLastKnownLocation().getLatitude(),
        locationLayerPlugin.getLastKnownLocation().getLongitude()));
    } else {
      permissionsManager = new PermissionsManager(this);
      permissionsManager.requestLocationPermissions(this);
    }
  }

  @Override
  public void onMapClick(@NonNull LatLng point) {
    makeTilequeryApiCall(point);
  }

  private void makeTilequeryApiCall(@NonNull LatLng point) {
    MapboxTilequery tilequery = MapboxTilequery.builder()
      .accessToken(getString(R.string.access_token))
      .mapIds("mapbox.enterprise-boundaries-a0-v2")
      .query(point)
      .radius(1000)
      .limit(30)
      .geometry("point")
      .dedupe(true)
      .layers("poi-label,admin-state-province,building,poi-label,country-label")
      .build();

    tilequery.enqueueCall(new Callback<FeatureCollection>() {
      @Override
      public void onResponse(Call<FeatureCollection> call, Response<FeatureCollection> response) {
        tilequeryResponseTextView.setText(response.body().toJson());
      }

      @Override
      public void onFailure(Call<FeatureCollection> call, Throwable throwable) {
        Timber.d("Request failed: " + throwable.getMessage());
      }
    });
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override
  public void onExplanationNeeded(List<String> permissionsToExplain) {
    Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPermissionResult(boolean granted) {
    if (granted) {
      enableLocationPlugin();
    } else {
      Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
      finish();
    }
  }

  // Add the mapView lifecycle to the activity's lifecycle methods
  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}
// #-end-code-snippet: tilequery-activity full-java