package cn.roy.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.R;
import cn.roy.demo.util.http.HttpUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-03-04 13:07
 * @Version: v1.0
 */
public class ContactListFragment extends BaseFragment {
    private ExpandableListView elv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        elv = (ExpandableListView) view;

        return view;
    }

    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();

        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1L);
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject jsonObject) {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        HttpUtil.getInstance().getWithoutHeader(ApplicationConfig.HttpConfig.API_GET_FRIEND_LIST,
                params, observer);
    }

}
