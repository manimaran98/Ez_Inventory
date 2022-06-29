package com.example.ez_inventory_system;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class transaction_list_adapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<item> itemList;

    public transaction_list_adapter(Context context, int layout, ArrayList<item> itemList) {
        this.context = context;
        this.layout = layout;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder{
        ImageView imageView;
        TextView txtName, txtPrice,txtQuantity;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName = (TextView) row.findViewById(R.id.txtName);
            holder.txtPrice = (TextView) row.findViewById(R.id.txtPrice);
            holder.txtQuantity = (TextView) row.findViewById(R.id.txtQuantity);
            holder.imageView = (ImageView) row.findViewById(R.id.stock_item);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        item item = itemList.get(position);

        holder.txtName.setText("Item Name: " + item.getName());
        holder.txtPrice.setText("Add/Drop: " + item.getPrice());
        holder.txtQuantity.setText("Quantity: " + item.getQuantity());

        byte[] itemImage = item.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(itemImage, 0, itemImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
