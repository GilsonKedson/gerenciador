package models.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import interfaces.CRUDInterface;
import models.dao.TaskDao;

@ManagedBean
@SessionScoped
public class TaskBean implements Serializable, CRUDInterface<TaskBean> {
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private String description;
	private int responsible;
	private String responsibleName;
	private int priority;
	private Date deadline;
	private boolean situation;
	TaskDao taskDAO = new TaskDao();


	public String getResponsibleName() {
		return responsibleName;
	}
	
	public void setResponsibleName(String responsible) {
		this.responsibleName = responsible;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getResponsible() {
		return responsible;
	}

	public void setResponsible(int responsible) {
		this.responsible = responsible;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public boolean isSituation() {
		return situation;
	}

	public void setSituation(boolean situation) {
		this.situation = situation;
	}

	@Override
	public String create(TaskBean entity) {
		return taskDAO.create(entity);
	}
	
	@Override
	public String update(TaskBean entity) {
		return taskDAO.update(entity);
	}
	
	@Override
	public String delete(int id) {
		return taskDAO.delete(id);
	}
	
	@Override
	public String getRegister(int id) {
		return taskDAO.getRegister(id);
	}
	
	@Override
	public ArrayList<TaskBean> readRegisters() {
		return taskDAO.readRegisters();
	}
	
	@Override
	public String findRegister(TaskBean params) {
		return taskDAO.findRegister(params);
	}
	
	public String back() {
		return "/pages/listar-tarefas.xhtml?faces-redirect=true";
	}
}
