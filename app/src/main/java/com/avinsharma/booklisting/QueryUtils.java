package com.avinsharma.booklisting;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Avin on 11-11-2016.
 */
public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public static String baseURL = "https://www.googleapis.com/books/v1/volumes?q=";

    private QueryUtils() {
    }

    private static String createStringUrl(String search) {
        StringBuilder stringUrl = new StringBuilder(baseURL);
        stringUrl.append(search);
        stringUrl.append("&maxResults=8");
        return stringUrl.toString();
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating Url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Book> extractBooks(String jsonResponse) {
        ArrayList<Book> books = new ArrayList<>();
        if (jsonResponse == null)
            return books;

        try {

            JSONObject root = new JSONObject(jsonResponse);
            JSONArray items = root.getJSONArray("items");

            JSONObject info;
            JSONArray authorsJson;
            String title;
            String description;
            StringBuilder author;

            for (int i = 0; i < items.length(); i++) {
                info = items.getJSONObject(i).getJSONObject("volumeInfo");
                title = info.getString("title");
                if (info.has("description"))
                    description = info.getString("description");
                else
                    description = "No description";
                if (info.has("authors")) {
                    authorsJson = info.getJSONArray("authors");
                    author = new StringBuilder(authorsJson.getString(0));
                    for (int j = 1; j < authorsJson.length(); j++) {
                        author.append("\n" + authorsJson.getString(j));
                    }
                } else
                    author = new StringBuilder("No authors");
                books.add(new Book(title, description, author.toString()));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return books;
    }

    public static ArrayList<Book> fetchSearchResults(String search) {

        Log.d(LOG_TAG, createStringUrl(search));
        URL url = createUrl(createStringUrl(search));
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error getting json response", e);
        }
        return extractBooks(jsonResponse);
    }
}
