package group14.tutoru;

/**
 * Created by Sam on 3/15/2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

//Class responsible for communicating with the server.
// The current method is very insecure however generic to allow for easy communication
//I will probably need to change this later
//This class takes a hashmap with the first value as the variable used in php, ex: username
//And the second value as the actual value, ex: jSmith123
public class PostResponseAsyncTask extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;

    private AsyncResponse delegate;
    private Context context;
    private HashMap<String, String> postData =
            new HashMap<String, String>();
    private String loadingMessage = "Loading...";
    private boolean pause;
    private boolean type;


    //Constructors
    //This allows multiple ways to communicate generically
    public PostResponseAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
        this.context = (Context)delegate;

        this.pause = true;
        this.type = true;
    }

    public PostResponseAsyncTask(AsyncResponse delegate,
                                 HashMap<String, String> postData){

        this.delegate = delegate;
        this.context = (Context)delegate;
        this.postData = postData;

        this.pause = true;
        this.type = true;
    }

    public PostResponseAsyncTask(AsyncResponse delegate, String loadingMessage){
        this.delegate = delegate;
        this.context = (Context)delegate;
        this.loadingMessage = loadingMessage;

        this.pause = true;
        this.type = true;
    }

    public PostResponseAsyncTask(AsyncResponse delegate,
                                 HashMap<String, String> postData, String loadingMessage){

        this.delegate = delegate;
        this.context = (Context)delegate;
        this.postData = postData;
        this.loadingMessage = loadingMessage;

        this.pause = true;
        this.type = true;
    }
    //End Constructors

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        if(pause) {
            progressDialog.setMessage(loadingMessage);
            progressDialog.show();
        }
        super.onPreExecute();
    }//onPreExecute

    @Override
    protected String doInBackground(String... urls){

        if(type) {
            String result = "";
            //Change this later for multiple urls
            for (int i = 0; i <= 0; i++) {

                result = invokePost(urls[i], postData);
            }

            return result;
        }
        else{
            InputStream is = null;
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int response = conn.getResponseCode();
                Log.d("DEBUG_TAG", "The response is: " + response);
                is = conn.getInputStream();

                //Length 500
                String contentAsString = readIt(is);
                return contentAsString;
            } catch(Exception e){
                e.printStackTrace();
                return null;
            }
            finally{
                if(is!=null){
                    try {
                        is.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }//doInBackground

    public String readIt(InputStream stream){
        try {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[500];
            reader.read(buffer);
            return new String(buffer);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String invokePost(String requestURL, HashMap<String,
            String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";
                //Look for this to see what's wrong
                Log.i("PostResponseAsyncTask", responseCode + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }//invokePost

    private String getPostDataString(HashMap<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }//getPostDataString

    @Override
    protected void onPostExecute(String result) {
        Log.d("RESULT******", result);
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        result = result.trim();

        delegate.processFinish(result);
    }//onPostExecute

    //Setter and Getter
    public String getLoadingMessage() {
        return loadingMessage;
    }

    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
    }

    public HashMap<String, String> getPostData() {
        return postData;
    }

    public void setPostData(HashMap<String, String> postData) {
        this.postData = postData;
    }

    public Context getContext() {
        return context;
    }

    public AsyncResponse getDelegate() {
        return delegate;
    }

    public void useLoad(boolean pause){ this.pause = pause;}

    public void GET(boolean type){ this.type = false;}

    public void POST(boolean type){ this.type = true;}

    //End Setter & Getter
}