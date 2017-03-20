package bulat.diet.helper_couch.activity;

import com.github.mikephil.charting.charts.PieChart;

import bulat.diet.helper_couch.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FreeAbonementActivity extends StatisticFCPActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.free_abonement,
				null);
		setContentView(viewToLoad);
		mParties = new String[] { getString(R.string.protein),
				getString(R.string.carbon), getString(R.string.fat) };
		
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
				
		initDietTypeSpinner();
		chartsLayout = (LinearLayout) 	findViewById (R.id.chartsLayout);

		ImageButton vkButton = (ImageButton) findViewById(R.id.buttonVKChart);
		vkButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {						
				Intent i = new Intent(getApplicationContext(), VkActivity.class);
				i.putExtra(VkActivity.IMAGE_PATH, getBitmapFromView(chartsLayout));
				i.putExtra(VkActivity.IMAGE_DESK, successInPercentageTV.getText().toString());
				startActivityForResult(i, 1);

			}
		});
		if (!getString(R.string.locale).equals("ru")){
			vkButton.setVisibility(View.GONE);
		}


		
		mChartIdeal = (PieChart) findViewById(R.id.chart1);
		initChart(mChartIdeal);
		mChartIdeal.setCenterText(getString(R.string.idealCheet));
		
		mChartClient = (PieChart) findViewById(R.id.chart2);
		initChart(mChartClient);
		mChartClient.setCenterText(getString(R.string.yourCheet));
		
		initTimeIntervalSelector();

		successInPercentageTV = (TextView) findViewById(R.id.successInPercentageTV);
		

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    onBackPressed();
	}
}
