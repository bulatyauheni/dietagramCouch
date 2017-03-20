package bulat.diet.helper_couch.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.item.DishType;
import bulat.diet.helper_couch.item.User;

public class LocalsArrayAdapter extends ArrayAdapter<User> {

	private Context context;	
	int layoutResourceId;
	List<DishType> list;

	public LocalsArrayAdapter(Context context, int layoutResourceId, List<DishType> locations) {
		super(context, layoutResourceId);
		this.layoutResourceId=layoutResourceId;
		this.context = context;
		this.list = locations;		
	}
	
	@Override
	public int getCount(){
		if( list != null ){
			return list.size();
		}
		return 0;
	}
	
	@Override
	public View  getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout rowView;
		DishType location = null;
		try{
			location = list.get(position);
		}catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		if (convertView == null) {
			rowView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(layoutResourceId, rowView, true);
		} else {
			rowView = (LinearLayout) convertView;
		}
		
		
		if (location != null){
			String name = location.getDescription();
			
			ImageView iv = (ImageView) rowView.findViewById(R.id.imageViewDay);
			iv.setImageResource(location.getTypeKey());
			TextView nameTextView = (TextView) rowView
					.findViewById(R.id.textViewName);

			nameTextView.setText(name);	
		}
		
		return rowView;
	}
		

}
