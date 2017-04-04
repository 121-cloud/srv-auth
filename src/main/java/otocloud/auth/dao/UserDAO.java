package otocloud.auth.dao;


import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import otocloud.persistence.dao.JdbcDataSource;
import otocloud.persistence.dao.OperatorDAO;
import otocloud.persistence.dao.TransactionConnection;

import java.util.List;


/**
 * 操作用户表的持久层（auth_user表）。
 * <p>
 * Created by better/zhangye on 15/9/29.
 */
public class UserDAO extends OperatorDAO {
	
    public UserDAO(JdbcDataSource dataSource) {
        super(dataSource);
    }

    public void create(JsonObject user, Future<JsonObject> future) {

        final String insertUserSQL = "INSERT INTO auth_user(" +
                "name, password, cell_no, email, status, entry_id, entry_datetime) " +
                "VALUES(?, ?, ?, ?, ?, ?, now())";
        JsonArray params = new JsonArray();
        //params.add(user.getOrgAcctId());
        params.add(user.getString("name"));
        params.add(user.getString("password"));
        params.add(user.getString("cell_no", ""));
        params.add(user.getString("email", ""));
        params.add("A"); //status
        params.add(user.getLong("entry_id", 0L)); //设置该记录的创建人

        Future<UpdateResult> innerFuture = Future.future();

        updateWithParams(insertUserSQL, params, innerFuture);

        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
                UpdateResult updateResult = result.result();
                int num = updateResult.getUpdated();

                Long userId = updateResult.getKeys().getLong(0);
                user.put("id", userId);

                future.complete(user);

                logger.info("更新了" + num + "条记录.");
                logger.info(updateResult.toJson());
            } else {
                future.fail(result.cause());
                logger.warn("AuthService 无法增加新用户." + result.cause().getMessage());
            }
        });
    }
    
    public void joinToAcct(JsonObject user, Long acctId, Boolean isOwner, Long bizUnitId, Boolean d_is_global_bu, Long postId, Long authRoleId, Long auth_user_id, Future<JsonObject> future) {
        
        Long entryId = user.getLong("entry_id", 0L);

		JDBCClient jdbcClient = this.getDataSource().getSqlClient();
		jdbcClient.getConnection(conRes -> {
			if (conRes.succeeded()) {				
				SQLConnection conn = conRes.result();        
		        TransactionConnection.createTransactionConnection(conn, transConnRet->{
					if(transConnRet.succeeded()){
						TransactionConnection transConn = transConnRet.result();

				                Future<UpdateResult> addAcctPostfuture = Future.future();		
				                
				                addAcctPost(transConn, acctId, bizUnitId, d_is_global_bu, postId, authRoleId, auth_user_id, isOwner, entryId, addAcctPostfuture);
				                
				                addAcctPostfuture.setHandler(addAcctPostRet -> {
						            if (addAcctPostRet.succeeded()) {
				                    	transConn.commitAndClose(closedRet->{
				                    		future.complete(user);
				                    	});
						            }else{						            	
						            	Throwable err = addAcctPostRet.cause();
						                future.fail(err);
						                logger.error("AuthService 无法增加新用户.事务回滚" + err.getMessage());
										transConn.rollbackAndClose(closedRet->{												
											future.fail(err);
										});	
						            }
				                });
						
					}else{
						Throwable err = transConnRet.cause();
						String errMsg = err.getMessage();
						logger.error(errMsg, err);	
						conn.close(closedRet->{
							future.fail(err);
						});	
					}
		        });
			}else{
				Throwable err = conRes.cause();
            	future.fail(err);
                logger.error("连接打开失败。" + err.getMessage());
		    }
		});

    }
    
    
    public void create(JsonObject user, Long acctId, Boolean isOwner, Long bizUnitId, Boolean d_is_global_bu, Long postId, Long authRoleId, Future<JsonObject> future) {

        final String insertUserSQL = "INSERT INTO auth_user(" +
                "name, password, cell_no, email, status, entry_id, entry_datetime) " +
                "VALUES(?, ?, ?, ?, ?, ?, now())";
        JsonArray params = new JsonArray();
        //params.add(user.getOrgAcctId());
        params.add(user.getString("name"));
        params.add(user.getString("password"));
        params.add(user.getString("cell_no", ""));
        params.add(user.getString("email", ""));
        params.add("A"); //status
        Long entryId = user.getLong("entry_id", 0L);
        params.add(entryId); //设置该记录的创建人

		JDBCClient jdbcClient = this.getDataSource().getSqlClient();
		jdbcClient.getConnection(conRes -> {
			if (conRes.succeeded()) {				
				SQLConnection conn = conRes.result();        
		        TransactionConnection.createTransactionConnection(conn, transConnRet->{
					if(transConnRet.succeeded()){
						TransactionConnection transConn = transConnRet.result();
						
						Future<UpdateResult> innerFuture = Future.future();					
						
				        updateWithParams(transConn, insertUserSQL, params, innerFuture);
				        
				        innerFuture.setHandler(result -> {
				            if (result.succeeded()) {
				                UpdateResult updateResult = result.result();
				                int num = updateResult.getUpdated();
				                
				                Long userId = updateResult.getKeys().getLong(0);				                
				                user.put("id", userId);
				                
				                logger.info("更新了" + num + "条记录.");
				                logger.info(updateResult.toJson());		
				                
				                Future<UpdateResult> addAcctPostfuture = Future.future();		
				                
				                addAcctPost(transConn, acctId, bizUnitId, d_is_global_bu, postId, authRoleId, userId, isOwner, entryId, addAcctPostfuture);
				                
				                addAcctPostfuture.setHandler(addAcctPostRet -> {
						            if (addAcctPostRet.succeeded()) {
				                    	transConn.commitAndClose(closedRet->{
				                    		future.complete(user);
				                    	});
						            }else{						            	
						            	Throwable err = addAcctPostRet.cause();
						                future.fail(err);
						                logger.error("AuthService 无法增加新用户.事务回滚" + err.getMessage());
										transConn.rollbackAndClose(closedRet->{												
											future.fail(err);
										});	
						            }
				                });			                

				            } else {		
				            	Throwable err = result.cause();
				                future.fail(err);
				                logger.error("AuthService 无法增加新用户.事务回滚" + err.getMessage());
								transConn.rollbackAndClose(closedRet->{												
									future.fail(err);
								});	
				            }
				        });
						
						
						
					}else{
						Throwable err = transConnRet.cause();
						String errMsg = err.getMessage();
						logger.error(errMsg, err);	
						conn.close(closedRet->{
							future.fail(err);
						});	
					}
		        });
			}else{
				Throwable err = conRes.cause();
            	future.fail(err);
                logger.error("连接打开失败。" + err.getMessage());
		    }
		});

    }
    
    
    private void addAcctPost(TransactionConnection transConn, Long acctId, Long bizUnitId, Boolean d_is_global_bu, Long postId, Long authRoleId, Long userId, Boolean isOwner, Long operatorId, Future<UpdateResult> future) {
    	
        final String insertAcctSQL = "INSERT INTO acct_user(auth_user_id, acct_id, is_owner, status, entry_id, entry_datetime) " +
                "VALUES(?, ?, ?, 'A', ?, now())" +
        		"ON DUPLICATE KEY UPDATE auth_user_id=?,acct_id=?";
        JsonArray params = new JsonArray();
        params.add(userId);
        params.add(acctId);
        params.add(isOwner ? 1 : 0);
        params.add(operatorId);
        params.add(userId);
        params.add(acctId); 

        Future<UpdateResult> innerFuture = Future.future();

        updateWithParams(transConn, insertAcctSQL, params, innerFuture);
        
        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
                UpdateResult updateResult = result.result();
                int num = updateResult.getUpdated();
                logger.info("更新了" + num + "条记录.");
                logger.info(updateResult.toJson());

                final String insertPostSQL = "INSERT INTO acct_user_post(auth_user_id, acct_id, d_acct_biz_unit_id, d_is_global_bu, acct_biz_unit_post_id, d_auth_role_id, status, entry_id, entry_datetime) " +
                        "VALUES(?, ?, ?, ?, ?, ?, 'A', ?, now())";
                JsonArray params2 = new JsonArray();
                params2.add(userId);
                params2.add(acctId);                
                params2.add(bizUnitId);
                params2.add(d_is_global_bu);
                params2.add(postId);         
                params2.add(authRoleId);  
                params2.add(operatorId);

                Future<UpdateResult> innerFuture2 = Future.future();

                updateWithParams(transConn, insertPostSQL, params2, innerFuture2);
                
                innerFuture2.setHandler(result2 -> {
                    if (result2.succeeded()) {
                    	future.complete(result2.result());
                    }else{
                    	future.fail(result2.cause());
                        logger.warn("AuthService 无法增加新用户角色." + result2.cause().getMessage());
                    }}
                );
            } else {
                future.fail(result.cause());
                logger.warn("AuthService 无法增加新用户角色." + result.cause().getMessage());
            }
        });
    }
    
    
    /**
     * 带内部事务的添加岗位角色方法
     * @param acctId
     * @param bizUnitId
     * @param postId
     * @param userId
     * @param operatorId
     * @param future
     */
    public void addAcctPost(Long acctId, Long bizUnitId, Long postId, Long authRoleId, Long userId, Boolean isOwner, Long operatorId, Future<UpdateResult> future) {
    	
        final String insertAcctSQL = "INSERT INTO acct_user(auth_user_id, acct_id, is_owner, status, entry_id, entry_datetime) " +
                "VALUES(?, ?, ?, 'A', ?, now())" +
        		"ON DUPLICATE KEY UPDATE auth_user_id=?,acct_id=?";
        JsonArray params = new JsonArray();
        params.add(userId);
        params.add(acctId);
        params.add(isOwner ? 1 : 0);
        params.add(operatorId);
        params.add(userId);
        params.add(acctId); 
        
		JDBCClient jdbcClient = this.getDataSource().getSqlClient();
		jdbcClient.getConnection(conRes -> {
			if (conRes.succeeded()) {				
				SQLConnection conn = conRes.result();        
		        TransactionConnection.createTransactionConnection(conn, transConnRet->{
					if(transConnRet.succeeded()){
						TransactionConnection transConn = transConnRet.result();
						
						Future<UpdateResult> innerFuture = Future.future();					
						
				        updateWithParams(transConn, insertAcctSQL, params, innerFuture);
				        
				        innerFuture.setHandler(result -> {
				            if (result.succeeded()) {
				                UpdateResult updateResult = result.result();
				                int num = updateResult.getUpdated();
				                logger.info("更新了" + num + "条记录.");
				                logger.info(updateResult.toJson());

				                final String insertPostSQL = "INSERT INTO acct_user_post(auth_user_id, acct_id, d_acct_biz_unit_id, acct_biz_unit_post_id, d_auth_role_id, status, entry_id, entry_datetime) " +
				                        "VALUES(?, ?, ?, ?, ?, 'A', ?, now())";
				                JsonArray params2 = new JsonArray();
				                params2.add(userId);
				                params2.add(acctId);                
				                params2.add(bizUnitId);
				                params2.add(postId);         
				                params2.add(authRoleId);  
				                params2.add(operatorId);

				                Future<UpdateResult> innerFuture2 = Future.future();

				                updateWithParams(transConn, insertPostSQL, params2, innerFuture2);
				                
				                innerFuture2.setHandler(result2 -> {
				                    if (result2.succeeded()) {
				                    	transConn.commitAndClose(closedRet->{
				                    		future.complete(result2.result());
				                    	});
				                    }else{
						            	Throwable err = result2.cause();
						                future.fail(err);
						                logger.error("AuthService 无法增加新用户角色.事务回滚" + err.getMessage());
										transConn.rollbackAndClose(closedRet->{												
											future.fail(err);
										});
				                    }}
				                );
				            } else {		
				            	Throwable err = result.cause();
				                future.fail(err);
				                logger.error("AuthService 无法增加新用户角色.事务回滚" + err.getMessage());
								transConn.rollbackAndClose(closedRet->{												
									future.fail(err);
								});	
				            }
				        });
						
						
						
					}else{
						Throwable err = transConnRet.cause();
						String errMsg = err.getMessage();
						logger.error(errMsg, err);	
						conn.close(closedRet->{
							future.fail(err);
						});	
					}
		        });
			}else{
				Throwable err = conRes.cause();
            	future.fail(err);
                logger.error("连接打开失败。" + err.getMessage());
		    }
		});

    }



    public void update(Long userId, String name, String encryPwd, String cell_no, String email, Future<UpdateResult> future) {
        JsonArray params = new JsonArray();

        String setClause = "";
        if(name != null && !name.trim().isEmpty()){
        	setClause = "name=?";
        	params.add(name);
        }
        if(encryPwd != null && !encryPwd.trim().isEmpty()){
           	if(setClause.trim().isEmpty())
        		setClause = "password=?";
        	else
        		setClause += ",password=?";
        	params.add(encryPwd);
        }
        if(cell_no != null && !cell_no.trim().isEmpty()){
        	if(setClause.trim().isEmpty())
        		setClause = "cell_no=?";
        	else
        		setClause += ",cell_no=?";
        	params.add(cell_no);
        }
        if(email != null && !email.trim().isEmpty()){
        	if(setClause.trim().isEmpty())
        		setClause = "email=?";
        	else
        		setClause += ",email=?";
        	params.add(email);
        }

        final String updateSQL = "UPDATE auth_user"
                + " SET " + setClause
                + " WHERE id=?";
        params.add(userId);

        Future<UpdateResult> innerFuture = Future.future();

        updateWithParams(updateSQL, params, innerFuture);

        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
                UpdateResult updateResult = result.result();
                //System.out.println(updateResult.toJson());
                future.complete(updateResult);
            } else {
                future.fail(result.cause());
                logger.warn("用户信息无法更改。");
            }
        });

    }

    /**
     * 根据参数中的主键，更新有变化的属性字段。如果参数中的属性为空或值不变，则不更新响应字段。
     *
     * @param userId 用户ID，数据库表的主键.
     */
    public void setLoginDateTime(String userId, Future<UpdateResult> future) {
        String updateSQL = "UPDATE auth_user"
                + " SET last_login_datetime=now()"
                + " WHERE id=" + userId;
        updateWithParams(updateSQL, null, future);
    }

    /**
     * 不执行真正的删除操作。
     *
     * @param where  查询条件.
     * @param future 删除成功后回调.
     */
    public void deleteBy(JsonObject where, Future<UpdateResult> future) {
        String deleteSQL = "UPDATE auth_user"
                + " SET delete_id=0, delete_datetime=now()"
                + " WHERE 1=1";
        StringBuilder builder = new StringBuilder();
        builder.append(deleteSQL);

        JsonArray params = new JsonArray();

        where.forEach(member -> {
            String name = member.getKey();
            Object value = member.getValue();

            builder.append(" AND ").append(name);
            builder.append("=");
            builder.append("?");

            params.add(value instanceof Long ? Long.parseLong(value.toString()) : value);
        });

        Future<UpdateResult> innerFuture = Future.future();

        deleteWithParams(builder.toString(), params, innerFuture);

        innerFuture.setHandler(ret -> {
            if (ret.succeeded()) {
                UpdateResult result = ret.result();
                int num = result.getUpdated();

                future.complete(result);

                logger.info("删除了" + num + "条记录.");
            } else {
                future.fail(ret.cause());
                logger.warn("记录无法删除: " + ret.cause().getMessage());
            }

        });
    }

    /**
     * 不执行数据删除，仅标记记录为删除状态。
     *
     * @param userId 用户ID.
     * @param future 删除成功后回调.
     */
    public void deleteById(Long userId, Future<Boolean> future) {
        Future<UpdateResult> updateResultFuture = Future.future();

        deleteBy(new JsonObject().put("id", userId), updateResultFuture);

        updateResultFuture.setHandler(ret -> {
            if (ret.succeeded()) {
                future.complete(true);
            } else {
                future.fail(ret.cause());
                logger.warn(String.format("ID 为%d的用户表记录无法删除: %s", userId, ret.cause().getMessage()));
            }
        });

    }

    /**
     * 异步根据用户ID查找单条记录。
     *
     * @param id     用户ID.
     * @param future 返回User对象.
     */
    public void findById(int id, Future<ResultSet> future) {
        queryBy("auth_user", new String[]{"id", "name", "cell_no", "email"}, new
                JsonObject().put("id", id), future);

    }

 

    /**
     * 根据用户名和密码判断用户是否存在。
     *
     * @param userName 用户名
     * @param password 密码
     * @return User(id, org_acct_id, name)
     */
    public void findBy(String userName, String password, Future<ResultSet> future) {

        queryBy("auth_user", new String[]{"id", "name"},
                new JsonObject().put("name", userName).put("password", password),
                future);

    }

    /**
     * 根据手机号和密码查找用户信息.
     *
     * @param cellNo   手机号.
     * @param password 密码
     * @param future   如果无法找到用户,返回null; 如果找到用户, 则返回User实体对象.
     */
    public void findByCellNo(String cellNo, String password, Future<ResultSet> future) {

        queryBy("auth_user", new String[]{"id", "name"},
                new JsonObject().put("cell_no", cellNo).put("password", password),
                future);
    }
    
    public void isRegisteredCellNo(String cellNo, Future<Long> future) {
             
        final String sql = "SELECT id FROM auth_user WHERE cell_no=?";
        JsonArray params = new JsonArray();
        params.add(cellNo);

        Future<ResultSet> innerFuture = Future.future();

        this.queryWithParams(sql, params, innerFuture);

        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
            	ResultSet resultSet = result.result();
            	List<JsonObject> retDataArrays = resultSet.getRows();
            	if(retDataArrays != null && retDataArrays.size() > 0){
            		//Long num = retDataArrays.get(0).getLong("num");
            		Long id = retDataArrays.get(0).getLong("id");            		
            		future.complete(id);            		
            	}else{
            		future.complete(-1L);
            	}

            } else {
            	Throwable err = result.cause();								
                future.fail(err);                
            }
        });

	}
    
    public void isRegisteredUser(String userName, String cellNo, Future<Long> future) {
        
        final String sql = "SELECT id FROM auth_user WHERE name=? OR cell_no=?";
        JsonArray params = new JsonArray();
        params.add(userName);
        params.add(cellNo);

        Future<ResultSet> innerFuture = Future.future();

        this.queryWithParams(sql, params, innerFuture);

        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
            	ResultSet resultSet = result.result();
            	List<JsonObject> retDataArrays = resultSet.getRows();
            	if(retDataArrays != null && retDataArrays.size() > 0){
            		//Long num = retDataArrays.get(0).getLong("num");
            		Long id = retDataArrays.get(0).getLong("id");            		
            		future.complete(id);            		
            	}else{
            		future.complete(-1L);
            	}

            } else {
            	Throwable err = result.cause();								
                future.fail(err);                
            }
        });

	}
    
    public void existUserInAcct(Long auth_user_id, Long acct_id, Future<Boolean> future) {
        
        final String sql = "SELECT count(*) as num FROM acct_user WHERE acct_id=? AND auth_user_id=?";
        JsonArray params = new JsonArray();
        params.add(acct_id);
        params.add(auth_user_id);

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
    

    public void countUser(Long bizUnitId, Future<Integer> future) {
        
        final String sql = "SELECT count(id) as total_num FROM view_acct_user_post where d_acct_biz_unit_id=?";
        JsonArray params = new JsonArray();
        //params.add(acctId);
        params.add(bizUnitId);

        Future<ResultSet> innerFuture = Future.future();

        queryWithParams(sql, params, innerFuture);

        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
            	ResultSet resultSet = result.result();
            	List<JsonObject> retDataArrays = resultSet.getRows();
            	Integer totalNum = retDataArrays.get(0).getInteger("total_num");
            	future.complete(totalNum);
            } else {
            	Throwable err = result.cause();								
                future.fail(err);                
            }
        });

	}
    
    public void getUserListByPage(Long bizUnitId, JsonObject pagingOptions, Future<ResultSet> future) {
       
    	String sortField = pagingOptions.getString("sort_field");
    	Integer sortDirection = pagingOptions.getInteger("sort_direction");
    	String sortStr = (sortDirection==1)?"ASC":"DESC";    
    	
        int pageNo = pagingOptions.getInteger("page_number");
        int pageSize = pagingOptions.getInteger("page_size");
        int startIndex = (pageNo-1) * pageSize;
       
	   final String sql = "SELECT * FROM view_acct_user_post where d_acct_biz_unit_id=? order by " +
			   sortField + " " + sortStr + " limit ?,?";
	   JsonArray params = new JsonArray();
	   params.add(bizUnitId);
	   params.add(startIndex);
	   params.add(pageSize);
	
	   this.queryWithParams(sql, params, future);
    	
    }   
    
    public void getUserListForAcctByPage(Long acctId, JsonObject pagingOptions, Future<ResultSet> future) {
        
    	String sortField = pagingOptions.getString("sort_field");
    	Integer sortDirection = pagingOptions.getInteger("sort_direction");
    	String sortStr = (sortDirection==1)?"ASC":"DESC";    
    	
        int pageNo = pagingOptions.getInteger("page_number");
        int pageSize = pagingOptions.getInteger("page_size");
        int startIndex = (pageNo-1) * pageSize;
       
	   final String sql = "SELECT * FROM view_acct_user2 where acct_id=? order by " +
			   sortField + " " + sortStr + " limit ?,?";
	   JsonArray params = new JsonArray();
	   params.add(acctId);	   
	   params.add(startIndex);
	   params.add(pageSize);
	
	   this.queryWithParams(sql, params, future);
    	
    }   

    public void countUserForAcct(Long acctId, Future<Integer> future) {
        
        final String sql = "SELECT count(*) as total_num FROM view_acct_user2 where acct_id=?";
        JsonArray params = new JsonArray();
        params.add(acctId);

        Future<ResultSet> innerFuture = Future.future();

        queryWithParams(sql, params, innerFuture);

        innerFuture.setHandler(result -> {
            if (result.succeeded()) {
            	ResultSet resultSet = result.result();
            	List<JsonObject> retDataArrays = resultSet.getRows();
            	Integer totalNum = retDataArrays.get(0).getInteger("total_num");
            	future.complete(totalNum);
            } else {
            	Throwable err = result.cause();								
                future.fail(err);                
            }
        });

	}
    
    
    public void getUserByName(String userName, Future<ResultSet> future) {
        
	   final String sql = "SELECT id,name,cell_no,email,password FROM auth_user where name=? AND status='A'";
	   JsonArray params = new JsonArray();
	   params.add(userName);
	
	   this.queryWithParams(sql, params, future);
    	
    }
    
    public void getUserByCellNo(String cellNo, Future<ResultSet> future) {
        
	   final String sql = "SELECT id,name,cell_no,email,password FROM auth_user where cell_no=? AND status='A'";
	   JsonArray params = new JsonArray();
	   params.add(cellNo);
	
	   this.queryWithParams(sql, params, future);
    	
    }
    
    
    public void getUserAccts(Long userId, Future<ResultSet> future) {
       
	   final String sql = "SELECT * FROM view_acct_user where auth_user_id=?";
	   JsonArray params = new JsonArray();
	   params.add(userId);

	   this.queryWithParams(sql, params, future);
    	
    }
    
    public void getOwnerUserByAcctId(Long acctId, Future<ResultSet> future) {
        
	   final String sql = "SELECT id,name,password,cell_no,email FROM view_acct_owner where acct_id=?";
	   JsonArray params = new JsonArray();
	   params.add(acctId);
	
	   this.queryWithParams(sql, params, future);
    	
    }
    
    public void getUserPosts(Long acctId, Long userId, Future<ResultSet> future) {
        
	   final String sql = "SELECT unit_code,unit_name,post_code,post_name FROM view_acct_user_post2 where id=? and acct_id=?";
	   JsonArray params = new JsonArray();
	   params.add(userId);
	   params.add(acctId);
	
	   this.queryWithParams(sql, params, future);
    	
    } 
}
