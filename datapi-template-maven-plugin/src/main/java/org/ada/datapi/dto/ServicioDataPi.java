package org.ada.datapi.dto;

import java.util.List;

import org.ada.datapi.utils.StringUtils;



/**
 * Objeto que representa el XML del servicio que se encuetra en la carpeta
 * servicios/
 * 
 * @author jmgoyesc
 * 
 */
public class ServicioDataPi {

	private Conexion conexion;
	private String nombre;
	private List<Operacion> operaciones;
	private String file;
	private String prefijo;

	public String getPrefijo() {
		if (prefijo == null) {
			prefijo = "true";
		}

		return (StringUtils.esVerdad(prefijo) ? "true" : "false");
	}

	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Operacion> getOperaciones() {
		return operaciones;
	}

	public void setOperaciones(List<Operacion> operaciones) {
		this.operaciones = operaciones;
	}

	
	public String toString() {
		String str = "ServicioDataPi(nombre=" + nombre + " | conexion=" + conexion + " | operaciones(" + operaciones + ") )";
		return str;
	}

	public Conexion getConexion() {
		return conexion;
	}

	public void setConexion(Conexion conexion) {
		this.conexion = conexion;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
