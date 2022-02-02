package org.ada.datapi.dto;

import java.util.List;

public class Consulta {

	private String nombre;
	private String sql;
	private List<Consulta> subconsulta;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Consulta> getSubconsulta() {
		return subconsulta;
	}

	public void setSubconsulta(List<Consulta> subconsulta) {
		this.subconsulta = subconsulta;
	}

}
