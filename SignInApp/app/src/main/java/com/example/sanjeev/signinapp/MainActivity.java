package com.example.sanjeev.signinapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
, View.OnClickListener{

    private static final int SIGNED_IN = 0;
    private static final int STATE_SIGNING_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN = 0;

    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;

    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSignInButton = findViewById(R.id.sign_in_button);
        mSignOutButton = findViewById(R.id.sign_out_button);
        mRevokeButton = findViewById(R.id.revoke_access_button);
        mTextView = findViewById(R.id.status_label);

        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mRevokeButton.setOnClickListener(this);
        mTextView.setOnClickListener(this);

        mGoogleApiClient = buildGoogleApiClient();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(mSignInProgress != STATE_IN_PROGRESS){
            mSignInIntent = connectionResult.getResolution();
            if(mSignInProgress == STATE_SIGNING_IN){
                resolveSignInError();
            }
        }
        onSignedOut();
    }

    private void onSignedOut() {
        mTextView.setText("Signed Out");
    }

    private void resolveSignInError() {
        if(mSignInIntent != null){
            try{
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            }catch (IntentSender.SendIntentException e){
                mSignInProgress = STATE_SIGNING_IN;
                mGoogleApiClient.connect();

            }
        }
        else{
            // Server error
        }
    }

    @Override
    public void onClick(View v) {
        if(!mGoogleApiClient.isConnecting()){
            switch (v.getId()){
                case R.id.sign_in_button :
                    mTextView.setText("Signing in");
                    resolveSignInError();
                    break;
                case R.id.sign_out_button :
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                    break;
                case R.id.revoke_access_button :
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                    mGoogleApiClient = buildGoogleApiClient();
                    mGoogleApiClient.connect();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RC_SIGN_IN :
                if(resultCode == RESULT_OK){
                    mSignInProgress = STATE_SIGNING_IN;
                }
                else{
                    mSignInProgress = SIGNED_IN;
                }

                if(!mGoogleApiClient.isConnecting()){
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mSignInProgress = SIGNED_IN;

        try{
            String emailAddress = Plus.AccountApi.getAccountName(mGoogleApiClient);
            mTextView.setText(String.format("Signed in as %s", emailAddress));
        }catch (Exception e){
            String exception = e.getLocalizedMessage();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(new Scope("email"))
                .build();
    }
}
