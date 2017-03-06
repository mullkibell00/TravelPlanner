package com.example.rosem.TravelPlanner.view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.object.Site;

/**
 * Created by rosem on 2017-03-06.
 */

public class SiteItemView extends RelativeLayout {
    TextView mSiteName;
    TextView mSiteAddr;
    ImageView editButton;
    ImageView deleteButton;
    ImageView confirmButton;

    Site mSite = null;
    RelativeLayout mView;
    Context mContext;
    public SiteItemView(Context context) {
        super(context);
        mContext = context;

        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.site_item_view,this,true);

        Typeface fontType = Typeface.createFromAsset(context.getAssets(),context.getString(R.string.font_name));

        mSiteName = (TextView)findViewById(R.id.site_item_name); mSiteName.setTypeface(fontType);
        mSiteAddr = (TextView)findViewById(R.id.site_item_addr); mSiteAddr.setTypeface(fontType);
        mView = (RelativeLayout)findViewById(R.id.site_item_layout);
        editButton = (ImageView)findViewById(R.id.site_item_edit);
        deleteButton = (ImageView)findViewById(R.id.site_item_delete);
        confirmButton = (ImageView)findViewById(R.id.site_item_ok);
        setButtonVisibility(View.INVISIBLE);
    }

    public String getSiteName()
    {
        return mSiteName.getText().toString();
    }

    public String getSiteAddr()
    {
        return mSiteAddr.getText().toString();
    }

    public Site getSite()
    {
        return mSite;
    }
    public void setSite(Site site)
    {
        mSite = site;
        mSiteName.setText(site.getPlaceName());
        mSiteAddr.setText(site.getAddress());
    }

    public void toggleVisibility()
    {
        if(editButton.getVisibility()== View.VISIBLE)
        {
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            confirmButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.VISIBLE);
        }
    }
    public int setButtonVisibility(int visibility)
    {
        int ret = editButton.getVisibility();

        editButton.setVisibility(visibility);
        deleteButton.setVisibility(visibility);
        confirmButton.setVisibility(visibility);

        return ret;
    }

}
