package com.laowch.pulltoback.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laowch.pulltoback.R;

/**
 * Created by lao on 14/12/9.
 */
public class ViewPagerFragment extends Fragment {

    ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("A Simple ViewPager");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_view_pager, container, false);

        pager = (ViewPager) layout.findViewById(R.id.pager);

        pager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return ScreenSlidePageFragment.create(i);
            }

            @Override
            public int getCount() {
                return 10;
            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

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

        return layout;
    }
}
