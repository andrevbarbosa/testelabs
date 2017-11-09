package br.com.phzero.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * Classe para efetuar indexação e pesquisa de dados em arquivos txt com dados de filmes.
 * 
 * @author andre barbosa
 *
 */
public class MovieSearch {
	private File data;

	public static void main(String[] args) {
		if (args.length==0) throw new IllegalArgumentException("Informe a expressão de busca.");
		
		MovieSearch m = new MovieSearch();
		try {
			HashMap<String, Set<String>> indice = m.indexar();
			TreeSet<String> resultado = m.pesquisar(args[0],  indice);
			
			if (resultado.size()==0) {
				System.out.printf("Não foram encontrados resultados para o termo \"%s\". \n", args[0]);
			}
			else {
				System.out.printf("Foram encontradas %s ocorrências pelo termo \"%s\".\n", resultado.size(), args[0]);  
				System.out.printf("Os arquivos que possuem \"%s\" são:\n", args[0]);
				
				resultado.forEach(s -> System.out.println("data/" + s));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Faz a indexação dos arquivos no diretório "data"
	 * 
	 * @return HashMap<String, Set<String>> Map com key = palavras e value = Set de nomes de arquivos
	 * @throws IOException
	 */
	public HashMap<String, Set<String>> indexar() throws IOException {
		if (data==null) {
			data = new File("data");
		}
		
		if (!data.isDirectory()) throw new IOException("Nao existe a pasta \"data\"");
		
		HashMap<String, Set<String>> indice = new HashMap<String, Set<String>>();
		String[] fileNames = data.list();

		for (String filename: fileNames) {
			File file = new File("./data/" + filename);
			BufferedReader b = new BufferedReader(new FileReader(file));
			String linha = b.readLine();
			if (linha==null) {
				b.close();
				continue;
			}
			
			do {
				String[] palavrasNaLinha = linha.split(" ");
				for (String novaPalavra:palavrasNaLinha) {
					novaPalavra = novaPalavra.toLowerCase();
					if (novaPalavra.trim().length()>0) {
						if (indice.containsKey(novaPalavra)) {
							indice.get(novaPalavra).add(filename);
						}
						else {
							Set<String> novoIndice = new HashSet<String>();
							novoIndice.add(filename);
							indice.put(novaPalavra, novoIndice);
						}
					}
				}
				linha = b.readLine();
			}
			while(linha!=null);
			b.close();
		}

		return indice;
	}
	
	
	/**
	 * Pesquisa no índice as ocorrências das palavras. 
	 * 
	 * @param param String com as palavras a serem pesquisadas, separadas por espaço
	 * @param indice Map com o índice das palavras x arquivos
	 * @return TreeSet<String> com os nomes dos arquivos onde ocorrem TODAS as palavras.
	 */
	public TreeSet<String> pesquisar(String param, HashMap<String, Set<String>> indice) {
		if (param == null || indice == null) throw new IllegalArgumentException("Parametros inválidos.");
		
		String[] palavras = param.split(" ");
		
		for (int i = 0; i<palavras.length; i++) {
			if (palavras[i].trim().length()==0) continue;
			
			palavras[i] = palavras[i].toLowerCase().trim();
		}
		
		TreeSet<String> resultado = new TreeSet<String>();
		for (int i = 0; i<palavras.length; i++) {
			if (indice.get(palavras[i])==null) {
				resultado = new TreeSet<String>();
				break;
			}
			
			if (i==0) 
				resultado.addAll(indice.get(palavras[i]));
			else
				resultado.retainAll(indice.get(palavras[i]));
		}
		
		return resultado;
	}
	
}












