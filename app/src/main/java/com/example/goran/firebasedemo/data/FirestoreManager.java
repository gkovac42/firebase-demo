package com.example.goran.firebasedemo.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class FirestoreManager {

    private static final String COL_USERS = "users";
    private static final String COL_CHECK_INS = "check_ins";

    private FirebaseUser user;

    public FirestoreManager() {
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void saveCheckIn(LatLng latLng) {
        CheckIn checkIn = new CheckIn(latLng.latitude, latLng.longitude, new Date());

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
                .get();
    }
}
