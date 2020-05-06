package com.example.book.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.javaBean.Book;
import com.example.book.R;
import com.example.book.javaBean.Cart;
import com.example.book.others.UpdateListener;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 大清爹 on 2019/5/2.
 */

public class AdminBookAdapter extends ArrayAdapter<Book> {

    private static final String TAG = "AdminBookAdapter";
    private String id;
    private int resourceId;
    private View dialogView;
    private UpdateListener listener;
    private EditText uBookName;
    private EditText uBookAuthor;
    private EditText uBookPublishing;
    private EditText uBookSummary;
    private EditText uBookNumber;
    private EditText uBookPrice;
    private String oldName;
    public AdminBookAdapter(Context context, int resource, List<Book> objects,UpdateListener listener) {
        super(context, resource, objects);
        resourceId=resource;
        this.listener=listener;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Book book=getItem(position);
        view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView bookImg=(ImageView)view.findViewById(R.id.book_img);
        final TextView bookName=(TextView)view.findViewById(R.id.book_name);
        TextView bookAuthor=(TextView)view.findViewById(R.id.book_author);
        TextView bookPrice=(TextView)view.findViewById(R.id.book_price);
        TextView bookId=(TextView)view.findViewById(R.id.book_id);
        TextView bookPublishing=(TextView)view.findViewById(R.id.book_publishing);
        TextView bookSummary=(TextView)view.findViewById(R.id.book_summary);
        TextView bookNumber=(TextView)view.findViewById(R.id.book_number);
        Button update=(Button)view.findViewById(R.id.button_update_book);
        Button delete=(Button)view.findViewById(R.id.button_delete_book);
        update.setTag(book.getId());
        delete.setTag(book.getId());

        final Bitmap bitmap= BitmapFactory.decodeFile(book.getBookImg());
        bookImg.setImageBitmap(bitmap);
        bookName.setText(book.getBookName());
        bookAuthor.setText(book.getAuthor());
        bookPrice.setText(String.valueOf(book.getPrice()));
        bookId.setText(String.valueOf(book.getId()));
        bookSummary.setText(book.getSummary());
        bookNumber.setText(String.valueOf(book.getNumber()));
        bookPublishing.setText(String.valueOf(book.getPublishing()));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=String.valueOf(v.getTag());
               // Log.d(TAG, "onClick: "+id);
                dialogView=LayoutInflater.from(getContext()).inflate(R.layout.update_book_dialog,null);
                uBookName=(EditText)dialogView.findViewById(R.id.book_name);
                uBookAuthor=(EditText)dialogView.findViewById(R.id.book_author);
                uBookPublishing=(EditText)dialogView.findViewById(R.id.book_publishing);
                uBookSummary=(EditText)dialogView.findViewById(R.id.book_summary);
                uBookNumber=(EditText)dialogView.findViewById(R.id.book_number);
                uBookPrice=(EditText)dialogView.findViewById(R.id.book_price);
                List<Book> books= DataSupport.where("id=?",id).find(Book.class);
                for(Book book:books){
                    uBookName.setText(book.getBookName());
                    //Log.d(TAG, "onClick: "+book.getBookName());
                    uBookAuthor.setText(book.getAuthor());
                    uBookPrice.setText(String.valueOf(book.getPrice()));
                    uBookSummary.setText(book.getSummary());
                    uBookNumber.setText(String.valueOf(book.getNumber()));
                    uBookPublishing.setText(String.valueOf(book.getPublishing()));
                    oldName=book.getBookName();
                }
                AlertDialog dialog=new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(isRight().equals("")){
                                    if(DataSupport.where("bookName=?",uBookName.getText().toString()).find(Book.class).size()<=0||
                                            uBookName.getText().toString().equals(oldName)){
                                        Book book=new Book();
                                        book.setBookName( uBookName.getText().toString());
                                        // Log.d(TAG, "onClick: "+book.getBookName());
                                        book.setAuthor( uBookAuthor.getText().toString());
                                        book.setSummary( uBookSummary.getText().toString());
                                        book.setPublishing( uBookPublishing.getText().toString());
                                        book.setNumber(Integer.valueOf( uBookNumber.getText().toString()));
                                        book.setPrice(Double.valueOf( uBookPrice.getText().toString()));
                                        book.updateAll("id=?",id);
                                        try{
                                            Cart cart=DataSupport.where("bookId=?",id).find(Cart.class).get(0);
                                            cart.setBookName(book.getBookName());
                                            cart.setBookPrice(book.getPrice());
                                            cart.setSum(cart.getNumber()*cart.getBookPrice());
                                            cart.updateAll("bookId=?",id);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                        listener.update();
                                    }else {
                                        Toast.makeText(getContext(), "该图书已存在", Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(getContext(), ""+isRight(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=String.valueOf(v.getTag());
                AlertDialog dialog=new AlertDialog.Builder(getContext())
                        .setTitle("删除确认")
                        .setMessage("是否确认删除！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataSupport.deleteAll(Book.class,"id=?",id);
                                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                listener.update();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });

        return view;
    }
    private String isRight(){
        String mess="";
        if( uBookName.getText().toString().equals(""))
            mess="图书书名不能为空";
        else if( uBookName.getText().toString().length()>20)
            mess="图书书名过长";
        else if( uBookAuthor.getText().toString().equals(""))
            mess="图书作者不能为空";
        else if( uBookAuthor.getText().toString().length()>20)
            mess="图书作者过长";
        else if( uBookPublishing.getText().toString().equals(""))
            mess="图书出版社不能为空";
        else if( uBookPublishing.getText().toString().length()>20)
            mess="图书出版社过长";
        else {
            try{
                if(Integer.valueOf(uBookNumber.getText().toString())<=0)
                    mess="图书库存不能少于0";
                else if(Integer.valueOf(uBookNumber.getText().toString())>1000)
                    mess="图书库存不能大于1000";
                else if(Double.valueOf(uBookPrice.getText().toString())<=0)
                    mess="图书单价不能小于0";
            }catch (Exception e){
                mess="图书库存和单价必须为纯数字";
            }
        }
        return mess;
    }

}
