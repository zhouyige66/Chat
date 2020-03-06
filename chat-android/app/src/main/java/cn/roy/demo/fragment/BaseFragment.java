package cn.roy.demo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.roy.demo.util.LogUtil;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-03-05 09:05
 * @Version: v1.0
 */
public class BaseFragment extends Fragment {
    protected boolean hasLoadData = false;
    private View tokenView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.tokenView = view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!hasLoadData) {
            hasLoadData = true;
            lazyLoadData();
        }
    }

    /**
     * 懒加载
     */
    protected void lazyLoadData() {
        LogUtil.d(this,"执行懒加载数据");
    }

    /**
     * 隐藏软键盘(适用于Fragment)
     */
    protected void hideSoftKeyboard() {
        if (tokenView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)getActivity().
                    getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(tokenView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
