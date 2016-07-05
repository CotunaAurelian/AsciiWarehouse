package com.xteam.warehouse.ascii.discount.ui.adapters;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        return new ViewHolder(contentView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Set the element from the mDataSet for the corresponding position.
        holder.mFaceTextView.setText(mDataSet.get(position).mFace);
        holder.mFaceTextView.setTag(position);

        holder.mFaceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onListItemClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    /**
     * Append the new items to the existing dataset
     * @param itemsList The items to be added to the dataset
     */
    public void addItems(List<AsciiProductDTO> itemsList){
        this.mDataSet.addAll(itemsList);
        notifyDataSetChanged();
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
     * Provides a reference to the type of view used by the adapter.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mFaceTextView;

        ViewHolder(View itemView) {
            super(itemView);
            this.mFaceTextView = (TextView) itemView.findViewById(R.id.face_text_view);
        }
    }


}
