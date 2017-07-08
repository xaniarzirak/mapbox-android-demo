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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static com.mapbox.mapboxsdk.style.functions.Function.property;
import static com.mapbox.mapboxsdk.style.functions.stops.Stop.stop;
import static com.mapbox.mapboxsdk.style.functions.stops.Stops.categorical;
import static com.mapbox.mapboxsdk.style.layers.Filter.eq;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;

public class EarthquakeSliderActivity extends AppCompatActivity {

  private MapboxMap mapboxMap;
  private MapView mapView;
  private Layer earthquakeCircles;
  private String[] months = {"January", "February", "March", "April", "May",
      "June", "July", "August", "September", "October", "November", "December"};
  private String monthToShow;
  private String earthquakeGeoJsonSourceId = "earthquakeSource";
  private int progressFilter;

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

    progressFilter = 0;
    monthAdjuster.setProgress(0);
    monthToShow = months[0];
    monthOfYearTextView.setText(monthToShow);


    monthAdjuster.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        double singleMonth = 100 / 12;

        if (progress <= singleMonth) {
          monthToShow = months[0];
          monthOfYearTextView.setText(monthToShow);


        } else if (progress > singleMonth && progress < singleMonth * 2) {
          monthToShow = months[1];
          monthOfYearTextView.setText(monthToShow);


        } else if (progress > singleMonth * 2 && progress < singleMonth * 3) {
          monthToShow = months[2];
          monthOfYearTextView.setText(monthToShow);


        } else if (progress > singleMonth * 3 && progress < singleMonth * 4) {
          monthToShow = months[3];
          monthOfYearTextView.setText(monthToShow);


        } else if (progress > singleMonth * 4 && progress < singleMonth * 5) {
          monthToShow = months[4];
          monthOfYearTextView.setText(monthToShow);


        } else if (progress > singleMonth * 5 && progress < singleMonth * 6) {
          monthToShow = months[5];
          monthOfYearTextView.setText(monthToShow);


        } else if (progress > singleMonth * 6 && progress < singleMonth * 7) {
          monthToShow = months[6];
          monthOfYearTextView.setText(monthToShow);


        } else if (progress > singleMonth * 7 && progress < singleMonth * 8) {
          monthToShow = months[7];
          monthOfYearTextView.setText(monthToShow);


        } else if (progress > singleMonth * 8 && progress < singleMonth * 9) {
          monthToShow = months[8];
          monthOfYearTextView.setText(monthToShow);


        } else if (progress > singleMonth * 9 && progress < singleMonth * 10) {
          monthToShow = months[9];
          monthOfYearTextView.setText(monthToShow);


        } else if (progress > singleMonth * 10 && progress < singleMonth * 11) {
          monthToShow = months[10];
          monthOfYearTextView.setText(monthToShow);


        } else if (progress > singleMonth * 11 && progress < singleMonth * 12) {
          monthToShow = months[11];


          monthOfYearTextView.setText(monthToShow);
        }
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

        EarthquakeSliderActivity.this.mapboxMap = mapboxMap;

        // Create GeoJsonSource and add to map
        GeoJsonSource earthquakeSource = new GeoJsonSource(earthquakeGeoJsonSourceId,
            loadJsonFromAsset("significant-earthquakes-2015.geojson"));
        mapboxMap.addSource(earthquakeSource);

        JSONArray earthquakeArray;
        StringBuilder sb = new StringBuilder();

        try {
          earthquakeArray = new JSONArray(sb.toString());
        } catch (JSONException exception) {
          throw new RuntimeException(exception);
        }

        for (int x = 0; x < earthquakeArray.length(); x++) {
          try {
            JSONObject singleEarthquake = earthquakeArray.getJSONObject(x);
            Log.d("earthslider", "onMapReady: singleEarthquake place =" + singleEarthquake.getString("place"));

            long time = singleEarthquake.getLong("time");
            Log.d("earthslider", "onMapReady: singleEarthquake time =" + singleEarthquake.getLong("time"));

            Date date = new Date(time);
            Log.d("earthslider", "onMapReady: date =" + date);


          } catch (JSONException exception) {
            throw new RuntimeException(exception);
          }
        }

        // Create CircleLayer with GeoJson data and then add it to the map
        CircleLayer earthquakeLayer = new CircleLayer("earthquakeLayer", earthquakeGeoJsonSourceId);
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
        earthquakeLayer.setFilter(
            eq("cluster", progressFilter)
        );
        mapboxMap.addLayer(earthquakeLayer);

        // Create and then add SymbolLayer to the map
        SymbolLayer earthquakeLabels = new SymbolLayer("earthquake-labels", earthquakeGeoJsonSourceId);
        earthquakeLabels.withProperties(
            textColor(Color.WHITE)
        );
        mapboxMap.addLayer(earthquakeLabels);

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
