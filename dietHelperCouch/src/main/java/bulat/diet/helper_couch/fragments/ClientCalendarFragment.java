package bulat.diet.helper_couch.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.activity.AddTodayDishActivity;
import bulat.diet.helper_couch.activity.CalendarActivityGroup;
import bulat.diet.helper_couch.activity.DishActivity;
import bulat.diet.helper_couch.activity.TemplateActivity;
import bulat.diet.helper_couch.adapter.DaysAdapter;
import bulat.diet.helper_couch.adapter.SocialCalendarAdapter;
import bulat.diet.helper_couch.db.TodayDishHelper;
import bulat.diet.helper_couch.item.Day;
import bulat.diet.helper_couch.item.TodayItemDTO;
import bulat.diet.helper_couch.service.RestService;
import bulat.diet.helper_couch.service.ServiceFactory;
import bulat.diet.helper_couch.utils.NetworkState;
import bulat.diet.helper_couch.utils.SaveCouchInfo;
import bulat.diet.helper_couch.utils.SaveUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yauheni.Bulat on 15.02.2017.
 */

public class ClientCalendarFragment extends android.support.v4.app.Fragment {
    TextView header;
    ListView daysList;
    Cursor c;
    int sum;
    private TextView avgFatView;
    private TextView avgProteinView;
    private TextView avgCarbonView;
    private TextView avgCalorisityView;
    DaysAdapter da;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.days_list, container, false);

        return view;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            //clear days list
            da = new DaysAdapter(getActivity().getApplicationContext(), null);
            daysList.setAdapter(da);
            //download and show actual days list for current client
            initData();
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        avgCalorisityView = (TextView) view.findViewById(R.id.textViewAVGValue);
        avgFatView = (TextView) view.findViewById(R.id.textViewFat);
        avgProteinView = (TextView) view.findViewById(R.id.textViewProtein);
        avgCarbonView = (TextView) view.findViewById(R.id.textViewCarbon);
        String[] avgVals = TodayDishHelper.getAvgDishCalorisity(getActivity());
        TextView tvFp = (TextView) view.findViewById(R.id.textViewFatPercent);
        TextView tvCp = (TextView) view.findViewById(R.id.textViewCarbonPercent);
        TextView tvPp = (TextView) view.findViewById(R.id.textViewProteinPercent);

        avgCalorisityView.setText(avgVals[0] + " " + getString(R.string.kcal));

        avgFatView.setText(avgVals[1]);
        avgCarbonView.setText(avgVals[2]);
        avgProteinView.setText(avgVals[3]);

        DecimalFormat df = new DecimalFormat("###.#");

        float sum = Float.valueOf(avgVals[1].replace(",", ".")) + Float.valueOf(avgVals[2].replace(",", "."))
                + Float.valueOf(avgVals[3].replace(",", "."));
        tvFp.setText("(" + df.format(Float.valueOf(avgVals[1].replace(",", ".")) * 100 / sum)
                + "%)");
        tvCp.setText("(" + df.format(Float.valueOf(avgVals[2].replace(",", ".")) * 100 / sum)
                + "%)");
        tvPp.setText("(" + df.format(Float.valueOf(avgVals[3].replace(",", ".")) * 100 / sum)
                + "%)");
        daysList = (ListView) view.findViewById(R.id.listViewDays);
        daysList.setOnItemClickListener(daysListOnItemClickListener);
        daysList.setItemsCanFocus(true);
        Button templateTab = (Button) view.findViewById(R.id.templateTab);
        templateTab.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                initData();
            }
        });

    }

    private void initData() {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        RestService service = ServiceFactory.createRetrofitService(RestService.class, RestService.SERVICE_API_ENDPOINT);

        service.getClientsHistory(SaveCouchInfo.getCurrentClientEmail(getActivity()) + ".json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TodayItemDTO>>() {
                    @Override
                    public final void onCompleted() {
                        progress.dismiss();
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("GithubDemo", e.getMessage());
                        progress.dismiss();
                    }

                    @Override
                    public void onNext(List<TodayItemDTO> todayItemDTOs) {
                        if (todayItemDTOs != null) {
                            TodayDishHelper.clearAll(getActivity());
                            for (TodayItemDTO todayItemDTO : todayItemDTOs) {
                                TodayDishHelper.addNewDish(todayItemDTO, getActivity());
                            }
                            c = TodayDishHelper.getDaysNew(getActivity().getApplicationContext());
                            if (c != null) {
                                try {
                                    int customLimit = SaveUtils.getCustomLimit(getActivity());
                                    if (customLimit > 0) {
                                        SaveUtils.saveBMR(String.valueOf(customLimit), getActivity());
                                        SaveUtils.saveMETA(String.valueOf(customLimit), getActivity());
                                    }
                                    da = new DaysAdapter(getActivity().getApplicationContext(), c);
                                    daysList.setAdapter(da);
                                } catch (Exception e) {
                                    if (c != null)
                                        c.close();
                                } finally {
                                    // c.close();
                                }
                            }
                        }
                        progress.dismiss();
                    }
                });
    }

    private AdapterView.OnItemClickListener daysListOnItemClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
            // switchTabInActivity(1)
            Intent intent = new Intent();
            TextView day = (TextView) v.findViewById(R.id.textViewDay);

            intent.putExtra(AddTodayDishActivity.TITLE,
                    getString(R.string.edit_today_dish));
            intent.putExtra(DishActivity.DATE, day.getTag().toString());
            intent.putExtra(DishActivity.BACKBTN, true);
            intent.putExtra(DishActivity.PARENT_NAME,
                    CalendarActivityGroup.class.toString());
            intent.setClass(getActivity(), DishActivity.class);
            startActivity(intent);
        }
    };
}
