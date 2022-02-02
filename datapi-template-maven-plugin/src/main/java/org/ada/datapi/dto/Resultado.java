package org.ada.datapi.dto;

import java.util.List;

/**
 * Representa el valor retorna por el servicio definido en el XML
 * 
 * @author jmgoyesc
 * 
 */
public class Resultado {

	private String nombre;
	private String tipo;
	private Integer orden;
	private List<Elemento> elementos;
	private String encriptado;

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
		String str = "Resultado(nombre=" + nombre + " | tipo=" + tipo + ")";
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

	public String getEncriptado() {
		return encriptado;
	}

	public void setEncriptado(String encriptado) {
		this.encriptado = encriptado;
	}
	
}
