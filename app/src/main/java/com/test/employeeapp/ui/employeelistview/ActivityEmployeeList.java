package com.test.employeeapp.ui.employeelistview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.employeeapp.R;
import com.test.employeeapp.data.local.SQLiteDBHelper;
import com.test.employeeapp.data.model.EmployeeListModel;
import com.test.employeeapp.data.model.EmployeeListResponseModel;
import com.test.employeeapp.data.retrofit.APIClient;
import com.test.employeeapp.data.retrofit.APIInterface;
import com.test.employeeapp.databinding.ActivityEmployeeHomeBinding;
import com.test.employeeapp.ui.employeedetails.ActivityEmployeeDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEmployeeList extends AppCompatActivity implements
        EmployeeListAdapter.OnEmployeeSelectedListener {

    ActivityEmployeeHomeBinding binding;
    SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(this);
    EmployeeListAdapter.OnEmployeeSelectedListener onEmployeeSelectedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_home);

        onEmployeeSelectedListener = this;

        setUpTextWatcher();

        getEmployeesList();

        binding.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etSearch.setText("");
                hideKeyboard();
            }
        });

        binding.tvErrorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvErrorText.setVisibility(View.GONE);
                binding.pbEmployeeList.setVisibility(View.VISIBLE);
                getEmployeesList();
            }
        });
    }

    private void getEmployeesList() {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        if (sqLiteDBHelper.getAllEmployees().size() == 0) {

            Call<ArrayList<EmployeeListResponseModel>> call1 = apiInterface.getEmployeeList();
            call1.enqueue(new Callback<ArrayList<EmployeeListResponseModel>>() {

                @Override
                public void onResponse(Call<ArrayList<EmployeeListResponseModel>> call,
                                       Response<ArrayList<EmployeeListResponseModel>> response) {
                    ArrayList<EmployeeListResponseModel> list = response.body();
                    for (EmployeeListResponseModel model : list) {
                        sqLiteDBHelper.addEmployee(model);
                    }

                    binding.rlEmployeeList.setVisibility(View.VISIBLE);
                    binding.rlSearchView.setVisibility(View.VISIBLE);
                    binding.pbEmployeeList.setVisibility(View.GONE);

                    setEmployeeAdapter(sqLiteDBHelper.getAllEmployees());
                }

                @Override
                public void onFailure(Call<ArrayList<EmployeeListResponseModel>> call, Throwable t) {
                    call.cancel();
                    binding.tvErrorText.setVisibility(View.VISIBLE);
                    binding.pbEmployeeList.setVisibility(View.GONE);
                }
            });
        } else {
            binding.rlEmployeeList.setVisibility(View.VISIBLE);
            binding.rlSearchView.setVisibility(View.VISIBLE);
            binding.pbEmployeeList.setVisibility(View.GONE);
            setEmployeeAdapter(sqLiteDBHelper.getAllEmployees());
        }
    }

    private void setEmployeeAdapter(ArrayList<EmployeeListModel> employeeList) {
        if (employeeList != null) {
            EmployeeListAdapter adapter = new EmployeeListAdapter(this, employeeList,
                    onEmployeeSelectedListener);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            binding.rcvEmployees.setLayoutManager(mLayoutManager);
            binding.rcvEmployees.setItemAnimator(new DefaultItemAnimator());
            binding.rcvEmployees.setAdapter(adapter);
        }
    }

    private void setUpTextWatcher() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    setEmployeeAdapter(sqLiteDBHelper.getAllEmployees());
                    binding.ivClear.setVisibility(View.GONE);
                    hideKeyboard();
                }
                else {
                    binding.ivClear.setVisibility(View.VISIBLE);
                    setEmployeeAdapter(sqLiteDBHelper.search(s.toString()));
                }
            }
        });
    }

    @Override
    public void onEmployeeSelected(int position) {
        Intent intent = new Intent(this, ActivityEmployeeDetails.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = getCurrentFocus();

        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
