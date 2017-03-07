package com.example.rosem.TravelPlanner.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.object.Site;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by rosem on 2017-03-07.
 */

public class RecommendView extends LinearLayout implements OnMapReadyCallback{

    TextView mName;
    TextView mAddr;
    ImageView mSelected;
    MapView mMapView;
    Context mContext;

    Site mSite = null;
    GoogleMap mMap;

    public RecommendView(Context context) {
        super(context);

        mContext = context;

        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.recommend_view,this,true);

        Typeface fontType = Typeface.createFromAsset(context.getAssets(),context.getString(R.string.font_name));

        mName = (TextView)findViewById(R.id.recommend_view_name); mName.setTypeface(fontType);
        mAddr = (TextView)findViewById(R.id.recommend_view_addr); mAddr.setTypeface(fontType);
        mMapView = (MapView)findViewById(R.id.recommend_view_map);
        mSelected = (ImageView)findViewById(R.id.recommend_selected); mSelected.setVisibility(View.INVISIBLE);
        mMapView.getMapAsync(this);

    }

    public Site getSite()
    {
        return mSite;
    }

    public void setSite(Site site)
    {
        mSite = site;
        mName.setText(site.getPlaceName());
        mAddr.setText(site.getAddress());
        mMap.addMarker(new MarkerOptions().position(new LatLng(site.getLat(),site.getLng())));
    }

    public void mapViewOnCreate(Bundle SaveInstanceState)
    {
        if(mMapView!=null)
        {
            mMapView.onCreate(SaveInstanceState);
        }
    }

    public void mapViewOnResume()
    {
        if(mMapView!=null)
        {
            mMapView.onResume();
        }
    }

    public boolean toggleVisibility()
    {
        if(mSelected.getVisibility()== View.VISIBLE)
        {
            mSelected.setVisibility(View.INVISIBLE);
            return false;
        }
        else
        {
            mSelected.setVisibility(View.VISIBLE);
            return true;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
