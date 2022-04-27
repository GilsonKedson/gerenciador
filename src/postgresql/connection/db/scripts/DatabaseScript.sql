CREATE DATABASE gerenciador-de-tarefas
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
    
CREATE TABLE members (
	member_id INT GENERATED ALWAYS AS IDENTITY,
	name VARCHAR ( 50 ) NOT NULL,
	
	PRIMARY KEY(member_id)
);

CREATE TABLE tasks (
	task_id INT GENERATED ALWAYS AS IDENTITY,
	member_id INT,
	title VARCHAR ( 50 ) UNIQUE NOT NULL,
	description TEXT,
	priority INT NOT NULL,
	deadline DATE NOT NULL,
	situation BOOLEAN NOT NULL,
	
	PRIMARY KEY(task_id),
	CONSTRAINT fk_member
      FOREIGN KEY(member_id) 
	  REFERENCES members(member_id)
	  ON DELETE SET NULL
);