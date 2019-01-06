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
        String gps_cdn;
        final String precision;
        String mls_param;
        String mls_result;
        String difference;

        Report report = dataset.get(i);

        date = String.format("%-20s: %s", "Timestamp", report.getDate());
        gps_cdn = String.format("%-20s: %s", "GPS Coordinates", report.getCdn());
        precision = String.format("%-20s: %f m", "Precision", report.getPrecision());
        mls_param = String.format("%-20s: %s", "MLS Parameter", report.getMLSParam());
        mls_result = String.format("%-20s: %s", "MLS Result", report.getMLSResult());
        difference = String.format("%-20s: %f m", "Difference", report.getDiff());

        ((BasicViewHolder) viewHolder).date.setText(date);
        ((BasicViewHolder) viewHolder).gps_cdn.setText(gps_cdn);
        ((BasicViewHolder) viewHolder).precision.setText(precision);
        ((BasicViewHolder) viewHolder).mls_param.setText(mls_param);
        ((BasicViewHolder) viewHolder).mls_result.setText(mls_result);
        ((BasicViewHolder) viewHolder).difference.setText(difference);
        ((BasicViewHolder) viewHolder).report.setText(i + 1);

        final boolean is_exp = i == expanded_pos;
        final int previous;

        ((BasicViewHolder) viewHolder).item_vis.setVisibility(is_exp ? View.VISIBLE : View.GONE);
        ((BasicViewHolder) viewHolder).item_vis.setActivated(is_exp);

        if (is_exp) {
            previous = expanded_pos;
        } else {
            previous = -1;
        }

        ((BasicViewHolder) viewHolder).card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded_pos = is_exp ? -1 : i;
                TransitionManager.beginDelayedTransition(recyclerView);

                if (previous != -1) {
                    notifyItemChanged(previous);

                }

                notifyItemChanged(expanded_pos);
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
        LinearLayout item_vis;
        TextView date;
        TextView gps_cdn;
        TextView precision;
        TextView mls_param;
        TextView mls_result;
        TextView difference;

        public BasicViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item);
            report = itemView.findViewById(R.id.id);
            item_vis = itemView.findViewById(R.id.item_vis);
            date = itemView.findViewById(R.id.date);
            gps_cdn = itemView.findViewById(R.id.gps_cdn);
            precision = itemView.findViewById(R.id.precision);
            mls_param = itemView.findViewById(R.id.mls_param);
            mls_result = itemView.findViewById(R.id.mls_result);
            difference = itemView.findViewById(R.id.difference);
        }
    }

}
