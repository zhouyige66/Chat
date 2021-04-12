package cn.roy.demo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2021/01/29
 * @Version: v1.0
 */
public class AddContactPopupWindow {

    public void init(Context context, View target) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_add_contact, null);
        PopupWindow popupWindow = new PopupWindow(view);
        popupWindow.setAnimationStyle(R.style.StylePopupWindowAnim);
        popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 显示
        popupWindow.showAsDropDown(target,0,0);
    }

}
