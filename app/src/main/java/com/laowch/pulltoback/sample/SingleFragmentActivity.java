package com.laowch.pulltoback.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.laowch.pulltoback.AbstractPullToBackActivity;
import com.laowch.pulltoback.R;


/**
 * @author Joosun
 * @since 2013-4-9
 */
public class SingleFragmentActivity extends AbstractPullToBackActivity {
    public static final String EXTRA_FRAGMENT_NAME = "extra_fragment_name";

    public static final String EXTRA_FRAGMENT_EXTRAS = "extra_fragment_extras";

    protected static final String TAG_SINGLE_FRAGMENT = "tag_single_fragment";

    @Override
    protected void onCreate(final Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);

        this.setContentView(R.layout.activity_simple);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Intent intent = this.getIntent();

        final FragmentManager fragmentManager = this.getSupportFragmentManager();

        final Fragment fragment = fragmentManager.findFragmentByTag(SingleFragmentActivity.TAG_SINGLE_FRAGMENT);

        final String fragmentName = intent.getStringExtra(SingleFragmentActivity.EXTRA_FRAGMENT_NAME);

        final Bundle bundle = intent.getBundleExtra(SingleFragmentActivity.EXTRA_FRAGMENT_EXTRAS);

        if (fragment == null) {
            this.addFragment(fragmentName, bundle);
        } else {
            if (!fragment.getClass().getName().equals(fragmentName)) {
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();

                this.addFragment(fragmentName, bundle);
            }
        }
    }


    @SuppressWarnings("unchecked")
    private void addFragment(final String pFragmentName, final Bundle pBundle) {
        try {
            final Class<? extends Fragment> fragmentClass = (Class<? extends Fragment>) Class.forName(pFragmentName);

            final FragmentManager fragmentManager = this.getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            final Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), pBundle);
            fragmentTransaction.add(R.id.content_frame, fragment, SingleFragmentActivity.TAG_SINGLE_FRAGMENT);
            fragmentTransaction.commit();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}