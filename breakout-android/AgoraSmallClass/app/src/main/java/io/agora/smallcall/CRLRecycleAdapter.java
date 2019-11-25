package io.agora.smallcall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
public class CRLRecycleAdapter extends RecyclerView.Adapter<CRLRecycleHolder> {
    private Context mContxt;
    private List<UserInfo> mDataSet;

    public CRLRecycleAdapter(Context ctx, List<UserInfo> dataSets){
        this.mContxt = ctx;
        this.mDataSet = dataSets;
    }

    public void update(List<UserInfo> ds) {
        mDataSet = ds;
        notifyDataSetChanged();
    }

    @Override
    public CRLRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CRLRecycleHolder(LayoutInflater.from(mContxt).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CRLRecycleHolder holder, int position) {
        if (mDataSet.get(position).sv.getParent() != null)
            ((ViewGroup)(mDataSet.get(position).sv.getParent())).removeAllViews();

        if (holder.mSmallVideoView.getChildCount() > 0)
            holder.mSmallVideoView.removeAllViews();

        holder.mSmallVideoView.addView(mDataSet.get(position).sv);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
