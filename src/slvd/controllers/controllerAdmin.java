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
import org.springframework.web.portlet.mvc.Controller;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Role;
import com.liferay.portal.theme.ThemeDisplay;

public class controllerAdmin implements Controller {

	@Override
	public void handleActionRequest(ActionRequest arg0, ActionResponse arg1)
			throws Exception {
		// TODO Auto-generated method stub

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
		boolean admin= false;
		int i=0;
		while (i <= liferayUserRoles.size() && admin==false){
			String rol = liferayUserRoles.get(0).getName();
			System.out.println(rol);
			if(rol.equals("Administrator"))
				admin=true;
			i++;
		}
		if(!admin)
			return new ModelAndView("errorAutenticado");
		Map<String, Object> aux = new HashMap<String, Object>();
		String path = request.getContextPath();
		aux.put("path", path);
		return new ModelAndView("administracion", "aux", aux);
	}

}
