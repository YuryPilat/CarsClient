package com.example.carclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carclient.managers.HttpManager;
import com.example.carclient.models.BaseModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final String YEAR = "Year";
    private final String CLASSES = "Classes";
    private final String CITY = "City";
    private final String DEALER = "Dealer";
    private final String FIRST_NAME = "FirstName";
    private final String SECOND_NAME = "SecondName";
    private final String LAST_NAME = "LastName";
    private final String PHONE = "Phone";
    private final String VIN = "VIN";
    private final String EMAIL = "Email";
    private final String CHOSEN = "isChosen";

    private EditText chosen_year;
    private EditText chosen_classes;
    private EditText chosen_city;
    private EditText chosen_dealer;
    private EditText first_name;
    private EditText second_name;
    private EditText last_name;
    private EditText phone_number;
    private EditText vin_number;
    private EditText email;
    private String _validationError;
    Spinner spinnerDealer;
    HttpManager httpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setScreen();
        httpManager = new HttpManager();

        setYears();
        getCities();
        getClasses();
        getDealers();

        spinnerDealer = findViewById(R.id.spinner_dealer);
        spinnerDealer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(chosen_city.getText().toString().equals(getResources().getString(R.string.hint_city))){
                        Toast.makeText(getApplicationContext(), R.string.toast_choose_city, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    else {
                        v.performClick();
                    }
                }return true;
            }
        });
    }

    private void getCities() {
        retrofit2.Call<List<BaseModel>> cities = httpManager.serverApi.getCities();
        cities.enqueue(new Callback<List<BaseModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<BaseModel>> call, @NonNull Response<List<BaseModel>> response) {
                List<BaseModel> recievedData = response.body();
                if (recievedData != null) {
                    ArrayList<String> array_cities = new ArrayList<>();
                    array_cities.add(getResources().getString(R.string.hint_city));
                    for (int i =0; i<recievedData.size(); i++) {
                        array_cities.add(recievedData.get(i).name);
                    }
                    setAdapterAndSpinner(R.id.spinner_city, chosen_city, CITY, array_cities);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BaseModel>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.ERROR_MSG, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getClasses() {
        retrofit2.Call<List<BaseModel>> cities = httpManager.serverApi.getClasses();
        cities.enqueue(new Callback<List<BaseModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<BaseModel>> call, @NonNull Response<List<BaseModel>> response) {
                List<BaseModel> recievedData = response.body();
                if (recievedData != null) {
                    ArrayList<String> array_classes = new ArrayList<>();
                    array_classes.add(getResources().getString(R.string.hint_clas));
                    for (int i =0; i<recievedData.size(); i++) {
                        array_classes.add(recievedData.get(i).name);
                    }
                    setAdapterAndSpinner(R.id.spinner_clas, chosen_classes, CLASSES, array_classes);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BaseModel>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.ERROR_MSG, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDealers() {
        retrofit2.Call<List<BaseModel>> cities = httpManager.serverApi.getDealers();
        cities.enqueue(new Callback<List<BaseModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<BaseModel>> call, @NonNull Response<List<BaseModel>> response) {
                List<BaseModel> recievedData = response.body();
                if (recievedData != null) {
                    ArrayList<String> array_dealers = new ArrayList<>();
                    array_dealers.add(getResources().getString(R.string.hint_dealer));
                    for (int i =0; i<recievedData.size(); i++) {
                        array_dealers.add(recievedData.get(i).name);
                    }
                    setAdapterAndSpinner(R.id.spinner_dealer, chosen_dealer, DEALER, array_dealers);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BaseModel>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.ERROR_MSG, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (IsDataValid()){
                    saveInputs();
                    Toast.makeText(this, R.string.SENT_OK, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, _validationError, Toast.LENGTH_LONG).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setScreen() {
        chosen_year = findViewById(R.id.chosen_year);
        chosen_classes = findViewById(R.id.classes);
        chosen_city = findViewById(R.id.cities);
        chosen_dealer = findViewById(R.id.dealer);
        first_name = findViewById(R.id.first_name);
        second_name = findViewById(R.id.second_name);
        last_name = findViewById(R.id.last_name);
        phone_number = findViewById(R.id.phone_number);
        vin_number = findViewById(R.id.vin_number);
        email = findViewById(R.id.email);

        TextWatcher textWatcher = new TextWatcher() {
            private boolean mFormatting;
            private int mAfter;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mAfter = after;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mFormatting) {
                    mFormatting = true;
                    if(mAfter!=0)
                    {
                        String num = s.toString();
                        String data = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            data = PhoneNumberUtils.formatNumber(num, "RU");
                        }
                        if(data!=null)
                        {
                            s.clear();
                            s.append(data);
                        }
                    }
                    mFormatting = false;
                }
            }
        };

        phone_number.addTextChangedListener(textWatcher);

        chosen_year.setText(Root.getString(YEAR, ""));
        chosen_classes.setText(Root.getString(CLASSES, ""));
        chosen_city.setText(Root.getString(CITY, ""));
        chosen_dealer.setText(Root.getString(DEALER, ""));
        first_name.setText(Root.getString(FIRST_NAME, ""));
        second_name.setText(Root.getString(SECOND_NAME, ""));
        last_name.setText(Root.getString(LAST_NAME, ""));
        phone_number.setText(Root.getString(PHONE, ""));
        vin_number.setText(Root.getString(VIN, ""));
        email.setText(Root.getString(EMAIL, ""));
        chosen_classes.setText(Root.getString(CLASSES, ""));
        chosen_city.setText(Root.getString(CITY, ""));
        chosen_dealer.setText(Root.getString(DEALER, ""));
    }

    private void setYears() {
        ArrayList<String> array_years = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        array_years.add(getResources().getString(R.string.hint_year));
        array_years.add(String.valueOf(year-2));
        for (int i=0; i <= 15; i++){
            array_years.add(String.valueOf(year - i));
        }
        setAdapterAndSpinner(R.id.spinner_year, chosen_year, YEAR, array_years);
    }

    private void setAdapterAndSpinner(int spinnerId, final EditText field, final String setText, final ArrayList<String> arrayList) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinnerYear = findViewById(spinnerId);
        spinnerYear.setAdapter(adapter);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0) {
                    Root.setString(setText, arrayList.get(position));
                    Root.setBoolean(setText + CHOSEN, false);
                    field.setText(Root.getString(setText, ""));
                } else {
                    Root.setString(setText, arrayList.get(position));
                    Root.setBoolean(setText+CHOSEN, true);
                    field.setText(Root.getString(setText, ""));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private boolean IsDataValid() {
        if (checkFieldsFill() && checkSpinnersFill()) {
            if(isValidEmailId(email.getText().toString())){
                return true;
            } else {
                _validationError = getResources().getString(R.string.invalid_email);
                email.findFocus();
            }
        } else {
            _validationError = getResources().getString(R.string.empty_fields);
        }
        return false;
    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)" +
                "|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$").matcher(email).matches();
    }

    private boolean checkFieldsFill() {
        EditText[] fields = {last_name,
                first_name,
                second_name,
                phone_number,
                email,
                vin_number,
                };
        for (EditText field : fields) {
            if (field.getText().toString().length() <= 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkSpinnersFill() {
        String [] fields = {YEAR, CITY, CLASSES, DEALER };
        for (String field : fields) {
            if (!Root.getBoolean(field + CHOSEN, false)) {
                return false;
            }
        }
        return true;
    }

    private void saveInputs() {
        Root.setString(LAST_NAME, last_name.getText().toString());
        Root.setString(FIRST_NAME, first_name.getText().toString());
        Root.setString(SECOND_NAME, second_name.getText().toString());
        Root.setString(PHONE, phone_number.getText().toString());
        Root.setString(EMAIL, email.getText().toString());
        Root.setString(VIN, vin_number.getText().toString());
    }

}
