package com.bayu.sejarahbatang.api;

import com.google.gson.JsonArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by delaroy on 7/3/17.
 */

public interface Service {

    @GET("android/lihatsemua/{lat}/{lng}")
    Call<JsonArray> getData(
            @Path("lat") String lat,
            @Path("lng") String lng
    );

    @GET("android/lihatwhere/{lat}/{lng}/{kategori}")
    Call<JsonArray> getWhere(
            @Path("lat") String lat,
            @Path("lng") String lng,
            @Path("kategori") String kategori
    );

    @FormUrlEncoded
    @POST("android/lihat_detail/")
    Call<ResponseBody> getDetail(
            @Field("lat") Double lat,
            @Field("lng") Double lng,
            @Field("id_sejarah") String id_sejarah
    );

    @GET("android/tampilkomentar/{id}")
    Call<JsonArray> getKomentL(
            @Path("id") String id
    );

    @GET("android/files/{id}")
    Call<JsonArray> getCarousel(
            @Path("id") String id
    );

    @GET("android/tampilseluruhkomentar/{id}")
    Call<JsonArray> getKoment(
            @Path("id") String id
    );

    @FormUrlEncoded
    @POST("android/simpankomen")
    Call<ResponseBody> addKoment(
            @Field("id_sejarah") String id_sejarah,
            @Field("alamat") String alamat,
            @Field("nama") String nama,
            @Field("komentar") String komentar
    );

}
