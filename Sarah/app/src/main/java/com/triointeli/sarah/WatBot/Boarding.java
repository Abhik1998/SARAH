package com.triointeli.sarah.WatBot;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.triointeli.sarah.R;

public class Boarding extends FragmentActivity implements View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;
    private ImageView[] indicators;
    private int[] colorList;
    private ArgbEvaluator evaluator;

    private Button btnSkip;
    private Button btnFinish;
    private ImageButton btnNext;
    private int page;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding);

        mUsername=getIntent().getExtras().getString("username");

        btnSkip = (Button) findViewById(R.id.footer_control_button_skip);
        btnFinish = (Button) findViewById(R.id.footer_control_button_finish);
        btnNext = (ImageButton) findViewById(R.id.footer_control_button_next);

        indicators = new ImageView[]{
                (ImageView) findViewById(R.id.footer_control_indicator_1),
                (ImageView) findViewById(R.id.footer_control_indicator_2),
                (ImageView) findViewById(R.id.footer_control_indicator_3),
                (ImageView) findViewById(R.id.footer_control_indicator_4)};


        colorList = new int[]{
                Color.parseColor("#FFC107"),
                Color.parseColor("#3F51B5"),
                Color.parseColor("#8BC34A"),
                Color.parseColor("#000000")};
        evaluator = new ArgbEvaluator();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setBackgroundColor(colorList[0]);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset,
                        colorList[position], position == colorList.length-1 ? colorList[position] : colorList[position+1]);

                mViewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                updateIndicator(position);
                mViewPager.setBackgroundColor(colorList[position]);


                btnSkip.setVisibility(position == 3? View.INVISIBLE: View.VISIBLE);
                btnNext.setVisibility(position == 3? View.GONE : View.VISIBLE);
                btnFinish.setVisibility(position == 3 ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(this);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final TextView textView=(TextView)findViewById(R.id.splash_text);
//                if(textView!=null) {
//                    textView.setText("WELCOME, " + mUsername);
//
//                    Intent intent = new Intent(Boarding.this, Splash_screen.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                }else {
                    finish();
                //}
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final TextView textView=(TextView)findViewById(R.id.splash_text);
//                if(textView!=null) {
//                    textView.setText("WELCOME, " + mUsername);
//
//                    Intent intent = new Intent(Boarding.this, Splash_screen.class);
//                    startActivity(intent);
//                }else {
                    finish();
                //}
            }
        });
    }

    private void updateIndicator(int position){
        for (int i = 0; i < indicators.length; i++) {
            if(i == position){
                indicators[i].setImageResource(R.drawable.indicator_selected);
            }else
                indicators[i].setImageResource(R.drawable.indicator_unselected);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_boarding, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.footer_control_button_next:
                page++;
                mViewPager.setCurrentItem(page, true);
                break;
        }

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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.slider_image);

            int sectNumb = getArguments().getInt(ARG_SECTION_NUMBER);
            int[] images = {R.drawable.ic_camera_black_24dp, R.drawable.ic_filter_vintage_black_24dp, R.drawable.ic_monochrome_photos_black_24dp,R.drawable.ic_swap_horiz_white_24dp};
            imageView.setImageResource(images[sectNumb]);
            int[] texts = {R.string.fragment1,R.string.fragment2,R.string.fragment3,R.string.fragment4};
            textView.setText(getString(texts[sectNumb], sectNumb));

            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
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
                case 3:
                    return "SECTION 4";
            }
            return null;
        }
    }
}