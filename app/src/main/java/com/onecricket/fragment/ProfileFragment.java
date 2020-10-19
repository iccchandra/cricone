package com.onecricket.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.onecricket.APICallingPackage.Class.APIRequestManager;
import com.onecricket.APICallingPackage.Config;
import com.onecricket.APICallingPackage.Interface.ResponseManager;
import com.onecricket.Bean.UserDetails;
import com.onecricket.activity.AddCashActivity;
import com.onecricket.activity.EditProfileActivity;
import com.onecricket.activity.GlobalLeaderActivity;
import com.onecricket.activity.HomeActivity;
import com.onecricket.activity.InviteFriendsActivity;
import com.onecricket.activity.InvitedFriendListActivity;
import com.onecricket.activity.MainActivity;
import com.onecricket.databinding.FragmentProfileBinding;
import com.onecricket.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.onecricket.APICallingPackage.Class.Validations.ShowToast;
import static com.onecricket.APICallingPackage.Config.MYACCOUNT;
import static com.onecricket.APICallingPackage.Config.MYPLAYINGHISTORY;
import static com.onecricket.APICallingPackage.Config.UpdateUserProfileImage;
import static com.onecricket.APICallingPackage.Constants.MYACCOUNTTYPE;
import static com.onecricket.APICallingPackage.Constants.MYPLAYINGHISTORYTYPE;
import static com.onecricket.APICallingPackage.Constants.UpdateProfileImage;


public class ProfileFragment extends Fragment implements ResponseManager {


    private static final String TAG = "ProfileFragment";
    SessionManager sessionManager;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;


    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    HomeActivity activity;
    Context context;
    String Deposited, Winnings, Bonus;


    int PICK_IMAGE_GALLERY = 100;
    int PICK_IMAGE_CAMERA = 101;

    Bitmap bitmap;

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        context = activity = (HomeActivity) getActivity();


        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(getActivity());


        binding.tvProfileChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowToast(context, "Not Available in Demo Version");
               /* Intent i = new Intent(getActivity(), NewPasswordActivity.class);
                i.putExtra("UserId", HomeActivity.sessionManager.getUser(getContext()).getUser_id());
                i.putExtra("IntentActivity", "ChangePassword");
                startActivity(i);*/
            }
        });

        binding.tvProfileLogout.setOnClickListener(view -> Logout());
        binding.tvProfileYourMail.setText(HomeActivity.sessionManager.getUser(getContext()).getEmail() + "");
        String UserEmail = HomeActivity.sessionManager.getUser(getContext()).getEmail();
        String Imageurl = HomeActivity.sessionManager.getUser(getContext()).getImage();
        Log.e("Imageurl", Config.ProfileIMAGEBASEURL + Imageurl);
        if (TextUtils.isEmpty(Imageurl) || Imageurl.equals("")) {

        } else {
            Glide.with(getActivity()).load(Config.ProfileIMAGEBASEURL + Imageurl)
                    .into(binding.imProfilepic);
        }
        if (UserEmail.equals("")) {
            binding.tvProfileUserName.setText(sessionManager.getUser(context).getMobile());
        } else {
            binding.tvProfileUserName.setText(UserEmail);
        }

        binding.tvProfileAccount.setOnClickListener(view -> {
            /*Intent i = new Intent(getActivity(), MyAccountActivity.class);
            startActivity(i);*/
        });

        binding.tvProfileAddBalance.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), AddCashActivity.class);
            startActivity(i);
        });
        binding.tvProfileView.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(i);
        });
        binding.tvInviteFriends.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), InviteFriendsActivity.class);
            startActivity(i);
        });
        binding.tvMyFriendsList.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), InvitedFriendListActivity.class);
            startActivity(i);
        });
        binding.LLGlobalRanking.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), GlobalLeaderActivity.class);
            startActivity(i);
        });
        binding.imProfilepic.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions();
            } else {
                ChooseImageDialog();
            }
        });

        callMyAccountDetails(false);
        callMyHistory(false);


        return binding.getRoot();
    }


    public void ChooseImageDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
        builderSingle.setTitle("Choose Image");
        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(activity,
                        android.R.layout.simple_list_item_1);
        arrayAdapter.add("Camera");
        arrayAdapter.add("Gallery");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                dialog.dismiss();
                if (strName.equals("Gallery")) {
                    bitmap = null;
                    binding.imProfilepic.setVisibility(View.GONE);
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
                } else {
                    bitmap = null;
                    binding.imProfilepic.setVisibility(View.GONE);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);
                }
            }
        });
        builderSingle.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_GALLERY &&
                resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.
                        getBitmap(activity.getContentResolver(), filePath);
                binding.imProfilepic.setVisibility(View.VISIBLE);
                binding.imProfilepic.setImageBitmap(bitmap);
                callUploadDoc(true);
                Log.e("Image Path", "onActivityResult: " + filePath);
            } catch (Exception e) {
                bitmap = null;
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                binding.imProfilepic.setVisibility(View.VISIBLE);
                binding.imProfilepic.setImageBitmap(bitmap);
                callUploadDoc(true);
                Log.e("Image Path", "onActivityResult: ");
            } catch (Exception e) {
                bitmap = null;
                e.printStackTrace();
            }
        }

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context = activity = (HomeActivity) getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) (context = activity = (HomeActivity) getActivity()),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), 100);
            return false;
        } else {
            ChooseImageDialog();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ChooseImageDialog();
            } else {

            }
            return;
        }
    }

    private void callMyAccountDetails(boolean isShowLoader) {
        try {
            apiRequestManager.callAPIWithAuthorization(MYACCOUNT,
                    createRequestJsonWin(), context, activity, MYACCOUNTTYPE,
                    isShowLoader, responseManager, sessionManager.getUser(context).getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJsonWin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", HomeActivity.sessionManager.getUser(context).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void callMyHistory(boolean isShowLoader) {
        try {
            apiRequestManager.callAPIWithAuthorization(MYPLAYINGHISTORY,
                    createRequestJsonHistory(), context, activity, MYPLAYINGHISTORYTYPE,
                    isShowLoader, responseManager, sessionManager.getUser(context).getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJsonHistory() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", HomeActivity.sessionManager.getUser(context).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void callUploadDoc(boolean isShowLoader) {
        try {
            apiRequestManager.callAPIWithAuthorization(UpdateUserProfileImage,
                    createRequestJson(), context, activity, UpdateProfileImage,
                    isShowLoader, responseManager, sessionManager.getUser(context).getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            sessionManager = new SessionManager();
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());

            jsonObject.put("profile_image", getStringImage(bitmap));
            jsonObject.put("token", sessionManager.getUser(context).getToken());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {


        if (type.equals(MYPLAYINGHISTORYTYPE)) {
            Log.d("ProfileFragment: MYP", result.toString());
            try {
                String wins = result.getString("wins");
                String series = result.getString("series");
                String contest = result.getString("contest");
                String matchs = result.getString("matchs");
                binding.tvJoinedContest.setText(contest);
                binding.tvJoinedMatches.setText(matchs);
                binding.tvJoinedSeries.setText(series);
                binding.tvWins.setText(wins);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type.equals(UpdateProfileImage)) {

            try {
                String imageurl = result.getString("data");
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.commit();
                UserDetails userDetails = new UserDetails();
                userDetails.setUser_id(HomeActivity.sessionManager.getUser(getContext()).getUser_id());
                userDetails.setName(HomeActivity.sessionManager.getUser(getContext()).getName());
                userDetails.setMobile(HomeActivity.sessionManager.getUser(getContext()).getMobile());
                userDetails.setEmail(HomeActivity.sessionManager.getUser(getContext()).getEmail());
                userDetails.setType(HomeActivity.sessionManager.getUser(getContext()).getType());
                userDetails.setReferral_code(HomeActivity.sessionManager.getUser(getContext()).getReferral_code());
                userDetails.setImage(imageurl);

                userDetails.setVerify(HomeActivity.sessionManager.getUser(getContext()).getVerify());
                sessionManager.setUser(context, userDetails);
                Log.e("picmgs", imageurl);
                Log.e("result", result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("ProfileFragment", result.toString());
            try {
                Deposited = result.getString("credit_amount");
                Winnings = result.getString("amount_in_hand");
                Bonus = result.getString("amount_in_play");
                binding.tvProfileDeposited.setText(Deposited);
                binding.tvProfileWinning.setText(Winnings);
                binding.tvProfileBonus.setText(Bonus);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onError(Context mContext, String type, String message) {
        Log.d(TAG, message);
    }

    public void Logout() {
        LoginManager.getInstance().logOut();
        loginPrefsEditor.clear();
        loginPrefsEditor.commit();
        Auth.GoogleSignInApi.revokeAccess(HomeActivity.mGoogleApiClient).setResultCallback(status -> {});
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);

    }
}
