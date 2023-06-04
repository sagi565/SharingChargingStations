package com.example.sharingchargingstations;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.sharingchargingstations.Model.ChargingStation;
import com.example.sharingchargingstations.Model.ChargingStationStatus;
import com.example.sharingchargingstations.Model.Model;
import com.example.sharingchargingstations.Model.TypeChargingStation;
import com.example.sharingchargingstations.Model.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfileFragment extends Fragment implements Model.IModelUpdate {
    private EditText etName;
    private EditText etCity;
    private EditText etStreet;
    private EditText etHouseNumber;
    private TextView tvMyChargingStation;
    private ImageView btnDeleteChargingStation;
    private ImageView ivProfile;
    private TextView tvItemAddress;
    private TextView tvItemHours;
    private TextView tvItemType;
    private TextView tvItemPricePerHour;
    private LinearLayout llChargingStation;
    private Button btnSelectImage;

    private Model model = Model.getInstance();
    private User currentUser = model.getCurrentUser();
    private ChargingStation chargingStation;
    public static final int RequestPermissionCode = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.fragment_profile,container, false);

        etName = view.findViewById(R.id.etName);
        etCity = view.findViewById(R.id.etCity);
        etStreet = view.findViewById(R.id.etStreet);
        etHouseNumber = view.findViewById(R.id.etHouseNumber);

        tvItemAddress = view.findViewById(R.id.tvItemAddress);
        tvItemHours = view.findViewById(R.id.tvItemHours);
        tvItemType = view.findViewById(R.id.tvChargingType);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);

        tvItemPricePerHour = view.findViewById(R.id.tvPricePerHour);
        btnDeleteChargingStation = view.findViewById(R.id.iv_trash);
        llChargingStation = view.findViewById(R.id.llChargingStation);

        ivProfile  = view.findViewById(R.id.ivProfile);
        EnableRuntimePermission();
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });

        chargingStation = model.getCurrentUser().getMyChargingStation();
        if(chargingStation != null && chargingStation.getStatus() == ChargingStationStatus.active){
            updateUiFields();
        }
        else{
            tvItemType.setVisibility(View.GONE);
            tvItemAddress.setVisibility(View.GONE);
            tvItemHours.setText("Add Charging Station");
            btnDeleteChargingStation.setColorFilter(Color.rgb(50,50,50));
            tvItemPricePerHour.setVisibility(View.GONE);
            btnDeleteChargingStation.setEnabled(false);
        }

        etName.setText(model.getCurrentUser().getName());

        if(currentUser.getProfileImage() != null)
            Picasso.get().load(currentUser.getProfileImage()).into(ivProfile);


        btnDeleteChargingStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvItemType.setVisibility(View.GONE);
                tvItemAddress.setVisibility(View.GONE);
                tvItemHours.setText("Add Charging Station");
                btnDeleteChargingStation.setColorFilter(Color.rgb(50,50,50));
                tvItemPricePerHour.setVisibility(View.GONE);
                model.getCurrentUser().getMyChargingStation().setStatus(ChargingStationStatus.canceled);
            }
        });
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        llChargingStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString() == "" || etCity.getText().toString() == "" || etStreet.getText().toString() == "" || etHouseNumber.getText().toString() == ""){
                    Toast.makeText(getActivity(), "You need to fill in all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(getActivity(), AddChargingStationActivity.class);
                i.putExtra("City", etCity.getText().toString());
                i.putExtra("Street", etStreet.getText().toString());
                i.putExtra("HouseNumber", etHouseNumber.getText().toString());
                startActivityForResult(i,1);
            }
        });


        model.registerModelUpdate(this);

        return view;
    }
    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 6 );
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 6) {

            // Get the url of the image from data
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                // update the preview image in the layout
                ivProfile.setImageURI(selectedImageUri);
                try {
                    model.uploadUserImage(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        if (requestCode == 7) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivProfile.setImageBitmap(imageBitmap);
            model.uploadUserImage(imageBitmap);
        }
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    private void updateUiFields(){
        tvItemType.setText(chargingStation.getType().toString());
        if(chargingStation.getType() == TypeChargingStation.CHAdeMO)
            tvItemType.setTextSize(13);
        else
            tvItemType.setTextSize(20);

        tvItemAddress.setText(chargingStation.getStationAddress().toString());
        tvItemHours.setText(chargingStation.getTime());
        tvItemPricePerHour.setText(String.valueOf(chargingStation.getPricePerHour()).replace(".0", "") + "₪");
        btnDeleteChargingStation.setColorFilter(Color.rgb(0,0,0));
        btnDeleteChargingStation.setEnabled(true);
        etCity.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getCity());
        etStreet.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getStreet());
        etHouseNumber.setText(model.getCurrentUser().getMyChargingStation().getStationAddress().getHouseNumber());
        if(currentUser.getProfileImage() != null)
            Picasso.get().load(currentUser.getProfileImage()).into(ivProfile);
        //ivProfile.setImageURI(Uri.parse(currentUser.getProfileImage()));

    }
    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(),"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        model.unRegisterModelUpdate(this);
    }

    @Override
    public void userUpdate() {

    }

    @Override
    public void stationUpdate() {
        updateUiFields();
    }
    @Override
    public void rentalUpdate() {

    }
}
