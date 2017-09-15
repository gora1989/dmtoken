package dm.com.token;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gora3 on 5/17/17.
 */

public class CustomParkingListAdapter extends ArrayAdapter<ParkingModel> implements View.OnClickListener {

    private ArrayList<ParkingModel> dataSet;
    Context mContext;

    private static class ViewHolder {
        LinearLayout parking_row_ll;
        TextView txtName;
        TextView txtPhone;
        TextView txtVehicleNo;
        TextView txtToken;
        TextView txtTimestamp;
        TextView txtStatus;
        //ImageView status;
    }

    public CustomParkingListAdapter(ArrayList<ParkingModel> data, Context context) {
        super(context, R.layout.parking_row, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        ParkingModel parkingModel = (ParkingModel) object;

     /*   switch (v.getId())
        {
            case R.id.status:

                Snackbar.make(v, "Status " +parkingModel.getStatus(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }*/
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ParkingModel parkingModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.parking_row, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtPhone = (TextView) convertView.findViewById(R.id.phone);
            viewHolder.txtVehicleNo = (TextView) convertView.findViewById(R.id.vehicle_num);
            viewHolder.txtToken = (TextView) convertView.findViewById(R.id.token_num);
            viewHolder.txtTimestamp = (TextView) convertView.findViewById(R.id.time);
            viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.status_text);
            // viewHolder.status = (ImageView) convertView.findViewById(R.id.status);
            viewHolder.parking_row_ll = (LinearLayout) convertView.findViewById(R.id.parking_row_ll);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(parkingModel.getName());
        viewHolder.txtPhone.setText(parkingModel.getPhone());
        viewHolder.txtVehicleNo.setText(parkingModel.getVehicle_no());
        viewHolder.txtToken.setText(parkingModel.getToken());
        viewHolder.txtTimestamp.setText(getDate(parkingModel.getParkingTimestamp()));
        viewHolder.txtStatus.setText(parkingModel.getStatus());
        /*viewHolder.status.setOnClickListener(this);
        viewHolder.status.setTag(position);*/
        return convertView;
    }


    private String getDate(long timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    public void showDialog() {
        // custom dialog

    }
}
