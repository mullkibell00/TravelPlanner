package com.example.rosem.TravelPlanner.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rosem.TravelPlanner.Activity.SignInActivity;
import com.example.rosem.TravelPlanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by rosem on 2017-01-09.
 */
public class SettingFragment extends android.support.v4.app.Fragment {

    private Typeface fontType;
    private final int SIGN_IN = 1214;
    private TextView logout;

    public SettingFragment()
    {

    }

    public static SettingFragment newInstance(Typeface fontType) {

        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
        fragment.setFontType(fontType);
        return fragment;
    }

    public void setFontType(Typeface type)
    {
        this.fontType = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.setting_layout,container,false);

        logout = (TextView)view.findViewById(R.id.logout);
        TextView license = (TextView)view.findViewById(R.id.license);

        logout.setTypeface(fontType);
        license.setTypeface(fontType);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                if(user!=null)
                {
                    auth.signOut();
                    Toast.makeText(getContext(), getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
                    logout.setText(getString(R.string.login));
                }
                else
                {
                    Intent intent = new Intent(getContext(),SignInActivity.class);
                    startActivityForResult(intent,SIGN_IN);

                }

            }
        });
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.license_url)));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SIGN_IN)
        {
            logout.setText(getString(R.string.logout));
        }
    }
}
