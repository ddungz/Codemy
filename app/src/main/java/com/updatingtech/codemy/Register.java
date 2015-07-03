package com.updatingtech.codemy;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author: ddungz
 * @purpose: send register request to server
 * @param
 * @output: json object
 */
public class Register extends Activity implements View.OnClickListener {

    EditText usernameId, emailId, passwordId;
    Button registerId;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Reference to the views
        usernameId = (EditText) findViewById(R.id.usernameId);
        emailId = (EditText) findViewById(R.id.emailId);
        passwordId = (EditText) findViewById(R.id.passwordId);
        registerId = (Button) findViewById(R.id.registerId);
    }

    /**
     * @purpose do post
     * @param url
     * @param user
     * @return
     */
    public static String POST(String url, User user) {
        InputStream inputStream = null;
        String result = "";

        try {
            // Create Http connection
            HttpClient httpClient = new DefaultHttpClient();

            // Make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // Build jsonObject
            JSONObject jsonObject = new JSONObject();
            /*jsonObject.accumulate("userName", user.getUserName());
            jsonObject.accumulate("email", user.getEmail());
            jsonObject.accumulate("password", user.getPassword());
            */
            jsonObject.accumulate("name", user.getUserName());
            jsonObject.accumulate("country", user.getEmail());
            jsonObject.accumulate("twitter", user.getPassword());

            // Convert JSONObject to JSON string
            json = jsonObject.toString();
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // Set JSON to StringEntity
            StringEntity se = new StringEntity(json);

            // Set httpPost entity
            httpPost.setEntity(se);

            // Set headers to inform server about the type of content
            httpPost.setHeader("Accept", "application/json"); // name - value
            httpPost.setHeader("Content-type", "application/json");

            // Execute POST request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // Receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // Convert inputStream to string
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Error";
            }
        } catch(Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // Return result
        return result;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerId:
                if(!validate())
                    Toast.makeText(getBaseContext(), "Enter your infor", Toast.LENGTH_SHORT).show();
                //new HttpAsyncTask().execute("http://localhost:8080/user/register");
                new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");
                System.out.print("registerBTN clicked");
                Log.i("CLICKED", "register button clicked");
                Toast.makeText(getBaseContext(), "You've just clicked Register btn", Toast.LENGTH_SHORT).show();
            break;
        }
    }

    // AsyncTAsk
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String ... urls) {
            user = new User();
            user.setUserName(usernameId.getText().toString());
            user.setEmail(emailId.getText().toString());
            user.setPassword(passwordId.getText().toString());
            return POST(urls[0], user);
        }

        // onPostExecute displays the results of AsyncTask
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Your info has been submitted", Toast.LENGTH_SHORT).show();
        }
    }

    // Validation of input field
    private boolean validate() {
        if(usernameId.getText().toString().trim().equals(""))
            return false;
        else if(emailId.getText().toString().trim().equals(""))
            return false;
        else if(passwordId.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException, IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        inputStream.close();
        return result;
    }
}
