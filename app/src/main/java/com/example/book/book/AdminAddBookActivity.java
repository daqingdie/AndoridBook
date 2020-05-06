package com.example.book.book;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.book.R;
import com.example.book.javaBean.Book;

import org.litepal.crud.DataSupport;

public class AdminAddBookActivity extends AppCompatActivity {


    private ImageView image;
    private Book book=new Book();
    private EditText bookName;
    private EditText bookAuthor;
    private EditText bookPublishing;
    private EditText bookSummary;
    private EditText bookNumber;
    private EditText bookPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_book);

        //初始化控件
        image=(ImageView)findViewById(R.id.imgage_view_book_img);
        bookName=(EditText)findViewById(R.id.book_name);
        bookAuthor=(EditText)findViewById(R.id.book_author);
        bookPublishing=(EditText)findViewById(R.id.book_publishing);
        bookSummary=(EditText)findViewById(R.id.book_summary);
        bookNumber=(EditText)findViewById(R.id.book_number);
        bookPrice=(EditText)findViewById(R.id.book_price);
        Button add_img=(Button)findViewById(R.id.button_add_book_img);
        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(AdminAddBookActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AdminAddBookActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlum();   //打开相册
                }
            }
        });
        Button add=(Button)findViewById(R.id.button_add_book);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRight().equals("")){              //检验数据
                    if(DataSupport.where("bookName=?",bookName.getText().toString()).find(Book.class).size()<=0){
                        book.setBookName(bookName.getText().toString());
                        book.setAuthor(bookAuthor.getText().toString());
                        book.setPublishing(bookPublishing.getText().toString());
                        book.setSummary(bookSummary.getText().toString());
                        book.setNumber(Integer.valueOf(bookNumber.getText().toString()));
                        book.setPrice(Double.valueOf(bookPrice.getText().toString()));
                        book.save();
                        Toast.makeText(AdminAddBookActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        Toast.makeText(AdminAddBookActivity.this, "该图书已存在", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AdminAddBookActivity.this, ""+isRight(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    openAlum();
                else
                    Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void openAlum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=gerImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                imagePath=gerImagePath(contentUri,null);
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                imagePath=gerImagePath(uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme())){
                imagePath=uri.getPath();
            }
            displayImage(imagePath);
            book.setBookImg(imagePath);
        }
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagepath=gerImagePath(uri,null);
        displayImage(imagepath);
    }

    private String gerImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToNext()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            image.setImageBitmap(bitmap);
            image.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }
    private String isRight(){
        String mess="";
        if(bookName.getText().toString().equals(""))
            mess="图书书名不能为空";
        else if(bookName.getText().toString().length()>20)
            mess="图书书名过长";
        else if(bookAuthor.getText().toString().equals(""))
            mess="图书作者不能为空";
        else if(bookAuthor.getText().toString().length()>20)
            mess="图书作者过长";
        else if(bookPublishing.getText().toString().equals(""))
            mess="图书出版社不能为空";
        else if(bookPublishing.getText().toString().length()>20)
            mess="图书出版社过长";
        else {
            try{
                if(book.getBookImg().equals(""));
            }catch (Exception e){
                mess="图书封面不能为空";
            }
            try{
                if(Integer.valueOf(bookNumber.getText().toString())<=0)
                    mess="图书库存不能少于0";
                else if(Integer.valueOf(bookNumber.getText().toString())>1000)
                    mess="图书库存不能大于1000";
                else if(Double.valueOf(bookPrice.getText().toString())<=0)
                    mess="图书单价不能小于0";
            }catch (Exception e){
                mess="图书库存和单价必须为纯数字";
            }
        }
        return mess;
    }

}
