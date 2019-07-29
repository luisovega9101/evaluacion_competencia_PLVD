package slvd.validar;

public class MyValidator {
	
	public MyValidator() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean vacio(Object o) {
		try {
			if (o == null) {
				return true;
			}
			System.out.println("******* Validating vacio " + o.toString());
			if (o instanceof String) {
				String str = (String) o;
				return str.equals("") || str.equals(" ") || str.equals("  ") ? true : false;
			}
		} catch (Exception e) {
			System.out.println("******* Validating Exception " + e.getMessage());
			return false;
		}
		return false;
	}
	
	public boolean esDouble(String cadena) {
		try {
			double test = Double.parseDouble(cadena);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean soloLetras(String cadena) {
		for (int i = 0; i < cadena.length(); i++) {
			char c = cadena.charAt(i);
			if (!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean soloLetrasNumeros(String cadena) {
		for (int i = 0; i < cadena.length(); i++) {
			char c = cadena.charAt(i);
			if (!Character.isDigit(c) || !Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}
}