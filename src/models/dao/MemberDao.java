package models.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.faces.context.FacesContext;

import interfaces.CRUDInterface;
import models.bean.MemberBean;
import postgresql.connection.ConnectionDB;

public class MemberDao implements CRUDInterface<MemberBean> {
	private Connection conn;
	private PreparedStatement stmt;
	private Statement state;
	private ResultSet result;
	
	@Override
	public String create(MemberBean entity) {
		try {
			conn = ConnectionDB.getConnection();
			PreparedStatement stmt = null;
			stmt = conn.prepareStatement("INSERT INTO members (name) VALUES (?)");
            stmt.setString(1, entity.getName());
            stmt.executeUpdate();
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		entity.setName("");
		
		return "/pages/listar-membros.xhtml?faces-redirect=true";
	}

	@Override
	public String update(MemberBean entity) {
		try {
			conn = ConnectionDB.getConnection();
			PreparedStatement stmt = null;
			stmt = conn.prepareStatement("UPDATE members SET name=? WHERE member_id=?");
			stmt.setString(1, entity.getName());
			stmt.setInt(2, entity.getId());
			stmt.executeUpdate();
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		return "/pages/listar-membros.xhtml?faces-redirect=true";
	}

	@Override
	public String delete(int id) {
		try {
			conn = ConnectionDB.getConnection();
			PreparedStatement stmt = null;
			stmt = conn.prepareStatement("DELETE FROM members WHERE member_id = " + id);
			stmt.executeUpdate();
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }

		return "/pages/listar-membros.xhtml?faces-redirect=true";
	}

	@Override
	public String getRegister(int id) {
		MemberBean editMember = null;
		Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
		try {
			conn = ConnectionDB.getConnection();
			state = conn.createStatement();
			result = state.executeQuery("SELECT * FROM members WHERE member_id = " + id);
		
			while(result.next()) {
				editMember = new MemberBean();
				editMember.setId(result.getInt("member_id"));
				editMember.setName(result.getString("name"));
			}
		
			sessionMapObj.put("editMember", editMember);
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		return "/pages/editar-membro.xhtml?faces-redirect=true";
	}

	@Override
	public ArrayList<MemberBean> readRegisters() {
		ArrayList<MemberBean> members = new ArrayList<MemberBean>();
		
		try {
			conn = ConnectionDB.getConnection();
			state = conn.createStatement();
			result = state.executeQuery("SELECT * FROM members ORDER BY member_id");
			
			while(result.next()){
				MemberBean member = new MemberBean();
				member.setId(result.getInt("member_id"));
				member.setName(result.getString("name"));
				
				members.add(member);
			}
					
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		return members;
	}

	@Override
	public String findRegister(MemberBean params) {
		ArrayList<MemberBean> members = new ArrayList<MemberBean>();
		
		try {
			conn = ConnectionDB.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM members WHERE name LIKE ?");
			stmt.setString(1, "%"+params.getName()+"%");
			result = stmt.executeQuery();
			
			while(result.next()){
				MemberBean member = new MemberBean();
				member.setId(result.getInt("member_id"));
				member.setName(result.getString("name"));
				
				members.add(member);
			}
					
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMapObj.put("resultMember", members);
		
		return "/pages/listar-resultados-membros.xhtml?faces-redirect=true";
	}
	
	public String getNameRegisters(int id) {
		MemberBean m = new MemberBean();
		try {
			conn = ConnectionDB.getConnection();
			state = conn.createStatement();
			result = state.executeQuery("SELECT * FROM members WHERE member_id = " + id);
		
			while(result.next()) {
				m.setId(result.getInt("member_id"));
				m.setName(result.getString("name"));
			}
			
        } catch (SQLException ex) {
        	System.out.println("Um erro foi identificado"+ ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
            ConnectionDB.closeConnection(conn, stmt);
        }
		
		return m.getName();
	}

}
