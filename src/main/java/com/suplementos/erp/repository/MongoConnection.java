package com.suplementos.erp.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

    private static final String CONNECTION_STRING = "mongodb+srv://nickcodachave_db_user:C4Awb6xj8ct8l391@clustertest.v8fzq0t.mongodb.net/?retryWrites=true&w=majority&appName=ClusterTest";
    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }
        return mongoClient.getDatabase("lojaSuplementos");
    }
}