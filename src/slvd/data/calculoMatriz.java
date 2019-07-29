package slvd.data;

import java.util.List;

import slvd.dao.IndicadorHome;
import slvd.database.Asignatura;
import slvd.database.Indicador;
import slvd.database.IndicadorIndicador;
import slvd.database.MatrizAdyacenciaAsociada;
import slvd.database.VariableLenguistica;

public class calculoMatriz {
	
	private IndicadorHome dao;

	public void calculoMatrizAdy(Asignatura asig){
		List<Indicador> indicadores = dao.listarIndicxAsign(asig);
		float valorSuma = 0;
		float valorAbs = 0;

		for (Indicador indicadorE : indicadores) {
			for (Indicador indicadorS : indicadores) {
				int indicE = indicadorE.getIdIndicador();
				int indicS = indicadorS.getIdIndicador();
				if (indicE != indicS) {	
					dao.insertIndicIndic(indicadorE, indicadorS);
					IndicadorIndicador idIndicIndic = dao.obtenerIndInd(indicadorE, indicadorS);
					List<MatrizAdyacenciaAsociada> lista = dao.obtenerMatrizAdyacPor(idIndicIndic);
					int cantElem = lista.size();
					for (MatrizAdyacenciaAsociada listado : lista) {
						VariableLenguistica listvar = dao
								.obtenerVarLengPorId(listado.getVariableLenguistica().getIdVariableLenguistica());
						float var = listvar.getValorNumerico();
						valorSuma += var;
					}
					if(cantElem!=0)
						valorAbs = valorSuma / cantElem;
					dao.construirMatriz(idIndicIndic, valorAbs);
					valorSuma = 0;
					valorAbs = 0;
				}
			}
		}
	}
	
	public IndicadorHome getDao() {
		return dao;
	}

	public void setDao(IndicadorHome dao) {
		this.dao = dao;
	}
}
