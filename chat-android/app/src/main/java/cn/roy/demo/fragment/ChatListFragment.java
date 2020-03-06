package cn.roy.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.roy.demo.R;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-03-04 13:07
 * @Version: v1.0
 */
public class ChatListFragment extends BaseFragment {
    private ListView lv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        lv = (ListView) view;

        return view;
    }

}
