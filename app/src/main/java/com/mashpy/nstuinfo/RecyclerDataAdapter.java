package com.mashpy.nstuinfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.Arrays;
import java.util.List;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.MyViewHolder> {

    ColorGenerator generator = ColorGenerator.MATERIAL;
    ColorGenerator list_Color = ColorGenerator.create(Arrays.asList(
            0xffffffff,
            0xff8ccbf1
    ));
    private List<RecyclerData> moviesList;

    public RecyclerDataAdapter(List<RecyclerData> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecyclerData recyclerData = moviesList.get(position);
        holder.title.setText(recyclerData.getTitle());
        holder.genre.setText(recyclerData.getGenre());
        holder.year.setText(recyclerData.getYear());
        String letter = String.valueOf(recyclerData.getTitle().charAt(0));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor());
        holder.letter.setImageDrawable(drawable);
        if (recyclerData.getType().equals("ad")) {
            //Red Color For ad ListItem
            holder.row_layout.setBackgroundColor(list_Color.getColor(1));
        } else {
            //White Color For non_ad ListItem
            holder.row_layout.setBackgroundColor(0xffffffff);
        }


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        ImageView letter;
        RelativeLayout row_layout;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            letter = (ImageView) view.findViewById(R.id.gmailitem_letter);
            row_layout = (RelativeLayout) view.findViewById(R.id.list_row);
        }
    }
}
