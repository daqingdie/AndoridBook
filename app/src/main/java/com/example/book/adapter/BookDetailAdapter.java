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

import com.example.book.R;
import com.example.book.javaBean.Book;

import java.util.List;

/**
 * Created by 大清爹 on 2019/5/2.
 */

public class BookDetailAdapter extends ArrayAdapter<Book> {


    int resourceId;
    public BookDetailAdapter(Context context, int resource, List<Book> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        Book book=getItem(position);
        TextView bookName=(TextView)view.findViewById(R.id.book_name);
        TextView bookAuthor=(TextView)view.findViewById(R.id.book_author);
        TextView bookPublishing=(TextView)view.findViewById(R.id.book_publishing);
        TextView bookNumber=(TextView)view.findViewById(R.id.book_number);
        TextView  bookPrice=(TextView)view.findViewById(R.id.book_price);
        TextView bookSummary=(TextView)view.findViewById(R.id.book_summary);
        ImageView bookImg=(ImageView)view.findViewById(R.id.book_img);

        bookName.setText(book.getBookName());
        bookAuthor.setText(book.getAuthor());
        bookPublishing.setText(book.getPublishing());
        bookNumber.setText(String.valueOf(book.getNumber()-1));
        bookPrice.setText(String.valueOf(book.getPrice()));
        bookSummary.setText(book.getSummary());
        Bitmap bitmap= BitmapFactory.decodeFile(book.getBookImg());
        bookImg.setImageBitmap(bitmap);
        return view;
    }
}
