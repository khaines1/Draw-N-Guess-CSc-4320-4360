package com.example.drawnguess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PaintView extends View {

    private Path path = new Path(); //line path
    private Paint pen = new Paint(); //holds style and color information

    public PaintView(Context context) {

        super(context);

        //pen settings
        pen.setAntiAlias(true); //smooth line
        pen.setColor(Color.BLACK); //default color is black
        pen.setStrokeWidth(8f);
        pen.setStyle(Paint.Style.STROKE); //make strokes
        pen.setStrokeJoin(Paint.Join.ROUND);//make stroke meeting round for smooth curves

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { //tracks touchscreen motion
        //get coordinates x and y
        float x = event.getX();
        float y = event.getY();

        //find action performed and execute appropriate operation
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: //screen touched
                path.moveTo(x,y); //jump to new location
                return true;
            case MotionEvent.ACTION_MOVE: //motion detected
                path.lineTo(x,y); //line to new location - track movement
                break;
            default:
                return false;
        }
        postInvalidate(); //invalidate from non-UI thread, force view to draw
        return false;
        //return super.onTouchEvent(event);
    }

    //draw path on canvas
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, pen);
    }

    public void clear() {
        //reset the path
        path.reset();
        //invalidate the view
        postInvalidate();
    }

}







// reference: https://www.codercrunch.com/post/1420391976/build-paint-app-in-android
