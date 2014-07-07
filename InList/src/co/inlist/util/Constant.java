package co.inlist.util;

public class Constant {

	public static final String BLANK = "";

	public static final String API = "http://www.inlistdev.com/";
	public static final String API_LIVE = "http://www.inlist.com/";
	public static final String STATUS = "status";
	public static final String SUCCESS = "1";
	public static final String FAIL = "0";
	public static final String LOADING = "Loading...";

	public static String network_error = "please check your network connectivity.";
	public static String AppName = "InList";

	// Facebook
	public static final String FB_API_KEY = "1510315072530999";
	// public static final String FB_API_KEY = "561300287292507";

	// Twitter
	public static final String CONSUMER_KEY = "6JefdM2o1mXhyVfQ6VT7Pg3qa";
	public static final String CONSUMER_SECRET = "zsBZKsYyR6UR7r7AJ429oeEton7kNDy4RWguaQluNImNCoKXsl";

	public static class ACTIONS {
		public static final String ADD_DEVICE = "common/add_device/";
		public static final String EVENTS = "events/";
		public static final String PARTY_AREA = "party_area/";
		public static final String PARTY_AREA_SET = "party_area/set/";
		public static final String REGISTRATION = "/user/register/?apiMode=%s&json=%s&device_id=%s&email=%s&password=%s&first_name=%s&last_name=%s&phone=%s&membership_question_id=%s&membership_question_answer=%s&request_vip=%s";
		public static final String USER_LIST = "user_list&user_id=%s";
		public static final String LOGIN = "user/login/?apiMode=%s&json=%s&device_id=%s&login=%s&password=%s";
		public static final String LOGIN_FB = "/facebook/login/?apiMode=%s&%s=true&device_id=%s&access_token=%s";
		public static final String FORGOT_PASSWORD = "request_password_reset/?apiMode=%s&json=%s&email=%S";
		public static final String REGISTER_FB = "/facebook/register/?apiMode=%s&json=%s&device_id=%s&email=%s&password=%s&first_name=%s&last_name=%s&phone=%s&membership_question_id=%s&membership_question_answer=%s&access_token=%s&request_vip=%s";
		public static final String PREPARE_REGISTER = "common/prepare_registration/?json=true";
		public static final String CHANGE_PASSWORD = "user/login/save/";
		public static final String PUSHNOTIFICATIONS = "push_notifications/";
		public static final String USER_INVITE = "user/invite/";
		public static final String LOGOUT = "user/logout/";
		public static final String PROFILE = "user/small_details/";
		public static final String ADD_CARD = "credit_card/save/?apiMode=VIP&json=true"; // &user_card_id=%s&card_type=%s&card_number=%s&card_name=%s&card_exp_year=%s&card_exp_month=%s&set_default=1";

	}

	public static class TAGS {
		public static final String FIRST_NAME = "first_name";
		public static final String LAST_NAME = "last_name";
		public static final String EMAIL = "email";
		public static final String GENDER = "gender";
		public static final String ADDRESS = "address";
		public static final String COUNTRY = "country";
		public static final String CITY = "city";
		public static final String DEVICE_TYPE = "device_type";
		public static final String STATE = "state";
		public static final String ZIP = "zip";
		public static final String COMPANY = "company";
		public static final String WEBSITE = "website";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String GPS_ADDRESS = "gps_address";
		public static final String DATE = "date";
		public static final String USER_AVATAR = "user_avatar";
		public static final String USER_ID = "user_id";

		// Notification Tag
		public static final String MESSAGE = "message";
		public static final String ID = "id";
		public static final String USER_PHOTO = "user_photo";
		public static final String USER_LIST = "user_list";
		public static final String ACCEPT = "accept";
		public static final String BLOCK = "block";
		public static final String REJECT = "reject";
		public static final String PASSWORD = "password";

		// Api Mode
		public static final String VIP = "VIP";
		public static final String NONVIP = "NON-VIP";

		// REQUEST_TYPE
		public static final String REQUEST_TYPE_JSON = "json";
		public static final String REQUEST_TYPE_XML = "xml";
	}

	public static class ERRORS {
		public static final String PLZ_FIRST_NAME = "Please enter Firstname";
		public static final String PLZ_LAST_NAME = "Please enter Lastname";
		public static final String PLZ_EMAIL = "Please enter email";
		public static final String PLZ_VALID_EMAIL = "Please enter valid EMAIL address";
		public static final String PLZ_CONTACT_NO = "Please enter contact no";
		public static final String PLZ_PASSWORD = "Please enter password";
		public static final String PLZ_REPASSWORD = "Please enter RePasword";
		public static final String NOT_MATCHED = "Password didn't match";
		public static final String RECORDS_NOT_FOUNT = "No records found";
		public static final String NO_PENDING_REQUEST = "You have no pending request";
		public static final String PLZ_ANS = "Please enter answer";
		public static final String SOMETHING_GOES_WRONG = "Something goes wrong, please try again";

		public static final String NO_INTERNET_CONNECTION = "We were unable to establish a connection to the internet. Check your signal, WiFi settings or restart the app or phone.";
		public static final String PLZ_CARD_NUMBER = "Please enter card number";
		public static final String PLZ_CARD_NAME = "Please enter card holder's name";
		public static final String PLZ_CARD_MONTH = "Please select month";
		public static final String PLZ_CARD_YEAR = "Please select year";

	}

	public static class SHRED_PR {
		public static final String SHARE_PREF = "inlist_preferences";
		public static final String KEY_USERID = "user_id";
		public static final String KEY_MEASUREMENT = "measurement";
		public static final String KEY_FIRSTNAME = "firstname";
		public static final String KEY_LASTNAME = "lastname";
		public static final String KEY_PHONE = "phone";
		public static final String KEY_EMAIL = "email";
		public static final String KEY_LOGIN_STATUS = "login_status";
		public static final String KEY_SESSIONID = "sessionId";
		public static final String KEY_CURRENT_PASSWORD = "current_password";

	}

	public static class PREF_VAL {
		public static final String KM = "km";
		public static final String MILES = "miles";
		public static final String PROFILE_PIC_NAME = "bl_profile_pic";
		public static final String DIR_NAME = "/buddylocation";
		public static final String OFFLINE_FILE = "offline.txt";
		public static final String OFFLINE_FILE_PRE_REGISTER = "prepare_register.txt";
	}

	public static class DIALOG {
		public static final String TITLE = "title";
		public static final String MSG = "msg";
		public static final String POSITIVE = "positive_button";
		public static final String NEGATIVE = "negative_button";
		public static final int NEG_FEEDBACK = 0;
		public static final int POS_FEEDBACK = 1;
	}

	public static class INTENT_K_V {
		public static final int ABOUT_APP = 111;
		public static final int HELP = 222;
		public static final int PRI_POLICY = 333;
		public static final int TERM_USE = 444;
		public static final int FAQ = 555;
		public static final int ABOUT_US = 666;
		public static final int SUPPORT = 777;

		public static final String GENWEB_KEY = "gen_web_key";
	}

}
