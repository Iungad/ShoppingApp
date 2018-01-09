package cf.aditya.shoppingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Iterator;

import cf.aditya.shoppingapp.adapter.ProductAdapter;
import cf.aditya.shoppingapp.data.Product;
import cf.aditya.shoppingapp.data.ProductCategory;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Menu category;
    ArrayList<ProductCategory> ProductCategoryArray;
    ArrayList<Product> ProductArray;
    ListView productList;
    RequestQueue requestQueue;
    TextView noInternetText;
    ImageView noInternetIcon;
    ProgressBar progressBar;
    private String producturl = "https://adityapahansu.000webhostapp.com/app/featured.php";
    private String categoryurl = "https://adityapahansu.000webhostapp.com/app/allproductcategory.php";
    private StringRequest stringRequest;
    private SharedPreferences sp;
    private String sessionurl ="https://adityapahansu.000webhostapp.com/app/session.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        noInternetText = (TextView)findViewById(R.id.nointernettxet);
        noInternetIcon = (ImageView)findViewById(R.id.nointerneticon);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ProductCategoryArray = new ArrayList<ProductCategory>();
        ProductArray = new ArrayList<Product>();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        if (isNetworkAvailable()){
            SetSession();
            GetCategoryTask();
            FeaturedProduct();
        } else
        {
            noInternetIcon.setVisibility(View.VISIBLE);
            noInternetText.setVisibility(View.VISIBLE);
        }



        productList =(ListView)findViewById(R.id.productList);

    }

    private void SetSession() {

        String result;
        sp = getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        result = sp.getString("session",null);
        if (result==null){
            JsonObjectRequest jObj = new JsonObjectRequest(JsonObjectRequest.Method.GET, sessionurl, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    SharedPreferences.Editor editor = sp.edit();
                    try {
                        editor.putString("session",response.getString("session"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.apply();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(jObj);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent mIntent = item.getIntent();

        startActivity(mIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            return  true;
        }
        return false;
    }

    // adding category list to drawer menu
    private void GetCategoryTask() {
        stringRequest = new StringRequest(StringRequest.Method.GET, categoryurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jArray = null;
                try {
                    jArray = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObj = null;
                    try {
                        jObj = jArray.getJSONObject(i);
                        ProductCategory pc = new ProductCategory();
                        pc.setId(jObj.getInt("id"));
                        pc.setName(jObj.getString("category"));
                        ProductCategoryArray.add(pc);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                addMenuItemInNavMenuDrawer();
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);

    }

        private void addMenuItemInNavMenuDrawer() {
            NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

            Menu menu = navView.getMenu();
            Menu submenu =  menu.addSubMenu("Categories");


            Iterator<ProductCategory> it = ProductCategoryArray.iterator();
            while (it.hasNext()){
                ProductCategory pc = (ProductCategory)it.next();
                Intent intent = new Intent(getApplicationContext(),ProductByCategory.class);
                intent.putExtra("CategoryId",pc.getId());
                intent.putExtra("CategoryName",pc.getName());
                submenu.add(pc.getName()).setIcon(R.drawable.ic_local_florist_black_24dp).setIntent(intent);
            }

            Menu submenu2 = menu.addSubMenu("User");

            submenu2.add("Account").setIcon(R.drawable.ic_account_name);
            submenu2.add("Cart").setIcon(R.drawable.ic_shopping_cart_black_24dp).setIntent(new Intent(getApplicationContext(),CartActivity.class));

            navView.invalidate();
        }

        // adding featured product to main activity's list view
    public void FeaturedProduct() {

        progressBar.setVisibility(View.VISIBLE);
        stringRequest = new StringRequest(StringRequest.Method.GET, producturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray jArray = new JSONArray(response);

                    for (int i =0;i<jArray.length();i++)
                    {
                        JSONObject jObj = jArray.getJSONObject(i);
                        Product pc = new Product();
                        pc.setId(jObj.getInt("id"));
                        pc.setName(jObj.getString("name"));
                        pc.setPrice(jObj.getDouble("price"));
                        pc.setDiscount(jObj.getInt("discount"));
                        pc.setBrand(jObj.getString("brand"));
                        pc.setDiscountedprice(pc.getPrice()-((pc.getPrice()*pc.getDiscount())/100));
                        pc.setImage(jObj.getString("image1"));
                        ProductArray.add(pc);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ProductAdapter pAdapter = new ProductAdapter(getApplicationContext(),R.layout.list_row,ProductArray);
                productList.setAdapter(pAdapter);


                progressBar.setVisibility(View.GONE);
                productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Product pd = (Product) adapterView.getItemAtPosition(i);
                        Intent intent = new Intent(getApplicationContext(), cf.aditya.shoppingapp.Product.class);
                        intent.putExtra("ProductName",pd.getName());
                        intent.putExtra("ProductId",pd.getId());
                        startActivity(intent);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
       requestQueue.add(stringRequest);
        }


}