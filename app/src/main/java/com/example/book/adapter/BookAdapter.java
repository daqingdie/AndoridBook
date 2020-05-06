package com.example.book.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book.javaBean.Book;
import com.example.book.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 大清爹 on 2019/4/28.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private int resourceId;
    public BookAdapter(Context context,int textViewResourceId, List<Book> objects) {
        super(context,textViewResourceId, objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Book book=getItem(position);
        ViewHandle viewHandle;
        if(convertView==null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHandle = new ViewHandle();
            viewHandle.bookImg=(ImageView)view.findViewById(R.id.book_img);
            viewHandle.bookName=(TextView)view.findViewById(R.id.book_name);
            viewHandle.bookAuthor=(TextView)view.findViewById(R.id.book_author);
            viewHandle.bookPrice=(TextView)view.findViewById(R.id.book_price);
            view.setTag(viewHandle);
        }else {
            view=convertView;
            viewHandle=(ViewHandle)view.getTag();
        }
        Bitmap bitmap= BitmapFactory.decodeFile(book.getBookImg());
        viewHandle.bookImg.setImageBitmap(bitmap);
        viewHandle.bookName.setText(book.getBookName());
        viewHandle.bookAuthor.setText(book.getAuthor());
        DecimalFormat sum = new DecimalFormat("#.00");
        viewHandle.bookPrice.setText(sum.format(book.getPrice()));

        return view;
    }

    private class ViewHandle{
        ImageView bookImg;
        TextView bookName;
        TextView bookAuthor;
        TextView bookPrice;
    }
}
