package br.com.phzero.search;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@FixMethodOrder(MethodSorters.JVM)
@RunWith(PowerMockRunner.class)
@PrepareForTest(MovieSearch.class)
public class MovieSearchTest {

	private HashMap<String, Set<String>> indice;
	
	@Mock
	private File data;
	
	@Mock
	private FileReader fileReader;
	
	@Mock
	private BufferedReader bufferedReader;


    @InjectMocks
    private MovieSearch search;
	
	@Before
	public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        indice = new HashMap<String, Set<String>>();
        
        Set<String> set1 = new HashSet<String>();
        set1.add("arquivo_1.txt");

        Set<String> set2 = new HashSet<String>();
        set2.add("arquivo_2.txt");
        
        Set<String> set3 = new HashSet<String>();
        set3.add("arquivo_3.txt");

        Set<String> set12 = new HashSet<String>();
        set12.add("arquivo_1.txt");
        set12.add("arquivo_2.txt");
        
        Set<String> set23 = new HashSet<String>();
        set23.add("arquivo_2.txt");
        set23.add("arquivo_3.txt");

        Set<String> set123 = new HashSet<String>();
        set123.add("arquivo_1.txt");
        set123.add("arquivo_2.txt");
        set123.add("arquivo_3.txt");
  
        
        indice.put("chave1", set1);
        indice.put("chave2", set2);
        indice.put("chave3", set3);
        indice.put("chave4", set12);
        indice.put("chave5", set23);
        indice.put("chave6", set123);
        indice.put("chave7", set1);
        indice.put("chave8", set2);
        indice.put("chave9", set3);
        indice.put("chave10", set1);
        indice.put("chave11", set23);
        indice.put("chave12", set12);
	}

	@Test
	public void indexar_DataDirectoryNotExist_ShouldThrowException() {
		when(data.isDirectory()).thenReturn(false);
		
		try {
			search.indexar();
			fail();
		} catch (IOException e) {
			assertTrue("Nao existe a pasta \"data\"".equals(e.getMessage()));;
		}
	}

	@Test
	public void indexar_DataDirectoryExist_ShouldReturnIndice() throws Exception {
		String[] dataList = new String[]{"little-manhattan.txt", "little-red-monkey.txt"};
		String linha1 = "little manhattan 2005 josh hutcherson charlie ray bradley whitford mark levin jennifer flackett gavin polone anderson";
		String linha2 = "little red monkey 1955 richard conte rona anderson russell napier ken hughes james eastwood ken hughes";
		
		when(data.isDirectory()).thenReturn(true);
		when(data.list()).thenReturn(dataList);
        PowerMockito.whenNew(FileReader.class).withAnyArguments().thenReturn(fileReader); 
        PowerMockito.whenNew(BufferedReader.class).withAnyArguments().thenReturn(bufferedReader);
        when (bufferedReader.readLine()).thenReturn(linha1).thenReturn(null).thenReturn(linha2).thenReturn(null);
		
        HashMap<String, Set<String>> indexado = search.indexar();
        
        // 28 palavras no índice
        assertTrue(indexado.size()==28); 
        // uma palavra indexada no arquivo "little-manhattan.txt"
        assertTrue(indexado.get("charlie").stream().filter(x -> x.equals("little-manhattan.txt")).findFirst().isPresent());
        // uma palavra indexada no arquivo "little-red-monkey.txt"
        assertTrue(indexado.get("conte").stream().filter(x -> x.equals("little-red-monkey.txt")).findFirst().isPresent());
        // uma palavra indexada nos dois arquivos
        assertTrue(indexado.get("anderson").stream().filter(x -> x.equals("little-manhattan.txt")).findFirst().isPresent());
        assertTrue(indexado.get("anderson").stream().filter(x -> x.equals("little-red-monkey.txt")).findFirst().isPresent());
        // uma palavra que NÃO está indexada no arquivo "little-manhattan.txt"
        assertFalse(indexado.get("conte").stream().filter(x -> x.equals("little-manhattan.txt")).findFirst().isPresent());
        // uma palavra que NÃO está indexada no arquivo "little-red-monkey.txt"
        assertFalse(indexado.get("hutcherson").stream().filter(x -> x.equals("little-red-monkey.txt")).findFirst().isPresent());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void pesquisar_ChaveNula_RetornaErro() {
		search.pesquisar(null, indice);
	}

	@Test(expected=IllegalArgumentException.class)
	public void pesquisar_IndiceNulo_RetornaErro() {
		search.pesquisar("chave", null);
	}
	
	@Test
	public void pesquisar_NenhumDadoEncontrado_RetornaZero() {
		TreeSet<String> resultado = search.pesquisar("chaveN", indice);
		
		assertTrue(resultado.size()==0);
	}

	@Test
	public void pesquisar_UmArquivoEncontrado_RetornaArquivo() {
		TreeSet<String> resultado = search.pesquisar("chave3", indice);
		
		assertTrue(resultado.size()==1);
	}

	@Test
	public void pesquisar_VariasChaves_RetornaArquivos() {
		TreeSet<String> resultado = search.pesquisar("chave5 chave11", indice);
		
		assertTrue(resultado.size()==2);
	}
	
	@Test
	public void pesquisar_VariasChaves_RetornaZero() {
		TreeSet<String> resultado = search.pesquisar("chave5 chave11 chave1", indice);
		
		assertTrue(resultado.size()==0);
	}
	
	@Test
	public void pesquisar_ChaveUnica_RetornaVariosArquivos() {
		TreeSet<String> resultado = search.pesquisar("chave6", indice);
		
		assertTrue(resultado.size()==3);
	}

}











