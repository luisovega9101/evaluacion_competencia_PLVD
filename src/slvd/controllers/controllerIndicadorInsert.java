package slvd.controllers;

import java.util.ArrayList;
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
import slvd.data.Indicadoradd;
import slvd.database.Asignatura;
import slvd.database.Indicador;

public class controllerIndicadorInsert extends SimpleFormController {

	private IndicadorHome dao;
	
	@Override
	protected Map referenceData(PortletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> listado = new ArrayList<String>();
		List<Asignatura> lista = dao.listarAsign();
		for (Asignatura asign : lista) {
			listado.add(asign.getNombreAsignatura());
		}
		map.put("listaasign", listado);
		return map;
	}
	
	@Override
	protected Object formBackingObject(PortletRequest request) throws Exception {
			// TODO Auto-generated method stub
			Indicadoradd ia = new Indicadoradd();
			String btneditar = ParamUtil.get(request, "submit_edit", "default");			
			if (!btneditar.equalsIgnoreCase("default")) {
				String idrecibido = ParamUtil.get(request, "id_indicador", "default");
				int idIndic = Integer.parseInt(idrecibido);
				Indicador indic = dao.listaIndicxId(idIndic);
				String descripcionIndicador = indic.getDescripcionIndicador();
				String asignatura = indic.getAsignatura().getNombreAsignatura();
				ia.setIdIndicador(idIndic);
				ia.setAsignatura(asignatura);
				ia.setAsignatura_vieja(asignatura);
				ia.setDescripcionIndicador(descripcionIndicador);			
			}			
			return ia;
	}		

	protected void onSubmitAction(ActionRequest request,
			ActionResponse response, Object command, BindException errors)
			throws Exception {		
		String btn = ParamUtil.get(request, "button_enviar_indic", "default");
		
		if (!btn.equalsIgnoreCase("default")) {
			logger.info("el boton ha sido oprimido");			
			Indicadoradd indicadoradd = (Indicadoradd) command;					
			String idenviado = request.getParameter("idIndicador");		
			String asign = request.getParameter("asignatura");
			List<Asignatura> lista = dao.listaAsignxNomb(asign);
			Asignatura a = lista.get(0);
			String descrip = request.getParameter("descripcionIndicador");
			String txt = "";
			List<Indicador> listind = dao.listarIndicxAsignxDescrip(a, descrip);
			if(listind.size()==0){
				if (!idenviado.equalsIgnoreCase("0")){
					String asign_vieja = request.getParameter("asignatura_vieja");
					int idd = Integer.parseInt(idenviado);
					Indicador ii= dao.listaIndicxId(idd);
					if(asign.equalsIgnoreCase(asign_vieja)){
						dao.eliminarIndicadorCorrelac(idd);
					}
					ii.setAsignatura(a);
					ii.setDescripcionIndicador(descrip);
					dao.modificarIndicador(ii);
				} 
				else{
					Indicador indicad = new Indicador();
					indicad.setAsignatura(a);
					indicad.setNomenclaturaIndicador("I"+dao.maxId());
					indicad.setDescripcionIndicador(descrip);
					dao.insertarIndicador(indicad);
				}				
			}
			else{
				txt = "El indicador enviado está ya registrado para esa asignatura en el sistema";				
			}
			request.setAttribute("txt", txt);
		} else {
			System.out.println("redirecting ...");
			PortletUtils.clearAllRenderParameters(response);
			response.setRenderParameter("action", "insertarindicador");
		}
	}
	
	@Override
	protected ModelAndView onSubmitRender(RenderRequest request,
			RenderResponse response, Object command, BindException errors)
			throws Exception {
		String txt = (String) request.getAttribute("txt");
		Map<String, Object> aux = new HashMap<String, Object>();
		if(!txt.equalsIgnoreCase("")){
			aux.put("txt", txt);
		}
		List<Indicador> indicadores = dao.listarIndic();
		int cantIndic = indicadores.size();
		List<Asignatura> asignatura = dao.listarAsign();	
		int cantAsig = asignatura.size();
		aux.put("indicadores", indicadores);
		aux.put("asignatura", asignatura);
		aux.put("cantIndic", cantIndic);
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