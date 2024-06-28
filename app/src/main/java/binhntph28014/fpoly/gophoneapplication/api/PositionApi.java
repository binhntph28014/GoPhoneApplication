package binhntph28014.fpoly.gophoneapplication.api;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface PositionApi {
    Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();
    PositionApi API = new Retrofit.Builder()
            .baseUrl("https://vapi.vnappmob.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PositionApi.class);

}
//