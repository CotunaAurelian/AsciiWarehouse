package com.xteam.warehouse.ascii.discount.ui.activities;

import java.util.List;

import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.xteam.warehouse.ascii.discount.R;
import com.xteam.warehouse.ascii.discount.business.datahandling.ProductsSearchListener;
import com.xteam.warehouse.ascii.discount.business.datastorage.SharedPreferenceManager;
import com.xteam.warehouse.ascii.discount.business.webservices.responses.DataFetchListener;
import com.xteam.warehouse.ascii.discount.business.datahandling.DataManager;
import com.xteam.warehouse.ascii.discount.business.webservices.responses.BaseResponse;
import com.xteam.warehouse.ascii.discount.model.dto.AsciiProductDTO;
import com.xteam.warehouse.ascii.discount.ui.adapters.ProductsAdapter;
import com.xteam.warehouse.ascii.discount.ui.views.OnRecyclerItemClickListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ProductsSearchListener, OnRecyclerItemClickListener {

    private Button mSearchAllButton;

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
     * Search view in the toolbar
     */
    private MaterialSearchView mSearchView;

    /**
     * Flag used to prevent the recyclerview to keep firing new requests when a request for data is ongoing
     */
    private boolean mIsDataLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        customizeToolbar();

        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);

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
                            DataManager.getInstance().fetchData(null, MainActivity.this, true, calculateNumberOfVerticalVisibleSquares(), mAdapter
                                            .getItemCount());
                            Toast.makeText(MainActivity.this, "Loading required", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        });

        mNoDataTextView = (TextView) findViewById(R.id.empty_text_view);

        mSearchAllButton = (Button) findViewById(R.id.searchAllButton);
        mSearchAllButton.setOnClickListener(this);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchAllButton:
                DataManager.getInstance().fetchData(null, this, false, calculateNumberOfVerticalVisibleSquares(),0);
        }
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
        return ((displayMetrics.heightPixels - getResources().getDimensionPixelSize(R.dimen.toolbar_height)) / itemSize +1) * SPAN_COUNT;


    }

    @Override
    public void onSuccess(@NonNull List<AsciiProductDTO> asciiProductDTO) {
        mNoDataTextView.setVisibility((asciiProductDTO == null || asciiProductDTO.size() == 0) ? View.VISIBLE : View.GONE);
        if (mAdapter == null){
            mAdapter = new ProductsAdapter(asciiProductDTO);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(MainActivity.this);
        }else {
            mAdapter.addItems(asciiProductDTO);
        }

        mIsDataLoading = false;
    }

    @Override
    public void onError(@Nullable Throwable exception) {
        mNoDataTextView.setVisibility(View.VISIBLE);
        mIsDataLoading = false;
        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onListItemClicked(int position) {
        Toast.makeText(MainActivity.this, "Clicked on item " + position, Toast.LENGTH_SHORT).show();
    }
}
