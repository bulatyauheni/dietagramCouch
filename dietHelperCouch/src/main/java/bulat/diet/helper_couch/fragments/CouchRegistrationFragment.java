package bulat.diet.helper_couch.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.item.BasicDTO;
import bulat.diet.helper_couch.item.CouchDTO;
import bulat.diet.helper_couch.item.TodayItemDTO;
import bulat.diet.helper_couch.service.RestService;
import bulat.diet.helper_couch.service.ServiceFactory;
import bulat.diet.helper_couch.utils.SaveCouchInfo;
import bulat.diet.helper_couch.utils.SaveUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yauheni.Bulat on 17.02.2017.
 */

public class CouchRegistrationFragment extends android.support.v4.app.Fragment {
    Button mAddButton;
    private EditText mEdtfirstname;
    private EditText mEdtlastname;
    private EditText mEdtPhone;
    private EditText mEdtEmail;
    private TextView mtvfirstname;
    private TextView mtvlastname;
    private TextView mtvPhone;
    private TextView mtvEmail;
    RelativeLayout registredLayout;
    RelativeLayout registerLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.register_couch_form, container, false);
        registredLayout = (RelativeLayout) view.findViewById(R.id.registredCouchLayout);
        registerLayout = (RelativeLayout) view.findViewById(R.id.registerCouchLayout);
        if (!TextUtils.isEmpty(SaveCouchInfo.getCouchId(getContext()))) {
            registredLayout.setVisibility(View.VISIBLE);
            registerLayout.setVisibility(View.GONE);
        } else {
            registredLayout.setVisibility(View.GONE);
            registerLayout.setVisibility(View.VISIBLE);
        }
        mAddButton = (Button) view.findViewById(R.id.btnRegister);
        mtvfirstname = (TextView) view.findViewById(R.id.tvfirstname);
        mtvlastname = (TextView) view.findViewById(R.id.tvlastname);
        mtvPhone = (TextView) view.findViewById(R.id.tvPhone);
        mtvEmail = (TextView) view.findViewById(R.id.tvEmail);

        mAddButton = (Button) view.findViewById(R.id.btnRegister);
        mEdtfirstname = (EditText) view.findViewById(R.id.edtfirstname);
        mEdtlastname = (EditText) view.findViewById(R.id.edtlastname);
        mEdtPhone = (EditText) view.findViewById(R.id.edtPhone);
        mEdtEmail = (EditText) view.findViewById(R.id.edtEmail);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(SaveCouchInfo.getCouchId(getContext()))) {
            initRegistred();
        } else {
            initRegistration();
        }

    }

    private void initRegistred() {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        RestService service = ServiceFactory.createRetrofitService(RestService.class, RestService.SERVICE_COMMON_ENDPOINT);


        service.getCouchInfo(SaveCouchInfo.getCouchId(getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CouchDTO>() {
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
                    public final void onNext(CouchDTO response) {
                        if (response != null) {
                            mtvfirstname.setText(response.getFirstname());
                            mtvlastname.setText(response.getLastname());
                            mtvPhone.setText(response.getPhone());
                            mtvEmail.setText(response.getEmail());
                        }
                    }
                });

    }

    private void initRegistration() {
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = new ProgressDialog(getActivity());
                progress.setTitle("Loading");
                progress.setMessage("Wait while loading...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                RestService service = ServiceFactory.createRetrofitService(RestService.class, RestService.SERVICE_COMMON_ENDPOINT);


                    service.registerCouch(mEdtfirstname.getText().toString(),
                            mEdtlastname.getText().toString(),
                            mEdtEmail.getText().toString(),
                            mEdtPhone.getText().toString(),
                            "T5")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<BasicDTO>() {
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
                                public final void onNext(BasicDTO response) {
                                    if (response != null) {
                                        SaveCouchInfo.setCouchId(response.getStatus(), getContext());
                                        registredLayout.setVisibility(View.VISIBLE);
                                        registerLayout.setVisibility(View.GONE);
                                        mtvfirstname.setText(mEdtfirstname.getText().toString());
                                        mtvlastname.setText(mEdtlastname.getText().toString());
                                        mtvPhone.setText(mEdtEmail.getText().toString());
                                        mtvEmail.setText(mEdtPhone.getText().toString());
                                    }
                                }
                            });

            }
        });
    }

}
