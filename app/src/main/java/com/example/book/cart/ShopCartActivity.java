package com.example.book.cart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.book.MainActivity;
import com.example.book.R;
import com.example.book.adapter.CartAdapter;
import com.example.book.javaBean.Cart;
import com.example.book.others.UpdateListener;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ShopCartActivity extends AppCompatActivity {

    private CartAdapter adapter;
    private List<Cart> carts;
    private TextView title;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);


        carts= DataSupport.where("userName=?", MainActivity.username).find(Cart.class);
        listView=(ListView)findViewById(R.id.cart_list_view);
        adapter=new CartAdapter(this, R.layout.cart_list_item,carts, new UpdateListener() {
            @Override
            public void update() {
                List<Cart> newCarts= DataSupport.where("userName=?", MainActivity.username).find(Cart.class);
                carts.clear();
                carts.addAll(newCarts);
                adapter.notifyDataSetChanged();
                isNull();
            }
        });
        listView.setAdapter(adapter);
        /*for(Cart cart:carts){
            Log.d("BookDetailActivity", "onCreate: "+cart.getBookId());
        }*/
        title=(TextView)findViewById(R.id.cart_text);
        isNull();
    }

    private void isNull(){
        if(carts.size()<1) {
            title.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }
}
