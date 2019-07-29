package slvd.validar;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import slvd.data.Preguntaadd;

public class preguntaValidator implements Validator {
	
	private MyValidator valid;

	@Override
	public boolean supports(Class arg0) {
		return arg0.equals(Preguntaadd.class);	
	}

	@Override
	public void validate(Object o, Errors errors) {
		Preguntaadd preg = (Preguntaadd)o;
		if(valid.vacio(preg.getPregunta())){
			errors.rejectValue("pregunta", "", "Escriba la pregunta");
		}
	}

	public void setValid(MyValidator valid) {
		this.valid = valid;
	}
	
	public MyValidator getValid() {
		return valid;
	}
}
