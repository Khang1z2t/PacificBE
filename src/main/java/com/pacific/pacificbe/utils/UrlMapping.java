package com.pacific.pacificbe.utils;

public class UrlMapping {

    public static final String API = "/api";
    public static final String LOGOUT = "/logout";
    public static final String AUTHENTICATE_TOKEN = "/authenticate-token";
    public static final String RESET_PASSWORD = "/reset-password";
    public static final String SEND_VERIFY_MAIL = "/send-verify-mail";
    public static final String VERIFY_EMAIL = "/verify-email";
    public static final String SEND_RESET_PASSWORD_MAIL = "/send-reset-password-mail";
    public static final String VERIFY_RESET_PASSWORD = "/verify-reset-password";

    public static final String COUNT_ALL_USERS = "/count/users";
    public static final String COUNT_ALL_TOURS = "/count/tours";
    public static final String COUNT_ALL_GUIDES = "/count/guides";
    public static final String COUNT_ALL_BOOKINGS = "/count/bookings";

    /*============================ Start auth API ===================================*/
    public static final String AUTH = API + "/auth";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String OAUTH2_GOOGLE = "/oauth2/google";
    public static final String OAUTH2_GOOGLE_CALLBACK = "/oauth2/google/callback";
    public static final String OAUTH2_FACEBOOK = "/oauth2/facebook";
    public static final String OAUTH2_FACEBOOK_CALLBACK = "/oauth2/facebook/callback";
    /*============================ End auth API ===================================*/



    /*============================ Start Tour API ===================================*/
    public static final String TOURS = API + "/tours";
    public static final String GET_TOUR_BY_ID = "/{id}";
    public static final String GET_TOUR_BY_NO = "/no/{tourNo}";
    public static final String GET_TOUR_BY_CATEGORY = "category/{category}";
    public static final String GET_TOUR_BY_RATING = "rating/{rating}";
    public static final String GET_TOUR_BY_DESTINATION = "destination/{destination}";
    public static final String GET_ALL_TOURS = "/all";
    public static final String ADD_TOUR = "/add";
    public static final String UPDATE_TOUR = "/update";
    public static final String DELETE_TOUR = "/delete/{id}";
    public static final String SEARCH_TOURS = "/search";

    /*============================ End Tour API ===================================*/

    /*============================ Start Tour Detail API ===================================*/
    public static final String TOUR_DETAILS = API + "/tour-details";
    public static final String GET_TOUR_DETAIL_BY_ID = "/{id}";
    public static final String GET_TOUR_DETAIL_BY_TOUR = "/tour/{tourId}";
    public static final String GET_TOUR_DETAIL_BY_CATEGORY = "/category/{category}";
    public static final String GET_ALL_TOUR_DETAILS = "/all";
    public static final String ADD_TOUR_DETAIL = "/add";
    public static final String UPDATE_TOUR_DETAIL = "/update";
    public static final String DELETE_TOUR_DETAIL = "/delete/{id}";
    /*============================ End Tour Detail API ===================================*/


    /*============================ Start Booking API ===================================*/
    public static final String BOOKINGS = API + "/bookings";
    public static final String GET_ALL_BOOKINGS = "/all";
    public static final String GET_BOOKING_BY_ID = "/book/{id}";
    public static final String GET_BOOKING_BY_NO = "/book/no/{bookingNo}";
    public static final String GET_BOOKING_BY_USER = "/book/user/{userId}";
    public static final String BOOK_TOUR =  "/book-tour";
    public static final String UPDATE_BOOKING = "/book/{id}/update";
    public static final String DELETE_BOOKING = "/book/{id}/delete";
    public static final String APPROVE_BOOKING = "/book/{id}/approve";
    public static final String CANCEL_BOOKING = "/book/{id}/cancel";
    public static final String DECLINE_BOOKINGS = "/book/{id}/decline";
    public static final String REVENUE_BOOKING = "/book/revenue";
    public static final String REVENUE_BOOKING_MONTH = "/book/revenue/month";
    public static final String REVENUE_BOOKING_YEAR = "/book/revenue/year";
    public static final String BOOK_AND_TOUR = "/book&tour/";
    public static final String CHECKOUT_BOOKING = "/checkout";
    public static final String CHECKOUT_RETURN = "/vnpay-payment-return";
    /*============================ End Booking API ===================================*/


    /*============================ Start User API ===================================*/
    public static final String USERS = API + "/users";
    public static final String GET_USER_BY_ID = "/{id}";
    public static final String GET_ALL_USERS = "/all";

    /*============================ End User API ===================================*/
    
	/* =========================== Start Report API ===================================== */
    public static final String EXPORT = API + "/export";
    public static final String EXPORT_PDF = "/PDF";
    /* =========================== End Report API ===================================== */


    /* =========================== Start Category API ===================================== */
    public static final String CATEGORY = API + "/categories";
    public static final String GET_ALL_CATEGORY = "/all";
    public static final String CREATE_CATEGORY = "/create";
    public static final String GET_CATEGORY_BY_ID = "/{id}";
    public static final String UPDATE_CATEGORY = "/update/{id}";
    public static final String DELETE_CATEGORY = "/delete/{id}";
    public static final String SEARCH_CATEGORY = "/search";
    /* =========================== End Category API ===================================== */

    /* =========================== Start Admin_review API ===================================== */
    public static final String ADMIN_REVIEW = API + "/admin/review";
    public static final String GET_ALL_ADMIN_REVIEW = "/getall";
    public static final String CREATE_ADMIN_REVIEW = "/create";
    public static final String GET_ADMIN_REVIEW_BY_ID = "/get/{id}";
    public static final String UPDATE_ADMIN_REVIEW = "/update/{id}";
    public static final String DELETE_ADMIN_REVIEW = "/delete/{id}";
    /* =========================== End Admin_review API ===================================== */

    /* =========================== Start Guide API ===================================== */
    public static final String GUIDE = API + "/guide";
    public static final String GET_ALL_GUIDE = "/all";
    public static final String CREATE_GUIDE = "/create";
    public static final String GET_GUIDE_BY_ID = "/{id}";
    public static final String UPDATE_GUIDE = "/update/{id}";
    public static final String DELETE_GUIDE = "/delete/{id}";
    /* =========================== End Guide API ===================================== */

    /* =========================== Start Admin User API ===================================== */
    public static final String ADD_USER = "/create";
    public static final String UPDATE_USER = "/update/{id}";
    public static final String UPDATE_STATUS_USER = "/updateStatus/{id}";
    /* =========================== End Admin User API ===================================== */

    /* =========================== Start Other URL ===================================== */
    public static final String FE_URL = "http://localhost:3000";
    public static final String GOOGLE_REDIRECT = FE_URL + "/google/redirect";
    /* =========================== End Other URL ===================================== */

}
