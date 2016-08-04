package com.mapbox.mapboxandroiddemo;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PolylinePolygonOrderingActivity extends AppCompatActivity {

    private static final String TAG = "ZOrderingActivity";

    private MapView mapView;
    private MapboxMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_polyline_polygon_z_ordering);

        final Button onTop = (Button) findViewById(R.id.button_on_top);
        final Button onBottom = (Button) findViewById(R.id.button_on_bottom);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                onTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Remove all the annotation from the map, you can also remove only certain
                        // polylines or polygons specifically using map.removePolygon(); or map.removePolyline();
                        map.removeAnnotations();

                        // Add the polygons to the map in the order you want them in z index.
                        new DrawGeoJSON("los_angeles_airport.geojson", "#e55e5e", 1f).execute();
                        new DrawGeoJSON("circle1.geojson", "#3887be", 1f).execute();
                        new DrawGeoJSON("circle2.geojson", "#3887be", 1f).execute();

                        // Handler is needed since we are loading the polygon in from files. It also
                        // emphasis that z ordering of annotations depends on the time it was added
                        // to the map.
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                newPolyline();
                            }
                        }, 200);

                    }
                });


                onBottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        map.removeAnnotations();

                        newPolyline();

                        new DrawGeoJSON("circle1.geojson", "#3887be", 1f).execute();
                        new DrawGeoJSON("circle2.geojson", "#3887be", 1f).execute();
                        new DrawGeoJSON("los_angeles_airport.geojson", "#e55e5e", 1f).execute();

                    }
                });
            }
        });
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

    private void newPolyline() {
        List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(33.98446161869916, -118.43485947568665));
        points.add(new LatLng(33.92141733335159, -118.39908953626404));

        map.addPolyline(new PolylineOptions()
                .addAll(points)
                .color(Color.parseColor("#56b881"))
                .width(6));
    }

    private class DrawGeoJSON extends AsyncTask<Void, Void, List<LatLng>> {

        private String file;
        private String colorValue;
        private float alpha;

        public DrawGeoJSON(String file, String colorValue, float alpha) {
            super();
            this.file = file;
            this.alpha = alpha;
            this.colorValue = colorValue;
        }

        @Override
        protected List<LatLng> doInBackground(Void... voids) {

            ArrayList<LatLng> points = new ArrayList<>();

            try {
                // Load GeoJSON file
                InputStream inputStream = getAssets().open(file);
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }

                inputStream.close();

                // Parse JSON
                JSONObject json = new JSONObject(sb.toString());
                JSONArray features = json.getJSONArray("features");
                JSONObject feature = features.getJSONObject(0);
                JSONObject geometry = feature.getJSONObject("geometry");
                if (geometry != null) {
                    String type = geometry.getString("type");

                    // Our GeoJSON only has one feature: a line string
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {

                        // Get the Coordinates
                        JSONArray coords = geometry.getJSONArray("coordinates");
                        for (int lc = 0; lc < coords.length(); lc++) {
                            JSONArray coord = coords.getJSONArray(lc);
                            LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                            points.add(latLng);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception Loading GeoJSON: " + e.toString());
            }

            return points;
        }

        @Override
        protected void onPostExecute(List<LatLng> points) {
            super.onPostExecute(points);

            if (points.size() > 0) {
                LatLng[] pointsArray = points.toArray(new LatLng[points.size()]);

                // Draw Points on MapView
                map.addPolygon(new PolygonOptions()
                        .add(pointsArray)
                        .fillColor(Color.parseColor(colorValue))
                        .alpha(alpha));
            }
        }
    }
}