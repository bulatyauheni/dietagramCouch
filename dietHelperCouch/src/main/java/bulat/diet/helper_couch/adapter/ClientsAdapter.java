package bulat.diet.helper_couch.adapter;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.activity.DietHelperActivity;
import bulat.diet.helper_couch.item.ClientDTO;

/**
 * Created by yauheni.bulat on 09.03.2017.
 */

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientsHolder>{
    List<ClientDTO> mClients;
    DietHelperActivity mContext;


    public ClientsAdapter(List<ClientDTO> clients, DietHelperActivity context) {
        mClients = clients;
        mContext = context;

    }
    @Override
    public ClientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_row, parent, false);

        return new ClientsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClientsHolder holder, int position) {
        ClientDTO client = mClients.get(position);
        holder.name.setText(client.getFirstname() +  " " +  client.getLastname());
        holder.email.setText(client.getEmail());
        holder.income.setText(client.getIncome());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mClients.size();
    }

    public class ClientsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView income;
        public TextView name;
        public TextView email;
        public int position;
        public ClientsHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.clientNameTW);
            email = (TextView) view.findViewById(R.id.clientEmailTW);
            income = (TextView) view.findViewById(R.id.textViewIncome);
            email.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            ((DietHelperActivity) mContext).goToCalendar(email.getText().toString(), name.getText().toString(), mClients.get(position).getLimit());
        }
    }
}
