package ch.epfl.sweng.zuluzulu.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseDocument;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseQuery;
import ch.epfl.sweng.zuluzulu.Firebase.Database.OperationWithFirebaseMapList;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.Utility;

public class CollectionMock implements DatabaseCollection {
    private Map<String, Object> map;

    CollectionMock() {
        map = Utility.createMapWithAll();
    }

    @Override
    public DatabaseDocument document(String documentPath) {
        return new DocumentMock();
    }

    @Override
    public DatabaseDocument document() {
        return new DocumentMock();
    }

    @Override
    public Task<DocumentReference> add(@NonNull Map<String, Object> data) {
        return new TaskMock<DocumentReference>();
    }

    @Override
    public Task<QuerySnapshot> getAndAddOnSuccessListener(OperationWithFirebaseMapList listener) {
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(map);
        List<FirebaseMapDecorator> list = new ArrayList<>();
        list.add(fmap);
        listener.applyList(list);
        IdlingResourceFactory.incrementCountingIdlingResource();
        return new TaskMock<>();
    }

    @Override
    public String getId() {
        return "1";
    }

    @Override
    public void addSnapshotListener(OperationWithFirebaseMapList listener) {
        FirebaseMapDecorator fmap = new FirebaseMapDecorator(map);
        List<FirebaseMapDecorator> list = new ArrayList<>();
        list.add(fmap);
        listener.applyList(list);
    }

    @Override
    public DatabaseQuery whereGreaterThan(String field, Object value) {
        return new QueryMock();
    }

    @Override
    public DatabaseQuery whereEqualTo(String field, Object value) {
        return new QueryMock();
    }

    @Override
    public DatabaseQuery orderBy(String field) {
        return new QueryMock();
    }

}
