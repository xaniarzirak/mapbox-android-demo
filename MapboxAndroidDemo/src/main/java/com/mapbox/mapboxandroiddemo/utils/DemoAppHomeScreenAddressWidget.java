package com.mapbox.mapboxandroiddemo.utils;

// #-code-snippet: homescreen-address-widget-activity full-java

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.ContextWrapper;
import android.location.Location;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxandroiddemo.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.support.constraint.Constraints.TAG;

/**
 * Use the Mapbox Java SDK to make a reverse geocode with the device's current coordiates. Display
 * the address in an Android home screen widget.
 */
public class DemoAppHomeScreenAddressWidget extends AppWidgetProvider implements
  LocationEngineListener, PermissionsListener {

  private PermissionsManager permissionsManager;
  private LocationEngine locationEngine;
  private Context context;
  private int singleWidgetId;
  private Location lastLocation;
  private AppWidgetManager appWidgetManager;

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    this.singleWidgetId = appWidgetIds[0];
    this.appWidgetManager = appWidgetManager;
    this.context = context;
    enableLocationPlugin(context);
  }

  @Override
  public void onEnabled(Context context) {
    // Add message/UI here if you'd like
  }

  @Override
  public void onPermissionResult(boolean granted) {
    if (granted) {
      initializeLocationEngine(context);
    } else {
      Toast.makeText(context, R.string.user_location_permission_not_granted, Toast.LENGTH_SHORT).show();
    }
  }

  private void enableLocationPlugin(Context context) {
    // Check if permissions are enabled and if not request
    try {
      if (PermissionsManager.areLocationPermissionsGranted(context)) {
        Timber.d("enableLocationPlugin: permissions granted");

        // Create a location engine instance
        initializeLocationEngine(context);
      } else {
        Timber.d("enableLocationPlugin: permissions not granted yet");
        permissionsManager = new PermissionsManager(this);
        permissionsManager.requestLocationPermissions(getActivity(context));
      }
    } catch (NullPointerException nullPointerException) {
      Toast.makeText(context, context.getString(R.string.enable_location), Toast.LENGTH_LONG).show();

    }

  }

  @SuppressWarnings( {"MissingPermission"})
  private void initializeLocationEngine(Context context) {
    locationEngine = new LocationEngineProvider(context).obtainBestLocationEngineAvailable();
    locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
    locationEngine.addLocationEngineListener(this);
    locationEngine.activate();
    lastLocation = locationEngine.getLastLocation();
    if (lastLocation != null) {
      getReverseGeocodeData(lastLocation, context);
    }
  }

  public Activity getActivity(Context context) {
    if (context == null) {
      return null;
    } else if (context instanceof ContextWrapper) {
      if (context instanceof Activity) {
        return (Activity) context;
      } else {
        return getActivity(((ContextWrapper) context).getBaseContext());
      }
    }
    return null;
  }

  private void getReverseGeocodeData(Location currentDeviceLocation, final Context finalContext) {

    // Build a Mapbox reverse geocode request
    MapboxGeocoding reverseGeocode = MapboxGeocoding.builder()
      .accessToken(finalContext.getString(R.string.access_token))
      .query(Point.fromLngLat(currentDeviceLocation.getLongitude(), currentDeviceLocation.getLatitude()))
      .build();

    reverseGeocode.enqueueCall(new Callback<GeocodingResponse>() {
      @Override
      public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
        try {
          List<CarmenFeature> carmenFeatureList = response.body().features();
          // Check that the reverse geocoding response has a place name to display
          if (carmenFeatureList.size() > 0 && !carmenFeatureList.get(0).placeName().isEmpty()) {

            // Parse through the response to get the place name
            String placeName = carmenFeatureList.get(0).placeName();

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(finalContext.getPackageName(), R.layout.demo_app_home_screen_widget);
            views.setTextViewText(R.id.device_location_textview, placeName);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(singleWidgetId, views);

          } else {
            Timber.d("onResponse: no place name");
          }

        } catch (NullPointerException exception) {
          Timber.d("onResponse: exception = " + exception);
        }

      }

      @Override
      public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
        Timber.d("onFailure: geocoding failure");
        throwable.printStackTrace();
      }
    });
  }

  @Override
  public void onDisabled(Context context) {
    // Add message/UI here if you'd like
  }

  @Override
  @SuppressWarnings( {"MissingPermission"})
  public void onConnected() {
    // Add message/UI here if you'd like
    locationEngine.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {
    if (location != null) {
      getReverseGeocodeData(location, context);
    } else {
      locationEngine.addLocationEngineListener(this);
    }
  }

  @Override
  public void onExplanationNeeded(List<String> permissionsToExplain) {
    // Add explanation here if you'd like
    Toast.makeText(context, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
  }
}
// #-end-code-snippet: homescreen-address-widget-activity full-java
