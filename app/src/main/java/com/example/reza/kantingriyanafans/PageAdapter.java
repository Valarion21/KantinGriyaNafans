package com.example.reza.kantingriyanafans;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    final int pageCount=2;
    Bundle info;
    public PageAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int pos){

        info = new Bundle();
        switch (pos){
            case 0:
                Makanan fp=new Makanan();
                info.putInt("CurrentPage",pos++);
                fp.setArguments(info);
                return fp;
            case 1:
                Minuman sp = new Minuman();
                info.putInt("CurrentPage",pos++);
                sp.setArguments(info);
                return sp;
        }
        return null;
    }
    @Override
    public int getCount(){
        return pageCount;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return "Makanan";
        }else if(position==1){
            return "Minuman";
        }else{
            return "Makanan";
        }
    }
}
