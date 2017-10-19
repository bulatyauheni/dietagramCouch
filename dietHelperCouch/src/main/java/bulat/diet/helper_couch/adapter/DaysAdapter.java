package bulat.diet.helper_couch.adapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.activity.CalendarActivity;
import bulat.diet.helper_couch.activity.CalendarActivityGroup;
import bulat.diet.helper_couch.activity.DishActivity;
import bulat.diet.helper_couch.activity.Info;
import bulat.diet.helper_couch.activity.StatisticFCPActivity;
import bulat.diet.helper_couch.activity.VolumeInfo;
import bulat.diet.helper_couch.db.DishProvider;
import bulat.diet.helper_couch.db.TodayDishHelper;
import bulat.diet.helper_couch.item.BodyParams;
import bulat.diet.helper_couch.utils.DialogUtils;
import bulat.diet.helper_couch.utils.GATraker;
import bulat.diet.helper_couch.utils.SaveUtils;
import bulat.diet.helper_couch.utils.SocialUpdater;
import bulat.diet.helper_couch.utils.StringUtils;


public class DaysAdapter extends CursorAdapter {
	private static final String CALENDAR_WEIGHT_BUTTON_CLICK = "CALENDAR_WEIGHT_BUTTON_CLICK";
	public static final String WEIGHT_BTN = "WEIGHT_BTN";
	DecimalFormat df = new DecimalFormat("###.#");
	private Context ctx;
	private CalendarActivityGroup parent;
	CalendarActivity page;
	int height = 0;
	int age = 0;
	int sex = 0;
	int activity = 0;
	private String[] mParties;
	
	public DaysAdapter(Context context, Cursor c) {
		super(context, c);
		mParties = new String[] { "","","" };
		this.page = page;
		ctx = context;
		height = SaveUtils.getHeight(ctx) + Info.MIN_HEIGHT;
		age = SaveUtils.getAge(ctx)+ Info.MIN_AGE;
		sex = SaveUtils.getSex(ctx);
		activity = SaveUtils.getActivity(ctx)+1;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.days_list_row, parent, false);
		return v;
		// return inflater.inflate(R.layout.item_list_row, null);
	}

	@Override
	public int getCount() {
		if (super.getCount() != 0) {
			return super.getCount();
		}
		return 0;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		String dayDate = c.getString(c
				.getColumnIndex(DishProvider.TODAY_DISH_DATE));
		String itemCaloricity = c.getString(c.getColumnIndex("val"));
		String itemWoterWeight = c.getString(c.getColumnIndex("woterweight"));
		String itemWeight = c.getString(c.getColumnIndex("weight"));
		String itemBodyWeight = "";

			try {
				if ((c.getInt(c.getColumnIndex("count")) - 1) > 0) {
					itemBodyWeight = String.valueOf(c.getFloat(c
							.getColumnIndex("bodyweight")));
					v.setBackgroundResource(R.color.main_color);
				} else {
					itemBodyWeight = String.valueOf(c.getFloat(c
							.getColumnIndex("bodyweight")));
					v.setBackgroundResource(R.color.main_color);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		TextView dateTextView = (TextView) v.findViewById(R.id.textViewDay);

		dateTextView.setText(dayDate.split(" ")[1]);
		dateTextView.setTag(dayDate);
		SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEE", new Locale(
				SaveUtils.getLang(context)));
		SimpleDateFormat month = new SimpleDateFormat("MMM", new Locale(
				SaveUtils.getLang(context)));
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM", new Locale(
				SaveUtils.getLang(context)));
		TextView monthTextView = (TextView) v.findViewById(R.id.textViewMonth);

		try {
			monthTextView.setText(month.format(sdf.parse(dayDate)));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		TextView dayOfWeekTextView = (TextView) v.findViewById(R.id.textViewDayOFWeek);
		dayOfWeekTextView.setText(dayDate.split(" ")[0]);

		View iv = (View) v.findViewById(R.id.imageViewDay);
		int flag = checkLimit(Integer.parseInt(itemCaloricity), Float.valueOf(itemBodyWeight));
		if (0==flag) {
			iv.setBackgroundResource(R.color.today_green);
		} else if (1==flag){
			iv.setBackgroundResource(R.color.red);
		} else{
			iv.setBackgroundResource(R.color.red);
		}
		LinearLayout le = (LinearLayout) v.findViewById(R.id.layoutEmpty);
		LinearLayout lf = (LinearLayout) v.findViewById(R.id.layoutParams);
		if ("0".equals(itemWeight) && "0".equals(itemCaloricity)) {
			lf.setVisibility(View.GONE);
			le.setVisibility(View.VISIBLE);

		} else {
			lf.setVisibility(View.VISIBLE);
			le.setVisibility(View.GONE);

			TextView fatView = (TextView) v.findViewById(R.id.textViewFat);
			fatView.setText(c.getString(c.getColumnIndex("fat"))==null?"0":c.getString(c.getColumnIndex("fat")));

			TextView carbonView = (TextView) v
					.findViewById(R.id.textViewCarbon);
			carbonView.setText(c.getString(c.getColumnIndex("carbon"))==null?"0":c.getString(c.getColumnIndex("carbon")));

			TextView proteinView = (TextView) v
					.findViewById(R.id.textViewProtein);
			proteinView.setText(c.getString(c.getColumnIndex("protein"))==null?"0":c.getString(c.getColumnIndex("protein")));
			
			TextView tvFatPercent = (TextView) v
					.findViewById(R.id.textViewFatPercent);
			TextView tvCarbPercent = (TextView) v
					.findViewById(R.id.textViewCarbonPercent);
			TextView tvProtPercent = (TextView) v
					.findViewById(R.id.textViewProteinPercent);
			
			float protein=  Float.valueOf(c.getString(c.getColumnIndex("protein"))==null?"0":c.getString(c.getColumnIndex("protein")));
			float fat=  Float.valueOf(c.getString(c.getColumnIndex("fat"))==null?"0":c.getString(c.getColumnIndex("fat")));
			float carbon=  Float.valueOf(c.getString(c.getColumnIndex("carbon"))==null?"0":c.getString(c.getColumnIndex("carbon")));
			
			float sum = protein + fat+ carbon;
			
			tvFatPercent.setText("("+df.format(fat*100/sum)+"%)");
			tvCarbPercent.setText("("+df.format(carbon*100/sum)+"%)");
			tvProtPercent.setText("("+df.format(protein*100/sum)+"%)");

			if (sum > 0) {
				tvFatPercent.setText("(" + df.format(Float.valueOf(fat) * 100 / sum)
						+ "%)");
				tvCarbPercent.setText("(" + df.format(Float.valueOf(carbon) * 100 / sum)
						+ "%)");
				tvProtPercent.setText("(" + df.format(Float.valueOf(protein) * 100 / sum)
						+ "%)");
			} else {
				tvFatPercent.setText("(0%)");
				tvCarbPercent.setText("(0%)");
				tvProtPercent.setText("(0%)");
			}

			TextView waterweightView = (TextView)v.findViewById(R.id.textViewWoter);
		      waterweightView.setText(itemWoterWeight + " " + context.getString(R.string.gram));
			TextView caloricityView = (TextView) v
					.findViewById(R.id.textViewCaloricity);

			caloricityView.setText(itemCaloricity + " "
					+ context.getString(R.string.kcal));
			TextView weightView = (TextView) v
					.findViewById(R.id.textViewWeightTotal);

			weightView.setText(itemWeight + " "
					+ context.getString(R.string.gram));

			TextView bodyweightView = (TextView) v
					.findViewById(R.id.textViewWeightBodyTotal);
			
			

			float currWeight = SaveUtils.getRealWeight(ctx);

			if (itemBodyWeight == null) {
				itemBodyWeight = "" + currWeight;
			}
			bodyweightView.setText(itemBodyWeight + " "
					+ context.getString(R.string.kgram));
		}


		PieChart mChartClient = (PieChart) v.findViewById(R.id.chartMini);

		float fat = Float.parseFloat(c.getString(c.getColumnIndex("fat"))==null?"0":c.getString(c.getColumnIndex("fat")));

		float carbon = Float.parseFloat(c.getString(c.getColumnIndex("carbon"))==null?"0":c.getString(c.getColumnIndex("carbon")));

		float protein =Float.parseFloat(c.getString(c.getColumnIndex("protein"))==null?"0":c.getString(c.getColumnIndex("protein")));

		float[] data = {protein, carbon, fat};
		try {
			if (protein == 0&& carbon==0 && fat==0) {
				v.setBackgroundResource(R.color.bg_group_item_dragging_state);
			} else {
				v.setBackgroundResource(R.color.main_color);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		drawChart(mChartClient, data);
		TextView tvi = (TextView) v.findViewById(R.id.textViewId);
		tvi.setText(c.getString(c
				.getColumnIndex(DishProvider.TODAY_DISH_DATE)));
	}

	private void drawChart(PieChart mChartClient, float[] data) {

		initChart(mChartClient);
		mChartClient.setCenterText(ctx.getString(R.string.yourCheet));
		setChartData(mChartClient, 3, 100, data);
	}

	private void setChartData(PieChart mChart, int count, float range, float[] data) {

		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		for (int i = 0; i < count; i++) {
			yVals1.add(new Entry(data[i], i));
		}

		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count + 1; i++)
			xVals.add(mParties[i % mParties.length]);

		PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
		dataSet.setSliceSpace(1f);
		dataSet.setSelectionShift(1f);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<Integer>();

		for (int c : ColorTemplate.VORDIPLOM_COLORS)
			colors.add(c);



		dataSet.setColors(colors);

		PieData pieData = new PieData(xVals, dataSet);
		pieData.setValueFormatter(new PercentFormatter());
		pieData.setValueTextSize(1f);
		pieData.setValueTextColor(Color.TRANSPARENT);
		mChart.setData(pieData);

		// undo all highlights
		mChart.highlightValues(null);

		mChart.invalidate();
	}
	protected void initChart(PieChart mChart) {
		// TODO Auto-generated method stub
		mChart.setUsePercentValues(false);
		mChart.setDescription("");

		mChart.setDragDecelerationFrictionCoef(0.95f);
		mChart.setCenterText("");

		mChart.setExtraOffsets(0.f, 0.f, 0.f, 0.f);

		mChart.setDrawHoleEnabled(true);
		mChart.setHoleColor(Color.WHITE);

		mChart.setTransparentCircleColor(Color.WHITE);
		mChart.setTransparentCircleAlpha(10);

		mChart.setHoleRadius(8f);
		mChart.setTransparentCircleRadius(10f);

		mChart.setDrawCenterText(false);
		mChart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ctx, StatisticFCPActivity.class);
				ctx.startActivity(intent);
			}
		});
		mChart.setRotationAngle(0);
		// enable rotation of the chart by touch
		mChart.setRotationEnabled(true);
		mChart.setHighlightPerTapEnabled(true);


		//mChart.setOnChartValueSelectedListener(this);
		mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
		// mChart.spin(2000, 0, 360);
		Legend l = mChart.getLegend();
		l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
		l.setEnabled(false);
	}
	public int checkLimit(int sum, float bodyweight){
		int BMR = getBMR(bodyweight);
		int META = getMeta(bodyweight);
		int mode = SaveUtils.getMode(ctx);
		try{
		switch (mode) {
		case 0:
			if(sum > BMR){
				return 1;
			}else{
				return 0;
			}			
		case 1:
			if(sum > META){
				return 1;
			}else{
				return 0;
			}				
		case 2:			
			if(sum < BMR){
				return 2;								
			}else{
				return 0;
			}
		default:
			if(sum > META){
				return 1;
			}else{
				return 0;
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;	
	}
	private int getMeta(float bobyweight){
		int sexValue = 0;
		if (sex == 0){
			sexValue = 166;
		}
		int BMR = (int)(9.99*(bobyweight) + 
				6.25*(height) - 
				4.92*(age) - 161 + 
				sexValue);
		BMR = (int)BMR;
		int META = 0;
		if(activity ==1){
			META = (int)(BMR*1.2);
					
		}else if(activity==2){
			META =(int)( BMR*1.35);
		}else if(activity==3){
			META = (int)(BMR*1.55);
		}else if(activity==4){
			META = (int)(BMR*1.75); 
		}else if(activity==5){
			META = (int)(BMR*1.92);
		}
		return META;			
	}
	private int getBMR(float bobyweight){
						
		return (int) (getMeta(bobyweight) * 0.8);
		
		
	}
	public int checkLimit(int sum) {
		int mode = SaveUtils.getMode(ctx);
		try {
			switch (mode) {
			case 0:
				if (sum > Integer.parseInt(SaveUtils.getBMR(ctx))) {
					return 1;
				} else {
					return 0;
				}
			case 1:
				if (sum > Integer.parseInt(SaveUtils.getMETA(ctx))) {
					return 1;
				} else {
					return 0;
				}
			case 2:
				if (sum < Integer.parseInt(SaveUtils.getMETA(ctx))) {
					return 2;
				} else {
					return 0;
				}
			default:
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
