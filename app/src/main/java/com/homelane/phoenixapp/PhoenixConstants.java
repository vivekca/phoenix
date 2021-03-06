package com.homelane.phoenixapp;

/**
 * Created by hl0395 on 21/12/15.
 */
public class PhoenixConstants {
    /**
     * Class holds all the appropriate keys for the
     * Customer
     */
    public static final class Customer {
        public static final String NAME = "Customer";
        public static final String CUSTOMER_NAME = "CustomerName";
        public static final String CUSTOMER_EMAIL = "CustomerEmail";
        public static final String CUSTOMER_MOBILE = "CustomerMobile";
        public static final String CUSTOMER_STATUS = "CustomerStatus";
        public static final String CUSTOMER_QUOTE_AMT = "CustomerQuoteAmount";
        public static final String CUSTOMER_INITIAL_QUOTE_AMT = "CustomerInitialQuoteAmount";
        public static final String CUSTOMER_FINAL_QUOTE_AMT = "CustomerFinalQuoteAmount";
        public static final String CUSTOMER_COLLECTED_AMT = "CustomerCollectedAmount";
        public static final String CUSTOMER_FOLLOW_UPDATE = "CustomerFollowUpdate";
    }

    public static final class Designer {
        public static final String NAME = "Designer";
        public static final String DESIGNER_NAME = "DesignerName";
        public static final String DESIGNER_EMAIL = "DesignerEmail";
        public static final String DESIGNER_MOBILE = "DesignerMobile";
        public static final String DESIGNER_ROLE = "DesignerRole";
        public static final String DESIGNER_STATUS = "DesignerStatus";
        public static final String DESIGNER_LAST_ACCESS = "DesignerLastAccess";
    }

    public static final class Project {
        public static final String NAME = "Project";
        public static final String PROJECT_NAME = "ProjectName";
        public static final String PROJECT_ID = "ProjectID";
        public static final String PROJECT_LOCATION = "ProjectLocation";
        public static final String PROJECT_STATUS = "ProjectStatus";
        public static final String PROJECT_STATE = "ProjectState";
    }

    public static final class DatePicker {
        public static final String SELECTED_DAY = "SelectedDay";
        public static final String SELECTED_MONTH = "SelectedMonth";
        public static final String SELECTED_YEAR = "SelectedYear";
    }

    public static final class Task {
        public static final String NAME = "Task";
        public static final String TASK_ID = "TaskID";
        public static final String TASK_NAME="TaskName";
        public static final String START_DATE  = "StartDate";
        public static final String COMPLETED_DATE = "ToDate";
        public static final String TASK_STATUS = "TaskStatus";
        public static final String TASK_FLAG = "TaskFlag";
        public static final String FILTER = "Filter";
    }
    public static final class State {
        public static final String NAME = "State";
        public static final String STATE_NAME="StateName";
        public static final String STATE_START_DATE  = "StateStartDate";
        public static final String STATE_COMPLETED_DATE = "StateCompletedDate";
        public static final String STATE_STATUS = "StateStatus";
    }
    /**
     * Class holds all keys against respective values in
     * configuration file.Make sure the value used against constants
     * will be matching the keys in the property file
     */
    public static final class AppConfig{
        public static final String HL_PHOENIX_BASE_URL  = "hl_phoenix_base_url";
        public static final String HL_PROJECT_DETAILS_URL  = "hl_project_details_url";
        public static final String HL_AGGREGATE_TASK_URL  = "hl_aggregate_task_url";
        public static final String HL_HISTORY_TASK_URL  = "hl_history_task_url";
        public static final int HL_ENVIRONMENT_DEV         = 0;
        public static final int HL_ENVIRONMENT_STAGE       = 1;
        public static final int HL_ENVIRONMENT_PROD        = 2;
    }


    public static final String SEARCH_EVENT = "SEARCH_EVENT";
    public static final String DISABLE_SEARCH_EVENT = "DISABLE_SEARCH_EVENT";
    public static final String DISABLE_FILTER_EVENT = "DISABLE_FILTER_EVENT";
    public static final String UPDATE_STATUS_EVENT = "UPDATE_STATUS_EVENT";
    public static final String STATUS_LIST = "STATUS_LIST";
    public static final String FILTER_STATUS = "FILTER_STATUS";
    public static final String FILTER_EVENT = "FILTER_EVENT";


    public static final String NAVIGATE_TO_PROJECT_DETAILS_EVENT ="NavigateToProjectDetails";
    public static final String SNACKBAR_DISPLAY_EVENT ="SnackbarDisplay";
    public static final String SNACKBAR_DISPLAY_MESSAGE ="SnackbarDisplayMessage";
    public static final String SELECTED_DATE_EVENT ="SelectedDate";
}
