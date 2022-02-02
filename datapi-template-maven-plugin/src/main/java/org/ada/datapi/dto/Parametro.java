package org.ada.datapi.dto;

import java.util.List;

public class Parametro {

	public static final String TIPO_STRING = "string";
	public static final String TIPO_INT = "int";
	public static final String TIPO_BOOLEAN = "boolean";
	public static final String TIPO_DATE = "date";
	public static final String TIPO_FLOAT = "float";
	public static final String TIPO_DOUBLE = "double";
	public static final String TIPO_ARRAY = "array";
	public static final String TIPO_OBJECT = "object";
	public static final String TIPO_SUBQUERY = "subquery";
	public static final String TIPO_BLOB = "blob";
	public static final String TIPO_CLOB = "clob";
	public static final String TIPO_LONG = "long";

	private String nombre;
	private String tipo;
	private Integer orden;
	private List<Elemento> elementos;
	private String id;
	private String encriptado;
	private Boolean obligatorio = true;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String toString() {
		String str = "Parametro(nombre=" + nombre + " | tipo=" + tipo + ")";
		return str;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public List<Elemento> getElementos() {
		return elementos;
	}

	public void setElementos(List<Elemento> elementos) {
		this.elementos = elementos;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEncriptado() {
		return encriptado;
	}

	public void setEncriptado(String encriptado) {
		this.encriptado = encriptado;
	}

	public Boolean getObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(Boolean obligatorio) {
		this.obligatorio = obligatorio;
		if(obligatorio == null){
			this.obligatorio = true;
		}
	}
	
}
