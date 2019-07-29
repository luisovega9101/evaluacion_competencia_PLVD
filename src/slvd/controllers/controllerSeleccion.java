package slvd.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Role;
import com.liferay.portal.theme.ThemeDisplay;

import slvd.dao.IndicadorHome;
import slvd.database.Asignatura;

public class controllerSeleccion extends AbstractController {
	
	IndicadorHome dao;

	@Override
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {
	}

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		Map userMap = (Map) request.getAttribute(PortletRequest.USER_INFO);
		if (userMap == null) {
			System.out
					.println("Debe estar autenticado para ver sus ejecuciones");
			return new ModelAndView("errorAutenticado");
		}

		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);
		String liferayUser = themeDisplay.getRealUser().getFullName();
		List<Role> liferayUserRoles = themeDisplay.getUser().getRoles();
		System.out.println("El Usuario es : " + liferayUser);
		System.out.println("Sus roles son:");
		boolean usuario= false;
		int i=0;		
		String sms = null;
		String seleccion = request.getParameter("sel");
		String accion = null;
		if(seleccion.equalsIgnoreCase("1")){
			while (i <= liferayUserRoles.size() && usuario==false){
				String rol = liferayUserRoles.get(0).getName();
				System.out.println(rol);
				if(rol.equals("Experto"))
					usuario=true;
				i++;
			}
			//if(!usuario){
			//return new ModelAndView("errorRole");	}
			sms = "Seleccione una asignatura para realizar la evaluación de competencias";
			accion= "correlacion";
		}else if(seleccion.equalsIgnoreCase("2")){
			while (i <= liferayUserRoles.size() && usuario==false){
				String rol = liferayUserRoles.get(0).getName();
				System.out.println(rol);
				if(rol.equals("Profesor") || rol.equals("Experto"))
					usuario=true;
				i++;
			}
			//if(!usuario){
			//return new ModelAndView("errorRole");}
			sms = "Seleccione una asignatura para gestionar el cuestionario";
			accion= "cuestionario";
		}else if(seleccion.equalsIgnoreCase("3")){
			while (i <= liferayUserRoles.size() && usuario==false){
				String rol = liferayUserRoles.get(0).getName();
				System.out.println(rol);
				if(rol.equals("Estudiante"))
					usuario=true;
				i++;
			}
			//if(!usuario){
			//return new ModelAndView("errorRole");}
			sms = "Seleccione una asignatura para la evaluación de competencias";
			accion= "evaluacion";
		}		
		String idexperto = (String) userMap.get("liferay.user.id");		
		List<Asignatura> asignatura = dao.listarAsign();
		Map<String, Object> aux = new HashMap<String, Object>();
		String path = request.getContextPath();
		String txt=null;
		aux.put("txt", txt);
		aux.put("sms", sms);
		aux.put("accion", accion);		
		aux.put("idexperto", idexperto);
		aux.put("nombusuario", liferayUser);
		aux.put("asignatura", asignatura);
		aux.put("path", path);
		return new ModelAndView("seleccionAsign", "aux", aux);
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}

}
