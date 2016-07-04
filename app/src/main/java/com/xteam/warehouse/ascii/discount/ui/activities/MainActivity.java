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
                DataManager.getInstance().fetchData(null, this);
        }
    }


    @Override
    public void onSuccess(@NonNull List<AsciiProductDTO> asciiProductDTO) {
        mNoDataTextView.setVisibility((asciiProductDTO == null || asciiProductDTO.size() == 0) ? View.VISIBLE : View.GONE);
        mAdapter = new ProductsAdapter(asciiProductDTO);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(MainActivity.this);
    }

    @Override
    public void onError(@Nullable Throwable exception) {
        mNoDataTextView.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onListItemClicked(int position) {
        Toast.makeText(MainActivity.this, "Clicked on item " + position, Toast.LENGTH_SHORT).show();
    }
}
