package UrnasFinalizado;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class DadosComparator implements Comparator<String>{

	private HashMap<String, List<String>> dados;
	private boolean conferirPorUrna;
	private String urna;
	
	public DadosComparator(HashMap<String, List<String>> dados) {
		this.dados = dados;
		this.conferirPorUrna = false;
	}
	
	public DadosComparator(HashMap<String, List<String>> dados, String urna) {
		this.dados = dados;
		this.conferirPorUrna = true;
		this.urna = urna;
	}
	
	@Override
	public int compare(String o1, String o2) {
		
		int votos1, votos2;
		
		if (conferirPorUrna) {
			votos1 = Collections.frequency(dados.get(o1), urna);
			votos2 = Collections.frequency(dados.get(o2), urna);
		}
		else {
			votos1 = dados.get(o1).size();
			votos2 = dados.get(o2).size();
		}
		
		
		if (votos1 > votos2) {
			return -1;
		}
		else if (votos1 < votos2) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
