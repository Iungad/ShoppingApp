package cf.aditya.shoppingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import cf.aditya.shoppingapp.adapter.CartListAdapter;
import cf.aditya.shoppingapp.data.CartItem;

public class CartActivity extends AppCompatActivity {
    private ListView cartlist;
    private RequestQueue reqQ;
    private TextView carttotal;
    private Button checkoutbutton;
    private ProgressBar cart_progress;
    private String getcartitem = "https://adityapahansu.000webhostapp.com/app/getcart.php";
    private ArrayList<CartItem> cartitems;
    private String deletefromcart = "https://adityapahansu.000webhostapp.com/app/deletefromcart.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        carttotal = (TextView)findViewById(R.id.cart_total);
        checkoutbutton = (Button) findViewById(R.id.checkout_button);
        cart_progress = (ProgressBar)findViewById(R.id.cart_progress_bar);
        cart_progress.setVisibility(View.VISIBLE);

        cartlist =(ListView)findViewById(R.id.cart_list);
        reqQ = Volley.newRequestQueue(getApplicationContext());
        cartitems = new ArrayList<CartItem>();
        GetCart();
        checkoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("userinfo",Context.MODE_PRIVATE);
                String userid = sp.getString("userid",null);

                if (cartlist.getCount()<=0) {
                    Snackbar.make(view, "Empty Cart !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if (userid==null)
                {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
                else if (userid!=null)
                {
                    startActivity(new Intent(getApplicationContext(),AddressActivity.class));
                }
            }
        });
    }

    private void GetCart(){
        StringRequest sReq = new StringRequest(StringRequest.Method.POST, getcartitem, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);

                        CartItem ci = new CartItem();
                        ci.setId(jObj.getInt("cartid"));
                        ci.setName(jObj.getString("name"));
                        ci.setProductId(jObj.getInt("id"));
                        ci.setQuantity(jObj.getInt("quantity"));
                        ci.setDiscountedprice(jObj.getDouble("price")-((jObj.getDouble("price")*jObj.getInt("discount"))/100));
                        ci.setImage(jObj.getString("image1"));

                        cartitems.add(ci);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                CartListAdapter cla = new CartListAdapter(getApplicationContext(),R.layout.cart_list_layout,cartitems,CartActivity.this);
                cartlist.setAdapter(cla);

                TotalPrice();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                SharedPreferences sp = getSharedPreferences("userinfo",Context.MODE_PRIVATE);
                String session = sp.getString("session",null);
                String userid = sp.getString("userid",null);

                if (session!=null)
                    param.put("sid",session);
                if (userid!=null)
                    param.put("uid",userid);


                return param;
            }
        };

        reqQ.add(sReq);
    }

    private void TotalPrice() {
        double price = 0;
        Iterator<CartItem> it = cartitems.iterator();
        while(it.hasNext())
        {
            CartItem cartDatas = it.next();
            price += cartDatas.getDiscountedprice()*cartDatas.getQuantity();
        }
        carttotal.setText("Total : ₹"+String.valueOf(price));
        cart_progress.setVisibility(View.INVISIBLE);
    }

    public void deleteItemCart(final int cartid)
    {
        cart_progress.setVisibility(View.VISIBLE);
        StringRequest sReq = new StringRequest(StringRequest.Method.POST, deletefromcart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cartitems.clear();
                GetCart();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Deleteion failed",Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                String value =String.valueOf(cartid);
                param.put("cartid",value);

                return param;
            }
        };

        reqQ.add(sReq);
    }
}
