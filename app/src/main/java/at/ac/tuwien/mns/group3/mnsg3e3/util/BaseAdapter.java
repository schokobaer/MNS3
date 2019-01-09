package at.ac.tuwien.mns.group3.mnsg3e3.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.mns.group3.mnsg3e3.Fragment_Detail;
import at.ac.tuwien.mns.group3.mnsg3e3.R;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

public class BaseAdapter extends RecyclerView.Adapter {

    List<Report> dataset;
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

        final Report report = dataset.get(i);

        ((BasicViewHolder) viewHolder).date.setText(report.getDate());
        ((BasicViewHolder) viewHolder).location.setText("Location: " + report.getCdn());


        ((BasicViewHolder) viewHolder).card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = recyclerView.getContext();
                FrameLayout layout = (FrameLayout) recyclerView.getParent();
                //layout.removeAllViews();
                Fragment detail = Fragment_Detail.newInstance(dataset.get(i));
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.main, detail).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public static class BasicViewHolder extends RecyclerView.ViewHolder {

        View card;
        TextView location;
        TextView date;

        public BasicViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item);
            location = itemView.findViewById(R.id.list_item_location);
            date = itemView.findViewById(R.id.list_item_date);
        }
    }

}
