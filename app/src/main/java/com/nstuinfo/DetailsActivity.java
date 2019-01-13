package com.nstuinfo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nstuinfo.R;
import com.nstuinfo.mOtherUtils.Preferences;
import com.nstuinfo.mRecyclerView.MyAdapter;
import com.nstuinfo.mRecyclerView.SpacesItemDecoration;
import com.nstuinfo.mJsonUtils.ExtractDataJson;
import com.nstuinfo.mJsonUtils.ReadWriteJson;
import com.nstuinfo.mViews.FontAppearance;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private String title = null;

    private LinearLayout ll, rootLL;
    private ScrollView scrollView;

    private Toolbar toolbar;
    private TextView appBarTitleTV;
    private TextView footerDateTV;

    private ExtractDataJson extractDataJson;
    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private List<String> itemsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_details);

        invalidateOptionsMenu();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        appBarTitleTV = findViewById(R.id.appBarTitleTV);
        footerDateTV = findViewById(R.id.dateTV);

        scrollView = findViewById(R.id.detailsScroll);
        rootLL = findViewById(R.id.detailsMainLL);
        ll = findViewById(R.id.mainLL);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(1));

        title = getIntent().getStringExtra("TITLE");

        extractDataJson = new ExtractDataJson(this, ReadWriteJson.readDataFile(this), ll);

        if (title != null) {
            appBarTitleTV.setText(title);
            if (extractDataJson.hasContents(title)) {
                mRecyclerView.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                itemsList = extractDataJson.getSecondaryItemsList(title);
                loadRecyclerView(true);
            } else {
                extractDataJson.getView(title);
            }

            if (!extractDataJson.getUpdatedDate(title).equalsIgnoreCase("")) {
                footerDateTV.setVisibility(View.VISIBLE);
                footerDateTV.setText(extractDataJson.getUpdatedDate(title));
            }

        } else {
            appBarTitleTV.setText(getResources().getString(R.string.app_name));
        }

        setTheme();

        FontAppearance.replaceDefaultFont(this);

    }

    private void setTheme() {
        if (Preferences.isDarkTheme(this)) {
            rootLL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
            toolbar.setBackgroundColor(Color.BLACK);
            toolbar.setPopupTheme(R.style.PopupMenuDark);
            footerDateTV.setTextColor(Color.WHITE);
            footerDateTV.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
        }
    }

    private void loadRecyclerView(boolean isList) {
        myAdapter = new MyAdapter(this, itemsList, title, "second");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (extractDataJson.hasContents(title)) {
            getMenuInflater().inflate(R.menu.menu_details, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home){
            finish();
        } else if (id == R.id.menu_item_search){
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    String query = newText.toLowerCase();

                    final List<String> filteredList = new ArrayList<>();

                    for (int i = 0; i < itemsList.size(); i++) {
                        final String text = itemsList.get(i).toLowerCase();
                        if (text.contains(query)) {
                            filteredList.add(itemsList.get(i));
                        }
                    }

                    myAdapter = new MyAdapter(DetailsActivity.this, filteredList, title, "second");
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
                    mRecyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();

                    return false;
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
