package org.ada.datapi.dto;

public class ConsultaArchivo {

	private String archivo;

	private String separador = ",";

	private String longitudes;

	private Boolean encabezado;

	private String consulta;

	public ConsultaArchivo() {
		
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String file) {
		this.archivo = file;
	}

	public String getSeparador() {
		return separador;
	}

	public void setSeparador(String separador) {
		this.separador = separador;
	}

	public String getLongitudes() {
		return longitudes;
	}

	public void setLongitudes(String tamano) {
		this.longitudes = tamano;
	}

	public Boolean getEncabezado() {
		return encabezado;
	}

	public void setEncabezado(Boolean encabezado) {
		this.encabezado = encabezado;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

}
