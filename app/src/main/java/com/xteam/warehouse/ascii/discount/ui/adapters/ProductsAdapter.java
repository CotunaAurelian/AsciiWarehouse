package com.xteam.warehouse.ascii.discount.ui.adapters;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xteam.warehouse.ascii.discount.R;
import com.xteam.warehouse.ascii.discount.model.dto.AsciiProductDTO;
import com.xteam.warehouse.ascii.discount.ui.views.OnRecyclerItemClickListener;

/**
 * Provides views to Ascii Products {@link android.support.v7.widget.RecyclerView} in {@link com.xteam.warehouse.ascii.discount.ui.activities.MainActivity}
 * <p/>
 * Created by cotuna on 7/4/16.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    /**
     * The type for the item view. This will be used to separate the footer and the items in onCreate and onBind views
     */
    private static final int TYPE_ITEM = 1;

    /**
     * The type for the footer view. This will be used to separate the footer and the items in onCreate and onBind views
     */
    private static final int TYPE_FOOTER = 2;

    /**
     * Dataset handled by the adapter
     */
    private List<AsciiProductDTO> mDataSet;

    /**
     * Listener to be notified when a click event on one of the element occurs.
     */
    private OnRecyclerItemClickListener mOnItemClickListener;


    /**
     * Creates a new instance of this adapter and sets it's elements
     *
     * @param itemsList List of {@link AsciiProductDTO} for this adapter
     */
    public ProductsAdapter(List<AsciiProductDTO> itemsList) {
        this.mDataSet = itemsList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //On this method new views will be created (it is invoked by the layout manager)
        if (viewType == TYPE_ITEM) {
            View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(contentView);
        } else {
            View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_data_layout, parent, false);

            return new FooterViewHolder(contentView);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            //Set the element from the mDataSet for the corresponding position.
            itemViewHolder.mFaceTextView.setText(mDataSet.get(position).mFace);
            itemViewHolder.mFaceTextView.setTag(position);
            boolean isOutOfStock = mDataSet.get(position).mStock <= 0;
            itemViewHolder.mOutOfStockLayout.setVisibility(isOutOfStock ? View.VISIBLE : View.GONE);

            itemViewHolder.mFaceTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onListItemClicked(position);
                    }
                }
            });
        } else {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;

            AnimationSet animatorSet = new AnimationSet(true);
            animatorSet.addAnimation(AnimationUtils.loadAnimation(footerViewHolder.mLoadingImageView.getContext(), R.anim.bouncing_animation));
            animatorSet.setInterpolator(new BounceInterpolator());
            footerViewHolder.mLoadingImageView.clearAnimation();
            footerViewHolder.mLoadingImageView.setAnimation(animatorSet);

            footerViewHolder.mLoadingImageView.startAnimation(animatorSet);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == mDataSet.size() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Returns an element at a certain position. If the dataset doesn't exists, or the position is outside of range, null is returned
     *
     * @param position The position of the item that is returned
     */
    public AsciiProductDTO getItemAtPosition(int position) {
        if (mDataSet != null && position < mDataSet.size()) {
            return mDataSet.get(position);
        } else {
            return null;
        }
    }

    /**
     * Append the new items to the existing dataset
     *
     * @param itemsList The items to be added to the dataset
     */
    public void addItems(List<AsciiProductDTO> itemsList) {
        int initialPosition = mDataSet.size();
        this.mDataSet.addAll(itemsList);
        notifyItemRangeInserted(initialPosition, itemsList.size());
    }

    /**
     * Clears the items in the list
     */
    public void clear() {
        int removedItems = mDataSet.size();
        this.mDataSet.clear();
        notifyItemRangeRemoved(0, removedItems);
    }

    /**
     * Set a listener to be notified when a click event on one of the elements occurs.
     *
     * @param listener The listener to be notified when the click event occurs
     */
    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    /**
     * Provides a reference to the type of view used by the adapter for normal items
     */
    static class ItemViewHolder extends ViewHolder {

        private TextView mFaceTextView;
        private LinearLayout mOutOfStockLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.mFaceTextView = (TextView) itemView.findViewById(R.id.face_text_view);
            this.mOutOfStockLayout = (LinearLayout) itemView.findViewById(R.id.out_of_stock_layout);
        }
    }


    /**
     * Provides a reference to the type of view used by the adapter for normal items
     */
    static class FooterViewHolder extends ViewHolder {
        private ImageView mLoadingImageView;

        FooterViewHolder(View itemView) {
            super(itemView);
            mLoadingImageView = (ImageView) itemView.findViewById(R.id.loading_data_image_view);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
