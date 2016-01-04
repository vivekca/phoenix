package com.homelane.phoenixapp;

import android.os.Bundle;

import com.hl.hlcorelib.mvp.events.HLCoreEvent;

/**
 * Created by hl0268 on 29/12/15.
 */
public class SearchEvent extends HLCoreEvent {

    /**
     * holds teh category of events
     */
    public static final class Category{
        public static final int ENABLE = 0;
        public static final int DISABLE = 1;
        public static final int SEARCH = 2;
    }

    public int getmCategory() {
        return mCategory;
    }

    /**
     * The category of event
     */
    private int mCategory;

    /**
     * Constructs a new instance of {@code Object}.
     *
     * @param type   the type of th event
     * @param mExtra
     * @param mCategory the category of event possible
     *                  values @see{@link com.homelane.phoenixapp.events.SearchEvent.Category}
     */
    public SearchEvent(String type, Bundle mExtra, int mCategory) {
        super(type, mExtra);
        this.mCategory = mCategory;
    }

}
