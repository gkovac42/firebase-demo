package com.example.goran.firebasedemo.data.remote;

import com.example.goran.firebasedemo.data.model.CheckIn;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreManager {

    private static final String COL_USERS = "users";
    private static final String COL_CHECK_INS = "check_ins";

    private FirebaseUser user;

    public FirestoreManager() {
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void saveCheckIn(CheckIn checkIn) {
        FirebaseFirestore.getInstance()
                .collection(COL_USERS)
                .document(user.getUid())
                .collection(COL_CHECK_INS)
                .add(checkIn);
    }

    public Task<QuerySnapshot> getAllCheckIns() {
        return FirebaseFirestore.getInstance()
                .collection(COL_USERS)
                .document(user.getUid())
                .collection(COL_CHECK_INS)
                .orderBy("date", Query.Direction.DESCENDING)
                .get();
    }
}
