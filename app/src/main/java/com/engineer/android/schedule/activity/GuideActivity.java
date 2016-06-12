package com.engineer.android.schedule.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import com.engineer.android.schedule.R;
import com.engineer.android.schedule.interaction.GuideAction;
import com.engineer.android.schedule.presenter.GuidePresenter;

public class GuideActivity extends BaseActivity<GuidePresenter> implements GuideAction {

    public static void start(Context context) {
        Intent starter = new Intent(context, GuideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        assert mViewPager != null;
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected GuidePresenter newPresenter() {
        return new GuidePresenter(this);
    }

    @Override
    public void startMainActivity() {
        MainActivity.start(this);
    }

    @Override
    public AppCompatActivity provideContext() {
        return this;
    }

    @Override
    public void finishActivity() {
        ActivityCompat.finishAfterTransition(this);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_guide, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (sectionNumber){
                case 1:
                    textView.setText(getText(R.string.guide_page_one_content));
                    break;
                case 2:
                    textView.setText(getText(R.string.guide_page_two_content));
                    break;
                case 3:
                    textView.setText(getText(R.string.guide_page_three_content));
                    Button buttonStartNow = (Button) rootView.findViewById(R.id.button_start_now);
                    buttonStartNow.setVisibility(View.VISIBLE);
                    buttonStartNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((GuideActivity)getActivity()).getPresenter().onStartNowButtonClick();
                        }
                    });
                    break;
            }
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
