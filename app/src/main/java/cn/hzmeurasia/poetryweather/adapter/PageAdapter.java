package cn.hzmeurasia.poetryweather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;

import cn.hzmeurasia.poetryweather.entity.PoetryDetail;
import cn.hzmeurasia.poetryweather.fragment.FMPoetryDetailComment;
import cn.hzmeurasia.poetryweather.fragment.FMPoetryDetailRemark;
import cn.hzmeurasia.poetryweather.fragment.FMPoetryDetailShangXi;
import cn.hzmeurasia.poetryweather.fragment.FMPoetryDetailTranslation;

/**
 * 管理fragment
 */
public class PageAdapter extends FragmentPagerAdapter {

    private int num;
    private PoetryDetail poetryDetail;
    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

    public PageAdapter(FragmentManager fm, int num, PoetryDetail poetryDetail) {
        super(fm);
        this.num = num;
        this.poetryDetail = poetryDetail;
    }


    @Override
    public Fragment getItem(int position) {
        return createFragment(position);
    }

    @Override
    public int getCount() {
        return num;
    }

    private Fragment createFragment(int pos) {
        Fragment fragment = mFragmentHashMap.get(pos);
        if (fragment == null) {
            switch (pos) {
                case 0:
                    fragment = new FMPoetryDetailTranslation();
                    break;
                case 1:
                    fragment = new FMPoetryDetailRemark();
                    break;
                case 2:
                    fragment = new FMPoetryDetailShangXi();
                    break;
                case 3:
                    fragment = new FMPoetryDetailComment();
                    break;
            }
            mFragmentHashMap.put(pos, fragment);
        }
        return fragment;
    }
}
