package UrnasFinalizado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/*
 * A classe contém três HashMaps que relacionam um nome (de candidato, partido ou urna) a uma lista.
 * 
 * -> Um candidato tem seu nome relacionado com as urnas que votaram nele, p. ex.:
 *    "JOÃO" tem a lista de urnas ["1", "2", "1", "12", "20", "15", "20", ...]
 * 
 * -> Da mesma forma, um partido tem seu nome relacionado com as urnas que votaram nele, p. ex:
 *    O partido "22" tem a lista de urnas ["1", "1", "1", "4", "5", ...]
 *   
 * -> Uma urna tem seu nome relacionado com os candidatos que nela foram escolhidos, p. ex:
 *    A urna "1" tems a lista de candidatos [ "JOÃO", "MARIA", "JOÃO", "CLEYTON", ...]
 * 
 * 
 * ----------------------------------------------------------------------------------------------------------------------------
 * Após fazer a leitura de todos os dados no documento txt, o método calculaCandidatosEleitos() deve ser chamado na Main.
 * Nesse método, a listaEleitos é preenchida com os 10 eleitos e o HashMap partidoCadeiras, contendo a relação partido-cadeirasObtidas, é gerado.
 * 
 * 
 */

public class DadosDaEleicao {
	private HashMap<String, List<String>> candidatos;
	private HashMap<String, List<String>> urnas;
	private HashMap<String, List<String>> partidos;
	
	private List<String> listaEleitos;
	HashMap<String, Integer> partidoCadeiras;

/////////////////////////////////////////CONSTRUTOR/////////////////////////////////////////
	public DadosDaEleicao() {
		candidatos = new HashMap<String, List<String>>();
		urnas = new HashMap<String, List<String>>();
		partidos = new HashMap<String, List<String>>();
		listaEleitos = new ArrayList<String>();
	}

/////////////////////////////////////////ENTRADA DE DADOS/////////////////////////////////////////
	public void adiciona(String votoNumero) {
		String separacao = "[;]";
		String[] listaVoto = votoNumero.split(separacao);
		String urna = listaVoto[0];
		String candidato = listaVoto[1];

		if (candidatos.containsKey(candidato)) {
			candidatos.get(candidato).add(urna);
		} else {
			List<String> listaInicial = new ArrayList<String>();
			listaInicial.add(urna);
			candidatos.put(candidato, listaInicial);
		}

		if (urnas.containsKey(urna)) {
			urnas.get(urna).add(candidato);
		} else {
			List<String> listaInicial = new ArrayList<String>();
			listaInicial.add(candidato);
			urnas.put(urna, listaInicial);
		}

		if (!candidato.equals("0") && !candidato.equals("1")) {
			String partido = candidato.substring(0, 2);

			if (partidos.containsKey(partido)) {
				partidos.get(partido).add(urna);
			} else {
				List<String> listaInicial = new ArrayList<String>();
				listaInicial.add(urna);
				partidos.put(partido, listaInicial);
			}
		}

	}
	
	

/////////////////////////////////////////GETS DE INTEIROS/////////////////////////////////////////
	public int getNumVotosCandidato(String candidato) {
		return candidatos.get(candidato).size();
	}

	public int getNumVotosCandidatoUrna(String candidato, String urna) {
		return Collections.frequency(candidatos.get(candidato), urna);
	}

	public int getNumVotosPartido(String partido) {
		return partidos.get(partido).size();
	}

	public int getNumVotosPartidoUrna(String partido, String urna) {
		return Collections.frequency(partidos.get(partido), urna);
	}

	public int getNumVotosUrna(String urna) {
		return urnas.get(urna).size();
	}

	public int getVotosTotais() {
		int contador = 0;
		for (HashMap.Entry<String, List<String>> d : candidatos.entrySet()) {
			List<String> val = d.getValue();
			contador += val.size();
		}
		return contador;
	}
	
	

/////////////////////////////////////////GETS DE LISTAS/////////////////////////////////////////
	public List<String> getListaOrdenadaDeCandidatos() {
		// retorna uma lista contendo os nomes dos candidatos, em ordem decrescente de
		// votos
		List<String> lista = new ArrayList<String>();
		lista.addAll(candidatos.keySet());

		lista.sort(new DadosComparator(candidatos));
		return lista;
	}

	public List<String> getListaOrdenadaDePartidos() {
		// retorna uma lista contendo os nomes dos partidos, em ordem decrescente de
		// votos
		List<String> lista = new ArrayList<String>();
		lista.addAll(partidos.keySet());

		lista.sort(new DadosComparator(partidos));
		return lista;
	}

	public List<String> getListaOrdenadaDeCandidatosPorUrna(String urna) {
		// retorna uma lista contendo os nomes dos candidatos, pela ordem decrescente de
		// votos da urna especificada
		List<String> lista = new ArrayList<String>();
		lista.addAll(candidatos.keySet());

		lista.sort(new DadosComparator(candidatos, urna));
		return lista;
	}
	
	//"351235"
	//

	public List<String> getListaOrdenadaDePartidosPorUrna(String urna) {
		// retorna uma lista contendo os nomes dos partidos, pela ordem decrescente de
		// votos da urna especificada
		List<String> lista = new ArrayList<String>();
		lista.addAll(partidos.keySet());

		lista.sort(new DadosComparator(partidos, urna));
		return lista;
	}

	public List<String> getListaDeUrnas() {
		// retorna a lista de urnas em ordem crescente
		List<String> lista = new ArrayList<String>();
		lista.addAll(urnas.keySet());

		List<Integer> listaInteiros = lista.stream().map(Integer::valueOf).collect(Collectors.toList());

		Collections.sort(listaInteiros);

		return listaInteiros.stream().map(p -> p + "").collect(Collectors.toList());

	}
	
	public List<String> getListaEleitos(){
		return listaEleitos;
	}
	
	public HashMap<String, Integer> getPartidoCadeiras(){
		return partidoCadeiras;
	}
	
	
/////////////////////////////////////////OPERAÇÕES/////////////////////////////////////////
	public double calculaQoucienteEleitoral() {
		int votosTotais = getVotosTotais();
		double votosValidos = votosTotais - getNumVotosCandidato("0") - getNumVotosCandidato("1");
		return votosValidos / 10d;
	}

	public double calculaQuocientePartidario(String partido) {
		double votosPartido = getNumVotosPartido(partido);
		return (votosPartido / calculaQoucienteEleitoral());
	}

	public boolean passouClausulaDeDesempenhoIndividual(String candidato) {
		double votosCandidato = getNumVotosCandidato(candidato);
		if (votosCandidato / calculaQoucienteEleitoral() >= 0.1d) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public HashMap<String, List<String>> pegaRelacaoPartidoCandidatos(){
		List<String> partidos = getListaOrdenadaDePartidos();
		List<String> candidatos = getListaOrdenadaDeCandidatos();
		
		HashMap<String, List<String>> partidoCandidatos = new HashMap<String, List<String>>();
		
		for(String partido: partidos) {
			List<String> listaCandidatos = new ArrayList<String>();
			
			
			for (String candidato: candidatos) {
				if (candidato.startsWith(partido)) {
					listaCandidatos.add(candidato);
				}
			}
			
			partidoCandidatos.put(partido, listaCandidatos);
		}
		
		return partidoCandidatos;
	}
	
	
	// método que relaciona o partido com o total de cadeiras que ele ocupa
	// retorna um hashmap simples com estes valores
	public HashMap<String, Integer> relacaoPartidoNumeroCadeiras()
	{
		List<String> partidos = getListaOrdenadaDePartidos();
		HashMap<String, Integer> partidoNumeroEleitos = new HashMap<String, Integer>();

		
		for (String partido : partidos) {
			int numeroCadeiras;
				
			int eleitos = (int) calculaQuocientePartidario(partido);
			numeroCadeiras = eleitos;
			partidoNumeroEleitos.put(partido, numeroCadeiras);
		}
		
		return partidoNumeroEleitos;
	}
	
	public void calculaCandidatosEleitos() {
		listaEleitos = new ArrayList<String>();
		HashMap<String, List<String>> partidosCandidato = pegaRelacaoPartidoCandidatos();
		List<String> partidos = getListaOrdenadaDePartidos();
		
		for (String partido : partidos) {

			
			int numEleitos = (int) calculaQuocientePartidario(partido);
			
			while(numEleitos > 0) {
				
				String candidatosMaisVotado = partidosCandidato.get(partido).get(0);
				listaEleitos.add(candidatosMaisVotado);
				partidosCandidato.get(partido).remove(0);
				numEleitos--;
			}
		}
		
	
		partidoCadeiras = relacaoPartidoNumeroCadeiras();	
		ArrayList<Double> divisaoVotosPorVagaMaisUm = new ArrayList<Double>();
		
		while (listaEleitos.size() < 10)
		{
			// nesse array ele armazena todas as divisões de todos os partidos da conta -> votos do partido / numero de vagas por partido mais 1
			// vai limpar o array toda vez que ocorrer o for
			divisaoVotosPorVagaMaisUm.clear();
			
			// vai passar por todos o partidos para coletar seu tota de votos
			for (String partido : partidos) {
				
				double numeroVagasPorPartidoMaisUm = (partidoCadeiras.get(partido) + 1);
				double votosPartido = getNumVotosPartido(partido);
				
				// vai dividir o total de votos do partido pelo total de vagas que ele ocupa + 1
				double divisaoVotosVagaMaisUm = votosPartido / numeroVagasPorPartidoMaisUm;
				divisaoVotosPorVagaMaisUm.add(divisaoVotosVagaMaisUm);
			}
			// pega o maior valor do array : "divisaoVotosPorVagaMaisUm"
			double maiorNumeroVotos = Collections.max(divisaoVotosPorVagaMaisUm);
			
			// nesse foreach vai realizar o mesmo processo do foreach de cima
			// para poder achar qual partido se relaciona com o maior valor encontrado no array : "divisaoVotosPorVagaMaisUm"
			// o partido que tiver o maior valor da divisao feita acima, vai receber mais uma cadeira
			for (String partido : partidos) {
				
				double numeroVagasPorPartidoMaisUm = (partidoCadeiras.get(partido) + 1);
				double votosPartido = getNumVotosPartido(partido);
				
				double divisaoVotosVagaMaisUm = votosPartido / numeroVagasPorPartidoMaisUm;
				
				// entra quando achar o partido que se relaciona com a maior valor da divisao feita no outro foreach
				if (divisaoVotosVagaMaisUm == maiorNumeroVotos)
				{
					// teu codigo ai, essas 3 linhas de baixo
					
					// quando acha o valor igual ao maior valor, ele vai pegar o numero completo do candidato
					// este candidato vai ser automaticamente o primeiro da lista de candidatos, porque quando
					// ele é adicionado, ele é removido na outra linha
					String candidatosMaisVotado = partidosCandidato.get(partido).get(0);
					listaEleitos.add(candidatosMaisVotado);
					partidosCandidato.get(partido).remove(0);
					
					// pega o valor do numero atual de cadeiras do partido e adiciona + 1
					// nao consegui fazer em uma linha, entao criei esta variavel para repor o valor dentro do hashmap 
					int novoNumeroCadeiras = (partidoCadeiras.get(partido)+1);
					partidoCadeiras.put(partido, novoNumeroCadeiras);
				}
			}
		}

	}
}

