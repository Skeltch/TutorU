package group14.tutoru;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;

//Testing crop
//Created by Samuel Cheung
public class test extends AppCompatActivity implements View.OnTouchListener {


        ImageView img;
        FrameLayout flCrop;

        int framWidth = 0;
        int framHeight = 0;

        int imageHeight ;
        int imageWidth ;

        int cropImageWidth = 320;
        int cropImageHeight = 263;

        public static int SELECT_PHOTO =0;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_test);
            img = (ImageView) findViewById(R.id.img);
            flCrop = (FrameLayout) findViewById(R.id.flCrop);
            img.setOnTouchListener(this);


        }

        public void btnCrop(View v) {
            ImageView mask = (ImageView) findViewById(R.id.troll_face);
            makeMaskImage(mask, R.drawable.ic_menu_gallery);
        }

        public void btnSelectPic(View v){
            startActivityForResult(new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (resultCode) {
                case RESULT_OK:
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;

                        imageHeight = options.outHeight;
                        imageWidth = options.outWidth;

                        img.setOnTouchListener(test.this);

                        img.setImageBitmap(bitmap);

                        break;
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
            }
        }

        // Method of creating mask runtime
        public void makeMaskImage(ImageView mImageView, int mContent) {

            flCrop.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(flCrop.getDrawingCache());
            flCrop.setDrawingCacheEnabled(false);

            Log.e("TEST", "Frame W : " + framWidth + " H : " + framHeight);
            Log.e("TEST",
                    "Bitmap W : " + bitmap.getWidth() + " H : "
                            + bitmap.getHeight());

            Bitmap result = Bitmap.createBitmap(bitmap, (framWidth / 2)
                    - (cropImageWidth / 2) +1, (framHeight / 2)
                    - (cropImageHeight / 2), cropImageWidth -2, cropImageHeight);
            mImageView.setImageBitmap(result);
            mImageView.setScaleType(ImageView.ScaleType.CENTER);
            mImageView.setBackgroundResource(R.drawable.border);
        }





        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // handle touch events here
            ImageView view = (ImageView) v;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    lastEvent = new float[4];
                    lastEvent[0] = event.getX(0);
                    lastEvent[1] = event.getX(1);
                    lastEvent[2] = event.getY(0);
                    lastEvent[3] = event.getY(1);
                    d = rotation(event);
                    Log.e("MainActivity","MainActivity down d = "+d);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        float dx = event.getX() - start.x;
                        float dy = event.getY() - start.y;
                        Log.e("TEST","Dx : "+ dx+" Dy : "+ dy);
                        matrix.postTranslate(dx, dy);
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = (newDist / oldDist);
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                        if (lastEvent != null && event.getPointerCount() == 3) {
                            newRot = rotation(event);
                            Log.e("MainActivity","MainActivity move d= "+d);
                            float r = newRot - d;
                            float[] values = new float[9];
                            matrix.getValues(values);
                            float tx = values[2];
                            float ty = values[5];
                            float sx = values[0];
                            float xc = (view.getWidth() / 2) * sx;
                            float yc = (view.getHeight() / 2) * sx;
                            matrix.postRotate(r, tx + xc, ty + yc);
                        }
                    }
                    break;
                default:
                    view.setImageMatrix(matrix);
                    return true;
            }
            view.setImageMatrix(matrix);
            return true;
        }

        /**
         * Determine the space between the first two fingers
         */
        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float)Math.sqrt(x * x + y * y);
        }

        /**
         * Calculate the mid point of the first two fingers
         */
        private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }

        /**
         * Calculate the degree to be rotated by.
         *
         * @param event
         * @return Degrees
         */
        private float rotation(MotionEvent event) {
            double delta_x = (event.getX(0) - event.getX(1));
            double delta_y = (event.getY(0) - event.getY(1));
            double radians = Math.atan2(delta_y, delta_x);
            return (float) Math.toDegrees(radians);
        }
        // these matrices will be used to move and zoom image
        private static Matrix matrix = new Matrix();
        private static Matrix savedMatrix = new Matrix();
        // we can be in one of these 3 states
        private static final int NONE = 0;
        private static final int DRAG = 1;
        private static final int ZOOM = 2;
        private int mode = NONE;
        // remember some things for zooming
        private PointF start = new PointF();
        private PointF mid = new PointF();
        private float oldDist = 1f;
        private float d = 0f;
        private float newRot = 0f;
        private float[] lastEvent = null;

        boolean flag ;
    }
