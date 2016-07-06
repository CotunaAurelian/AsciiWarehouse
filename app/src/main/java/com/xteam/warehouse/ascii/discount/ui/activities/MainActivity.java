package com.xteam.warehouse.ascii.discount.ui.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.xteam.warehouse.ascii.discount.R;
import com.xteam.warehouse.ascii.discount.business.datahandling.ProductsSearchListener;
import com.xteam.warehouse.ascii.discount.business.datastorage.SharedPreferenceManager;
import com.xteam.warehouse.ascii.discount.business.datahandling.DataManager;
import com.xteam.warehouse.ascii.discount.model.dto.AsciiProductDTO;
import com.xteam.warehouse.ascii.discount.ui.AsciiWarehouseConstants;
import com.xteam.warehouse.ascii.discount.ui.adapters.ProductsAdapter;
import com.xteam.warehouse.ascii.discount.ui.views.OnRecyclerItemClickListener;

public class MainActivity extends AppCompatActivity implements ProductsSearchListener, OnRecyclerItemClickListener {


    /**
     * The span count for the grid used to display products.
     */
    private static final int SPAN_COUNT = 3;

    /**
     * Recycler view that will hold a grid with products displayed on the screen
     */
    private RecyclerView mRecyclerView;

    /**
     * Grid layout manager used to layout the elements. The layout manager defines how the elements are displayed in a {@link RecyclerView}
     */
    private GridLayoutManager mGridLayoutManager;

    /**
     * The adapter that will hold the data needed by the recycler view, and will handle the user interaction with the items
     */
    private ProductsAdapter mAdapter;

    /**
     * The text view displayed when there is no data available, or no result returned from the web server, or something was wrong with the request
     */
    private TextView mNoDataTextView;
    /**
     * The container for the ImageView and TextView for the error loading messages. This is used to show or hide the whole functionality
     * for the loading error ImageView and TextView.
     */
    private LinearLayout mErrorDataContainer;

    /**
     * Search view in the toolbar
     */
    private MaterialSearchView mSearchView;

    /**
     * Flag used to prevent the recyclerview to keep firing new requests when a request for data is ongoing
     */
    private boolean mIsDataLoading;


    /**
     * The image view that will bounce until the loading is complete.
     */
    private ImageView mLoadingImageView;

    /**
     * The text view that will display a message when the loading is ongoing
     */
    private TextView mLoadingText;

    /**
     * The container for the ImageView and TextView for the loading messages and animation. This is used to show or hide the whole functionality
     * for the loading ImageView and TextView.
     */
    private LinearLayout mLoadingContainer;

    /**
     * The searched words that the user completes into the search bar. If this string contains information, it will trigger a force call to the
     * server and the cache will be bypassed.
     */
    private List<String> mSearchQuery;

    /**
     * The SwipeRefreshLayout should be used to refresh the content of the screen via a vertical swipe gesture.
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        customizeToolbar();
        initializeLoadingViews();
        initializeSearchView();
        initializeSwipeToRefreshViews();
        initializeProductsRecyclerView();

        mErrorDataContainer = (LinearLayout) findViewById(R.id.error_data_container);
        mErrorDataContainer.setVisibility(View.GONE);
        mNoDataTextView = (TextView) findViewById(R.id.empty_text_view);

        showLoadingAnimation();
        DataManager.getInstance().fetchData(mSearchQuery, this, true, calculateNumberOfVerticalVisibleSquares(), 0);
    }

    /**
     * Initialize the RecyclerView for the products and handle the scrolling events
     */
    private void initializeProductsRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.products_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(MainActivity.this, SPAN_COUNT);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mIsDataLoading = false;

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    //If the RecyclerView is scrolled down
                    int visibleItemCount = mGridLayoutManager.getChildCount();
                    int totalItemCount = mGridLayoutManager.getItemCount();
                    int pastVisiblesItems = mGridLayoutManager.findFirstVisibleItemPosition();

                    //If we approach the end of the line or we are on the end of the screen
                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        if (!mIsDataLoading) {
                            mIsDataLoading = true;
                            DataManager.getInstance().fetchData(null, MainActivity.this, true, calculateNumberOfVerticalVisibleSquares(),
                                            mAdapter.getItemCount());
                            Toast.makeText(MainActivity.this, "Loading required", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        });
    }

    /**
     * Initialize the swipe to refresh views and handle the swipe gestures
     */
    private void initializeSwipeToRefreshViews(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Show the loading animation in the middle of the screen only if no items are available
                if (mAdapter == null || mAdapter.getItemCount() ==0){
                    hideErrorContainer();
                    showLoadingAnimation();
                }
                //Force refresh items, bypassing cache and do not skip items
                DataManager.getInstance().fetchData(mSearchQuery, MainActivity.this, true, calculateNumberOfVerticalVisibleSquares(), 0);
            }

        });
    }
    /**
     * Initialize the search view and handle seach queries
     */
    private void initializeSearchView() {
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && query.trim().length() > 0) {

                    //Clear existing items, since the search will refresh the content of the screen
                    if (mAdapter!= null){
                        mAdapter.clear();
                    }
                    mSearchQuery = Arrays.asList(query.split("\\s\\{2,\\}"));
                    DataManager.getInstance().fetchData(mSearchQuery, MainActivity.this, true, calculateNumberOfVerticalVisibleSquares(), 0);
                    return true;
                } else {
                    mSearchQuery = null;
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Initialize the loading views if data is not available on cache. If the data is available on cache, then it will be fetched fast, and there
     * is no need to display it
     */
    private void initializeLoadingViews() {
        mLoadingImageView = (ImageView) findViewById(R.id.loading_data_image_view);
        mLoadingText = (TextView) findViewById(R.id.loading_data_text);
        mLoadingContainer = (LinearLayout) findViewById(R.id.loading_data_container);

        showLoadingAnimation();
    }


    /**
     * Customize the toolbar and set it as actionbar
     */
    private void customizeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final MenuItem showInStock = menu.findItem(R.id.menu_show_in_stock);

        showInStock.setChecked(SharedPreferenceManager.getInstance().isOnlyInStockChecked());
        showInStock.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                boolean isChecked = SharedPreferenceManager.getInstance().isOnlyInStockChecked();
                SharedPreferenceManager.getInstance().setOnlyInStockChecked(!isChecked);
                showInStock.setChecked(!isChecked);
                return true;
            }
        });

        mSearchView.setMenuItem(searchMenuItem);

        return true;
    }



    /**
     * Calculate how many items can fit the screen vertically. Items in the grid are squares and we can determine how many can fit the screen,
     * before they are layout out by the adapter.
     *
     * @return The number of items that can fit the screen
     */
    private int calculateNumberOfVerticalVisibleSquares() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;

        //Each item's width is equal to it's height, because the views layout are squares. Only a SPAN_COUNT number of squares can fit the screen's
        // width
        int itemSize = screenWidth / SPAN_COUNT;
        if (itemSize == 0) itemSize++;

        //We want to have an additional rows displayed on the bottom, because it can be partially seen, so we add one more
        return ((displayMetrics.heightPixels - getResources().getDimensionPixelSize(R.dimen.toolbar_height)) / itemSize + 1) * SPAN_COUNT;
    }

    /**
     * Hides the animation views and clear any ongoing animation on it
     */
    private void hideAnimationContainer() {
        mLoadingImageView.clearAnimation();
        mLoadingContainer.setVisibility(View.GONE);
    }


    /**
     * Hides the error container views
     */
    private void hideErrorContainer(){
        mErrorDataContainer.setVisibility(View.GONE);
    }

    /**
     * Initialize, attach and start an animation on the loading animation image view
     */
    private void showLoadingAnimation() {
        mLoadingContainer.setVisibility(View.VISIBLE);
        AnimationSet animatorSet = new AnimationSet(true);
        animatorSet.addAnimation(AnimationUtils.loadAnimation(this, R.anim.bouncing_animation));
        animatorSet.setInterpolator(new BounceInterpolator());
        mLoadingImageView.clearAnimation();
        mLoadingImageView.setAnimation(animatorSet);

        mLoadingImageView.startAnimation(animatorSet);
    }

    @Override
    public void onSuccess(@NonNull List<AsciiProductDTO> asciiProductDTO) {
        boolean errorOrNoData = (asciiProductDTO == null || asciiProductDTO.size() == 0 ||
                        (asciiProductDTO.size() == 1 && asciiProductDTO.get(0) == null));

        mErrorDataContainer.setVisibility(errorOrNoData ? View.VISIBLE : View.GONE);

        if (errorOrNoData) {
            mLoadingContainer.setVisibility(View.GONE);
            mSearchView.closeSearch();
            mAdapter = new ProductsAdapter(new ArrayList<AsciiProductDTO>());
            mAdapter.setOnItemClickListener(MainActivity.this);
            mRecyclerView.setAdapter(mAdapter);
            mSearchQuery = null;
            return;
        }

        if (mAdapter == null) {
            mAdapter = new ProductsAdapter(asciiProductDTO);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(MainActivity.this);

        } else {
            mAdapter.addItems(asciiProductDTO);
            mAdapter.setOnItemClickListener(MainActivity.this);
        }

        hideAnimationContainer();
        mSwipeRefreshLayout.setRefreshing(false);
        mIsDataLoading = false;
    }

    @Override
    public void onError(@Nullable Throwable exception) {
        mErrorDataContainer.setVisibility(View.VISIBLE);
        hideAnimationContainer();
        mSwipeRefreshLayout.setRefreshing(false);
        mIsDataLoading = false;
    }

    @Override
    public void onListItemClicked(int position) {

        Intent startingIntent = new Intent(this, BuyProductActivity.class);
        startingIntent.putExtra(AsciiWarehouseConstants.PRODUCT_BUNDLE_KEY, mAdapter.getItemAtPosition(position));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        mRecyclerView.getLayoutManager().findViewByPosition(position), "");
        ActivityCompat.startActivity(this, startingIntent, options.toBundle());
    }
}
