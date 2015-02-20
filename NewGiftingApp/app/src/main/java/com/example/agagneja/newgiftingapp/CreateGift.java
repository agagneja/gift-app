package com.example.agagneja.newgiftingapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;


public class CreateGift extends Activity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    EditText amount;
    String val;
    String cur;
    ImageButton camera_btn;
    ImageButton gallery_btn;
    Button preview;
    EditText msgText;
    String message;
    String picturePath;
    String cameraString;
    Bitmap bp;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static int RESULT_LOAD_IMAGE = 1;
    private Uri fileUri;
    private static final int CAMERA_CAPTURE = 100;
    private static final int GALLERY_UPLOAD = 200;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    static int flag =0;
    String name;
    ImageView showImg;
    Button back;
    Button send;
    String FUNDING_ID;
    String FULFIL_ID;
    String FULFIL_BODY;
    TextView tView;
    private static String imgval;
    String contact_Id;
   Uri photoPath;
    String photo;
    SimpleCursorAdapter mAdapter;
    MatrixCursor mMatrixCursor;
    MatrixCursor reader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gift);
        amount = (EditText) findViewById(R.id.amount);
        spinner = (Spinner) findViewById(R.id.spinner);
        camera_btn = (ImageButton) findViewById(R.id.camera_btn);
        gallery_btn = (ImageButton) findViewById(R.id.gallery_button);
        preview = (Button) findViewById(R.id.preview);
        msgText=(EditText) findViewById(R.id.msgText);
        showImg = (ImageView)findViewById(R.id.viewImg);
        back = (Button) findViewById(R.id.back_btn);
        send = (Button) findViewById(R.id.gift_send);
      //  tView = (TextView) findViewById(R.id.textView);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currencies_array,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        Bundle bd = getIntent().getExtras();
        if(bd!=null)
        {
            if(bd.getString("name")!= null)
            {
                name = bd.getString("name");
                //nameText.setText(name);
                Toast.makeText(this,("Selected user :"+name).toString(),Toast.LENGTH_SHORT).show();
                ActionBar actionBar = getActionBar();
                actionBar.setTitle(name);
                actionBar.setIcon(R.drawable.gift);

            }
        }

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                startActivityForResult(intent, CAMERA_CAPTURE);

            }
        });

        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
               // startActivityForResult(i,GALLERY_UPLOAD);
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val = amount.getText().toString();
                message = msgText.getText().toString();
                amount.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                msgText.setVisibility(View.INVISIBLE);
                camera_btn.setVisibility(View.INVISIBLE);
                gallery_btn.setVisibility(View.INVISIBLE);
                preview.setVisibility(View.INVISIBLE);
                showImg.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                send.setVisibility(View.VISIBLE);
                back.setEnabled(true);
                send.setEnabled(true);
                camera_btn.setEnabled(false);
                gallery_btn.setEnabled(false);
                preview.setEnabled(false);
                tView.setText("Here is what your gift looks like!!");

                if(flag == 1)
                {
                    showImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    Bitmap img = BitmapFactory.decodeFile(picturePath);
                    imgval= convertToString(img);
                    Log.e("Value1",imgval);
                }

                if(flag == 2)
                {
                    showImg.setImageBitmap(bp);
                    imgval= convertToString(bp);
                    Log.e("Value2",imgval);

                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amount.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                msgText.setVisibility(View.VISIBLE);
                camera_btn.setVisibility(View.VISIBLE);
                gallery_btn.setVisibility(View.VISIBLE);
                preview.setVisibility(View.VISIBLE);
                showImg.setVisibility(View.INVISIBLE);
                back.setVisibility(View.INVISIBLE);
                send.setVisibility(View.INVISIBLE);
                back.setEnabled(false);
                send.setEnabled(false);
                camera_btn.setEnabled(true);
                gallery_btn.setEnabled(true);
                preview.setEnabled(true);
                msgText.setText("");
                amount.setText("");
                tView.setText("Customize your gift here!!");

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateGiftCall().execute();

            }
        });

    }

    public String convertToString(Bitmap img)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

        }

        if (requestCode == CAMERA_CAPTURE) {

            if (resultCode == RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);
                bp = (Bitmap) data.getExtras().get("data");

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }


    }
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_gift, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();
        cur = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private class CreateGiftCall extends AsyncTask<Void, Void, String>
    {
        public String doInBackground(Void... params)
        {
            String body = getBodyCreateGift();
            JSONObject response = ConnectionUtils.createGift(body);
            if(response!=null)
            {
                return response.toString();
            }
            else {
                return null;
            }
        }

        public void onPostExecute(String result)
        {
            if(result!=null) {
                Log.d("Response:", result);
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                try
                {
                    JSONObject jobj = new JSONObject(result);
                    FUNDING_ID = jobj.getString("id");
                    Log.e("ID",FUNDING_ID);
                    new CreateFundingCall().execute();
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }

            }
            else
            {
                Log.d("Response:","failed");
                Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();


            }

        }
    }

    private class CreateFundingCall extends AsyncTask<Void, Void, String>
    {
        public String doInBackground(Void... params)
        {
            // String body = getBodyCreateGift();
            JSONObject response = ConnectionUtils.fundingOptionsRequest(FUNDING_ID);
            if(response!=null)
            {
                return response.toString();
            }
            else {
                return null;
            }
        }

        public void onPostExecute(String result)
        {
            if(result!=null) {
                Log.d("Response:", result);
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jobj = new JSONObject(result);
                    JSONArray jAry = jobj.getJSONArray("funding_options");
                    JSONObject job = (JSONObject)jAry.get(0);
                    FULFIL_ID = job.getString("id");
                    Log.e("FULFIL ID",FULFIL_ID);
                    new CreateFulfillmentCall().execute();

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
            else
            {
                Log.d("Response:","failed");
                Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();


            }

        }
    }

    private class CreateFulfillmentCall extends AsyncTask<Void, Void, String>
    {
        public String doInBackground(Void... params)
        {
            // String body = getBodyCreateGift();
            FULFIL_BODY = getBodyFulfill();
            JSONObject response = ConnectionUtils.fulfillmentOptionsRequest(FUNDING_ID, FULFIL_BODY);
            if(response!=null)
            {
                return response.toString();
            }
            else {
                return null;
            }
        }

        public void onPostExecute(String result)
        {
            if(result!=null) {
                Log.d("Response:", result);
                Toast.makeText(getApplicationContext(),"gift sent!",Toast.LENGTH_SHORT).show();
                amount.setText("");
               launchContacts();


            }
            else
            {
                Log.d("Response:","failed");
                Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();


            }

        }
    }
    public String getBodyCreateGift()
    {
        if(name == null)
        {
            name = "cdayanand-pre@paypal.com";
        }
        StringBuffer sb = new StringBuffer("{\"receiver\":{\"id\":\"");
        sb.append(name);
        sb.append("\",\"type\":\"EMAIL\"},\"amount\":{\"value\":\"");
        sb.append(val);
        sb.append("\",\"currency\":\"");
        sb.append(cur);
        sb.append("\"},\"template\":{\"id\":\"1\",\"background_color\":\"#ffeeff\"},\"comments\":[{\"message\":\"");
        sb.append(message);
        sb.append("\",\"location\":{\"latitude\":37.779568,\"longitude\":-122.41387,\"place\":\"San Francisco, CA\"},\"media\":{\"images\":[{\"image\":\"");
       // sb.append(imgval);
        sb.append("R0lGODlhAQABAIAAAP///////yH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==");
        sb.append("\"}],\"audio\":[],\"video\":[]}}]}");
        return sb.toString();
    }

    public String getBodyFulfill()
    {
        StringBuffer sb = new StringBuffer("{\"funding_option_id\":\"");
        sb.append(FULFIL_ID);
        sb.append("\"}");
        return sb.toString();
    }

    public void launchContacts()
    {
        Intent intent = new Intent(getApplicationContext(), ChooseContact.class);
        startActivity(intent);
    }



}
