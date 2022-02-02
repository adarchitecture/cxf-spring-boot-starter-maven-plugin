package org.ada.datapi.dto;

import java.util.ArrayList;
import java.util.List;

public class Operacion {

	private List<Parametro> parametros = new ArrayList<Parametro>();
	private Consulta consulta;
	private String procedimiento;
	private List<Resultado> resultados = new ArrayList<Resultado>();
	private String nombre;
	private ConsultaArchivo consultaArchivo;
	
	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public String getProcedimiento() {
		return procedimiento;
	}

	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}

	public List<Resultado> getResultados() {
		return resultados;
	}

	public void setResultados(List<Resultado> resultados) {
		this.resultados = resultados;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public String toString() {
		String str = "Operacion(nombre=" + nombre + " | parametros=" + parametros + " | resultados=" + resultados;
		if (consulta != null) {
			str += " | consulta=" + consulta;
		}
		if (procedimiento != null) {
			str += " | procedimiento=" + procedimiento;
		}
		str += ")";
		return str;
	}

	public ConsultaArchivo getConsultaArchivo() {
		return consultaArchivo;
	}

	public void setConsultaArchivo(ConsultaArchivo consultaArchivo) {
		this.consultaArchivo = consultaArchivo;
	}

}
