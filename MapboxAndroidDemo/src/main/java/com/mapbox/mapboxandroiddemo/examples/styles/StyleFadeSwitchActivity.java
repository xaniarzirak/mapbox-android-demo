package com.mapbox.mapboxandroiddemo.examples.styles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.Source;

import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.rasterOpacity;

public class StyleFadeSwitchActivity extends AppCompatActivity implements
  OnMapReadyCallback, MapboxMap.OnCameraMoveListener {

  private MapView mapView;
  private MapboxMap mapboxMap;
  private String SATELLITE_RASTER_SOURCE_ID = "SATELLITE_RASTER_SOURCE_ID";
  private String SATELLITE_RASTER_LAYER_ID = "SATELLITE_RASTER_LAYER_ID";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_style_fade_switch);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    StyleFadeSwitchActivity.this.mapboxMap = mapboxMap;

    Source satelliteRasterSource = new RasterSource(SATELLITE_RASTER_SOURCE_ID, "mapbox://mapbox.satellite", 512);
    mapboxMap.addSource(satelliteRasterSource);

    RasterLayer satelliteRasterLayer = new RasterLayer(SATELLITE_RASTER_LAYER_ID, SATELLITE_RASTER_SOURCE_ID);
    satelliteRasterLayer.withProperties(
      rasterOpacity(interpolate(linear(), zoom(),
        /*stop(0, 0),*/
        stop(13, .5),
        stop(14.5, 1)
      ))
    );
    mapboxMap.addLayer(satelliteRasterLayer);
  }

  @Override
  public void onCameraMove() {
    Log.d("StyleFadeSwitchActivity", "onCameraMove: zoom = " + mapboxMap.getCameraPosition().zoom);
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