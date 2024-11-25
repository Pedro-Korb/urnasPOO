package UrnasFinalizado;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		//inserir o caminho do arquivo na variável PATH
		final String PATH = "";
		
		File votos = new File(PATH);
		Scanner input = new Scanner(votos);
		DadosDaEleicao de = new DadosDaEleicao();

		while (input.hasNextLine()) {
			String voto_numero = input.nextLine();
			de.adiciona(voto_numero);
		}

		de.calculaCandidatosEleitos();

		System.out.println("----------------------------------------------------------------");
		System.err.println("Quociente Eleitoral (QE)");
		System.out.println(de.calculaQoucienteEleitoral());

		System.err.println("\nQuociente Partidário (QP)");
		mostraQuocientePartidario(de);
		

		System.err.println("\nResumno das urnas");
		mostraResumoUrnas(de);

		System.err.println("\nVotos de cada candidato");
		mostraCandidatos(de);

		System.err.println("\nVotos brancos e nulos");
		mostraBrancoENulo(de);

		System.err.println("\nLista de candidatos eleitos: ");
		System.out.println(de.getListaEleitos());

		System.err.println("\nVotos de cada candidato eleito");
		mostraVotosEleitos(de);

		System.err.println("\nCadeiras por partido");
		mostraCadeirasPartidos(de);

		System.err.println("\nResumo dos partidos");
		mostraResumoPartidos(de);

		System.err.println("\nTotal eleitos");
		System.out.println(de.getListaEleitos().size());
		System.out.println("----------------------------------------------------------------");
		input.close();
	}

	static void mostraQuocientePartidario(DadosDaEleicao de) {
		for (String partido : de.getListaOrdenadaDePartidos()) {

			System.out.println(partido + " -> " + String.format("%.4f", de.calculaQuocientePartidario(partido)));

		}
	}

	static void mostraResumoUrnas(DadosDaEleicao de) {
		for (String urna : de.getListaDeUrnas()) {
			int votosTotais = de.getNumVotosUrna(urna);
			int i = 0;

			System.out.println("URNA " + urna);
			printaAmarelo("posicao\t\tcandidato\t\tvotos\t\tpercentual");

			for (String candidato : de.getListaOrdenadaDeCandidatosPorUrna(urna)) {
				int votosCandidato = de.getNumVotosCandidatoUrna(candidato, urna);
				if (candidato.length() > 1 && votosCandidato > 0) {
					i++;
					String percentual = String.format("%.2f", (votosCandidato * 1d / votosTotais) * 100);
					System.out.println(i + "\t\t" + candidato + "\t\t\t" + votosCandidato + "\t\t" + percentual);
				}
			}
			String percentualNulos = String.format("%.2f",
					(de.getNumVotosCandidatoUrna("0", urna) * 1d / votosTotais) * 100);

			String percentualBrancos = String.format("%.2f",
					(de.getNumVotosCandidatoUrna("1", urna) * 1d / votosTotais) * 100);

			System.out.println("\nBrancos\t" + " -> " + de.getNumVotosCandidatoUrna("1", urna) + " | percentual: "
					+ percentualBrancos);
			System.out.println(
					"Nulos\t" + " -> " + de.getNumVotosCandidatoUrna("0", urna) + " | percentual: " + percentualNulos);
			System.out.println("\n\n");
		}

	}

	static void mostraCandidatos(DadosDaEleicao de) {
		int votosTotais = de.getVotosTotais();
		printaAmarelo("posicao\tcandidato\tvotos\tpercentual\tstatus");
		int i = 0;
		for (String candidato : de.getListaOrdenadaDeCandidatos()) {
			int votosCandidato = de.getNumVotosCandidato(candidato);
			if (candidato.length() > 1) {
				i++;
				String percentual = String.format("%.2f", (votosCandidato * 1d / votosTotais) * 100);

				String txt = i + "\t" + candidato + "\t\t" + votosCandidato + "\t" + percentual;
				if (de.getListaEleitos().contains(candidato)) {
					txt += "\u001B[32m" + "\t\t\tELEITO" + "\u001B[0m";
				} else {
					txt += "\t\tNÃO ELEITO";
				}
				System.out.println(txt);
			}
		}
	}

	static void mostraBrancoENulo(DadosDaEleicao de) {
		System.out.println("Brancos" + " -> " + de.getNumVotosCandidato("1"));
		System.out.println("Nulos" + " -> " + de.getNumVotosCandidato("0"));
	}

	static void mostraVotosEleitos(DadosDaEleicao de) {
		for (String candidato : de.getListaEleitos()) {

			System.out.println(candidato + " -> " + de.getNumVotosCandidato(candidato));

		}
	}

	static void mostraCadeirasPartidos(DadosDaEleicao de) {
		HashMap<String, Integer> partidoCadeiras = de.getPartidoCadeiras();

		for (String partido : de.getListaOrdenadaDePartidos()) {

			System.out.println(partido + " -> " + partidoCadeiras.get(partido));

		}
	}

	static void mostraResumoPartidos(DadosDaEleicao de) {
		int votosTotais = de.getVotosTotais();
		printaAmarelo("posicao\t\tpartido\t\tvotos\t\tpercentual");
		int i = 0;
		for (String partido : de.getListaOrdenadaDePartidos()) {
			int votosPartido = de.getNumVotosPartido(partido);
			i++;
			String percentual = String.format("%.2f", (votosPartido * 1d / votosTotais) * 100);

			String txt = i + "\t\t" + partido + "\t\t" + votosPartido + "\t\t" + percentual;
			System.out.println(txt);
		}
	}

	static void printaAmarelo(String txt) {
		System.out.println("\u001B[33m" + txt + "\u001B[0m");
	}

}
