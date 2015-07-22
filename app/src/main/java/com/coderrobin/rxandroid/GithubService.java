package com.coderrobin.rxandroid;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * Retrofit interface to make Github API calls
 */
public interface GithubService {

    @GET("/search/repositories")
    Observable<GithubResults> getTopNewAndroidRepos(@QueryMap Map<String, String> queryMap);

}
