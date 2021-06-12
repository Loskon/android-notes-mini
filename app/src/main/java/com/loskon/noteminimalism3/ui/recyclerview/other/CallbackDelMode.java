package com.loskon.noteminimalism3.ui.recyclerview.other;

/**
 * Callbacks for recyclerViewAdapter
 */

public interface CallbackDelMode {
    void onCallBackSelMode(boolean isSelMode);
    void onCallBackNotAllSelected(boolean isNotAllSelected);
    void onCallBackNumSel(int numSelItem);
    void onCallBackUni();
}
