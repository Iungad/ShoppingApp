package cf.aditya.shoppingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    TextView error_message;
    Button login,tosignup;
    ProgressBar login_progress;
    RequestQueue reqQ;
    private String loginurl ="https://adityapahansu.000webhostapp.com/app/login.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);


        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        error_message =(TextView)findViewById(R.id.login_error);
        login_progress =(ProgressBar)findViewById(R.id.login_progress_bar);

        login =(Button)findViewById(R.id.login_button);
        tosignup =(Button)findViewById(R.id.goto_sign_up);
        reqQ = Volley.newRequestQueue(getApplicationContext());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().length()==0)
                {
                    error_message.setText("Username Empty");
                }
                else if (password.getText().toString().length()==0)
                {
                    error_message.setText("Password Empty");
                }
                else
                {
                    login_progress.setVisibility(View.VISIBLE);
                    StringRequest sReq = new StringRequest(StringRequest.Method.POST, loginurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jObj = new JSONArray(response).getJSONObject(0);
                                Boolean success = jObj.getBoolean("success");
                                if (success)
                                {
                                    SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();

                                    editor.putString("userid",jObj.getString("id"));
                                    editor.putString("uname",jObj.getString("uname"));

                                    editor.apply();
                                    login_progress.setVisibility(View.GONE);
                                    LoginActivity.this.finish();
                                }
                                else
                                {
                                    error_message.setText("Wrong Username/Password");
                                    password.setText("");
                                    username.setText("");
                                    login_progress.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            login_progress.setVisibility(View.GONE);
                            error_message.setText("Can't Connect To Server");
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> param  = new HashMap<String, String>();
                            param.put("uname",username.getText().toString());
                            param.put("upass",password.getText().toString());

                            return  param;
                        }
                    };

                    reqQ.add(sReq);
                }
            }
        });

        tosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
                LoginActivity.this.finish();
            }
        });

    }

}
