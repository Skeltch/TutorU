package group14.tutoru;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sam on 5/22/2016.
 */
public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.TutorViewHolder> {

    public List<TutorInfo> tutorList;

    public TutorAdapter(List<TutorInfo> tutorList){
        this.tutorList = tutorList;
    }

    @Override
    public int getItemCount(){
        return tutorList.size();
    }

    @Override
    public void onBindViewHolder(TutorViewHolder tutorViewHolder, int i){
        TutorInfo ti = tutorList.get(i);
        ti.tutorInfo="Test";
        tutorViewHolder.vTutorInfo.setText(ti.tutorInfo);
        /*
        tutorViewHolder.vName.setText(ti.name);
        tutorViewHolder.vGpa.setText(ti.gpa);
        tutorViewHolder.vGradYear.setText(ti.gradYear);
        tutorViewHolder.vMajor.setText(ti.major);
        */
    }

    @Override
    public TutorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_cards, viewGroup, false);
        return new TutorViewHolder(itemView);
    }

    public static class TutorViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile;
        public TextView vTutorInfo, vName, vGpa, vGradYear, vMajor, vClasses, vDescription, vPrice;

        public TutorViewHolder(View v) {
            super(v);
            vTutorInfo = (TextView) v.findViewById(R.id.tutorInfo);
            /*
            vName =  (TextView) v.findViewById(R.id.name);
            vGpa = (TextView)  v.findViewById(R.id.gpa);
            vGradYear = (TextView)  v.findViewById(R.id.graduation_year);
            vMajor = (TextView) v.findViewById(R.id.major);
            */
        }
    }
}
