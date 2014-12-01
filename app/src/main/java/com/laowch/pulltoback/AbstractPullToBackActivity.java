package com.laowch.pulltoback;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by lao on 14/11/30.
 */
public class AbstractPullToBackActivity extends ActionBarActivity {


    protected PullToBackLayout getPullToBackLayout() {
        return (PullToBackLayout) findViewById(R.id.pull_to_back_layout);
    }


    protected View getTitleView() {
        return findViewById(R.id.toolbar_title);
    }


    @Override
    public void setTitle(CharSequence title) {
        if (getTitleView() instanceof TextView) {
            ((TextView) getTitleView()).setText(title);
        } else {
            super.setTitle(title);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (getPullToBackLayout() != null) {
            getPullToBackLayout().startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom));
        }

    }


}
