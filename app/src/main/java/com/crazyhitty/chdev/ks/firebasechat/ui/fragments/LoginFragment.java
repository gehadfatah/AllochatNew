package com.crazyhitty.chdev.ks.firebasechat.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.login.LoginContract;
import com.crazyhitty.chdev.ks.firebasechat.core.login.LoginPresenter;
import com.crazyhitty.chdev.ks.firebasechat.core.users.get.all.GetUsersContract;
import com.crazyhitty.chdev.ks.firebasechat.core.users.get.all.GetUsersPresenter;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.ui.activities.RegisterActivity;
import com.crazyhitty.chdev.ks.firebasechat.ui.activities.UserListingActivity;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.crazyhitty.chdev.ks.firebasechat.utils.SharedPrefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 10:36 AM
 * Project: FirebaseChat
 */

public class LoginFragment extends Fragment implements GetUsersContract.View,
        View.OnClickListener, LoginContract.View {
    private LoginPresenter mLoginPresenter;
    private String mVerificationId;
    private EditText mETxtEmailOrphone, mETxtPassword;
    private Button mBtnLogin, mBtnRegister;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ProgressDialog mProgressDialog;
    String password, emailOrPhone;
    String TAG = "LoginFragment";
    String default_email = "", default_pass = "";
    boolean PhoneOrNot = false;
    private GetUsersPresenter mGetUsersPresenter;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        bindViews(fragmentView);

        return fragmentView;
    }

    private void bindViews(View view) {

        mETxtEmailOrphone = (EditText) view.findViewById(R.id.edit_text_email_id);
        mETxtPassword = (EditText) view.findViewById(R.id.edit_text_password);
        mBtnLogin = (Button) view.findViewById(R.id.button_login);
        mBtnRegister = (Button) view.findViewById(R.id.button_register);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mLoginPresenter = new LoginPresenter(this);
        mGetUsersPresenter = new GetUsersPresenter(this);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);

        setDummyCredentials();
    }

    private void setDummyCredentials() {
        try {
            default_email = new SharedPrefUtil(getActivity()).getString(Constants.ARG_USER_EMAIL);
            default_pass = new SharedPrefUtil(getActivity()).getString(Constants.ARG_USER_password);
        } catch (Exception e) {
            e.getMessage();
            default_email = "";
            default_pass = "";
        }

        mETxtEmailOrphone.setText(default_email);
        mETxtPassword.setText(default_pass);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_login:
                onLogin(view);
                break;
            case R.id.button_register:
                onRegister(view);
                break;
        }
    }

    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static boolean isNumericc(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;

    }

    private void onLogin(View view) {
        emailOrPhone = mETxtEmailOrphone.getText().toString();
        password = mETxtPassword.getText().toString();
        // PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        /*for (int i=0;i<=emailOrPhone.length();i++) {*/
        if (isNumeric(emailOrPhone)) {
            if (default_pass == null) {
                default_pass = "9174932283";
            }
            if (default_email == null) {
                default_email = "jdjd@bdjdlw.com";
            }
            //   Toast.makeText(getActivity(),"Please sure you enter correct email",Toast.LENGTH_LONG).show();
            PhoneOrNot = true;
            mLoginPresenter.login(getActivity(), default_email, default_pass, PhoneOrNot);
            mProgressDialog.show();
               /*mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                   @Override
                   public void onVerificationCompleted(PhoneAuthCredential credential) {
                       // This callback will be invoked in two situations:
                       // 1 - Instant verification. In some cases the phone number can be instantly
                       //     verified without needing to send or enter a verification code.
                       // 2 - Auto-retrieval. On some devices Google Play services can automatically
                       //     detect the incoming verification SMS and perform verificaiton without
                       //     user action.
                       Log.d(TAG, "onVerificationCompleted:" + credential);

                       signInWithPhoneAuthCredential(credential);
                   }






                   @Override
                   public void onVerificationFailed(FirebaseException e) {
                       // This callback is invoked in an invalid request for verification is made,
                       // for instance if the the phone number format is not valid.
                       Log.w(TAG, "onVerificationFailed", e);

                       if (e instanceof FirebaseAuthInvalidCredentialsException) {
                           // Invalid request
                           // ...
                       } else if (e instanceof FirebaseTooManyRequestsException) {
                           // The SMS quota for the project has been exceeded
                           // ...
                       }

                       // Show a message and update the UI
                       // ...
                   }

                   @Override
                   public void onCodeSent(String verificationId,
                                          PhoneAuthProvider.ForceResendingToken token) {
                       // The SMS verification code has been sent to the provided phone number, we
                       // now need to ask the user to enter the code and then construct a credential
                       // by combining the code with a verification ID.
                       Log.d("TAG ", "onCodeSent:" + verificationId);

                       // Save verification ID and resending token so we can use them later
                       mVerificationId = verificationId;
                       mResendToken = token;

                       // ...
                   }
               };
               PhoneAuthProvider.getInstance().verifyPhoneNumber(
                       emailOrPhone,        // Phone number to verify
                       60,                 // Timeout duration
                       TimeUnit.SECONDS,   // Unit of timeout
                       getActivity(),               // Activity (for callback binding)
                       mCallbacks);
               mProgressDialog.show();
*/
        } else {
            // OnVerificationStateChangedCallbacks
            PhoneOrNot = false;
            mLoginPresenter.login(getActivity(), emailOrPhone, password, PhoneOrNot);
            mProgressDialog.show();
        }



       /* if (emailOrPhone.equals("") || emailOrPhone.equals(null)) {
            emailOrPhone=new SharedPrefUtil(getContext()).getString(Constants.ARG_USER_EMAIL, null);
        }*/

    }

    private void onRegister(View view) {
        RegisterActivity.startActivity(getActivity());
    }

    @Override
    public void onLoginSuccess(String message) {
        /*FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS)
                .child(getFi.getUid())*/
        if (PhoneOrNot) {
            getUsers();
        } else {
            mProgressDialog.dismiss();
            Toast.makeText(getActivity(), "Logged in successfully", Toast.LENGTH_SHORT).show();
            UserListingActivity.startActivity(getActivity(),
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }


    }

    @Override
    public void onLoginFailure(String message) {
        mProgressDialog.dismiss();
        try {
            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();

        } catch (Exception i) {
            i.getMessage();
        }
    }

    private void getUsers() {
       /* if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_CHATS)) {
          //  mDatabaseReference = mFirebaseDatabase.getReference().child("messages");

        } else if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_One_chat)) {
            mGetUsersPresenter.getAllUsers();
        }*/
        mGetUsersPresenter.getAllUsers(true);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            mProgressDialog.dismiss();

                            //  FirebaseUser user = task.getResult().getUser().;
                            Toast.makeText(getActivity(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                            UserListingActivity.startActivity(getActivity(),
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    @Override
    public void onGetAllUsersSuccess(List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getPassword().equals(password) && user.getPhone().equals(emailOrPhone)) {
                //  if (user.getPhone().equals(emailOrPhone)) {
               /* Toast.makeText(getActivity(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                UserListingActivity.startActivity(getActivity(),
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);*/
                PhoneOrNot = false;
                mLoginPresenter.login(getActivity(), user.getEmail(), user.getPassword(), PhoneOrNot);
               // mProgressDialog.show();
               break;
                //  }
            } else if (i == users.size() - 1 && (!user.getPassword().equals(password) || !user.getPhone().equals(emailOrPhone))) {
                try {
                    Toast.makeText(getContext(), "please insert correct email and password or register ", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }

    @Override
    public void onGetAllUsersFailure(String message) {
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();

    }
}
