package bulat.diet.helper_couch.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.activity.DietHelperActivity;
import bulat.diet.helper_couch.adapter.ClientsAdapter;
import bulat.diet.helper_couch.item.ClientDTO;
import bulat.diet.helper_couch.item.CouchDTO;
import bulat.diet.helper_couch.service.RestService;
import bulat.diet.helper_couch.service.ServiceFactory;
import bulat.diet.helper_couch.utils.SaveCouchInfo;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yauheni.Bulat on 15.02.2017.
 */

public class ClientsListFragment extends android.support.v4.app.Fragment {
    ImageView addButton;
    private RecyclerView recyclerView;
    private ClientsAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.clients_list, container, false);
        addButton = (ImageView) view.findViewById(R.id.add_client_btn);
        recyclerView = (RecyclerView) view.findViewById(R.id.listClients);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddClientDialogFragment alertDialogFragment = new AddClientDialogFragment();
                alertDialogFragment.show(getFragmentManager(), "fragment_edit_name");
            }
        });
        initClientsList();
    }

    private void initClientsList() {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        RestService service = ServiceFactory.createRetrofitService(RestService.class, RestService.SERVICE_COMMON_ENDPOINT);


        service.getClients(SaveCouchInfo.getCouchId(getContext()), "gmail_todo")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ClientDTO>>() {
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
                    public final void onNext(List<ClientDTO> response) {
                        if (response != null) {
                            mAdapter = new ClientsAdapter(response, (DietHelperActivity) getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                        }
                    }
                });

    }
}
