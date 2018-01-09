package cf.aditya.shoppingapp;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    EditText name,number,email,username,password;
    Button signup_button,tologin;
    ProgressBar signup_progress;
    RequestQueue reqQ;
    private String registerurl ="https://adityapahansu.000webhostapp.com/app/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        reqQ = Volley.newRequestQueue(getApplicationContext());

        name =(EditText)findViewById(R.id.signup_name);
        number =(EditText)findViewById(R.id.signup_number);
        email =(EditText)findViewById(R.id.signup_email);
        username =(EditText)findViewById(R.id.signup_username);
        password =(EditText)findViewById(R.id.signup_password);

        signup_button =(Button)findViewById(R.id.signup_button);
        tologin =(Button)findViewById(R.id.goto_login);

        signup_progress =(ProgressBar)findViewById(R.id.signup_progress_bar);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.length()==0)
                {
                   Snackbar.make(view,"Name Can't Be Empty",Snackbar.LENGTH_LONG).show();

                }else if (!Patterns.PHONE.matcher(number.getText().toString()).matches())
                {
                    Snackbar.make(view,"Invalid Mobile Number",Snackbar.LENGTH_LONG).show();
                }else if (username.length()<=5)
                {
                    Snackbar.make(view,"Username Must Be 5 Character Long!",Snackbar.LENGTH_LONG).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                {
                    Snackbar.make(view,"Invalid Email",Snackbar.LENGTH_LONG).show();
                }else if (password.length()<=6)
                {
                    Snackbar.make(view,"Password Must Be 6 Character Long !",Snackbar.LENGTH_LONG).show();
                }
                else {
                    Register();
                }
            }
        });




        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                SignUpActivity.this.finish();
            }
        });
    }

    private void Register() {
        signup_progress.setVisibility(View.VISIBLE);
        StringRequest sReq = new StringRequest(StringRequest.Method.POST, registerurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean register = jObj.getBoolean("register");

                    signup_progress.setVisibility(View.GONE);
                    if (register) {
                        Toast.makeText(getApplicationContext(), "Sign Up Successful Please Login", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        SignUpActivity.this.finish();
                    } else
                    {
                        Toast.makeText(getApplicationContext(),"Sign Up Failed! Try Again With Different Credentials",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Register();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();

                param.put("name",name.getText().toString());
                param.put("uname",username.getText().toString());
                param.put("upass",password.getText().toString());
                param.put("phone",number.getText().toString());
                param.put("email",email.getText().toString());


                return param;
            }
        };
        reqQ.add(sReq);
    }
}
