package com.example.rosem.TravelPlanner.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.rosem.TravelPlanner.fragment.FavoriteFragment;
import com.example.rosem.TravelPlanner.fragment.ManageFragment;
import com.example.rosem.TravelPlanner.fragment.SettingFragment;
import com.example.rosem.TravelPlanner.fragment.ShareFragment;
import com.example.rosem.TravelPlanner.R;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    FavoriteFragment favorite;
    ManageFragment manage;
    ShareFragment share;
    SettingFragment setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);//내가 만든 툴바를 가져오기
        setSupportActionBar(toolbar);//내가 만든 툴바를 액션바로 셋
        ActionBar actionBar = getSupportActionBar();//셋해둔 액션바를 진짜 액션바로 넘기기
        actionBar.setDisplayShowTitleEnabled(false);//기본 액션바 끄는 거

        //fragment 객체를 만들기
        favorite = FavoriteFragment.newInstance(getSupportFragmentManager());
        manage = new ManageFragment();
        share = new ShareFragment();
        setting = new SettingFragment();

        //즐겨찾기가 메인이 되도록 하기
        getSupportFragmentManager().beginTransaction().replace(R.id.container,favorite).commit();//main tap

        //탭들 생성
        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setIcon(R.drawable.star));
        tabs.addTab(tabs.newTab().setIcon(R.drawable.route));
        tabs.addTab(tabs.newTab().setIcon(R.drawable.share));
        tabs.addTab(tabs.newTab().setIcon(R.drawable.settings));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();//어느 탭이 선택되었나
                Fragment selected = null; //눌려진 탭을 저장할 변수

                if(position ==0)//첫번째 탭(즐겨찾기)
                {
                    selected = favorite;
                }
                else if(position ==1)//두번째 탭(전체 스케쥴관리)
                {
                    selected = manage;
                }
                else if(position==2)//세번째 탭(공유)
                {
                    selected = share;
                }
                else if(position==3)//네번째탭(세팅)
                {
                    selected = setting;
                }

                //현재 보여주는 탭을 selected로 변경

                getSupportFragmentManager().beginTransaction().replace(R.id.container,selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
