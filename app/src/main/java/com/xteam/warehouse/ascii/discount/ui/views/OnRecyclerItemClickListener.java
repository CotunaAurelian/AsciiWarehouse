package com.xteam.warehouse.ascii.discount.ui.views;

/**
 * Interface definition for callbacks to be invoked when recycler item view has been clicked. This interface will be used in the user interface layer.
 * <p/>
 * Created by cotuna on 7/4/16.
 */

public interface OnRecyclerItemClickListener {

    /**
     * Called when an item has been clicked
     *
     * @param position The position of the item that has been clicked
     */
    void onListItemClicked(int position);
}
