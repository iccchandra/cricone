package com.game.onecricket.APICallingPackage;


public class Config {

//    public static String SERVERURL = "http://13.232.85.74/";
    public static String SERVERURL = "http://api.1cricket.in/";
    public static String BASEURL = SERVERURL+"myrest/user/";

    public static String Authentication = "";

    public static String IMAGEBASEURL = "http://1cricket-7dbd.kxcdn.com/uploads/";

    public static String ProfileIMAGEBASEURL = SERVERURL+"uploads/user/";

    //APK URL
    public static String APKURL = "https://play.google.com/store/apps/details?id=com.game.onecricket";
    //APK NAME
    public static String APKNAME = "1Cricket.apk";


    public static String TEAMFLAGIMAGE = "";
    //public static String PLAYERIMAGE = IMAGEBASEURL+"player/";
    public static String PLAYERIMAGE = "";

    public static String BANNERIMAGE = IMAGEBASEURL+"offers/";

    public static String LEADERBOARDPLAYERIMAGE = IMAGEBASEURL+"leaderboard/";

    public static String TERMSANDCONDITIONSURL = "http://1cricket.in/tandcmobile.html";
    public static String legality = "http://1cricket.in/legalitymobile.html";
    public static String Privacypolicy = "http://1cricket.in/privacymobile.html";
    public static String ABOUTUSURL = "http://1cricket.in/aboutmobile.html";
    public static String HELPDESKURL = "http://1cricket.in/tandcmobile.html";
    public static String PRICING = SERVERURL;

    public static String REFUNDPOLICY = "http://api.1cricket.in/docs/Privacy_Policy.pdf";
    public static String LEGALITY = "http://1cricket.in/legalitymobile.html";
    public static String WITHDRAWPOLICY = SERVERURL+"withdraw-cash";


    public static String SIGNUP = BASEURL + "user_registration_with_otp";
    public static String LOGIN = BASEURL + "login_with_otp ";
    public static String VERIFYOTP = BASEURL + "verify_login_otp";
    public static String RESENDOTP = BASEURL + "resend_otp";

    public static String FORGOTPASSWORD = BASEURL + "forget_password";
    public static String VERIFYFORGOTPASSWORD = BASEURL + "varify_forgot_password";

    public static String UPDATENEWPASSWORD = BASEURL + "update_password";
    public static String CHANGEPASSWORD = BASEURL + "change_password";

    public static String HOMEFIXTURES = BASEURL + "match_record";
    public static String MYFIXTURES = BASEURL + "mymatch_record";
    public static String CONTESTLIST = BASEURL + "contest_list";
    public static String WINNINGINFOLIST = BASEURL + "winning_info";
    public static String PLAYERLIST = BASEURL + "team_list";
    public static String VIEWPROFILE = BASEURL + "view_profile";
    public static String EDITPROFILE = BASEURL + "edit_profile";

    public static String PLAYERINFO = BASEURL + "Player_information";

    public static String SAVETEAM = BASEURL + "save_team";
    public static String MYTEAMLIST = BASEURL + "my_team_list";

    public static String JOINCONTEST = BASEURL + "join_contest";

    public static String MYJOINCONTESTLIST = BASEURL + "my_join_contest_list";

    public static String MYFIXTURELEADERBOARD = BASEURL + "joined_contest";
    public static String MYFIXTURELEADERBOARDTEAM = BASEURL + "my_joined_team_list";

    /// MyLive Contest List
    public static String MYJOINLIVECONTESTLIST = BASEURL + "my_join_contest_list_live";

    //Live Leaderboard
    public static String MYLIVELEADERBOARD = BASEURL + "joined_contest";

    //Live Leaderboard Team Preview
    public static String MYLIVELEADERBOARDTEAM = BASEURL + "my_joined_team_list";

    /// MyResult Contest List
    public static String MYJOINRESULTCONTESTLIST = BASEURL + "my_join_contest_list_live";

    //MyAccount
    public static String MYACCOUNT = BASEURL + "my_account";

    //MyPlaying History
    public static String MYPLAYINGHISTORY = BASEURL + "playing_history";

    //AddMoney
    public static String ADDAMOUNT = BASEURL + "add_amount";

    public static String ADDAMOUNTOFFER = BASEURL + "bonus_offer";

    //MyTransactionList
    public static String MYTRANSACTIONLIST = BASEURL + "my_account_transaction";

    public static String TRANSACTION_HISTORY = BASEURL + "coin_transaction_list";
    //Notification List
    public static String NOTIFICATIONLIST = BASEURL + "notification";

    //Invited Friends List
    public static String INVITEDFRIENDSLIST = BASEURL + "refer_friend_list";

    //Withdraw Amount User Data If Saved
    public static String WITHDRAWAMOUNTUSERDATA = BASEURL + "user_withdrow_information";

    //Submit Withdrawl Request
    public static String WITHDRAWLREQUEST = BASEURL + "withdrow_amount";

    //Global Ranking Request
    public static String GLOBALRANKINGLIST = BASEURL + "global_rank";

    //HashKeyRequest
    public static String HASHKEYREQUEST = BASEURL + "hashkey";

    //HomeBannerRequest
    public static String HOMEBANNER = BASEURL + "get_offers";

    //UploadDocument
    public static String UPLOADDOUCMENT = BASEURL + "update_documents";
    //UploadImage
    public static String UpdateUserProfileImage = BASEURL + "update_user_profile_image";
    //UpdateApp
    public static String UPDATEAPP = BASEURL + "update_app";

    //RankCreateContest
    public static String CREATECONTESTRANK = BASEURL + "user_contest";

    //CreateOwnContest
    public static String CREATEOWNCONTEST = BASEURL + "user_contestCreate";

    //CreateOwnContestList
    public static String CREATEOWNCONTESTLIST = BASEURL + "user_contestList";

    //TrakNPayPaymentGateway Verify Hash Link
    public static String VERIFYHASH = BASEURL + "verifyHash";

    //Cashfree Return Token Url
    public static String RETURNTOKEN = BASEURL + "CashFree_token";

    //Home Match Api
    public static String MYMATCHRECORD = BASEURL + "my_match_record";

    public static String STATE_STATUS = BASEURL + "state_status";



}