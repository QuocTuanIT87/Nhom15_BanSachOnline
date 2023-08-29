package com.tuan1611pupu.appbansach.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tuan1611pupu.appbansach.Adapter.ViewPagerAdapter;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Book;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    ViewPager viewPager;
    ArrayList<Book> data = new ArrayList<>();
    GridView gvListBook;
    RecyclerView rcListBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.layout_home);
        setControl();
        setEvent();
    }

    private void setEvent() {
        setUpViewPager();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_search:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_cart:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.action_my_page:
                        viewPager.setCurrentItem(3);
                        break;

                }
                return true;
            }
        });
    }

    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        navigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.action_search).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.action_cart).setChecked(true);
                        break;
                    case 3:
                        navigationView.getMenu().findItem(R.id.action_my_page).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setControl() {
        navigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.view_pager);
        //gvListBook = findViewById(R.id.gvListBook);
        rcListBook = findViewById(R.id.rcListBook);
    }
}