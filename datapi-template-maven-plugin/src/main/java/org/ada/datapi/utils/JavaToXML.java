package org.ada.datapi.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaToXML {

	// #region 01 logger declaration ---------------------
	private static final Logger logger = LoggerFactory.getLogger(JavaToXML.class);
	// #endregion 01 -------------------------------------

	/**
	 * Formato de fecha predefinido
	 */
	private static String date_format = "dd/MM/yyyy HH:mm:ss";

	private JavaToXML() {
	}

	private static Set<String> metodosInvalidos;

	static {
		metodosInvalidos = new HashSet<String>();
		metodosInvalidos.add("getSerializer");
		metodosInvalidos.add("getDeserializer");
		metodosInvalidos.add("getTypeDesc");
		metodosInvalidos.add("getClass");

	}


	// -----------------------------------------------------

	public static StringBuffer exe(String name, Object obj) {
		return exe(name, obj, false, null, null, null);
	}

	// -----------------------------------------------------

	public static StringBuffer exe(String name, Object obj, boolean isname) {
		return exe(name, obj, isname, null, null, null);
	}

	// -----------------------------------------------------

	@SuppressWarnings({"unchecked","rawtypes"})
	public static StringBuffer exe(String name, Object obj, boolean isname, String prefix, String uri, Boolean one_prefix) {

		String strprefix = (prefix == null || prefix.trim().length() == 0) ? "" : (prefix.trim() + ":");
		String struri = (uri == null || uri.trim().length() == 0) ? "" : " xmlns:" + prefix + "=\"" + uri + "\"";

		if ((!"#RESPUESTA".equals(name)) && one_prefix != null && one_prefix) {
			prefix = null;
			one_prefix = null;
			uri = null;
		}

		if (obj == null) {
			if (isname || name == null) {
				return new StringBuffer("");
			} else {
				return new StringBuffer("<" + strprefix + ajustarNombre(name) + "/>");
			}
		}

		// --------------------------------------------

		String cname = obj.getClass().getName();
		Class[] interfaces = obj.getClass().getInterfaces();

		boolean viewname = name != null && name.charAt(0) != '#';

		// --------------------------------------------

		StringBuffer ret = new StringBuffer();

		if (viewname) {
			if (isname) {
				ret.append("<" + strprefix + ajustarNombre(name) + struri + " name=\"" + simplevalidateName(name) + "\">");
			} else {

				StringBuffer params = new StringBuffer("");
				for (Class class1 : interfaces) {
					if (class1.getName().equals("java.util.Map")) {
						Map mapa = (Map) obj;

						Set<Entry<Object, Object>> entrySet = mapa.entrySet();

						for (Iterator<Entry<Object, Object>> iterator = entrySet.iterator(); iterator.hasNext();) {
							Entry<Object, Object> entry = iterator.next();
							Object kk = entry.getKey();
							Object value = entry.getValue();

							if (kk != null && kk.toString().charAt(0) == '@') {
								params.append(" " + ajustarNombre(kk.toString().substring(1)) + "=\"" + simplevalidateName(value) + "\"");
							}

						}

					}
				}

				ret.append("<" + strprefix + ajustarNombre(name) + struri + params + ">");
			}
		}

		// --------------------------------------------
		// basicos

		if (cname.equals("java.lang.String") || cname.equals("java.lang.Integer") || cname.equals("java.lang.Float") || cname.equals("java.lang.Double") || cname.equals("java.lang.Long") || cname.equals("java.lang.Boolean") || cname.equals("java.math.BigDecimal") || cname.equals("java.math.BigInteger") || cname.equals("java.lang.Character")) {

			if (isname) {
				return new StringBuffer("<" + strprefix + ajustarNombre(name) + struri + " name=\"" + simplevalidateName(name) + "\">" + StringUtils.escapeXML(StringUtils.toString(obj)) + "</" + strprefix + ajustarNombre(name) + ">");
			} else {
				return new StringBuffer("<" + strprefix + ajustarNombre(name) + struri + ">" + StringUtils.escapeXML(StringUtils.toString(obj)) + "</" + strprefix + ajustarNombre(name) + ">");
			}

		} else if (obj != null && obj instanceof java.util.Date) {

			if (isname) {
				return new StringBuffer("<" + strprefix + ajustarNombre(name) + struri + " format=\"" + date_format + "\" name=\"" + simplevalidateName(name) + "\" time=\"" + ((Date) obj).getTime() + "\">" + new SimpleDateFormat(date_format).format((Date) obj) + "</" + strprefix + ajustarNombre(name) + ">");
			} else {
				return new StringBuffer("<" + strprefix + ajustarNombre(name) + struri + " format=\"" + date_format + "\" time=\"" + ((Date) obj).getTime() + "\">" + new SimpleDateFormat(date_format).format((Date) obj) + "</" + strprefix + ajustarNombre(name) + ">");
			}

		} else if (obj != null && obj instanceof java.util.Calendar) {

			if (isname) {
				return new StringBuffer("<" + strprefix + ajustarNombre(name) + struri + " format=\"" + date_format + "\" name=\"" + simplevalidateName(name) + "\" time=\"" + ((Calendar) obj).getTime() + "\">" + new SimpleDateFormat(date_format).format(((Calendar) obj).getTime()) + "</" + strprefix + ajustarNombre(name) + ">");
			} else {
				return new StringBuffer("<" + strprefix + ajustarNombre(name) + struri + " format=\"" + date_format + "\" time=\"" + ((Calendar) obj).getTime() + "\">" + new SimpleDateFormat(date_format).format(((Calendar) obj).getTime()) + "</" + strprefix + ajustarNombre(name) + ">");
			}

		} else if (obj != null && obj instanceof java.io.File) {

			if (isname) {
				return new StringBuffer("<" + strprefix + ajustarNombre(name) + struri + " name=\"" + simplevalidateName(name) + "\">" + StringUtils.escapeXML(((java.io.File) obj).getAbsolutePath()) + "</" + strprefix + ajustarNombre(name) + ">");
			} else {
				return new StringBuffer("<" + strprefix + ajustarNombre(name) + struri + ">" + StringUtils.escapeXML(((java.io.File) obj).getAbsolutePath()) + "</" + strprefix + ajustarNombre(name) + ">");
			}

		}// --------------------------------------------
		else /*-- Si es un conjunto */ if(obj.getClass().isArray()){
			
			Object[] aa = (Object[]) obj;

			for (Object eleemnto : aa) {
				ret.append(exe(viewname ? eleemnto.getClass().getSimpleName() : name.substring(1), eleemnto, isname, prefix, uri, one_prefix));
			}
		} else  /*-- Si es una lista */ if (obj instanceof java.util.List) {
			List lista = (List) obj;
			for (Object object : lista) {
				if (object != null) {
					ret.append(exe(viewname ? object.getClass().getSimpleName() : name.substring(1), object, isname, prefix, uri, one_prefix));
				}
			}
		} else /*-- Si es un conjunto */ if (obj instanceof java.util.Set) {
			Set lista = (Set) obj;
			for (Object object : lista) {
				if (object != null) {
					ret.append(exe(viewname ? object.getClass().getSimpleName() : name.substring(1), object, isname, prefix, uri, one_prefix));
				}
			}
		} else /* -- Si es un mapa*/ if (obj instanceof java.util.Map) {
			Map mapa = (Map) obj;

			Set<Entry<Object, Object>> entrySet = mapa.entrySet();

			for (Iterator<Entry<Object, Object>> iterator = entrySet.iterator(); iterator.hasNext();) {
				Entry<Object, Object> entry = iterator.next();
				Object kk = entry.getKey();
				Object value = entry.getValue();

				if (isname || (kk != null && kk.toString().charAt(0) != '@')) {
					ret.append(exe(kk.toString(), value, isname, prefix, uri, one_prefix));
				}
			}

		} else /*--Si no es ninguno de los anteriores*/{
			
			// --------------------------------------------
			boolean hasGetters = false;
			Method[] mm = obj.getClass().getMethods();
	
			for (Method method : mm) {
	
				if (method.getName().indexOf("get") == 0 && method.getName().length() > 3 && !metodosInvalidos.contains(method.getName())) {
	
					String namemethod = ajustarNombre(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
					try {
						ret.append(exe(namemethod, method.invoke(obj, new Object[0]), isname, prefix, uri, one_prefix));
						hasGetters = true;
					} catch (Exception e) {
					}
				}
	
				if (method.getName().indexOf("is") == 0 && method.getReturnType().getSimpleName().equals("Boolean")) {
					String namemethod = ajustarNombre(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
					ret.append("<" + strprefix + namemethod + struri + ">");
					try {
						ret.append(method.invoke(obj, new Object[0]));
						hasGetters = true;
					} catch (Exception e) {
					}
					ret.append("</" + strprefix + namemethod + ">");
				}
			}
			//-- Si no es ninguno de los anteriores ni tiene getters
			if( ! hasGetters ){
				ret.append(StringUtils.escapeXML(StringUtils.toString(obj)));
			}
		}

		if (viewname) {
			ret.append("</" + strprefix + ajustarNombre(name) + ">");
		}

		return ret;
	}

	// -----------------------------------------------------

	
	// -----------------------------------------------------

	// -----------------------------------------------------

	@SuppressWarnings({"unchecked","rawtypes"})
	public static Object createObject(String classname, Class<?> tipo, String nombreparametro, Map<String, String> mapRequest) {
		Object ret = null;

		if (nombreparametro != null && nombreparametro.trim().length() > 0) {

			nombreparametro = nombreparametro.trim();
			String param = mapRequest.get(nombreparametro);

			if (param != null && param.length() > 0) {

				try {
					// --
					tipo = ClassUtils.primitiveToWrapper(tipo);
					if (tipo.getName().equals("java.lang.String")) {
						ret = param;
					} else
					// --
					if (tipo.getName().equals("java.lang.Character")) {
						ret = param.charAt(0);
					} else
					// --
					if (tipo.getName().equals("java.lang.Integer")) {
						ret = Integer.parseInt(param);
					} else
					// --
					if (tipo.getName().equals("java.util.Date")) {
						try {
							ret = new Date(Long.parseLong(param));
						} catch (Exception e) {
							ret = new SimpleDateFormat("dd/MM/yyyy").parse(param);
						}
					} else
					// --
					if (tipo.getName().equals("java.lang.Float")) {
						ret = Float.parseFloat(param);
					} else
					// --
					if (tipo.getName().equals("java.lang.Long")) {
						ret = Long.parseLong(param);
					} else
					// --
					if (tipo.getName().equals("java.lang.Double")) {
						ret = Double.parseDouble(param);
					} else
					// --
					if (tipo.getName().equals("java.lang.Boolean")) {
						ret = Boolean.parseBoolean(param);
					} else
					// --
					if (tipo.getName().equals("java.math.BigDecimal")) {
						ret = new BigDecimal(param);
					}
				} catch (Exception e) {
					ret = null;
				}
			}

			if (ret == null && tipo.getName().equals("java.util.List")) {

				String sname = classname;
				if (sname.indexOf(">") > 0) {
					sname = sname.substring(sname.indexOf("<") + 1);
					sname = sname.substring(0, sname.indexOf(">"));
				}

				try {
					Class<?> ntipo = Class.forName(sname);

					Set<String> sset = mapRequest.keySet();

					Set<String> nnset = new HashSet<String>();
					for (String bpar : sset) {
						if (bpar.indexOf(nombreparametro + ":") == 0) {
							// verifico si tengo que crearlo
							if (ret == null) {
								ret = new ArrayList();
							}
							String d = bpar.substring((nombreparametro + ":").length());
							if (d.indexOf(".") > 0) {
								d = d.substring(0, d.indexOf("."));
							}
							if (d.indexOf(":") > 0) {
								d = d.substring(0, d.indexOf(":"));
							}
							nnset.add(nombreparametro + ":" + d);
						}
					}

					if (ret != null) {
						for (String clname : nnset) {
							List aret = (List) ret;
							aret.add(createObject(sname, ntipo, clname, mapRequest));
						}
					}

				} catch (Exception e) {
				}

			}

			if (ret == null && tipo.getName().startsWith("[")) {

				String sname = classname.substring(0, classname.length() - 2);
				if (sname.indexOf(">") > 0) {
					sname = sname.substring(sname.indexOf("<") + 1);
					sname = sname.substring(0, sname.indexOf(">"));
				}

				try {
					Class<?> ntipo = Class.forName(sname);

					Set<String> sset = mapRequest.keySet();

					Set<String> nnset = new HashSet<String>();
					for (String bpar : sset) {
						if (bpar.indexOf(nombreparametro + ":") == 0) {
							// verifico si tengo que crearlo
							if (ret == null) {
								ret = new ArrayList();
							}
							String d = bpar.substring((nombreparametro + ":").length());
							if (d.indexOf(".") > 0) {
								d = d.substring(0, d.indexOf("."));
							}
							if (d.indexOf(":") > 0) {
								d = d.substring(0, d.indexOf(":"));
							}
							nnset.add(nombreparametro + ":" + d);
						}
					}

					if (ret != null) {
						for (String clname : nnset) {
							List aret = (List) ret;
							aret.add(createObject(sname, ntipo, clname, mapRequest));
						}
					}
					if (ret != null) {
						ret = ((List) ret).toArray();
					}

				} catch (Exception e) {
				}

			}

			if (ret == null && tipo.getName().equals("java.util.Set")) {

				String sname = classname;
				sname = sname.substring(sname.indexOf("<") + 1);
				sname = sname.substring(0, sname.indexOf(">"));

				try {
					Class<?> ntipo = Class.forName(sname);

					Set<String> sset = mapRequest.keySet();

					Set<String> nnset = new HashSet<String>();
					for (String bpar : sset) {
						if (bpar.indexOf(nombreparametro + ":") == 0) {
							// verifico si tengo que crearlo
							if (ret == null) {
								ret = new HashSet();
							}
							String d = bpar.substring((nombreparametro + ":").length());
							if (d.indexOf(".") > 0) {
								d = d.substring(0, d.indexOf("."));
							}
							if (d.indexOf(":") > 0) {
								d = d.substring(0, d.indexOf(":"));
							}
							nnset.add(nombreparametro + ":" + d);
						}
					}

					if (ret != null) {
						for (String clname : nnset) {
							Set aret = (Set) ret;
							aret.add(createObject(sname, ntipo, clname, mapRequest));
						}
					}

				} catch (Exception e) {
				}

			}

			if (ret == null && tipo.getName().indexOf("java.") < 0) {

				try {

					String classKey = nombreparametro+".class";
					String tipoConcreto = mapRequest.get(classKey);
					if(tipoConcreto != null){
						tipo = Class.forName(tipoConcreto);
					}
					
					Method[] nmetos = tipo.getMethods();

					for (Method nmeto : nmetos) {
						// Buscar sobre los metodos

						if (nmeto.getName().indexOf("set") == 0 && nmeto.getName().length() > 3) {
							// que sean set

							String namemethod = ajustarNombre(nmeto.getName().substring(3, 4).toLowerCase() + nmeto.getName().substring(4));

							Set<String> sset = mapRequest.keySet();

							for (String bpar : sset) {

								if (bpar.indexOf(nombreparametro + ".") == 0) {
									// verifico si tengo que crearlo
									if (ret == null) {
										ret = tipo.newInstance();
									}
								}

								if (bpar.equals(nombreparametro + "." + namemethod) || bpar.indexOf(nombreparametro + "." + namemethod + ".") == 0 || bpar.indexOf(nombreparametro + "." + namemethod + ":") == 0) {
									// verifico si tengo que llenarlo

									Class<?>[] mclas = nmeto.getParameterTypes();

									if (mclas != null && mclas.length == 1) {

										Object[] argumentos = new Object[1];
										String sname = nmeto.toGenericString();
										sname = sname.substring(sname.indexOf("(") + 1);
										sname = sname.substring(0, sname.indexOf(")"));

										argumentos[0] = createObject(sname, mclas[0], nombreparametro + "." + namemethod, mapRequest);
										try {
											nmeto.invoke(ret, argumentos);
										} catch (Exception e) {
											logger.error("error", e);
										}

									}
									break;
								}
							}
						}
					}
				} catch (Exception e) {
					ret = null;
				}
			}
		}
		return ret;
	}

	// -----------------------------------------------------

	private static String simplevalidateName(Object name) {

		if (name == null)
			return "";

		return name.toString().trim().replace('\"', '_').replace("&", "&amp;");
	}

	private static String ajustarNombre(String name) {

		if (name == null)
			return "_";

		if ("01234567890".indexOf(name.charAt(0) + "") >= 0) {
			name = "N" + name;
		}
		if(org.apache.commons.lang3.StringUtils.isAlphanumeric(name)){
			return name.trim();
		}
		return name.trim().replace("[]", "Array").replace('?', '_').replace(':', '_').replace('/', '_').replace('\"', '_').replaceAll("[ ]", "_").replaceAll("[@]", "").replaceAll("[-]", "_").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\(", "").replaceAll("\\)", "");
	}

	// -----------------------------------------------------

	public static Map<String, String> objectToParameters(String basename, Object objeto) {
		return objectToParameters(basename, objeto, false);
	}

	// -----------------------------------------------------
	@SuppressWarnings({"rawtypes"})
	public static Map<String, String> objectToParameters(String basename, Object objeto, boolean verNulos) {

		Map<String, String> ret = new HashMap<String, String>();

		if (objeto == null && verNulos) {
			ret.put(basename, "");
		}

		if (objeto != null) {
			Class<?> tipo = objeto.getClass();

			if (basename == null || basename.trim().length() == 0) {
				basename = tipo.getSimpleName();
			}

			// -- si es arreglo

			try {

				Object[] objs = (Object[]) objeto;

				for (int i = 0; i < objs.length; i++) {
					ret.putAll(objectToParameters(basename + ":" + (i + 1), objs[i], verNulos));
				}

			} catch (Exception e) {
			}

			// -- Si es una lista

			if (tipo.getName().equals("java.util.List")) {
				List lista = (List) objeto;
				int i = 0;
				for (Object object : lista) {
					ret.putAll(objectToParameters(basename + ":" + (i + 1), object, verNulos));
					i++;
				}
			}

			// -- Si es un conjunto

			if (tipo.getName().equals("java.util.Set")) {
				Set lista = (Set) objeto;
				int i = 0;
				for (Object object : lista) {
					ret.putAll(objectToParameters(basename + ":" + (i + 1), object, verNulos));
					i++;
				}
			}

			// -- Si es basico

			try {
				// --
				if (tipo.getName().equals("java.lang.String")) {
					ret.put(basename, objeto.toString());
				} else
				// --
				if (tipo.getName().equals("java.lang.Integer")) {
					ret.put(basename, objeto.toString());
				} else
				// --
				if (tipo.getName().equals("java.util.Date")) {
					ret.put(basename, ((Date) objeto).getTime() + "");
					ret.put(basename + ".FORMAT", StringUtils.toString(objeto));
				} else
				// --
				if (tipo.getName().equals("java.lang.Float")) {
					ret.put(basename, objeto.toString());
				} else
				// --
				if (tipo.getName().equals("java.lang.Double")) {
					ret.put(basename, objeto.toString());
				} else
				// --
				if (tipo.getName().equals("java.lang.Boolean")) {
					ret.put(basename, objeto.toString());
				} else
				// --
				if (tipo.getName().equals("java.math.BigDecimal")) {
					ret.put(basename, objeto.toString());
				}

			} catch (Exception e) {

			}

			// si es objeto

			if (ret.size() == 0 && tipo.getName().indexOf("java.") < 0) {

				if (basename.indexOf(".") < 0) {
					ret.put(basename + ".CLASSNAME", tipo.getName());
				}

				Method[] nmetos = tipo.getMethods();

				for (Method nmeto : nmetos) {
					// Buscar sobre los metodos

					try {
						if (nmeto.getName().indexOf("get") == 0 && nmeto.getName().length() > 3) {
							// que sean get

							Set<String> nmet = new HashSet<String>();
							nmet.add("getClass");
							nmet.add("getTypeDesc");
							nmet.add("getSerializer");
							nmet.add("getDeserializer");

							if (!nmet.contains(nmeto.getName().trim())) {
								String namemethod = ajustarNombre(nmeto.getName().substring(3, 4).toLowerCase() + nmeto.getName().substring(4));

								ret.putAll(objectToParameters(basename + "." + namemethod, nmeto.invoke(objeto, new Object[0]), verNulos));
							}
						}
					} catch (Exception e) {
						//SimpleLogger.setError("E", e);
					}
				}
			}
		}

		return ret;

	}

	

	// ###################################################

	/**
	 * Permite cambiar el formato de las fechas de manera estática, se debe tener en cuenta que al cambiar el formato por defecto se afectan los servicios datapi.
	 * 
	 */

	public static void setDate_format(String date_format) {
		JavaToXML.date_format = date_format;
	}
}
