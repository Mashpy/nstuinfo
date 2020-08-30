package com.nstuinfo.mRecyclerView;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by whoami on 10/31/2018.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        //outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        //if (parent.getChildLayoutPosition(view) == 0) {
          //  outRect.top = space;
        //} else {
          //  outRect.top = 0;
        //}
    }
}
