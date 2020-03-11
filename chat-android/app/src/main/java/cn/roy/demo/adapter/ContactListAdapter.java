package cn.roy.demo.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.roy.demo.R;
import cn.roy.demo.model.Apply;
import cn.roy.demo.model.Group;
import cn.roy.demo.model.User;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-03-09 13:49
 * @Version: v1.0
 */
public class ContactListAdapter extends BaseExpandableListAdapter {
    private List<String> groupData;
    private Map<String, List> childData;

    public ContactListAdapter(List<String> groupData, Map<String, List> childData) {
        this.groupData = groupData;
        this.childData = childData;
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String key = groupData.get(groupPosition);
        return childData.get(key).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String group = getGroup(groupPosition);
        Object child = childData.get(group).get(childPosition);
        return child;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Context context = parent.getContext();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_elv_group, parent, false);
        }
        View view = convertView;
        TextView tv = view.findViewById(R.id.tv_group);
        TextView tv2 = view.findViewById(R.id.tv_count);
        tv.setText(getGroup(groupPosition));
        tv2.setText("" + childData.get(getGroup(groupPosition)).size());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Context context = parent.getContext();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_list, parent, false);
        }
        View view = convertView;
        ImageView iv = view.findViewById(R.id.iv_user_head);
        TextView tv = view.findViewById(R.id.tv_user_name);
        Object item = childData.get(getGroup(groupPosition)).get(childPosition);
        if (item instanceof Group) {
            Group group = (Group) item;
            tv.setText(group.getName());
        } else if (item instanceof User) {
            User user = (User) item;
            tv.setText(user.getName());
        } else if (item instanceof Apply) {
            Apply apply = (Apply) item;
            tv.setText("申请记录：" + apply.getId());
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
