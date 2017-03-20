package bulat.diet.helper_couch.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;

import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.adapter.FragAdapter;

import bulat.diet.helper_couch.component.CustomViewPager;
import bulat.diet.helper_couch.fragments.ClientCalendarFragment;
import bulat.diet.helper_couch.fragments.ClientsListFragment;
import bulat.diet.helper_couch.fragments.CouchRegistrationFragment;
import bulat.diet.helper_couch.utils.SaveCouchInfo;


public class DietHelperActivity extends AppCompatActivity {

	private BottomNavigationView bottomNavigationView;
	private CustomViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private FragAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initview();

		
	}

	private void initview() {
		bottomNavigationView = (BottomNavigationView)
				findViewById(R.id.bottomNavigation);

		mPager = (CustomViewPager) findViewById(R.id.viewPager);
		initvieViewPager();
		initvieBottomNavigationView();
		bottomNavigationView.selectTab(0);
	}

	private void initvieViewPager() {
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new ClientsListFragment());
		fragmentList.add(new ClientCalendarFragment());
		fragmentList.add(new CouchRegistrationFragment());


		adapter = new FragAdapter(getSupportFragmentManager(), fragmentList);

		mPager.setAdapter(adapter);
		mPager .setOnTouchListener( new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});
	}

	public void goToCalendar(String clientsEmail) {
		SaveCouchInfo.setCurrentClientEmail(clientsEmail, this);
		bottomNavigationView.selectTab(1);
		mPager.setCurrentItem(1, true);

	}

	private void initvieBottomNavigationView() {
		BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
				(getString(R.string.clients), ContextCompat.getColor(this, R.color.bg_group_item_dragging_active_state), R.drawable.ic_group_add_black_24dp);
		BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
				(getString(R.string.tab_calendar), ContextCompat.getColor(this, R.color.bg_item_dragging_state), R.drawable.ic_history_black_24dp);
		BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
				(getString(R.string.tab_setting), ContextCompat.getColor(this, R.color.bg_item_normal_state), R.drawable.ic_settings_black_24dp);
		bottomNavigationView.addTab(bottomNavigationItem);
		bottomNavigationView.addTab(bottomNavigationItem1);
		bottomNavigationView.addTab(bottomNavigationItem2);
		mPager.setNestedScrollingEnabled(false);
		mPager.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		mPager.setPagingEnabled(false);
		bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
			@Override
			public void onNavigationItemClick(int index) {
				switch (index) {
					case 0:
						mPager.setCurrentItem(index, true);
						break;
					case 1:
						mPager.setCurrentItem(index, true);
						break;
					case 2:
						mPager.setCurrentItem(index, true);
						break;
				}
			}
		});

	}
}
