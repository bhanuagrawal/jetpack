package agrawal.bhanu.jetpack.network;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.network.model.RequestDetails;

@Singleton
public class WebService {

    RequestQueue mRequestQueue;

    @Inject
    public WebService(RequestQueue mRequestQueue) {
        this.mRequestQueue = mRequestQueue;

    }


    public void makeRequest(final RequestDetails requestDetails, final HtttpResponseListner responseListner){

        if(requestDetails.getOnSuccess() == null){
            requestDetails.setOnSuccess(new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    responseListner.onResponse(requestDetails, response);
                }
            });
        }

        if(requestDetails.getOnError() == null){
            requestDetails.setOnError(new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    responseListner.onError(requestDetails, error);
                }
            });
        }

        StringRequest request = new StringRequest(requestDetails.getRequestType(), requestDetails.getUrl(), requestDetails.getOnSuccess(), requestDetails.getOnError()){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestDetails.getRequestBody() == null ? null : requestDetails.getRequestBody().getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestDetails.getRequestBody(), "utf-8");
                    return null;
                }
            }
        };


        mRequestQueue.add(request);

    }


    public interface HtttpResponseListner {
        public void onResponse(RequestDetails requestDetails, Object object);
        public void onError(RequestDetails requestDetails, VolleyError error);
    }
}
