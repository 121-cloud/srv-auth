package otocloud.auth.dao;


import java.util.List;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import otocloud.persistence.dao.JdbcDataSource;
import otocloud.persistence.dao.OperatorDAO;


/**
 * 操作用户表的持久层（auth_user表）。
 * <p>
 * Created by better/zhangye on 15/9/29.
 */
public class AuthDAO extends OperatorDAO {
	
    public AuthDAO(JdbcDataSource dataSource) {
        super(dataSource);
    }

    public void create(Long acct_biz_unit_post_id, 
			    		Long acct_app_activity_id, 
			    		Long acct_id, 
			    		Long app_id,
			    		Long app_activity_id,
			    		Long entry_id,  
			    		Handler<AsyncResult<Long>> done) {
    	
  	  Future<Long> retFuture = Future.future();
  	  retFuture.setHandler(done);	  


        final String insertUserSQL = "INSERT INTO acct_biz_unit_post_activity(" +
                "acct_biz_unit_post_id, acct_app_activity_id, acct_id, d_app_id, d_app_activity_id, entry_id, entry_datetime) " +
                "VALUES(?, ?, ?, ?, ?, ?, now())";
        JsonArray params = new JsonArray();
        //params.add(user.getOrgAcctId());
        params.add(acct_biz_unit_post_id);
        params.add(acct_app_activity_id);
        params.add(acct_id);
        params.add(app_id);
        params.add(app_activity_id);
        params.add(entry_id);

        Future<UpdateResult> innerFuture = Future.future();

        updateWithParams(insertUserSQL, params, innerFuture);

        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
                UpdateResult updateResult = result.result();

                Long id = updateResult.getKeys().getLong(0);                

                retFuture.complete(id);
            } else {
            	retFuture.fail(result.cause());
            }
        });
    }

    /**
     * 不执行数据删除，仅标记记录为删除状态。
     *
     * @param userId 用户ID.
     * @param future 删除成功后回调.
     */
    public void deleteById(Long id, Handler<AsyncResult<UpdateResult>> resultFuture) {
    	
		  Future<UpdateResult> retFuture = Future.future();
		  retFuture.setHandler(resultFuture);
	  
		  String sql = "delete from acct_biz_unit_post_activity WHERE id=?";
		  
		  this.updateWithParams(sql, 
				  	new JsonArray()
						  .add(id),  
						  retFuture);	

    }

    public void getList(Long acct_biz_unit_post_id, Future<ResultSet> future) {
        
	   final String sql = "SELECT * FROM view_acct_biz_unit_post_activity where acct_biz_unit_post_id=?";
	   JsonArray params = new JsonArray();
	   params.add(acct_biz_unit_post_id);
	
	   Future<ResultSet> innerFuture = Future.future();
	
	   this.queryWithParams(sql, params, innerFuture);
	
	   innerFuture.setHandler(result -> {
	       if (result.succeeded()) {
		       	ResultSet resultSet = result.result();
		       	future.complete(resultSet);	
	       } else {
	       		Throwable err = result.cause();								
	            future.fail(err);                
	       }
	   });    	
    	
    }
    
    public void appPermissionVerify(Long acctId, Long appId, Long userId, Future<Boolean> future) {
        
        final String sql = "SELECT count(*) as num FROM view_user_app WHERE acct_id=? AND app_id=? AND auth_user_id=?";
        JsonArray params = new JsonArray();
        params.add(acctId);
        params.add(appId);
        params.add(userId);

        Future<ResultSet> innerFuture = Future.future();

        this.queryWithParams(sql, params, innerFuture);

        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
            	ResultSet resultSet = result.result();
            	List<JsonObject> retDataArrays = resultSet.getRows();
            	if(retDataArrays != null && retDataArrays.size() > 0){
            		Long num = retDataArrays.get(0).getLong("num");
            		if(num > 0){
            			future.complete(true);
            		}else{
            			future.complete(false);
            		}
            	}else{
            		future.complete(false);
            	}

            } else {
            	Throwable err = result.cause();								
                future.fail(err);                
            }
        });

	}

    public void activityPermissionVerify(Long acctId, Long activityId, Long userId, Future<Boolean> future) {
        
        final String sql = "SELECT count(*) as num FROM view_user_activity WHERE acct_id=? AND d_app_activity_id=? AND auth_user_id=?";
        JsonArray params = new JsonArray();
        params.add(acctId);
        params.add(activityId);
        params.add(userId);

        Future<ResultSet> innerFuture = Future.future();

        this.queryWithParams(sql, params, innerFuture);

        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
            	ResultSet resultSet = result.result();
            	List<JsonObject> retDataArrays = resultSet.getRows();
            	if(retDataArrays != null && retDataArrays.size() > 0){
            		Long num = retDataArrays.get(0).getLong("num");
            		if(num > 0){
            			future.complete(true);
            		}else{
            			future.complete(false);
            		}
            	}else{
            		future.complete(false);
            	}

            } else {
            	Throwable err = result.cause();								
                future.fail(err);                
            }
        });

	}
    
}
