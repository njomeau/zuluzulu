package ch.epfl.sweng.zuluzulu.Database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseQuery;

public class QueryMock implements DatabaseQuery{
    @Override
    public DatabaseQuery orderBy(String field) {
        return this;
    }

    @Override
    public DatabaseQuery limit(int limit) {
        return this;
    }

    @Override
    public Task<QuerySnapshot> get() {
        return new TaskMock<>();
    }
}
