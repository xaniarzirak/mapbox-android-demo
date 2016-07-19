package com.mapbox.mapboxandroiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mapbox.mapboxandroiddemo.adapter.ExampleAdapter;
import com.mapbox.mapboxandroiddemo.model.ExampleItemModel;
import com.mapbox.mapboxandroiddemo.utils.ItemClickSupport;
import com.mapbox.mapboxsdk.MapboxAccountManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<ExampleItemModel> exampleItemModel;
    private ExampleAdapter adapter;
    private int currentCategory = R.id.nav_basics;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Mapbox access token is configured here.
        MapboxAccountManager.start(this, getString(R.string.access_token));

        exampleItemModel = new ArrayList<>();

        // Create the adapter to convert the array to views
        adapter = new ExampleAdapter(this, exampleItemModel);
        // Attach the adapter to a ListView
        recyclerView = (RecyclerView) findViewById(R.id.details_list);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
        if (savedInstanceState == null) {
            listItems(R.id.nav_basics);
        } else {
            currentCategory = savedInstanceState.getInt("CURRENT_CATEGORY");
            listItems(currentCategory);
        }

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                startActivity(exampleItemModel.get(position).getActivity());

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_basics);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                moveTaskToBack(true);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id != currentCategory) {

            listItems(id);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void listItems(int id) {
        exampleItemModel.clear();
        switch (id) {
            case R.id.nav_basics:

                exampleItemModel.add(new ExampleItemModel(R.string.activity_basic_polygon_with_line_title,R.string.activity_basic_polygon_with_line_description, new Intent(MainActivity.this, PolygonWithLineActivity.class), R.string.polygon_url));
                exampleItemModel.add(new ExampleItemModel(R.string.activity_basic_circle_marker_title, R.string.activity_basic_circle_marker_description, new Intent(MainActivity.this, CircleMarkerActivity.class), R.string.circle_url));
                currentCategory = R.id.nav_basics;
                break;

        }
        adapter.notifyDataSetChanged();

        // Scrolls recycler view back to top.
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);
    }

    public int getCurrentCategory() {
        return currentCategory;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate toolbar items
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CURRENT_CATEGORY", currentCategory);
    }
}