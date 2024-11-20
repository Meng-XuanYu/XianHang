package com.pzj.navigatetabbar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;

/**
 * PersonFragment
 *
 * @author PengZhenjin
 * @date 2017-9-11
 */
public class PersonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.menu.fragment_person, container, false);
    }
}
