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

public class WebService {

    @Inject RequestQueue mRequestQueue;
    Application application;
    RequestDetails mRequestDetails;
    HtttpResonseListner htttpResonseListner;

    public WebService(Application application) {
        this.application = application;
        ((MyApp)application).getWebComponent().inject(this);

    }

    public void setHtttpResonseListner(HtttpResonseListner htttpResonseListner) {
        this.htttpResonseListner = htttpResonseListner;
    }

    public void makeRequest(RequestDetails requestDetails){

        mRequestDetails = requestDetails;
        StringRequest request = new StringRequest(mRequestDetails.getRequestType(), mRequestDetails.getUrl(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        htttpResonseListner.onSuccess(mRequestDetails, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        htttpResonseListner.onError(mRequestDetails, error);
                    }
                }
        ){
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
                    return mRequestDetails.getRequestBody() == null ? null : mRequestDetails.getRequestBody().getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestDetails.getRequestBody(), "utf-8");
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
