package cn.roy.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @Description 通用适配器
 * @Author kk20
 * @Date 2017/5/17
 * @Version V1.0.0
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected int mItemLayoutId;
    protected List<T> mData;
    protected LayoutInflater mInflater;

    public CommonAdapter(Context context, int itemLayoutId, List<T> data) {
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
        this.mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterViewHolder holder = AdapterViewHolder.get(mContext, convertView, parent,
                mItemLayoutId, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(AdapterViewHolder holder, T t);
}
