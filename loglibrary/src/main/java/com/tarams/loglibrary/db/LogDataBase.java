package com.tarams.loglibrary.db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.tarams.loglibrary.Logs.info;

public class LogDataBase {
    public void insertLogData(TraceAspectLogModelDb model) {
        if (model.id == null) {
            model.id = UUID.randomUUID().toString();
        }
        updateToDb(model);
    }

    private void updateToDb(final TraceAspectLogModelDb model) {
        info("inserting db");
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                info("executing db");
                realm.copyToRealmOrUpdate(model);
                info("executing completed");
            }
        });
    }

    public void deleteLogs(final String[] strings) {
        info("deleting db");
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                info("executing db");
                for (int i = 0; i < strings.length; i++) {
                    TraceAspectLogModelDb obj = realm.where(TraceAspectLogModelDb.class)
                            .contains("id", strings[i])
                            .findFirst();
                    if(obj!=null){
                        obj.deleteFromRealm();
                    }
                }
                info("executing completed");
            }
        });
    }

    public List<TraceAspectLogModelDb> logList() {
        RealmResults<TraceAspectLogModelDb> listItems = Realm.getDefaultInstance().where(TraceAspectLogModelDb.class)
                .findAll();
        List<TraceAspectLogModelDb> result=new ArrayList<>();
        for(TraceAspectLogModelDb item:listItems){
            result.add(new TraceAspectLogModelDb(item));
        }
        return result;
    }

    public void update(TraceAspectLogModelDb item) {
        updateToDb(item);
    }

    public void insertLogDatas(List<TraceAspectLogModelDb> logs) {

    }
    private void updateToDb(final List<TraceAspectLogModelDb> model) {
        for(TraceAspectLogModelDb item:model){
            if(item.id==null){
                item.id=UUID.randomUUID().toString();
            }
        }
        info("inserting db");
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                info("executing db");
                realm.copyToRealmOrUpdate(model);
                info("executing completed");
            }
        });
    }
}
