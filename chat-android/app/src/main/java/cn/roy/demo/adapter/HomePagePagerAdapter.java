package cn.roy.demo.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020-03-04 11:08
 * @Version: v1.0
 */
public class HomePagePagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public HomePagePagerAdapter(@NonNull FragmentManager fm, int behavior,
                                List<Fragment> fragmentList) {
        super(fm, behavior);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
