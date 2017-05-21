package com.example.rosem.TravelPlanner.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.R;

/**
 * Created by rosem on 2017-02-25.
 */

public class PlanNameView extends RelativeLayout {

    private TextView planName;
    //private ImageView upIcon;
    //private ImageView downIcon;
    private ImageView deleteIcon;
    private ImageView okIcon;
    private Typeface fontType;
    private Context context;
    private int iconColor;
    private PorterDuff.Mode iconMode;

    public PlanNameView(Context context) {
        super(context);

        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.plan_name_list,this,true);

        this.context = context;
        iconColor = ContextCompat.getColor(context,R.color.colorButton);
        iconMode = PorterDuff.Mode.SRC_IN;

        fontType = Typeface.createFromAsset(context.getAssets(),context.getString(R.string.font_name));

        planName = (TextView)findViewById(R.id.plan_name);
        planName.setTypeface(fontType);

        //upIcon = (ImageView)findViewById(R.id.plan_name_list_up);

        //downIcon = (ImageView)findViewById(R.id.plan_name_list_down);

        deleteIcon = (ImageView)findViewById(R.id.plan_name_delete);
        okIcon = (ImageView)findViewById(R.id.plan_name_ok);

       // upIcon.setVisibility(INVISIBLE);
        //downIcon.setVisibility(INVISIBLE);
        deleteIcon.setVisibility(INVISIBLE);

        setDeleteIcon();
        setOkIcon();
    }

    public void setTypeface(Typeface fontType)
    {
        planName.setTypeface(fontType);
    }

    public void setDeleteIcon() {

        Drawable img = ContextCompat.getDrawable(context,R.mipmap.trash_bin);
        img.setColorFilter(iconColor,iconMode);

        deleteIcon.setImageDrawable(img);
    }

    public void setOkIcon()
    {
        Drawable img = ContextCompat.getDrawable(context,R.mipmap.ok_pressed);
        img.setColorFilter(iconColor,iconMode);
        okIcon.setImageDrawable(img);
    }

    public void setDownIcon(int imgId) {

        Drawable img = ContextCompat.getDrawable(context,imgId);
        img.setColorFilter(iconColor,iconMode);

        //downIcon.setImageDrawable(img);
    }

    public void setUpIcon(int imgId) {

        Drawable img = ContextCompat.getDrawable(context,imgId);
        img.setColorFilter(iconColor,iconMode);

        //upIcon.setImageDrawable(img);
    }

    public void setDeleteIcon(Drawable img) {
        img.setColorFilter(iconColor,iconMode);
        deleteIcon.setImageDrawable(img);
    }

    public void setDownIcon(Drawable img) {
        img.setColorFilter(iconColor,iconMode);
        //downIcon.setImageDrawable(img);
    }

    public void setUpIcon(Drawable img) {
        img.setColorFilter(iconColor,iconMode);
        //upIcon.setImageDrawable(img);
    }

    public String getPlanName() {
        return planName.getText().toString();
    }

    public void setPlanName(String name) {
        planName.setText(name);
    }

    public void toggleVisibility()
    {
        if(deleteIcon.getVisibility()==VISIBLE)
        {
           // upIcon.setVisibility(INVISIBLE);
           // downIcon.setVisibility(INVISIBLE);
            deleteIcon.setVisibility(INVISIBLE);
            okIcon.setVisibility(INVISIBLE);
        }
        else
        {
          //  upIcon.setVisibility(VISIBLE);
           // downIcon.setVisibility(VISIBLE);
            deleteIcon.setVisibility(VISIBLE);
            okIcon.setVisibility(VISIBLE);
        }
    }

    public void setVisible()
    {
       // upIcon.setVisibility(VISIBLE);
       // downIcon.setVisibility(VISIBLE);
        deleteIcon.setVisibility(VISIBLE);
        okIcon.setVisibility(VISIBLE);
    }
    public void setInvisible()
    {
       // upIcon.setVisibility(INVISIBLE);
       // downIcon.setVisibility(INVISIBLE);
        deleteIcon.setVisibility(INVISIBLE);
        okIcon.setVisibility(INVISIBLE);
    }
}
