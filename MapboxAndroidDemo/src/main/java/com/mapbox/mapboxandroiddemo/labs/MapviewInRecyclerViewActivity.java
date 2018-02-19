package com.mapbox.mapboxandroiddemo.labs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Show a mapview and other information in a single recyclerview item
 */

public class MapviewInRecyclerViewActivity extends AppCompatActivity {

  private MapView mapView;
  private RecyclerView recyclerView;
  private FishLocationRecyclerViewAdapter locationAdapter;
  private ArrayList<SingleRecyclerViewFishLocation> caughtFishLocationList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_mapviews_in_recyclerview);

    recyclerView = findViewById(R.id.fish_location_recyclerview);

    setUpRvList();

    // Set up the recyclerView
    locationAdapter = new FishLocationRecyclerViewAdapter(caughtFishLocationList);
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
        LinearLayoutManager.HORIZONTAL, true));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(locationAdapter);
    SnapHelper snapHelper = new LinearSnapHelper();
    snapHelper.attachToRecyclerView(recyclerView);

  }

  private void setUpRvList() {

    caughtFishLocationList = new ArrayList<>();

    LatLng[] coordinates = new LatLng[]{
        new LatLng(-34.6054099, -58.363654800000006),
        new LatLng(-34.6041508, -58.38555650000001),
        new LatLng(-34.6114412, -58.37808899999999),
        new LatLng(-34.6097604, -58.382064000000014),
        new LatLng(-34.596636, -58.373077999999964),
    };

    String[] fishNames = {"Bass", "Grouper", "Catfish", "Eel", "Tilapia"};
    String[] fishLocation = {"Muddy water in weeds", "In deep clear water", "Near the river bank",
        "In rushing rapids near rocks", "Next to the waterfall"};

    for (int x = 0; x < 4; x++) {
      SingleRecyclerViewFishLocation singleLocation = new SingleRecyclerViewFishLocation();
      singleLocation.setFishName(fishNames[x]);
      singleLocation.setFishLocation(fishLocation[x]);
      singleLocation.setFishCatchCoordinates(coordinates[x]);
      caughtFishLocationList.add(singleLocation);
    }
  }

  /**
   * POJO model class for a single fish location in the recyclerview
   */
  class SingleRecyclerViewFishLocation {

    private String fishName;
    private String fishLocation;
    private LatLng fishCatchCoordinates;

    public String getFishName() {
      return fishName;
    }

    public void setFishName(String fishName) {
      this.fishName = fishName;
    }

    public String getFishLocation() {
      return fishLocation;
    }

    public void setFishLocation(String fishLocation) {
      this.fishLocation = fishLocation;
    }

    public LatLng getFishCatchCoordinates() {
      return fishCatchCoordinates;
    }

    public void setFishCatchCoordinates(LatLng fishCatchCoordinates) {
      this.fishCatchCoordinates = fishCatchCoordinates;
    }
  }

  static class FishLocationRecyclerViewAdapter extends
      RecyclerView.Adapter<FishLocationRecyclerViewAdapter.MyViewHolder> {

    private List<SingleRecyclerViewFishLocation> fishLocationList;

    public FishLocationRecyclerViewAdapter(List<SingleRecyclerViewFishLocation> locationList) {
      this.fishLocationList = locationList;
    }

    @Override
    public FishLocationRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View itemView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.layout_mapview_in_rv_item, parent, false);
      return new FishLocationRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FishLocationRecyclerViewAdapter.MyViewHolder holder, int position) {
      SingleRecyclerViewFishLocation singleRecyclerViewFishLocation = fishLocationList.get(position);
      holder.fishName.setText(singleRecyclerViewFishLocation.getFishName());
      holder.fishLocation.setText(singleRecyclerViewFishLocation.getFishLocation());
      holder.setClickListener(new ItemClickListener() {
        @Override
        public void onClick(View view, int position) {


        }
      });
    }

    @Override
    public int getItemCount() {
      return fishLocationList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
      TextView fishName;
      TextView fishLocation;
      CardView singleCard;
      ItemClickListener clickListener;

      MyViewHolder(View view) {
        super(view);
        fishName = view.findViewById(R.id.fish_name_textview);
        fishLocation = view.findViewById(R.id.fish_location_textview);
        singleCard = view.findViewById(R.id.fish_recyclerview_card);
        singleCard.setOnClickListener(this);
      }

      public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
      }

      @Override
      public void onClick(View view) {
        clickListener.onClick(view, getLayoutPosition());
      }
    }
  }

  public interface ItemClickListener {
    void onClick(View view, int position);
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