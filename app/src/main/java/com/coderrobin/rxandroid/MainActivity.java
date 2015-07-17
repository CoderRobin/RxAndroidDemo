package com.coderrobin.rxandroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;


public class MainActivity extends ActionBarActivity {
    private static final String TAG="coderrobin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testOrigin();
        testSecond();
        testMap();
    }


    public void testOrigin(){
            Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("Hello RxAndroid !!");
                        subscriber.onCompleted();
                    }
                }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.v(TAG,"testFirst:onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.v(TAG,"onNext:"+s);
            }
        });

    }


    public void testSecond(){
        String[] array = {"coderrobin", "testSecond"};
        Observable<String> simpleObservable = Observable.from(array);
        simpleObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.v(TAG, s);
            }
        });
    }

    public void testMap(){

        Observable.just("coderrobin").map(new Func1<String, List>() {
            @Override
            public List<String> call(String s) {
                List<String> test = new ArrayList<String>();
                test.add(s);
                test.add("testMap");
                return test;
            }
        }).flatMap(new Func1<List, Observable<String>>() {
            @Override
            public Observable<String> call(List list) {
                return Observable.from(list);
            }
        })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String text) {
                        Log.v(TAG,text);
                    }
                });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
