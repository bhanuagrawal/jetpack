package agrawal.bhanu.thoughtworks.network;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import agrawal.bhanu.thoughtworks.WebService;
import agrawal.bhanu.thoughtworks.pojo.RequestDetails;

public class MyStringRequest extends StringRequest {



    public MyStringRequest(final RequestDetails requestDetails, final WebService.HtttpResonseListner resonseListner) {


        super(requestDetails.getRequestType(), requestDetails.getUrl(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resonseListner.onSuccess(requestDetails, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        resonseListner.onError(requestDetails, error);
                    }
                });
    }



}