package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.exception.ServiceException;

import java.time.LocalDateTime;

public class ServletParameter {
  public static final String SLASH = "/";
  public static final String EMPTY_PATH = "/";
  public static final String NEW_PATH = "/new";
  public static final String DASHBOARD_PATH = "/dashboard";
  public static final String ADMIN_DASHBOARD_PATH = "/admin/dashboard";
  public static final String REDIRECT_LOGIN_PATH = "/login";
  public static final String REDIRECT_ACCESS_DENIED_PATH = "/access-denied.jsp";

  public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm";

  public static final String USERNAME_PARAMETER = "username";
  public static final String PASSWORD_PARAMETER = "password";
  public static final String EMAIL_PARAMETER = "email";
  public static final String ROLE_PARAMETER = "role";
  public static final String PET_TYPE_PARAMETER = "petType";
  public static final String SERVICES_PARAMETER = "services";
  public static final String PREFERRED_DATE_PARAMETER = "preferredDate";
  public static final String NOTES_PARAMETER = "notes";

  public static final String USER_ATTRIBUTE = "user";
  public static final String USER_ID_ATTRIBUTE = "userId";
  public static final String BOOKING_ID_ATTRIBUTE = "bookingId";
  public static final String USERNAME_ATTRIBUTE = "username";
  public static final String USER_ROLE_ATTRIBUTE = "userRole";
  public static final String ERROR_ATTRIBUTE = "error";
  public static final String BOOKING_ATTRIBUTE = "booking";
  public static final String PRESERVED_USERNAME_ATTRIBUTE = "preservedUsername";
  public static final String PRESERVED_EMAIL_ATTRIBUTE = "preservedEmail";
  public static final String ACTION_ATTRIBUTE = "action";
  public static final String ASSIGNED_DATA_ATTRIBUTE = "assignedDate";
  public static final String ACTION_CONFIRM_ATTRIBUTE = "confirm";
  public static final String ACTION_SCHEDULE_ATTRIBUTE = "schedule";
  public static final String ACTION_REJECT_ATTRIBUTE = "reject";
  public static final String ACTION_COMPLETE_ATTRIBUTE = "complete";
  public static final String ACTION_ASSIGN_DATE_TIME_ATTRIBUTE = "assign_data_time";

  public static final String LOGIN_JSP = "/pages/login.jsp";
  public static final String REGISTER_JSP = "/pages/register.jsp";
  public static final String SIGNUP_JSP = "/pages/signup.jsp";
  public static final String BOOKINGS_JSP = "/pages/booking.jsp";
  public static final String NEW_BOOKING_JSP = "/pages/new-booking.jsp";
  public static final String ADMIN_BOOKINGS_JSP = "/pages/admin-bookings.jsp";

  public static final String BOOKINGS_PATH_PREFIX = "/booking/";

  public static final String COMMA_SEPARATOR = ", ";

  public static final int SESSION_TIMEOUT_SECONDS = 30 * 60;
}
