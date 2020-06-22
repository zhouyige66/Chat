package cn.roy.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.kk20.chat.base.message.ChatMessage;
import cn.kk20.chat.base.message.chat.ChatMessageType;
import cn.roy.demo.R;
import cn.roy.demo.activity.ChatActivity;
import cn.roy.demo.adapter.AdapterViewHolder;
import cn.roy.demo.adapter.CommonAdapter;
import cn.roy.demo.model.RecentContact;
import cn.roy.demo.util.CacheManager;
import cn.roy.demo.util.ChatMessageUtil;
import cn.roy.demo.util.DateUtil;
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
public class ChatListFragment extends BaseFragment {
    private ListView listView;
    private List<RecentContact> recentContactList;
    private CommonAdapter<RecentContact> adapter;
    private CompositeDisposable compositeDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        listView = (ListView) view;
        recentContactList = CacheManager.getInstance().getRecentContactList();
        adapter = new CommonAdapter<RecentContact>(getContext(), R.layout.item_recent_contact_list,
                recentContactList) {
            @Override
            public void convert(AdapterViewHolder holder, RecentContact recentContact) {
                TextView tv_user_name = holder.getView(R.id.tv_user_name);
                TextView tv_not_read_count = holder.getView(R.id.tv_not_read_count);
                TextView tv_chat_msg = holder.getView(R.id.tv_chat_msg);
                TextView tv_chat_time = holder.getView(R.id.tv_chat_time);

                int notReadCount = recentContact.getNotReadCount();
                if (notReadCount > 100) {
                    tv_not_read_count.setText("···");
                } else {
                    tv_not_read_count.setText(String.valueOf(notReadCount));
                }
                tv_not_read_count.setVisibility(notReadCount == 0 ? View.GONE : View.VISIBLE);
                String contact = recentContact.getContact();
                if (contact.startsWith("group")) {
                    tv_user_name.setText(recentContact.getGroup().getName());
                } else {
                    tv_user_name.setText(recentContact.getUser().getName());
                }
                ChatMessage chatMessage = recentContact.getChatMessage();
                tv_chat_msg.setText(ChatMessageUtil.getMsg(chatMessage));

                Date sendDate = new Date(chatMessage.getSendTimestamp());
                Date time = Calendar.getInstance().getTime();
                String format = DateUtil.format(time, "yyyy-MM-dd");
                try {
                    Date parse = DateUtil.parse(format, "yyyy-MM-dd");
                    if (sendDate.before(parse)) {
                        tv_chat_time.setText(DateUtil.format(sendDate, "yyyy-MM-dd"));
                    } else {
                        tv_chat_time.setText(DateUtil.format(sendDate, "HH:ss"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecentContact recentContact = recentContactList.get(position);
                String contact = recentContact.getContact();
                ChatMessageType chatMessageType;
                Serializable data;
                if (contact.startsWith("group")) {
                    chatMessageType = ChatMessageType.GROUP;
                    data = recentContact.getGroup();
                } else {
                    chatMessageType = ChatMessageType.SINGLE;
                    data = recentContact.getUser();
                }
                ChatActivity.launch(getActivity(), chatMessageType, data);
                getActivity().overridePendingTransition(R.anim.anim_right_enter, R.anim.anim_left_exit);
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
                        if (integer == 2) {
                            recentContactList = CacheManager.getInstance().getRecentContactList();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

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

}
