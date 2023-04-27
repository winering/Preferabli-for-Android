package com.ringit.sdk.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import classes.API_PreferabliException;
import classes.API_ResultHandler;
import classes.Object_Customer;
import classes.Object_Food;
import classes.Object_GuidedRec;
import classes.Object_LabelRecResults;
import classes.Object_PreferabliUser;
import classes.Object_PreferenceData;
import classes.Object_Product;
import classes.Object_Profile;
import classes.Object_Recommendation;
import classes.Object_Tag;
import classes.Object_Variant;
import classes.Object_WhereToBuy;
import classes.Preferabli;

public class MainActivity extends Activity implements RecyclerViewAdapter.ShouldWeShowListener, AdapterView.OnItemSelectedListener {

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
        String[] items2 = new String[]{"View Authenticated Actions", "Rate Product", "Wishlist Product", "Get Profile", "Get Recs", "Get Foods", "Get Rated Products", "Get Wishlisted Products", "Get Purchased Products", "Get Customer"};
        ArrayAdapter<String> spinAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        authenticatedActions.setAdapter(spinAdapter2);
        authenticatedActions.setOnItemSelectedListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        registerForContextMenu(recyclerView);
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
            second.setText("You are now browsing as an authenticated " + (Preferabli.isPreferabliUserLoggedIn() ? "Preferabli ser" : "customer") + ". You can log them out below.");
            customerButton.setVisibility(View.GONE);
            authenticatedActions.setVisibility(View.VISIBLE);
        } else {
            email.setVisibility(View.VISIBLE);
            password.setVisibility(customer ? View.GONE : View.VISIBLE);
            email.setHint(customer ? "Customer ID (Email or Phone)" : "Preferabli User Email");
            submit.setText("SUBMIT");
            second.setText("To unlock additional actions, link a customer...");
            customerButton.setVisibility(View.GONE);
            authenticatedActions.setVisibility(View.GONE);
        }
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

    public void searchProducts() {
        showLoadingView();

        Preferabli.main().searchProducts("wine", true, null, null, true, new API_ResultHandler<ArrayList<Object_Product>>() {
            @Override
            public void onSuccess(ArrayList<Object_Product> data) {
                products = data;
                items = new ArrayList<>(products.stream().map(x -> x.getName()).collect(Collectors.toList()));
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

    public void getCustomer() {
        Preferabli.main().getCustomer(null, new API_ResultHandler<Object_Customer>() {
            @Override
            public void onSuccess(Object_Customer data) {
                products.clear();
                items = new ArrayList<>(Arrays.asList("Got the customer.", "Display Name: " + data.getName()));
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

    public void labelRec() {
        showLoadingView();
        Preferabli.main().labelRecognition(getExampleAsFile(), true, new API_ResultHandler<Object_LabelRecResults>() {
            @Override
            public void onSuccess(Object_LabelRecResults results) {
                ArrayList<Object_LabelRecResults.Object_LabelRecResult> data = results.getResults();
                products = new ArrayList<>(data.stream().map(x -> x.getProduct()).collect(Collectors.toList()));
                items = new ArrayList<>(products.stream().map(x -> x.getName()).collect(Collectors.toList()));
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

    public void guidedRec() {
        showLoadingView();
        Preferabli.main().getGuidedRec(Object_GuidedRec.WINE_DEFAULT, new API_ResultHandler<Object_GuidedRec>() {
            @Override
            public void onSuccess(Object_GuidedRec data) {
                ArrayList<Long> selected_choice_ids = new ArrayList<>();
                for (Object_GuidedRec.Object_GuidedRecQuestion question : data.getQuestions()) {
                    if (question.getChoices().size() > 0) {
                        selected_choice_ids.add(question.getChoices().get(new Random().nextInt(question.getChoices().size())).getId());
                    }
                }
                Preferabli.main().getGuidedRecResults(data.getId(), selected_choice_ids, null, null, Preferabli.PRIMARY_INVENTORY_ID, true, new API_ResultHandler<ArrayList<Object_Product>>() {
                    @Override
                    public void onSuccess(ArrayList<Object_Product> data) {
                        products = data;
                        items = new ArrayList<>(products.stream().map(x -> x.getName()).collect(Collectors.toList()));
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

            @Override
            public void onFailure(API_PreferabliException e) {
                hideLoadingView();
                showSnackbar(e.getMessage());
            }
        });
    }

    public void whereToBuy() {
        showLoadingView();

        Preferabli.main().getPreferabliProductId(null, "107811", new API_ResultHandler<Long>() {
            @Override
            public void onSuccess(Long data) {
                Preferabli.main().whereToBuy(data, null, null, null, new API_ResultHandler<Object_WhereToBuy>() {
                    @Override
                    public void onSuccess(Object_WhereToBuy data) {
                        products.clear();
                        if (data.getLinks().size() > 0) {
                            items = new ArrayList<>(data.getLinks().stream().map(x -> x.getProductName()).collect(Collectors.toList()));
                        } else {
                            items = new ArrayList<>(data.getVenues().stream().map(x -> x.getName()).collect(Collectors.toList()));
                        }
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

            @Override
            public void onFailure(API_PreferabliException e) {
                hideLoadingView();
                showSnackbar(e.getMessage());
            }
        });
    }

    public void lttt() {
        showLoadingView();

        Preferabli.main().lttt(11263, null, null, null, new API_ResultHandler<ArrayList<Object_Product>>() {
            @Override
            public void onSuccess(ArrayList<Object_Product> data) {
                products = data;
                items = new ArrayList<>(products.stream().map(x -> x.getName()).collect(Collectors.toList()));
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

    public void getRecs() {
        showLoadingView();

        Preferabli.main().getRecs(Object_Product.Other_ProductCategory.WINE, Object_Product.Other_ProductType.RED, Preferabli.PRIMARY_INVENTORY_ID, null, null, null, null, null, new API_ResultHandler<Object_Recommendation>() {
            @Override
            public void onSuccess(Object_Recommendation data) {
                products = data.getProducts();
                items = new ArrayList<>(products.stream().map(x -> x.getName()).collect(Collectors.toList()));
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

    public void getFoods() {
        showLoadingView();

        Preferabli.main().getFoods(new API_ResultHandler<ArrayList<Object_Food>>() {
            @Override
            public void onSuccess(ArrayList<Object_Food> data) {
                products.clear();
                items = new ArrayList<>(data.stream().map(x -> x.getName()).collect(Collectors.toList()));
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

    public void getRatedProducts() {
        showLoadingView();

        Preferabli.main().getRatedProducts(null, null, new API_ResultHandler<ArrayList<Object_Product>>() {
            @Override
            public void onSuccess(ArrayList<Object_Product> data) {
                products = data;
                items = new ArrayList<>(products.stream().map(x -> x.getName()).collect(Collectors.toList()));
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

    public void getPurchasedProducts() {
        showLoadingView();

        Preferabli.main().getPurchasedProducts(null, null, null, new API_ResultHandler<ArrayList<Object_Product>>() {
            @Override
            public void onSuccess(ArrayList<Object_Product> data) {
                products = data;
                items = new ArrayList<>(products.stream().map(x -> x.getName()).collect(Collectors.toList()));
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

    public void getProfile() {
        showLoadingView();

        Preferabli.main().getProfile(null, new API_ResultHandler<Object_Profile>() {
            @Override
            public void onSuccess(Object_Profile data) {
                products.clear();
                items = new ArrayList<>(data.getProfileStyles().stream().map(x -> x.getName()).collect(Collectors.toList()));
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

    public void rateProduct() {
        showLoadingView();

        Preferabli.main().rateProduct(11263, Object_Variant.CURRENT_VARIANT_YEAR, Object_Tag.Other_RatingLevel.SOSO, null, null, null, null, null, new API_ResultHandler<Object_Product>() {
            @Override
            public void onSuccess(Object_Product data) {
                products.clear();
                products.add(data);
                items = new ArrayList<>(products.stream().map(x -> x.getName()).collect(Collectors.toList()));
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

    public void wishlistProduct() {
        showLoadingView();

        Preferabli.main().wishlistProduct(11263, Object_Variant.CURRENT_VARIANT_YEAR, null, null, null, null, null, new API_ResultHandler<Object_Product>() {
            @Override
            public void onSuccess(Object_Product data) {
                products.clear();
                products.add(data);
                items = new ArrayList<>(products.stream().map(x -> x.getName()).collect(Collectors.toList()));
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

    public File getExampleAsFile() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.label_rec_example);

        File imageFile = new File(getExternalCacheDir(), "label_rec_example.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);

            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();

            return imageFile;

        } catch (IOException e) {
            Log.e("app", e.getMessage());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            return null;
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == authenticatedActions) {
            if (i == 1) {
                rateProduct();
            } else if (i == 2) {
                wishlistProduct();
            } else if (i == 3) {
                getProfile();
            } else if (i == 4) {
                getRecs();
            } else if (i == 5) {
                getFoods();
            } else if (i == 6) {
                getRatedProducts();
            } else if (i == 8) {
                getPurchasedProducts();
            } else if (i == 9) {
                getCustomer();
            }
        } else {
            if (i == 1) {
                searchProducts();
            } else if (i == 2) {
                labelRec();
            } else if (i == 3) {
                guidedRec();
            } else if (i == 4) {
                whereToBuy();
            } else if (i == 5) {
                lttt();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = item.getGroupId();
        Object_Product product = products.get(position);

        switch (item.getItemId()) {
            case R.id.wtb:
                showLoadingView();
                product.whereToBuy(null, null, null, new API_ResultHandler<Object_WhereToBuy>() {
                    @Override
                    public void onSuccess(Object_WhereToBuy data) {
                        products.clear();
                        if (data.getLinks().size() > 0) {
                            items = new ArrayList<>(data.getLinks().stream().map(x -> x.getProductName()).collect(Collectors.toList()));
                        } else {
                            items = new ArrayList<>(data.getVenues().stream().map(x -> x.getName()).collect(Collectors.toList()));
                        }
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

                return true;
            case R.id.wishlist:
                // nada

                return true;
            case R.id.rate:
                showLoadingView();
                product.rate(Object_Tag.Other_RatingLevel.SOSO, null, null, null, null, null, new API_ResultHandler<Object_Product>() {
                    @Override
                    public void onSuccess(Object_Product data) {
                        products.clear();
                        products.add(data);
                        items = new ArrayList<>(products.stream().map(x -> x.getName()).collect(Collectors.toList()));
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
                return true;
            case R.id.lttt:
                showLoadingView();
                product.lttt(null, null, new API_ResultHandler<ArrayList<Object_Product>>() {
                    @Override
                    public void onSuccess(ArrayList<Object_Product> data) {
                        products = data;
                        items = new ArrayList<>(products.stream().map(x -> x.getName()).collect(Collectors.toList()));
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
                return true;
            case R.id.score:
                showLoadingView();
                product.getPreferabliScore(new API_ResultHandler<Object_PreferenceData>() {
                    @Override
                    public void onSuccess(Object_PreferenceData data) {
                        products.clear();
                        items = new ArrayList<>();
                        items.add(data.toString());

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
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean shouldWeShow(int position) {
        if (products.size() > 0 && products.size() > position) {
            return true;
        }
        return false;
    }
}