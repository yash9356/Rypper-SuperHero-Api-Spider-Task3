package com.example.rypper;


import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private RecyclerView mRecycleView;
    private ExampleAdapter mEampleAdapter;
    private ArrayList<Exampleitem> mExampleList;
    private RequestQueue mRequestQueue;
    Boolean isScrolling=false;
    int currrentItem,totalItem,scrollOutItem;
    private EditText searchtxt;
    private LinearLayoutManager manager;
    private ImageButton searchbtn,backbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        searchtxt=findViewById(R.id.editTextHeroName);
        searchbtn=findViewById(R.id.searchbtn);
        backbtn=findViewById(R.id.back);
        mRecycleView=findViewById(R.id.recycle_view);
        mRecycleView.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(manager);

        mExampleList= new ArrayList<>();
        mRequestQueue= Volley.newRequestQueue(this);
        parseJSON();
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExampleList.clear();
                mEampleAdapter.notifyDataSetChanged();
                searchfun();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExampleList.clear();
                mEampleAdapter.notifyDataSetChanged();
                parseJSON();
                searchtxt.setText("");
            }
        });

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);


    }

    private void pagination() {
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currrentItem= manager.getChildCount();
                totalItem=manager.getItemCount();
                scrollOutItem=manager.findFirstVisibleItemPosition();

                if(isScrolling && (currrentItem + scrollOutItem == 5)){
                    manager=new LinearLayoutManager(MainActivity.this){
                        @Override
                        public boolean canScrollVertically(){
                            return false;
                        }
                    };

                    isScrolling =false;
                    fetchData();

                }
            }
        });
    }


    private void fetchData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                manager=new LinearLayoutManager(MainActivity.this){
                    @Override
                    public boolean canScrollVertically(){
                        return true;
                    }
                };
            }
        },5000);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }



    private void parseJSON(){
        String url="https://akabab.github.io/superhero-api/api/all.json";


        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {


                    for(int i=0;i<563;i++){
                        JSONObject Heroes =response.getJSONObject(i);

                        String SuperHero = Heroes.getString("name");
                        int id=Heroes.getInt("id");
                        JSONObject imglink =Heroes.getJSONObject("images");
                        String imgurl=imglink.getString("lg");
                        JSONObject powerstat =Heroes.getJSONObject("powerstats");
                        int  power =powerstat.getInt("power");

                        mExampleList.add(new Exampleitem(imgurl,SuperHero,power,id));

                    }

                    mEampleAdapter =new ExampleAdapter(MainActivity.this,mExampleList);
                    mRecycleView.setAdapter(mEampleAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(request);
    }

    public void searchfun(){
        String url="https://akabab.github.io/superhero-api/api/all.json";


        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String searchHero =searchtxt.getText().toString();

                    String empty=" ";

                    for(int i=0;i<563;i++){
                        JSONObject Heroes =response.getJSONObject(i);

                        String SuperHero = Heroes.getString("name");
                        int id=Heroes.getInt("id");
                        JSONObject imglink =Heroes.getJSONObject("images");
                        String imgurl=imglink.getString("lg");
                        JSONObject powerstat =Heroes.getJSONObject("powerstats");
                        int  power =powerstat.getInt("power");
                        if(!searchHero.equals(empty)){
                            String SuperHeroCapital=capitalizeWord(searchHero);
                            if(SuperHero.equals(searchHero) ||  SuperHero.equals(SuperHeroCapital)  ){
                                mExampleList.add(new Exampleitem(imgurl,SuperHero,power,id));
                                mEampleAdapter =new ExampleAdapter(MainActivity.this,mExampleList);
                                mRecycleView.setAdapter(mEampleAdapter);
                            }
                            try {
                                if( Integer.parseInt(searchHero)==id){
                                    mExampleList.add(new Exampleitem(imgurl,SuperHero,power,id));
                                    mEampleAdapter =new ExampleAdapter(MainActivity.this,mExampleList);
                                    mRecycleView.setAdapter(mEampleAdapter);
                                }
                            } catch (NumberFormatException e) {
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
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(request);
    }

    public void searchHero(String herogender){
        String url="https://akabab.github.io/superhero-api/api/all.json";


        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<563;i++){
                        JSONObject Heroes =response.getJSONObject(i);

                        String SuperHero = Heroes.getString("name");
                        int id=Heroes.getInt("id");
                        JSONObject imglink =Heroes.getJSONObject("images");
                        String imgurl=imglink.getString("lg");
                        JSONObject powerstat =Heroes.getJSONObject("powerstats");
                        int  power =powerstat.getInt("power");
                        JSONObject gender=Heroes.getJSONObject("appearance");
                        String gender1=gender.getString("gender");
                        if(gender1.equals(herogender)){
                            mExampleList.add(new Exampleitem(imgurl,SuperHero,power,id));
                            mEampleAdapter =new ExampleAdapter(MainActivity.this,mExampleList);
                            mRecycleView.setAdapter(mEampleAdapter);
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
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(request);
    }

    public void searchComic(String publishercompany){
        String url="https://akabab.github.io/superhero-api/api/all.json";


        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<563;i++){
                        JSONObject Heroes =response.getJSONObject(i);

                        String SuperHero = Heroes.getString("name");
                        int id=Heroes.getInt("id");
                        JSONObject imglink =Heroes.getJSONObject("images");
                        String imgurl=imglink.getString("lg");
                        JSONObject powerstat =Heroes.getJSONObject("powerstats");
                        int  power =powerstat.getInt("power");
                        JSONObject publisher =Heroes.getJSONObject("biography");
                        String publisher1=publisher.getString("publisher");
                        if(publisher1.equals(publishercompany)){
                            mExampleList.add(new Exampleitem(imgurl,SuperHero,power,id));
                            mEampleAdapter =new ExampleAdapter(MainActivity.this,mExampleList);
                            mRecycleView.setAdapter(mEampleAdapter);
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
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.maleHero){
            mExampleList.clear();
            mEampleAdapter.notifyDataSetChanged();
            searchHero("Male");
            Toast.makeText(getApplicationContext(), "Male SuperHero", Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.femaleHero){
            mExampleList.clear();
            mEampleAdapter.notifyDataSetChanged();
            searchHero("Female");
            Toast.makeText(getApplicationContext(), "Female SuperHero", Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.Marvel){
            mExampleList.clear();
            mEampleAdapter.notifyDataSetChanged();
            searchComic("Marvel Comics");
            Toast.makeText(getApplicationContext(), "Marvel Comics Characters", Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.DC){
            mExampleList.clear();
            mEampleAdapter.notifyDataSetChanged();
            searchComic("DC Comics");
            Toast.makeText(getApplicationContext(), "DC Comics Characters", Toast.LENGTH_SHORT).show();
        }
        //This is for closing the drawer after acting on it
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static String capitalizeWord(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }
}