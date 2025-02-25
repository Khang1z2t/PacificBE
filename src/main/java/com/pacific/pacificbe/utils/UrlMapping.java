package com.pacific.pacificbe.utils;

public class UrlMapping {
    public static final String API = "/api";
    public static final String AUTH = API + "/auth";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
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

}
