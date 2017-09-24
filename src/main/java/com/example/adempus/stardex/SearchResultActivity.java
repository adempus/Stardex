package com.example.adempus.stardex;

import com.example.adempus.stardex.buildercomponents.StarBuilder;
import com.example.adempus.stardex.starproperties.Star;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearchResultActivity extends AppCompatActivity
{
    private TextView starName;
    private TextView descriptionTag;
    private TextView starData;
    private TextView errorText;
    private Typeface starNameFont;
    private Typeface starDataFont;
    private ProgressDialog progressDialog;
    private StarInfoDownloader infoDownloader;
    private JSONObject responseJSON;
    private StarBuilder starBuilder;
    private Star star;
    private ImageView starImage;
    private String baseInfoURL, baseImgURL;
    private AtomicBoolean error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initFonts();

        // gather text view resources
        starName = (TextView) findViewById(R.id.starName);
        descriptionTag = (TextView) findViewById(R.id.descriptionTag);
        starData = (TextView) findViewById(R.id.starData);
        errorText = (TextView) findViewById(R.id.errorMessage);

        // initialize text view fonts
        starData.setTypeface(starDataFont);
        descriptionTag.setTypeface(starDataFont);
        starName.setTypeface(starNameFont);
        errorText.setTypeface(starDataFont);

        // initialize text view font sizes
        starName.setTextSize(42);
        descriptionTag.setTextSize(13);
        starData.setTextSize(17);
        starImage = (ImageView) findViewById(R.id.starView);


        error = new AtomicBoolean();
        // initialize error text message, and default visibility to gone.
        errorText.setText("Star not found.");
        errorText.setVisibility(View.GONE);

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            queryStarByName(getIntent().getStringExtra(SearchManager.QUERY));
            error.set(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) { }

    protected void displaySearchResponse() {
        try {
            star = buildStar();
            starName.setText(star.getName());
            descriptionTag.setText(star.getTypeDescription());
            setColorIndicator(star.getColorValue());
            starData.setText(star.getData());
            System.out.println(star);
        } catch ( JSONException | NullPointerException ex ) {
            ex.printStackTrace();
            errorText.setVisibility(View.VISIBLE);
            System.err.println("Star not found.");
        }
    }

    protected void displaySearchImage(Bitmap image) {
        starImage.setImageBitmap(image);
    }

    private Star buildStar() throws JSONException {
        starBuilder = new StarBuilder();
        starBuilder.initStarAttributesFromJSON(responseJSON);
        return starBuilder.createStar();
    }

    public void setColorIndicator(int color) {
        Drawable indicator = ContextCompat.getDrawable(this, R.drawable.indicator);
        descriptionTag.setCompoundDrawablesWithIntrinsicBounds(indicator, null, null, null);
        GradientDrawable indicatorColor = (GradientDrawable)  indicator.getCurrent();
        indicatorColor.setColor(color);
    }

    public void setResponseJSON (JSONObject obj) {
        responseJSON = obj;
    }

    public void initFonts () {
        starDataFont = Typeface.createFromAsset(getAssets(),
                "fonts/Open_Sans/OpenSans-Light.ttf");
        starNameFont = Typeface.createFromAsset(getAssets(),
                "fonts/Julius_Sans_One/JuliusSansOne-Regular.ttf" );
    }

    public void queryStarByName(String key) {
        infoDownloader = new StarInfoDownloader(key, SearchResultActivity.this);
        infoDownloader.execute();
    }

    /**
     * Downloads the JSON data result pertaining to a star from the URL query.
     * @return a string of the JSON data retrieved from the URL.
     * @throws IOException if the query returned an invalid result (star not found).
     */
    private String downloadStarData() throws IOException {
        String response = "";
        BufferedReader br = null;
        try {
            URL url = new URL(baseInfoURL);
            try {
                String line = "";
                br = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                while ((line = br.readLine()) != null)
                    response += "\n" + line;
            } catch (IOException IOEx) {
                throw new IOException();
            }
        } catch (IOException IOEx) {
            throw new IOException();
        }
        finally {
            br.close();
        }
        return response;
    }

    private void downloadStarImage() {
        if (!error.get()) {
            new StarImageDownloader().execute(baseImgURL);
        }
    }

    /**
     * StarInfoDownloader accesses methods via URL links to download basic star properties in JSON from the
     * Astropical.space API. The JSON is parsed by the StarBuilder class to create Star objects.
     * An AsyncTask is a worker thread separate from the activity that performs operations in the background
     * to prevent freezing the UI. In this case, performing network operations to retrieve data.
     *
     */
    private class StarInfoDownloader extends AsyncTask<Void, Void, JSONObject>
    {
        private final String dataURLFragment1, dataURLFragment2, imgURLFragment1, imgURLFragment2;
        private String jsonResponse, queryKey;
        private JSONObject starJSONData;
        private SearchResultActivity resultActivity;

        public StarInfoDownloader(String key, SearchResultActivity resultActivity) {
            super();
            this.queryKey = key;
            this.resultActivity = resultActivity;
            this.dataURLFragment1 = "http://www.astropical.space/astrodb/api.php?table=stars&which=name&limit=";
            this.dataURLFragment2 = "&format=json";
            this.imgURLFragment1 = "http://alasky.u-strasbg.fr/hips-thumbnails/thumbnail?object=";
            this.imgURLFragment2 = "&fov=1.3&width=450&height=230";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(resultActivity);
            progressDialog.setMessage("Searching star...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                initURLs(queryKey);
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
            return starJSONData;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            // dismiss waiting dialog;
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            // check if results are valid before building a star.
            resultActivity.setResponseJSON(result);
            displaySearchResponse();
            downloadStarImage();
        }

        /**
         * Initializes base URLs for JSON data and image retrieval.
         * @param searchKey is the name of a star to search.
         */
        public void initURLs(String searchKey) throws IOException, JSONException{
            queryKey = searchKey;
            baseInfoURL = buildInfoURL(searchKey);
            baseImgURL = buildImageURL(searchKey);
            connectToBaseURLs();
        }

        /**
         * @param searchKey is the name of a star to search for.
         * @return a string of the newly created base URL for retrieving JSON data.
         */
        private String buildInfoURL(String searchKey) {
            if (searchKey.contains(" ")) {
                searchKey = searchKey.replaceAll("\\s", "%20");
            }
            return dataURLFragment1+searchKey +dataURLFragment2;
        }

        /**
         * @param searchKey is the image of a star to search for.
         * @return a string of the newly created base URL for retrieving an image.
         */
        private String buildImageURL(String searchKey) {
            return imgURLFragment1+searchKey+imgURLFragment2;
        }

        /**
         * Invokes methods for retrieval of JSON and jpg image data for a queried star.
         * Checks JSON response for errors to prevent invalid image downloads.
         * Image retrieval runs concurrently to speed things up.
         */
        private void connectToBaseURLs() throws JSONException {
            try {
                jsonResponse = downloadStarData();
                extractInitialStarData();
            } catch (IOException | RuntimeException ex) {
                error.set(true);
            }
        }


        /**
         * Parses available properties of a queried star from the JSON response.
         * 'star0' (the first element) is a key to an object pair in the 'hipstars' object,
         * which contains the basic properties to parse.
         *
         * @throws RuntimeException on a failed attempt to parse the JSON.
         *                          (Usually means the star wasn't found)
         */
        private void extractInitialStarData() throws RuntimeException, JSONException {
            JSONObject starData;
            try {
                jsonResponse = jsonResponse.replaceAll(" ,", "null,");
                starData = new JSONObject(jsonResponse)
                        .getJSONObject("hipstars")
                        .getJSONObject("star1");
            } catch (RuntimeException REx) {
                throw new RuntimeException();
            }
            starJSONData = starData;
            System.out.println(starJSONData);
        }
    }


    private ProgressDialog imgProgressDialog;
    private AtomicBoolean imageError;

    /**
     * Opens a stream to the URL of the queried star's image to download in jpg format.
     * The file is saved in folder: '/resources/star_images' to be accessed for display
     * and persistence.
     * TODO: These images are persisted to the user's account. (rpi db)
     * */
    private class StarImageDownloader extends AsyncTask<String, Void, Bitmap>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageError = new AtomicBoolean();
            imageError.set(false);
            imgProgressDialog = new ProgressDialog(SearchResultActivity.this);
            imgProgressDialog.setMessage("Retrieving image...");
            imgProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String ... url) {
            String imgURL = url[0];
            Bitmap bitmap = null;

            try {
                InputStream input = new java.net.URL(imgURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception ex) {
                ex.printStackTrace();
                imageError.set(true);
                imgProgressDialog.setMessage("Image not found");
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (!imageError.get()) {
                displaySearchImage(result);
            }
            imgProgressDialog.dismiss();
        }
    }
}
