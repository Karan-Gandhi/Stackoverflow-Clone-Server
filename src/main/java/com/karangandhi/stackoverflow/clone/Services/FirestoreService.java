package com.karangandhi.stackoverflow.clone.Services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.karangandhi.stackoverflow.clone.Services.FirebaseService.app;

public class FirestoreService {
    private static Firestore database;

    public static Firestore Init() {
        database = FirestoreClient.getFirestore(app);
        return database;
    }

    public static WriteResult addData(String reference, Map<String, ?> data) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = database.document(reference);
        ApiFuture<WriteResult> result = documentReference.set(data);
        return result.get();
    }

    public static WriteResult addData(String collection, String document, Map<String, ?> data) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = database.collection(collection).document(document);
        ApiFuture<WriteResult> result = documentReference.set(data);
        return result.get();
    }

    public static <T> WriteResult addData(String collection, String document, T data) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = database.collection(collection).document(document);
        ApiFuture<WriteResult> result = documentReference.set(data);
        return result.get();
    }

    public static DocumentSnapshot readData(String collection, String document) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = database.collection(collection).document(document);
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot snapshot = documentSnapshotApiFuture.get();
        return snapshot.exists() ? snapshot : null;
    }

    public static DocumentSnapshot readData(String reference) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = database.document(reference);
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot snapshot = documentSnapshotApiFuture.get();
        return snapshot.exists() ? snapshot : null;
    }

    public static List<QueryDocumentSnapshot> getCollectionSnapshot(String collection) throws ExecutionException, InterruptedException {
        CollectionReference documentReference = database.collection(collection);
        ApiFuture<QuerySnapshot> snapshotApiFuture = documentReference.get();
        List<QueryDocumentSnapshot> queryDocumentSnapshotList = snapshotApiFuture.get().getDocuments();
        return queryDocumentSnapshotList;
    }

    public static WriteResult deleteData(String collection, String document) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResultApiFuture = database.collection(collection).document(document).delete();
        WriteResult writeResult = writeResultApiFuture.get();
        return writeResult;
    }

    public static void deleteCollection(String collection, int batchSize) throws ExecutionException, InterruptedException {
        deleteCollection(database.collection(collection), batchSize);
    }

    // delete the collections in batches avoid errors
    private static void deleteCollection(CollectionReference collectionReference, int batchSize) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> apiFuture = collectionReference.limit(batchSize).get();
        int deleted = 0;
        List<QueryDocumentSnapshot> documentSnapshots = apiFuture.get().getDocuments();
        for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
            documentSnapshot.getReference().delete();
            deleted++;
            if (deleted >= batchSize) deleteCollection(collectionReference, batchSize);
        }
    }
}
