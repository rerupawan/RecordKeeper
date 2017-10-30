package rerucreations.recordkeeperapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AddRecordActivity extends AppCompatActivity implements AddRecordActivitySubmitFragment.OnButtonClickListener, AddRecordActivityTitleFragment.OnBackListener{

    private String TAG = "AddRecordActivity";
    AddRecordActivityDetailFragment addRecordActivityDetailFragment;
    AddRecordActivitySubmitFragment addRecordActivitySubmitFragment;
    AddRecordActivityTitleFragment addRecordActivityTitleFragment;

    SQLitDB sqLitDB;
    String id;

    double latitude, longitude;
    String picturePath = "", title, place, duration, comment, activity_type, str_date, str_time,
            user_location;

    public SQLitDB getSqLitDB() {
        if (sqLitDB == null)
            sqLitDB = new SQLitDB(AddRecordActivity.this);
        return sqLitDB;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record_activity);


        Bundle b = getIntent().getExtras();
        if(b != null)
            id = b.getString("id"); //get activity id


        addRecordActivityDetailFragment = new AddRecordActivityDetailFragment();
        addRecordActivitySubmitFragment = new AddRecordActivitySubmitFragment();
        addRecordActivityTitleFragment = new AddRecordActivityTitleFragment();

        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        addRecordActivityTitleFragment.setArguments(bundle);
        addRecordActivityDetailFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().add(R.id.fragment_title, addRecordActivityTitleFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_detail, addRecordActivityDetailFragment, addRecordActivityDetailFragment.TAG).commit();
        getFragmentManager().beginTransaction().add(R.id.fragment_submit, addRecordActivitySubmitFragment).commit();



    }

     @Override
    public void onSubmitClick()
    {

        if (addRecordActivityTitleFragment.isAllFilled() && addRecordActivityDetailFragment.isAllFilled()) {

            title = addRecordActivityTitleFragment.getTitle();
            picturePath = addRecordActivityTitleFragment.getPicturePath();

            activity_type = addRecordActivityDetailFragment.getActivity_type();
            str_date = addRecordActivityDetailFragment.getStr_date();
            str_time = addRecordActivityDetailFragment.getStr_time();
            user_location = addRecordActivityDetailFragment.getUser_location();
            place = addRecordActivityDetailFragment.getPlace();
            duration = addRecordActivityDetailFragment.getDuration();
            comment = addRecordActivityDetailFragment.getComment();
            latitude = addRecordActivityDetailFragment.getLatitude();
            longitude = addRecordActivityDetailFragment.getLongitude();

            Log.d(TAG, "--------------");

            Log.d(TAG, "Title-->         " + title);
            Log.d(TAG, "Photo Path-->    " + picturePath);
            Log.d(TAG, "Activity Type--> " + activity_type);
            Log.d(TAG, "Date-->          " + str_date);
            Log.d(TAG, "Time-->          " + str_time);
            Log.d(TAG, "Location-->      " + user_location);
            Log.d(TAG, "Place-->         " + place);
            Log.d(TAG, "Duration-->      " + duration);
            Log.d(TAG, "Comment-->       " + comment);

            Log.d(TAG, "--------------");

            String dateTime = str_date.concat(" ,").concat(str_time);

            Records Records = new Records(title, activity_type, place, duration, comment,
                    String.valueOf(latitude), String.valueOf(longitude), dateTime, picturePath);

            if (!id.equalsIgnoreCase("0")) {
                Log.d(TAG, "Calling Update");
                getSqLitDB().updateRecords(id, Records);
            } else {
                Log.d(TAG, "Calling Add");
                getSqLitDB().addRecord(Records);
            }
            finish();
//            MainActivityContentFragment.setList();
//           getActivity().getSupportFragmentManager().popBackStackImmediate();

    }
    }


    @Override
    public void onCancelClick()
    {
        finish();
    }
    @Override
    public void  onBackClick(){
       addRecordActivityTitleFragment.saveTempData();
        addRecordActivityDetailFragment.saveTempData(); finish();}


}
