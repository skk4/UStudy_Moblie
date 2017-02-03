package org.yoya.com.yoyaorg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import org.yoya.com.yoyaorg.R;


/**
 * Created by Administrator on 2016/3/29.
 */
public class UpdateTipsDialog {

    public static Dialog showDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.custom_dialog);
        dialog.setContentView(R.layout.dialog_update_tips);
        dialog.setCanceledOnTouchOutside(false);
        dialog.findViewById(R.id.tv_dialog_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        return dialog;
    }
}
