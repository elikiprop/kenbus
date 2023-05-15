package com.example.kenbus;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kenbus.databinding.ActivityViewTicketBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;


public class ViewTicket extends AppCompatActivity {
    String the_output;

    DatabaseReference databaseReference_getId = FirebaseDatabase.getInstance().getReferenceFromUrl("https://kenbus-ae7e5-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ActivityViewTicketBinding binding;
    DatabaseReference reference;

    Button generate;
    ImageView qr_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewTicketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        generate = findViewById(R.id.readdataBtn);
        qr_pic = findViewById(R.id.qr_code);

        String theUserId = mUser.getUid();
        readData(theUserId);

       // String sample = "jane";


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix = multiFormatWriter.encode(the_output, BarcodeFormat.QR_CODE,300,300);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                    qr_pic.setImageBitmap(bitmap);

                }catch (WriterException e){
                    throw new RuntimeException(e);
                }
            }
        });




    }

    private void readData(String theUserId) {
        reference = FirebaseDatabase.getInstance().getReference("tickets");
        reference.child(theUserId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        Toast.makeText(ViewTicket.this,"Successfully Read",Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();

                        String _date = String.valueOf(dataSnapshot.child("date").getValue());
                        String _departure = String.valueOf(dataSnapshot.child("departure").getValue());
                        String _destination= String.valueOf(dataSnapshot.child("destination").getValue());
                        String _fullnames = String.valueOf(dataSnapshot.child("fullnames").getValue());
                        String _mpesaNumber = String.valueOf(dataSnapshot.child("mpesaNumber").getValue());
                        String _status = String.valueOf(dataSnapshot.child("status").getValue());
                        String _time = String.valueOf(dataSnapshot.child("time").getValue());

                        Map<String, String> ticketMap = new HashMap<>();
                        ticketMap.put("date",_date);
                        ticketMap.put("departure",_departure);
                        ticketMap.put("destination",_destination);
                        ticketMap.put("fullnames",_fullnames);
                        ticketMap.put("mpesaNumber",_mpesaNumber);
                        ticketMap.put("status",_status);
                        ticketMap.put("time",_time);

                        String content = mapToString(ticketMap);
                        the_output = content;


                        binding.tvDeparture.setText(_departure);
                        binding.tvDate.setText(_date);
                        binding.tvDestination.setText(_destination);
                        binding.tvMpesaNumber.setText(_mpesaNumber);
                        binding.tvName.setText(_fullnames);
                        binding.tvStatus.setText(_status);
                        binding.tvTime.setText(_time);
                        

                    }else{
                        Toast.makeText(ViewTicket.this,"User Doesn't Exist",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(ViewTicket.this,"Failed to read",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private String mapToString(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }
}