package com.mapbox.mapboxandroiddemo.examples.styles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.net.MalformedURLException;
import java.net.URL;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.step;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

/**
 * Use GeoJSON to visualize point data as a clusters.
 */
public class GeoJsonClusteringActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap mapboxMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_geojson_clustering);

        mapView = findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap map) {

                mapboxMap = map;

                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(12.099, -79.045), 3));

                addClusteredGeoJsonSource();

                Toast.makeText(GeoJsonClusteringActivity.this, R.string.zoom_map_in_and_out_instruction,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
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
    public void onStop() {
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


    private void addClusteredGeoJsonSource() {

        // Add a new source from the GeoJSON data and set the 'cluster' option to true.
        try {
            mapboxMap.addSource(
                    // Point to GeoJSON data. This example visualizes all M1.0+ earthquakes from
                    // 12/22/15 to 1/21/16 as logged by USGS' Earthquake hazards program.
                    new GeoJsonSource("earthquakes",
                            new URL("https://www.mapbox.com/mapbox-gl-js/assets/earthquakes.geojson"),
                            new GeoJsonOptions()
                                    .withCluster(true)
                                    .withClusterMaxZoom(14)
                                    .withClusterRadius(100)
                    )
            );
        } catch (MalformedURLException malformedUrlException) {
            Log.e("dataClusterActivity", "Check the URL " + malformedUrlException.getMessage());
        }


        // Use the earthquakes GeoJSON source to create three layers: One layer for each cluster category.
        // Each point range gets a different image

        Bitmap blue_star = BitmapFactory.decodeResource(
                GeoJsonClusteringActivity.this.getResources(), R.drawable.star);

        Bitmap red_polygon = BitmapFactory.decodeResource(
                GeoJsonClusteringActivity.this.getResources(), R.drawable.polygon);

        Bitmap green_circle = BitmapFactory.decodeResource(
                GeoJsonClusteringActivity.this.getResources(), R.drawable.circle);

        Bitmap yellow_rectangle = BitmapFactory.decodeResource(
                GeoJsonClusteringActivity.this.getResources(), R.drawable.rectangle);

        // Add image to style's sprite sheet
        mapboxMap.addImage("blue_star", blue_star);
        mapboxMap.addImage("green_circle", green_circle);
        mapboxMap.addImage("red_polygon", red_polygon);
        mapboxMap.addImage("yellow_rectangle", yellow_rectangle);

        // Create a marker layer for single data points
        SymbolLayer unclustered = new SymbolLayer("unclustered-points", "earthquakes");
        unclustered.setProperties(iconImage("marker-15"));
        mapboxMap.addLayer(unclustered);


        // Add unique images for each cluster
        SymbolLayer clusterSymbols = new SymbolLayer("clustered-images", "earthquakes");

        // Create stops array
        Expression.Stop[] stops = new Expression.Stop[]{
                stop(0, "blue_star"),
                stop(20, "green_circle"),
                stop(150, "red_polygon")
        };

        Expression pointCount = toNumber(get("point_count"));
        
        clusterSymbols.setProperties(
                iconAllowOverlap(true),
                iconImage(Expression.step(pointCount, literal("yellow-rectangle"), stops)));

        mapboxMap.addLayer(clusterSymbols);

        //Add the cluster count labels
        SymbolLayer count = new SymbolLayer("count", "earthquakes");
        count.setProperties(
                textField("{point_count}"),
                textSize(12f),
                textColor(Color.BLACK)
        );
        mapboxMap.addLayer(count);

    }
}
