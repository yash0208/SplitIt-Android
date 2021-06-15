package com.rajaryan.splitit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddExpense extends AppCompatActivity {
    EditText email,desc,amount;
    String e,d,a,c,c1;
    RadioButton genderradioButton;
    RadioGroup radioGroup;
    FirebaseAuth mAuth;
    FloatingActionButton b;
    float owei;
    float give;
    String g1;
    String owe;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        b=findViewById(R.id.fab);
        email=findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
       uid=user.getUid().toString();
        HashMap<String, Object> result = new HashMap<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/" + uid).child("Owe");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               owe=snapshot.getValue().toString();
               Toast.makeText(AddExpense.this,owe,Toast.LENGTH_LONG).show();
                owei=Float.parseFloat(owe);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference myRef1 = database.getReference("Users/" + uid).child("Have To Pay");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                g1=snapshot.getValue().toString();
                Toast.makeText(AddExpense.this,g1,Toast.LENGTH_LONG).show();
                give=Float.parseFloat(g1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        desc=findViewById(R.id.desc);
        amount=findViewById(R.id.cost);
        mAuth = FirebaseAuth.getInstance();
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e=email.getText().toString();
                d=desc.getText().toString();
                a=amount.getText().toString();
                if(TextUtils.isEmpty(e)){
                    Toast.makeText(AddExpense.this,"Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(d)){
                    Toast.makeText(AddExpense.this,"Enter Valid Description", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(a)){
                    Toast.makeText(AddExpense.this,"Enter Valid Ammount", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(c)){
                    Toast.makeText(AddExpense.this,"Select Valid Choice", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(c.equals("Paid By You")){
                        c1="1";
                        float owe2=Integer.parseInt(amount.getText().toString());
                        float finalowe=owe2/2;
                        float O=owei+finalowe;
                        String OweAmt=String.valueOf(O);
                        Toast.makeText(AddExpense.this,OweAmt,Toast.LENGTH_LONG).show();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users/" + uid);
                        myRef.child("Owe").setValue(OweAmt);
                    }
                    else {
                        c1="0";
                        float owe2=Integer.parseInt(amount.getText().toString());
                        float finalowe=owe2/2;
                        float O1=give+finalowe;
                        String OweAmt=String.valueOf(O1);
                        Toast.makeText(AddExpense.this,OweAmt,Toast.LENGTH_LONG).show();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users/" + uid);
                        myRef.child("Have To Pay").setValue(OweAmt);
                    }
                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid=user.getUid().toString();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("Partner",e);
                    result.put("Description", d);
                    result.put("Amount", a);
                    result.put("Choice",c1);
                    result.put("Uid",uid);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Users/" + uid);
                    myRef.child("Activity").push().setValue(result);

                }
            }
        });
    }
    public void onclickbuttonMethod(View v){
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderradioButton = (RadioButton) findViewById(selectedId);
        if(selectedId==-1){
            Toast.makeText(AddExpense.this,"Nothing selected", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(AddExpense.this,genderradioButton.getText(), Toast.LENGTH_SHORT).show();
            c=genderradioButton.getText().toString();
        }

    }

    public void back(View view) {
        onBackPressed();
    }
}