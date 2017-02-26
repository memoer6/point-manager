package com.memoer6.pointreader4.view;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.memoer6.pointreader4.R;
import com.memoer6.pointreader4.model.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<Transaction> transactionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView points, date, description;

        public MyViewHolder(View view) {
            super(view);
            points = (TextView) view.findViewById(R.id.transactionPoints);
            date = (TextView) view.findViewById(R.id.transactionDate);
            description = (TextView) view.findViewById(R.id.transactionDescription);
        }
    }


    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.points.setText(String.valueOf(transaction.getValue()));
        holder.date.setText(transaction.getDate());
        holder.description.setText(transaction.getDescription());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

}
