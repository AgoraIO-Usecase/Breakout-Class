package io.agora.smallcall;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

public class CRLRecycleHolder extends RecyclerView.ViewHolder {
    public FrameLayout mSmallVideoView;

    public CRLRecycleHolder(View itemView) {
        super(itemView);
        mSmallVideoView = itemView.findViewById(R.id.fl_small_video_item);
    }
}
