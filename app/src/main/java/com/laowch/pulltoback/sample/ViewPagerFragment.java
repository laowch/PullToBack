package com.laowch.pulltoback.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laowch.pulltoback.PullToBackLayout;
import com.laowch.pulltoback.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lao on 14/12/9.
 */
public class ViewPagerFragment extends Fragment {

    PullToBackLayout layout;

    ViewPager pager;

    MyPagerAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("A Simple ViewPager");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (PullToBackLayout) inflater.inflate(R.layout.fragment_view_pager, container, false);

        pager = (ViewPager) layout.findViewById(R.id.pager);

        adapter = new MyPagerAdapter(getFragmentManager());

        pager.setAdapter(adapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ScreenSlidePageFragment fragment = (ScreenSlidePageFragment) adapter.getFragment(position);
                layout.setScrollChild(fragment.getScroll());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    pager.requestDisallowInterceptTouchEvent(false);
                } else {
                    pager.requestDisallowInterceptTouchEvent(true);
                }
            }
        });

        pager.post(new Runnable() {
            @Override
            public void run() {
                ScreenSlidePageFragment fragment = (ScreenSlidePageFragment) adapter.getFragment(pager.getCurrentItem());
                layout.setScrollChild(fragment.getScroll());
            }
        });

        return layout;
    }


    // A simple adapter can get Fragment by position.
    class MyPagerAdapter extends FragmentPagerAdapter {


        Map<Integer, String> mFragmentTags;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = ScreenSlidePageFragment.create(getActivity(), i);
            return fragment;
        }

        @Override
        public int getCount() {
            return 10;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return getFragmentManager().findFragmentByTag(tag);
        }
    }
}
