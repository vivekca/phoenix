package com.homelane.phoenixapp.main.project.designers;

import com.hl.hlcorelib.mvp.presenters.HLCoreFragment;
import com.hl.hlcorelib.orm.HLObject;
import com.homelane.phoenixapp.PhoenixConstants;
import com.homelane.phoenixapp.main.MainPresenter;

import java.util.ArrayList;

/**
 * Created by hl0395 on 21/12/15.
 */
public class DesignerPresenter extends HLCoreFragment<DesignerView> {


    @Override
    protected void onBindView() {
        super.onBindView();
        ((MainPresenter)getActivity()).setTabVisible(false);

        ArrayList<HLObject> customerList = new ArrayList<HLObject>();

        HLObject customer = new HLObject(PhoenixConstants.Designer.NAME);
        customer.put(PhoenixConstants.Designer.DESIGNER_NAME,"Vivek CA");
        customer.put(PhoenixConstants.Designer.DESIGNER_EMAIL, "vivek.c@homelane.com");
        customer.put(PhoenixConstants.Designer.DESIGNER_MOBILE, "9739344416");
        customer.put(PhoenixConstants.Designer.DESIGNER_STATUS, "Active");
        customer.put(PhoenixConstants.Designer.DESIGNER_ROLE, "Admin");
        customer.put(PhoenixConstants.Designer.DESIGNER_LAST_ACCESS, "09/12/2015 02:31 PM");

        customerList.add(customer);
        customer = new HLObject(PhoenixConstants.Designer.NAME);
        customer.put(PhoenixConstants.Designer.DESIGNER_NAME, "BhanuPrasad");
        customer.put(PhoenixConstants.Designer.DESIGNER_EMAIL, "bhanuprasad.m@homelane.com");
        customer.put(PhoenixConstants.Designer.DESIGNER_MOBILE, "9739344416");
        customer.put(PhoenixConstants.Designer.DESIGNER_STATUS, "InActive");
        customer.put(PhoenixConstants.Designer.DESIGNER_ROLE, "Designer");
        customer.put(PhoenixConstants.Designer.DESIGNER_LAST_ACCESS, "16/10/2015 06:14 PM");

        customerList.add(customer);
        customer = new HLObject(PhoenixConstants.Designer.NAME);
        customer.put(PhoenixConstants.Designer.DESIGNER_NAME, "Vinith");
        customer.put(PhoenixConstants.Designer.DESIGNER_EMAIL, "vinith.k@homelane.com");
        customer.put(PhoenixConstants.Designer.DESIGNER_MOBILE, "9739344416");
        customer.put(PhoenixConstants.Designer.DESIGNER_STATUS, "Active");
        customer.put(PhoenixConstants.Designer.DESIGNER_ROLE, "City Head");
        customer.put(PhoenixConstants.Designer.DESIGNER_LAST_ACCESS, "30/12/2015 03:41 PM");

        customerList.add(customer);

        DesignerListAdapter customerDashboardListAdapter = new DesignerListAdapter();
        customerDashboardListAdapter.setmDataSet(customerList);
        mView.mCustomerList.setAdapter(customerDashboardListAdapter);
    }

    @Override
    protected Class<DesignerView> getVuClass() {
        return DesignerView.class;
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
