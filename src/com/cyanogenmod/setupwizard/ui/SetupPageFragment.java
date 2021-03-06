/*
 * Copyright (C) 2013 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.setupwizard.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyanogenmod.setupwizard.R;
import com.cyanogenmod.setupwizard.setup.Page;
import com.cyanogenmod.setupwizard.setup.SetupDataCallbacks;

public abstract class SetupPageFragment extends Fragment {

    protected SetupDataCallbacks mCallbacks;
    protected String mKey;
    protected Page mPage;
    protected View mRootView;
    protected TextView mTitleView;
    protected ViewGroup mHeaderView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mKey = args.getString(Page.KEY_PAGE_ARGUMENT);
        if (mKey == null) {
            throw new IllegalArgumentException("No KEY_PAGE_ARGUMENT given");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutResource(), container, false);
        mTitleView = (TextView) mRootView.findViewById(android.R.id.title);
        mHeaderView = (ViewGroup )  mRootView.findViewById(R.id.header);
        getActivity().startPostponedEnterTransition();
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPage = mCallbacks.getPage(mKey);
        if (mTitleView != null) {
            mTitleView.setText(mPage.getTitleResId());
        }
        initializePage();
        mCallbacks.onPageLoaded(mPage);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof SetupDataCallbacks)) {
            throw new ClassCastException("Activity implement SetupDataCallbacks");
        }
        mCallbacks = (SetupDataCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    protected abstract void initializePage();
    protected abstract int getLayoutResource();

}
