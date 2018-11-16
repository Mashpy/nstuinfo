package com.mashpy.nstuinfo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.mashpy.nstuinfo.mJsonUtils.ExtractInitialJson;
import com.mashpy.nstuinfo.mOtherUtils.ExtraUtils;
import com.mashpy.nstuinfo.mOtherUtils.Preferences;
import com.mashpy.nstuinfo.mRecyclerView.MyAdapter;
import com.mashpy.nstuinfo.mRecyclerView.SpacesItemDecoration;
import com.mashpy.nstuinfo.mViews.FontAppearance;
import com.mashpy.nstuinfo.mJsonUtils.Constants;
import com.mashpy.nstuinfo.mJsonUtils.ExtractDataJson;
import com.mashpy.nstuinfo.mJsonUtils.ReadWriteJson;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TextView appBarTitleTV;
    private ImageButton syncBtn;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    MenuItem nav_last;
    private RelativeLayout navLL;

    private CoordinatorLayout coordinatorLayout;
    private ConstraintLayout mConstraintLayout;

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private List<String> itemsList;
    private ExtractDataJson dataJsonExtract;
    private ExtractInitialJson initialJsonExtract;

    private double jsonOfflineVersion = 0.0;
    private double jsonOnlineVersion = 0.0;

    private PopupWindow mPopUpWindow;

    private String initialFont;
    private boolean isInitialThemeDark = false;
    private boolean isInitialViewGrid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        setTheme();

        initialFont = Preferences.getFontAppearance(this);
        isInitialThemeDark = Preferences.isDarkTheme(this);
        isInitialViewGrid = Preferences.isGridView(this);

        if (ReadWriteJson.readDataFile(this).equals("") || ReadWriteJson.readInitialJsonFile(this).equals("")) {
            ReadWriteJson.saveDataFile(this, ReadWriteJson.readOfflineJsonFromAssets(this));
            dataJsonExtract = new ExtractDataJson(this, ReadWriteJson.readDataFile(this));
            if (itemsList != null) {
                itemsList.clear();
            }
            itemsList = dataJsonExtract.getMainItemsList();
            loadRecyclerView();
            nav_last.setTitle(itemsList.get(itemsList.size()-1));
        }

        if (ReadWriteJson.readInitialJsonFile(this).equals("")) {
            if (isInternetOn()) {
                parseUrlAndCheckData(true);
            }
        } else {
            if (isInternetOn()) {
                if (ReadWriteJson.readDataFile(this).equals("")) {
                    parseDataJson(null);
                }
                dataJsonExtract = new ExtractDataJson(this, ReadWriteJson.readDataFile(this));
                if (itemsList != null) {
                    itemsList.clear();
                }
                itemsList = dataJsonExtract.getMainItemsList();
                loadRecyclerView();
                nav_last.setTitle(itemsList.get(itemsList.size()-1));
                parseUrlAndCheckData(false);
            } else {
                dataJsonExtract = new ExtractDataJson(this, ReadWriteJson.readDataFile(this));
                if (itemsList != null) {
                    itemsList.clear();
                }
                itemsList = dataJsonExtract.getMainItemsList();
                loadRecyclerView();
                nav_last.setTitle(itemsList.get(itemsList.size()-1));
            }
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            if (isInitialViewGrid != Preferences.isGridView(this)) {
                loadRecyclerView();
                isInitialViewGrid = Preferences.isGridView(this);
            }

            if (isInitialThemeDark != Preferences.isDarkTheme(this)) {
                setTheme();
                loadRecyclerView();
                isInitialThemeDark = Preferences.isDarkTheme(this);
            }

            if (!initialFont.equals(Preferences.getFontAppearance(this))) {
                loadRecyclerView();
                initialFont = Preferences.getFontAppearance(this);
            }

        }
    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarTitleTV = findViewById(R.id.appBarTitleTV);
        appBarTitleTV.setText(getResources().getString(R.string.app_name));

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        syncBtn = findViewById(R.id.syncBtn);
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetOn()) {
                    parseUrlAndCheckData(true);
                } else {
                    Snackbar.make(view, "Please check your data connection!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                navViewImageAlteration();
            }
        });

        syncBtn.setVisibility(View.GONE);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(1));

        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN ||
                        event.getAction() == MotionEvent.ACTION_UP||
                        event.getAction() == MotionEvent.ACTION_SCROLL) {

                    syncBtn.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            syncBtn.setVisibility(View.INVISIBLE);
                        }
                    }, 4000);

                }

                return false;
            }
        });

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        mConstraintLayout = findViewById(R.id.homeConstraintLayout);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();
        nav_last = menu.findItem(R.id.nav_dev_msg);

        navigationView.setNavigationItemSelectedListener(this);

        navLL = navigationView.getHeaderView(0).findViewById(R.id.navLL);

        navViewImageAlteration();
    }

    private void navViewImageAlteration() {
        int rand = ExtraUtils.getRandomNumber(1,7);
        if (rand == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_1));
            }
        } else if (rand == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_2));
            }
        } else if (rand == 3) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_3));
            }
        } else if (rand == 4) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_4));
            }
        } else if (rand == 5) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_5));
            }
        } else if (rand == 6) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_6));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navLL.setBackground(getResources().getDrawable(R.drawable.nstu_cover_7));
            }
        }
    }

    private void setTheme() {
        if (Preferences.isDarkTheme(this)) {
            mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
            toolbar.setBackgroundColor(Color.BLACK);
            toolbar.setPopupTheme(R.style.PopupMenuDark);
            navigationView.setBackgroundColor(getResources().getColor(R.color.dark_color_secondary));
            navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
            navigationView.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
        } else {
            mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setPopupTheme(R.style.PopupMenuLight);
            navigationView.setBackgroundColor(Color.WHITE);
            navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
            navigationView.setItemIconTintList(ColorStateList.valueOf(Color.BLACK));
        }
    }

    private void loadRecyclerView() {
        myAdapter = new MyAdapter(this, itemsList, "main");

        if (Preferences.isGridView(this)) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        mRecyclerView.setAdapter(myAdapter);
    }

    private void parseUrlAndCheckData(final boolean pdVisibility) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Checking data....");
        progressDialog.setCancelable(true);
        if (pdVisibility) {
            progressDialog.show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.JSON_CHECKING_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (pdVisibility) {
                            progressDialog.dismiss();
                            initialJsonExtract = new ExtractInitialJson(HomeActivity.this, response);
                            jsonOnlineVersion = initialJsonExtract.getDataVersionFromInitialJson();
                            Constants.JSON_DATA_URL = initialJsonExtract.getDataUrl();

                            dataJsonExtract = new ExtractDataJson(HomeActivity.this, ReadWriteJson.readDataFile(HomeActivity.this));
                            double tempVersion = dataJsonExtract.getDataVersionFromDataJson();

                            initialJsonExtract.getPopupNotificationDialog();
                            if (ReadWriteJson.readInitialJsonFile(HomeActivity.this).equals("")) {
                                ReadWriteJson.saveInitialJsonFile(HomeActivity.this, response);
                                if (tempVersion < jsonOnlineVersion) {
                                    parseDataJson(null);
                                }
                            } else {
                                initialJsonExtract = new ExtractInitialJson(HomeActivity.this, ReadWriteJson.readInitialJsonFile(HomeActivity.this));
                                jsonOfflineVersion = initialJsonExtract.getDataVersionFromInitialJson();
                                if (jsonOfflineVersion < jsonOnlineVersion) {
                                    updatedDataVersionNoticeDialog(response);
                                } else {
                                    Toast.makeText(getApplicationContext(), "No updates found!!", Toast.LENGTH_LONG).show();
                                }
                            }

                        } else {
                            initialJsonExtract = new ExtractInitialJson(HomeActivity.this, response);
                            jsonOnlineVersion = initialJsonExtract.getDataVersionFromInitialJson();
                            initialJsonExtract.getPopupNotificationDialog();
                            Constants.JSON_DATA_URL = initialJsonExtract.getDataUrl();
                            initialJsonExtract = new ExtractInitialJson(HomeActivity.this, ReadWriteJson.readInitialJsonFile(HomeActivity.this));
                            jsonOfflineVersion = initialJsonExtract.getDataVersionFromInitialJson();
                            if (jsonOfflineVersion < jsonOnlineVersion) {
                                updatedDataVersionNoticeDialog(response);
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseDataJson(final String initialResponse) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching data....");
        progressDialog.setCancelable(true);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.JSON_DATA_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        dataJsonExtract = new ExtractDataJson(HomeActivity.this, response);
                        if (itemsList != null) {
                            itemsList.clear();
                        }
                        itemsList = dataJsonExtract.getMainItemsList();
                        loadRecyclerView();
                        nav_last.setTitle(itemsList.get(itemsList.size()-1));
                        Toast.makeText(getApplicationContext(), "Data is updated!!", Toast.LENGTH_LONG).show();
                        ReadWriteJson.saveDataFile(HomeActivity.this, response);
                        if (initialResponse != null) {
                            ReadWriteJson.saveInitialJsonFile(HomeActivity.this, initialResponse);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        getBaseContext();
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }

        return false;
    }

    boolean backButtonPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(backButtonPressedOnce){
                super.onBackPressed();
                return;
            }

            this.backButtonPressedOnce = true;
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please press back again to exit!!", Snackbar.LENGTH_SHORT);
            snackbar.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backButtonPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_search) {
            searchPopupWindow();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_theme) {
            popupThemeWindow();
        } else if (id == R.id.nav_font_appearance) {
            fontAppearanceDialog();
        } else if (id == R.id.nav_itemview) {
            itemViewDialog();
        } else if (id == R.id.nav_web_site) {
            startActivity(new Intent(this, WebviewActivity.class));
        } else if (id == R.id.nav_dev_msg) {
            if (itemsList != null) {
                Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("TITLE", itemsList.get(itemsList.size()-1).trim());
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("InflateParams")
    private void popupThemeWindow(){

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.theme_popup_window,null);
        }

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        mPopUpWindow = new PopupWindow(layout, width, height, true);

        RelativeLayout backDimRL = null;
        RelativeLayout mainRL = null;
        TextView titleTV = null;
        CircleMenu circleMenu = null;

        if (layout != null) {
            backDimRL = layout.findViewById(R.id.dimRL);
            mainRL = layout.findViewById(R.id.main_popup);
            titleTV = layout.findViewById(R.id.popUpTitleTV);
            circleMenu = layout.findViewById(R.id.circleMenu);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( (int) (width*.95), (int) (height*.84) );

        assert mainRL != null;
        mainRL.setLayoutParams(params);

        titleTV.setText("Theme");

        FontAppearance.setPrimaryTextSize(this, titleTV);

        if (Preferences.isDarkTheme(HomeActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mainRL.setBackground(getResources().getDrawable(R.drawable.popup_dark_shape));
                titleTV.setBackground(getResources().getDrawable(R.drawable.popup_dark_title_shape));
            } else {
                mainRL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
                titleTV.setBackgroundColor(Color.BLACK);
            }
            backDimRL.setBackgroundColor(getResources().getColor(R.color.dim_white));
        }

        assert backDimRL != null;
        backDimRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopUpWindow.dismiss();
            }
        });

        mainRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing to do
            }
        });

        circleMenu.setMainMenu(
                getResources().getColor(R.color.colorPrimary),
                R.mipmap.ic_add,
                R.mipmap.ic_remove)
                .addSubMenu(Color.WHITE, R.mipmap.ic_sun)
                .addSubMenu(Color.BLACK, R.mipmap.ic_cloud)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        if (index == 0) {
                            Preferences.setDarkTheme(HomeActivity.this, false);
                        } else if (index == 1) {
                            Preferences.setDarkTheme(HomeActivity.this, true);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPopUpWindow.dismiss();
                            }
                        }, 1500);
                    }
                });

        //Set up touch closing outside of pop-up
        mPopUpWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_bg));
        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopUpWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mPopUpWindow.setOutsideTouchable(true);

        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setContentView(layout);
        mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    private void fontAppearanceDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.font_appearance_dialog);
        dialog.setCancelable(true);

        RelativeLayout dlgMainRL = dialog.findViewById(R.id.dialogMainRL);
        TextView dlgTitleTV = dialog.findViewById(R.id.fontDialogTitleTV);
        final RadioButton smallRB = dialog.findViewById(R.id.smallFontRB);
        final RadioButton mediumRB = dialog.findViewById(R.id.mediumFontRB);
        final RadioButton largeRB = dialog.findViewById(R.id.largeFontRB);

        FontAppearance.setPrimaryTextSize(this, dlgTitleTV);

        if (Preferences.isDarkTheme(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                dlgMainRL.setBackground(getResources().getDrawable(R.drawable.popup_dark_shape));
                dlgTitleTV.setBackground(getResources().getDrawable(R.drawable.popup_dark_title_shape));
            } else {
                dlgMainRL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
                dlgTitleTV.setBackgroundColor(getResources().getColor(R.color.dark_color_secondary));
            }
            dlgTitleTV.setTextColor(Color.WHITE);
            smallRB.setTextColor(Color.WHITE);
            mediumRB.setTextColor(Color.WHITE);
            largeRB.setTextColor(Color.WHITE);
        }

        if (Preferences.getFontAppearance(HomeActivity.this).equals(Preferences.MEDIUM_FONT)){
            mediumRB.setChecked(true);
        } else if (Preferences.getFontAppearance(HomeActivity.this).equals(Preferences.LARGE_FONT)){
            largeRB.setChecked(true);
        } else {
            smallRB.setChecked(true);
        }

        smallRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallRB.setChecked(true);
                mediumRB.setChecked(false);
                largeRB.setChecked(false);
                Preferences.setFontAppearance(HomeActivity.this, Preferences.SMALL_FONT);
                dialog.dismiss();
            }
        });

        mediumRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallRB.setChecked(false);
                mediumRB.setChecked(true);
                largeRB.setChecked(false);
                Preferences.setFontAppearance(HomeActivity.this, Preferences.MEDIUM_FONT);
                dialog.dismiss();
            }
        });

        largeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallRB.setChecked(false);
                mediumRB.setChecked(false);
                largeRB.setChecked(true);
                Preferences.setFontAppearance(HomeActivity.this, Preferences.LARGE_FONT);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void itemViewDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_view_dialog);
        dialog.setCancelable(true);

        RelativeLayout dlgMainRL = dialog.findViewById(R.id.dialogMainRL);
        TextView dlgTitleTV = dialog.findViewById(R.id.viewDialogTitleTV);
        final RadioButton listRB = dialog.findViewById(R.id.ListRB);
        final RadioButton gridRB = dialog.findViewById(R.id.GridRB);

        FontAppearance.setPrimaryTextSize(this, dlgTitleTV);

        if (Preferences.isDarkTheme(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                dlgMainRL.setBackground(getResources().getDrawable(R.drawable.popup_dark_shape));
                dlgTitleTV.setBackground(getResources().getDrawable(R.drawable.popup_dark_title_shape));
            } else {
                dlgMainRL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
                dlgTitleTV.setBackgroundColor(getResources().getColor(R.color.dark_color_secondary));
            }
            dlgTitleTV.setTextColor(Color.WHITE);
            listRB.setTextColor(Color.WHITE);
            gridRB.setTextColor(Color.WHITE);
        }

        if (Preferences.isGridView(this)){
            gridRB.setChecked(true);
        } else {
            listRB.setChecked(true);
        }

        listRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRB.setChecked(true);
                gridRB.setChecked(false);
                Preferences.setGridView(HomeActivity.this, false);
                dialog.dismiss();
            }
        });

        gridRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRB.setChecked(false);
                gridRB.setChecked(true);
                Preferences.setGridView(HomeActivity.this, true);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updatedDataVersionNoticeDialog(final String response) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#0D47A1'>Update notice!!</font>"));
        builder.setMessage(Html.fromHtml("<font color='#000000'>New data version has been arrived. Do you want to update app data?</font>"));
        builder.setPositiveButton(Html.fromHtml("YES"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                parseDataJson(response);
                dialog.cancel();
            }
        });
        builder.setNegativeButton(Html.fromHtml("LATER"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void searchPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.search_popup_window,null);
        }

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        mPopUpWindow = new PopupWindow(layout, width, height, true);

        RelativeLayout backDimRL = null;
        RelativeLayout mainRL = null;
        RelativeLayout etBGRL = null;
        EditText editText = null;
        RecyclerView recyclerView = null;

        if (layout != null) {
            backDimRL = layout.findViewById(R.id.dimRL);
            mainRL = layout.findViewById(R.id.main_popup);
            etBGRL = layout.findViewById(R.id.ETRL);
            editText = layout.findViewById(R.id.popUpSearchET);
            recyclerView = layout.findViewById(R.id.RecyclerVIew);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( (int) (width*.95), (int) (height*.9) );

        assert mainRL != null;
        mainRL.setLayoutParams(params);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));

        if (Preferences.isDarkTheme(HomeActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mainRL.setBackground(getResources().getDrawable(R.drawable.popup_dark_shape));
                etBGRL.setBackground(getResources().getDrawable(R.drawable.popup_dark_title_shape));
            } else {
                mainRL.setBackgroundColor(getResources().getColor(R.color.dark_color_primary));
                etBGRL.setBackgroundColor(Color.BLACK);
            }
            backDimRL.setBackgroundColor(getResources().getColor(R.color.dim_white));
        }

        searchContent(editText, recyclerView);

        assert backDimRL != null;
        backDimRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopUpWindow.dismiss();
            }
        });

        mainRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing to do
            }
        });

        //Set up touch closing outside of pop-up
        mPopUpWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_bg));
        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopUpWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mPopUpWindow.setOutsideTouchable(true);

        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setContentView(layout);
        mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    private void searchContent(EditText editText, final RecyclerView rcView) {

        final List<String> contentList = dataJsonExtract.getAllContents();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String query = s.toString().toLowerCase();

                final List<String> filteredList = new ArrayList<>();

                for (int i = 0; i < contentList.size(); i++) {
                    final String text = contentList.get(i).toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(contentList.get(i));
                    }
                }

                if (filteredList.size() > 25) {
                    MyAdapter adapter = new MyAdapter(HomeActivity.this, filteredList.subList(0, 25), "content");
                    rcView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    rcView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    MyAdapter adapter = new MyAdapter(HomeActivity.this, filteredList, "content");
                    rcView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    rcView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        navViewImageAlteration();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPopUpWindow != null) {
            mPopUpWindow.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopUpWindow != null) {
            mPopUpWindow.dismiss();
        }
    }
}
