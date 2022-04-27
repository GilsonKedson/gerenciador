package models.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.faces.context.FacesContext;

import interfaces.CRUDInterface;
import models.bean.TaskBean;
import postgresql.connection.ConnectionDB;

public class TaskDao implements CRUDInterface<TaskBean> {
	private Connection conn;
	private PreparedStatement stmt;
	private Statement state;
	private ResultSet result;
	
	@Override
	public String create(TaskBean entity) {
		try {
			conn = ConnectionDB.getConnection();
			PreparedStatement stmt = null;
			stmt = conn.prepareStatement("INSERT INTO tasks "
					+ "(title, description, member_id, priority, deadline, situation) "
					+ "VALUES (?, ?, ?, ?, ?, ?)");
			
			stmt.setString(1, entity.getTitle());
			stmt.setString(2, entity.getDescription());
			stmt.setInt(3, entity.getResponsible());
			stmt.setInt(4, entity.getPriority());
			stmt.setDate(5, new Date(entity.getDeadline().getTime()));
			stmt.setBoolean(6, entity.isSituation());
            
			stmt.executeUpdate();
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		return "/pages/listar-tarefas.xhtml?faces-redirect=true";
	}

	@Override
	public String update(TaskBean entity) {
		try {
			conn = ConnectionDB.getConnection();
			PreparedStatement stmt = null;
			stmt = conn.prepareStatement("UPDATE tasks SET "
					+ "title=?, "
					+ "description=?, "
					+ "member_id=?,"
					+ "priority=?,"
					+ "deadline=?,"
					+ "situation=?"
					+ "WHERE task_id=?"
			);
			
			stmt.setString(1, entity.getTitle());
			stmt.setString(2, entity.getDescription());
			stmt.setInt(3, entity.getResponsible());
			stmt.setInt(4, entity.getPriority());
			stmt.setDate(5, new Date(entity.getDeadline().getTime()));
			stmt.setBoolean(6, entity.isSituation());
			stmt.setInt(7, entity.getId());
			
			stmt.executeUpdate();
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		return "/pages/listar-tarefas.xhtml?faces-redirect=true";
	}

	@Override
	public String delete(int id) {
		try {
			conn = ConnectionDB.getConnection();
			PreparedStatement stmt = null;
			stmt = conn.prepareStatement("DELETE FROM tasks WHERE task_id ="+ id);
			stmt.executeUpdate();
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		return "/pages/listar-tarefas.xhtml?faces-redirect=true";
	}

	@Override
	public String getRegister(int id) {
		TaskBean editTask = null;
		Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
		try {
			conn = ConnectionDB.getConnection();
			state = conn.createStatement();
			result = state.executeQuery("SELECT * FROM tasks WHERE task_id = " + id);
			MemberDao m = new MemberDao();
			
			while(result.next()){
				editTask = new TaskBean();
				editTask.setId(result.getInt("task_id"));
				editTask.setTitle(result.getString("title"));
				editTask.setDescription(result.getString("description"));
				editTask.setPriority(result.getInt("priority"));
				editTask.setDeadline(result.getDate("deadline"));
				editTask.setSituation(result.getBoolean("situation"));
				editTask.setResponsible(result.getInt("member_id"));
				
				if(result.getInt("member_id") == 0) {
					editTask.setResponsibleName("Sem responsável");
				}else {
					editTask.setResponsibleName(m.getNameRegisters(result.getInt("member_id")));
				}
			}

			sessionMapObj.put("editTask", editTask);
			
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		return "/pages/editar-tarefa.xhtml?faces-redirect=true";
	}

	@Override
	public ArrayList<TaskBean> readRegisters() {
		ArrayList<TaskBean> tasks = new ArrayList<TaskBean>();
		MemberDao m = new MemberDao();
		
		try {
			conn = ConnectionDB.getConnection();
			state = conn.createStatement();
			result = state.executeQuery("SELECT * FROM tasks");
			
			while(result.next()){
				TaskBean task = new TaskBean();
				task.setId(result.getInt("task_id"));
				task.setTitle(result.getString("title"));
				task.setSituation(result.getBoolean("situation"));
				task.setResponsible(result.getInt("member_id"));
				
				if(result.getInt("member_id") == 0) {
					task.setResponsibleName("Sem responsável");
				}else {
					task.setResponsibleName(m.getNameRegisters(result.getInt("member_id")));
				}
				
				tasks.add(task);
			}
					
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		return tasks;
	}

	@Override
	public String findRegister(TaskBean params) {
		ArrayList<TaskBean> tasks = new ArrayList<TaskBean>();
		MemberDao m = new MemberDao();
		
		String sql = "SELECT * FROM tasks WHERE ";
		String query;
		
		// SELECT DE MILHÕES, SQN
		if(params.getId() != 0 && params.getResponsible() != 0 && !params.getTitle().isEmpty() && !params.getDescription().isEmpty()) {
			sql+= "task_id="+params.getId()+" AND ";
			sql+= "member_id="+params.getResponsible()+" AND ";
			sql+= "situation="+params.isSituation()+" AND ";
			sql+= "title LIKE '%"+params.getTitle()+"%' AND ";
			sql+= "description LIKE '%"+params.getDescription()+"%'";
		}
		
		else {
			if(params.getId() != 0) {
				sql+= "task_id="+params.getId()+" AND ";
			}
			
			if(params.getResponsible() != 0) {
				sql+= "member_id="+params.getResponsible()+" AND ";
			}
			
			if(!params.getTitle().isEmpty()) {
				sql+= "title LIKE '%"+params.getTitle()+"%' AND ";
			}
			
			if(!params.getDescription().isEmpty()) {
				sql+= "description LIKE '%"+params.getDescription()+"%' AND ";
			}
			
			sql+= "situation="+params.isSituation();
		}
		
		try {
			conn = ConnectionDB.getConnection();
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			while(result.next()){
				TaskBean task = new TaskBean();
				task.setId(result.getInt("task_id"));
				task.setTitle(result.getString("title"));
				task.setDescription(result.getString("description"));
				task.setSituation(result.getBoolean("situation"));
				task.setResponsible(result.getInt("member_id"));
				
				if(result.getInt("member_id") == 0) {
					task.setResponsibleName("Sem responsável");
				}else {
					task.setResponsibleName(m.getNameRegisters(result.getInt("member_id")));
				}
				
				tasks.add(task);
			}
					
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMapObj.put("resultTasks", tasks);
		
		return "/pages/listar-resultados-tarefas.xhtml?faces-redirect=true";
	}
}
