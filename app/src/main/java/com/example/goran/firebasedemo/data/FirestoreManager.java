package com.example.goran.firebasedemo.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class FirestoreManager {

    private static final String USERS = "users";
    private static final String CHECK_INS = "check_ins";
    private static final String BOOKMARKS = "bookmarks";

    private FirebaseUser user;

    public FirestoreManager() {
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<DocumentReference> saveCheckIn(LatLng latLng) {
        CheckIn checkIn = new CheckIn(latLng.latitude, latLng.longitude, new Date());
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.getUid())
                .collection(CHECK_INS)
                .add(checkIn);
    }

    public Task<DocumentReference> saveAsBookmark(LatLng latLng) {
        CheckIn checkIn = new CheckIn(latLng.latitude, latLng.longitude, new Date());
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.getUid())
                .collection(BOOKMARKS)
                .add(checkIn);
    }

    public Task<QuerySnapshot> getAllCheckIns() {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.getUid())
                .collection(CHECK_INS)
                .get();
    }

    public Task<QuerySnapshot> getBookmarks() {
        return FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(user.getUid())
                .collection(BOOKMARKS)
                .get();
    }
}
