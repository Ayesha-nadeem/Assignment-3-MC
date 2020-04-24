package pk.edu.pucit.assignment3;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    Context context;

    private ArrayList<String> maintitle;
    private ArrayList<String> level;
    private ArrayList<String> info;
    private ArrayList<String> imgid;
    private ArrayList<String> bookUrl;
    private  String bookName="";

    ProgressDialog mProgressDialog;
    //private Integer[] imgid;
   //ArrayList<String>  imgid
    //Integer[] imgid

    public RVAdapter(Context context, ArrayList<String> maintitle, ArrayList<String>  level,ArrayList<String>  info,ArrayList<String>  imgid,ArrayList<String> bookUrl ) {

        // declare the dialog as a member field of your activity
        this.context = context;

// instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(this.context);
        mProgressDialog.setMessage("downloading...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);


        this.maintitle= new ArrayList<String>(maintitle.size());
        for(String i : maintitle){

            this.maintitle.add(new String(i));
        }
        this.info= new ArrayList<String>(info.size());
        for(String i : info){

            this.info.add(new String(i));
        }
        //this.maintitle=maintitle.clone();
        //this.maintitle = maintitle;
        this.imgid = new ArrayList<String>(imgid.size());
        for(String i : imgid){
            this.imgid.add(new String(i));
        }
        //this.imgid=imgid;
        this.level= new ArrayList<String>(level.size());
        for(String i : level){
           this.level.add(new String(i));
        }
        this.bookUrl= new ArrayList<String>(bookUrl.size());
        for(String i : bookUrl){
            this.bookUrl.add(new String(i));
        }
//        ImageView one = (ImageView)findViewById(R.id.imageView);
//        one.setImageBitmap(bitmap);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        ViewHolder holder = new ViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_title.setText(maintitle.get(position));
        holder.tv_level.setText(level.get(position));
        holder.tv_info.setText(info.get(position));
//        Picasso.get().load("https://github.com/revolunet/PythonBooks/blob/master/img/BMH.png").into(holder.iv_icon);
        Picasso.with(context).load("https://github.com/revolunet/PythonBooks/blob/master/img/Learning-IPython-for-Interactive-Computing-and-Data-Visualization-Second-Edition.png").into(holder.iv_icon);

        //new DownloadImageTask(holder.iv_icon).execute(imgid.get(position));
       // ImageView imgView = new ImageView(this);
//        String img=imgid.get(position).substring(2);
//        InputStream is = getClass().getResourceAsStream("/res/drawable" + img);
//        holder.iv_icon.setImageDrawable(Drawable.createFromStream(is, ""));
        //holder.iv_icon.setImageResource(R.drawable.a);
        //holder.iv_icon.setImageResource(imgid.get(position));
       // holder.iv_icon.setImageBitmap(imgid.get(position));

        Log.e("title at pos-"+position,imgid.get(position));
        //Log.e("title at pos-"+position,imgid.get(position));
        String a=bookUrl.get(position);
        String asub=a.substring(a.length() - 3, a.length());
        if(asub.equals("pdf") || asub.equals("zip") )
        {
            holder.btn_book.setText("DOWNLOAD");
        }
        else
        {
            holder.btn_book.setText("READ ONLINE");
        }

        holder.btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=bookUrl.get(position);
               String sub=s.substring(s.length() - 3, s.length());
                if(sub.equals("pdf") || sub.equals("zip"))
                {
                    bookName=maintitle.get(position);
                    new DownloadFileFromURL().execute(s);
                    //Toast.makeText(context, sub, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Uri uri = Uri.parse(bookUrl.get(position)); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return maintitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public TextView tv_level;
        public TextView tv_info;
        public ImageView iv_icon;
        public Button btn_book;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.title);
            tv_level = itemView.findViewById(R.id.level);
            tv_info = itemView.findViewById(R.id.info);
            iv_icon = itemView.findViewById(R.id.imageView);
            btn_book=itemView.findViewById(R.id.button);
        }
    }
//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap bmp = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                bmp = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return bmp;
//        }
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }
//    public class DownloadService extends IntentService {
//        public static final int UPDATE_PROGRESS = 8344;
//
//        public DownloadService() {
//            super("DownloadService");
//        }
//        @Override
//        protected void onHandleIntent(Intent intent) {
//
//            String urlToDownload = intent.getStringExtra("url");
//            ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
//            try {
//
//                //create url and connect
//                URL url = new URL(urlToDownload);
//                URLConnection connection = url.openConnection();
//                connection.connect();
//
//                // this will be useful so that you can show a typical 0-100% progress bar
//                int fileLength = connection.getContentLength();
//
//                // download the file
//                InputStream input = new BufferedInputStream(connection.getInputStream());
//
//                String path = "/sdcard/BarcodeScanner-debug.apk" ;
//                OutputStream output = new FileOutputStream(path);
//
//                byte data[] = new byte[1024];
//                long total = 0;
//                int count;
//                while ((count = input.read(data)) != -1) {
//                    total += count;
//
//                    // publishing the progress....
//                    Bundle resultData = new Bundle();
//                    resultData.putInt("progress" ,(int) (total * 100 / fileLength));
//                    receiver.send(UPDATE_PROGRESS, resultData);
//                    output.write(data, 0, count);
//                }
//
//                // close streams
//                output.flush();
//                output.close();
//                input.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Bundle resultData = new Bundle();
//            resultData.putInt("progress" ,100);
//
//            receiver.send(UPDATE_PROGRESS, resultData);
//        }
//    }


    //download class

    class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }
        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());

                final int PERMISSION_REQUEST_CODE = 12345;
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

                //Log.i("Filesdir", getFilesDir().getAbsolutePath());
                //File file = new File(getFilesDir().getAbsolutePath() + "/first_file.txt");
                // Output stream
                OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+bookName+".pdf");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress((int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
        }
    }


}