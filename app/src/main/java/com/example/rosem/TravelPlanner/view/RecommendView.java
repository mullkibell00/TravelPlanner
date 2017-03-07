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
import com.google.android.gms.maps.CameraUpdateFactory;
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
    private int ZOOM_LEVEL;

    Site mSite = null;
    GoogleMap mMap;
    NotifyMapReady notifyMapReady = null;

    public RecommendView(Context context) {
        super(context);

        mContext = context;

        ZOOM_LEVEL = getResources().getInteger(R.integer.zoom_level);

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
        if(mMap!=null)
        {
            LatLng location = new LatLng(site.getLat(),site.getLng());
            mMap.addMarker(new MarkerOptions().position(location));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM_LEVEL));
        }
    }

    public void setNotifyMapReady(NotifyMapReady mr)
    {
        notifyMapReady = mr;
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

    public void setSelectionVisibility(int visibility)
    {
        mSelected.setVisibility(visibility);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(notifyMapReady!=null)
        {
            notifyMapReady.notifyMapReady();
        }
    }

    public interface NotifyMapReady
    {
        public void notifyMapReady();
    }
}
