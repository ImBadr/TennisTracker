package com.example.tennistracker.classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.tennistracker.MatchDetailsActivity;
import com.example.tennistracker.R;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    List<Match> modelList;
    Context context;

    public ListAdapter(List<Match> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Match match = modelList.get(position);
        holder.bindData(match);

        holder.relativeLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, MatchDetailsActivity.class);

            intent.putExtra("id", match.getId());
            intent.putExtra("player1_name", match.getPlayer1());
            intent.putExtra("player2_name", match.getPlayer2());
            intent.putExtra("player1_match_score", match.getPlayer1_score());
            intent.putExtra("player2_match_score", match.getPlayer2_score());
            intent.putExtra("location", match.getLocations());

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView player1_name, player2_name, player1_match_score, player2_match_score, location;

        RelativeLayout relativeLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            player1_name = itemView.findViewById(R.id.player1_name);
            player2_name = itemView.findViewById(R.id.player2_name);
            player1_match_score = itemView.findViewById(R.id.player1_match_score);
            player2_match_score = itemView.findViewById(R.id.player2_match_score);
            location = itemView.findViewById(R.id.location);

            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }

        void bindData(final Match item) {
            player1_name.setText(item.getPlayer1());
            player2_name.setText(item.getPlayer2());
            player1_match_score.setText(item.getPlayer1_score());
            player2_match_score.setText(item.getPlayer2_score());
        }
    }
}