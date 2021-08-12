package com.example.rypper;

import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;

import android.transition.TransitionInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoActivity extends AppCompatActivity {


    private static InfoActivity instance = null;
    ImageView imageView;
    String gender1;
    String race1;
    String FullName;
    TextView heroname,herogender,herorace,herofullname,heropublisher,heroconnection,heroappearence,heropower,herocombat,herointelligence,herospeed,herostrength,heroalignment;
    private RequestQueue mRequestQueue;
    int id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        imageView=findViewById(R.id.image_view);
        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.transition_image));
        imageView.setTransitionName("image_view");
        String ImgUrl=getIntent().getStringExtra("ImgUrl");
        Picasso.with(InfoActivity.this).load(ImgUrl).fit().centerInside().into(imageView);

        heroname =findViewById(R.id.HeroName);
        herogender=findViewById(R.id.HeroGender);
        herorace=findViewById(R.id.HeroRace);
        heropublisher=findViewById(R.id.publisherTxt);
        heroconnection=findViewById(R.id.connectionTxt);
        herocombat=findViewById(R.id.CombatTxt);
        heropower=findViewById(R.id.PowerTxt);
        heroappearence=findViewById(R.id.appearenceTxt);
        herointelligence=findViewById(R.id.IntelligenceTxt);
        herospeed=findViewById(R.id.SpeedTxt);
        herostrength=findViewById(R.id.StrengthTxt);
        herofullname=findViewById(R.id.FullName);
        heroalignment=findViewById(R.id.Heroalignment);
        mRequestQueue= Volley.newRequestQueue(this);
        id1=getIntent().getIntExtra("ID",0);
        parseJson(id1,imageView);

    }

    public void parseJson(int id1,ImageView imageView){

        String url="https://akabab.github.io/superhero-api/api/all.json";

        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<563;i++){
                        JSONObject Heroes =response.getJSONObject(i);
                        int id=Heroes.getInt("id");
                        String SuperHero = Heroes.getString("name");

                        JSONObject imglink =Heroes.getJSONObject("images");
                        //String imgurl=imglink.getString("md");
                        JSONObject powerstat =Heroes.getJSONObject("powerstats");
                        String power=powerstat.getString("power");
                        String intelligence=powerstat.getString("intelligence");
                        String strength=powerstat.getString("strength");
                        String speed=powerstat.getString("speed");
                        String combat=powerstat.getString("combat");
                        JSONObject connection =Heroes.getJSONObject("connections");
                        String groupconnection =connection.getString("groupAffiliation");

                        JSONObject gender=Heroes.getJSONObject("appearance");
                        try {
                            gender1=gender.getString("gender");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            race1=gender.getString("race");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONObject biography=Heroes.getJSONObject("biography");
                        try {
                             FullName =biography.getString("fullName");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String publisher=biography.getString("publisher");
                        String alignment=biography.getString("alignment");
                        String firstAppearance=biography.getString("firstAppearance");

                        if (id1 == id) {
//                            Picasso.with(InfoActivity.this).load(imgurl).fit().centerInside().into(imageView);
                            heroname.setText(SuperHero);
                            heropublisher.setText(publisher);
                            heroappearence.setText(firstAppearance);
                            heroalignment.setText(alignment);
                            heropower.setText(power);
                            herospeed.setText(speed);
                            herostrength.setText(strength);
                            herocombat.setText(combat);
                            herointelligence.setText(intelligence);
                            heroconnection.setText(groupconnection);


                            try {
                                herogender.setText(gender1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                herorace.setText(race1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                herofullname.setText(FullName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(InfoActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(request);

    }


}