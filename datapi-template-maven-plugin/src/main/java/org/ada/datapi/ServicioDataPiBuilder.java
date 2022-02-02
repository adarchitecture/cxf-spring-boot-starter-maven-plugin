/*
 * MIT License 
 * 
 * Copyright (c) 2018 Ownk
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 */

package org.ada.datapi;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;

import org.ada.datapi.dto.ServicioDataPi;
import org.ada.datapi.utils.FileUtils;
import org.ada.datapi.utils.JavaToXML;

/**
 *
 * <h1>ServicioDataPiBuilder</h1>
 *
 * Description
 *
 * @author THEOVERLORDKOTAN (ADA) 
 * @version 1.0
 * 
 */
public class ServicioDataPiBuilder {
	
    /** Instance logger */
    private  Log log;
    private  String LOG_PREFIX;
    private  List<File> globalpaths = null;
	/**
	 * Guarda el listado de servicios configurados en la aplicacion (con *.conf).
	 */
	private Map<String, ServicioDataPi> listadoServicios;
	private Map<String, ServicioDataPi> listadoRecursosDataPI;
    
	private static ServicioDataPiBuilder servicioDataPiBuilder;

	private ServicioDataPiBuilder() {
		listadoServicios = new HashMap<String, ServicioDataPi>();
		listadoRecursosDataPI = new HashMap<String, ServicioDataPi>();
	}

	public static ServicioDataPiBuilder getInstance() {
		if (servicioDataPiBuilder == null) {
			servicioDataPiBuilder = new ServicioDataPiBuilder();
			
		}
		return servicioDataPiBuilder;
	}

	public static void loadServices(List<File> paths) {
		ServicioDataPiBuilder.getInstance().globalpaths = paths;
		loadServices();
	}

	public static void loadServices() {
		Map<String, ServicioDataPi> listadoServicios = new HashMap<String, ServicioDataPi>();

		logWithPrefix("Cargado servicios datapi...");

		for (File archivo : ServicioDataPiBuilder.getInstance().globalpaths) {

			logWithPrefix("Datapi --> Path: " + archivo.getAbsolutePath());

			if (archivo != null) {
				if (archivo.isFile() && archivo.getName().endsWith(".conf")) {

					logWithPrefix(" Datapi --> File: " + archivo.getName());

					try {
						ServicioDataPi servicioDataPi = ServicioDataPiBuilder.crearServicio(archivo);
						listadoServicios.put(servicioDataPi.getNombre(), servicioDataPi);
					} catch (Exception e) {
						logWithPrefixError("(" + ServicioDataPiBuilder.class + "). error al cargar el servicio (" + archivo.getName() + "). Excepcion=" + e.getMessage(),e);
					}
				}
			}
		}

		ServicioDataPiBuilder.getInstance().setListadoServicios(listadoServicios);

	}

	public static ServicioDataPi crearServicio(File archivo) throws Exception {

		String[] contenido = FileUtils.getContentFileToArray(archivo.getAbsolutePath());

		if (contenido == null || contenido.length == 0) {
			return null;
		}

		Map<String, String> mapRequest = new HashMap<String, String>();

		for (String linea : contenido) {

			linea = linea.replace('\t', ' ').trim();

			if (linea.length() > 0 && linea.charAt(0) != '#') {
				int a = linea.indexOf(" ");

				String param = "ServicioDataPi." + linea.substring(0, a).trim();
				String valor = linea.substring(a).trim();

				mapRequest.put(param, valor);
			}
		}

		String nombreparametro = "ServicioDataPi";
		String classname = "org.ada.datapi.dto.ServicioDataPi";
		return (ServicioDataPi) JavaToXML.createObject(classname, Class.forName(classname), nombreparametro, mapRequest);

	}

	// #########################################################

	// ----------------------------------------------------

	public  void startUp() {
		try {

			logWithPrefix("Recargando Servicios Datapi.");
			ServicioDataPiBuilder.loadServices(ServicioDataPiBuilder.getInstance().globalpaths);

		} catch (Exception e) {
			logWithPrefixError("Fallo ela recarga de servicios Datapi de la aplicacion. ", e);
		}
	}

    private static  void logWithPrefix(String logMessage) {
    	ServicioDataPiBuilder.getInstance().log.info(ServicioDataPiBuilder.getInstance().LOG_PREFIX + logMessage);
    }
    private static  void logWithPrefixError(String logMessage, Exception e) {
    	ServicioDataPiBuilder.getInstance().log.info(ServicioDataPiBuilder.getInstance().LOG_PREFIX + logMessage,e);
    }
    
	// -------------------------------------------------------------

	public Map<String, ServicioDataPi> getListadoServicios() {
		return listadoServicios;
	}

	public void setListadoServicios(Map<String, ServicioDataPi> listadoServicios) {
		this.listadoServicios = listadoServicios;
	}


	public Map<String, ServicioDataPi> getListadoRecursosDataPI() {
		return listadoRecursosDataPI;
	}

	public void setListadoRecursosDataPI(Map<String, ServicioDataPi> listadoRecursosDataPI) {
		this.listadoRecursosDataPI = listadoRecursosDataPI;
	}

	public static ServicioDataPiBuilder getServicioDataPiBuilder() {
		return servicioDataPiBuilder;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public void setLOG_PREFIX(String lOG_PREFIX) {
		LOG_PREFIX = lOG_PREFIX;
	}

	public void setGlobalpaths(List<File> globalpaths) {
		this.globalpaths = globalpaths;
	}
	

}
