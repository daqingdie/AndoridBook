package com.example.book.book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.book.R;
import com.example.book.adapter.AdminBookAdapter;
import com.example.book.javaBean.Book;
import com.example.book.others.UpdateListener;

import org.litepal.crud.DataSupport;

import java.util.List;

public class AdminBookActivity extends AppCompatActivity {


    private AdminBookAdapter adapter;
    private EditText queryBook;
    private List<Book> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_book);

        Button button=(Button)findViewById(R.id.button_to_add_book);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminBookActivity.this,AdminAddBookActivity.class);
                startActivityForResult(intent,1);
            }
        });

        books=DataSupport.findAll(Book.class);
        ListView listView=(ListView)findViewById(R.id.book_list_view);
        adapter=new AdminBookAdapter(this, R.layout.book_list_item_2,books , new UpdateListener() {
            @Override
            public void update() {
                List<Book> newBooks=DataSupport.findAll(Book.class);
                books.clear();
                books.addAll(newBooks);
                adapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(adapter);

        queryBook=(EditText)findViewById(R.id.query_book_name);
        Button query=(Button)findViewById(R.id.button_query_book);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> newBooks = DataSupport.where("bookName like ?", "%"+queryBook.getText().toString()+"%")
                        .find(Book.class);
                books.clear();
                books.addAll(newBooks);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    List<Book> newBooks = DataSupport.where("bookName like ?", "%"+queryBook.getText().toString()+"%")
                            .find(Book.class);
                    books.clear();
                    books.addAll(newBooks);
                    adapter.notifyDataSetChanged();
                }
        }
    }
}
