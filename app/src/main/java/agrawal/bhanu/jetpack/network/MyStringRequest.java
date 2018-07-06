package agrawal.bhanu.jetpack.network;


import com.android.volley.toolbox.StringRequest;

import agrawal.bhanu.jetpack.model.RequestDetails;

public class MyStringRequest extends StringRequest {



    public MyStringRequest(final RequestDetails requestDetails) {

        super(requestDetails.getRequestType(), requestDetails.getUrl(),requestDetails.getOnSuccess(), requestDetails.getOnError());

    }
}