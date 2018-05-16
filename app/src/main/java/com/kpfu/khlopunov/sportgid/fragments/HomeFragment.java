package com.kpfu.khlopunov.sportgid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.adapters.KindSportsAdapter;
import com.kpfu.khlopunov.sportgid.api.SportGidApi;
import com.kpfu.khlopunov.sportgid.api.SportGidApiRequests;
import com.kpfu.khlopunov.sportgid.models.ApiResult;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.service.ApiService;
import com.kpfu.khlopunov.sportgid.service.SearchService;
import com.vk.sdk.VKAccessToken;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hlopu on 24.10.2017.
 */

public class HomeFragment extends Fragment implements NotifyFragment, OnBackPressedListener, ApiCallback {
    private VKAccessToken access_token;

    private RecyclerView rvKinds;
    private SearchView searchView;
    private KindSportsAdapter adapter;
    private ProgressBar progressBar;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        VKSdk.initialize(getActivity().getApplicationContext()); // TODO в onCreate приложения
        access_token = VKAccessToken.tokenFromSharedPreferences(getContext(), VKAccessToken.ACCESS_TOKEN);
        System.out.println("ACCESS TOKEN VK " + access_token);
//        if (!VKSdk.isLoggedIn()) {

        rvKinds = view.findViewById(R.id.rv_kind_of_sports);
        searchView = view.findViewById(R.id.et_search);
        progressBar = view.findViewById(R.id.pb_home);

        rvKinds.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        setVisible();
        adapter = new KindSportsAdapter(getActivity(), HomeFragment.this);
        adapter.setmKindSportListener(kindSport -> {
            ListObjectsFragment fragment = ListObjectsFragment.newInstance(kindSport.getId());
            fragment.setEventsListener(nextFragment -> getChildFragmentManager().beginTransaction()
                    .replace(R.id.frame_home_fragment, nextFragment, ListObjectsFragment.class.getName())
                    .addToBackStack(HomeFragment.class.getName())
                    .commit());
            getChildFragmentManager().beginTransaction()
                    .add(R.id.frame_home_fragment, fragment, ListObjectsFragment.class.getName())
                    .addToBackStack(HomeFragment.class.getName())
                    .commit();

//               TabPagerAdapter pagerAdapter = TabPagerAdapter.getInstance(getChildFragmentManager());
//                List<Fragment> fragments = pagerAdapter.getmFragmentList();
//                fragments.set(0, ListObjectsFragment.newInstance());
//                pagerAdapter.setFragmentList(fragments);
        });
        ApiService apiService = new ApiService(getActivity());
//            List<KindSport> kindSports = apiService.getKindSports(HomeFragment.this);
        apiService.getKindSports(HomeFragment.this);
//            adapter.setKindSports(kindSports);
        rvKinds.setAdapter(adapter);

        SportGidApiRequests requests = SportGidApi.getInstance().getmSportGidApiRequests();
        SearchService.fromView(searchView)
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String text) throws Exception {
                        return !text.isEmpty();
                    }
                })
                .distinctUntilChanged()
                .switchMap(query -> {
                    setVisible();
                    Log.d("SWITCH MAP", "SWITCH MAP");
                    return requests.searchKindSport(query)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                })
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        callback(apiResult.getBody());
                    }
                }, throwable -> {
                    callback("ERROR");
                    //Todo переделать на контекст
                    Toast.makeText(getActivity(), "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA SearchKindSports " + throwable.getMessage());
                });

    }

    @Override
    public void notifyData() {
        setVisible();
        //TODO убрать
    }

    public void setVisible() {
        progressBar.setVisibility(View.VISIBLE);
        rvKinds.setVisibility(View.GONE);
    }

    @Override
    public void callback(Object object) {
        if (object instanceof String) {
            if (((String) object).equals("ERROR"))
                Toast.makeText(getActivity(), "Не удалось загрузить", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setKindSports((List<KindSport>) object);
            adapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
        rvKinds.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        System.out.println("IM PRESSED");
//        getChildFragmentManager().popBackStack(R.id.frame_home_fragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getChildFragmentManager().popBackStack();


    }
}
