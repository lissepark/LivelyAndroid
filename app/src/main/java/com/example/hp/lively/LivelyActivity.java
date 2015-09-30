package com.example.hp.lively;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class LivelyActivity extends ActionBarActivity {

    Fragment createFragment(){
        return new LivelyFragment();
    }

    protected int getLayoutResId(){
        return R.layout.activity_lively;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        //создадим объект FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if(fragment == null){
            fragment = createFragment();
            //создадим и закрепим транзакцию фрагмента
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
            //R.id.fragmentContainer - идентификатор контейнерного представления, выполняет 2 ф-ии:
            //1. сообщает FragmentManager где в представлении активности должно находиться
            //представление фрагмента,
            //2. обеспечивает однозначную идентификацию фрагмента в списке FragmentManager
        }
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lively, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
