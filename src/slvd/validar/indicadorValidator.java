package slvd.validar;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import slvd.data.Indicadoradd;

public class indicadorValidator implements Validator {
	
	private MyValidator valid;

	@Override
	public boolean supports(Class arg0) {		
		return arg0.equals(Indicadoradd.class);
	}

	@Override
	public void validate(Object o, Errors errors) {		
		Indicadoradd indic = (Indicadoradd)o;
		if(valid.vacio(indic.getDescripcionIndicador())){
			errors.rejectValue("descripcionIndicador", "", "Entre la descripci�n del indicador");
		}else{
			String descripc = indic.getDescripcionIndicador();
			if(!valid.soloLetras(descripc)){
				errors.rejectValue("descripcionIndicador", "", "La descripci�n es s�lo caracteres");
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
