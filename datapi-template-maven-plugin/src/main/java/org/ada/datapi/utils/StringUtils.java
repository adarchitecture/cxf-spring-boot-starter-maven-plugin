package org.ada.datapi.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings({ "deprecation", "restriction" })
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static final String segureCode = "MEDKSIWEODMLSLLJRJIYEBQPWORJUNMXNZKAJEUFHGHJSKLALSKASDJHOJOWIYEBKJKLASDJAWQOEUBDUEWGFTTRUEOPIUQOWEHWSNMKCDSJKC";

	private static Map<String, String> dateFormatRegEx;

	static {
		dateFormatRegEx = new HashMap<String, String>();
		dateFormatRegEx.put("dd/MM/yyyy", "[0-3]\\d/[01]\\d/\\d{4}");
		dateFormatRegEx.put("dd/MM/yyyy HH:mm:ss", "[0-3]\\d/[01]\\d/\\d{4} [0-2]\\d:[0-5]\\d:[0-5]\\d");
		dateFormatRegEx.put("dd-MM-yyyy", "[0-3]\\d-[01]\\d-\\d{4}");
		dateFormatRegEx.put("dd-MM-yyyy HH:mm:ss", "[0-3]\\d-[01]\\d-\\d{4} [0-2]\\d:[0-5]\\d:[0-5]\\d");
		dateFormatRegEx.put("yyyy-MM-dd", "\\d{4}-[01]\\d-[0-3]\\d");
		dateFormatRegEx.put("yyyy-MM-dd HH:mm:ss", "\\d{4}-[01]\\d-[0-3]\\d [0-2]\\d:[0-5]\\d:[0-5]\\d");
		dateFormatRegEx.put("yyyy/MM/dd", "\\d{4}/[01]\\d/[0-3]\\d");
		dateFormatRegEx.put("yyyy/MM/dd HH:mm:ss", "\\d{4}/[01]\\d/[0-3]\\d [0-2]\\d:[0-5]\\d:[0-5]\\d");
	}

	/**
	 * Convertir un string ASCII en Hexagesimal Si hay un error retorna null
	 * 
	 * @param str
	 * @return
	 */
	public static String convertStringToHex(String str) {
		try {
			char[] chars = str.toCharArray();

			StringBuffer hex = new StringBuffer();
			for (int i = 0; i < chars.length; i++) {
				hex.append(Integer.toHexString((int) chars[i]));
			}

			return hex.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Convertir un valor Hexagesimal en String ASCII. Si hay error retorna null
	 * 
	 * @param hex
	 * @return
	 */
	public static String convertHexToString(String hex) {
		try {
			StringBuilder sb = new StringBuilder();
			StringBuilder temp = new StringBuilder();

			// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
			for (int i = 0; i < hex.length() - 1; i += 2) {

				// grab the hex in pairs
				String output = hex.substring(i, (i + 2));
				// convert hex to decimal
				int decimal = Integer.parseInt(output, 16);
				// convert the decimal to character
				sb.append((char) decimal);

				temp.append(decimal);
			}

			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static List<String> subString(List<?> datos, int ini) {

		List<String> ret = new ArrayList<String>();

		if (datos != null && !datos.isEmpty()) {

			for (Object dat : datos) {

				if (dat != null) {
					String strdat = dat.toString();

					try {
						ret.add(strdat.substring(ini));
					} catch (Exception e) {
					}
				}
			}
		}

		return ret;
	}

	public static List<String> subString(List<?> datos, int ini, int fin) {

		List<String> ret = new ArrayList<String>();

		if (datos != null && !datos.isEmpty()) {

			for (Object dat : datos) {

				if (dat != null) {
					String strdat = dat.toString();

					try {
						ret.add(strdat.substring(ini, fin));
					} catch (Exception e) {
					}
				}
			}
		}

		return ret;
	}

	public static List<String> filtrarLista(List<?> datos, String txt) {

		List<String> ret = new ArrayList<String>();

		if (datos != null && !datos.isEmpty()) {

			for (Object dat : datos) {

				if (dat != null) {
					String strdat = dat.toString();

					if (strdat.indexOf(txt) >= 0) {
						ret.add(strdat);
					}
				}
			}
		}

		return ret;
	}

	public static String aplanar(List<?> datos, String separador) {

		StringBuffer ret = new StringBuffer("");

		if (datos != null && !datos.isEmpty()) {

			for (int i = 0; i < datos.size(); i++) {
				Object reg = datos.get(i);

				if (reg != null && reg.toString().trim().length() > 0) {
					if (ret.length() > 0) {
						ret.append(separador);
					}
					ret.append(reg.toString().trim());
				}
			}

		}

		return ret.toString();
	}

	public static Boolean esVerdad(String string) {

		if (esVacio(string)) {
			return false;
		}

		string = string.trim().toLowerCase();

		return (string.equals("si") || string.equals("yes") || string.equals("true") || string.equals("1") || string.equals("y") || string.equals("s") || string.equals("t"));
	}

	/**
	 * valida que un String solo tenga ciertos caracteres (todos o alguno), no sea vacio
	 * 
	 * @param texto
	 *            Texto a validar
	 * @param caracteres
	 *            Caracteres validos por ej: SN valida que tena S o N o los dos depende de lengthMax
	 * @param lengthMax
	 *            longitud máxima del texto
	 * @return
	 */
	public static Boolean contieneSolo(String texto, String caracteres, Integer lengthMax) {
		if (StringUtils.esVacio(texto) || texto.length() > lengthMax || !org.apache.commons.lang3.StringUtils.containsOnly(texto, caracteres)) {
			return false;
		}

		return true;
	}

	/**
	 * determina si un string es vacio (el espacio es un caracter vacio)
	 * 
	 * @param string
	 * @return true si todos son vacios
	 */
	public static Boolean esVacio(Object... string) {

		if (string == null || string.length == 0) {
			return true;
		}

		Boolean ret = true;
		for (Object obj : string) {
			ret = ret && (obj == null || obj.toString().trim().length() == 0);
		}

		return ret;
	}

	/**
	 * determina si un string es vacio (el espacio es un caracter vacio)
	 * 
	 * @param string
	 * @return true, si todos no son vacios
	 */
	public static Boolean esNoVacio(Object... string) {

		if (string == null || string.length == 0) {
			return false;
		}

		Boolean ret = true;
		for (Object obj : string) {
			ret = ret && (obj != null && obj.toString().trim().length() > 0);
		}

		return ret;
	}

	public static Boolean esAlgunoVacio(Object... string) {
		return !esNoVacio(string);
	}

	public static String espacios(int cantidad) {
		return repetir(cantidad, ' ');
	}

	public static String repetir(int cantidad, char str) {
		StringBuffer r = new StringBuffer("");
		for (int i = 0; i < cantidad; i++) {
			r.append(str);
		}
		return r.toString();
	}

	public static String derecha(Object txt, int cantidad) {

		return derecha(txt, cantidad, ' ');
	}

	public static String derecha(Object obj, int cantidad, char caracter) {

		String txt = (obj == null) ? "" : obj.toString();

		return txt.trim() + repetir(cantidad - txt.trim().length(), caracter);
	}

	public static String izquierda(Object obj, int cantidad) {

		return izquierda(obj, cantidad, ' ');
	}

	public static String izquierda(Object obj, int cantidad, char caracter) {

		String txt = (obj == null) ? "" : obj.toString();

		return repetir(cantidad - txt.trim().length(), caracter) + txt.trim();
	}

	// ---------------------------------------------------

	public static String traducir(String cadena, Map<?, ?> transformaciones) {
		String respuesta = cadena;
		Set<?> conjTranformaciones = transformaciones.entrySet();
		Iterator<?> it = conjTranformaciones.iterator();
		while (it.hasNext()) {
			Map.Entry<?, ?> clave = (Map.Entry<?, ?>) it.next();
			String val1 = (String) clave.getKey();
			String val2 = (String) clave.getValue();
			respuesta = respuesta.replaceAll(val1, val2);
		}
		return respuesta;
	}

	// ---------------------------------------------------

	public static String escapeSQL(String sql) {
		return "";
		//return StringEscapeUtils.escapeSql(trimToEmpty(sql));
	}

	// ---------------------------------------------------

	
	public static String escapeXML(String str) {

		return StringEscapeUtils.escapeXml(trimToEmpty(str));
	}

	// ---------------------------------------------------

	public static String convertirAColExcel(Integer num) {

		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String p2 = abc.charAt((num - 1) % 26) + "";
		String p1 = num <= 26 ? "" : (abc.charAt((num - 1) / 26 - 1) + "");

		return p1 + p2;
	}

	// ---------------------------------------------------

	public static String toFileName(String name) {
		if (name == null || name.trim().length() == 0) {
			return "empty";
		}

		return name.replace(' ', '_').replace('/', '_').replace('\\', '_').replace('$', '_').replace('&', '_');
	}

	// ---------------------------------------------------

	public static String getStringCorchetes(String str) {
		if (str == null)
			return "";

		str = str.substring(str.indexOf("[") + 1, str.length());

		int cc = str.indexOf("]");
		if (cc >= 0) {
			str = str.substring(0, cc);
		}

		return str;

	}

	// ---------------------------------------------------

	private static String CRIPTOALPHA = "ABCDEFGHIJKLMNOPQRSTUBWXYZabcdefghijklmnopqrstubwxyz0123456789.:";

	private static String getString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];

			int a = (int) b + 128;

			int p1 = a / CRIPTOALPHA.length();
			int p2 = a % CRIPTOALPHA.length();

			sb.append(CRIPTOALPHA.charAt(p1));
			sb.append(CRIPTOALPHA.charAt(p2));
		}
		return sb.toString();
	}

	public static String MD5(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(source.getBytes());
			return getString(bytes);
		} catch (Exception e) {
			//SimpleLogger.setError("Error al realizar MD5", e);
			return null;
		}
	}

	public static String SHA(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte[] bytes = md.digest(source.getBytes());
			return getString(bytes);
		} catch (Exception e) {
			//SimpleLogger.setError("Error al realizar SHA", e);
			return null;
		}
	}

	public static String SHA512(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytes = md.digest(source.getBytes());
			return getString(bytes);
		} catch (Exception e) {
			//SimpleLogger.setError("Error al realizar SHA-512", e);
			return null;
		}
	}

	public static String cifrar(String source, String algoritmo) {

		algoritmo = StringUtils.trimToEmpty(algoritmo).toUpperCase();

		if ("SHA".equals(algoritmo)) {
			return SHA(source);
		}

		if ("SHA512".equals(algoritmo)) {
			return SHA512(source);
		}

		if ("MD5".equals(algoritmo)) {
			return MD5(source);
		}

		return null;
	}

	public static String toString(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	public static InputStream toInputStream(String str) {
		byte[] bytes = str.getBytes();
		return new ByteArrayInputStream(bytes);
	}

	private static SecureRandom random = new SecureRandom();

	public static String randomString(Integer size) {

		if (size == null) {
			size = 32;
		}

		String alfa = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";

		StringBuffer salida = new StringBuffer("");

		for (int i = 0; i < size; i++) {
			int p = (int) (random.nextDouble() * alfa.length());
			salida.append(alfa.charAt(p));
		}

		return salida.toString();

	}

	
	public static String getStringDate() {
		String dia;
		Integer anno, mes, ndia;

		Date date = new Date();

		anno = date.getYear() + 1900;
		mes = date.getMonth() + 1;
		ndia = date.getDate();

		switch (date.getDay()) {
		case 0:
			dia = "Domingo";
			break;
		case 1:
			dia = "Lunes";
			break;
		case 2:
			dia = "Martes";
			break;
		case 3:
			dia = "Miércoles";
			break;
		case 4:
			dia = "Jueves";
			break;
		case 5:
			dia = "Viernes";
			break;
		case 6:
			dia = "Sábado";
			break;
		default:
			dia = "??";
			break;
		}

		return (dia + ", " + (ndia < 10 ? "0" : "") + ndia + "/" + (mes < 10 ? "0" : "") + mes + "/" + anno);
	}

	
	public static String getStringTime() {
		Integer hora, minutos;

		Date date = new Date();

		hora = date.getHours();
		minutos = date.getMinutes();

		return getStringDate() + " - " + (hora < 10 ? "0" : "") + hora + ":" + (minutos < 10 ? "0" : "") + minutos;
	}

	/**
	 * Recibe el patrón (string) para el formato y retorna la cadena con la fecha/hora formateada
	 * 
	 * @param pattern
	 *            patron estándar para fecha/hora
	 * @return String con fecha actual formateada
	 */
	public static String getStringTime(String pattern) {
		String resultado = "";

		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();

		SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, new Locale("es", "ES"));

		try {
			formatter.applyPattern(pattern);
			resultado = formatter.format(date);
		} catch (Exception e) {
			formatter.applyPattern("EEEE, dd/MM/yyyy - HH:mm");
			resultado = formatter.format(date);
		}

		return resultado;
	}

	public static String trim(String str) {
		if (str == null) {
			return "";
		} else {
			return str.trim();
		}
	}

	public static String[] trim(String str[]) {

		if (str == null) {
			return null;
		}

		String ret[] = new String[str.length];
		for (int i = 0; i < str.length; i++) {
			ret[i] = trim(str[i]);
		}

		return ret;
	}

	public static List<String> trim(List<String> str) {

		ArrayList<String> ret = new ArrayList<String>();
		if (str != null) {
			for (String string : str) {
				ret.add(trim(string));
			}
		}

		return ret;
	}

	public static String simpleCripto(String data) {

		if (data == null) {
			return null;
		}

		String res = "";

		for (int i = 0; i < data.length(); i++) {
			res = res + izquierda("" + ((int) data.charAt(i)), 3, '0');
		}

		return res;
	}

	public static String simpleDesCripto(String data) {

		if (data == null) {
			return null;
		}

		try {
			String res = "";

			for (int i = 0; i < data.length(); i += 3) {
				res = res + ((char) (Integer.parseInt(data.substring(i, i + 3))));
			}

			return res;

		} catch (Throwable e) {
			return null;
		}

	}

	public static Date toDate(String strdate) {
		Iterator<Entry<String, String>> it = dateFormatRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = it.next();
			if (strdate.matches(pairs.getValue())) {
				return toDate(strdate, pairs.getKey());
			}
		}
		return null;
	}

	public static Date toDate(String strdate, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		try {
			return sdf.parse(strdate);
		} catch (Exception e) {
		}
		return null;
	}

	public static String toString(Object valor) {

		if (valor == null) {
			return null;
		}

		if (valor instanceof Date) {

			try {
				return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format((Date) valor).replace(" 00:00:00", "").trim();
			} catch (Exception e) {
			}
		}

		if (valor instanceof Double) {
			return toString(new BigDecimal((Double) valor));
		}

		return valor.toString();
	}

	public static String toStringFormat(Object valor) {

		if (valor == null) {
			return null;
		}

		if (valor instanceof Date) {

			try {
				return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format((Date) valor).replace(" 00:00:00", "").trim();
			} catch (Exception e) {
			}
		}

		if (valor instanceof Double) {
			return toStringFormat(new BigDecimal((Double) valor));
		}

		if (valor instanceof BigDecimal) {
			NumberFormat formatter = new DecimalFormat("##,##0.00################");
			return formatter.format(valor);
		}

		return valor.toString();
	}

	public static int calcularDigitoVerificacion(String nit1) {

		if (!StringUtils.isNumeric(nit1)) {
			throw new RuntimeException("No se puede calcular el digito de verificación.");
		}

		int dv1;

		Integer vpri[] = new Integer[16];
		int x = 0;
		int y = 0;
		int z = nit1.length();
		vpri[1] = 3;
		vpri[2] = 7;
		vpri[3] = 13;
		vpri[4] = 17;
		vpri[5] = 19;
		vpri[6] = 23;
		vpri[7] = 29;
		vpri[8] = 37;
		vpri[9] = 41;
		vpri[10] = 43;
		vpri[11] = 47;
		vpri[12] = 53;
		vpri[13] = 59;
		vpri[14] = 67;
		vpri[15] = 71;

		for (int i = 0; i < z; i++) {
			y = Integer.parseInt(nit1.substring(i, i + 1));
			x += (y * vpri[z - i]);
		}
		y = x % 11;

		if (y > 1) {
			dv1 = 11 - y;
		} else {
			dv1 = y;
		}

		return dv1;

	}

	public static Map<String, Object> MapToUpperCase(Map<String, Object> mapa) {
		if (mapa == null) {
			return null;
		}

		Map<String, Object> map_ret = new HashMap<String, Object>();

		Set<String> keys = mapa.keySet();

		if (keys == null) {
			return mapa;
		}

		for (String key : keys) {
			map_ret.put(key.toUpperCase(), mapa.get(key));
		}

		return map_ret;
	}

	public static List<Map<String, Object>> ListMapToUpperCase(List<Map<String, Object>> listmapa) {
		if (listmapa == null) {
			return null;
		}

		List<Map<String, Object>> listmapa_ret = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> map : listmapa) {
			listmapa_ret.add(MapToUpperCase(map));
		}

		return listmapa_ret;
	}

	// ---------------------------------------------------

	public static Boolean validaExpRegular(String string, String patron) {

		if (esVacio(string) || esVacio(patron)) {
			return false;
		}

		boolean result = string.matches(patron);
		return result;
	}

	/** [\\d]+ = Número, equivale a [0-9] */
	public static Boolean validaNumero(String string) {
		boolean result = validaExpRegular(string, "^[\\d]+$");
		return result;
	}

	/** [\\w]+ = Palabra, equivale a [a-zA-Z_0-9-]. No incluye espacios! */
	public static Boolean validaPalabra(String string) {
		boolean result = validaExpRegular(string, "^[\\w\\-]+$");
		return result;
	}

	/** [\\w]+ = Palabra, equivale a [a-zA-Z_0-9-]. No incluye espacios! */
	public static Boolean validaPalabraEspeciales(String string) {
		boolean result = validaExpRegular(string, "^[\\w\\-\\.\\' ]+$");
		return result;
	}

	/**
	 * Valida el correo-e
	 */
	public static Boolean validaCorreoE(String string) {

		boolean result = validaExpRegular(string.toLowerCase(), "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$");

		return result;
	}

	/** Validación OR de la cadena para todos los patrones enviados en la lista */
	public static Boolean validaListadoPatrones(String string, List<String> lista) {
		int longitud = lista.size();
		int tmp = 0;
		String patronConcat = "";

		if (longitud > 0) {
			tmp = longitud - 1;
		} else {
			return false;
		}

		for (int i = 0; i < longitud; i++) {
			patronConcat = patronConcat + lista.get(i);
			if (i < tmp) {
				patronConcat = patronConcat + "|";
			}
		}

		boolean result = validaExpRegular(string, patronConcat);
		return result;
	}

	/***
	 * Funcion que reemplaza caracteres especiales tales como
	 * 
	 * 
	 */
	public static String quitarCaracteresEspeciales(String texto, String diccionario, Map<Character, Character> mapaTraductor) {

		if (texto == null) {
			return null;
		}

		if (mapaTraductor != null) {
			Set<Character> mapakey = mapaTraductor.keySet();

			// Se remplazan todos los caracteres especificados en el mapa
			for (Character key : mapakey) {
				texto = texto.replace(key, mapaTraductor.get(key));
			}
		}

		// Luego de reemplazar se valida con el diccionario
		StringBuffer textoReemplazado = new StringBuffer();
		for (int i = 0; i < texto.length(); i++) {
			if (diccionario.contains(texto.charAt(i) + "")) {
				textoReemplazado.append(texto.charAt(i));
			}
		}

		return textoReemplazado.toString();

	}

	/***
	 * Reemplaza caracteres especiales para nombres. Para ello utila un diccionario básico Diccionario básico: abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890' ' y remplaza los siguientes caracteres especiales ñÑáéíóúÁÉÍÓÚ
	 * 
	 * @param
	 * @return
	 */

	public static String reemplazarCaracteresEspecialesParaNombre(String nombre) {

		String diccionario = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 ";

		Map<Character, Character> mapaTraductor = new HashMap<Character, Character>();
		mapaTraductor.put('ñ', 'n');
		mapaTraductor.put('Ñ', 'N');
		mapaTraductor.put('á', 'a');
		mapaTraductor.put('é', 'e');
		mapaTraductor.put('í', 'i');
		mapaTraductor.put('ó', 'o');
		mapaTraductor.put('ú', 'u');
		mapaTraductor.put('Á', 'A');
		mapaTraductor.put('É', 'E');
		mapaTraductor.put('Í', 'I');
		mapaTraductor.put('Ó', 'O');
		mapaTraductor.put('Ú', 'U');

		return quitarCaracteresEspeciales(nombre, diccionario, mapaTraductor);

	}

	/**
	 * Elimina los messajes de oracle (ORA-xxxx) de un texto especifico
	 * 
	 * @param texto
	 *            de entrada
	 * @return texto sin ora.
	 */
	public static String limpiaOra(String texto) {

		if (texto == null) {
			return texto;
		}

		texto = texto.replaceAll("([(][O][R][A][-])([0-9])*[)]", "");
		texto = texto.replaceAll("([O][R][A][-])([0-9])*[:]", "");
		texto = texto.replaceAll("([O][R][A][-])([0-9])*", "");

		texto = texto.replaceAll("([P][L][/][S][Q][L][:][ ][e][r][r][o][r][ ][:])", "");
		texto = texto.replaceAll("([P][L][/][S][Q][L][:][ ][e][r][r][o][r])", "");
		texto = texto.replaceAll("([P][L][/][S][Q][L][:])", "");
		texto = texto.replaceAll("([P][L][/][S][Q][L])", "");

		return texto;
	}

	/**
	 * Elimina los messajes de oracle (ORA-xxxx) de los String del objetos
	 * 
	 * @param texto
	 *            de entrada
	 * @return texto sin ora.
	 */

	public static <T> T limpiaOra(T obj) {

		try {
			ArrayList<Object> objetos = new ArrayList<Object>();
			limpiaOraT(obj, objetos);
		} catch (Throwable e) {
		}
		return obj;
	}

	/**
	 * Elimina los <code>0</code>'s con los que comience la cadena <code>str</code>
	 * 
	 * @param str
	 *            texto de entrada
	 * @return texto sin ceros
	 */
	public static String removeLeadingZeros(String str) {
		return str.replaceFirst("^0+(?!$)", "");
	}

	private static void limpiaOraT(Object obj, List<Object> objetos) {

		if (obj == null) {
			return;
		}

		if (objetos.contains(obj)) {
			return;
		}

		objetos.add(obj);

		// --

		if (obj instanceof Collection<?>) {

			Collection<?> lista = (Collection<?>) obj;

			for (Object object : lista) {
				limpiaOraT(object, objetos);
			}

			return;
		}

		if (obj instanceof Object[]) {

			Object[] arr = (Object[]) obj;

			for (Object object : arr) {
				limpiaOraT(object, objetos);
			}

			return;
		}

		// --

		Method[] methods = obj.getClass().getMethods();

		for (Method method : methods) {

			Class<?>[] p = method.getParameterTypes();

			if (p == null || p.length == 0) {

				// para los metodos con nombre cod_rspta no se les quita el ora.
				if (method.getName().indexOf("cod_rspta") < 0 && method.getName().startsWith("get")) {

					Method m = null;
					try {
						String nameget = "set" + method.getName().substring(3);
						m = obj.getClass().getMethod(nameget, method.getReturnType());
					} catch (Exception e) {
					}

					if (m != null) {
						try {
							if (method.getReturnType() == String.class) {
								m.invoke(obj, limpiaOra((String) method.invoke(obj)));
							} else {
								limpiaOraT(method.invoke(obj), objetos);
							}
						} catch (Exception e) {
						}
					}

				}
			}
		}

	}

	public static String encodeURIComponent(String component) {

		if (component == null) {
			return "";
		}

		String result = null;

		try {
			result = URLEncoder.encode(component, "UTF-8").replaceAll("\\%28", "(").replaceAll("\\%29", ")").replaceAll("\\+", "%20").replaceAll("\\%27", "'").replaceAll("\\%21", "!").replaceAll("\\%7E", "~");
		} catch (UnsupportedEncodingException e) {
			result = component;
		}

		return result;
	}

	
	public static byte[] decodeBase64(String str) {
		if (str == null) {
			return new byte[0];
		}

		BASE64Decoder decoder = new BASE64Decoder();

		try {
			return decoder.decodeBuffer(str);
		} catch (IOException e) {
			//SimpleLogger.setError("Error decodeBase64", e);
			return null;
		}
	}

	
	public static String encodeBase64(byte[] decode) {

		if (decode == null) {
			return null;
		}

		BASE64Encoder encoder = new BASE64Encoder();

		try {
			return encoder.encodeBuffer(decode).replace("\r\n", "").replace("\n", "");
		} catch (Exception e) {
			////SimpleLogger.setError("Error encodeBase64", e);
			return null;
		}
	}

}