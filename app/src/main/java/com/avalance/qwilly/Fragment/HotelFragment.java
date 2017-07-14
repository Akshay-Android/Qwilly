package com.avalance.qwilly.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avalance.qwilly.Model.DbLink;
import com.avalance.qwilly.Model.Hotel;
import com.avalance.qwilly.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HotelFragment extends Fragment {
    String output;
    private List<Hotel> offerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Hoffers_adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hotel, container, false);
        DbLink.Back_key=1;

        recyclerView = (RecyclerView) view.findViewById(R.id.offer_list);

        mAdapter = new Hoffers_adapter(offerList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        GetOffers getOffers= new GetOffers();
        getOffers.execute();
        return view;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView textView = (TextView) getActivity().findViewById(R.id.title);
        Typeface type = Typeface.createFromAsset(getContext().getAssets(),"fonts/Back to Black Demo.ttf");
        textView.setTypeface(type);
        textView.setTextSize(32);
        textView.setText("Qwilly");

    }

    private class GetOffers extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            String inputLine = null;

            try {
                url=new URL(DbLink.Url);

                URLConnection urlConnection=url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                String req_data= URLEncoder.encode("Content-type", "UTF-8" ) +"="+ URLEncoder.encode("application/json","UTF-8")
                        +"&"+URLEncoder.encode("operation","UTF-8") +"="+ URLEncoder.encode("hotel_offer","UTF-8");

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(req_data);
                writer.flush();

                BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                inputLine=reader.readLine();


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject Object=new JSONObject(inputLine);
                output=Object.getString("re");

                JSONArray jsonArray=Object.getJSONArray("rag");

                offerList = new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object=jsonArray.getJSONObject(i);

                    Hotel hotel = new Hotel();
                    hotel.setHotel_id(object.getString("hotel_id"));
                    hotel.setOffer_name(object.getString("offer_name"));
                    hotel.setOffer_des(object.getString("offer_des"));
                    hotel.setOffer_price(object.getString("offer_price"));
                    hotel.setOffer_image(DbLink.imageUrl+"images/offer/"+object.getString("offer_image"));

                    offerList.add(hotel);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
             Log.e("outpit: ", String.valueOf(inputLine));
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mAdapter = new Hoffers_adapter(offerList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    class Hoffers_adapter extends RecyclerView.Adapter<Hoffers_adapter.MyViewHolder> {

        private List<Hotel> hotelList;

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView offer_name,offer_des, offer_price;
            // public RatingBar hotel_rating;
            ImageView offer_image;

            MyViewHolder(View view) {
                super(view);
                offer_name = (TextView) view.findViewById(R.id.offer_name);
                offer_des = (TextView) view.findViewById(R.id.offer_des);
                offer_price = (TextView) view.findViewById(R.id.offer_price);
                offer_image = (ImageView) view.findViewById(R.id.offer_image);

            }
        }


        Hoffers_adapter(List<Hotel> hotelList) {
            this.hotelList = hotelList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.raw_offer, parent, false);

            return new MyViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Hotel hotel = hotelList.get(position);
            holder.offer_name.setText(hotel.getOffer_name());
            holder.offer_des.setText(hotel.getOffer_des());
            holder.offer_price.setText(Html.fromHtml( "&#8377; " +"<font color=#e31c15>"+hotel.getOffer_price()+"</font>" ));

            Picasso.with(getContext())
                    .load(hotel.getOffer_image())
                    .into(holder.offer_image);


        }

        @Override
        public int getItemCount() {
            return hotelList.size();
        }
    }
}
