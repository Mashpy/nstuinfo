package com.nstuinfo.mRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nstuinfo.mOtherUtils.AnimationUtils;
import com.nstuinfo.mOtherUtils.Preferences;
import com.nstuinfo.mViews.FontAppearance;
import com.nstuinfo.R;
import com.nstuinfo.DetailsActivity;
import com.nstuinfo.mJsonUtils.ExtractDataJson;
import com.nstuinfo.mJsonUtils.ReadWriteJson;

import java.util.Arrays;
import java.util.List;

/**
 * Created by whoami on 10/16/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ColorGenerator generator = ColorGenerator.MATERIAL;
    ColorGenerator list_Color = ColorGenerator.create(Arrays.asList(
            0xffffffff,
            0xff8ccbf1
    ));

    private List<String> itemsList = null;
    private Context context;
    private String tag = null;
    private String title = null;
    private int previousPosition = -1;

    public MyAdapter(Context context, List<String> itemsList, String tag) {
        this.context = context;
        this.itemsList = itemsList;
        this.tag = tag;
    }

    public MyAdapter(Context context, List<String> itemsList, String title, String tag) {
        this.context = context;
        this.itemsList = itemsList;
        this.title = title;
        this.tag = tag;
    }

    public MyAdapter(Context context, List<String> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;

        if (tag != null) {
            if (tag.equalsIgnoreCase("second")) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_model_second, parent, false);
            } else if (tag.equalsIgnoreCase("content")) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_view, parent, false);
            } else {
                if (Preferences.isGridView(context)) {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_grid_model_home, parent, false);
                } else {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_list_model_home, parent, false);
                }
            }
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_list_model_home, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (tag.equalsIgnoreCase("content")) {

            String text = itemsList.get(position);

            if (Preferences.isDarkTheme(context)) {
                holder.contentCard.setCardBackgroundColor(context.getResources().getColor(R.color.dark_color_secondary));
                holder.contentTv.setTextColor(Color.WHITE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.contentTv.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.contentTv.setText(Html.fromHtml(text));
            }

            FontAppearance.setSecondaryTextSize(context, holder.contentTv);

            Linkify.addLinks(holder.contentTv, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
            holder.contentTv.setLinksClickable(true);

            holder.contentCallImg.setVisibility(View.GONE);
            holder.contentMailImg.setVisibility(View.GONE);

            if ( text.contains("Phone") || text.contains("Telephone") || text.contains("Mobile") ||
                    text.contains("phone:") || text.contains("telephone:") || text.contains("mobile:") ) {

                holder.contentCallImg.setVisibility(View.VISIBLE);
            }

            if (text.contains("Email") || text.contains("E-mail") || text.contains("Mail") ||
                    text.contains("email:") || text.contains("e-mail:") || text.contains("mail:")) {

                holder.contentMailImg.setVisibility(View.VISIBLE);
            }


        } else {
            holder.titleTV.setText(itemsList.get(position));

            FontAppearance.setPrimaryTextSize(context, holder.titleTV);

            if (!tag.equalsIgnoreCase("second")) {

                // SET THEME AT HOME RECYCLER VIEW
                if (Preferences.isDarkTheme(context)) {
                    holder.modelMainRL.setBackgroundColor(context.getResources().getColor(R.color.dark_color_secondary));
                    holder.titleTV.setTextColor(Color.WHITE);
                } else {
                    holder.modelMainRL.setBackgroundColor(Color.WHITE);
                    holder.titleTV.setTextColor(Color.BLACK);
                }

                // G-MAIL STYLE IMAGE BACKGROUND
                String letter = String.valueOf(itemsList.get(position).charAt(0));
                TextDrawable drawable = TextDrawable.builder().buildRound(letter, generator.getRandomColor());
                holder.imageView.setImageDrawable(drawable);

            } else {

                // SET THEME AT DETAILS RECYCLER VIEW
                if (Preferences.isDarkTheme(context)) {
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.dark_color_secondary));
                } else {
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.list_item_color));
                }

            }
        }

        if (position > previousPosition) { // scrolling down
            AnimationUtils.animateRecyclerView(holder.itemView, true);
        } else { // scrolling up
            AnimationUtils.animateRecyclerView(holder.itemView, false);
        }
        previousPosition = position;
        AnimationUtils.setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        TextView titleTV;
        ImageView imageView;
        RelativeLayout modelMainRL;

        // Content views
        TextView contentTv;
        CardView contentCard;
        ImageView contentCallImg;
        ImageView contentMailImg;

        ViewHolder(View itemView) {
            super(itemView);
            modelMainRL = itemView.findViewById(R.id.recyclerModelMainRL);
            imageView = itemView.findViewById(R.id.recyclerImageView);
            titleTV = itemView.findViewById(R.id.recyclerTextViewTitle);
            cardView = itemView.findViewById(R.id.list_model_second_cardView);

            // Content views
            contentTv = itemView.findViewById(R.id.contentTV);
            contentCard = itemView.findViewById(R.id.content_item_view_card);
            contentCallImg = itemView.findViewById(R.id.phoneIMGV);
            contentMailImg = itemView.findViewById(R.id.mailIMGV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (!tag.equalsIgnoreCase("content")) {
                if (tag.equalsIgnoreCase("second")) {
                    if (Preferences.isDarkTheme(context)) {
                        cardView.setCardBackgroundColor(Color.BLACK);
                    } else {
                        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.list_item_selection_color));
                    }
                    detailsPopUpWindow(titleTV.getText().toString().trim());
                } else {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("TITLE", titleTV.getText().toString().trim());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private void detailsPopUpWindow(String tvTitle) {

        final PopupWindow mPopUpWindow;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.details_popup_window,null);
        }

        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;

        mPopUpWindow = new PopupWindow(layout, width, height, true);

        RelativeLayout backDimRL = null;
        RelativeLayout mainRL = null;
        LinearLayout linearLayout = null;
        TextView textView = null;
        RelativeLayout rlPopup = null;

        if (layout != null) {
            backDimRL = layout.findViewById(R.id.dimRL);
            mainRL = layout.findViewById(R.id.main_popup);
            linearLayout = layout.findViewById(R.id.mainLL);
            textView = layout.findViewById(R.id.popUpTitleTV);
            rlPopup = layout.findViewById(R.id.popUpThemeRL);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( (int) (width*.95), (int) (height*.88) );

        assert mainRL != null;
        mainRL.setLayoutParams(params);

        textView.setText(tvTitle);

        FontAppearance.setPrimaryTextSize(context, textView);

        if (Preferences.isDarkTheme(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                rlPopup.setBackground(context.getResources().getDrawable(R.drawable.popup_dark_shape));
                textView.setBackground(context.getResources().getDrawable(R.drawable.popup_dark_title_shape));
            } else {
                rlPopup.setBackgroundColor(context.getResources().getColor(R.color.dark_color_primary));
                textView.setBackgroundColor(Color.BLACK);
            }
            backDimRL.setBackgroundColor(context.getResources().getColor(R.color.dim_white));
        }

        assert backDimRL != null;
        backDimRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopUpWindow.dismiss();
            }
        });

        assert linearLayout != null;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing to do
            }
        });


        ExtractDataJson extractDataJson = new ExtractDataJson(context, ReadWriteJson.readDataFile(context), linearLayout);
        extractDataJson.getPopUpView(title, tvTitle);


        //Set up touch closing outside of pop-up
        mPopUpWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pop_up_bg));
        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopUpWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mPopUpWindow.setOutsideTouchable(true);

        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setContentView(layout);
        mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

}
