package slvd.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.Controller;

public class controllerIndex implements Controller {

	@Override
	public void handleActionRequest(ActionRequest request, ActionResponse response)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {			
		Map<String, Object> aux = new HashMap<String, Object>();		
		String path = request.getContextPath();
		aux.put("path", path);		
		String inferencia = "";		
		aux.put("inferencia", inferencia);
		return new ModelAndView("index", "aux", aux);
	}
}