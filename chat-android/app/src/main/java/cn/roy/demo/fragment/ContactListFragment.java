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
import cn.roy.demo.model.Group;
import cn.roy.demo.model.User;
import cn.roy.demo.util.CacheManager;
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
    private View v_loading;
    private TextView tv_loading;
    private ViewStub vs_retry;
    private View v_retry;
    private ImageView iv_load_tip;
    private ExpandableListView elv;

    private Map<String, List> dataMap;
    private ContactListAdapter contactListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        v_loading = view.findViewById(R.id.v_loading);
        vs_retry = view.findViewById(R.id.vs_retry);
        elv = view.findViewById(R.id.elv_content);

        List<String> groupList = new ArrayList<>();
        groupList.add("apply");
        groupList.add("group");
        groupList.add("friend");
        dataMap = new HashMap<>(3);
        for (String key : groupList) {
            dataMap.put(key, new ArrayList());
        }
        contactListAdapter = new ContactListAdapter(groupList, dataMap);
        elv.setAdapter(contactListAdapter);
        elv.setVisibility(View.GONE);
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                if (groupPosition > 0) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("chatType",
                            groupPosition == 1 ? ChatMessageType.GROUP.getCode()
                                    : ChatMessageType.SINGLE.getCode());
                    String key = groupPosition == 1 ? "group" : "friend";
                    intent.putExtra("data", (Serializable) dataMap.get(key).get(childPosition));
                    startActivity(intent);
                } else {
                    LogUtil.d(ContactListFragment.this, "点击了审批子项");
                }
                return true;
            }
        });

        return view;
    }

    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();

        v_loading.setVisibility(View.VISIBLE);
        requestSuccessCount = 0;
        requestFailCount = 0;
        getVerifies();
        getGroups();
        getFriends();
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
                requestSuccessCount++;
                dataMap.get("apply").clear();
                JSONArray list = jsonObject.getJSONArray("list");
                if (list != null && list.size() > 0) {
                    List<Apply> applies = JSON.parseObject(list.toJSONString(), new TypeReference<List<Apply>>() {
                    });
                    dataMap.get("apply").addAll(applies);
                }
                check();
            }

            @Override
            public void onError(Throwable e) {
                requestFailCount++;
                check();
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
        params.put("userId", CacheManager.getInstance().getCurrentUserId());
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject jsonObject) {
                requestSuccessCount++;
                dataMap.get("group").clear();
                JSONArray list = jsonObject.getJSONArray("list");
                if (list != null && list.size() > 0) {
                    List<Group> groups = JSON.parseObject(list.toJSONString(), new TypeReference<List<Group>>() {
                    });
                    dataMap.get("group").addAll(groups);
                }
                check();
            }

            @Override
            public void onError(Throwable e) {
                requestFailCount++;
                check();
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
        params.put("userId", CacheManager.getInstance().getCurrentUserId());
        Observer<JSONObject> observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject jsonObject) {
                requestSuccessCount++;
                dataMap.get("friend").clear();
                JSONArray list = jsonObject.getJSONArray("list");
                if (list != null && list.size() > 0) {
                    List<User> users = JSON.parseObject(list.toJSONString(), new TypeReference<List<User>>() {
                    });
                    dataMap.get("friend").addAll(users);
                }
                check();
            }

            @Override
            public void onError(Throwable e) {
                requestFailCount++;
                check();
            }

            @Override
            public void onComplete() {
            }
        };
        HttpUtil.getInstance().getWithoutHeader(ApplicationConfig.HttpConfig.API_GET_FRIEND_LIST,
                params, observer);
    }

    private int requestSuccessCount = 0;
    private int requestFailCount = 0;

    private void check() {
        LogUtil.d(this, "执行一次检查：" + requestSuccessCount + "/" + requestFailCount);
        if (requestFailCount + requestSuccessCount == 3) {
            v_loading.setVisibility(View.GONE);
            if (requestFailCount > requestSuccessCount) {
                // 失败次数大于成功次数
                if (vs_retry.getParent() != null) {
                    v_retry = vs_retry.inflate();
                    iv_load_tip = v_retry.findViewById(R.id.iv_load_tip);
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
            } else {
                elv.setVisibility(View.VISIBLE);
                if (contactListAdapter != null) {
                    contactListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}
