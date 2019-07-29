package slvd.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ccpp.SetAttribute;
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
import slvd.data.Correlacion;
import slvd.database.Asignatura;
import slvd.database.Indicador;
import slvd.database.IndicadorIndicador;
import slvd.database.MatrizAdyacenciaAsociada;
import slvd.database.VariableLenguistica;

public class controllerCorrelacionFile extends SimpleFormController {

	private IndicadorHome dao;

	@Override
	protected Map referenceData(PortletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> listado = new ArrayList<String>();
		List<VariableLenguistica> lista = dao.listarVarLeng();
		for (VariableLenguistica variableLenguistica : lista) {
			listado.add(variableLenguistica.getImpacto());
		}
		map.put("listavarleng", listado);
		return map;
	}

	@Override
	protected Object formBackingObject(PortletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Correlacion c = new Correlacion();
		String btneditar = ParamUtil.get(request, "submit_edit", "default");
		if (!btneditar.equalsIgnoreCase("default")) {
			String idrecibido = ParamUtil.get(request, "id_indicadorindicador",
					"default");
			int idIndicIndic = Integer.parseInt(idrecibido);
			
			String idasignrecibido = ParamUtil.get(request, "id_asignatura",
					"default");
			int idasignatura = Integer.parseInt(idasignrecibido);
			
			String idUsuario = ParamUtil.get(request, "id_usuario", "default");
			IndicadorIndicador idIndInd = dao.listaIndicIndicXId(idIndicIndic);
			List<MatrizAdyacenciaAsociada> madyas = dao.obtenerMatrizAdyacUserIdd(
					idUsuario, idIndInd);
			MatrizAdyacenciaAsociada m = madyas.get(0);
			int idindicadorentrada = dao.listaIndicxId(
					idIndInd.getIndicadorByIdIndicadorEntrada()
							.getIdIndicador()).getIdIndicador();
			String descripcentrada = dao.listaIndicxId(
					idIndInd.getIndicadorByIdIndicadorEntrada()
							.getIdIndicador()).getDescripcionIndicador();
			int idindicadorsalida = dao
					.listaIndicxId(
							idIndInd.getIndicadorByIdIndicadorSalida()
									.getIdIndicador()).getIdIndicador();
			String descripsalida = dao
					.listaIndicxId(
							idIndInd.getIndicadorByIdIndicadorSalida()
									.getIdIndicador())
					.getDescripcionIndicador();
			String variablelenguistica = dao.obtenerVarLengPorId(
					m.getVariableLenguistica().getIdVariableLenguistica())
					.getImpacto();

			c.setIdusuario(idUsuario);
			c.setIdasignatura(idasignrecibido);
			c.setIdindicadorentrada(idindicadorentrada);
			c.setDescripcentrada(descripcentrada);
			c.setIdindicadorsalida(idindicadorsalida);
			c.setDescripsalida(descripsalida);
			c.setIdindicadorindicador(idIndicIndic);
			c.setVariablelenguistica(variablelenguistica);
		}
		return c;
	}

	protected void onSubmitAction(ActionRequest request,
			ActionResponse response, Object command, BindException errors)
			throws Exception {
		System.out.println("On submit action fromcontroller ");
		String btn = ParamUtil.get(request, "button_enviar_correl", "default");

		if (!btn.equalsIgnoreCase("default")) {
			logger.info("el boton ha sido oprimido");
			String user = request.getParameter("idusuario");
			Correlacion correlacion = (Correlacion) command;
			int idIndInd = Integer.parseInt(request
					.getParameter("idindicadorindicador"));
			String impacto = request.getParameter("variablelenguistica");
			int idVarLeng = dao.obtenerVarLengPorImpacto(impacto)
					.getIdVariableLenguistica();
			dao.crearMatrizAsoc(user, idIndInd, idVarLeng);
			
			request.setAttribute("idexper", user);
			String idA = request.getParameter("idasignatura");
			request.setAttribute("idasig", idA);
			
		} else {
			System.out.println("redirecting ...");
			PortletUtils.clearAllRenderParameters(response);
			response.setRenderParameter("action", "editarcorrelacion");
		}
	}

	@Override
	protected ModelAndView onSubmitRender(RenderRequest request,
			RenderResponse response, Object command, BindException errors)
			throws Exception {
		String idexperto = (String) request.getAttribute("idexper");
		String idA = (String) request.getAttribute("idasig");		
		int idasign = Integer.parseInt(idA);
		Asignatura asig = dao.listaAsignxId(idasign);
		Map<String, Object> aux = new HashMap<String, Object>();
		List<Indicador> indicadores = dao.listarIndicxAsign(asig);
		int s = indicadores.size();
		if (s < 2) {
			List<Asignatura> asignatura = dao.listarAsign();
			String txt = "No existen indicadores suficientes para establecer la evaluación de competencia";
			aux.put("txt", txt);
			aux.put("idexperto", idexperto);
			aux.put("asignatura", asignatura);
			String sms = "Seleccione una asignatura para la evaluación de competencias";
			String accion= "correlacion";			
			aux.put("sms", sms);
			aux.put("accion", accion);
			return new ModelAndView("seleccionAsign", "aux", aux);
		}		
		List<IndicadorIndicador> listaindicindic = dao.listarIndicIndic();
		List<MatrizAdyacenciaAsociada> matrizady = dao
				.obtenerMatrizAdyacUsuario(idexperto);
		List<VariableLenguistica> varleng = dao.listarVarLeng();
		
		aux.put("idexperto", idexperto);
		aux.put("asignatura", asig);
		aux.put("indicadores", indicadores);
		aux.put("listaindicindic", listaindicindic);
		aux.put("matrizady", matrizady);
		aux.put("varleng", varleng);
		return new ModelAndView(getSuccessView(), "aux", aux);
	}

	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
}
