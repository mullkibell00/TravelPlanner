package com.example.rosem.TravelPlanner.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.Fragment.FavoriteFragment;
import com.example.rosem.TravelPlanner.Fragment.ManageFragment;
import com.example.rosem.TravelPlanner.Fragment.RecommendFragment;
import com.example.rosem.TravelPlanner.Fragment.TestFragment;
import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.course.Course;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.rosem.TravelPlanner.R.mipmap.route;
import static com.example.rosem.TravelPlanner.R.mipmap.star;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    FavoriteFragment favorite;
    ManageFragment manage;
    RecommendFragment recommend;
    //SettingFragment setting;
    //for debugging
    TestFragment test;
    Typeface fontType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for debugging
        createInitialFavorite();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //checking user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null)
        {
            //signin activity로 넘어간다
            startActivity(new Intent(this,SignInActivity.class));
        }

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
        recommend = RecommendFragment.newInstance(fontType);
        //setting = new SettingFragment();
        //for debugging
        test = new TestFragment();

        //즐겨찾기가 메인이 되도록 하기
        getSupportFragmentManager().beginTransaction().replace(R.id.container,favorite).commit();//main tap

        //탭들 생성
        //아이콘 색 선정
        int iconColor = ContextCompat.getColor(this, R.color.colorPrimary);
        PorterDuff.Mode iconMode = PorterDuff.Mode.SRC_IN;
        //tab load
        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);
        //set tab drawables
        Drawable iconStar = ContextCompat.getDrawable(getApplicationContext(), star);
        iconStar.setColorFilter(iconColor, iconMode);
        Drawable iconRoute = ContextCompat.getDrawable(getApplicationContext(), route);
        iconRoute.setColorFilter(iconColor, iconMode);
        Drawable iconShare = ContextCompat.getDrawable(getApplicationContext(),R.mipmap.share);
        iconShare.setColorFilter(iconColor, iconMode);
        Drawable iconSettings = ContextCompat.getDrawable(getApplicationContext(),R.mipmap.settings);
        iconSettings.setColorFilter(iconColor, iconMode);
        //set tab icons
        tabs.addTab(tabs.newTab().setIcon(iconStar));
        tabs.addTab(tabs.newTab().setIcon(iconRoute));
        tabs.addTab(tabs.newTab().setIcon(iconShare));
        tabs.addTab(tabs.newTab().setIcon(iconSettings));

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
                    selected = recommend;
                }
                else if(position==3)//네번째탭(세팅)
                {
                    //selected = setting;
                    //for debugging
                    selected = test;
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
        c.setSpendTime("15min");
        c.setCostTime("10min");
        c.setAddr("London");

        day1.put(c);

        Course c2 = new Course();

        c2.setName("London Bridge");
        c2.setTime("10:35~11:00");
        c2.setSpendTime("20min");
        c2.setCostTime("5min");
        c2.setAddr("London");

        day1.put(c2);

        plan.addDay(day1);

        JSONArray day2 = new JSONArray();

        Course c3 = new Course();

        c3.setName("London Tower");
        c3.setTime("10:10~10:15");
        c3.setSpendTime("10min");
        c3.setCostTime("15min");
        c3.setAddr("London");

        day2.put(c3);

        Course c4 = new Course();

        c4.setName("Great Britain Museum");
        c4.setTime("10:45~13:00");
        c4.setSpendTime("30min");
        c4.setCostTime("20min");
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
