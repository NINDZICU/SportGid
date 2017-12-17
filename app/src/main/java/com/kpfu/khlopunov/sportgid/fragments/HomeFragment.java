package com.kpfu.khlopunov.sportgid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kpfu.khlopunov.sportgid.activities.AuthentificationActivity;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.adapters.KindSportsAdapter;
import com.kpfu.khlopunov.sportgid.service.ApiService;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;

/**
 * Created by hlopu on 24.10.2017.
 */

public class HomeFragment extends Fragment {
    private VKAccessToken access_token;

    private RecyclerView rvKinds;
    private EditText etSearch;
    private KindSportsAdapter adapter;

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
        System.out.println("ACCESS TOKEN VK "+access_token);
        if (VKSdk.isLoggedIn()) {
            Intent intent = new Intent(getActivity(), AuthentificationActivity.class);
            startActivity(intent);
        }
        else{
            rvKinds = view.findViewById(R.id.rv_kind_of_sports);
            etSearch = view.findViewById(R.id.et_search);

            rvKinds.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            adapter = new KindSportsAdapter(getActivity());
            adapter.setmKindSportListener(kindSport ->{
                ListObjectsFragment fragment = ListObjectsFragment.newInstance(kindSport.getId());
                fragment.setEventsListener(nextFragment->{
                    getChildFragmentManager().beginTransaction()
                            .add(R.id.frame_home_fragment, nextFragment, ListObjectsFragment.class.getName())
                            .addToBackStack(HomeFragment.class.getName())
                            .commit();
                });
                getChildFragmentManager().beginTransaction()
                        .add(R.id.frame_home_fragment, fragment, ListObjectsFragment.class.getName())
                        .addToBackStack(HomeFragment.class.getName())
                        .commit();

//               TabPagerAdapter pagerAdapter = TabPagerAdapter.getInstance(getChildFragmentManager());
//                List<Fragment> fragments = pagerAdapter.getmFragmentList();
//                fragments.set(0, ListObjectsFragment.newInstance());
//                pagerAdapter.setFragmentList(fragments);
            });
//            List<KindSport> kindSports = new ArrayList<>();
//            adapter.setKindSports(kindSports);
            ApiService apiService = new ApiService(getActivity());
            apiService.getKindSports(adapter);
            rvKinds.setAdapter(adapter);
        }
    }
}
