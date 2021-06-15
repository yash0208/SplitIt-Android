package com.rajaryan.splitit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    RecyclerView rec;
    Adapter adepter;
    float owei;
    float give;
    String g1;
    TextView welcome;
    String owe;
    DatabaseReference ref;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rec=findViewById(R.id.rec);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        welcome=findViewById(R.id.welcome);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid().toString();
        HashMap<String, Object> result = new HashMap<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Ref = database.getReference("Users/" + uid).child("Name");
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.getValue().toString();
                welcome.setText("Welcome,"+name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference myRef = database.getReference("Users/" + uid).child("Owe");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                owe=snapshot.getValue().toString();
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
                give=Float.parseFloat(g1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String uid = user.getUid();
        Query query= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Activity");
        FirebaseRecyclerOptions<Tracker> option =
                new FirebaseRecyclerOptions.Builder<Tracker>()
                        .setQuery(query,Tracker.class)
                        .setLifecycleOwner(this)
                        .build();
        adepter=new Adapter(option);
        rec.setAdapter(adepter);
        rec.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adepter.startListening();
    }

    public void addexp(View view) {
        Intent i=new Intent(HomeActivity.this,AddExpense.class);
        startActivity(i);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent i=new Intent(HomeActivity.this,MainActivity.class);
        startActivity(i);
    }

    public class Adapter extends FirebaseRecyclerAdapter<Tracker,Adapter.viewholder>{
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter(@NonNull FirebaseRecyclerOptions<Tracker> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull Tracker tracker) {
            viewholder.partner.setText(tracker.Partner);
            viewholder.desc.setText(tracker.Description);
            viewholder.amount.setText(tracker.Amount);
            String id=getRef(i).getKey();
            viewholder.id.setText(id);
            float owe2=Float.parseFloat(tracker.Amount);
            viewholder.reminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ tracker.Partner});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Reminder");
                    email.putExtra(Intent.EXTRA_TEXT, "I Would Like To Remind About This Expense Where Expense Was About "+tracker.Description+" And Amount Was "+tracker.Amount);

                    //need this to prompts email client only
                    email.setType("message/rfc822");

                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
            });
            viewholder.settle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ref=FirebaseDatabase.getInstance().getReference("Users/" + uid).child("Activity").child(viewholder.id.getText().toString());
                    ref.removeValue();
                    if(tracker.Choice.equals("1")){
                        float finalowe=owe2/2;
                        float O=owei-finalowe;
                        String OweAmt=String.valueOf(O);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users/" + uid);
                        myRef.child("Owe").setValue(OweAmt);
                    }
                    else {
                        float finalowe=owe2/2;
                        float O1=give-finalowe;
                        String OweAmt=String.valueOf(O1);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users/" + uid);
                        myRef.child("Have To Pay").setValue(OweAmt);
                    }

                }

            });

        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tracker, parent, false);
            return new viewholder(view);
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView partner,desc,amount,id;
            Button settle,reminder;
            public viewholder(@NonNull View itemView) {
                super(itemView);
                id=itemView.findViewById(R.id.id);
                partner=itemView.findViewById(R.id.partner);
                desc=itemView.findViewById(R.id.desc);
                amount=itemView.findViewById(R.id.amount);
                settle=itemView.findViewById(R.id.settle);
                reminder=itemView.findViewById(R.id.reminder);            }
        }
    }
}
