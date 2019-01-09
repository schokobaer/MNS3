package at.ac.tuwien.mns.group3.mnsg3e3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.ac.tuwien.mns.group3.mnsg3e3.interfaces.ICommunication;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;
import org.w3c.dom.Text;

public class Fragment_Detail extends Fragment {


    public static Fragment newInstance(Report report){
        Fragment fragment = new Fragment_Detail();
        Bundle bundle = new Bundle();
        bundle.putSerializable("report", report);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {

        final Report report = (Report) getArguments().getSerializable("report");

        String date = String.format("%-20s: %s", "Date", report.getDate());
        String gps_cdn = String.format("%-20s: %s", "Coordinates", report.getCdn());
        String precision = String.format("%-20s: %s", "Precision", report.getPrecision());
        String mls_param = String.format("%-20s: %s", "MLS Parameters", report.getMls_param());
        String mls_result = String.format("%-20s: %s", "MLS Result", report.getMls_result());
        String difference = String.format("%-20s: %s", "Difference", report.getDiff());

        ((TextView) view.findViewById(R.id.date)).setText(date);
        ((TextView) view.findViewById(R.id.gps_cdn)).setText(gps_cdn);
        ((TextView) view.findViewById(R.id.precision)).setText(precision);
        ((TextView) view.findViewById(R.id.mls_param)).setText(mls_param);
        ((TextView) view.findViewById(R.id.mls_result)).setText(mls_result);
        ((TextView) view.findViewById(R.id.difference)).setText(difference);

        ((FloatingActionButton) view.findViewById(R.id.mail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO implement send
            }
        });

        ((FloatingActionButton) view.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO implement delete and returning to activity

                ((ICommunication) getActivity()).delete(report);
            }
        });


    }


}
