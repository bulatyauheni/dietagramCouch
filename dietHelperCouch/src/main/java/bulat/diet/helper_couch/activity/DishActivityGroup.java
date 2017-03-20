package bulat.diet.helper_couch.activity;

import java.util.ArrayList;
import java.util.Stack;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.db.NotificationDishHelper;
import bulat.diet.helper_couch.item.NotificationDish;
import bulat.diet.helper_couch.utils.Constants;
import bulat.diet.helper_couch.utils.SaveUtils;


public class DishActivityGroup extends ActivityGroup implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

	private Stack<String> stack;
	private boolean flag=true;

	private void addWaterNotification() {
		String name = null;
		int enabled = 1;
		name = getString(R.string.water_name);
		String id = NotificationDishHelper.addNewNotification(new NotificationDish("50", name, "0", "0", enabled, 0), this);
		Log.d("NOTIF", id + " water");
		SaveUtils.setWaterNotificationLoded(true, this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		
		if (stack == null) {
			stack = new Stack<String>();
		}
		if(!SaveUtils.getWaterNotificationLoded(DishActivityGroup.this)){
			addWaterNotification();
		}
		//String lastLoadDate = SaveUtils.loadLastLoadDate(this);
		//if (lastLoadDate.length() == 0){

		//mHandler = new Handler();
       // mDungeonsPurchaseObserver = new StartPurchaseObserver(mHandler);
       //mBillingService = new BillingService();
       // mBillingService.setContext(this.getParent());
       // mBillingService.requestPurchase("android.test.purchased", "inapp", "bulat");
       //mPurchaseDatabase = new PurchaseDatabase(this);
       // setupWidgets();

        // Check if billing is supported.
    //    ResponseHandler.register(mDungeonsPurchaseObserver);
     //   if (!mBillingService.checkBillingSupported()) {
            //showDialog(DIALOG_CANNOT_CONNECT_ID);
     //   }
        
     //   if (!mBillingService.checkBillingSupported(Consts.ITEM_TYPE_SUBSCRIPTION)) {
            //showDialog(DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID);
    //    }   	
		//} else {
			push("DishActivity", new Intent(this, DishActivity.class));
	//	}
			
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//ResponseHandler.register(mDungeonsPurchaseObserver);
	}

	@Override
	protected void onResume() {
		//boolean firstLaunch = SaveUtils.loadFirstLaunchFlag(this);
       // if (firstLaunch){
		if(flag && stack.size() == 0){
			getFirst();
		}else{
			flag=true;
		}
       // }
		super.onResume();
	}
	
	@Override
	  public void finishFromChild(Activity child) {
	    pop();
	  }

	  @Override
	  public void onBackPressed() {
	    pop();
	  }


	  public void push(String id, Intent intent) {
	    Window window = getLocalActivityManager().startActivity(id, intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    if (window != null) {
	      stack.push(id);
	      setContentView(window.getDecorView());
	    }
	  }

	  public void pop() {
		  try{
	    if (stack.size() == 1) {
	    	finish();
	    }else{
		    LocalActivityManager manager = getLocalActivityManager();
		    manager.destroyActivity(stack.pop(), true);
		    if (stack.size() > 0) {
		      Intent lastIntent = manager.getActivity(stack.peek()).getIntent();
		      Window newWindow = manager.startActivity(stack.peek(), lastIntent);
		      setContentView(newWindow.getDecorView());
		    }
	    }
		  }catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  
	  public void getFirst(){
		  if(stack.size() > 0){
			  LocalActivityManager manager = getLocalActivityManager();
			  while (stack.size() != 1){
				  manager.destroyActivity(stack.pop(), true);
			  }
			  Intent lastIntent = manager.getActivity(stack.peek()).getIntent();
		      Window newWindow = manager.startActivity(stack.peek(), lastIntent);
		      setContentView(newWindow.getDecorView());
		  }
	  }
	
	  
	  @Override
	    protected void onStop() {
	        super.onStop();
	        //ResponseHandler.unregister(mDungeonsPurchaseObserver);
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

	}

	@Override
	public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

	}
}
