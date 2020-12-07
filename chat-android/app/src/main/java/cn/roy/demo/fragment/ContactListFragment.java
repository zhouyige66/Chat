package cn.roy.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.roy.demo.ApplicationConfig;
import cn.roy.demo.R;
import cn.roy.demo.activity.ChatActivity;
import cn.roy.demo.adapter.ContactListAdapter;
import cn.roy.demo.model.Apply;
import cn.roy.demo.service.ChatService;
import cn.roy.demo.util.CacheManager;
import cn.roy.demo.util.LogUtil;
import cn.roy.demo.util.http.HttpUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-03-04 13:07
 * @Version: v1.0
 */
public class ContactListFragment extends BaseFragment {
    private View v_loading;
    private TextView tv_loading;
    private ViewStub vs_retry;
    private View v_retry;
    private ImageView iv_retry_tip;
    private SwipeRefreshLayout srf;
    private ExpandableListView elv;

    private Map<String, List> dataMap;
    private ContactListAdapter contactListAdapter;

    private CompositeDisposable compositeDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        v_loading = view.findViewById(R.id.v_loading);
        vs_retry = view.findViewById(R.id.vs_retry);
        srf = view.findViewById(R.id.sfl);
        elv = view.findViewById(R.id.elv_content);

        List<String> groupList = new ArrayList<>();
        groupList.add("apply");
        groupList.add("group");
        groupList.add("friend");
        dataMap = new HashMap<>(3);
        for (String key : groupList) {
            dataMap.put(key, new ArrayList());
        }
        updateUI();

        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().startService(new Intent(getActivity(), ChatService.class));
            }
        });
        contactListAdapter = new ContactListAdapter(groupList, dataMap);
        elv.setAdapter(contactListAdapter);
        elv.setVisibility(View.GONE);
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                if (groupPosition > 0) {
                    String key = groupPosition == 1 ? "group" : "friend";
                    ChatMessageType type = groupPosition == 1 ? ChatMessageType.GROUP
                            : ChatMessageType.SINGLE;
                    Object item = dataMap.get(key).get(childPosition);

                    ChatActivity.launch(getActivity(), type, (Serializable) item);
                    getActivity().overridePendingTransition(R.anim.anim_right_enter, R.anim.anim_left_exit);
                } else {
                    LogUtil.d(ContactListFragment.this, "点击了审批子项");
                }
                return true;
            }
        });

        compositeDisposable = new CompositeDisposable();
        CacheManager.getInstance().getBaseInfoObservable().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer < 2) {
                            updateUI();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        srf.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        compositeDisposable.dispose();
    }

    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();

        v_loading.setVisibility(View.VISIBLE);
        getVerifies();
    }

    private void getVerifies() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", CacheManager.getInstance().getCurrentUserId());
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject jsonObject) {
                v_loading.setVisibility(View.GONE);
                dataMap.get("apply").clear();
                JSONArray list = jsonObject.getJSONArray("data");
                if (list != null && list.size() > 0) {
                    List<Apply> applies = JSON.parseObject(list.toJSONString(), new TypeReference<List<Apply>>() {
                    });
                    dataMap.get("apply").addAll(applies);
                }
                elv.setVisibility(View.VISIBLE);
                if (contactListAdapter != null) {
                    contactListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {
                v_loading.setVisibility(View.GONE);
                // 失败次数大于成功次数
                if (vs_retry.getParent() != null) {
                    v_retry = vs_retry.inflate();
                    TextView tv_retry = v_retry.findViewById(R.id.tv_retry);
                    tv_retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v_retry.setVisibility(View.GONE);
                            lazyLoadData();
                        }
                    });
                } else {
                    v_retry.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onComplete() {
            }
        };
        HttpUtil.getInstance().getWithoutHeader(ApplicationConfig.HttpConfig.API_GET_VERIFY_LIST,
                params, observer);
    }

    private void updateUI() {
        List group = dataMap.get("group");
        List friend = dataMap.get("friend");
        group.clear();
        friend.clear();
        group.addAll(CacheManager.getInstance().getGroupList());
        friend.addAll(CacheManager.getInstance().getFriendList());
        if (contactListAdapter != null) {
            contactListAdapter.notifyDataSetChanged();
        }

        srf.setRefreshing(false);
    }
}
