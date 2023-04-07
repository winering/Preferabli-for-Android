package com.ringit.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.Arrays;
import classes.API_PreferabliException;
import classes.API_ResultHandler;
import classes.Object_Customer;
import classes.Object_PreferabliUser;
import classes.Object_Product;
import classes.Preferabli;

public class MainActivity extends Activity implements RecyclerViewAdapter.LongClickListener, AdapterView.OnItemSelectedListener {

    private ImageView preferabliLogo;
    private TextView second;
    private Button customerButton;
    private EditText email;
    private EditText password;
    private Button submit;
    private Spinner authenticatedActions;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private boolean customer = true;
    private ArrayList<Object_Product> products = new ArrayList<>();
    private ArrayList<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferabliLogo = findViewById(R.id.preferabliLogo);
        second = findViewById(R.id.second);
        customerButton = findViewById(R.id.customerButton);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        authenticatedActions = findViewById(R.id.spinner2);
        recyclerView = findViewById(R.id.recyclerView);

        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"View Unauthenticated Actions", "Search", "Label Rec", "Guided Rec", "Where to Buy", "Like That, Try This"};
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(spinAdapter);
        dropdown.setOnItemSelectedListener(this);

        authenticatedActions = findViewById(R.id.spinner2);
        String[] items2 = new String[]{"View Authenticated Actions", "Rate Product", "Wishlist Product", "Get Profile", "Get Recs", "Get Foods", "Get Rated Products", "Get Wishlisted Products", "Get Purchased Products"};
        ArrayAdapter<String> spinAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        authenticatedActions.setAdapter(spinAdapter2);
        authenticatedActions.setOnItemSelectedListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, this);
        recyclerView.setAdapter(adapter);

        preferabliLogo.setImageDrawable(Preferabli.getPoweredByPreferabliLogo(false));

        handleViews();
    }

    public void customerButtonClicked(View view) {
        customer = !customer;
        handleViews();
    }

    public void handleViews() {
        if (Preferabli.isPreferabliUserLoggedIn() || Preferabli.isCustomerLoggedIn()) {
            email.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            submit.setText("LOGOUT");
            second.setText("You are now browsing as an authenticated " + (Preferabli.isPreferabliUserLoggedIn() ? "Preferabli ser" : "customer") +  ". You can log them out below.");
            customerButton.setVisibility(View.GONE);
            authenticatedActions.setVisibility(View.VISIBLE);
        } else {
            email.setVisibility(View.VISIBLE);
            password.setVisibility(customer ? View.GONE : View.VISIBLE);
            email.setHint(customer ? "Customer ID (Email or Phone)" : "Preferabli User Email");
            submit.setText("SUBMIT");
            second.setText("To unlock additional actions, link a customer or login an existing Preferabli user...");
            customerButton.setText(customer ? "Link a Customer" : "Preferabli User Login");
            customerButton.setVisibility(View.VISIBLE);
            authenticatedActions.setVisibility(View.GONE);
        }
    }

    public void unauthenticatedActionsClicked(View view) {

    }

    public void authenticatedActionsClicked(View view) {

    }

    public void submitClicked(View view) {
        showLoadingView();
        hideKeyboard();

        if (Preferabli.isCustomerLoggedIn() || Preferabli.isPreferabliUserLoggedIn()) {
            Preferabli.main().logout(new API_ResultHandler<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    products.clear();
                    items = new ArrayList<>(Arrays.asList("Logged out."));
                    adapter.updateData(items);
                    hideLoadingView();
                    handleViews();
                }

                @Override
                public void onFailure(API_PreferabliException e) {
                    hideLoadingView();
                    showSnackbar(e.getMessage());
                }
            });
        } else if (customer) {
            Preferabli.main().loginCustomer(email.getText().toString(), "123ABC", new API_ResultHandler<Object_Customer>() {
                @Override
                public void onSuccess(Object_Customer data) {
                    products.clear();
                    items = new ArrayList<>(Arrays.asList("Customer logged in.", "Display Name: " + data.getName()));
                    adapter.updateData(items);
                    hideLoadingView();
                    handleViews();
                }

                @Override
                public void onFailure(API_PreferabliException e) {
                    hideLoadingView();
                    showSnackbar(e.getMessage());
                }
            });
        } else {
            Preferabli.main().loginPreferabliUser(email.getText().toString(), password.getText().toString(), new API_ResultHandler<Object_PreferabliUser>() {
                @Override
                public void onSuccess(Object_PreferabliUser data) {
                    products.clear();
                    items = new ArrayList<>(Arrays.asList("Preferabli User logged in.", "Display Name: " + data.getDisplayName()));
                    adapter.updateData(items);
                    hideLoadingView();
                    handleViews();
                }

                @Override
                public void onFailure(API_PreferabliException e) {
                    hideLoadingView();
                    showSnackbar(e.getMessage());
                }
            });
        }
    }

    public void showLoadingView() {
        findViewById(R.id.progressRL).setVisibility(View.VISIBLE);
    }

    public void hideLoadingView() {
        findViewById(R.id.progressRL).setVisibility(View.GONE);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) view = new View(this);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}