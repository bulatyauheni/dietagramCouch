package bulat.diet.helper_couch.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import bulat.diet.helper_couch.R;
import bulat.diet.helper_couch.activity.DietHelperActivity;
import bulat.diet.helper_couch.activity.NotificationActivity;
import bulat.diet.helper_couch.item.BasicDTO;
import bulat.diet.helper_couch.item.Client;
import bulat.diet.helper_couch.item.ClientDTO;
import bulat.diet.helper_couch.item.CouchDTO;
import bulat.diet.helper_couch.item.TodayItemDTO;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface  RestService {
	String SERVICE_COMMON_ENDPOINT = "http://old.dietagram.ru";
	String SERVICE_API_ENDPOINT = "http://api.dietagram.ru";
	//http://old.dietagram.ru/registerCouch.php?first_name=man&last_name=mumu&email=dasda@asda.com&phone=323412312&country=ru
	//https://old.dietagram.ru/registerCouch.php?first_name=T1&last_name=T2&email=T3&phone=T4&country=T5
	@GET("/registerCouch.php")
	Observable<BasicDTO> registerCouch(@Query("first_name") String name, @Query("last_name") String lastName, @Query("email") String email, @Query("phone") String phone, @Query("country") String country);

	@GET("/getCouch.php")
	Observable<CouchDTO> getCouchInfo(@Query("couch_id") String id);

	@GET("/getClients.php")
	Observable<List<ClientDTO>> getClients(@Query("couch_id") String id, @Query("g_id") String email);

	/*@GET("/users/{login}")
	Observable<Client> getClients(@Path("login") String login);*/

	@GET("/upload/{clientid}")
	Observable<List<TodayItemDTO>> getClientsHistory(@Path("clientid") String clientid);

}