package com.example.sai_h.reviewuoso;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class MainActivity extends Fragment {
    ViewPager vp;
    PagerAdapter pa;
    TabLayout tl;
    MapSearch ms = new MapSearch();
    MapOpt mo = new MapOpt();
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.activity_main, container, false);
            vp = (ViewPager) v.findViewById(R.id.viewpg);
            pa = new PagerAdapter(getChildFragmentManager(), getContext());
            vp.setAdapter(pa);
            tl = (TabLayout) v.findViewById(R.id.tab);
            tl.setupWithViewPager(vp);
            for (int i = 0; i < tl.getTabCount(); i++) {
                TabLayout.Tab t = tl.getTabAt(i);
                t.setCustomView(pa.getTabView(i));
            }
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    class PagerAdapter extends FragmentPagerAdapter{
        String tabTitles[] = new String[] {"Custom Search", "Nearby Search"};
        Context con;
        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.con = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0: return ms;
                case 1: return mo;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
        public View getTabView(int position){
            View tab = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab,null);
            TextView t=(TextView)tab.findViewById(R.id.custom_text);
            t.setText(tabTitles[position]);
            return tab;
        }
    }
}



