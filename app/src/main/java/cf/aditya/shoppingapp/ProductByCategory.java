package cf.aditya.shoppingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cf.aditya.shoppingapp.adapter.ProductAdapter;
import cf.aditya.shoppingapp.data.*;
import cf.aditya.shoppingapp.data.Product;

public class ProductByCategory extends AppCompatActivity {
    private String categoryName;
    private String categoryId;
    private String url="https://adityapahansu.000webhostapp.com/app/productbycategory.php";
    RequestQueue requestQueue;
    StringRequest stringRequest;
    private ArrayList<cf.aditya.shoppingapp.data.Product> ProductArray;
    private cf.aditya.shoppingapp.data.Product pc;
    ListView productList;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        categoryName = this.getIntent().getStringExtra("CategoryName");
        categoryId = String.valueOf(getIntent().getIntExtra("CategoryId",0));
        this.setTitle(categoryName);
        ProductArray = new ArrayList<Product>();


        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        productList = (ListView)findViewById(R.id.productList2);


        GetProductByCategory();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void GetProductByCategory(){

        progressBar.setVisibility(View.VISIBLE);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest = new StringRequest(StringRequest.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jArray = new JSONArray(response);

                    for (int i =0;i<jArray.length();i++)
                    {
                        JSONObject jObj = jArray.getJSONObject(i);
                        pc = new cf.aditya.shoppingapp.data.Product();
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
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("cat", categoryId);
                return param;
            }
        };


        requestQueue.add(stringRequest);
    }

}
