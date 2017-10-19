package bulat.diet.helper_couch.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.item.BasicDTO;
import bulat.diet.helper_couch.item.CouchDTO;
import bulat.diet.helper_couch.service.RestService;
import bulat.diet.helper_couch.service.ServiceFactory;
import bulat.diet.helper_couch.utils.SaveCouchInfo;
import bulat.diet.helper_couch.utils.StringUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Yauheni.Bulat on 15.02.2017.
 */

public class AddClientDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPhone;
    private EditText mDgrId;
    private EditText mEmail;
    private Button mButtonAdd;
    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    public interface UserNameListener {
        void onFinishUserDialog(String user);
    }

    // Empty constructor required for DialogFragment
    public AddClientDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_client_form, container);
        mFirstName = (EditText) view.findViewById(R.id.edtfirstname);
        mLastName = (EditText) view.findViewById(R.id.edtlastname);
        mPhone = (EditText) view.findViewById(R.id.edtPhone);
        mDgrId = (EditText) view.findViewById(R.id.edtDietagramID);
        mEmail = (EditText) view.findViewById(R.id.edtEmail);
        // set this instance as callback for editor action
        mFirstName.requestFocus();
        mButtonAdd = (Button) view.findViewById(R.id.buttonAddClient);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClient();
            }
        });
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(getContext().getString(R.string.client_info));

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Return input text to activity
        UserNameListener activity = (UserNameListener) getActivity();
        this.dismiss();
        return true;
    }

    private void addClient() {

        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        RestService service = ServiceFactory.createRetrofitService(RestService.class, RestService.SERVICE_COMMON_ENDPOINT);

        service.addClient(SaveCouchInfo.getCouchId(getContext()),
                StringUtils.getEmail(getContext()),
                mFirstName.getText().toString(),
                mLastName.getText().toString(),
                mPhone.getText().toString(),
                mEmail.getText().toString(),
                mDgrId.getText().toString()
                ).subscribeOn(Schedulers.io())
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
                        progress.dismiss();
                        dismiss();

                }
                });

    }
}