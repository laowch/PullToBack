package com.laowch.pulltoback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.laowch.pulltoback.sample.ListViewFragment;
import com.laowch.pulltoback.sample.ScrollViewFragment;
import com.laowch.pulltoback.sample.SingleFragmentActivity;
import com.laowch.pulltoback.sample.ViewPagerFragment;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{
                "ListView", "ScrollView", "ViewPager"}));

        listView.setOnItemClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, SingleFragmentActivity.class);


        switch (position) {
            case 0:
                intent.putExtra(SingleFragmentActivity.EXTRA_FRAGMENT_NAME, ListViewFragment.class.getName());
                startActivity(intent);
                break;
            case 1:
                intent.putExtra(SingleFragmentActivity.EXTRA_FRAGMENT_NAME, ScrollViewFragment.class.getName());
                startActivity(intent);
                break;
            case 2:
                intent.putExtra(SingleFragmentActivity.EXTRA_FRAGMENT_NAME, ViewPagerFragment.class.getName());
                startActivity(intent);
                break;
        }
    }
}
