package com.example.hp.lively;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergii Varenyk on 28.09.15.
 */
public class LivelyFragment extends Fragment{
    GridView mGridView;
    ArrayList<LivelyPin> mPins;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchEvent().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.lively_gallery,container,false);
        mGridView = (GridView)v.findViewById(R.id.gridView);
        setupAdapter();
        return v;
    }

    private class FetchEvent extends AsyncTask<Void,Void,ArrayList<LivelyPin>> {
        private ArrayList<LivelyPin> pinList = new ArrayList<LivelyPin>();
        private LivelyPin pin;

        @Override
        protected ArrayList<LivelyPin> doInBackground(Void... params) {
            try {
                String result = new LivelyFetch().getUrl("https://api.pinterest.com/v1/me/pins/?access_token=AbVjN2mfM600svPbvse7FyqD22sPFAeqkvDuMidCgxHUduAUXwAAAAA" +
                        "&fields=id,link,note,image[small]&limit=100");
                Log.i("Ura: it works ", "" + result);
                pin = new LivelyPin();
                pinList = pin.makePinList(result);

            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList imageUrlList = new ArrayList();
            for(LivelyPin lp : pinList) {
                imageUrlList.add((lp.getImageUrl()).toString());
                Log.i("pinList", "" + (lp.getImageUrl()).toString());
            }
            return imageUrlList;
        }

        @Override
        protected void onPostExecute(ArrayList<LivelyPin> imageUrlList){
            mPins = imageUrlList;
            setupAdapter();

        }
    }

    void setupAdapter(){
        if (getActivity() == null || mGridView == null){
            return;
        }
        if(mPins != null){
            mGridView.setAdapter(new PinsGalleryAdapter(mPins));
        }else{
            mGridView.setAdapter(null);
        }
    }

    //create user's adapter
    private class PinsGalleryAdapter extends ArrayAdapter<LivelyPin>{

        public PinsGalleryAdapter(ArrayList<LivelyPin> pins) {
            super(getActivity(), 0, pins);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_pins,parent,false);
            }
            ImageView imageView = (ImageView)convertView.findViewById(R.id.gallery_pins_imageView);
            imageView.setImageResource(R.mipmap.loading);
            return convertView;
        }
    }

}
