package com.tuan1611pupu.appbansach.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.admin.AccountFragment;
import com.tuan1611pupu.appbansach.admin.AdminListbook;
import com.tuan1611pupu.appbansach.admin.AuthorFragment;
import com.tuan1611pupu.appbansach.admin.CategoryFragment;
import com.tuan1611pupu.appbansach.admin.OrderListFragment;
import com.tuan1611pupu.appbansach.admin.PublisherFragment;
import com.tuan1611pupu.appbansach.model.UserExprech;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

public class AdminMenuActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
   // CRUDFragment crudFragment = new CRUDFragment();
    AuthorFragment authorFragment = new AuthorFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    OrderListFragment orderListFragment = new OrderListFragment();
    PublisherFragment publisherFragment = new PublisherFragment();
    AdminListbook adminListbook = new AdminListbook();

    private PreferenceManager preferenceManager;
    TextView btnAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        preferenceManager = new PreferenceManager(getApplicationContext());

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.admin_menu_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.admin_menu_container,authorFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.author:
                        getSupportFragmentManager().beginTransaction().replace(R.id.admin_menu_container,authorFragment).commit();
                        return true;
                    case R.id.category:
                        getSupportFragmentManager().beginTransaction().replace(R.id.admin_menu_container,categoryFragment).commit();
                        return true;
                    case R.id.order:
                        getSupportFragmentManager().beginTransaction().replace(R.id.admin_menu_container,orderListFragment).commit();
                        return true;
                    case R.id.publisher:
                        getSupportFragmentManager().beginTransaction().replace(R.id.admin_menu_container,publisherFragment).commit();
                        return true;
                    case R.id.list_book:
                        getSupportFragmentManager().beginTransaction().replace(R.id.admin_menu_container,adminListbook).commit();
                        return true;
                }
                return false;
            }
        });
        btnAcc = (TextView) findViewById(R.id.admin_menu_btnAccount);
        btnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountFragment accountFragment = new AccountFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_menu_container,accountFragment).commit();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}