package group14.tutoru;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Samuel on 4/4/2016.
 */
//Necessary?
public class profileInfo {

    public JSONObject profileT;
    public JSONObject info;
    public JSONArray classesArray;
    public JSONObject tutorInfo;

    public profileInfo(String output){
        try {
            profileT = new JSONObject(output);
            //Change to string inputs?
            info = profileT.optJSONObject("info");
            classesArray = profileT.optJSONArray("classes");
            tutorInfo = profileT.optJSONObject("tutorInfo");
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}
