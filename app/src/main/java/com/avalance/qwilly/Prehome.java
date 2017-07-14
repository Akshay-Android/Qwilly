package com.avalance.qwilly;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avalance.qwilly.Model.DbLink;
import com.avalance.qwilly.Model.Hotel;
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

public class Prehome extends AppCompatActivity {
    private List<Hotel> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HotelAdapter mAdapter;
    String output;
    double longitude=73.165933,latitude=22.310891;
    static String TAG="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prehome);

        recyclerView = (RecyclerView) findViewById(R.id.home_view);

        mAdapter = new HotelAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        GetHotelsList getHotelsList= new GetHotelsList();
        getHotelsList.execute();
    }

    class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.MyViewHolder> {

        private List<Hotel> hotelList;

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView hotel_name, hotel_distance, hotel_address;
            // public RatingBar hotel_rating;
            ImageView hotel_image;
            LinearLayout show_hotel;

            MyViewHolder(View view) {
                super(view);
                hotel_name = (TextView) view.findViewById(R.id.hotel_name);
                hotel_distance = (TextView) view.findViewById(R.id.hotel_distance);
                hotel_address = (TextView) view.findViewById(R.id.hotel_address);
                hotel_image = (ImageView) view.findViewById(R.id.hotel_image);
                show_hotel = (LinearLayout) view.findViewById(R.id.show_hotel);
            }
        }


        HotelAdapter(List<Hotel> hotelList) {
            this.hotelList = hotelList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.raw_home, parent, false);

            return new MyViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Hotel hotel = hotelList.get(position);
            holder.hotel_name.setText(hotel.getHotel_name());
            holder.hotel_distance.setText(hotel.getHotel_distance()+" Km");
            holder.hotel_address.setText(hotel.getHotel_address());

            Picasso.with(Prehome.this)
                    .load(hotel.getHotel_imageUrl())
                    .into(holder.hotel_image);


            holder.show_hotel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(Prehome.this,Home.class);
                    startActivity(intent);

                   /* Fragment fragment = new HotelFragment();
                    if (fragment != null) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment,TAG);
                        ft.addToBackStack(null);
                        ft.commit();
                    }*/
                }
            });
        }

        @Override
        public int getItemCount() {
            return hotelList.size();
        }
    }

    private class GetHotelsList extends AsyncTask<String,String,String> {

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
                        +"&"+URLEncoder.encode("operation","UTF-8") +"="+ URLEncoder.encode("home","UTF-8");

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

                movieList = new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object=jsonArray.getJSONObject(i);

                    Hotel hotel = new Hotel();
                    hotel.setHotel_name(object.getString("hotel_name"));
                    hotel.setHotel_address(object.getString("hotel_address"));
                    hotel.setHotel_id(object.getString("hotel_id"));
                    hotel.setHotel_rating(object.getString("hotel_rating"));
                    hotel.setHotel_phone(object.getString("hotel_phone"));
                    hotel.setHotel_imageUrl(DbLink.imageUrl+"images/hotels/"+object.getString("hotel_image"));

                    Double hotel_lat1= Double.valueOf(object.getString("hotel_lat"));
                    Double hotel_long1= Double.valueOf(object.getString("hotel_long"));

                    double earthRadius = 3958.75;

                    double dLat = Math.toRadians(latitude-hotel_lat1);
                    double dLng = Math.toRadians(longitude-hotel_long1);
                    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                            Math.cos(Math.toRadians(hotel_lat1)) * Math.cos(Math.toRadians(latitude)) *
                                    Math.sin(dLng/2) * Math.sin(dLng/2);
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                    double dist = earthRadius * c ;

                    String distance= String.valueOf(dist);
                    int in=distance.indexOf(".");
                    String d=distance.substring(0,in+3);

                    hotel.setHotel_distance(d);

                    Log.e("distance", String.valueOf(dist));
                    movieList.add(hotel);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mAdapter = new HotelAdapter(movieList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
