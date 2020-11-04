package com.game.onecricket.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.game.onecricket.APICallingPackage.retrofit.ApiClient;
import com.game.onecricket.APICallingPackage.retrofit.ApiInterface;
import com.game.onecricket.APICallingPackage.retrofit.group.CreateGroupResponse;
import com.game.onecricket.R;
import com.game.onecricket.activity.MatchOddsTabsActivity;
import com.game.onecricket.adapter.MatchesAdapter;
import com.game.onecricket.pojo.MatchesInfo;
import com.game.onecricket.utils.CommonProgressDialog;
import com.game.onecricket.utils.DateFormat;
import com.game.onecricket.utils.NetworkState;
import com.game.onecricket.utils.SessionManager;
import com.game.onecricket.utils.crypto.AlertDialogHelper;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.CLIPBOARD_SERVICE;

public class InProgressMatchesFragment extends Fragment implements MatchesAdapter.ClickListener {

    private static final String TAG = "MatchesFragment";
    private RecyclerView recyclerView;
    private Context context;
    private AlertDialog progressAlertDialog;
    private List<MatchesInfo> matchesInfoList;
    private AlertDialogHelper alertDialogHelper;
    private TextView nodataView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        recyclerView = view.findViewById(R.id.matches);
        nodataView   = view.findViewById(R.id.no_data_view);
        alertDialogHelper = AlertDialogHelper.getInstance();

        progressAlertDialog = CommonProgressDialog.getProgressDialog(context);
        if (NetworkState.isNetworkAvailable(context)) {
            callMatchesAPI();
        }
        else {
            if (!alertDialogHelper.isShowing()) {
                alertDialogHelper.showAlertDialog(context,
                        getString(R.string.internet_error_title),
                        getString(R.string.no_internet_message));
            }
        }
        return view;
    }

    private void dismissProgressDialog(AlertDialog progressAlertDialog) {
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            progressAlertDialog.dismiss();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void callMatchesAPIRetrofit() {
//        final ApiInterface      apiService = RetrofitClient.getClient().create(ApiInterface.class);
//        Call<ResponseBody> call       = apiService.getMatchesData();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call,
//                                   @NonNull retrofit2.Response<ResponseBody> response)
//            {
//                Log.d(TAG, response.toString());
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                Log.d(TAG, "response.toString()");
//            }
//        });

    }

    private void callMatchesAPI() {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = ApiClient.BASE_URL +  ":4040/inplay/matches";
//        String URL = "https://api.b365api.com/v1/betfair/sb/inplay?sport_id=4&token=61925-2bBIpJrOkeLtND";

        Log.d(TAG, URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                                                             URL ,
                                                             null,
                response -> {

/*                     String responseStr = "{\n" +
                            "  \"success\": 1,\n" +
                            "  \"pager\": {\n" +
                            "    \"page\": 1,\n" +
                            "    \"per_page\": 50,\n" +
                            "    \"total\": 2\n" +
                            "  },\n" +
                            "  \"results\": [\n" +
                            "    {\n" +
                            "      \"id\": \"29971937\",\n" +
                            "      \"sport_id\": \"4\",\n" +
                            "      \"time\": \"1598634000\",\n" +
                            "      \"time_status\": \"1\",\n" +
                            "      \"home\": {\n" +
                            "        \"id\": \"101516\",\n" +
                            "        \"name\": \"England\"\n" +
                            "      },\n" +
                            "      \"away\": {\n" +
                            "        \"id\": \"101132\",\n" +
                            "        \"name\": \"Pakistan\"\n" +
                            "      },\n" +
                            "      \"our_event_id\": \"2661294\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": \"29978134\",\n" +
                            "      \"sport_id\": \"4\",\n" +
                            "      \"time\": \"1598641200\",\n" +
                            "      \"time_status\": \"1\",\n" +
                            "      \"league\": {\n" +
                            "        \"id\": \"11893330\",\n" +
                            "        \"name\": \"T20 Blast\"\n" +
                            "      },\n" +
                            "      \"home\": {\n" +
                            "        \"id\": \"122456\",\n" +
                            "        \"name\": \"Birmingham Bears\"\n" +
                            "      },\n" +
                            "      \"away\": {\n" +
                            "        \"id\": \"113564\",\n" +
                            "        \"name\": \"Somerset\"\n" +
                            "      },\n" +
                            "      \"our_event_id\": \"2661553\"\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";*/

                    dismissProgressDialog(progressAlertDialog);
                    Log.d(TAG, "callPointsScanAPI response: " + response.toString());
                    matchesInfoList = new ArrayList<>();
                    try {
//                         JSONObject responseJson = new JSONObject(responseStr);
                        JSONArray resultsArray = response.getJSONArray("results");
                        for (int i = 0; i < resultsArray.length(); i++) {
                            MatchesInfo matchesInfo = new MatchesInfo();
                            JSONObject results = resultsArray.getJSONObject(i);

                            if (results.has("id")) {
                                String id = results.getString("id");
                                matchesInfo.setId(id);
                            }

                            if (results.has("league")) {
                                JSONObject leagueJSON = results.getJSONObject("league");
                                String leagueName = leagueJSON.getString("name");
                                matchesInfo.setLeagueName(leagueName);
                            }
                            else {
                                matchesInfo.setLeagueName("");
                            }

                            if (results.has("time")) {
                                String time = results.getString("time");
                                matchesInfo.setTime(DateFormat.getReadableTimeFormat(time));
                                matchesInfo.setDate(DateFormat.getReadableDateFormat(time));
                                matchesInfo.setDateTime(DateFormat.getReadableDateFormat(time));
                            }

                            matchesInfo.setMatchInProgress(true);

                            JSONObject homeJSON = results.getJSONObject("home");
                            String name = homeJSON.getString("name");
                            matchesInfo.setHomeTeam(name);

                            JSONObject awayJSON = results.getJSONObject("away");
                            String away = awayJSON.getString("name");
                            matchesInfo.setVisitorsTeam(away);
                            matchesInfoList.add(matchesInfo);
                        }

                        if (matchesInfoList.size() > 0) {
                            nodataView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            MatchesAdapter adapter = new MatchesAdapter(matchesInfoList, "In-Play");
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setHasFixedSize(true);
                            adapter.setRecyclerViewItemClickListener(InProgressMatchesFragment.this);
                            recyclerView.setAdapter(adapter);
                        }
                        else {
                            nodataView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    dismissProgressDialog(progressAlertDialog);
                    Log.e(TAG, "Error: " + error.getMessage());
                })
        {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(jsonObjReq);

    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(getActivity(), MatchOddsTabsActivity.class);
        intent.putExtra("MatchInfo", matchesInfoList.get(position));
        startActivity(intent);
    }

    private int selectedPosition;
    @Override
    public void onCreateGroupClicked(int position) {
        selectedPosition = position;
        showCreateGroupNameAlert(position);
    }

    @Override
    public void onCodeClicked(int position) {

    }

    private void showCreateGroupNameAlert(int position) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.group_name);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                    String userInputString = userInput.getText().toString();
                    createGroup(userInputString, position);
                })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void createGroup(String groupName, int position) {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        SessionManager sessionManager = new SessionManager();;
        ApiInterface apiInterface = ApiClient.getClientWithAuthorisation(sessionManager.getUser(context).getToken()).create(ApiInterface.class);

        MatchesInfo matchesInfo = matchesInfoList.get(position);

        JSONObject inputJSON = new JSONObject();
        try {



            inputJSON.put("contest_name", groupName);
            inputJSON.put("home_team", matchesInfo.getHomeTeam());
            inputJSON.put("visitor_team", matchesInfo.getVisitorsTeam());
            inputJSON.put("match_date", matchesInfo.getDate());
            inputJSON.put("match_time", matchesInfo.getTime());
            inputJSON.put("fi_id", matchesInfo.getId());
            inputJSON.put("status", "inplay");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Observable<CreateGroupResponse> observable = apiInterface.createGame(ApiClient.getRequestBody(inputJSON));
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::onSuccessResponse, this::onErrorResponse);
    }

    private void onErrorResponse(Throwable throwable) {
        dismissProgressDialog(progressAlertDialog);
        Log.d(TAG, throwable.getMessage());
    }

    private void onSuccessResponse (CreateGroupResponse responseBody){
        dismissProgressDialog(progressAlertDialog);

        MatchesInfo matchesInfo = matchesInfoList.get(selectedPosition);
        matchesInfo.setContest("private");
        matchesInfo.setPrivateId(responseBody.getData());
        String boldText=responseBody.getData();
        String message;
        int responsecode=Integer.parseInt(responseBody.getResponsecode());
        if(responsecode==500){
            message=responseBody.getMessage();

            new FancyGifDialog.Builder((Activity) context)
                    .setTitle("pls check groups!")

                    .setMessage(String.valueOf(message))
                    .setNegativeBtnBackground("#FF4081")
                    .setNegativeBtnText("ok")
                    .setGifResource(R.drawable.common_gif)
                    .isCancellable(true)
                    .OnNegativeClicked(() -> {

                    })
                    .build();

        }
        else {
            String normalText = "you have created an private game, friends can join with code:";
            SpannableString str = new SpannableString(normalText + "\n\n\n" + boldText);
            str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            new FancyGifDialog.Builder((Activity) context)
                    .setTitle("Hooray!")

                    .setMessage(String.valueOf(str))
                    .setPositiveBtnBackground("#FF4081")
                    .setPositiveBtnText("Share Code")
                    .setGifResource(R.drawable.common_gif)
                    .isCancellable(true)
                    .setNegativeBtnText("Copy code")
                    .OnPositiveClicked(() -> {
                        onShareClicked(responseBody.getData());
                    })
                    .OnNegativeClicked(() -> {
                        copyCodeInClipBoard(this.getContext(), responseBody.getData(), "Copied");
                    })

                    .build();
        }
    }

    public static void copyCodeInClipBoard(Context context, String text, String label) {
        if (context != null) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, text);
            if (clipboard == null || clip == null)
                return;
            clipboard.setPrimaryClip(clip);
            CharSequence text1 ="code Copied"+ "\n"+text;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text1, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

            toast.show();

        }
    }

    private void onShareClicked (String code) {
          /*  Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "I made predictions on 1Cricket App. Join me if you are interested.");
            intent.setType("text/plain");
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
            }

*/

        Picasso.get().load("http://1cricket.in/assets/img/hexa.png").into(new Target() {

            @Override

            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, "I invite you to join with Code and fun with Cricket Prediction." +
                        "\n"+ code+"\n"+"www.1cricket.in");
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                context.startActivity(Intent.createChooser(i, "Share using"));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }



            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });



    }

    private void gotoMatchOdds ( int position){
        Intent intent = new Intent(getActivity(), MatchOddsTabsActivity.class);
        intent.putExtra("MatchInfo", matchesInfoList.get(position));
        startActivity(intent);
    }

    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 50, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
