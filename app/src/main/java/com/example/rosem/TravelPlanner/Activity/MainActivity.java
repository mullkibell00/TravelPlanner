package com.example.rosem.TravelPlanner.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.course.Course;
import com.example.rosem.TravelPlanner.fragment.FavoriteFragment;
import com.example.rosem.TravelPlanner.fragment.ManageFragment;
import com.example.rosem.TravelPlanner.fragment.SettingFragment;
import com.example.rosem.TravelPlanner.fragment.ShareFragment;
import com.example.rosem.TravelPlanner.plan.Plan;

import org.json.JSONArray;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    FavoriteFragment favorite;
    ManageFragment manage;
    ShareFragment share;
    SettingFragment setting;
    Typeface fontType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for debugging
        createInitialFavorite();

        fontType = Typeface.createFromAsset(getAssets(),getString(R.string.font_name));

        toolbar = (Toolbar)findViewById(R.id.toolbar);//내가 만든 툴바를 가져오기
        setSupportActionBar(toolbar);//내가 만든 툴바를 액션바로 셋
        ActionBar actionBar = getSupportActionBar();//셋해둔 액션바를 진짜 액션바로 넘기기
        actionBar.setDisplayShowTitleEnabled(false);//기본 액션바 끄는 거
        //setting font
        TextView appTitle = (TextView)toolbar.findViewById(R.id.title_bar_title);
        appTitle.setTypeface(fontType);


        //fragment 객체를 만들기
        favorite = FavoriteFragment.newInstance(getSupportFragmentManager());
        manage = new ManageFragment();
        share = new ShareFragment();
        setting = new SettingFragment();

        //즐겨찾기가 메인이 되도록 하기
        getSupportFragmentManager().beginTransaction().replace(R.id.container,favorite).commit();//main tap

        //탭들 생성
        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setIcon(R.mipmap.star));
        tabs.addTab(tabs.newTab().setIcon(R.mipmap.route));
        tabs.addTab(tabs.newTab().setIcon(R.mipmap.share));
        tabs.addTab(tabs.newTab().setIcon(R.mipmap.settings));

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

    //for debugging
    private void createInitialFavorite()
    {
        Realm realm = Realm.getDefaultInstance();

        Plan plan = new Plan();
        JSONArray day1 = new JSONArray();

        Course c =new Course();
        c.setName("London Eye");
        c.setTime("10:00~10:15");
        c.setCostTime("15min");
        c.setCostMoney("10pounds");
        c.setAddr("London");

        day1.put(c);

        Course c2 = new Course();

        c2.setName("London Bridge");
        c2.setTime("10:35~11:00");
        c2.setCostTime("20min");
        c2.setCostMoney("5pounds");
        c2.setAddr("London");

        day1.put(c2);

        plan.addDay(day1);

        JSONArray day2 = new JSONArray();

        Course c3 = new Course();

        c3.setName("London Tower");
        c3.setTime("10:10~10:15");
        c3.setCostTime("10min");
        c3.setCostMoney("15pounds");
        c3.setAddr("London");

        day2.put(c3);

        Course c4 = new Course();

        c4.setName("Great Britain Museum");
        c4.setTime("10:45~13:00");
        c4.setCostTime("30min");
        c4.setCostMoney("20pounds");
        c4.setAddr("London");

        day2.put(c4);

        plan.addDay(day2);

        plan.setPlanName("favorite");
        plan.setFavorite(true);
        plan.setPlanFromPlanArray();
        Log.v("Main:::","plan\n"+plan.toString());

        realm.beginTransaction();
        //만약 코드에서 생성한 객체를 집어넣으려면 copyTo를!
        realm.copyToRealmOrUpdate(plan);
        realm.commitTransaction();

        RealmResults<Plan> results = realm.where(Plan.class).equalTo("isFavorite",true).findAll();
        Log.v("Main:::","resultSize="+results.size());
        Log.v("Main:::","plan\n"+(results.first()).getPlan());

        RealmResults<Plan> test =realm.where(Plan.class).findAll();
        Log.v("Main:::","resultSize="+test.size());
        Log.v("Main:::","plan\n"+(test.first()).getPlan());

        if(realm!=null)
        {
            realm.close();
        }
    }
}
