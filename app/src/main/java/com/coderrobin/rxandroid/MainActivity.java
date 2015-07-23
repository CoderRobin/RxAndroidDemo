package com.coderrobin.rxandroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RestAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity {
    private static final String TAG="coderrobin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testOrigin();
        testSecond();
        testMap();
        testNetwork();
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
                    Log.v(TAG, "testFirst:onCompleted");
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(String s) {
                    Log.v(TAG, "onNext:" + s);
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

        Observable.just("coderrobin").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return "hello"+test;
            }
        })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String text) {
                        Log.v(TAG, text);
                    }
                });
    }

    private void testFlatMap(){
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
                        Log.v(TAG, text);
                    }
                });
    }

    public void testFilter(){
        Observable.just(1,2,3,4,5).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer>3;
            }
        })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer num) {
                        Log.v(TAG, num+"");
                    }
                });
    }


    public void testNetwork(){
        Map<String,String> options = new HashMap<>();
        options.put("q","coderrobin");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.github.com")
                .build();
        GithubService apiService = restAdapter.create(GithubService.class);
        apiService.getTopNewAndroidRepos(options)
                .observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<GithubResults, Observable<Repo>>() {
                    @Override
                    public Observable<Repo> call(GithubResults results) {
                        Log.v(TAG,results.total_count+"");

                        return Observable.from(results.items);
                    }
                }).map(new Func1<Repo, String >() {
              @Override
                public String call(Repo repo) {
                return repo.url;
            }
        })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        Log.v(TAG, "url:" + url);
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
