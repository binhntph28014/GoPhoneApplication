package binhntph28014.fpoly.gophoneapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatAdminActivity extends AppCompatActivity {

    private EditText messageInput;
    private Button sendButton;
    private ListView messageListView;
    private ArrayAdapter<String> messageAdapter;
    private ArrayList<String> messageList;
    private RequestQueue requestQueue;
    private String username;
    private Socket mSocket;
    private TextView usernameHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatadmin);

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");

        // Khởi tạo các view
        usernameHeader = findViewById(R.id.username_header);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        messageListView = findViewById(R.id.message_list_view);

        // Hiển thị tên người dùng
        usernameHeader.setText(username);

        // Khởi tạo adapter và request queue
        messageList = new ArrayList<>();
        messageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messageList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                String message = getItem(position);
                if (convertView == null) {
                    int layout = message.startsWith(username + ":") ? R.layout.item_message_user : R.layout.item_message_admin;
                    convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
                }
                TextView messageTextView = convertView.findViewById(R.id.message_text);
                String messageContent = message.split(": ", 2)[1];
                messageTextView.setText(messageContent);
                return convertView;
            }
        };
        messageListView.setAdapter(messageAdapter);

        requestQueue = Volley.newRequestQueue(this);

        try {
            mSocket = IO.socket("http://192.168.0.105:5000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.on("newTinnhan", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String nguoidung;
                        String tinnhan;
                        String recipient;
                        try {
                            nguoidung = data.getString("nguoidung");
                            tinnhan = data.getString("tinnhan");
                            recipient = data.getString("recipient");
                            if (recipient.equals(username) || nguoidung.equals(username)) {
                                String formattedMessage = nguoidung + ": " + tinnhan;
                                if (!messageList.contains(formattedMessage)) {
                                    messageList.add(formattedMessage);
                                    messageAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        mSocket.connect();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButton.setEnabled(false); // Disable the button
                sendMessage();
            }
        });

        fetchChatHistory();
    }

    private void sendMessage() {
        String tinnhan = messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(tinnhan)) {
            Toast.makeText(this, "Tinnhan cannot be empty", Toast.LENGTH_SHORT).show();
            sendButton.setEnabled(true); // Re-enable button
            return;
        }

        String url = "http://192.168.0.105:5000/api/tinnhans";
        JSONObject tinnhanJson = new JSONObject();
        try {
            tinnhanJson.put("nguoidung", username);
            tinnhanJson.put("tinnhan", tinnhan);
            tinnhanJson.put("recipient", "Admin"); // Gửi tinnhan đến Admin
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, tinnhanJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        messageInput.setText("");
                        sendButton.setEnabled(true); // Re-enable button
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ChatAdminActivity.this, "Error sending tinnhan: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        sendButton.setEnabled(true); // Re-enable button
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void fetchChatHistory() {
        String url = "http://192.168.0.105:5000/api/tinnhans/" + username;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        messageList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject tinnhan = response.getJSONObject(i);
                                String nguoidung = tinnhan.getString("nguoidung");
                                String msg = tinnhan.getString("tinnhan");
                                String formattedMessage = nguoidung + ": " + msg;
                                messageList.add(formattedMessage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        messageAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ChatAdminActivity.this, "Error fetching chat history: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off("newTinnhan");
        }
    }
}
//nhuong