package com.homelane.phoenixapp.main.project.dashboard;

import com.hl.hlcorelib.mvp.presenters.HLCoreFragment;
import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.main.MainPresenter;

import java.util.ArrayList;

/**
 * Created by hl0395 on 21/12/15.
 */
public class CustomerDashboardPresenter extends HLCoreFragment<CustomerDashboardView> {


    @Override
    protected void onBindView() {
        super.onBindView();
        ((MainPresenter)getActivity()).setTabVisible(false);

        ArrayList<HLObject> customerList = new ArrayList<HLObject>();

        HLObject customer = new HLObject(PhoenixConstants.Customer.NAME);
        customer.put(PhoenixConstants.Customer.CUSTOMER_NAME,"Vivek CA");
        customer.put(PhoenixConstants.Customer.CUSTOMER_EMAIL, "vivek.c@homelane.com");
        customer.put(PhoenixConstants.Customer.CUSTOMER_QUOTE_AMT, "100000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_INITIAL_QUOTE_AMT, "150000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_FINAL_QUOTE_AMT, "175000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_COLLECTED_AMT, "15000");
        customer.put(PhoenixConstants.Customer.CUSTOMER_FOLLOW_UPDATE, "1/1/2016");

        customerList.add(customer);
        customer = new HLObject(PhoenixConstants.Customer.NAME);
        customer.put(PhoenixConstants.Customer.CUSTOMER_NAME, "Vivek CA");
        customer.put(PhoenixConstants.Customer.CUSTOMER_EMAIL, "vivek.c@homelane.com");

        customerList.add(customer);
        customer = new HLObject(PhoenixConstants.Customer.NAME);
        customer.put(PhoenixConstants.Customer.CUSTOMER_NAME,"Vivek CA");
        customer.put(PhoenixConstants.Customer.CUSTOMER_EMAIL, "vivek.c@homelane.com");

        customerList.add(customer);

        CustomerDashboardListAdapter customerDashboardListAdapter = new CustomerDashboardListAdapter();
        customerDashboardListAdapter.setmDataSet(customerList);
        mView.mCustomerList.setAdapter(customerDashboardListAdapter);
    }

    @Override
    protected Class<CustomerDashboardView> getVuClass() {
        return CustomerDashboardView.class;
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
