package com.uki121.pooni;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SheetAdapter extends RecyclerView.Adapter <SheetAdapter.ViewHolder> {
    private ArrayList<SheetItem> answerList;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public RadioGroup rbGroup;
        public RadioButton btn1, btn2, btn3, btn4, btn5;
        public ViewHolder(TextView view) {
            super(view);
            mTextView = (TextView)view.findViewById(R.id.ans_number);
            rbGroup = (RadioGroup) view.findViewById(R.id.ans_radio_group);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public SheetAdapter(Context _context, ArrayList<SheetItem> _answerList) {
        context = _context;
        answerList = _answerList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SheetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer_sheet, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(answerList.get(position).getNumber());

        holder.rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                answerList.get(position).setAnswer(checkedId);
            }
        });
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(String.valueOf(answerList.get(position).getAnswer())));
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.answerList.size();
    }
}
