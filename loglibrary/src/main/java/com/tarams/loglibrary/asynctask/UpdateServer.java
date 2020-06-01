package com.tarams.loglibrary.asynctask;

import com.tarams.loglibrary.Logs;
import com.tarams.loglibrary.api.LogstatusEnum;
import com.tarams.loglibrary.api.PostModel;
import com.tarams.loglibrary.db.TraceAspectLogModelDb;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

import static com.tarams.loglibrary.Logs.info;


public class UpdateServer extends AsyncTaskWrapper<String, Void, String[]> {
    private static final String DEFAULT_JSON = "{ \"index\":{}}";
    private final Logs logs;

    public UpdateServer(AsyncHelper<Void, String[]> result, Logs logs) {
        super(result);
        this.logs = logs;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        List<TraceAspectLogModelDb> list = logs.getLogDatabase().logList();
        String[] ids = new String[list.size()];
        StringBuilder bulkRequestBody = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i).id;
            bulkRequestBody.append("{\"index\":{}}\n" +
                    new Gson().toJson(new PostModel(logs.getContext(), list.get(i))));
            bulkRequestBody.append("\n");

        }
        info("reqBody:after " + bulkRequestBody.toString());
        HttpClient httpclient = HttpClients.createDefault();
        HttpEntity entity = new ByteArrayEntity(bulkRequestBody.toString().getBytes());
        HttpPost httppost = new HttpPost("http://18.210.43.198:9200/poc/_bulk");

        try {
            httppost.addHeader("Authorization", "Basic "+ logs.getToken());
            httppost.addHeader("Content-Type", "application/json");
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            int code = response.getStatusLine().getStatusCode();
            info("Upload logs Started items: " + ids);
            if (code == 200) {
                info("Success");
            } else {
                info("failure");
                ids = null;
                failedLogsWithReason(list,"Api returned error code"+code);
            }


        } catch (ClientProtocolException e) {
            failedLogsWithReason(list,"ClientProtocolException");
            ids = null;
            e.printStackTrace();
        } catch (IOException e) {
            failedLogsWithReason(list,"IOException");
            ids = null;
            e.printStackTrace();
        } catch (Exception e) {
            failedLogsWithReason(list,e.getLocalizedMessage());
            ids = null;
        }
        return ids;
    }

    private void failedLogsWithReason(List<TraceAspectLogModelDb> traceAspectLogModelDbs, String localizedMessage) {
        for(TraceAspectLogModelDb item: traceAspectLogModelDbs){
            if(item.status== LogstatusEnum.FAILED.status){
                item.message=item.message+" failed log reason"+localizedMessage;
            }
            item.status= LogstatusEnum.FAILED.status;
            logs.getLogDatabase().update(item);
        }
    }

    @Override
    protected void onPostExecute(String[] v) {
        super.onPostExecute(v);
    }
}
