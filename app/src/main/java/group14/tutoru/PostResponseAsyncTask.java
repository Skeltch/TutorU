package group14.tutoru;

/**
 * Created by Sam on 3/15/2016.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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

// The current method is somewhat insecure however generic to allow for easy communication
//This class takes a hashmap with the first value as the variable used in php, ex: username
//And the second value as the actual value, ex: jSmith123
//Basis for code from KosalGeek
//https://github.com/kosalgeek/generic_asynctask_v2

//Side note: some activites may not require a reload feature
//Maybe add ability to remove retry
public class PostResponseAsyncTask extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private AsyncResponse delegate;
    public Context context;
    private HashMap<String, String> postData =
            new HashMap<>();
    private String loadingMessage = "Loading...";

    //Online vs local server
    public  String ip="http://107.170.52.23/";
    //public String ip = "http://192.168.1.7/app/";
    private boolean pause;
    private int type;
    public int len;
    private Bitmap bitmap;


    //Used for uploading files via content buffer
    /*
    String attachmentName = "bitmap";
    String attachmentFileName = "bitmap.bmp";
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";
    */



    //Constructors
    //This allows multiple ways to communicate generically
    public PostResponseAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
        this.context = (Context)delegate;

        this.pause = true;
        this.type = 0;
        len=500;
    }

    public PostResponseAsyncTask(AsyncResponse delegate,
                                 HashMap<String, String> postData){

        this.delegate = delegate;
        this.context = (Context)delegate;
        this.postData = postData;

        this.pause = true;
        this.type = 0;
        len=500;
    }

    public PostResponseAsyncTask(AsyncResponse delegate, String loadingMessage){
        this.delegate = delegate;
        this.context = (Context)delegate;
        this.loadingMessage = loadingMessage;

        this.pause = true;
        this.type = 0;
        len=500;
    }

    public PostResponseAsyncTask(AsyncResponse delegate,
                                 HashMap<String, String> postData, String loadingMessage){

        this.delegate = delegate;
        this.context = (Context)delegate;
        this.postData = postData;
        this.loadingMessage = loadingMessage;

        this.pause = true;
        this.type = 0;
        len=500;
    }

    public PostResponseAsyncTask(AsyncResponse delegate, Bitmap bitmap){
        this.delegate = delegate;
        this.context = (Context)delegate;
        this.bitmap = bitmap;

        this.pause = false;
        this.type = 2;
    }
    //End Constructors

    @Override
    protected void onPreExecute() {
        //Check network connectivity before doing anything
        ConnectivityManager connectivityManager
                //activity.getIntent();
                //activity.recreate();
                //Attempt at general retry button
            /*
            View view = activity.getLayoutInflater().inflate(R.layout.retry, null);
            Button retryButton = (Button) view.findViewById(R.id.retryButton);
            retryButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    activity.recreate();
                }
            });
            */           = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if(!isConnected){
            final Activity activity = (Activity) this.context;

            //This creates an alert dialog that will retry to load the page on poor or no network
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle("Network Unavailable");
            builder.setMessage("Sorry there was an error getting data from the Internet.");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    activity.recreate();
                }
            }).setNegativeButton("Cancel",null);
            AlertDialog dialog = builder.create();
            dialog.show();
            Toast.makeText(this.context.getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
            //Cancel async task
            this.cancel(true);
        }
        else {
            progressDialog = new ProgressDialog(context);
            if (pause) {
                progressDialog.setMessage(loadingMessage);
                progressDialog.show();
            }
            super.onPreExecute();
        }
    }//onPreExecute

    @Override
    protected String doInBackground(String... urls){

        if(type==0) {
            String result = "";
            //This can be changed later for multiple urls
            for (int i = 0; i <= 0; i++) {
                urls[i]=ip+urls[i];
                result = invokePost(urls[i], postData);
            }

            return result;
        }
        //Not really needed
        else if(type==1){
            InputStream istream = null;
            try{
                urls[0]=ip+urls[0];
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int response = conn.getResponseCode();
                Log.d("DEBUG_TAG", "The response is: " + response);
                istream = conn.getInputStream();

                Reader reader = new InputStreamReader(istream, "UTF-8");
                char[] buffer = new char[len];
                reader.read(buffer);
                return new String(buffer);
            } catch(Exception e){
                e.printStackTrace();
                return null;
            }
            finally{
                if(istream!=null){
                    try {
                        istream.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        //File upload/Image upload
        else{
            try {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                int bytes = bitmap.getByteCount();
                Log.e("bitmap bytes",Integer.toString(bytes));
                byte[] byte_arr = baos.toByteArray();
                String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                SharedPreferences settings = context.getSharedPreferences("Userinfo", 0);
                String id = settings.getString("id","");
                postData.put("id",id);
                postData.put("image", image_str);
                urls[0]=ip+urls[0];
                return invokePost(urls[0], postData);

                //Unused content wrapper
                /*
                HttpURLConnection httpUrlConnection;
                URL url = new URL(ip+urls[0]);
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setUseCaches(false);
                httpUrlConnection.setDoOutput(true);

                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
                httpUrlConnection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + this.boundary);

                DataOutputStream request = new DataOutputStream(
                        httpUrlConnection.getOutputStream());

                request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        this.attachmentName + "\";filename=\"" +
                        this.attachmentFileName + "\"" + this.crlf);
                request.writeBytes(this.crlf);



//                byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
//                for (int i = 0; i < bitmap.getWidth(); ++i) {
//                    for (int j = 0; j < bitmap.getHeight(); ++j) {
//                        //we're interested only in the MSB of the first byte,
//                        //since the other 3 bytes are identical for B&W images
//                        pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80));
//                    }
//                }


                //ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.JPEG ,100,baos);

                int bytes = bitmap.getByteCount();
                Log.e("bitmap bytes",Integer.toString(bytes));
                ByteBuffer buffer = ByteBuffer.allocate(bytes);
                bitmap.copyPixelsToBuffer(buffer);
                byte[] pixels = buffer.array();

                request.write(pixels);
                request.writeBytes(this.crlf);
                request.writeBytes(this.twoHyphens + this.boundary +
                        this.twoHyphens + this.crlf);
                request.flush();
                request.close();

                //Response
                InputStream responseStream = new
                        BufferedInputStream(httpUrlConnection.getInputStream());

                BufferedReader responseStreamReader =
                        new BufferedReader(new InputStreamReader(responseStream));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                responseStreamReader.close();

                String response = stringBuilder.toString();
                responseStream.close();
                httpUrlConnection.disconnect();
                Log.e("Response", response);
                return response;

                /*
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                String attachmentName="bitmap";
                String attachmentFileName = "bitmap.bmp";

                // open a URL connection to the Servlet
                URL url = new URL(ip+urls[0]);

                // Open a HTTP  connection to  the URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Cache-Control","no-cache");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                //Content wrapper
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                //Change uploaded_file to other
                dos.writeBytes("Content-Disposition: form-data; name=\""+ attachmentName + "\";filename=\""
                        + attachmentFileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                //Convert Bitmap to ByteBuffer for file upload
                byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
                for (int i = 0; i < bitmap.getWidth(); ++i) {
                    for (int j = 0; j < bitmap.getHeight(); ++j) {
                        //we're interested only in the MSB of the first byte,
                        //since the other 3 bytes are identical for B&W images
                        //pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80) >> 7);
                        pixels[i+j] = (byte) ((bitmap.getPixel(i,j) & 0x80));
                    }
                }
                dos.write(pixels);
                // end content wrapper
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                dos.flush();
                dos.close();


                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){
                    Log.e("responseCode","200");
                }

                //Response
                InputStream responseStream = new
                        BufferedInputStream(conn.getInputStream());

                BufferedReader responseStreamReader =
                        new BufferedReader(new InputStreamReader(responseStream));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                responseStreamReader.close();

                String response = stringBuilder.toString();

                Log.e("File response", response);

                responseStream.close();
                conn.disconnect();
                return response;
                */
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }//doInBackground

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
            final int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                //Can be used for large amounts of data as well
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

                final Activity activity = (Activity) this.context;
                final Context temp = this.context;
                //UI changes have to made on a different thread
                new Thread() {
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //This creates an alert dialog that will retry to load the page on poor or no network
                                AlertDialog.Builder builder = new AlertDialog.Builder(temp);
                                builder.setTitle("Network Error");
                                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        activity.recreate();
                                    }
                                }).setNegativeButton("Cancel", null);
                                builder.setMessage("Sorry there was an error contacting our server.");
                                if (responseCode == 500) {
                                    //This should never happen
                                    builder.setMessage("There was an error on our part, sorry! We'll fix this as soon as possible.");
                                }
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                //Needed?
                                //this.cancel(true);
                            }

                        });
                    }
                }.start();
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

            for (Map.Entry<String, String> entry : params.entrySet()) {
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
        //Debugging and general usage
        Log.d("RESULT", result);
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

    public void POST(){ this.type = 0;}

    public void GET(){ this.type = 1;}

    public void FILE(){
        this.type=2;
    }
    //End Setter & Getter
}