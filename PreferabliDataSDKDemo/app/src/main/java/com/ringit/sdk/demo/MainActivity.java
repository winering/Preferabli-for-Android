package com.ringit.sdk.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.IOException;

import api.WRException;
import journal.JournalTools;
import searchwines.CameraPreview;
import tools.WRTools;
import tools.WineRing;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailField;
    private EditText passwordField;
    private RelativeLayout progressRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        progressRL = findViewById(R.id.progressRL);
        loginButton = findViewById(R.id.loginButton);

        if (WRTools.isUserLoggedIn()) {
            userIsLoggedIn();
        } else {
            userIsLoggedOut();
        }
    }

    public void userIsLoggedIn() {
        loginButton.setText("Logout");
        emailField.setVisibility(View.GONE);
        passwordField.setVisibility(View.GONE);
    }

    public void userIsLoggedOut() {
        loginButton.setText("Login");
        emailField.setVisibility(View.VISIBLE);
        passwordField.setVisibility(View.VISIBLE);
    }

    public void loginClicked(View view) {
        if (WRTools.isUserLoggedIn()) {
            new Logout().execute();
        } else {
            new Login().execute();
        }
    }

    public void getRecsClicked(View view) {
        WineRing.main().presentFragmentInDialog(getSupportFragmentManager(), WineRing.main().getGetRecsFragment());
    }

    public void journalClicked(View view) {
        WineRing.main().presentFragmentInDialog(getSupportFragmentManager(), WineRing.main().getJournalFragment());
    }

    public void getRateWinesClicked(View view) {
        WineRing.main().presentFragmentInDialog(getSupportFragmentManager(), WineRing.main().getRateWinesFragment());
    }

    public void getManageCellarsClicked(View view) {
        WineRing.main().presentFragmentInDialog(getSupportFragmentManager(), WineRing.main().getManageCellarsFragment());
    }

    public void getPreferencesClicked(View view) {
        WineRing.main().presentFragmentInDialog(getSupportFragmentManager(), WineRing.main().getPreferencesFragment());
    }

    public void getConnectionsClicked(View view) {
        WineRing.main().presentFragmentInDialog(getSupportFragmentManager(), WineRing.main().getConnectionsFragment());
    }

    public void getChannelsClicked(View view) {
        WineRing.main().presentFragmentInDialog(getSupportFragmentManager(), WineRing.main().getChannelsFragment());
    }

    public class Login extends AsyncTask<Void, Void, Exception> {

        private String email;
        private String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WRTools.hideKeyboard(MainActivity.this);
            progressRL.setVisibility(View.VISIBLE);
            email = emailField.getText().toString();
            password = passwordField.getText().toString();
        }

        @Override
        protected Exception doInBackground(Void... passed) {

            try {
                WineRing.main().login(email, password);
                return null;
            } catch (WRException | IOException | InterruptedException e) {
                // failed so return the exception
                return e;
            }
        }

        @Override
        protected void onPostExecute(Exception exception) {
            progressRL.setVisibility(View.GONE);
            if (exception == null) {
                // Login was a success
                userIsLoggedIn();
            } else {
                // Login failed show an error message
                WRTools.showSnackBar(findViewById(android.R.id.content).getRootView(), exception.getMessage(), true);
            }
        }
    }

    public class Logout extends AsyncTask<Void, Void, Exception> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WRTools.hideKeyboard(MainActivity.this);
            progressRL.setVisibility(View.VISIBLE);
        }

        @Override
        protected Exception doInBackground(Void... passed) {

            try {
                WineRing.main().logout();
                return null;
            } catch (WRException e) {
                // failed so return the exception
                return e;
            }
        }

        @Override
        protected void onPostExecute(Exception exception) {
            progressRL.setVisibility(View.GONE);
            if (exception == null) {
                // Login was a success
                userIsLoggedOut();
            } else {
                // Login failed show an error message
                WRTools.showSnackBar(findViewById(android.R.id.content).getRootView(), exception.getMessage(), true);
            }
        }
    }
}