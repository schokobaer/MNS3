package at.ac.tuwien.mns.group3.mnsg3e3.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.mns.group3.mnsg3e3.R;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;
import org.w3c.dom.Text;

import java.util.List;

public class BaseAdapter extends RecyclerView.Adapter {

    List<Report> dataset;
    private int expanded_pos = -1;
    RecyclerView recyclerView;

    public BaseAdapter(List<Report> dataset) {
        this.dataset = dataset;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, viewGroup, false);
        BasicViewHolder holder = new BasicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        String date;
        Report report = dataset.get(i);

        date = String.format("%-20s: %s", "Timestamp", report.getDate());

        ((BasicViewHolder) viewHolder).date.setText(date);
        ((BasicViewHolder) viewHolder).report.setText("ID: " + report.getId());


        ((BasicViewHolder) viewHolder).card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Open fragment
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public static class BasicViewHolder extends RecyclerView.ViewHolder {

        View card;
        TextView report;
        TextView date;

        public BasicViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item);
            report = itemView.findViewById(R.id.id);
            date = itemView.findViewById(R.id.date);
        }
    }

}
