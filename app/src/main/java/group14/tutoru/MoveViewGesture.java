package group14.tutoru;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Sam on 5/24/2016.
 */

//Unfortunately there is no way to generically use LayoutParams for any view in a simple manner
//That means for this class, a framelayout must be the parent layout
public class MoveViewGesture extends AppCompatActivity implements GestureDetector.OnGestureListener{
    private View view, wholeView;
    private Context context;
    private int prevX,prevY, returnX, returnY, id, temp, endValueX=1500;
    private float viewRotation;
    private boolean swiped, undo;
    //private RelativeLayout.LayoutParams par;
    private ViewGroup.MarginLayoutParams par;
    private float SWIPE_MAX_OFF_PATH=500;
    private float SWIPE_MIN_DISTANCE=20;
    private float SWIPE_THRESHOLD_VELOCITY=10;

    public MoveViewGesture(View view, Context context){
        this.view=view;
        try {
            //par = (FrameLayout.LayoutParams) view.getLayoutParams();
            //par = (RelativeLayout.LayoutParams) view.getLayoutParams();
            par = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        } catch(ClassCastException e){
            //FrameLayout temp = new FrameLayout(context);
            //parent.addView(temp);
            //remove view
            e.printStackTrace();
        }
        this.context=context;
    }
    @Override
    public void onShowPress(MotionEvent e) {
        //Toast.makeText(getApplicationContext(), "Show Press gesture", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //Go to profile
        //Log.e("Just tap","tap");
        Intent profile = new Intent(context, otherProfile.class);
        profile.putExtra("id", Integer.toString(id));
        context.startActivity(profile);
        return true;
    }
    @Override
    public void onLongPress(MotionEvent event) {

    }
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        //Should be the same as OnUp
        //snapBack();
        //private float SWIPE_MAX_OFF_PATH=100;
        //private float SWIPE_MIN_DISTANCE=20;
        //private float SWIPE_THRESHOLD_VELOCITY=10;
        Log.e(Float.toString(velocityX), Float.toString(velocityY));
        Log.e("Fling", Float.toString(Math.abs(event1.getX() - event2.getX())));
        Log.e(Float.toString(event1.getX()),Float.toString(event2.getX()));
        //any swipe
        if (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            exit((int)velocityX);
            return true;
        }
        return false;
    }
    @Override
    public boolean onDown(MotionEvent event) {
        view.clearAnimation();
        prevX=(int)event.getRawX();
        prevY=(int)event.getRawY();
        par.bottomMargin=-2*view.getHeight();
        par.rightMargin=-2*view.getWidth();
        view.setLayoutParams(par);

        viewRotation = view.getRotation();
        return true;
        //return true;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent event, float distanceX, float distanceY)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_MOVE: {
                par.topMargin+=(int)event.getRawY()-prevY;
                prevY=(int)event.getRawY();
                par.leftMargin+=(int)event.getRawX()-prevX;
                prevX=(int)event.getRawX();
                view.setLayoutParams(par);

                //Log.e("rotationValue",Float.toString((float) (viewRotation + newFingerRotation - fingerRotation)));
                //view.setRotation((float) (viewRotation + newFingerRotation - fingerRotation));
                Log.e(Float.toString(par.leftMargin),Float.toString(par.leftMargin*(45f/700f)));
                view.setRotationY((viewRotation+(par.leftMargin)*(25f/700f)));
                view.setRotation((par.leftMargin * (3f / 700f)));
                view.setAlpha(1/(1+Math.abs(par.leftMargin*(1f/700f))));
                setColor();
                //view.setRotationY(tempRotation);
                return true;
            }
            case MotionEvent.ACTION_UP: {
                Log.d("action","up");
                par.topMargin+=(int)event.getRawY()-prevY;
                par.leftMargin+=(int)event.getRawX()-prevX;
                view.setLayoutParams(par);
            }
        }
        return false;
        /*
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams))
        {
            return false;
        }

        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        marginLayoutParams.leftMargin = (int)((double)marginLayoutParams.leftMargin - distanceX);
        marginLayoutParams.topMargin =  (int)((double)marginLayoutParams.topMargin - distanceY);

        view.requestLayout();

        return true;
        */
        //return true;
    }
    public void action(){
        //final ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if(Math.abs(par.leftMargin)>500){
            exit(par.leftMargin);
        }
        else{
            snapBack();
        }
    }
    public void snapBack ()
    {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            //final ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

            final int startValueX = par.leftMargin;
            final int startValueY = par.topMargin;
            final int endValueX = this.returnX;
            final int endValueY = this.returnY;
            //if((startValueX-endValueX)>snapDiff){
            view.clearAnimation();
            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    int leftMarginInterpolatedValue = (int) (startValueX + (endValueX - startValueX) * interpolatedTime);
                    par.leftMargin = leftMarginInterpolatedValue;

                    int topMarginInterpolatedValue = (int) (startValueY + (endValueY - startValueY) * interpolatedTime);
                    par.topMargin = topMarginInterpolatedValue;

                    view.requestLayout();
                }
            };
            //int[] temp = new int[2];
            //view.getLocationOnScreen(temp);
            //Animation rotateAnimation = new RotateAnimation(tempRotation, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(view, "rotation", view.getRotationY(), 0);
            //Animation rotateAnimation = new RotateAnimation(tempRotation, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //AnimationSet animationSet = new AnimationSet(true);
            //animationSet.setDuration(200);
            //animationSet.setInterpolator(new DecelerateInterpolator());
            //animationSet.addAnimation(animation);
            //animationSet.addAnimation(rotateAnimation);

//            animationSet.setFillAfter(true);
//            animation.setFillBefore(false);
//            animationSet.setFillEnabled(true);
//            animationSet.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation arg0) {
//                    view.setRotation(tempRotation);
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation arg0) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animation arg0) {
//                    view.setRotation(0);
//                }
//            });
//            Log.e("currentRotation", Float.toString(view.getRotation()));
            //animationSet.setFillAfter(true);
            //rotateAnimator.setDuration(1000);
            //rotateAnimator.setInterpolator(new DecelerateInterpolator());
            //view.animate().rotation(0).setInterpolator(new DecelerateInterpolator()).setDuration(200);
            //view.startAnimation(animationSet);
            //view.animate().translationX(0).setInterpolator(new DecelerateInterpolator()).setDuration(100).start();
            //view.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).setDuration(100).start();
            //view.animate().translationZ(0).setDuration(100);
            //rotateAnimator.start();
            animation.setDuration(200);
            animation.setInterpolator(new DecelerateInterpolator());
            view.startAnimation(animation);
            //Reset
            view.animate().alpha(100).setInterpolator(new DecelerateInterpolator()).setDuration(200);
            view.animate().rotationY(0).setInterpolator(new DecelerateInterpolator()).setDuration(200);
            view.animate().rotationX(0).setInterpolator(new DecelerateInterpolator()).setDuration(200);
            view.animate().rotation(0).setInterpolator(new DecelerateInterpolator()).setDuration(200);
            //Change to drawables
            //view.setBackgroundColor(0xFFFFFFFF);
        }
    }

    public void exit(int direction){
        //final ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        final int startValueX = par.leftMargin;
        final int startValueY = par.topMargin;
        final int endValueY = 0;
        swiped=true;
        view.clearAnimation();
        if(direction<0){
            temp = endValueX*-1;
        }
        else{
            temp=endValueX;
        }
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int leftMarginInterpolatedValue = (int) (startValueX + (temp - startValueX) * interpolatedTime);
                par.leftMargin = leftMarginInterpolatedValue;

                int topMarginInterpolatedValue = (int) (startValueY + (endValueY - startValueY) * interpolatedTime);
                par.topMargin = topMarginInterpolatedValue;

                view.requestLayout();
            }
        };
        animation.setDuration(200);
        animation.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(animation);
        if(undo) {
            Snackbar.make(this.wholeView, "Tutor card removed", Snackbar.LENGTH_SHORT).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swiped=false;
                    snapBack();
                }
            }).show();
        }
    }

    private void setColor(){
        if(par.leftMargin<0){
            //view.setBackgroundColor(0x0Fff4d4d);
        }
        else{
            //view.setBackgroundColor(0x0F4dff4d);
        }
    }

    //Where the view should return in terms of left margin and top margin
    public void setReturn(int x, int y){
        this.returnX=x;
        this.returnY=y;
    }

    public void setId(int id){
        this.id=id;
    }

    public boolean isSwiped(){
        return swiped;
    }

    public void setUndo(View view){
        this.undo=true;
        this.wholeView=view;
    }

    public void snapBack (final View view)
    {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            //final ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

            final int startValueX = par.leftMargin;
            final int startValueY = par.topMargin;
            final int endValueX = 0;
            final int endValueY = 0;

            view.clearAnimation();

            Animation animation = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t)
                {
                    int leftMarginInterpolatedValue = (int) (startValueX + (endValueX - startValueX) * interpolatedTime);
                    par.leftMargin = leftMarginInterpolatedValue;
                    //par.leftMargin=endValueX;

                    int topMarginInterpolatedValue = (int) (startValueY + (endValueY - startValueY) * interpolatedTime);
                    par.topMargin = topMarginInterpolatedValue;

                    view.requestLayout();
                }
            };
            animation.setDuration(200);
            animation.setInterpolator(new DecelerateInterpolator());
            view.startAnimation(animation);
        }
    }
}
