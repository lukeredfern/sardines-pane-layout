package com.redfern.luke.sardines;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    ImageButton addItemBtn;
    ImageButton submitItemBtn;
    View itemEditView;
    SupportAnimator itemEditAnimator;

    ColorPickerPagerAdapter pickerPagerAdapter;
    View itemEditBackground;
    ViewPager pickerPager;

    CrossFadeSlidingPaneLayout slidingPaneLayout;
    SlidingTopPanel topPanel;
    LinearLayout parentLayout;

    ViewGroup revealLayout;
    ViewGroup itemListHolder;

    ArrayList<Item> items = new ArrayList<>();
    ItemRecyclerViewAdapter itemRecyclerAdapter;
    RecyclerView itemRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        slidingPaneLayout = (CrossFadeSlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
        slidingPaneLayout.setSliderFadeColor(Color.TRANSPARENT);
        revealLayout = (ViewGroup) findViewById(R.id.reveal_frame_layout);
        topPanel = (SlidingTopPanel) findViewById(R.id.top_pane);

        initItemPane();


        // Make activity move up to accommodate keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // set screen height for top panel
        parentLayout = (LinearLayout) findViewById(R.id.top_level_linear_layout);

    }

    private ArrayList<Item> randomItemList(int size) {

        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Item newItem = new Item(randInt(10, 50), randInt(10, 50), randInt(10, 50), (int) randInt(1,3), 0, 0,"Custom Item "+(i+1));
            newItem.setUnits(1);
            newItem.setColor((int) randInt(0,16));
            items.add(newItem);

        }
        return items;
    }

    public static double randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private void initItemPane() {
        addItemBtn = (ImageButton) findViewById(R.id.add_item);
        itemEditView = findViewById(R.id.item_edit_layout);
        itemEditView.setVisibility(View.INVISIBLE);
        itemEditBackground = findViewById(R.id.item_edit_background);
        itemEditBackground.setVisibility(View.INVISIBLE);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemEditView();
                revealEditItemView(addItemBtn);
            }
        });

        //generate random items
        items = randomItemList(10);
        // RecyclerView
        itemRecyclerView = (RecyclerView) findViewById(R.id.item_recycler_view);
        itemRecyclerAdapter = new ItemRecyclerViewAdapter(this, items);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemRecyclerView.setAdapter(itemRecyclerAdapter);
    }

    /** Temporary Values*/
    int itemColor = 2;
    private void updateItemEditView(){
        itemEditView.setBackgroundColor(MaterialColor.getColor(itemColor, 7));

        LinearLayout itemEditContent = (LinearLayout) findViewById(R.id.item_edit_content);
        int delta = itemEditView.getHeight()-itemEditContent.getHeight();
        if (delta<0) {
            topPanel.animatePanelDelta(delta);
        }

        pickerPager = (ViewPager) findViewById(R.id.item_color_view_pager);
        pickerPagerAdapter =
                new ColorPickerPagerAdapter(this,pickerPager.getWidth(),pickerPager.getHeight(),17,itemColor);
        pickerPager.setAdapter(pickerPagerAdapter);
        pickerPager.setOffscreenPageLimit(10);
        pickerPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (
                        event.getAction() == MotionEvent.ACTION_DOWN &&
                                v instanceof ViewGroup
                        ) {
                    ((ViewGroup) v).requestDisallowInterceptTouchEvent(true);
                }
                if (
                        event.getAction() == MotionEvent.ACTION_UP &&
                                v instanceof ViewGroup
                        ) {
                    pickerPagerAdapter.setEndPoint(v.getLeft() + event.getX(), event.getY());
                }


                return false;
            }
        });

        submitItemBtn = (ImageButton) findViewById(R.id.submit_item);
        submitItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditItemView();
                submitItemBtn.setEnabled(false);
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    public void changeActiveItemColor(int n, int px, int py){

        final View itemEditBackgroundOld = new View(context);
        itemEditBackgroundOld.setBackgroundColor(MaterialColor.getColor(itemColor, 7));
        revealLayout.addView(itemEditBackgroundOld,0);
        itemColor = n;
        itemEditBackground.setBackgroundColor(MaterialColor.getColor(itemColor, 7));

        // get the final radius for the clipping circle
        int dx = itemEditBackground.getWidth();
        int dy = itemEditBackground.getHeight();
        float finalRadius = (float) Math.hypot(dx, dy);

        int cx = px;
        int cy = dy-py;

        SupportAnimator itemBackgroundAnimator =
                ViewAnimationUtils.createCircularReveal(itemEditBackground, cx, cy, 0, finalRadius);
        itemBackgroundAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        itemBackgroundAnimator.setDuration(300);
        itemBackgroundAnimator.addListener(new SupportAnimator.SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd() {
                super.onAnimationEnd();
                revealLayout.removeView(itemEditBackgroundOld);
            }
        });
        itemBackgroundAnimator.start();
    }

    public float getBottomPanelHeight(){
        //Log.d("paneltop", String.valueOf(topPanel.getBottom()));
        return slidingPaneLayout.getHeight();
    }

    private void revealEditItemView(View clickView) {
        itemEditView.setVisibility(View.VISIBLE);
        // get the center for the clipping circle
        int cx = (clickView.getLeft() + clickView.getRight()) / 2;
        int cy = (clickView.getTop() + clickView.getBottom()) / 2;

        // get the final radius for the clipping circle
        int dx = itemEditView.getWidth();
        int dy = itemEditView.getHeight();
        float finalRadius = (float) Math.hypot(dx, dy);

        itemEditAnimator =
                ViewAnimationUtils.createCircularReveal(itemEditView, cx, cy, 0, finalRadius);
        itemEditAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        itemEditAnimator.setDuration(300);
        itemEditAnimator.addListener(new SupportAnimator.SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd() {
                super.onAnimationEnd();
                submitItemBtn.setEnabled(true);
                itemEditBackground.setBackgroundColor(MaterialColor.getColor(itemColor, 7));
                itemEditBackground.setVisibility(View.VISIBLE);
                itemEditBackground.invalidate();
                itemEditView.setBackgroundColor(Color.TRANSPARENT);
                itemEditView.invalidate();
            }
        });
        itemEditAnimator.start();
    }

    private void hideEditItemView(){
        itemEditView.setBackgroundColor(MaterialColor.getColor(itemColor, 7));
        itemEditView.invalidate();
        itemEditBackground.setVisibility(View.INVISIBLE);
        itemEditBackground.invalidate();
        itemEditAnimator = itemEditAnimator.reverse();
        itemEditAnimator.removeAllListeners();
        itemEditAnimator.addListener(new SupportAnimator.SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd() {
                super.onAnimationEnd();
                itemEditView.setVisibility(View.INVISIBLE);
            }
        });
        itemEditAnimator.start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
