package group14.tutoru;

/**
 * Created by Sam on 5/17/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class availabilityFragment extends Fragment {
    View mView;
    public availabilityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.availability_fragment, container, false);
        return inflater.inflate(R.layout.availability_fragment, container, false);
    }
    public void gone(){
        this.mView.setVisibility(View.GONE);
    }
}