package com.nielsen.simon.foodatcth.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nielsen.simon.foodatcth.R;

/**
 * Created by Simon on 2015-07-29.
 */
public class MenuDetailDialog extends DialogFragment {

    private Drawable image;
    private String title, description;

    public static MenuDetailDialog newInstance(Drawable image, String title, String description){
        MenuDetailDialog dialog = new MenuDetailDialog();
        dialog.image = image;
        dialog.title = title;
        dialog.description = description;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_menu_details, null);

        ((ImageView)dialogView.findViewById(R.id.dialogMenuImage)).setImageDrawable(image);
        ((TextView)dialogView.findViewById(R.id.dialogMenuTitle)).setText(title);
        ((TextView)dialogView.findViewById(R.id.dialogMenuDescription)).setText(description);

        builder.setView(dialogView)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
}
