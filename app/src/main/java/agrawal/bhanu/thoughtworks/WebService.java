package agrawal.bhanu.thoughtworks;
import android.app.Application;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.inject.Inject;

import agrawal.bhanu.thoughtworks.network.MyStringRequest;
import agrawal.bhanu.thoughtworks.pojo.RequestDetails;

public class WebService {

    @Inject RequestQueue mRequestQueue;
    Application application;

    public WebService(Application application) {
        this.application = application;
        ((MyApp)application).getWebComponent().inject(this);

    }


    public void makeRequest(final RequestDetails requestDetails, HtttpResonseListner resonseListner){

        MyStringRequest request = new MyStringRequest(requestDetails, resonseListner){


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


    public interface HtttpResonseListner{
        public void onSuccess(RequestDetails requestDetails, Object object);
        public void onError(RequestDetails requestDetails, VolleyError error);
    }
}
