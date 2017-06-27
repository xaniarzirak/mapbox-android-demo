package com.mapbox.mapboxandroiddemo.examples.extrusions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.functions.Function;
import com.mapbox.mapboxsdk.style.functions.stops.Stops;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import static com.mapbox.mapboxsdk.style.functions.Function.property;
import static com.mapbox.mapboxsdk.style.functions.stops.Stop.stop;
import static com.mapbox.mapboxsdk.style.functions.stops.Stops.categorical;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionBase;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;

public class IndoorSpacesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

  private MapView mapView;
  private MapboxMap mapboxMap;
  private String[] roomNum;
  private String[] colorStrings;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_indoor_spaces);

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        IndoorSpacesActivity.this.mapboxMap = mapboxMap;

        // TODO: Fill in roomData vector source url below
        VectorSource roomData = new VectorSource("room-data-source", "");
        mapboxMap.addSource(roomData);

        setUpStopsStrings();
        setUpColorStrings();

        FillExtrusionLayer roomLayer = new FillExtrusionLayer("course", "room-data-source");
        roomLayer.setSourceLayer("room_cleaned-b0ndmw");
        roomLayer.withProperties(
          fillExtrusionColor(property("RMTYP", categorical(
            stop(roomNum[0], circleColor(Color.parseColor(colorStrings[0]))),
            stop(roomNum[1], circleColor(Color.parseColor(colorStrings[1]))),
            stop(roomNum[2], circleColor(Color.parseColor(colorStrings[2]))),
            stop(roomNum[3], circleColor(Color.parseColor(colorStrings[3]))),
            stop(roomNum[4], circleColor(Color.parseColor(colorStrings[4]))),
            stop(roomNum[5], circleColor(Color.parseColor(colorStrings[5]))),
            stop(roomNum[6], circleColor(Color.parseColor(colorStrings[6]))),
            stop(roomNum[7], circleColor(Color.parseColor(colorStrings[7]))),
            stop(roomNum[8], circleColor(Color.parseColor(colorStrings[8]))),
            stop(roomNum[9], circleColor(Color.parseColor(colorStrings[9]))),
            stop(roomNum[10], circleColor(Color.parseColor(colorStrings[10]))),
            stop(roomNum[11], circleColor(Color.parseColor(colorStrings[11]))),
            stop(roomNum[12], circleColor(Color.parseColor(colorStrings[12]))),
            stop(roomNum[13], circleColor(Color.parseColor(colorStrings[13]))),
            stop(roomNum[14], circleColor(Color.parseColor(colorStrings[14]))),
            stop(roomNum[15], circleColor(Color.parseColor(colorStrings[15]))),
            stop(roomNum[16], circleColor(Color.parseColor(colorStrings[16]))),
            stop(roomNum[17], circleColor(Color.parseColor(colorStrings[17]))),
            stop(roomNum[18], circleColor(Color.parseColor(colorStrings[18]))),
            stop(roomNum[19], circleColor(Color.parseColor(colorStrings[19]))),
            stop(roomNum[20], circleColor(Color.parseColor(colorStrings[20]))),
            stop(roomNum[21], circleColor(Color.parseColor(colorStrings[21]))),
            stop(roomNum[22], circleColor(Color.parseColor(colorStrings[22]))),
            stop(roomNum[23], circleColor(Color.parseColor(colorStrings[23]))),
            stop(roomNum[24], circleColor(Color.parseColor(colorStrings[24]))),
            stop(roomNum[25], circleColor(Color.parseColor(colorStrings[25]))),
            stop(roomNum[26], circleColor(Color.parseColor(colorStrings[26]))),
            stop(roomNum[27], circleColor(Color.parseColor(colorStrings[27]))),
            stop(roomNum[28], circleColor(Color.parseColor(colorStrings[28]))),
            stop(roomNum[29], circleColor(Color.parseColor(colorStrings[29]))),
            stop(roomNum[30], circleColor(Color.parseColor(colorStrings[30]))),
            stop(roomNum[31], circleColor(Color.parseColor(colorStrings[31]))),
            stop(roomNum[32], circleColor(Color.parseColor(colorStrings[32]))),
            stop(roomNum[33], circleColor(Color.parseColor(colorStrings[33]))),
            stop(roomNum[34], circleColor(Color.parseColor(colorStrings[34]))),
            stop(roomNum[35], circleColor(Color.parseColor(colorStrings[35])))
            )
          )),
          fillExtrusionOpacity(1f),
          fillExtrusionBase(Function.property("floor_height", Stops.<Float>identity())),
          fillExtrusionHeight(Function.property("ceiling_height", Stops.<Float>identity()))
        );
        mapboxMap.addLayer(roomLayer);

        setUpSpinner();
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  private void setUpStopsStrings() {
    roomNum = new String[36];
    roomNum[0] = "010";
    roomNum[1] = "020";
    roomNum[2] = "030";
    roomNum[3] = "040";
    roomNum[4] = "250";
    roomNum[5] = "255";
    roomNum[6] = "850";
    roomNum[7] = "835";
    roomNum[8] = "810";
    roomNum[9] = "855";
    roomNum[10] = "830";
    roomNum[11] = "820";
    roomNum[12] = "910";
    roomNum[13] = "920";
    roomNum[14] = "950";
    roomNum[15] = "310";
    roomNum[16] = "315";
    roomNum[17] = "350";
    roomNum[18] = "610";
    roomNum[19] = "630";
    roomNum[20] = "635";
    roomNum[21] = "650";
    roomNum[22] = "655";
    roomNum[23] = "660";
    roomNum[24] = "665";
    roomNum[25] = "670";
    roomNum[26] = "675";
    roomNum[27] = "720";
    roomNum[28] = "725";
    roomNum[29] = "110";
    roomNum[30] = "115";
    roomNum[31] = "210";
    roomNum[32] = "215";
    roomNum[33] = "410";
    roomNum[34] = "420";
    roomNum[35] = "430";
  }

  private void setUpColorStrings() {
    colorStrings = new String[36];
    colorStrings[0] = "#afafbe";
    colorStrings[1] = "#eeeeee";
    colorStrings[2] = "#cfcfda";
    colorStrings[3] = "#d62728";
    colorStrings[4] = "#fc8d62";
    colorStrings[5] = "#fc8d62";
    colorStrings[6] = "#B04E69";
    colorStrings[7] = "#B04E69";
    colorStrings[8] = "#B04E69";
    colorStrings[9] = "#B04E69";
    colorStrings[10] = "#B04E69";
    colorStrings[11] = "#B04E69";
    colorStrings[12] = "#80b192";
    colorStrings[13] = "#80b192";
    colorStrings[14] = "#80b192";
    colorStrings[15] = "#8da0cb";
    colorStrings[16] = "#8da0cb";
    colorStrings[17] = "#7B6698";
    colorStrings[18] = "#7B6698";
    colorStrings[19] = "#2ca02c";
    colorStrings[20] = "#2ca02c";
    colorStrings[21] = "#2ca02c";
    colorStrings[22] = "#2ca02c";
    colorStrings[23] = "#2ca02c";
    colorStrings[24] = "#2ca02c";
    colorStrings[25] = "#2ca02c";
    colorStrings[26] = "#2ca02c";
    colorStrings[27] = "#2ca02c";
    colorStrings[28] = "#2ca02c";
    colorStrings[29] = "#e6b74c";
    colorStrings[30] = "#e6b74c";
    colorStrings[31] = "#e6b74c";
    colorStrings[32] = "#e6b74c";
    colorStrings[33] = "#4EB091";
    colorStrings[34] = "#4EB091";
    colorStrings[35] = "#4EB091";
  }

  private void setUpSpinner() {
    Spinner spinner = (Spinner) findViewById(R.id.extrusions_indoor_room_spinner_menu);
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
      R.array.rooms_array, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(this);
  }

  public void onItemSelected(AdapterView<?> parent, View view,
                             int pos, long id) {

    Log.d("IndoorSpacesActivity", "onItemSelected: Selected route = " + parent.getItemAtPosition(pos).toString());

  }

  public void onNothingSelected(AdapterView<?> parent) {
    // Another interface callback
  }


}
