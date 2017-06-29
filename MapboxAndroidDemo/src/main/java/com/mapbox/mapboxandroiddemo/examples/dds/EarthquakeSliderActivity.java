package com.mapbox.mapboxandroiddemo.examples.dds;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.IOException;
import java.io.InputStream;

import static com.mapbox.mapboxsdk.style.functions.Function.property;
import static com.mapbox.mapboxsdk.style.functions.stops.Stop.stop;
import static com.mapbox.mapboxsdk.style.functions.stops.Stops.categorical;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;

public class EarthquakeSliderActivity extends AppCompatActivity {

  private MapboxMap map;
  private MapView mapView;
  private Layer earthquakeCircles;
  private String[] months = {"January", "February", "March", "April", "May",
    "June", "July", "August", "September", "October", "November", "December"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_earthquake_slider);

    final SeekBar monthAdjuster = (SeekBar) findViewById(R.id.seek_bar_month_adjuster);
    final TextView monthOfYearTextView = (TextView) findViewById(R.id.textview_month_of_year);


    int x = 3;
    String monthToShow = months[x];
    monthOfYearTextView.setText(monthToShow);

    monthAdjuster.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (earthquakeCircles != null) {
          earthquakeCircles.setProperties(
          );

        }

        int x = 3;
        String monthToShow = months[x];
        Log.d("earthsliderActivity", "onProgressChanged: progress = " + progress);
        monthOfYearTextView.setText(monthToShow);

      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(final MapboxMap mapboxMap) {

        map = mapboxMap;

        GeoJsonSource earthquakeSource = new GeoJsonSource("earthquakeSource", loadJsonFromAsset("significant-earthquakes-2015.geojson"));
        map.addSource(earthquakeSource);

        CircleLayer earthquakeLayer = new CircleLayer("earthquakeLayer", "earthquakeSource");
        earthquakeLayer.setProperties(
          circleColor(
            property("mag", categorical(
              stop(6, circleColor(Color.parseColor("#FCA107"))),
              stop(8, circleColor(Color.parseColor("#7F3121")))
              )
            )
          ),
          circleRadius(
            property("mag", categorical(
              stop(6, circleRadius(20f)),
              stop(8, circleRadius(40f))
              )
            )
          ),
          circleOpacity(0.75f)
        );
        map.addLayer(earthquakeLayer);

        SymbolLayer earthquakeLabels = new SymbolLayer("earthquake-labels", "earthquakeSource");
        earthquakeLabels.withProperties(
          textColor("rgba(0,0,0,0.5)")
        );

        map.addLayer(earthquakeLabels);

        earthquakeCircles = map.getLayer("earthquakeLayer");
      }
    });
  }


  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
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

  private String loadJsonFromAsset(String filename) {
    // Using this method to load in GeoJSON files from the assets folder.

    try {
      InputStream is = getAssets().open(filename);
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      return new String(buffer, "UTF-8");

    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }
}
