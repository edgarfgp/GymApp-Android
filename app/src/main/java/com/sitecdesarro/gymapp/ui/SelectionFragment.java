package com.sitecdesarro.gymapp.ui;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sitecdesarro.gymapp.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectionFragment extends Fragment {


    private FragmentManager fragmentManager;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    NavigationView navView;
    private CircleImageView imagen;
    private ContentResolver resolver;
    private StorageReference storageRef;
    MainActivity activity;

    public ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity =(MainActivity)context;
        navView = (NavigationView)activity.findViewById(R.id
                .navigationView);

        navView.getMenu().findItem(R.id.itemProfile).setVisible(true);
        navView.getMenu().findItem(R.id.itemReservation).setVisible(true);
        navView.getMenu().findItem(R.id.itemLogOut).setVisible(true);
        mAuth = FirebaseAuth.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://pruebaproye-2561a.appspot.com");
        resolver = activity.getContentResolver();
        StorageReference sub = storageRef.child("imagenes").child(mAuth.getCurrentUser().getUid());


        imagen = (CircleImageView) activity.findViewById(R.id.imgUserNavigate);
        imagen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            22);
                }else{
                    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,44);
                }


            }

        });

        sub.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imagen.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(mAuth.getCurrentUser().getUid());

        final TextView nom = (TextView)activity.findViewById(R.id.txtNameNav);
        final TextView cor = (TextView)activity.findViewById(R.id.txtEmailNav);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombre").getValue().toString();
                String correo = dataSnapshot.child("email").getValue().toString();
                nom.setText(nombre);
                cor.setText(correo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 22: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,44);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public SelectionFragment() {
        // Required empty public constructor

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == 44 && resultCode == RESULT_OK && data != null) {

            try {
                InputStream stream = activity.getContentResolver().openInputStream(data.getData());
                Bitmap bitmapx = BitmapFactory.decodeStream(stream);
                stream.close();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapx.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                final StorageReference imgref = storageRef.child("imagenes").child(mAuth.getCurrentUser().getUid());
                byte[] data1 = baos.toByteArray();
                //Bitmap resized = resize(bitmapx,1000,1000);

                UploadTask uploadTask = imgref.putBytes(data1);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    }
                });
                imagen.setImageBitmap(bitmapx);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_selection, container, false);




        rootView.findViewById(R.id.cardBike).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), BikeActivity.class));


            }
        });

        rootView.findViewById(R.id.cardFunctional).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), FunctionalActivity
                        .class));
            }
        });

        return rootView;

    }



}
