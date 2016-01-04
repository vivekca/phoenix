package com.homelane.phoenixapp.main.project.customer;

import com.hl.hlcorelib.mvp.presenters.HLCoreFragment;
import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.main.MainPresenter;

import java.util.ArrayList;

/**
 * Created by hl0395 on 21/12/15.
 */
public class CustomerPresenter extends HLCoreFragment<CustomerView> {


    @Override
    protected void onBindView() {
        super.onBindView();
        ((MainPresenter)getActivity()).setTabVisible(false);

        ArrayList<HLObject> customerList = new ArrayList<HLObject>();

        HLObject customer = new HLObject(PhoenixConstants.Customer.NAME);
        customer.put(PhoenixConstants.Customer.CUSTOMER_NAME,"Vivek CA");
        customer.put(PhoenixConstants.Customer.CUSTOMER_EMAIL, "vivek.c@homelane.com");
        customer.put(PhoenixConstants.Customer.CUSTOMER_MOBILE, "9739344416");
        customer.put(PhoenixConstants.Customer.CUSTOMER_STATUS, "To Send Initial Quote");
        customer.put(PhoenixConstants.Customer.CUSTOMER_QUOTE_AMT, "100000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_INITIAL_QUOTE_AMT, "150000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_FINAL_QUOTE_AMT, "175000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_COLLECTED_AMT, "15000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_FOLLOW_UPDATE, "1/1/2016");

        customerList.add(customer);
        customer = new HLObject(PhoenixConstants.Customer.NAME);
        customer.put(PhoenixConstants.Customer.CUSTOMER_NAME, "BhanuPrasad");
        customer.put(PhoenixConstants.Customer.CUSTOMER_EMAIL, "bhanuprasad.m@homelane.com");
        customer.put(PhoenixConstants.Customer.CUSTOMER_MOBILE, "9739344416");
        customer.put(PhoenixConstants.Customer.CUSTOMER_STATUS, "To Collect 10%");
        customer.put(PhoenixConstants.Customer.CUSTOMER_QUOTE_AMT, "150000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_INITIAL_QUOTE_AMT, "180000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_FINAL_QUOTE_AMT, "275000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_COLLECTED_AMT, "19000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_FOLLOW_UPDATE, "31/12/2015");

        customerList.add(customer);
        customer = new HLObject(PhoenixConstants.Customer.NAME);
        customer.put(PhoenixConstants.Customer.CUSTOMER_NAME, "Vinith");
        customer.put(PhoenixConstants.Customer.CUSTOMER_EMAIL, "vinith.k@homelane.com");
        customer.put(PhoenixConstants.Customer.CUSTOMER_MOBILE, "9739344416");
        customer.put(PhoenixConstants.Customer.CUSTOMER_STATUS, "To Collect 10%");
        customer.put(PhoenixConstants.Customer.CUSTOMER_QUOTE_AMT, "1000000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_INITIAL_QUOTE_AMT, "9500000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_FINAL_QUOTE_AMT, "7750000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_COLLECTED_AMT, "750000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_FOLLOW_UPDATE, "1/1/2016");

        customerList.add(customer);

        CustomerListAdapter customerDashboardListAdapter = new CustomerListAdapter();
        customerDashboardListAdapter.setmDataSet(customerList);
        mView.mCustomerList.setAdapter(customerDashboardListAdapter);
    }

    @Override
    protected Class<CustomerView> getVuClass() {
        return CustomerView.class;
    }

    @Override
    protected int getMenuLayout() {
        return 0;
    }

    @Override
    protected int[] getDisabledMenuItems() {
        return new int[0];
    }

}
