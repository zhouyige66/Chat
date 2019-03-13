 package cn.roy.demo.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Description
 * @Author kk20
 * @Date 2017/5/17
 * @Version V1.0.0
 */
public class AdapterViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    public AdapterViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static AdapterViewHolder get(Context context, View convertView, ViewGroup parent,
                                        int layoutId, int position) {
        AdapterViewHolder holder = null;
        if (convertView == null) {
            holder = new AdapterViewHolder(context, parent, layoutId, position);
        } else {
            holder = (AdapterViewHolder) convertView.getTag();
            holder.mPosition = position;
        }

        return holder;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public int getPosition() {
        return mPosition;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        return (T) view;
    }

}
