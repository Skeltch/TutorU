package group14.tutoru;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sam on 5/16/2016.
 */
public class reviewFragment extends Fragment {
    int id;
    NestedScrollView layout;
    public reviewFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        id = getArguments() != null ? getArguments().getInt("num") : 1;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        layout = (NestedScrollView)inflater.inflate(R.layout.review_fragment, container, false);
        return layout;
    }
    public void gone(){
        getView().setVisibility(View.GONE);
    }
    public void setTutee(){
        //getView().setVisibility(View.GONE);
    }
    public int id(){
        return ((ViewGroup)getView().getParent()).getId();
        //return id;
    }
}
