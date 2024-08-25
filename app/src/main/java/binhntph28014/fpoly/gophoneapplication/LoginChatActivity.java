package binhntph28014.fpoly.gophoneapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;

public class LoginChatActivity extends AppCompatActivity {

    private EditText usernameInput;
    private Button startChatButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginchat);

        usernameInput = findViewById(R.id.username_input);
        startChatButton = findViewById(R.id.start_chat_button);


        requestQueue = Volley.newRequestQueue(this);

        startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginChatActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    saveUsername(username);
                }
            }
        });
    }

    private void saveUsername(final String username) {
        String url = "http://192.168.0.105:5000/api/nguoidungs";
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, userJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Sau khi người dùng được lưu vào cơ sở dữ liệu, phát sự kiện qua Socket.IO
                        try {
                            IO.socket("http://192.168.0.105:5000").emit("newNguoidung", response);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        // Chuyển đến ChatActivity
                        Intent intent = new Intent(LoginChatActivity.this, ChatAdminActivity.class);
                        intent.putExtra("USERNAME", username);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(LoginChatActivity.this, "Error saving username: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

}
//nhuong