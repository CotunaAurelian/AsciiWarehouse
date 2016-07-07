package com.xteam.warehouse.ascii.discount.ui.activities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.RelativeLayout;
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

    /**
     * Root view that will be used to animate the whole screen in
     */
    private RelativeLayout mRootView;

    /**
     * The initial x position of the view. This is used to create the smooth transition animation on x axis on both enter animation and exit animation
     */
    private float mInitialXPosition;
    /**
     * The initial y position of the view. This is used to create the smooth transition animation on x axis on both enter animation and exit animation
     */
    private float mInitialYPosition;

    /**
     * Layout displayed when an order is completed.
     */
    private RelativeLayout mOrderCompleteLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_product_layout);
        if (getIntent().hasExtra(AsciiWarehouseConstants.PRODUCT_BUNDLE_KEY)) {
            this.mData = getIntent().getParcelableExtra(AsciiWarehouseConstants.PRODUCT_BUNDLE_KEY);
            this.mInitialXPosition = getIntent().getFloatExtra(AsciiWarehouseConstants.VIEW_X_POSITION, 0);
            this.mInitialYPosition = getIntent().getFloatExtra(AsciiWarehouseConstants.VIEW_Y_POSITION, 0);
        }
    }

    @Override
    public void onBackPressed() {
        runExitAnimation();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Initialize views components
        mRootView = (RelativeLayout) findViewById(R.id.root_view);
        mFaceTextView = (TextView) findViewById(R.id.face_text_view);
        mPriceTextView = (TextView) findViewById(R.id.price_text_view);
        mBuyProductButton = (Button) findViewById(R.id.buy_button);
        mBuyProductButton.setOnClickListener(this);
        mOrderCompleteLayout = (RelativeLayout) findViewById(R.id.order_complete_layout);
        bindViews();

        runEnterAnimation();
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
            mBuyProductButton.setBackgroundResource(R.drawable.buy_button_drawable_selector);
            mBuyProductButton.setTextColor(getResources().getColor(R.color.colorTextWhite));
            if (mData.mStock == 0) {
                mBuyProductButton.setText(getResources().getString(R.string.lbl_no_items_left));
                mBuyProductButton.setTextColor(getResources().getColor(R.color.colorTextBrown));
                mBuyProductButton.setBackgroundColor(getResources().getColor(R.color.disabledColor));
            } else if (mData.mStock == 1) {
                //If we have only one in stock, change the text on the button
                mBuyProductButton.setText(getResources().getString(R.string.lbl_only_one_in_stock));
            } else {
                mBuyProductButton.setText(getResources().getString(R.string.lbl_buy_now));
            }
        }
    }


    /**
     * The enter animation scales the text view in from its previous size/location. In parallel, the background of the activity is fading in.
     */
    private void runEnterAnimation() {
        final long duration = 300;

        // Figure out where the thumbnail and full size versions are, relative
        // to the screen and each other
        int[] screenLocation = new int[2];
        mFaceTextView.getLocationOnScreen(screenLocation);
        float mLeftDelta = mInitialXPosition - screenLocation[0];
        float mTopDelta = mInitialYPosition - screenLocation[1];


        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the
        // size/location, from which we'll animate it back up
        mFaceTextView.setPivotX(0);
        mFaceTextView.setPivotY(0);
        mFaceTextView.setScaleX(mFaceTextView.getWidth());
        mFaceTextView.setScaleY(mFaceTextView.getHeight());
        mFaceTextView.setTranslationX(mLeftDelta);
        mFaceTextView.setTranslationY(mTopDelta);

        // Animate scale and translation to full size
        mFaceTextView.animate().setDuration(duration).
                        scaleX(1).scaleY(1).
                        translationX(0).translationY(0).
                        setInterpolator(new DecelerateInterpolator());

        //Animate the background
        mRootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_animation));

    }

    /**
     * The enter animation scales the text view in from its previous size/location. In parallel, the background of the activity is fading out.
     * When the fade out animation ends, this activity is closed
     */
    private void runExitAnimation() {
        final long duration = 200;
        //Animate the background
        mRootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_animation));
        mFaceTextView.animate().
                        setDuration(duration).
                        scaleX(0.65f).
                        scaleY(0.65f).
                        setInterpolator(new AccelerateInterpolator()).
                        translationX(mInitialXPosition).
                        translationY(mInitialYPosition).withEndAction(new Runnable() {
            @Override
            public void run() {
                BuyProductActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        expand(mOrderCompleteLayout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                collapse(mOrderCompleteLayout);
            }
        }, 1500);
        mData.mStock--;
        bindViews();
    }


    /**
     * Creates a collapse animation on the provided view. The view has to be measured already when this method is called, because it will use
     * the initial position to determine the duration of the animation. It sets the duration automatically to 1dp / millisecond.
     * The animation will have AccelerateDecelerateInterpolator added to it to create a smooth movement
     *
     * @param view The view that will be collapsed
     */
    public void collapse(final View view) {
        final int initialHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        // 1dp/ms
        animation.setDuration((int) (initialHeight / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }

    /**
     * Creates an expand animation on the provided view. It sets the duration automatically to 1dp / millisecond.
     * Initially this method will set the view's height to 0, because on Android level < API 21, any animations for views with height of 0, are
     * canceled.
     * The animation will have AccelerateDecelerateInterpolator added to it to create a smooth movement
     *
     * @param view The view that will be expanded
     */

    public void expand(final View view) {
        view.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        view.getLayoutParams().height = 1;
        view.setVisibility(View.VISIBLE);
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation) {
                view.getLayoutParams().height =
                                interpolatedTime == 1 ? RelativeLayout.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        // 1dp/ms
        animation.setDuration((int) (targetHeight / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }
}
