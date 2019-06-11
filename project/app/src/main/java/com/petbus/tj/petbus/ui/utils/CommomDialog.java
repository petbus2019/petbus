package com.petbus.tj.petbus.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

//https://blog.csdn.net/gongyong666/article/details/80766211

public class CommomDialog extends Dialog {

    public CommomDialog(Context context) {
        super(context);
    }

    public CommomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String cameraButtonText;
        private String photoButtonText;
        private View contentView;
        private DialogInterface.OnClickListener cameraButtonClickListener;
        private DialogInterface.OnClickListener photoButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /** 
         * Set the Dialog message from resource 
         *
         * @param title 
         * @return 
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /** 
         * Set the Dialog title from resource 
         *
         * @param title 
         * @return 
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /** 
         * Set the Dialog title from String 
         *
         * @param title 
         * @return 
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /** 
         * Set the positive button resource and it's listener 
         *
         * @param cameraButtonText 
         * @return 
         */
        public Builder setCameraButton(int cameraButtonText,
                DialogInterface.OnClickListener listener) {
            this.cameraButtonText = (String) context
                    .getText(cameraButtonText);
            this.cameraButtonClickListener = listener;
            return this;
        }

        public Builder setCameraButton(String cameraButtonText,
                DialogInterface.OnClickListener listener) {
            this.cameraButtonText = cameraButtonText;
            this.cameraButtonClickListener = listener;
            return this;
        }

        public Builder setPhotoButton(int photoButtonText,
                DialogInterface.OnClickListener listener) {
            this.photoButtonText = (String) context
                    .getText(photoButtonText);
            this.photoButtonClickListener = listener;
            return this;
        }

        public Builder setPhotoButton(String photoButtonText,
                DialogInterface.OnClickListener listener) {
            this.photoButtonText = photoButtonText;
            this.photoButtonClickListener = listener;
            return this;
        }

        public CommomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CommomDialog dialog = new CommomDialog( context,R.style.dialog );
            View layout = inflater.inflate(R.layout.dialog_photo_select, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

             if (cameraButtonText != null) {
                ((Button) layout.findViewById(R.id.button_camera))
                        .setText(cameraButtonText);
                if (cameraButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.button_camera))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    cameraButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.button_camera).setVisibility(
                        View.GONE);
            }

             if (photoButtonText != null) {
                ((Button) layout.findViewById(R.id.button_photo))
                        .setText(photoButtonText);
                if (photoButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.button_photo))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    photoButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.button_photo).setVisibility(
                        View.GONE);
            }

            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}  