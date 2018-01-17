import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import models.Fibonacci;
import play.Logger;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.test.*;
import static play.test.Helpers.*;

public class ApplicationLiveTest{
    private static final String BASE_URL = "http://localhost:9000";


/*
    @Test
    public void whenCreatesRecord_thenCorrect() {
        Fibonacci fibonacci = new Fibonacci(5);
        JSONArray obj = new JSONArray(makeRequest(BASE_URL, "POST",  new JSONObject(fibonacci) ));
        assertTrue(obj.getBoolean(0*//*"isSuccessfull"*//*));// assertTrue проверяет, является ли результат выражения верным.
        JSONArray body = obj.getJSONArray(1*//*"body"*//*);
        assertEquals(fibonacci.getParameterN(), body.getInt(2*//*"parameter"*//*));
        assertEquals(fibonacci.getSequence(), body.getString(3*//*"Sequence"*//*));
    }*/

    @Test
    public void whenCreatesRecord_thenCorrect() {
        Fibonacci fibonacci = new Fibonacci(5);
        JSONObject obj = new JSONObject(makeRequest(BASE_URL, "POST",  new JSONObject(fibonacci) ));
        assertTrue(obj.getBoolean("isSuccessfull"));// assertTrue проверяет, является ли результат выражения верным.
        JSONObject body = obj.getJSONObject("body");
        assertEquals(fibonacci.getParameterN(), body.getInt("parameter"));
        assertEquals(fibonacci.getSequence().toString(), body.getString("Sequence"));
    }













    @Test
    public void whenDeletesCreatedRecord_thenCorrect() {
        Fibonacci fibonacci = new Fibonacci(7);
        JSONObject ob1 = new JSONObject(makeRequest(BASE_URL, "POST", new JSONObject(fibonacci))).getJSONObject("body");
        int id = ob1.getInt("id");
        JSONObject obj1 = new JSONObject(makeRequest(BASE_URL + "/" + id, "POST", new JSONObject()));
        assertTrue(obj1.getBoolean("isSuccessfull"));
        makeRequest(BASE_URL + "/" + id, "DELETE", null);
        JSONObject obj2 = new JSONObject(makeRequest(BASE_URL + "/" + id, "POST", new JSONObject()));
        assertFalse(obj2.getBoolean("isSuccessfull"));
    }

    //В вышеприведенном тесте мы сначала создаем новую запись, успешно получаем ее
    // новый идентификатор , и затем удаляем ее.
    //Когда мы пытаемся снова получить один и тот же идентификатор ,
    // операция завершится неудачно, как ожидалось.

    @Test
    public void whenUpdatesCreatedRecord_thenCorrect() {
        Fibonacci fibonacci = new Fibonacci(8);
        JSONObject body1 = new JSONObject(makeRequest(BASE_URL, "POST", new JSONObject(fibonacci))).getJSONObject("body");
        assertEquals(fibonacci.getParameterN(), body1.getInt("parameter"));
        int newParam = 10;
        body1.put("parameter", newParam);
        JSONObject body2 = new JSONObject(makeRequest(BASE_URL, "PUT", body1)).getJSONObject("body");
        assertFalse(fibonacci.getParameterN() == body2.getInt("parameter"));
        assertTrue(newParam == body2.getInt("parameter"));
    }

    //Вышеупомянутый тест демонстрирует изменение значения поля параметра
    // после обновления записи.

    @Test
    public void whenGetsAllRecords_thenCorrect() {
        Fibonacci fibonacci1 = new Fibonacci(1);
        Fibonacci fibonacci2 = new Fibonacci(11);
        Fibonacci fibonacci3 = new Fibonacci(4);
        Fibonacci fibonacci4 = new Fibonacci(3);

        makeRequest(BASE_URL, "POST", new JSONObject(fibonacci1));
        makeRequest(BASE_URL, "POST", new JSONObject(fibonacci2));
        makeRequest(BASE_URL, "POST", new JSONObject(fibonacci3));
        makeRequest(BASE_URL, "POST", new JSONObject(fibonacci4));

        JSONObject objects = new JSONObject(makeRequest(BASE_URL, "GET", null));
        assertTrue(objects.getBoolean("isSuccessfull"));
        JSONArray array = objects.getJSONArray("body");
        assertTrue(array.length() >= 4);
    }


    //С помощью вышеуказанного теста мы выясняем
    // правильное функционирование действия контроллера listSequences .

    private static String makeRequest(String myUrl, String httpMethod, JSONObject  parameters) {

        URL url = null;
        try {
            url = new URL(myUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {

            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setDoInput(true);

        conn.setReadTimeout(300000); //не более чем 5 мин

        conn.setRequestProperty("Content-Type", "application/json");
        DataOutputStream dos = null;
        int respCode = 0;
        String inputString = null;
        try {
            conn.setRequestMethod(httpMethod);

            if (Arrays.asList("POST", "PUT").contains(httpMethod)) {
                String params = parameters.toString();

                conn.setDoOutput(true);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(params);
                dos.flush();
                dos.close();
            }
            respCode = conn.getResponseCode();
            if (respCode != 200 && respCode != 201) {
                String error = inputStreamToString(conn.getErrorStream());
                return error;
            }
            inputString = inputStreamToString(conn.getInputStream());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return inputString;
    }

    private static String inputStreamToString(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}