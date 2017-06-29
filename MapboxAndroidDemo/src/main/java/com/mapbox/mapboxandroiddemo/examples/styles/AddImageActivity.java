package com.mapbox.mapboxandroiddemo.examples.styles;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class AddImageActivity extends AppCompatActivity {

  private static final String CUSTOM_IMAGE = "custom-icon";

  private MapboxMap map;
  private MapView mapView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_add_image);

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {

        mapboxMap.addImage(CUSTOM_IMAGE, BitmapFactory.decodeResource(getResources(), R.drawable.radar));

        // Add a source with a geojson point
        Point point = Point.fromCoordinates(Position.fromCoordinates(41.874, -75.789));
        GeoJsonSource source = new GeoJsonSource("point", FeatureCollection.fromFeatures(new Feature[] {Feature.fromGeometry(point)})
        );
        mapboxMap.addSource(source);

        SymbolLayer imageLayer = new SymbolLayer("imageLayer", "point");
        imageLayer.setProperties(
          // Set the id of the sprite to use
          iconImage(CUSTOM_IMAGE)
        );

        // lets add a circle below labels!
        mapboxMap.addLayer(imageLayer);

      }
    });
  }

  @Override
  protected void onResume() {
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
  protected void onPause() {
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
