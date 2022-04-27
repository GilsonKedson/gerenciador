package models.bean;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import interfaces.CRUDInterface;
import models.dao.MemberDao;

@ManagedBean
@SessionScoped
public class MemberBean implements Serializable, CRUDInterface<MemberBean>{
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private MemberDao memberDAO = new MemberDao();

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String create(MemberBean entity) {
		return memberDAO.create(entity);
	}

	@Override
	public String update(MemberBean entity) {
		return memberDAO.update(entity);
	}

	@Override
	public String delete(int id) {
		return memberDAO.delete(id);
	}

	@Override
	public String getRegister(int id) {
		return memberDAO.getRegister(id);
	}

	@Override
	public ArrayList<MemberBean> readRegisters() {
		return memberDAO.readRegisters();
	}

	@Override
	public String findRegister(MemberBean params) {
		return memberDAO.findRegister(params);
	}
	
	public String toString() {
		return this.getName();
	}

	public String back() {
		return "/pages/listar-membros.xhtml?faces-redirect=true";
	}
}
