package com.xteam.warehouse.ascii.discount.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xteam.warehouse.ascii.discount.R;
import com.xteam.warehouse.ascii.discount.model.dto.AsciiProductDTO;
import com.xteam.warehouse.ascii.discount.ui.AsciiWarehouseConstants;


/**
 * The activity used to display the product, price and allow user to buy the product
 * Created by cotuna on 7/6/16.
 */

public class BuyProductActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The TextView used to display the ascii code face
     */
    private TextView mFaceTextView;

    /**
     * The TextView used to display the price of the ascii code
     */
    private TextView mPriceTextView;

    /**
     * The Button used to buy items.
     */
    private Button mBuyProductButton;

    /**
     * Data used to display information on the screen
     */
    private AsciiProductDTO mData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_product_layout);
        if (getIntent().hasExtra(AsciiWarehouseConstants.PRODUCT_BUNDLE_KEY)) {
            this.mData = getIntent().getParcelableExtra(AsciiWarehouseConstants.PRODUCT_BUNDLE_KEY);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Initialize views components
        mFaceTextView = (TextView) findViewById(R.id.face_text_view);
        mPriceTextView = (TextView) findViewById(R.id.price_text_view);
        mBuyProductButton = (Button) findViewById(R.id.buy_button);
        mBuyProductButton.setOnClickListener(this);

        bindViews();
    }

    /**
     * Bind the data to the views. If the data has no items left in stock, the buy product button will be disabled. If only one item in stock,
     * the buy button will change it's message.
     */
    private void bindViews() {
        //Bind views to data
        if (mData != null) {
            mFaceTextView.setText(mData.mFace);
            //Show decimal only if needed
            String price = (mData.mPrice % 1 == 0) ? String.valueOf((int) mData.mPrice) : String.valueOf(mData.mPrice);
            mPriceTextView.setText(String.format("$ %s", price));

            mBuyProductButton.setEnabled(mData.mStock > 0);
            if (mData.mStock == 0) {
                mBuyProductButton.setText(getResources().getString(R.string.lbl_no_items_left));
            } else if (mData.mStock == 1) {
                //If we have only one in stock, change the text on the button
                mBuyProductButton.setText(getResources().getString(R.string.lbl_only_one_in_stock));
            } else {
                mBuyProductButton.setText(getResources().getString(R.string.lbl_buy_now));
            }
        }
    }

    @Override
    public void onClick(View view) {
        Snackbar snackbar = Snackbar.make(view, R.string.lbl_order_completed, Snackbar.LENGTH_SHORT);
        snackbar.getView().setMinimumHeight(mBuyProductButton.getHeight());
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.order_completion_color));
        snackbar.show();
        mData.mStock--;
        bindViews();
    }
}
