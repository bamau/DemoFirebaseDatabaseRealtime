package com.tbm.bamau.realtimedatabasefirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CapNhatContactActivity extends AppCompatActivity {

    EditText edtId,edtTen,edtPhone,edtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_contact);
        addControls();
        getContactDetail();
    }
    private void getContactDetail() {
        Intent intent=getIntent();
        final String key=intent.getStringExtra("KEY");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("contacts");

        //truy suất và lắng nghe sự thay đổi dữ liệu
        //chỉ truy suất node được chọn trên ListView myRef.child(key)
        //addListenerForSingleValueEvent để lấy dữ liệu đơn
        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    //nó trả về 1 DataSnapShot, mà giá trị đơn nên gọi getValue trả về 1 HashMap
                    HashMap<String,Object> hashMap= (HashMap<String, Object>) dataSnapshot.getValue();
                    //HashMap này sẽ có kích  thước bằng số Node con bên trong node truy vấn
                    //mỗi phần tử có key là name được định nghĩa trong cấu trúc Json của Firebase
                    edtId.setText(key);
                    edtTen.setText(hashMap.get("name").toString());
                    edtEmail.setText(hashMap.get("email").toString());
                    edtPhone.setText(hashMap.get("phone").toString());
                }
                catch (Exception ex)
                {
                    Log.e("LOI_JSON",ex.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("LOI_CHITIET", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
    private void addControls() {
        edtId=findViewById(R.id.edtContactId);
        edtTen=findViewById(R.id.edtTen);
        edtPhone=findViewById(R.id.edtPhone);
        edtEmail=findViewById(R.id.edtEmail);
    }
    public void xulySua(View view) {
        String key=edtId.getText().toString();
        String phone=edtPhone.getText().toString();
        String name=edtTen.getText().toString();
        String email=edtEmail.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("contacts");
        myRef.child(key).child("phone").setValue(phone);
        myRef.child(key).child("email").setValue(email);
        myRef.child(key).child("name").setValue(name);
        finish();
    }

    public void xulyXoa(View view) {
        String key=edtId.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
        DatabaseReference myRef = database.getReference("contacts");
        myRef.child(key).removeValue();
        finish();
    }
}
