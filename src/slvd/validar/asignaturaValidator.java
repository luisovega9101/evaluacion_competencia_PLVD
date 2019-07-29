package slvd.validar;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import slvd.database.Asignatura;

public class asignaturaValidator implements Validator {
	
	private MyValidator valid;

	@Override
	public boolean supports(Class arg0) {
		return arg0.equals(Asignatura.class);		
	}

	@Override
	public void validate(Object o, Errors errors) {
		Asignatura asign = (Asignatura)o;
		if(valid.vacio(asign.getNombreAsignatura())){
			errors.rejectValue("nombreAsignatura", "", "Entre el nombre de una asignatura");
		}else{
			String nombre = asign.getNombreAsignatura();
			if(!valid.soloLetras(nombre)){
				errors.rejectValue("nombreAsignatura", "", "El nombre de la asignatura es sólo caracteres");
			}
		}
	}

	public void setValid(MyValidator valid) {
		this.valid = valid;
	}
	
	public MyValidator getValid() {
		return valid;
	}
}
