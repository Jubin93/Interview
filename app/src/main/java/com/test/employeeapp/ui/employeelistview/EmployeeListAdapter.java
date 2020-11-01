package com.test.employeeapp.ui.employeelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.test.employeeapp.BR;
import com.test.employeeapp.R;
import com.test.employeeapp.data.model.EmployeeListModel;
import com.test.employeeapp.databinding.ItemEmployeeListBinding;

import java.util.ArrayList;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter
        .EmployeeListViewHolder>{
    Context context;
    ArrayList<EmployeeListModel> employeeListModels;
    OnEmployeeSelectedListener onEmployeeSelectedListener;

    EmployeeListAdapter(Context context, ArrayList<EmployeeListModel> employeeListModels
            ,OnEmployeeSelectedListener onEmployeeSelectedListener) {
        this.context = context;
        this.employeeListModels = employeeListModels;
        this.onEmployeeSelectedListener = onEmployeeSelectedListener;
    }

    @NonNull
    @Override
    public EmployeeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEmployeeListBinding itemEmployeeListBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_employee_list, parent, false);
        return new EmployeeListViewHolder(itemEmployeeListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListViewHolder holder, int position) {
        EmployeeListModel dataModel = employeeListModels.get(position);
        holder.itemEmployeeListBinding.tvEmployeeName.setText(dataModel.getName());
        holder.itemEmployeeListBinding.tvCompanyName.setText(dataModel.getCompanyName());
        Glide.with(context).load(dataModel.getProfileImage()).apply(new RequestOptions()
                .placeholder(R.drawable.no_image_placeholder))
                .into(holder.itemEmployeeListBinding.ivProfilePicture);
        holder.itemEmployeeListBinding.llItemEmployee
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEmployeeSelectedListener.onEmployeeSelected(dataModel.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeListModels.size();
    }

    public class EmployeeListViewHolder extends RecyclerView.ViewHolder {
        ItemEmployeeListBinding itemEmployeeListBinding;

        public EmployeeListViewHolder(ItemEmployeeListBinding itemEmployeeListBinding) {
            super(itemEmployeeListBinding.getRoot());
            this.itemEmployeeListBinding = itemEmployeeListBinding;
        }

    }

    interface OnEmployeeSelectedListener {
        void onEmployeeSelected(int position);
    }
}
