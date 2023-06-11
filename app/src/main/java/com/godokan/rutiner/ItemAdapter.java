package com.godokan.rutiner;

import static android.view.View.inflate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    ArrayList<ListItem> items = new ArrayList<>();
    Context context;

    public ItemAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    public void addItem(ListItem item){
        items.add(item);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListItem listItem = items.get(i);

        View itemView = inflate(context, R.layout.list_item, null);
        TextView type = itemView.findViewById(R.id.type);
        TextView context = itemView.findViewById(R.id.context);
        TextView date = itemView.findViewById(R.id.date);
        TextView flag = itemView.findViewById(R.id.flag);

        type.setText(listItem.getType());
        context.setText(listItem.getName()+" / "+listItem.getContext());
        date.setText(listItem.getDate());
        flag.setText(listItem.getFlag());

        return itemView;
    }
}
