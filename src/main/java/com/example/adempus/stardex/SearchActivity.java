package com.example.adempus.stardex;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class SearchActivity extends AppCompatActivity {
    private static Context mainAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainAppContext = getApplicationContext();
        setContentView(R.layout.activity_search);
        setActivityBackgroundColor();
    }

    public void setActivityBackgroundColor() {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.rgb(85, 29, 130));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(
                menu.findItem(R.id.star_search)
        );
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName())
        );
        return true;
    }

    public static Context getMainAppContext() {
        return mainAppContext;
    }
}