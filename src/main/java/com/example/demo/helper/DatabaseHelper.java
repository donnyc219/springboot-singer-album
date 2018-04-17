package com.example.demo.helper;

import com.google.cloud.spanner.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseHelper implements InitializingBean {

    private DatabaseClient dbClient = null;
    private Spanner spanner = null;

    @Override
    public void afterPropertiesSet() {
        prepareDB();
    }

    public DatabaseClient getDbClient() {
        return dbClient;
    }

    public void addNewUser(String name) {

        List<Mutation> mutations = new ArrayList<Mutation>();
        Mutation m = Mutation.newInsertBuilder("User")
                .set("user_id")
                .to(4)
                .set("name")
                .to("testing")
                .build();
        mutations.add(m);
        dbClient.write(mutations);
    }

    public long getMaxId(String tablename, String columnName){
        long maxID = 0;
        String sql = "SELECT MAX(" + columnName + ") FROM " + tablename;
        try {
            ResultSet rs = dbClient.singleUse().executeQuery(Statement.of(sql));

            while (rs.next())   maxID = rs.getLong(0);
        } catch (Exception e){

        }

        return maxID;
    }

    public void closeSpanner(){
        this.spanner.close();
        System.out.println("Spanner closed");
    }

    private void prepareDB(){

//        SpannerOptions options = SpannerOptions.newBuilder().build();
        SpannerOptions options = SpannerOptions.newBuilder().setProjectId("my-little-app-200719").build();
        Spanner spanner = options.getService();
        this.spanner = spanner;
        try {
//            String projectId = "my-little-app-200719";
            String projectId = options.getProjectId();
            String instanceId = "test-instance";
            String database = "example-db";
            // of(String project, String instance, String database)
            DatabaseId db = DatabaseId.of(options.getProjectId(), instanceId, database);
            // [END init_client]
            // This will return the default project id based on the environment.
            String clientProject = spanner.getOptions().getProjectId();
            if (!db.getInstanceId().getProject().equals(projectId)) {
                System.err.println("Invalid project specified. Project in the database id should match"
                        + "the project name set in the environment variable GCLOUD_PROJECT. Expected: "
                        + clientProject);
            }
            // [START init_client]
            DatabaseClient dbClient = spanner.getDatabaseClient(db);
//            DatabaseAdminClient dbAdminClient = spanner.getDatabaseAdminClient();
            // [END init_client]
            this.dbClient = dbClient;
        } catch (Exception e){
            System.out.println("something wrong!");
        } finally {
//            spanner.close();
        }
    }
}
