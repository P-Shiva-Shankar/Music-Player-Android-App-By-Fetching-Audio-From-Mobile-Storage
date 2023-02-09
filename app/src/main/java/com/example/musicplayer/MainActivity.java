package com.example.musicplayer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.viewpager2.widget.ViewPager2;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private static final  int REQUST_CODE = 1;//setting the default request code for making condition true.,
    private String[] titles=new String[]{"Songs","Albums"};//array of strings which is to be passed tab_layout to display the names
    static ArrayList<MusicFiles> musicFiles;
    ViewPagerFragmentAdapter viewPagerFragmentAdapter;//module for page fragments in main activity..,
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        permissions();//permisssion function..,
    }
    private void permissions(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    ,REQUST_CODE);
        }
        else
        {

            musicFiles=getAllAudio(this);
            viewPager2=findViewById(R.id.view_pager);
            tabLayout=findViewById(R.id.tab_layout);
            viewPagerFragmentAdapter=new ViewPagerFragmentAdapter(this);
            viewPager2.setAdapter(viewPagerFragmentAdapter);
            new TabLayoutMediator(tabLayout,viewPager2,((tab, position) ->tab.setText(titles[position]))).attach();
            Toast.makeText(this,"Enjoy The Rhythm Of Beats..,",Toast.LENGTH_SHORT).show();

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUST_CODE)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {

                musicFiles=getAllAudio(this);
                viewPager2=findViewById(R.id.view_pager);
                tabLayout=findViewById(R.id.tab_layout);
                viewPagerFragmentAdapter=new ViewPagerFragmentAdapter(this);
                viewPager2.setAdapter(viewPagerFragmentAdapter);
                new TabLayoutMediator(tabLayout,viewPager2,((tab, position) ->tab.setText(titles[position]))).attach();
                Toast.makeText(this,"Enjoy The Rhythm Of Beats..,",Toast.LENGTH_SHORT).show();

            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        ,REQUST_CODE);

            }
        }
    }


    public static ArrayList<MusicFiles> getAllAudio(Context context)
    {
       ArrayList<MusicFiles> tempAudioList=new ArrayList<>();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection={
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,//FOR PATH
                MediaStore.Audio.Media.ARTIST,
        };
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);
        if (cursor!=null)
        {
            while (cursor.moveToNext())
            {
                String album=cursor.getString( 0);
                String title=cursor.getString( 1);
                String duration=cursor.getString( 2);
                String path=cursor.getString( 3);
                String artist=cursor.getString( 4);

                MusicFiles musicFiles=new MusicFiles(path,title,artist,album,duration);
                //take log.e for check
                Log.e("Path :"+path,"Album :"+album);
                tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;
    }







}