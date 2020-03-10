package cn.roy.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.R;
import cn.roy.demo.adapter.ContactListAdapter;
import cn.roy.demo.util.LogUtil;
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

        List<String> groupList = new ArrayList<>();
        groupList.add("apply");
        groupList.add("group");
        groupList.add("friend");
        Map<String, List> map = new HashMap<>();
        for (String key : groupList) {
            map.put(key, new ArrayList());
        }

        ContactListAdapter contactListAdapter = new ContactListAdapter(groupList, map);
        elv.setAdapter(contactListAdapter);
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        elv.setIndicatorBounds(width - 40, width - 10);

        return view;
    }

    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();

        getVerifies();
        getGroups();
        getFriends();
    }

    private void getVerifies() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1L);
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject jsonObject) {
                LogUtil.d(ContactListFragment.this, "审批列表：" + jsonObject.toJSONString());
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        HttpUtil.getInstance().getWithoutHeader(ApplicationConfig.HttpConfig.API_GET_VERIFY_LIST,
                params, observer);
    }

    private void getGroups() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1L);
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject jsonObject) {
                LogUtil.d(ContactListFragment.this, "群组列表：" + jsonObject.toJSONString());
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        HttpUtil.getInstance().getWithoutHeader(ApplicationConfig.HttpConfig.API_GET_GROUP_LIST,
                params, observer);
    }

    private void getFriends() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1L);
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject jsonObject) {
                LogUtil.d(ContactListFragment.this, "好友列表：" + jsonObject.toJSONString());
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
