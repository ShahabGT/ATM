package ir.shahabazimi.atm.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import ir.shahabazimi.atm.Fragments.IntroFirstFragment;
import ir.shahabazimi.atm.Fragments.IntroFourthFragment;
import ir.shahabazimi.atm.Fragments.IntroSecondFragment;
import ir.shahabazimi.atm.Fragments.IntroThirdFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;


    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
        list = new ArrayList<>();

        list.add(new IntroFirstFragment());
        list.add(new IntroSecondFragment());
        list.add(new IntroThirdFragment());
        list.add(new IntroFourthFragment());

    }

    @Override
    public Fragment getItem(int i) {


        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
