package com.example.vishwasdamle.quicknote.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.vishwasdamle.quicknote.R;

import java.util.ArrayList;

public class ButtonAdaptor extends BaseAdapter {
    private Context context;
    private ArrayList<String> numbers;

    public ButtonAdaptor(Context context) {
        this.context = context;
        numbers = new ArrayList<>();
        numbers.add(context.getResources().getString(R.string.number1));
        numbers.add(context.getResources().getString(R.string.number2));
        numbers.add(context.getResources().getString(R.string.number3));
        numbers.add(context.getResources().getString(R.string.number4));
        numbers.add(context.getResources().getString(R.string.number5));
        numbers.add(context.getResources().getString(R.string.number6));
        numbers.add(context.getResources().getString(R.string.number7));
        numbers.add(context.getResources().getString(R.string.number8));
        numbers.add(context.getResources().getString(R.string.number9));
        numbers.add(context.getResources().getString(R.string.numberNegative));
        numbers.add(context.getResources().getString(R.string.number0));
        numbers.add(context.getResources().getString(R.string.numberFloatingPoint));
    }

    @Override
    public int getCount() {
        return numbers.size();
    }

    @Override
    public Object getItem(int i) {
        return numbers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_num_pad_button, viewGroup, false);
        }
        Button key = (Button) view.findViewById(R.id.numPadKey);
        key.setText(numbers.get(i));
        return view;
    }
}
