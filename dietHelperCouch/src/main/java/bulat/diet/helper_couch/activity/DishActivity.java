package bulat.diet.helper_couch.activity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.android.gms.common.api.GoogleApiClient;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.adapter.DaysAdapter;
import bulat.diet.helper_couch.adapter.ExpandableDraggableSwipeableExampleAdapter;
import bulat.diet.helper_couch.common.data.ExampleExpandableDataProvider;
import bulat.diet.helper_couch.db.DishProvider;
import bulat.diet.helper_couch.db.NotificationDishHelper;
import bulat.diet.helper_couch.db.TemplateDishHelper;
import bulat.diet.helper_couch.db.TodayDishHelper;
import bulat.diet.helper_couch.item.BodyParams;
import bulat.diet.helper_couch.item.DishType;
import bulat.diet.helper_couch.item.NotificationDish;
import bulat.diet.helper_couch.item.TodayDish;
import bulat.diet.helper_couch.utils.DialogUtils;
import bulat.diet.helper_couch.utils.GATraker;
import bulat.diet.helper_couch.utils.SaveUtils;
import bulat.diet.helper_couch.utils.SocialUpdater;
import bulat.diet.helper_couch.utils.StringUtils;

public class DishActivity extends BaseActivity implements RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener, OnChartValueSelectedListener{
    public static final String DATE = "date";
    public static final String PARENT_NAME = "parentname";
    public static final String DAY_TIME_ID = "DAY_TIME_ID";
    public static final String BACKBTN = "backbtn";
    private static final int DIALOG_TEMPLATE_NAME = 0;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private static final String TODAT_WEIGHT_BUTTON_CLICK = "TODAT_WEIGHT_BUTTON_CLICK";
    private static final String FCP_CHART_TODAY = "FCP_CHART_TODAY";
    private static final String OPEN_FCP_FROM_TODAY = "OPEN_FCP_FROM_TODAY";
    private static final String IS_EDIT_TIMES_TIP_SHOWN = "IS_EDIT_TIMES_TIP_SHOWN";
    private static final String IS_REMOVE_DISH_TIP_SHOWN = "IS_REMOVE_DISH_TIP_SHOWN";
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private boolean flagLoad = false;
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ExpandableDraggableSwipeableExampleAdapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;
    private String parentName;
    private TextView header;
    String curentDateandTime;
    private String selectedTemplate = "";
    private ImageView mWeightButton;
    private TextView mWeighLabel;
    protected String[] mParties = new String[] { "", "", ""};

    private OnClickListener changeWeightClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            GATraker.sendEvent(DaysAdapter.WEIGHT_BTN, TODAT_WEIGHT_BUTTON_CLICK);
            final Dialog dialog = new Dialog(DishActivity.this);
            dialog.setContentView(R.layout.update_weight_dialog);
            dialog.setTitle(R.string.change_weight_dialog_title);
            LinearLayout l1 = (LinearLayout) dialog.findViewById(R.id.linearLayoutForearm);
            if (SaveUtils.getForearmEnbl(DishActivity.this)) l1.setVisibility(View.VISIBLE);
            LinearLayout l2 = (LinearLayout) dialog.findViewById(R.id.linearLayoutWaist);
            if (SaveUtils.getWaistEnbl(DishActivity.this)) l2.setVisibility(View.VISIBLE);
            LinearLayout l3 = (LinearLayout) dialog.findViewById(R.id.linearLayoutChest);
            if (SaveUtils.getChestEnbl(DishActivity.this)) l3.setVisibility(View.VISIBLE);
            LinearLayout l4 = (LinearLayout) dialog.findViewById(R.id.linearLayoutNeck);
            if (SaveUtils.getNeckEnbl(DishActivity.this)) l4.setVisibility(View.VISIBLE);
            LinearLayout l5 = (LinearLayout) dialog.findViewById(R.id.linearLayoutShin);
            if (SaveUtils.getShinEnbl(DishActivity.this)) l5.setVisibility(View.VISIBLE);
            LinearLayout l6 = (LinearLayout) dialog.findViewById(R.id.linearLayoutBiceps);
            if (SaveUtils.getBicepsEnbl(DishActivity.this)) l6.setVisibility(View.VISIBLE);
            LinearLayout l7 = (LinearLayout) dialog.findViewById(R.id.linearLayoutPelvis);
            if (SaveUtils.getPelvisEnbl(DishActivity.this)) l7.setVisibility(View.VISIBLE);
            LinearLayout l8 = (LinearLayout) dialog.findViewById(R.id.linearLayoutHip);
            if (SaveUtils.getHipEnbl(DishActivity.this)) l8.setVisibility(View.VISIBLE);
            StringUtils.setSpinnerValues(dialog, DishActivity.this);
            final Spinner weightSpinner = (Spinner) dialog
                    .findViewById(R.id.search_weight);
            final Spinner weightSpinnerDec = (Spinner) dialog
                    .findViewById(R.id.search_weight_decimal);
            DialogUtils.setArraySpinnerValues(weightSpinner,
                    Info.MIN_WEIGHT, Info.MAX_WEIGHT, DishActivity.this);
            DialogUtils.setArraySpinnerValues(weightSpinnerDec, 0, 10,
                    DishActivity.this);
            weightSpinner.setSelection(SaveUtils.getWeight(DishActivity.this));
            weightSpinnerDec.setSelection(SaveUtils.getWeightDec(DishActivity.this));
            final Spinner chestSpinner = (Spinner) dialog.findViewById(R.id.SpinnerChest);
            final Spinner chestDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerChestDecimal);

            final Spinner pelvisSpinner = (Spinner) dialog.findViewById(R.id.SpinnerPelvis);
            final Spinner pelvisDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerPelvisDecimal);

            final Spinner neckSpinner = (Spinner) dialog.findViewById(R.id.SpinnerNeck);
            final Spinner neckDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerNeckDecimal);

            final Spinner bicepsSpinner = (Spinner) dialog.findViewById(R.id.SpinnerBiceps);
            final Spinner bicepsDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerBicepsDecimal);

            final Spinner forearmSpinner = (Spinner) dialog.findViewById(R.id.SpinnerForearm);
            final Spinner forearmDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerForearmDecimal);

            final Spinner waistSpinner = (Spinner) dialog.findViewById(R.id.SpinnerWaist);
            final Spinner waistDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerWaistDecimal);

            final Spinner hipSpinner = (Spinner) dialog.findViewById(R.id.SpinnerHip);
            final Spinner hipDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerHipDecimal);

            final Spinner shinSpinner = (Spinner) dialog.findViewById(R.id.SpinnerShin);
            final Spinner shinDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerShinDecimal);
            Button buttonOk = (Button) dialog
                    .findViewById(R.id.buttonYes);
            buttonOk.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {

                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "EEE dd MMMM", new Locale(SaveUtils.getLang(DishActivity.this)));
                    String lastDate = TodayDishHelper.getLastDate(DishActivity.this);

                    if (curentDateandTime.equals(TodayDishHelper.getLastDate(DishActivity.this))) {
                        SaveUtils.saveWeight((int) weightSpinner
                                .getSelectedItemId(), DishActivity.this);
                        SaveUtils.saveWeightDec(
                                (int) weightSpinnerDec
                                        .getSelectedItemId(), DishActivity.this);
                        SaveUtils.saveChest((int) chestSpinner.getSelectedItemId(), DishActivity.this);
                        SaveUtils.saveChestDec((int) chestDecSpinner.getSelectedItemId(), DishActivity.this);

                        SaveUtils.savePelvis((int) pelvisSpinner.getSelectedItemId(), DishActivity.this);
                        SaveUtils.savePelvisDec((int) pelvisDecSpinner.getSelectedItemId(), DishActivity.this);

                        SaveUtils.saveNeck((int) neckSpinner.getSelectedItemId(), DishActivity.this);
                        SaveUtils.saveNeckDec((int) neckDecSpinner.getSelectedItemId(), DishActivity.this);

                        SaveUtils.saveBiceps((int) bicepsSpinner.getSelectedItemId(), DishActivity.this);
                        SaveUtils.saveBicepsDec((int) bicepsDecSpinner.getSelectedItemId(), DishActivity.this);

                        SaveUtils.saveForearm((int) forearmSpinner.getSelectedItemId(), DishActivity.this);
                        SaveUtils.saveForearmDec((int) forearmDecSpinner.getSelectedItemId(), DishActivity.this);

                        SaveUtils.saveWaist((int) waistSpinner.getSelectedItemId(), DishActivity.this);
                        SaveUtils.saveWaistDec((int) waistDecSpinner.getSelectedItemId(), DishActivity.this);

                        SaveUtils.saveHip((int) hipSpinner.getSelectedItemId(), DishActivity.this);
                        SaveUtils.saveHipDec((int) hipDecSpinner.getSelectedItemId(), DishActivity.this);

                        SaveUtils.saveShin((int) shinSpinner.getSelectedItemId(), DishActivity.this);
                        SaveUtils.saveShinDec((int) shinDecSpinner.getSelectedItemId(), DishActivity.this);
                        if (SaveUtils.getUserUnicId(DishActivity.this) != 0) {
                            new SocialUpdater(DishActivity.this).execute();
                        }
                    }
                    TodayDishHelper.updateBobyParams(
                            DishActivity.this,
                            curentDateandTime,
                            String.valueOf(((float) weightSpinner
                                    .getSelectedItemId() + Info.MIN_WEIGHT)
                                    + (float) weightSpinnerDec
                                    .getSelectedItemId()
                                    / 10),
                            new BodyParams(String.valueOf((float) chestSpinner.getSelectedItemId() + VolumeInfo.MIN_CHEST + (float) chestDecSpinner.getSelectedItemId() / 10),
                                    String.valueOf((float) bicepsSpinner.getSelectedItemId() + VolumeInfo.MIN_BICEPS + (float) bicepsDecSpinner.getSelectedItemId() / 10),
                                    String.valueOf((float) pelvisSpinner.getSelectedItemId() + VolumeInfo.MIN_PELVIS + (float) pelvisDecSpinner.getSelectedItemId() / 10),
                                    String.valueOf((float) neckSpinner.getSelectedItemId() + VolumeInfo.MIN_NECK + (float) neckDecSpinner.getSelectedItemId() / 10),
                                    String.valueOf((float) waistSpinner.getSelectedItemId() + VolumeInfo.MIN_WAIST + (float) waistDecSpinner.getSelectedItemId() / 10),
                                    String.valueOf((float) forearmSpinner.getSelectedItemId() + VolumeInfo.MIN_FOREARM + (float) forearmDecSpinner.getSelectedItemId() / 10),
                                    String.valueOf((float) hipSpinner.getSelectedItemId() + VolumeInfo.MIN_HIP + (float) hipDecSpinner.getSelectedItemId() / 10),
                                    String.valueOf((float) shinSpinner.getSelectedItemId() + VolumeInfo.MIN_SHIN + (float) shinDecSpinner.getSelectedItemId() / 10)));
                    dialog.cancel();
                    DishActivity.this.mWeighLabel.setText(String.valueOf(TodayDishHelper.getBodyWeightByDate(curentDateandTime, DishActivity.this)));
                }
            });
            Button nobutton = (Button) dialog
                    .findViewById(R.id.buttonNo);
            nobutton.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private PieChart mChartClient;

    private void bindActivity() {

        //   mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        ImageView loadButton = (ImageView) findViewById(R.id.loadtemplate);

        ImageView saveButton = (ImageView) findViewById(R.id.save_as_template);

        header = (TextView) findViewById(R.id.textViewTitle);
        mWeightButton = (ImageView) findViewById(R.id.weightButton);
        mWeighLabel = (TextView) findViewById(R.id.textViewCurrentWeight);
        mChartClient = (PieChart) findViewById(R.id.chartMini);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.activity_today_new, null);
        this.setContentView(viewToLoad);

        bindActivity();
        initRecyclerView(savedInstanceState);
        mWeightButton.setOnClickListener(changeWeightClickListener);
    }

    private void initDishTable() {
        //update dishes list
        if (mAdapter != null) {
            mAdapter.getProvider().setData(getData());
            mAdapter.notifyDataSetChanged();
        }

        //update header
        ArrayList<TodayDish> baseData = TodayDishHelper.getArrayDishesByDate(this, curentDateandTime);

        float sumF = 0;
        float sumC = 0;
        float sumP = 0;
        int sum = 0;
        int sumMinus = 0;

        for (TodayDish dish : baseData) {
            if (dish.getCaloricity()>=0) {
                sumC = sumC + dish.getCarbon();
                sumF = sumF + dish.getFat();
                sumP = sumP + dish.getProtein();
                sum = sum + dish.getCaloricity();
            } else {
                sumMinus = sumMinus + dish.getCaloricity();
            }
        }

        TextView tvTotalCalorie = (TextView) findViewById(R.id.textViewTotalValue);
        TextView tvLoose = (TextView) findViewById(R.id.textViewTotalLooseValue);
        TextView tvF = (TextView) findViewById(R.id.textViewFatTotal);
        TextView tvC = (TextView) findViewById(R.id.textViewCarbonTotal);
        TextView tvP = (TextView) findViewById(R.id.textViewProteinTotal);
        TextView tvFp = (TextView) findViewById(R.id.textViewFatPercent);
        TextView tvCp = (TextView) findViewById(R.id.textViewCarbonPercent);
        TextView tvPp = (TextView) findViewById(R.id.textViewProteinPercent);
        tvTotalCalorie.setText(String.valueOf(sum + sumMinus));
        //tvLoose.setText(String.valueOf(sumLoose));
        DecimalFormat df = new DecimalFormat("###.#");
        tvF.setText(df.format(Float.valueOf(sumF)));
        tvC.setText(df.format(Float.valueOf(sumC)));
        tvP.setText(df.format(Float.valueOf(sumP)));
        float sumTemp = Float.valueOf(sumF) + Float.valueOf(sumC)
                + Float.valueOf(sumP);
        if (sumTemp > 0) {
            tvFp.setText("(" + df.format(Float.valueOf(sumF) * 100 / sumTemp)
                    + "%)");
            tvCp.setText("(" + df.format(Float.valueOf(sumC) * 100 / sumTemp)
                    + "%)");
            tvPp.setText("(" + df.format(Float.valueOf(sumP) * 100 / sumTemp)
                    + "%)");

        } else {
            tvFp.setText("(0%)");
            tvCp.setText("(0%)");
            tvPp.setText("(0%)");
        }
        checkLimit(sum);

        drawChart();

    }

    private void drawChart() {
        
        initChart(mChartClient);
        mChartClient.setCenterText(getString(R.string.yourCheet));
        setChartData(mChartClient, 3, 100, StatisticFCPActivity.getValues(TodayDishHelper.getStatisticFCP(this, curentDateandTime)));
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
                GATraker.sendEvent(FCP_CHART_TODAY, OPEN_FCP_FROM_TODAY);
                intent.setClass(DishActivity.this, StatisticFCPActivity.class);
                startActivity(intent);

            }
        });
        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);


        mChart.setOnChartValueSelectedListener(this);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setEnabled(false);
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

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        String date = null;
        parentName = DishActivityGroup.class.toString();
        if (extras != null) {
            date = extras.getString(DATE);
            parentName = extras.getString(PARENT_NAME);
        }
        if (parentName.equals(CalendarActivityGroup.class.toString())) {
            Button buttonBack = (Button)findViewById(R.id.buttonBack);
            buttonBack.setVisibility(View.VISIBLE);
            buttonBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        Locale locale = new Locale(SaveUtils.getLang(this));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        SimpleDateFormat title = new SimpleDateFormat("dd MMMM", new Locale(
                SaveUtils.getLang(this)));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM", new Locale(
                SaveUtils.getLang(this)));
        if (date == null) {

            curentDateandTime = sdf.format(new Date());
        } else {
            curentDateandTime = date;
        }


        mWeighLabel.setText(String.valueOf(TodayDishHelper.getBodyWeightByDate(curentDateandTime, this)));
        initDishTable();

        try {
            header.setText(date == null ? title.format(new Date()) : title
                    .format(sdf.parse(date)));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }

    private void initRecyclerView(Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
       /* mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) this.getDrawable(this, R.drawable.material_shadow_z3));
*/
        // swipe manager
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();

        //adapter
        final ExpandableDraggableSwipeableExampleAdapter myItemAdapter =
                new ExpandableDraggableSwipeableExampleAdapter(mRecyclerViewExpandableItemManager, getDataProvider());

        myItemAdapter.setEventListener(new ExpandableDraggableSwipeableExampleAdapter.EventListener() {
            @Override
            public void onGroupItemButtonClick(String dayTimeId) {

            }

            @Override
            public void onGroupItemRemoved(int groupPosition) {

            }

            @Override
            public void onChildItemRemoved(int groupPosition, int childPosition) {

            }

            @Override
            public void onGroupItemPinned(int groupPosition) {
                (DishActivity.this).onGroupItemPinned(groupPosition);
            }

            @Override
            public void onChildItemPinned(int groupPosition, int childPosition) {

            }

            @Override
            public void onItemViewClicked(View v, boolean pinned) {
                (DishActivity.this).onItemViewClick(v, pinned);
            }
        });

        mAdapter = myItemAdapter;


        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(myItemAdapter);       // wrap for expanding
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mWrappedAdapter);           // wrap for dragging
        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(mWrappedAdapter);      // wrap for swiping

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Disable the change animation in order to make turning back animation of swiped item works properly.
        // Also need to disable them when using animation indicator.
        animator.setSupportsChangeAnimations(false);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setHasFixedSize(false);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) this.getResources().getDrawable(R.drawable.material_shadow_z1)));
        }
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(this.getResources().getDrawable(R.drawable.list_divider_h), true));


        // NOTE:
        // The initialization order is very important! This order determines the priority of touch event handling.
        //
        // priority: TouchActionGuard > Swipe > DragAndDrop > ExpandableItem
        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);
    }


    public void onGroupItemButtonClick(String groupId) {

    }

    public void onGroupItemPinned(int groupPosition) {
        Intent intent = new Intent();
        intent.setClass(this, NotificationActivity.class);
        intent.putExtra(NotificationActivity.TYPE, "calendar");
        startActivity(intent);
    }
    public void onGroupItemRemoved(int groupPosition) {
        onGroupItemPinned(groupPosition);
    }


    public void onChildItemRemoved(int groupPosition, int childPosition) {

    }

    public void onChildItemPinned(int groupPosition, int childPosition) {
        onChildItemRemoved(groupPosition, childPosition);
    }

    public void onGroupItemClicked(int groupPosition) {
    }

    public void onChildItemClicked(int groupPosition, int childPosition) {

    }

    private void onItemUndoActionClicked() {

    }

    public void onNotifyExpandableItemPinnedDialogDismissed(int groupPosition, int childPosition, boolean ok) {

    }

    public ExampleExpandableDataProvider getDataProvider() {
        return new ExampleExpandableDataProvider(this, getIntent().getStringExtra(DATE));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state to support screen rotation, etc...
        if (mRecyclerViewExpandableItemManager != null) {
            outState.putParcelable(
                    SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    mRecyclerViewExpandableItemManager.getSavedState());
        }
    }

    @Override
    public void onDestroy() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerViewSwipeManager != null) {
            mRecyclerViewSwipeManager.release();
            mRecyclerViewSwipeManager = null;
        }

        if (mRecyclerViewTouchActionGuardManager != null) {
            mRecyclerViewTouchActionGuardManager.release();
            mRecyclerViewTouchActionGuardManager = null;
        }

        if (mRecyclerViewExpandableItemManager != null) {
            mRecyclerViewExpandableItemManager.release();
            mRecyclerViewExpandableItemManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        super.onDestroy();
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {
    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {
        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }

    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = this.getResources().getDimensionPixelSize(R.dimen.list_item_height);
        int topMargin = (int) (this.getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp

        mRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }

    private void onItemViewClick(View v, boolean pinned) {
        final int flatPosition = mRecyclerView.getChildAdapterPosition(v);

        if (flatPosition == RecyclerView.NO_POSITION) {
            return;
        }

        final long expandablePosition = mRecyclerViewExpandableItemManager.getExpandablePosition(flatPosition);
        final int groupPosition = RecyclerViewExpandableItemManager.getPackedPositionGroup(expandablePosition);
        final int childPosition = RecyclerViewExpandableItemManager.getPackedPositionChild(expandablePosition);

        if (childPosition == RecyclerView.NO_POSITION) {
            (this).onGroupItemClicked(groupPosition);
        } else {
            (this).onChildItemClicked(groupPosition, childPosition);
        }
    }

    private boolean supportsViewElevation() {
        return false;//(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public void notifyGroupItemRestored(int groupPosition) {
        mAdapter.notifyDataSetChanged();

        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForGroup(groupPosition);
        final int flatPosition = mRecyclerViewExpandableItemManager.getFlatPosition(expandablePosition);
        mRecyclerView.scrollToPosition(flatPosition);
    }

    public void notifyChildItemRestored(int groupPosition, int childPosition) {
        mAdapter.notifyDataSetChanged();

        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, childPosition);
        final int flatPosition = mRecyclerViewExpandableItemManager.getFlatPosition(expandablePosition);
        mRecyclerView.scrollToPosition(flatPosition);
    }

    public void notifyGroupItemChanged(int groupPosition) {
        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForGroup(groupPosition);
        final int flatPosition = mRecyclerViewExpandableItemManager.getFlatPosition(expandablePosition);

        mAdapter.notifyItemChanged(flatPosition);
    }

    public void notifyChildItemChanged(int groupPosition, int childPosition) {
        final long expandablePosition = RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, childPosition);
        final int flatPosition = mRecyclerViewExpandableItemManager.getFlatPosition(expandablePosition);

        mAdapter.notifyItemChanged(flatPosition);
    }

    private ExampleExpandableDataProvider.DishItemData findDish(int groupId, int itemId) {
        List<Pair<ExampleExpandableDataProvider.DayTimeGroupData, List<ExampleExpandableDataProvider.DishItemData>>> data = getData();

        return data.get(groupId).second.get(itemId);
    }

    private List<Pair<ExampleExpandableDataProvider.DayTimeGroupData, List<ExampleExpandableDataProvider.DishItemData>>> getData() {
        List<Pair<ExampleExpandableDataProvider.DayTimeGroupData, List<ExampleExpandableDataProvider.DishItemData>>> mData = new LinkedList<>();
        ArrayList<TodayDish> baseData = TodayDishHelper.getArrayDishesByDate(this, curentDateandTime);
        ArrayList<NotificationDish> nots = NotificationDishHelper.getEnabledNotificationsList(this);
        nots.add(new NotificationDish("99", getString(R.string.today_fitnes), "",
                "", 1, 1));
        int i = 0;
        for (NotificationDish notif : nots) {
            //noinspection UnnecessaryLocalVariable
            final long groupId = i;
            final String groupText = notif.getName();
            final ExampleExpandableDataProvider.DayTimeGroupData group = new ExampleExpandableDataProvider.DayTimeGroupData(groupId, groupText);
            final List<ExampleExpandableDataProvider.DishItemData> children = new ArrayList<>();
            float tempFat = 0;
            float tempCarbon = 0;
            float tempProtein = 0;
            int tempCalory = 0;
            int tempWeight = 0;
            for (TodayDish dish : baseData) {
                final long childId = Long.parseLong(dish.getId());
                final String childText = dish.getName();
                if (dish.getDayTyme().equals(notif.getId())) {
                    children.add(new ExampleExpandableDataProvider.DishItemData(childId, childText, dish));
                    tempCarbon = tempCarbon + dish.getCarbon();
                    tempFat = tempFat + dish.getFat();
                    tempProtein = tempProtein + dish.getProtein();
                    tempCalory = tempCalory + dish.getCaloricity();
                    tempWeight = tempWeight + dish.getWeight();
                }
            }
            group.setFat(tempFat);
            group.setCarbon(tempCarbon);
            group.setProtein(tempProtein);
            group.setCalory(tempCalory);
            group.setWeight(tempWeight);

            mData.add(new Pair<ExampleExpandableDataProvider.DayTimeGroupData, List<ExampleExpandableDataProvider.DishItemData>>(group, children));
            i++;
        }
        return mData;
    }







    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Intent intent = new Intent();
        intent.setClass(this, StatisticFCPActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNothingSelected() {

    }


}
