package rerucreations.recordkeeperapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 10/16/2017.
 */

public class MainActivityContentFragment extends Fragment {

    OnSelectListener onSelectListener;
    TextView txt_no_data;
    ListView list_records;
    SQLitDB sqLitDB;

    public SQLitDB getSqLitDB() {
        if (sqLitDB == null)
            sqLitDB = new SQLitDB(getActivity());
        return sqLitDB;
    }


    public class RecordAdapter extends BaseAdapter {

        Activity context;
        List<Records> recordsList;
        private LayoutInflater inflater;

        public RecordAdapter(Activity context, List<Records> recordsList) {
            this.context = context;
            this.recordsList = recordsList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return recordsList.size();
        }

        @Override
        public Object getItem(int position) {
            return recordsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_saved_records, viewGroup, false);

                viewHolder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
                viewHolder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                viewHolder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
                viewHolder.txt_place = (TextView) convertView.findViewById(R.id.txt_place);
                viewHolder.txt_duration = (TextView) convertView.findViewById(R.id.txt_duration);
                viewHolder.txt_comment = (TextView) convertView.findViewById(R.id.txt_comment);
                viewHolder.btn_go = (Button) convertView.findViewById(R.id.btn_go_map);
                viewHolder.btn_edit = (Button) convertView.findViewById(R.id.btn_edit);
                viewHolder.btn_delete = (Button) convertView.findViewById(R.id.btn_delete);
                viewHolder.img_preview = (ImageView) convertView.findViewById(R.id.img_preview);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txt_title.setText("Title :" + recordsList.get(position).getTitle());
            viewHolder.txt_name.setText("Activity :" + recordsList.get(position).getActivity_type());
            viewHolder.txt_date.setText("Date & Time :" + recordsList.get(position).getDate());
            viewHolder.txt_place.setText("Place :" + recordsList.get(position).getPlace());
            viewHolder.txt_duration.setText("Duration :" + recordsList.get(position).getDuration());
            viewHolder.txt_comment.setText("Comment :" + recordsList.get(position).getComment());

            String picturePath = recordsList.get(position).getPhoto();

            if (!picturePath.equalsIgnoreCase("")) {
                BitmapDrawable bitmapDrawable = null;
                bitmapDrawable = PictureUtils.getScaledDrawable(context, picturePath);
                viewHolder.img_preview.setImageDrawable(bitmapDrawable);
            }


            viewHolder.btn_go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String lat = recordsList.get(position).getLat();
                        String lng = recordsList.get(position).getLng();

                        Log.e("Adatper", "Get Lat : " + lat);
                        Log.e("Adatper", "Get Lng : " + lng);
                        if ((lat == null) || (lat.equalsIgnoreCase("")) || (lat.equalsIgnoreCase("0.0"))) {
                            showMapAlert();
                        } else {
                            Intent intent = new Intent(context, MapsActivity.class);
                            intent.putExtra("lat", lat);
                            intent.putExtra("lng", lng);
                            startActivity(intent);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String deleteId = recordsList.get(position).getId();
                    onSelectListener.onSelect(deleteId);
                }
            });

            viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String deleteId = recordsList.get(position).getId();
                    getSqLitDB().deleteRow(deleteId);
                    setList();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView txt_title, txt_name, txt_date, txt_place, txt_duration, txt_comment;
            ImageView img_preview;
            Button btn_go, btn_edit, btn_delete;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_main_activity_content, container, false);
        txt_no_data = (TextView)view.findViewById(R.id.txt_no_data);
        list_records = (ListView)view.findViewById(R.id.list_records);
        onSelectListener = (OnSelectListener)getActivity();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setList();
    }

    public void showMapAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("For current Lat-Lng not found");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void setList() {
        if (getSqLitDB().getRecordCount() == 0) {
            list_records.setVisibility(View.GONE);
            txt_no_data.setVisibility(View.VISIBLE);
        } else {
            list_records.setVisibility(View.VISIBLE);
            txt_no_data.setVisibility(View.GONE);
            list_records.setAdapter(new RecordAdapter(getActivity(), getSqLitDB().getRecordsList()));
        }
    }

    interface OnSelectListener{
        void onSelect(String id);
    }

}
