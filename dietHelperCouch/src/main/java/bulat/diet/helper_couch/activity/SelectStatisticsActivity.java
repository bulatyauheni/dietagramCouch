package bulat.diet.helper_couch.activity;

import java.util.Date;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.utils.CustomAlertDialogBuilder;
import bulat.diet.helper_couch.utils.SaveUtils;

public class SelectStatisticsActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.statistics_list, null);
		setContentView(viewToLoad);	
			
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		LinearLayout linear=(LinearLayout) findViewById(R.id.linearLayoutChart);
		final String[] items = new String[] {getString(R.string.payment), getString(R.string.statistic_top10),getString(R.string.statistic_weight), getString(R.string.statistic_FCP),getString(R.string.statistic_export),getString(R.string.statistic_import)};
		
		ListView listView = (ListView) findViewById(R.id.listViewStatistics);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
	            this,android.R.layout.simple_list_item_single_choice, items){
			 @Override
		        public View getView(int position, View convertView,ViewGroup parent) {
		            View view =super.getView(position, convertView, parent);

		            TextView textView=(TextView) view.findViewById(android.R.id.text1);

		            /*YOUR CHOICE OF COLOR*/
		            textView.setTextColor(Color.DKGRAY);

		            return view;
		        }
	    };		 
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();	
				if(arg2==0){

				}else if(arg2==1){
					intent.setClass(getParent(), Top10.class);
				}else if(arg2==2){
					intent.setClass(getParent(), WeightChartActivity.class);
				}else if(arg2==3){
					intent.setClass(getParent(), StatisticFCPActivity.class);
				}else if(arg2==4){
					intent.setClass(getParent(), StatisticExportActivity.class);
				}else if(arg2==5){
					intent.setClass(getParent(), StatisticImportActivity.class);
				}
				StatisticActivityGroup activityStack = (StatisticActivityGroup) getParent();
					activityStack.push("StatisticActivity", intent);
				
				
			}
		});
		Date currDate = new Date();
		if(currDate.getTime()>SaveUtils.getEndPDate(this)){
			CustomAlertDialogBuilder bld = new CustomAlertDialogBuilder(SelectStatisticsActivity.this.getParent().getParent());
			bld.setLayout(R.layout.section_alert_dialog_two_buttons)
			.setMessage(SelectStatisticsActivity.this.getParent().getString(R.string.payment_dialog_alert))		
			.setPositiveButton(R.id.dialogButtonOk, new OnClickListener() {
				
				@Override
				public void onClick(View v) {

				}
			});
			bld.setPositiveButtonText(R.string.ok)
			.setNegativeButton(R.id.dialogButtonCancel, new OnClickListener() {
				@Override
				public void onClick(View v) {	
					if(!SaveUtils.isUseFreeAbonement(getApplicationContext())){
						try {
							Intent intent = new Intent();
							intent.setClass(getParent().getParent(), FreeAbonementActivity.class);
							startActivity(intent);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			})
			.setNegativeButtonText(R.string.get_free_time);
			bld.show();		
		}
	
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:

				break;			
			}
		}
	};
}
