package cf.aditya.shoppingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

import cf.aditya.shoppingapp.adapter.ProductImageAdapter;

public class Product extends AppCompatActivity {

    private static final String INTERNET ="INTERNET_REQUEST" ;
    String productName,productId;
    private String url = "https://adityapahansu.000webhostapp.com/app/product.php";
    TextView name,price,detail,oldprice,brand;
    private ProductImageAdapter productImageAdapter;
    private ViewPager vp;
    ProgressBar productLoadingBar;
    private RequestQueue reqQ;
    private String carturl ="https://adityapahansu.000webhostapp.com/app/addtocart.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent rIntent = getIntent();
        productName = rIntent.getStringExtra("ProductName");
        productId = String.valueOf(rIntent.getIntExtra("ProductId",0));
        setTitle(productName);
        reqQ = Volley.newRequestQueue(getApplicationContext());
        name = (TextView)findViewById(R.id.product_Name);
        price =(TextView)findViewById(R.id.product_Price);
        detail =(TextView)findViewById(R.id.product_detail);
        oldprice=(TextView)findViewById(R.id.product_old_price);
        brand=(TextView)findViewById(R.id.product_brand);
        productLoadingBar =(ProgressBar)findViewById(R.id.product_loading_bar);
        productLoadingBar.setVisibility(View.VISIBLE);

        GetProduct();
        vp = (ViewPager) findViewById(R.id.productimagepager);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Adding Product to Cart", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addToCart();
            }
        });
    }

    @Override
    protected void onStop() {
        reqQ.cancelAll(INTERNET);
        super.onStop();
    }

    private void GetProduct() {

        StringRequest sReq = new StringRequest(StringRequest.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONArray(response).getJSONObject(0);

                    name.setText(jObj.getString("name"));
                    price.setText("₹"+String.valueOf(jObj.getDouble("price")-((jObj.getDouble("price")*jObj.getInt("discount"))/100)));
                    oldprice.setText("₹"+jObj.getString("price"));
                    brand.setText(jObj.getString("brand"));
                    detail.setText(jObj.getString("detail"));

                    String[] s = new String[]{"https://adityapahansu.000webhostapp.com/panel/uploads/images/products/tiny/"+jObj.getString("image1"),
                    "https://adityapahansu.000webhostapp.com/panel/uploads/images/products/tiny/"+jObj.getString("image2"),
                    "https://adityapahansu.000webhostapp.com/panel/uploads/images/products/tiny/"+jObj.getString("image3"),
                    "https://adityapahansu.000webhostapp.com/panel/uploads/images/products/tiny/"+jObj.getString("image4"),
                    "https://adityapahansu.000webhostapp.com/panel/uploads/images/products/tiny/"+jObj.getString("image5")};
                    productImageAdapter = new ProductImageAdapter(getApplicationContext(),R.layout.image_view,s);
                    vp.setAdapter(productImageAdapter);
                    productLoadingBar.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Failed "+productId,Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                param.put("id",productId);
                return  param;
            }
        };
        sReq.setTag(INTERNET);
        reqQ.add(sReq);
    }

    private void addToCart(){
       StringRequest sReq = new StringRequest(StringRequest.Method.POST, carturl, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               Toast.makeText(getApplicationContext(),"Product Added"+response,Toast.LENGTH_LONG).show();
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(getApplicationContext(),"Product Adding to cart Failed",Toast.LENGTH_LONG).show();
           }
       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> param = new HashMap<String,String>();
               String session = getSharedPreferences("userinfo", Context.MODE_PRIVATE).getString("session",null);
               String uid =  getSharedPreferences("userinfo", Context.MODE_PRIVATE).getString("userid",null);
               param.put("pid",productId);
               param.put("sid",session);
               param.put("qty","1");
               if (uid!=null)
               param.put("uid",uid);


               return param;
           }
       };
       sReq.setTag(INTERNET);
       reqQ.add(sReq);
    }

}
