package com.example.rosem.TravelPlanner.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rosem.TravelPlanner.Interface.GetRecommendService;
import com.example.rosem.TravelPlanner.R;
import com.example.rosem.TravelPlanner.plan.Plan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by rosem on 2017-05-22.
 */

public class TestFragment extends Fragment {

    Realm db;
    Plan plan = null;
    ServerResponseHandler serverHandler;
    private String errorMsg;
    private final int SERVER_DONE = 1567;
    TextView text;
    TextView err;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Realm.getDefaultInstance();
        plan = db.where(Plan.class).findFirst();
        errorMsg = getString(R.string.error);
        serverHandler = new ServerResponseHandler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(db!=null)
        {
            db.close();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.test_fragment_layout,container,false);
        TextView button = (TextView)view.findViewById(R.id.button);
        text = (TextView)view.findViewById(R.id.text);
        err = (TextView)view.findViewById(R.id.error);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                err.setText("");
                text.setText("");
                send();
            }
        });

        return view;
    }

    public void send()
    {
        //서버에 전송
        JSONObject body = new JSONObject();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null)
        {
            return;
        }
/*
        Gson gson = new Gson();
        SendData data = new SendData();
        data.setCountry(plan.getCountry());
        data.setPlanName(plan.getPlanName());
        data.setNumOfDay(plan.getNumOfDays());
        data.setUserId(user.getUid());
        if(plan.setPlanArrayFromPlan())
        {
            data.setPlan(plan.getPlan());
        }
        String str = gson.toJson(data);
*/
        //creating body

        try {
            body.put("country",plan.getCountry());
            body.put("planName",plan.getPlanName());
            body.put("numOfDay",plan.getNumOfDays());
            body.put("userId",user.getUid());
            if(plan.setPlanArrayFromPlan())
            {
                body.put("plan",plan.getPlan());
            }
            else
            {
                err.setText("error : creating json");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            err.setText(e.toString());
        }
        //send to server
        try {
            String strBody = URLEncoder.encode(plan.getCountry(),"UTF-8");
            new SaveToServer(strBody,serverHandler).execute();
        } catch (Exception e) {
            e.printStackTrace();
            //FirebaseCrash.report(e);
            err.setText(e.toString());
        }
    }

    private class SaveToServer extends AsyncTask<Call<String>,Void,String>
    {
        String country;
        ServerResponseHandler handler;
        public SaveToServer(String country, ServerResponseHandler handler) {
            super();
            this.country = country;
            this.handler = handler;
        }

        @Override
        protected String doInBackground(Call<String>... params) {
            GetRecommendService service = GetRecommendService.retrofit.create(GetRecommendService.class);
            Call<ResponseBody>  call = service.getRecommend(country, 0, 2);
            ResponseBody response = null;
            try {
                response = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response!=null)
            {
                //try {
                String result = null;
                try {
                    result = response.string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "error! response.string()";
                }
                return result;
                //} catch (IOException e) {
               //     e.printStackTrace();
               // }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            if(s!=null)
            {
               // Log.v("SERVER_SAVE::",s);
                text.setText(s);
                handler.sendEmptyMessage(SERVER_DONE);
            }
        }
    }

    private class ServerResponseHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case SERVER_DONE:
                    err.setText("sending finished");
            }
        }
    }

    private class SendData
    {
        String country;
        String planName;
        int numOfDay;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public int getNumOfDay() {
            return numOfDay;
        }

        public void setNumOfDay(int numOfDay) {
            this.numOfDay = numOfDay;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        String userId;
        String plan;

    }
}
