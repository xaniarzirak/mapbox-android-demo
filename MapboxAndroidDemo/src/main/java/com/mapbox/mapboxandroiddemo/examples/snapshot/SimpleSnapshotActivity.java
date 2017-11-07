package com.mapbox.mapboxandroiddemo.examples.snapshot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;

import java.util.Random;

import timber.log.Timber;

/**
 * The most basic example of adding a map to an activity.
 */
public class SimpleSnapshotActivity extends AppCompatActivity {

  private RelativeLayout layout;
  private MapSnapshotter snapshotter;
  private ImageView mapImage;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    setContentView(R.layout.activity_snapshot_simple_snapshot);

    // start snapshotting as soon as the view is measured
    layout = (RelativeLayout) findViewById(R.id.top_layout);
    layout.getViewTreeObserver()
      .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          //noinspection deprecation
          layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
          addSnapshot(layout.getWidth(), layout.getHeight()/2);
        }
      });
  }


  @Override
  public void onPause() {
    super.onPause();

    // Make sure to stop the snapshotter on pause
    snapshotter.cancel();
  }

  private void addSnapshot(int width, int height) {

    if (snapshotter == null) {
      // Define the dimensions
      MapSnapshotter.Options options = new MapSnapshotter.Options(width, height)
        // Optionally the pixel ratio
        .withPixelRatio(1)

        // Optionally the style
        .withStyle(Style.SATELLITE);

      // Optionally the visible region
      options.withRegion(new LatLngBounds.Builder()
        .include(new LatLng(randomInRange(-80, 80), randomInRange(-160, 160)))
        .include(new LatLng(randomInRange(-80, 80), randomInRange(-160, 160)))
        .build());

      // Optionally the camera options
//    options.withCameraPosition(new CameraPosition.Builder()
//        .target(options.getRegion() != null
//          ? options.getRegion().getCenter()
//          : new LatLng(randomInRange(-80, 80), randomInRange(-160, 160)))
//        .bearing(randomInRange(0, 360))
//        .tilt(randomInRange(0, 60))
//        .zoom(randomInRange(0, 20))
//        .build()
//      );

      snapshotter = new MapSnapshotter(SimpleSnapshotActivity.this, options);
    }

    snapshotter.start(new MapSnapshotter.SnapshotReadyCallback() {
      @Override
      public void onSnapshotReady(MapSnapshot snapshot) {
        Timber.i("Got the snapshot");
        mapImage = new ImageView(SimpleSnapshotActivity.this);
        mapImage.setImageBitmap(snapshot.getBitmap());
        layout.addView(mapImage);

        mapImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (isFullScreen) {
                hideFullScreen();
              } else {
                showFullScreen();
              }
          }
        });

      }

    });
  }

  private boolean isFullScreen = false;

  private void showFullScreen() {
    if (!isFullScreen) {
      isFullScreen = true;
      mapImage.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
      mapImage.setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }
  }

  private void hideFullScreen() {
    if (isFullScreen) {
      isFullScreen = false;
      mapImage.setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
          | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
          | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

  }

  private static Random random = new Random();

  public static float randomInRange(float min, float max) {
    return (random.nextFloat() * (max - min)) + min;
  }


}