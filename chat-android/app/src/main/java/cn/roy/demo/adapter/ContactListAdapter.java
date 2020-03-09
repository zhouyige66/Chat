package cn.roy.demo.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.roy.demo.R;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-03-09 13:49
 * @Version: v1.0
 */
public class ContactListAdapter extends BaseExpandableListAdapter {
    private List<String> groupData;
    private Map<String,List> childData;

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
        if(convertView == null){
            Context context = parent.getContext();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_list,null,false);
        }
        View view = convertView;
        TextView tv = view.findViewById(R.id.tv_user);
        tv.setText(getGroup(groupPosition));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            Context context = parent.getContext();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_list,null,false);
        }
        View view = convertView;
        TextView tv = view.findViewById(R.id.tv_user);
        tv.setText(getGroup(groupPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
