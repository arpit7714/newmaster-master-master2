
package com.quirodev.usagestatsmanagersample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class UsageStatAdapter extends RecyclerView.Adapter<UsageStatVH> {

    public List<AppItem1> list;
    public List<AppItem1> list2;
    Activity mcontext;

    //it will not rerquire any extral list beacuse adapter has been provided with the list in mainactivity with setlist() method
    public UsageStatAdapter(Activity context){

        mcontext=context;
        list = new ArrayList<>();
        list2 = new ArrayList<>();
    }

    @Override
    public UsageStatVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usage_stat_item, parent, false);
        return new UsageStatVH(view);
    }

    @Override
    public void onBindViewHolder(UsageStatVH holder, int position) {
        holder.bindTo(mcontext,list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<AppItem1> list) {
        this.list = list;
        //    this.list2.addAll(this.list);
        notifyDataSetChanged();
    }

    public  void updateList(List<AppItem1>list1)
    {
        list = new ArrayList<>();

        list.addAll(list1);

        // this.list2.addAll(this.list);
        notifyDataSetChanged();
    }
    // filter
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list2.addAll(list);
        list.clear();


        if (charText.length() == 0) {
            list.addAll(list2);
        }
        else{

            for (AppItem1 wp : list2) {
                String str = wp.appname;
                if (str.toLowerCase(Locale.getDefault()).contains(charText)) {
                    list.add(wp);
                }
            }
        }

        HashSet<AppItem1> hashSet = new HashSet<AppItem1>();
        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);


        // this.list2.addAll(this.list);
        notifyDataSetChanged();
    }
}