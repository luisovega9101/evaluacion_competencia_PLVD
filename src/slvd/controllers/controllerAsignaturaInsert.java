package slvd.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.springframework.web.portlet.util.PortletUtils;

import com.liferay.portal.kernel.util.ParamUtil;

import slvd.dao.IndicadorHome;
import slvd.database.Asignatura;

public class controllerAsignaturaInsert extends SimpleFormController {

	private IndicadorHome dao;
	
	@Override
	protected Object formBackingObject(PortletRequest request) throws Exception {
			// TODO Auto-generated method stub
			Asignatura a = new Asignatura();
			String btneditar = ParamUtil.get(request, "submit_edit", "default");
			if (!btneditar.equalsIgnoreCase("default")) {
				String idrecibido = ParamUtil.get(request, "id_asignatura", "default");
				int idAsign = Integer.parseInt(idrecibido);
				a = dao.listaAsignxId(idAsign);					
			}			
			return a;
	}		

	protected void onSubmitAction(ActionRequest request,
			ActionResponse response, Object command, BindException errors)
			throws Exception {		

		System.out.println("On submit action fromcontroller ");
		String btn = ParamUtil.get(request, "button_enviar_asign", "default");
		
		if (!btn.equalsIgnoreCase("default")) {
			logger.info("el boton ha sido oprimido");			
			Asignatura asignat = (Asignatura) command;
			String idenviado = request.getParameter("idAsignatura");
	
			String nombre = request.getParameter("nombreAsignatura");		
			List<Asignatura> x = dao.listaAsignxNomb(nombre);
			if(x.size()==0){
				if (!idenviado.equalsIgnoreCase("0")){
					System.out.println("Toy modificando si dios kiere");
					int idd = Integer.parseInt(idenviado);
					Asignatura aa= dao.listaAsignxId(idd);
					aa.setNombreAsignatura(request.getParameter("nombreAsignatura"));
					dao.modificarAsignatura(aa);
				} 
				else{
					dao.insertarAsignatura(asignat);
				}
			}
			else{
				String txt = "La asignatura enviada está ya registrada en el sistema";
				request.setAttribute("txt", txt);
			}
		} else {
			System.out.println("redirecting ...");
			PortletUtils.clearAllRenderParameters(response);
			response.setRenderParameter("action", "insertarasignatura");
		}
	}
	
	@Override
	protected ModelAndView onSubmitRender(RenderRequest request,
			RenderResponse response, Object command, BindException errors)
			throws Exception {
		Map<String, Object> aux = new HashMap<String, Object>();
		String txt = (String) request.getAttribute("txt");
		if(txt!=null){
			aux.put("txt", txt);
		}
		List<Asignatura> asignatura = dao.listarAsign();
		int cantAsig = asignatura.size();
		
		aux.put("asignatura", asignatura);
		aux.put("cantAsig", cantAsig);
		return new ModelAndView(getSuccessView(), "aux", aux);		
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
}