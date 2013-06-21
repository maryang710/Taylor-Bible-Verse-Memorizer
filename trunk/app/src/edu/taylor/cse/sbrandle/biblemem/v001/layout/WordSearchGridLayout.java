package edu.taylor.cse.sbrandle.biblemem.v001.layout;
import java.util.ArrayList;

import edu.taylor.cse.sbrandle.biblemem.v001.global.WordSearchSharedClass;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.TextView;

public class WordSearchGridLayout extends GridView implements  OnTouchListener{

	//private Listener mListener;

	private Canvas gridCanvas;
	private Paint paint1, paint2;
	private float startX =0, startY =0, stopX=0, stopY=0;
	private View myView;
	private ArrayList<MyLine> lines; 
	private StringBuilder answer;
	private ArrayList<TextView> strHelper;
	private String question;
	private WordSearchSharedClass sharedInfo;
	//public Context pcontext;

	public WordSearchGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(this.isInEditMode())
			return;
		setOnTouchListener(this);
		myView = this;

		paint1 = new Paint();
		paint1.setAntiAlias(true);
		paint1.setDither(true);
		paint1.setColor(Color.argb(248, 255, 255, 255));
		paint1.setStrokeWidth(20f);
		paint1.setStyle(Paint.Style.STROKE);
		paint1.setStrokeJoin(Paint.Join.ROUND);
		paint1.setStrokeCap(Paint.Cap.ROUND);

		paint2 = new Paint();
		paint2.set(paint1);
		paint2.setColor(Color.argb(235, 74, 138, 255));
		paint2.setStrokeWidth(30f);
		paint2.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));

		lines =  new ArrayList<MyLine>();
		answer = new StringBuilder();
		strHelper = new ArrayList<TextView>();
		question ="";
		sharedInfo = new WordSearchSharedClass(context);
		//pcontext = context;
	}



	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(this.isInEditMode()) 
			return;
		gridCanvas = canvas;
		gridCanvas.drawLine(startX, startY, stopX, stopY, paint2);
		if(lines.size() > 0)
			for(int i =0; i < lines.size(); i++)
				gridCanvas.drawLine(lines.get(i).startX, lines.get(i).startY, lines.get(i).stopX,  lines.get(i).stopY, paint2);
	}



	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		Rect rect; 
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			strHelper = new ArrayList<TextView>();
			answer = new StringBuilder();
			startX =event.getX();
			startY =event.getY();
			try {
				buildAnswerString(startX, startY);
			}catch(NullPointerException e ){
				//Log.i("NULLLING", "out of gridview");
			} 
			break;

		case MotionEvent.ACTION_MOVE:
			rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
			stopX = event.getX();
			stopY = event.getY();
			if(rect.contains((int)event.getX(), (int)event.getY())){
				//gridCanvas.drawLine(startX, startY, event.getX(),  event.getY(), paint);
				try {
					hilightItemOnDrawLine(stopX,stopY);
					buildAnswerString(stopX,stopY);
				}catch(NullPointerException e ){
					//Log.i("NULLLING", "out of gridview");
				} 
			}
			v.invalidate();
			break;

		case MotionEvent.ACTION_UP:
			rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
			stopX = event.getX();
			stopY = event.getY();
			if(rect.contains((int)event.getX(), (int)event.getY())){
				//gridCanvas.drawLine(startX, startY, event.getX(),  event.getY(), paint);
				try {
					lines.add(new MyLine(startX, startY, stopX, stopY));
					try {
						gridCanvas.drawLine(startX, startY, stopX, stopY, paint2);
						buildAnswerString(stopX,stopY);
						evaluateAnswer();
						v.invalidate();
					}catch(NullPointerException e ){
						//Log.i("NULLLING", "out of gridview");
					} 
				}catch(NullPointerException e ){
					//Log.i("NULLLING", "out of gridview");
				}
			}else{
				evaluateAnswer();
			}
			break;

		case MotionEvent.ACTION_CANCEL:
			break;
		default:
			break;
		}

		return true;
	}

	public String getAnswer(){
		return answer.toString();
	}

	public void setQuestion(String q){
		question = q;
	}

	public void evaluateAnswer(){

		Log.i("EVALUATION", sharedInfo.getCurrentlySelectecWord());
		Log.i("EVALUATION", answer.toString());
		if(sharedInfo.getCurrentlySelectecWord().equals(answer.toString()))
			Log.i("EVALUATION", "DING DING DING");
	}

	public void hilightItemOnDrawLine(float currentXPosition,float currentYPosition) throws NullPointerException {
		int position = this.pointToPosition((int) currentXPosition, (int) currentYPosition); 
		TextView tv = (TextView) this.getChildAt(position);
		View v =  (View)this.getChildAt(position);

		tv.setTextColor( Color.CYAN);
		tv.setShadowLayer((float)0.5, 0, 0, Color.RED);
		//tv.setAlpha((float) 0.6);
		tv.invalidate();
	}

	public void buildAnswerString(float currentXPosition,float currentYPosition) throws NullPointerException {
		int position = this.pointToPosition((int) currentXPosition, (int) currentYPosition); 
		TextView tv = (TextView) this.getChildAt(position);
		if (!(strHelper.contains(tv))){
			strHelper.add(tv);
			answer.append(tv.getText());
		}

		//Log.i("the answer is> ", answer.toString());
	}



	public class MyLine{
		float startX =0, startY =0, stopX=0, stopY=0;
		public MyLine(float sx, float sy, float stx, float sty){

			startX =sx;
			startY =sy;
			stopX=stx;
			stopY= sty;
		}

		public float getStartX(){
			return startX;
		}
		public float getStartY(){

			return startY; 
		}
		public float  getStopX(){
			return stopX;
		}
		public float  getStopY(){
			return stopY;
		}
	}
}