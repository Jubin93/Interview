package com.test.employeeapp.ui.employeedetails;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.test.employeeapp.R;
import com.test.employeeapp.data.local.SQLiteDBHelper;
import com.test.employeeapp.data.model.EmployeeDetailsModel;
import com.test.employeeapp.databinding.ActivityEmployeeDetailsBinding;

public class ActivityEmployeeDetails extends AppCompatActivity {

    ActivityEmployeeDetailsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_details);

        int id = getIntent().getIntExtra("position", 0);

        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(this);
        EmployeeDetailsModel employeeDetailsModel = sqLiteDBHelper.getEmployeeDetails(id);

        setData(employeeDetailsModel);

        binding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setData(EmployeeDetailsModel employeeDetailsModel){
        Glide.with(this).load(employeeDetailsModel.getProfileImage())
                .apply(new RequestOptions().placeholder(R.drawable.no_image_placeholder))
                .into(binding.ivEmployeeImage);
        binding.tvEmployeeName.setText(employeeDetailsModel.getName());
        binding.tvEmployeeUsername.setText(employeeDetailsModel.getUsername());
        binding.tvEmployeeEmail.setText(employeeDetailsModel.getEmail());
        binding.tvEmployeePhone.setText(employeeDetailsModel.getPhone());
        binding.tvEmployeeAddress.setText(employeeDetailsModel.getStreet() + " , " +
                employeeDetailsModel.getSuite() + " , " + employeeDetailsModel.getCity() + " , " +
                employeeDetailsModel.getZipcode() + " , " + employeeDetailsModel.getLat() + " , " +
                employeeDetailsModel.getLng());
        binding.tvEmployeeWebsite.setText(employeeDetailsModel.getWebsite());
        binding.tvEmployeeCompany.setText(employeeDetailsModel.getCompanyName() + " , " +
                employeeDetailsModel.getCatchPhrase() + " , " + employeeDetailsModel.getBs());
    }
}
