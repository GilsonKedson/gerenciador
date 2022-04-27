package interfaces;

import java.util.ArrayList;

public interface CRUDInterface<E> {
	String create(E entity);
	String update(E entity);
	String delete(int id);
	String getRegister(int id);
	ArrayList<E> readRegisters();
	String findRegister(E params);
}
